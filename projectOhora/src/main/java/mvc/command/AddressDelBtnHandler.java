package mvc.command;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.persistence.AddressDAO;
import ohora.persistence.AddressDAOImpl;

public class AddressDelBtnHandler implements CommandHandler {

   @Override
   public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
      System.out.println("addressDelBtnHandler test");
      
      String contextPath = request.getContextPath();
      
      boolean isCheck;
      
      // addr_id 파라미터들을 배열로 받기
        String[] addrIdsStr = request.getParameterValues("addr_id");
        
        Connection conn = ConnectionProvider.getConnection();
      AddressDAO dao = new AddressDAOImpl(conn);
      
      try {
         
         isCheck = dao.addressDelBtn(addrIdsStr);
         System.out.println("isCheck : " + isCheck);
         
         if (isCheck) {
            // 리다이렉트
            response.sendRedirect( contextPath + "/address/addressselect.do" );
         } // if
         
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         conn.close();
      } // try catch finally
        
      return null;
      
   }

}
