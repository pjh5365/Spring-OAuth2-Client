package pjh5365.springoauth2client.auth.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;
import pjh5365.springoauth2client.auth.domain.entity.UserAccount;

/**
 * 사용자의 정보를 담는 클래스
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
@RequiredArgsConstructor
public class CustomUserDetails implements OAuth2User, UserDetails {

	private final UserAccount userAccount;

	@Override
	public String getPassword() {
		return userAccount.getPassword();
	}

	@Override
	public String getUsername() {
		return userAccount.getUsername();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add((GrantedAuthority)() -> userAccount.getRole().toString());
		return collection;
	}

	@Override
	public String getName() {
		return userAccount.getName();
	}
}
