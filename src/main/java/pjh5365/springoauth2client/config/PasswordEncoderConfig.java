package pjh5365.springoauth2client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 순환참조를 없애기 위한 인코더 분리
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
@Configuration
public class PasswordEncoderConfig {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
