package mvc.command;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import net.sf.json.JSONObject;
import ohora.persistence.ReviewDAO;
import ohora.persistence.ReviewDAOImpl;

public class ReviewCmtDeleteHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		System.out.println("댓글삭제 핸들러 진입 완료");
		int cmtId= Integer.parseInt(request.getParameter("cmt_id")) ; 
		System.out.println("넘어온 삭제할 댓글 ID >> " + cmtId);
		int rev_id= Integer.parseInt(request.getParameter("revId")) ; 
		System.out.println("넘어온 삭제할 댓글 리뷰 ID >> " + rev_id);
		Connection conn = ConnectionProvider.getConnection();
		ReviewDAO dao = new ReviewDAOImpl(conn);
		JSONObject revId = null;
		
		try {
			revId = dao.deleteComment(conn, cmtId,rev_id );			
			
			 return revId.toString(); // 리뷰 아이디만 다시 반환

		} catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("리뷰 댓글 삭제 dao 진입 실패");
		} finally {
			conn.close();
		}

		return null;

	}

}
