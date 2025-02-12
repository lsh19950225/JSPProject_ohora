package kr.ohora.www.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.ohora.www.domain.UserDTO;
import kr.ohora.www.service.UserService;
import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequestMapping("/ajax/*")
public class UserRESTController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	//  <bean id="bCryptPasswordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"></bean>

	// 중복 체크 
	@GetMapping(value = "/jungbokCK.ajax")
	public Integer jungbokCK(@RequestParam("msgTag") String msgTag, 
			@RequestParam("val") String value) throws SQLException {
		log.info("중복 체크 요청 받음");

		UserDTO dto = new UserDTO();

		if ( msgTag.equals("idMsg") ) {
			dto.setUserLoginId(value);
		} else if ( msgTag.equals("emailMsg")) {
			dto.setUserEmail(value);
		} else { //폰
			dto.setUserTel(value);
		}
		try {
			// 중복 체크 메서드 호출
			Integer isDuplicated = this.userService.jungbokCK(dto);
			log.info("> 중복 체크 결과: " + isDuplicated);
			return isDuplicated;
		} catch (SQLException e) {
			log.error("Database error 발생", e);
		}

		return 1; // // 예외 발생 시 중복으로 처리

	}

	// 인증번호 발급
	@GetMapping(value = "/getCode.ajax")
	public Integer getCode(HttpSession session) {
	    try {
	        // 랜덤 숫자 생성
	        Random random = new Random();
	        int numberCode = 10000 + random.nextInt(90000); 

	        String authCode = String.valueOf(numberCode);

	        log.info("사용자에게 발송할 인증번호: " + authCode);

	        // BCryptPasswordEncoder를 이용한 해시화
	        String encryptedAuthCode = passwordEncoder.encode(authCode);

	        // 인증번호와 생성 시간을 세션에 저장
	        long timestamp = System.currentTimeMillis();
	        session.setAttribute("authCode", encryptedAuthCode);
	        session.setAttribute("authCodeTimestamp", timestamp);

	        log.info("인증번호는 해시화된 상태로 세션에 저장됨: " + encryptedAuthCode);

	        return 1;  // 발급 성공 메시지
	    } catch (Exception e) {
	        log.error("인증번호 발송 중 오류 발생: ", e);
	        return 0;  // 발급  실패 메시지
	    }
	}



	// 인증번호 확인
	@GetMapping(value = "/checkCode.ajax")
	public Integer checkCode(@RequestParam("myCode") String myCode, HttpSession session) {
		
		// 세션에서 해시화된 인증번호 및 생성 시간 조회
		String authCode = (String) session.getAttribute("authCode");
		Long timestamp = (Long) session.getAttribute("authCodeTimestamp");

		if (authCode == null || timestamp == null) {
			log.info("인증번호가 세션에 없습니다.");
			return 0;  // 인증번호가 없으면 실패
		}

		// 인증번호 생성 후 5분이 지나면 만료 처리
		long currentTime = System.currentTimeMillis();
		if (currentTime - timestamp > 3 * 60 * 1000) {  // 3분 이상 경과
			log.info("인증번호가 만료되었습니다.");
			session.removeAttribute("authCode");  // 만료된 인증번호 제거
			session.removeAttribute("authCodeTimestamp");  // 만료된 생성 시간 제거
			return 0;  // 인증 실패
		}

		log.info("웹에서 입력한 인증번호: " + myCode);
		log.info("세션에 저장된 해시화된 인증번호: " + authCode);

		// 웹에서 입력된 값과 해시화되어 세션에 저장되어 있던 인증번호 비교
		boolean isMatch = passwordEncoder.matches(myCode, authCode);

		if (isMatch) {
			log.info("인증 성공");
			// 인증 후 세션에서 인증번호와 생성 시간 삭제
			session.removeAttribute("authCode");
			session.removeAttribute("authCodeTimestamp");
			return 1;  // 인증 성공
		} else {
			log.info("인증 실패");
			return 0;  // 인증 실패
		}
		
	}

}
