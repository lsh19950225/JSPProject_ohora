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
// 테스트로 offcart 넣어놈 / 몇개 넣어도 된다.
@WebFilter(
			dispatcherTypes = {DispatcherType.REQUEST}
			, urlPatterns = {
					""
			}
		)
public class AdminCheckFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("adminCheckFilter test");
		
		// 관리자 체크 필터
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		// 이전 페이지 저장
		String referer = httpRequest.getHeader("Referer");
		
		String contextPath = httpRequest.getContextPath();
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		if (userId == null || userId != 1001) {
			// 관리자가 아닐 시 adminCheck=fail 을 달고 이전 페이지로 이동한다.
			if ( referer != null ) {
				// 이러면 이전 페이지마다 다 adminCheck=fail 로 alert 창 띄어야 된다.
				httpResponse.sendRedirect( referer + "?adminCheck=fail" );
			} else {
				httpResponse.sendRedirect( contextPath + "/ohora/oho_main.jsp" );
			}
			return;
		} else if ( userId == 1001 ) { // 1001 이 관리자다.
			chain.doFilter(request, response);
		} // else if
		
	}

}
