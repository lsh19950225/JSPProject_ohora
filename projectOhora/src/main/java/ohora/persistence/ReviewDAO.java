package ohora.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONObject;
import ohora.domain.RevMedia;
import ohora.domain.ReviewDTO;
import ohora.domain.ReviewRating;
import ohora.domain.UserDTO;

public interface ReviewDAO {

	//리뷰 뿌리기
	ArrayList<ReviewDTO> select(Connection conn, int pdt_id, String sort) throws SQLException;

	//리뷰 댓글
	JSONObject selectComment(Connection conn, int rev_id) throws SQLException;

	//리뷰 전체 미디어 받아오기
	ArrayList<ReviewDTO> midiaCount(Connection conn, int pdt_id) throws SQLException;

	// 리뷰 댓글 작성
	int insertComment(Connection conn, int revId, int userId, String comment, Date writedate) throws SQLException;

	//리뷰마다 미디어 뿌리기
	ArrayList<RevMedia> selectPhotos(Connection conn3, int pdt_id) throws SQLException;

	//별점 그래프 및 평균
	ReviewRating ratingAVG(Connection conn, int pdt_id) throws SQLException;

	//댓글 더보기
	JSONObject selectMoreReview(Connection conn, int currentRevCnt, int pdtId, String sort) throws SQLException;


	//댓글 삭제
	JSONObject deleteComment(Connection conn, int cmtId, int rev_id) throws SQLException;
}
