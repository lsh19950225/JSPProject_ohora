package mvc.command;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;

import ohora.persistence.UserCartDAO;
import ohora.persistence.UserCartDAOImpl;

public class ChangePasswdHandler implements CommandHandler {

   @Override
   public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
      System.out.println("changePasswdHandler test");
      
      String contextPath = request.getContextPath();
      String confirmPassword = request.getParameter("confirm_password");
      String userID = request.getParameter("user_id");
      
      // 비밀번호 암호화
      try {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         md.update(confirmPassword.getBytes());
         byte [] byteData = md.digest();
         StringBuilder sb = new StringBuilder();
         for (byte b : byteData) {
            sb.append(String.format("%02x", b));
         } // for
         confirmPassword = sb.toString();
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException(e);
      } // try catch
      
      System.out.println("confirmPassword : " + confirmPassword);
      System.out.println("userID : " + userID);
      
      Connection conn = ConnectionProvider.getConnection();
      MemberDAO dao = new MemberDAOImpl(conn);
      
      int numConfirm;
      
      try {
         
         numConfirm = dao.changePasswd(confirmPassword, userID);
         
         if (numConfirm > 0) {
            response.sendRedirect( contextPath + "/ohora.do?changeLogin=goChangeLogin" );
         } else {
            response.sendRedirect( contextPath + "/ohora/changePasswd.jsp?success=false" );
         } // if else
         
         return null;
         
      } catch (Exception e) {
         e.printStackTrace();
         response.sendRedirect(contextPath + "/ohora/changePasswd.jsp?error=true");
      } finally {
         if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // try catch
      } // try catch finally
      
      return null;
      
   }

}
