package pjh5365.springoauth2client.auth.domain.jwt.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import pjh5365.springoauth2client.auth.domain.CustomUserDetails;
import pjh5365.springoauth2client.auth.domain.entity.UserAccount;
import pjh5365.springoauth2client.auth.domain.jwt.JwtTokenProvider;
import pjh5365.springoauth2client.auth.domain.jwt.entity.JwtAccessToken;
import pjh5365.springoauth2client.auth.domain.jwt.entity.JwtRefreshToken;
import pjh5365.springoauth2client.auth.domain.jwt.repository.JwtAccessTokenRepository;
import pjh5365.springoauth2client.auth.domain.jwt.repository.JwtRefreshTokenRepository;
import pjh5365.springoauth2client.auth.repository.UserAccountRepository;

/**
 * JWT 토큰 필터
 *
 * @author : parkjihyeok
 * @since : 2024/08/13
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final UserAccountRepository userAccountRepository;
	private final JwtAccessTokenRepository jwtAccessTokenRepository;
	private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 쿠키에 토큰이 있는지 검사
		Cookie[] cookies = request.getCookies();
		String username = null;
		String authorization = null;
		String cookieRefreshToken = null;
		if (cookies == null) { // 쿠키가 없다면 넘기기
			chain.doFilter(request, response);
			return;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("username")) {
				username = cookie.getValue();
			} else if (cookie.getName().equals("Authorization")) {
				authorization = cookie.getValue();
			} else if (cookie.getName().equals("RefreshToken")) {
				cookieRefreshToken = cookie.getValue();
			}
		}
		if (authorization == null || username == null) {
			chain.doFilter(request, response);
			return;
		}

		Optional<JwtAccessToken> accessToken = jwtAccessTokenRepository.findById(username);

		// 요청한 사용자정보로 redis 에 accessToken 이 존재할 때
		if (accessToken.isPresent()) {
			// 토큰에서 사용자 아이디 가져오기
			String tokenUsername = accessToken.get().getUsername();

			// 전달받은 사용자 id 와 토큰에서 꺼낸 사용자 id 가 같고 요청한 엑세스토큰이 redis 에 저장된 값과 일치할 때
			if (username.equals(tokenUsername) && authorization.equals(accessToken.get().getAccessToken())) {
				UserAccount userAccount = userAccountRepository.findByUsername(username) // 해당 사용자 id 로 사용자 정보가 있는지 찾기
						.orElseThrow(() -> {
							// 로그인에 성공한 사람들(사용자 정보가 있는 경우)만 토큰을 부여받기 때문에 예외를 던진다면 데이터베이스 오류이거나 로그인 로직에 버그가 있는 것임.
							return new UsernameNotFoundException("해당 사용자 정보를 찾을 수 없습니다. [DB OR 로그인 로직 버그]");
						});

				CustomUserDetails userDetails = new CustomUserDetails(userAccount);
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
				chain.doFilter(request, response);
			}
		}

		if (cookieRefreshToken != null) {    // 리프레시 토큰이 전달되었다면
			Optional<JwtRefreshToken> jwtRefreshToken = jwtRefreshTokenRepository.findById(username);
			if (jwtRefreshToken.isPresent()) { // Redis에 리프레시 토큰이 존재한다면
				JwtRefreshToken refreshToken = jwtRefreshToken.get();
				// Redis의 토큰의 username과 쿠키의 username이 같은지 검사하고, redis의 토큰과 쿠키의 토큰이 같은지 확인한다.
				if (refreshToken.getUsername().equals(username) && refreshToken.getRefreshToken().equals(cookieRefreshToken)) {
					JwtAccessToken newAccessToken = new JwtAccessToken(username, jwtTokenProvider.createAccessToken(username));
					jwtAccessTokenRepository.save(newAccessToken);
					response.addCookie(createCookie("Authorization", newAccessToken.getAccessToken()));
				}
			}
		}
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(10 * 60);
		// cookie.setSecure(true); HTTPS 에서만 사용
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		return cookie;
	}
}
