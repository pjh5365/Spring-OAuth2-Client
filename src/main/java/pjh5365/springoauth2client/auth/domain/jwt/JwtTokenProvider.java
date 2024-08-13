package pjh5365.springoauth2client.auth.domain.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pjh5365.springoauth2client.auth.domain.jwt.dto.JwtTokenResponse;

/**
 * JWT 토큰을 관리하는 객체
 *
 * @author : parkjihyeok
 * @since : 2024/08/13
 */
@Component
public class JwtTokenProvider {

	private final String accessSecretKey;
	private final String refreshSecretKey;
	private final String issuer;

	private final long accessTokenValidTime = 10 * 60 * 1000L;    // 액세스 토큰의 유효기간은 10분
	private final long refreshTokenValidTime = 10 * 24 * 60 * 60 * 1000L;    // 리프레시 토큰의 유효기간은 10일

	public JwtTokenProvider(
			@Value("${JWT.ACCESS_SECRET_KEY}") String accessSecretKey,
			@Value("${JWT.REFRESH_SECRET_KEY}") String refreshSecretKey,
			@Value("${JWT.ISSUER}") String issuer) {

		this.accessSecretKey = accessSecretKey;
		this.refreshSecretKey = refreshSecretKey;
		this.issuer = issuer;
	}

	/**
	 * Access 토큰을 생성하는 메서드
	 *
	 * @param username 사용자의 username 를 전달받아 jwt 토큰을 생성
	 * @return 생성된 Access 토큰
	 */
	public String createAccessToken(String username) {
		Date now = new Date();

		return Jwts.builder()
				.setSubject(username)
				.setIssuer(issuer)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + accessTokenValidTime))
				.signWith(SignatureAlgorithm.HS256, accessSecretKey)
				.compact();
	}

	/**
	 * Refresh 토큰을 생성하는 메서드, AccessToken 과 다른 비밀번호로 암호화
	 *
	 * @param username 사용자의 username 를 전달받아 jwt 토큰을 생성
	 * @return 생성된 Refresh 토큰
	 */
	public String createRefreshToken(String username) {
		Date now = new Date();

		return Jwts.builder()
				.setSubject(username)
				.setIssuer(issuer)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + refreshTokenValidTime))
				.signWith(SignatureAlgorithm.HS256, refreshSecretKey)
				.compact();
	}

	/**
	 * RefreshToken 에서 사용자 정보를 가져오는 메서드
	 *
	 * @param refreshToken 토큰을 입력받으면
	 * @return username
	 */
	public String getUserPkFromRefreshToken(String refreshToken) {
		return Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken).getBody().getSubject();
	}
}
