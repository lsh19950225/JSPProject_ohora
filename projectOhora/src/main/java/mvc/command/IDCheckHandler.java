package mvc.command;

import java.sql.Connection; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.domain.UserDTO;
import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;


public class IDCheckHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("중복체크 진입 완료");
		boolean isjungbok = true;
		String msgTag = trim(request.getParameter("msgTag") );
		String value= trim(request.getParameter("val") ); 
		
		System.out.println(msgTag + "/" + value); //idMsg/asdaa22
		
		Connection conn = ConnectionProvider.getConnection();
		MemberDAO dao = new MemberDAOImpl(conn);
		UserDTO dto = new UserDTO();
		
		
		if ( msgTag.equals("idMsg") ) {
			dto.setUser_login_id(value);
			
		} else if ( msgTag.equals("emailMsg")) {
			dto.setUser_email(value);
			
		} else { //폰
			dto.setUser_tel(value);
			
		}
		
		try {
			isjungbok = dao.jungbokCK(conn, dto);
			
			return  String.format("{ \"result\":%b }",  isjungbok);
			/*
			if ( isjungbok == false) {
				System.out.println(" 중복 값 없음 ");
			
			} else{
				System.out.println(" 중복 값 있음 ");
				
			}
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			conn.close();
		}

		return null;
	}
	
	 
	
	private String trim(String str) {
		return str == null ? null : str.trim();
	}
	
}
