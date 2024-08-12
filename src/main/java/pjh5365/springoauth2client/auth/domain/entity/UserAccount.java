package pjh5365.springoauth2client.auth.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pjh5365.springoauth2client.auth.domain.UserRole;

/**
 * 회원 Entity
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

	@Id
	@Column(name = "user_account_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String name;
	private String email;
	private String password;

	@Enumerated(EnumType.STRING)    // enum 을 데이터베이스에 문자열로 저장한다.
	private UserRole role;
}
