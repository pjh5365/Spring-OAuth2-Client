package pjh5365.springoauth2client.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pjh5365.springoauth2client.auth.domain.entity.UserAccount;

/**
 * 회원 Repository
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	Optional<UserAccount> findByUsername(String username);
}
