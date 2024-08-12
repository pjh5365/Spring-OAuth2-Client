package pjh5365.springoauth2client.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 권한 확인용 컨트롤러
 *
 * @author : parkjihyeok
 * @since : 2024/08/12
 */
@RestController
public class MainController {

	@GetMapping("/")
	public String mainApi() {
		return "누구나 접근가능한 API입니다.";
	}

	@GetMapping("/test")
	public String authApi() {
		return "권한이 필요한 API입니다.";
	}
}
