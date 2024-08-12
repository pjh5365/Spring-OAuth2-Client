package pjh5365.springoauth2client.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import pjh5365.springoauth2client.auth.service.CustomOAuth2Service;

/**
 * 스프링 시큐리티 설정파일
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2Service customOAuth2Service;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/").permitAll()
						// h2 허용
						.requestMatchers(PathRequest.toH2Console()).permitAll()
						.anyRequest().authenticated())
				// h2 허용
				.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

		// TODO: 2024/08/12 로그인 성공 핸들러 추가하기, JWT 토큰 필터 추가하기, JWT 로직 추가하기
		http
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable);
		// 로그인 성공핸들러 추가하고 JWT 토큰을 발행하면 세션은 사용하지 않도록 설정하기
		// .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.oauth2Login(auth -> auth
				.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2Service)));

		return http.build();
	}
}
