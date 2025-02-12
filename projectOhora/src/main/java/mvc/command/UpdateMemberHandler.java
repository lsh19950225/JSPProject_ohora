package mvc.command;

import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.domain.UserDTO;
import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;

public class UpdateMemberHandler implements CommandHandler {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("UpdateMember Handler...");

        // 필드 초기화
        char smsAgree = 'N';
        char emailAgree = 'N';
        String user_login_id = request.getParameter("member-id");        
        String passwd = request.getParameter("passwd");
        String name = request.getParameter("name");        
        String email = request.getParameter("email1");        
		/* String phone = request.getParameter("mobile[]"); */
     // 전화번호 받기
        String phone = request.getParameter("user_tel"); // "010-1234-5678" 형태로 전달됨
        
        // 동의 여부 처리
        if (request.getParameter("is_sms") != null) {
            smsAgree = request.getParameter("is_sms").charAt(0);
        }
        if (request.getParameter("is_news_mail") != null) {
            emailAgree = request.getParameter("is_news_mail").charAt(0);
        }

        // 패스워드 해싱
        if (passwd != null && !passwd.isEmpty()) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(passwd.getBytes());
                byte[] byteData = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : byteData) {
                    sb.append(String.format("%02x", b));
                }
                passwd = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            passwd = null; // 패스워드가 입력되지 않았으면 변경하지 않음
        }

        // 생년월일 처리
        String bYear = request.getParameter("birth_year");
        String bMonth = request.getParameter("birth_month");
        String bDay = request.getParameter("birth_day");
        String strBirthDay = bYear + "-" + bMonth + "-" + bDay; 
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date user_birthday = format.parse(strBirthDay);

        Connection conn = ConnectionProvider.getConnection();
        MemberDAO dao = new MemberDAOImpl(conn);

        // UserDTO 설정
        UserDTO dto = new UserDTO();
        dto.setUser_login_id(user_login_id);
        dto.setUser_email(email);
        dto.setUser_birth(user_birthday);
        dto.setUser_emailAgree(emailAgree);
        dto.setUser_name(name);
        dto.setUser_password(passwd); // Null일 경우 변경하지 않음
        dto.setUser_smsAgree(smsAgree);
        dto.setUser_tel(phone);

        // 업데이트 실행
        try {
            int rowCount = dao.updateUser(conn, dto);  
            if (rowCount == 1) {
                System.out.println("회원 정보 수정 완료");
            } else {
                System.out.println("회원 정보 수정 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        // 완료 페이지 넘기기
        request.setAttribute("user_login_id", user_login_id);
        request.setAttribute("email", email);
        request.setAttribute("name", name);

        return "/ohora/oho_mypage.jsp";  // 수정 완료 페이지
    }
}
