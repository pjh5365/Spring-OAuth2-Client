package pjh5365.springoauth2client.auth.domain.dto;

import java.util.Map;

/**
 * 카카오 로그인에 성공하면 카카오가 반환하는 객체
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
public class KakaoResponse implements OAuth2Response {

	private final Map<String, Object> attribute;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getEmail() {
		return attribute.get("email").toString();
	}

	@Override
	public String getName() {
		return attribute.get("nickname").toString();
	}
}
