package pjh5365.springoauth2client.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pjh5365.springoauth2client.auth.domain.CustomUserDetails;
import pjh5365.springoauth2client.auth.domain.UserRole;
import pjh5365.springoauth2client.auth.domain.dto.KakaoResponse;
import pjh5365.springoauth2client.auth.domain.dto.OAuth2Response;
import pjh5365.springoauth2client.auth.domain.entity.UserAccount;
import pjh5365.springoauth2client.auth.repository.UserAccountRepository;

/**
 * OAuth2 클라이언트 서비스
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {

	private final UserAccountRepository userAccountRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);
		OAuth2Response response;
		if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			response = new KakaoResponse(oauth2User.getAttributes());
		} else {
			throw new IllegalArgumentException("사용할  수 없는 인증방법입니다.");
		}

		String provider = response.getProvider();
		String providerId = response.getProviderId();
		String username = provider + "_" + providerId; //중복이 발생하지 않도록 provider와 providerId를 조합
		// String email = response.getEmail();

		Optional<UserAccount> byUsername = userAccountRepository.findByUsername(username);
		UserAccount userAccount = null;
		if (byUsername.isEmpty()) {
			 userAccount = UserAccount.builder()
					.username(username)
					// .email(email)
					.password(bCryptPasswordEncoder.encode(username + "kakao"))
					.role(UserRole.ROLE_USER)
					.build();

			userAccountRepository.save(userAccount);
		} else {
			userAccount = byUsername.get();
		}

		return new CustomUserDetails(userAccount);
	}
}
