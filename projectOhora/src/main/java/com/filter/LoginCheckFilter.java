package com.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 순서는 아직 안정함
// 테스트로 iscart 넣어놈 / 몇개 넣어도 된다.
@WebFilter(
			dispatcherTypes = {DispatcherType.REQUEST}
			, urlPatterns = {
					""
			}
		)
public class LoginCheckFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("loginCheckFilter test");
		
		// 로그인 체크 필터
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		String contextPath = httpRequest.getContextPath();
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if (userId == null) {
			// 로그인이 안될 시 loginCheck=fail 을 달고 로그인 페이지로 이동한다.
			httpResponse.sendRedirect( contextPath + "/ohora/login.jsp?loginCheck=fail" );
			return;
		} // if
		
		chain.doFilter(request, response);
		
	}

}
