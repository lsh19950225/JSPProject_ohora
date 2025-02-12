package mvc.command;

import java.sql.Connection; 
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import net.sf.json.JSONObject;
import ohora.domain.ReviewDTO;

import ohora.persistence.ReviewDAO;
import ohora.persistence.ReviewDAOImpl;

public class ReviewMoreRevHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		
		System.out.println("리뷰 더보기 버튼 핸들러 진입");
		int pdtId= Integer.parseInt(request.getParameter("pdtId")) ; 
		int currentpage= Integer.parseInt(request.getParameter("currentRevPage")) ; 
		String sort= request.getParameter("sort") ; 
		
		
	    Connection conn = ConnectionProvider.getConnection();
	    ReviewDAO dao = new ReviewDAOImpl(conn);
		JSONObject reviewM = null;

		try {
			reviewM = dao.selectMoreReview(conn, currentpage , pdtId, sort);			
			
			System.out.println("돌아온 list => "+ reviewM.toString());

	        return reviewM.toString(); // 최종 JSON 문자열 반환

		} catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("리뷰 댓글 dao 진입 실패");
		} finally {
			conn.close();
		}
		
		return null;
	}

}
