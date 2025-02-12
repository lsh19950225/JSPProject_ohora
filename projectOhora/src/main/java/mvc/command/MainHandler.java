package mvc.command;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.domain.ProductDTO;
import ohora.persistence.ProductDAO;
import ohora.persistence.ProductDAOImpl;

public class MainHandler implements CommandHandler {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection conn = null;
        ProductDAO dao = null;
        ArrayList<ProductDTO> productList = null;

        try {
            // 데이터베이스 연결 및 DAO 초기화
            conn = ConnectionProvider.getConnection();
            dao = new ProductDAOImpl(conn);

            // 전체 상품 리스트 가져오기
            productList = dao.getAllProducts();  // 모든 상품을 가져오는 메서드 호출

            // JSP에 전달할 데이터 설정
            request.setAttribute("productList", productList);

        } finally {
            if (conn != null) conn.close();
        }
        // 디버깅용
        System.out.println(productList);

        // 메인 페이지 JSP로 이동
        return "/ohora/oho_main.jsp";
    }
}
