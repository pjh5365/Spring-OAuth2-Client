package pjh5365.springoauth2client.auth.domain.jwt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 액세스 토큰
 *
 * @author : parkjihyeok
 * @since : 2024/08/13
 */
@Getter
@RedisHash(value = "JwtAccessToken", timeToLive = 60 * 10) // 액세스 토큰의 유효기간은 10분
@RequiredArgsConstructor
public class JwtAccessToken {

	@Id
	private final String username;
	private final String accessToken;
}
