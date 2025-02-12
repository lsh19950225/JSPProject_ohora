package mvc.command;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ukdoHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String login = request.getParameter("login");
		String signUp = request.getParameter("join");
		String findPasswd = request.getParameter("findPasswd");
	    String changePasswd = request.getParameter("changePasswd");
	    String changeLogin = request.getParameter("changeLogin");
		
		System.out.println("파라미터 ==" + login);
		System.out.println("파라미터2 ==" + signUp);
		
		
		if ("signup".equals(signUp)  ) {
			return "/ohora/oho_signUpForm.jsp";
		}
		else if ("gologin".equals(login) ) {
			System.out.println("로그인do 진입완료");
			return "/ohora/login.jsp";
		}
		else if ("fail".equals(login) ) {
			System.out.println("로그인do 진입실패");
			return "/ohora/login.jsp";
		}
		else if ("goFindPasswd".equals(findPasswd)) {
	         System.out.println("비밀번호 찾기 do 진입 완료");
	         return "/ohora/findPasswd.jsp";
	      }
	      else if ("goChangePasswd".equals(changePasswd)) {
	         System.out.println("비밀번호 변경 do 진입 완료");
	         return "/ohora/changePasswd.jsp";
	      }
	      else if ("goChangeLogin".equals(changeLogin)) {
	         System.out.println("비밀번호 변경 후 로그인 do 진입 완료");
	         return "/ohora/login.jsp";
	      }
		
		
		// 아이디 찾기
		String findID = request.getParameter("findID");
		
		// 아이디 찾기
		if ("findID1".equals(findID) ) {
			System.out.println("findID 진입완료");
			return "/ohora/findID.jsp";
		} 
		
		
		
		
		

		// 이벤트
		String event = request.getParameter("event");
		
		// 이벤트 
				if ("event".equals(event) ) {
					System.out.println("이벤트do 진입완료");
					return "/ohora/event_index.jsp";
				}
				else if ("eventnew".equals(event) ) {
					System.out.println("이벤트_신규회원 진입완료");
					return "/ohora/event_event6.jsp";
				}
				else if ("eventvip8".equals(event) ) {
					System.out.println("이벤트_vip 진입완료");
					return "/ohora/event_vipgift08.jsp";
				}
				else if ("eventvip7".equals(event) ) {
					System.out.println("이벤트_vip 진입완료");
					return "/ohora/event_vipgift07.jsp";
				}
				else if ("gelnailtip".equals(event) ) {
					System.out.println("이벤트_gelnailtip 진입완료");
					return "/ohora/event_gelnailtip.jsp";
				}
				else if ("lesserafim".equals(event) ) {
					System.out.println("이벤트_lesserafim 진입완료");
					return "/ohora/event_lesserafim.jsp";
				}
				else if ("eventvip6".equals(event) ) {
					System.out.println("이벤트_eventvip6 진입완료");
					return "/ohora/event_vipgift06.jsp";
				}
				else if ("eventdayz".equals(event) ) {
					System.out.println("이벤트_eventdayz 진입완료");
					return "/ohora/event_dazedayz_2.jsp";
				}
				else if ("eventchoa".equals(event) ) {
					System.out.println("이벤트_eventchoa진입완료");
					return "/ohora/event_eventchoa.jsp";
				}
		
		// how to
		String howto = request.getParameter("howto");
		
		// how to
				if ("howto".equals(howto) ) {
					 System.out.println("howto 진입완료");
					 return "/ohora/howto.jsp";
				}		
				else if ("howto01".equals(howto) ) {
					 System.out.println("howto01 진입완료");
					 return "/ohora/howto_detail01.jsp";
				}		
				else if ("howto02".equals(howto) ) {
					 System.out.println("howto02 진입완료");
					 return "/ohora/howto_detail02.jsp";
				}		
				else if ("howto03".equals(howto) ) {
					 System.out.println("howto03 진입완료");
					 return "/ohora/howto_detail03.jsp";
				}		
				else if ("howto04".equals(howto) ) {
					 System.out.println("howto04 진입완료");
					 return "/ohora/howto_detail04.jsp";
				}		
				else if ("howto05".equals(howto) ) {
					 System.out.println("howto05 진입완료");
					 return "/ohora/howto_detail05.jsp";
				}	
				else if ("howto06".equals(howto) ) {
					 System.out.println("howto06 진입완료");
					 return "/ohora/howto_detail06.jsp";
				}		
				else if ("howto07".equals(howto) ) {
					 System.out.println("howto07 진입완료");
					 return "/ohora/howto_detail07.jsp";
				}		
				else if ("howto08".equals(howto) ) {
					 System.out.println("howto08 진입완료");
					 return "/ohora/howto_detail08.jsp";
				}		
				else if ("howto09".equals(howto) ) {
					 System.out.println("howto09 진입완료");
					 return "/ohora/howto_detail09.jsp";
				}		
				else if ("howto10".equals(howto) ) {
					 System.out.println("howto10 진입완료");
					 return "/ohora/howto_detail10.jsp";
				}		
		
				
		//about
		String about = request.getParameter("about");
		
		// about
		if ("about".equals(about) ) {
			 System.out.println("about 진입완료");
			 return "/ohora/about.jsp";
		}		
	
		
		
		// membership
		String membership = request.getParameter("membership");
		
		// membership
				if ("membership".equals(membership) ) {
					 System.out.println("membership 진입완료");
					 return "/ohora/membership.jsp";
				}		
		
			
				
		// magazine
		String magazine = request.getParameter("magazine");
		
		// magazine
		if ("magazine".equals(magazine) ) {
			 System.out.println("magazine 진입완료");
			 return "/ohora/magazine.jsp";
		}	
		
		
		// notice
		String notice = request.getParameter("notice");

		// notice
				if ("notice".equals(notice) ) {
					 System.out.println("notice 진입완료");
					 return "/ohora/notice.jsp";
				}	
		
		// FAQ
		String FAQ = request.getParameter("FAQ");
		
		// FAQ
		if ("FAQ".equals(FAQ) ) {
			 System.out.println("FAQ 진입완료");
			 return "/ohora/FAQ.jsp";
		}	
		
		// FAQdetail
				if ("FAQdetail".equals(FAQ) ) {
					 System.out.println("FAQdetail 진입완료");
					 return "/ohora/FAQ_detail.jsp";
				}	
	
		// 간편회원가입
				String signUpfast = request.getParameter("signUpfast");
				
				if ("signUpfast".equals(signUpfast)  ) {
					return "/ohora/join.jsp";
				}
				
		// 주문조회
				String ordercheck = request.getParameter("ordercheck");
				
				if ("ordercheck_nolog".equals(ordercheck)  ) {
					return "/ohora/ordercheck_nolog.jsp";
				}
	
		// 이용약관
				String termsofservice = request.getParameter("termsofservice");
				
				if ("termsofservice".equals(termsofservice)  ) {
					return "/ohora/termsofservice.jsp";
				}
				
		// 개인정보처리방침
				String policy = request.getParameter("policy");
				
				if ("policy".equals(policy)  ) {
					return "/ohora/policy.jsp";
				}
				
				
				
				
		
		
	return null;
	
	}

}
