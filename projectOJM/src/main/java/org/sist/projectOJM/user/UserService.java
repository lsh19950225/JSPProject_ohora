package org.sist.projectOJM.user;

import java.util.Optional;

import org.sist.projectOJM.exception.DataNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public SiteUser create(String userLoginId, String userName, String userEmail, String userPassword1) {
		SiteUser user = new SiteUser(); // 엔터티 객체 생성
		user.setUserLoginId(userLoginId);
		user.setUserName(userName);
		user.setUserEmail(userEmail);
		user.setUserPassword1(userPassword1);
		
		// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setUserPassword1(passwordEncoder.encode(userPassword1));
			
		return this.userRepository.save(user);
		// return user;
	}

	public SiteUser getUser(String userLoginId) {
		Optional<SiteUser> siteUser = this.userRepository.findByUserLoginId(userLoginId);
		if (siteUser.isPresent()) {
			return siteUser.get(); // SiteUser
		} else {
			throw new DataNotFoundException("siteUser not found");
		}
		
	}
	
} // class
