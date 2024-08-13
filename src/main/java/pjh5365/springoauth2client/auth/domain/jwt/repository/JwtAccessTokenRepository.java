package pjh5365.springoauth2client.auth.domain.jwt.repository;

import org.springframework.data.repository.CrudRepository;

import pjh5365.springoauth2client.auth.domain.jwt.entity.JwtAccessToken;

/**
 * 액세스 토큰 Repository
 *
 * @author : parkjihyeok
 * @since : 2024/08/13
 */
public interface JwtAccessTokenRepository extends CrudRepository<JwtAccessToken, String> {
}
