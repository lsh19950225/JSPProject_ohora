package mvc.command;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.domain.ProductDTO;
import ohora.persistence.ProductDAO;
import ohora.persistence.ProductDAOImpl;

public class ViewHandler implements CommandHandler {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("View Handler process...");

        // 상품 ID를 요청 파라미터에서 가져옴
        String productId = request.getParameter("product_id");
        System.out.println("Received product_id: " + productId);  // 디버깅용 출력

        // DAO 및 데이터베이스 연결
        Connection conn = null;
        ProductDAO dao = null;
        ProductDTO product = null;

        try {
            conn = ConnectionProvider.getConnection();
            dao = new ProductDAOImpl(conn);
            
            // 상품 ID가 null이 아니고 유효할 때 상품 조회
            if (productId != null && !productId.trim().isEmpty()) {
                int pdtId = Integer.parseInt(productId);
                product = dao.selectProductById(pdtId);  // 상품 ID로 특정 상품 정보 조회
            } else {
                System.out.println("상품 ID가 유효하지 않습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
        }

        // 상품 정보를 요청에 저장
        request.setAttribute("product", product);

        // 페이지 이동 경로 반환 (RequestDispatcher 사용 X)
        return "/ohora/prd_detail_view.jsp";
    }
}
