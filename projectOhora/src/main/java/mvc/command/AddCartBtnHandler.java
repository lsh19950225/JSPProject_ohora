package mvc.command;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.ConnectionProvider;

import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;
import ohora.persistence.ProductDAO;
import ohora.persistence.ProductDAOImpl;

public class AddCartBtnHandler implements CommandHandler {

   @Override
   public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
      System.out.println("addCartBtnHandler test");
      
      HttpSession session = request.getSession();
      Integer userId = (Integer) session.getAttribute("userId");
      
      String contextPath = request.getContextPath();
      boolean ischeck;
      
      // pdtId와 pdtCount 파라미터들을 각각 배열로 받기
       String[] pdtIds = request.getParameterValues("pdtId");
       String[] pdtCounts = request.getParameterValues("pdtCount");
       
       Connection conn = ConnectionProvider.getConnection();
        ProductDAO dao = new ProductDAOImpl(conn);
        
        try {
           ischeck = dao.addCartBtn(userId, pdtIds, pdtCounts, request);
           
           System.out.println("ischeck : " + ischeck);
           
           if (ischeck) {
              // 상세 페이지가 어딘지 몰라서 일단 iscart 로 보냄
            response.sendRedirect( contextPath + "/cart/useriscart.do" );
         } // if
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         conn.close();
      } // try catch finally
        
       return null;
       
   }

}
