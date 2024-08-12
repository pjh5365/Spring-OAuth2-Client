package pjh5365.springoauth2client.auth.domain;

import lombok.Getter;

/**
 * 회원 권한
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
@Getter
public enum UserRole {
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_USER("ROLE_USER");

	private final String role;

	UserRole(String role) {
		this.role = role;
	}
}
