package pjh5365.springoauth2client.auth.domain.dto;

/**
 * OAuth2 로그인 응답
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
public interface OAuth2Response {

	String getProvider();

	String getProviderId();

	String getEmail();

	String getName();
}
