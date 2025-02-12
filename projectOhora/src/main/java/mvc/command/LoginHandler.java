package mvc.command;

import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.ConnectionProvider;

import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;


public class LoginHandler implements CommandHandler {
   
   // .do 로 요청 필요
   @Override
   public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
         
         System.out.println("loginHandler test");
         
         String contextPath = request.getContextPath();
         
         // System.out.println(contextPath);
         
         String id = request.getParameter("member_id");
         String password = request.getParameter("member_passwd");
         
         // 비밀번호 암호화
         try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte [] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
               sb.append(String.format("%02x", b));
            } // for
            password = sb.toString();
         } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
         } // try catch
         
         System.out.println("id : " + id + " / " + "password : " + password);
         
         Connection conn = ConnectionProvider.getConnection();
         MemberDAO dao = new MemberDAOImpl(conn);
         
         int dbUserId;
         int productCount;
         
         try {
            
            dbUserId = dao.loginNum(id, password);
            System.out.println("user pk : " + dbUserId);
            
            productCount = dao.cartCount(dbUserId);
            
            if ( dbUserId != -1 ) {
            	
            	 Cookie[] cookies = request.getCookies();
 		        if (cookies != null) {
 		            for (Cookie cookie : cookies) {
 		                if (cookie.getName().equals("basketItems")) {
 		                    Cookie deleteCookie = new Cookie(cookie.getName(), "");
 		                    deleteCookie.setPath("/");
 		                    deleteCookie.setMaxAge(0);
 		                    deleteCookie.setDomain(request.getServerName());
 		                    response.addCookie(deleteCookie);
 		                }
 		            }
 		        }
 			
            	
               HttpSession session = request.getSession();
               session.setAttribute("userId", dbUserId);
               session.setAttribute("productCount", productCount);
               
               response.sendRedirect( "/projectOhora/product/main.do" );
               // RequestDispatcher dispatcher = request.getRequestDispatcher("/ohora/oho_main.jsp");
               // dispatcher.forward(request, response);
               
               return null;
            } else {
               response.sendRedirect( contextPath + "/ohora.do?login=fail" );
               
               return null;
            } // if
            
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            
            if(conn != null) {
               
               try {
                  conn.close();
               } catch (Exception e) {
                  e.printStackTrace();
               } // try catch
               
            } // if
            
         } // try catch finally
            
      return null;
      
   }

} // class