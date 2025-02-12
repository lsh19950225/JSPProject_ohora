package mvc.command;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.domain.UserDTO;
import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;
import ohora.persistence.ReviewDAO;
import ohora.persistence.ReviewDAOImpl;

public class MemModifyHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int userId = Integer.parseInt(request.getParameter("userId")) ;
		System.out.println(userId);

		Connection conn = ConnectionProvider.getConnection();
		MemberDAO dao = new MemberDAOImpl(conn);

		UserDTO user = null;

		try {

			// 회원정보 조회
			user = dao.selectUserInfo(conn, userId); //회원정보를 DTO에 담아서 가져옴

			System.out.println("MemModifyHandler : "+ user.toString());


			request.setAttribute("user", user);


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("리뷰 댓글 dao 진입 실패");
		} finally {
			conn.close();
		}

		return "/ohora/oho_memInfo.jsp";
	}

}
