package org.sist.projectOJM.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 설정 파일을 나타내는 어노테이션
@EnableWebSecurity 
// 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 어노테이션
// 						  -> SecurityFilterChain 필터가 적용!
// 스프링 시큐리티를 활성화하는 역할


@EnableMethodSecurity(prePostEnabled = true) // 이 어노테이션을 동작할 수 있는 스프링 시큐리티 설정 @PreAuthorize("isAuthenticated()")
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
			
			// 403 접근 권한 없음 오류 해결
			.csrf((csrf) -> csrf
				.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
			
			// 권한은 있으나 화면이 블락되어 들어가지지 않을 때
			.headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
		
		    //
			.formLogin((formLogin) -> formLogin
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/"))
		
			// 로그아웃시 세션 제거
			.logout((logout) -> logout
	                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
	                .logoutSuccessUrl("/")
	                .invalidateHttpSession(true));
		
		return http.build();
		
	}
	
	// 해시화 빈객체 생성
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	// AuthenticationManager 빈 객체 생성
	// 스프링 시큐리티의 인증 처리하는 담당자.
	// UserSecurityService와 PasswordEncoder 자동 내부적으로 사용된다.
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	
	
}
