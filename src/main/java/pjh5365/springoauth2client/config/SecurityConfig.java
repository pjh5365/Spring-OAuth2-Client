package pjh5365.springoauth2client.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import pjh5365.springoauth2client.auth.domain.handler.LoginSuccessHandler;
import pjh5365.springoauth2client.auth.domain.jwt.filter.JwtFilter;
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
	private final LoginSuccessHandler loginSuccessHandler;
	private final JwtFilter jwtFilter;

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
// TODO: 2024/08/13 로그아웃 로직추가하기
		http
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.oauth2Login(auth -> auth
				.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2Service))
				.successHandler(loginSuccessHandler));

		return http.build();
	}
}
