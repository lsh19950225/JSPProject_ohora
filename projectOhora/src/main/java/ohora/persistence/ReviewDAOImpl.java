package ohora.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.util.JdbcUtil;

import mvc.command.CommandHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import ohora.domain.RevMedia;
import ohora.domain.ReviewDTO;
import ohora.domain.ReviewRating;
import ohora.domain.UserDTO;

public class ReviewDAOImpl implements ReviewDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private ResultSet rs2 = null;

	public ReviewDAOImpl(Connection conn) {
		super();
		this.conn = conn;
	}

	//제품아이디대로 리뷰뿌리기
	@Override
	public ArrayList<ReviewDTO> select(Connection conn, int prd_id , String sort) throws SQLException {
		System.out.println("리뷰 impl 진입.." + prd_id +" / " + sort);
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<ReviewDTO> list  = null;
		String sql = "";

		if (sort.equals("recommend")) {
			sql = " SELECT * FROM ( "
					+  " SELECT REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, " 
					+	" CASE WHEN (SYSDATE - REV_WRITEDATE) < 1 THEN 'true' ELSE 'false' END AS new, " 
					+	" u.USER_NAME " 
					+	" FROM o_review r " 
					+	" JOIN o_user u ON r.user_id = u.user_id " 
					+	" WHERE pdt_id = ? " 
					+	" GROUP BY REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, u.USER_NAME " 
					+	" ORDER BY  CASE WHEN REV_ISRECOMMEND = 'Y' THEN 0 ELSE 1 END, REV_GOOD_COUNT DESC "
					+  " ) WHERE ROWNUM < 3 ";
		} else if ( sort.equals("new")) {
			sql =  " SELECT * FROM ( "
					+	" SELECT REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, " 
					+	" CASE WHEN (SYSDATE - REV_WRITEDATE) < 1 THEN 'true' ELSE 'false' END AS new, " 
					+	" u.USER_NAME " 
					+	" FROM o_review r " 
					+	" JOIN o_user u ON r.user_id = u.user_id " 
					+	" WHERE pdt_id = ? " 
					+	" GROUP BY REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, u.USER_NAME " 
					+	" ORDER BY  CASE WHEN REV_ISRECOMMEND = 'Y' THEN 0 ELSE 1 END, REV_WRITEDATE DESC "
					+  " ) WHERE ROWNUM < 3 ";
		}else if ( sort.equals("rating")) {
			sql = " SELECT * FROM ( "
					+	" SELECT REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, " 
					+	" CASE WHEN (SYSDATE - REV_WRITEDATE) < 1 THEN 'true' ELSE 'false' END AS new, " 
					+	" u.USER_NAME " 
					+	" FROM o_review r " 
					+	" JOIN o_user u ON r.user_id = u.user_id " 
					+	" WHERE pdt_id = ? " 
					+	" GROUP BY REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, u.USER_NAME " 
					+	" ORDER BY  CASE WHEN REV_ISRECOMMEND = 'Y' THEN 0 ELSE 1 END, REV_RATING DESC "
					+  " ) WHERE ROWNUM < 3 ";

		}else if ( sort.equals("photo")) {
			sql = " SELECT * FROM ( "
					+ " SELECT REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, REV_RATING, "
					+ " REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, REV_ISPHOTO, "
					+ " REV_AGE_GROUP, REV_OPTION, PDT_ID, "
					+ " CASE WHEN (SYSDATE - REV_WRITEDATE) < 1 THEN 'true' ELSE 'false' END AS new, "
					+ " u.USER_NAME "
					+ " FROM o_review r "
					+ " JOIN o_user u ON r.user_id = u.user_id "
					+ " WHERE pdt_id = ? "
					+ " ORDER BY CASE WHEN REV_ISRECOMMEND = 'Y' THEN 0 ELSE 1 END, "
					+ " CASE WHEN REV_ISPHOTO = 'Y' THEN 0 ELSE 1 END "
					+ " ) WHERE ROWNUM < 3 ";
		}

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prd_id);

			System.out.println(sql);
			rs = pstmt.executeQuery();


			if( rs.next() ) {
				list = new ArrayList<ReviewDTO>(); 
				do {
					ReviewDTO review = new ReviewDTO().builder()
							.rev_id(rs.getInt("rev_id"))
							.user_id(rs.getInt("user_id"))
							.ord_pk(rs.getInt("ord_pk"))
							.opdt_id(rs.getInt("opdt_id"))
							.rev_content(rs.getString("rev_content"))
							.rev_writedate(rs.getDate("rev_writedate"))
							.rev_rating(rs.getInt("rev_rating"))
							.rev_good_count(rs.getInt("rev_good_count"))
							.rev_bad_count(rs.getInt("rev_bad_count"))
							.rev_comment_count(rs.getInt("rev_comment_count"))
							.rev_isrecommend(rs.getString("rev_isrecommend") != null ? rs.getString("rev_isrecommend") : "N" )
							.rev_isphoto(rs.getString("rev_isphoto")!= null ? rs.getString("rev_isphoto") : "N")
							.rev_age_group(rs.getString("rev_age_group")!= null ? rs.getString("rev_age_group") : "모름")
							.rev_option(rs.getString("rev_option")!= null ? rs.getString("rev_option") : "없음")
							.pdt_id(rs.getInt("pdt_id"))
							.user_name(rs.getString("user_name"))
							.newImg( new Boolean(  rs.getString("new") ) )
							//.mediacount(rs.getInt("mediacount") )
							.build();

					list.add(review);
				} while (rs.next());

			} // if 

		} catch (Exception e) {
			System.out.println("리뷰 Impl 캐치잡혔따");
			e.printStackTrace();
		} finally {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			// conn.close(); // 커넥션풀 반환..
		}

		System.out.println(list);
		return list;
	}


	//리뷰 댓글보기
	@Override
	public JSONObject selectComment(Connection conn, int review_id) throws SQLException {
		System.out.println("리뷰 댓글 impl 진입.." + review_id);
		PreparedStatement pstmt = null;
		ResultSet rs = null;  


		String sql = " select CMT_ID, REV_ID , oc.USER_ID , CMT_WRITEDATE , CMT_CONTENT , USER_NAME "
				+ " from o_comment oc JOIN o_user ou ON oc.user_id = ou.user_id "
				+ " where rev_id = ? "
				+ " order by cmt_writedate DESC ";

		//{ } json객체 만든다
		JSONObject jsonData = new JSONObject();
		// [] 배열 만들기
		JSONArray jsonCmtArray = new JSONArray();

		try{

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, review_id);
			rs = pstmt.executeQuery(); 

			while( rs.next() ){ 
				int cmt_id = rs.getInt("cmt_id");
				int rev_id = rs.getInt("rev_id");
				int user_id = rs.getInt("user_id");
				Date writedate = rs.getDate("cmt_writedate");
				String cmt_content = rs.getString("cmt_content");
				String cmt_writer = rs.getString("user_name");

				String cmt_writedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(writedate);

				JSONObject jsonComment = new JSONObject(); 
				jsonComment.put("cmt_id", cmt_id);
				jsonComment.put("rev_id", rev_id);
				jsonComment.put("user_id", user_id);
				jsonComment.put("cmt_writedate", cmt_writedate);   
				jsonComment.put("cmt_content", cmt_content);
				jsonComment.put("user_name", cmt_writer);


				jsonCmtArray.add(jsonComment);    

			}// while

			jsonData.put("comments",jsonCmtArray);

			System.out.println(jsonData);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("리뷰댓글 Impl 캣치..");
		}finally{
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}


		return jsonData;

	}

	//사진 총량 뿌리기
	@Override
	public ArrayList<ReviewDTO> midiaCount(Connection conn, int prd_id) throws SQLException {

		System.out.println("리뷰 미디어 impl 진입..");
		PreparedStatement pstmt = null;
		ResultSet rs = null;  

		ArrayList<ReviewDTO> list  = null;

		String sql = " select COUNT(CASE WHEN rev_isphoto='Y' THEN 1 END) mediaCnt  "
				+ " from o_review "
				+ " WHERE pdt_id = ? ";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prd_id);

			//System.out.println(sql);
			rs = pstmt.executeQuery();


			if( rs.next() ) {
				list = new ArrayList<ReviewDTO>(); 
				do {
					ReviewDTO media = new ReviewDTO().builder()
							.mediaCnt(rs.getInt("mediaCnt"))
							.build();

					list.add(media);
				} while (rs.next());

			} // if 

		} catch (Exception e) {
			System.out.println("리뷰미디어 Impl 캐치잡혔따");
			e.printStackTrace();
		} finally {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			//conn.close(); // 커넥션풀 반환..
		}

		System.out.println("미디어 >> " + list);
		return list;

	}

	//댓글작성
	@Override
	public int insertComment(Connection conn, int revId, int userId, String comment, Date writedate) throws SQLException {
		System.out.println("리뷰 댓글 작성 impl 진입..");
		PreparedStatement pstmt = null;
		ResultSet rs = null;  

		int rowCount = 0;

		String sql = " INSERT INTO o_comment VALUES ( O_COMMENT_SEQ.NEXTVAL , ? , ? ,? ,?  ) ";
		String sql2 = " UPDATE o_review SET rev_comment_count = rev_comment_count + 1 WHERE rev_id = ?  " ;


		//{ } json객체 만든다
		JSONObject jsonData = new JSONObject();
		// [] 배열 만들기
		JSONArray jsonCmtArray = new JSONArray();

		try{

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, revId);
			pstmt.setInt(2, userId);
			pstmt.setDate(3, new java.sql.Date(writedate.getTime())); 
			pstmt.setString(4, comment);

			rowCount = pstmt.executeUpdate();

			if (rowCount ==1) {
				PreparedStatement pstmt2 = null;

				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1, revId);

				rowCount = pstmt2.executeUpdate();
			}

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("리뷰댓글작성 Impl 캣치..");
		}finally{
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}


		return rowCount;

	}

	//리뷰 사진
	@Override
	public ArrayList<RevMedia> selectPhotos(Connection conn, int pdt_id) throws SQLException {

		System.out.println("리뷰 사진 뿌릴 impl 진입..");
		PreparedStatement pstmt = null;
		ResultSet rs = null;  

		String sql = " select u.rev_id , filesystemname, fileoriginalname "
				+ " from o_revurl u JOIN o_review w ON u.rev_id = w.rev_id "
				+ " WHERE pdt_id = ? ";
		ArrayList<RevMedia> list  = null;


		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,pdt_id);

			rs = pstmt.executeQuery();


			if( rs.next() ) {
				list = new ArrayList<RevMedia>(); 
				do {
					RevMedia media = new RevMedia().builder()
							.rev_id(rs.getInt("rev_id"))
							.filesystemname(rs.getString("filesystemname"))
							.fileoriginalname(rs.getString("fileoriginalname"))
							.build();

					list.add(media);
				} while (rs.next());

			} // if 

		} catch (Exception e) {
			System.out.println("사진들 Impl 캐치잡혔따");
			e.printStackTrace();
		} finally {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			//conn.close(); // 커넥션풀 반환..
		}

		System.out.println("사진들 >> " + list);
		return list;

	}

	//평점 평균
	@Override
	public ReviewRating ratingAVG(Connection conn, int pdt_id) throws SQLException {
		System.out.println("평점 평균 impl 진입..");
		PreparedStatement pstmt = null;

		ResultSet rs = null;  

		String sql = " SELECT "
				+ " AVG(rev_rating) AS avg_rating, "
				+ " COUNT(CASE WHEN rev_rating = 5 THEN 1 END) AS count_5, "
				+ " COUNT(CASE WHEN rev_rating = 4 THEN 1 END) AS count_4, "
				+ " COUNT(CASE WHEN rev_rating = 3 THEN 1 END) AS count_3, "
				+ " COUNT(CASE WHEN rev_rating = 2 THEN 1 END) AS count_2, "
				+ " COUNT(CASE WHEN rev_rating = 1 THEN 1 END) AS count_1 "
				+ " FROM o_review "
				+ " WHERE pdt_id = ? ";

		ReviewRating rating  = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,pdt_id);

			rs = pstmt.executeQuery();

			if( rs.next() ) {

				do {
					rating = new ReviewRating().builder()
							.avg_rating(rs.getDouble("avg_rating"))
							.count_5(rs.getInt("count_5"))
							.count_4(rs.getInt("count_4"))
							.count_3(rs.getInt("count_3"))
							.count_2(rs.getInt("count_2"))
							.count_1(rs.getInt("count_1"))
							.build();

				} while (rs.next());

			} // if 

		} catch (Exception e) {
			System.out.println("평점 Impl 캐치잡혔따");
			e.printStackTrace();
		} finally {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			//conn.close(); // 커넥션풀 반환..
		}

		return rating;
	}

	@Override
	public JSONObject selectMoreReview(Connection conn , int currentPage,  int pdtId, String sort) throws SQLException {
		System.out.println("리뷰 더보기 버튼 impl 진입..");
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;  
		ResultSet rs2 = null;  
		ResultSet rs3 = null;  

		System.out.println("리뷰 더보기 impl 넘어온 값 : " + currentPage+ "/" +pdtId +"/" +sort);

		JSONObject jsonData = new JSONObject();

		JSONArray jsonRevArray = new JSONArray();



		JSONObject photoData = new JSONObject();
		// 이건 포토 개개별 JSON 객체
		JSONArray photosArray = null; 
		//이건 포토 개개별을 묶을 어레이 => 리뷰 객체에 넣어주면 객체안에 배열 완성..
		String sql = "";

		if (sort.equals("recommend")) {
			sql = 
			" SELECT e.* "
			+ " FROM ( "
			+ " SELECT ROWNUM rnn, tmp.* "
			+ " FROM ( " 
			+ " SELECT ROWNUM rn, REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, " 
			+ " REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, " 
			+ " REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, " 
			+ " CASE WHEN (SYSDATE - REV_WRITEDATE) < 1 THEN 'true' ELSE 'false' END AS new, " 
			+ " u.USER_NAME " 
			+ " FROM o_review r " 
			+ "JOIN o_user u ON r.user_id = u.user_id "
			+ " WHERE pdt_id = ? " 
			+ " ORDER BY CASE WHEN REV_ISRECOMMEND = 'Y' THEN 0 ELSE 1 END, REV_GOOD_COUNT DESC " 
			+ " ) tmp " 
			+ " ORDER BY  rn ASC "
			+ " ) e "
			+ " WHERE rnn >= (?*2)+1 AND rnn <= (?*2)+2 "
			+ " ORDER BY  rnn ASC ";
		} else if (sort.equals("new")) {
			sql =
			" SELECT e.* "
			+ " FROM ( "
			+ " SELECT ROWNUM rnn, tmp.* "
			+ " FROM ( " 
			+ " SELECT ROWNUM rn, REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, " 
			+ " REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, " 
			+ " REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, " 
			+ " CASE WHEN (SYSDATE - REV_WRITEDATE) < 1 THEN 'true' ELSE 'false' END AS new, " 
			+ " u.USER_NAME " 
			+ " FROM o_review r " 
			+ "JOIN o_user u ON r.user_id = u.user_id "
			+ " WHERE pdt_id = ? " 
			+ " ORDER BY CASE WHEN REV_ISRECOMMEND = 'Y' THEN 0 ELSE 1 END, REV_WRITEDATE DESC " 
			+ " ) tmp " 
			+ " ORDER BY  rn ASC "
			+ " ) e "
			+ " WHERE rnn >= (?*2)+1 AND rnn <= (?*2)+2 "
			+ " ORDER BY  rnn ASC ";
		} else if (sort.equals("rating")) {
			sql = 
			" SELECT e.* "
			+ " FROM ( "
			+ " SELECT ROWNUM rnn, tmp.* "
			+ " FROM ( " 
			+ " SELECT ROWNUM rn, REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, " 
			+ " REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, " 
			+ " REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, " 
			+ " CASE WHEN (SYSDATE - REV_WRITEDATE) < 1 THEN 'true' ELSE 'false' END AS new, " 
			+ " u.USER_NAME " 
			+ " FROM o_review r " 
			+ "JOIN o_user u ON r.user_id = u.user_id "
			+ " WHERE pdt_id = ? " 
			+ " ORDER BY CASE WHEN REV_ISRECOMMEND = 'Y' THEN 0 ELSE 1 END, REV_RATING DESC " 
			+ " ) tmp " 
			+ " ORDER BY  rn ASC "
			+ " ) e "
			+ " WHERE rnn >= (?*2)+1 AND rnn <= (?*2)+2 "
			+ " ORDER BY  rnn ASC ";
		} else if (sort.equals("photo")) {
			sql = " SELECT e.* "
					+ " FROM ( "
					+ " SELECT ROWNUM rnn, tmp.* "
					+ " FROM ( " 
					+ " SELECT ROWNUM rn, REV_ID, r.USER_ID, ORD_PK, OPDT_ID, REV_CONTENT, REV_WRITEDATE, " 
					+ " REV_RATING, REV_GOOD_COUNT, REV_BAD_COUNT, REV_COMMENT_COUNT, REV_ISRECOMMEND, " 
					+ " REV_ISPHOTO, REV_AGE_GROUP, REV_OPTION, PDT_ID, " 
					+ " CASE WHEN (SYSDATE - REV_WRITEDATE) < 1 THEN 'true' ELSE 'false' END AS new, " 
					+ " u.USER_NAME " 
					+ " FROM o_review r " 
					+ "JOIN o_user u ON r.user_id = u.user_id "
					+ " WHERE pdt_id = ? " 
					+ " ORDER BY CASE WHEN REV_ISRECOMMEND = 'Y' THEN 0 ELSE 1 END, CASE WHEN REV_ISPHOTO = 'Y' THEN 0 ELSE 1 END " 
					+ " ) tmp " 
					+ " ORDER BY  rn ASC "
					+ " ) e "
					+ " WHERE rnn >= (?*2)+1 AND rnn <= (?*2)+2 "
					+ " ORDER BY  rnn ASC ";
		}


		String sql2 = " SELECT COUNT(*) allRevCnt "
				+ " FROM o_review "
				+ " WHERE pdt_id = ? " ;



		System.out.println("sql :" +sql);
		System.out.println("sql2 :" +sql2);

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt2 = conn.prepareStatement(sql2);

			pstmt.setInt(1, pdtId);
			pstmt.setInt(2, currentPage);
			pstmt.setInt(3, currentPage);

			pstmt2.setInt(1, pdtId);



			//System.out.println(sql);
			rs = pstmt.executeQuery();
			rs2 = pstmt2.executeQuery();


			JSONObject photolist = null; 

			if (rs2.next()) {
				int allRevCnt = rs2.getInt(1); // 첫 번째 컬럼인 COUNT(*) 값을 가져옵니다.

				while (rs.next()) {
					int rev_id = rs.getInt("rev_id");
					int user_id = rs.getInt("user_id");
					int ord_pk = rs.getInt("ord_pk");
					int opdt_id = rs.getInt("opdt_id");
					int rev_comment_count = rs.getInt("rev_comment_count");
					String rev_isrecommend = rs.getString("rev_isrecommend") != null ? rs.getString("rev_isrecommend") : "N" ;
					String rev_isphoto = rs.getString("rev_isphoto")!= null ? rs.getString("rev_isphoto") : "N" ;
					String rev_age_group = rs.getString("rev_age_group")!= null ? rs.getString("rev_age_group") : "모름";
					int pdt_id = rs.getInt("pdt_id");
					String user_name = rs.getString("user_name");
					Boolean newImg = new Boolean(  rs.getString("new") );
					String rev_content = rs.getString("rev_content");
					Date rev_writedate = rs.getDate("rev_writedate");
					int rev_rating = rs.getInt("rev_rating");
					int rev_good_count = rs.getInt("rev_good_count");
					int rev_bad_count = rs.getInt("rev_bad_count");

					//안에서 포토 리뷰번호별 분리
					String sql3 = " SELECT r.rev_id rev_id , filesystemname "
							+ " FROM o_review r JOIN o_revurl u ON r.rev_id = u.rev_id "
							+ " WHERE r.rev_id = ? ";

					pstmt3 = conn.prepareStatement(sql3);
					pstmt3.setInt(1, rev_id);

					rs3 = pstmt3.executeQuery();


					photolist = new JSONObject();
					photosArray = new JSONArray();

					while (rs3.next()) {
						String filesystemname = rs3.getString("filesystemname");

						photolist.put("filesystemname",filesystemname);

						photosArray.add(photolist); // 포토 배열 완성
					}

					String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rev_writedate);

					JSONObject list = new JSONObject();
					list.put("rev_id", rev_id);
					list.put("user_id", user_id);
					list.put("ord_pk", ord_pk);
					list.put("opdt_id", opdt_id);
					list.put("rev_comment_count", rev_comment_count);
					list.put("rev_isrecommend", rev_isrecommend);
					list.put("rev_isphoto", rev_isphoto);
					list.put("rev_age_group", rev_age_group);
					list.put("pdt_id", pdt_id);
					list.put("newImg", newImg);
					list.put("user_name", user_name);
					list.put("rev_content", rev_content);
					list.put("rev_writedate", formattedDate);
					list.put("rev_rating", rev_rating);
					list.put("rev_good_count", rev_good_count);
					list.put("rev_bad_count", rev_bad_count);
					list.put("allRevCnt", allRevCnt); // 전체 리뷰 개수
					list.put("photos", photosArray );

					jsonRevArray.add(list);

				}

			}


			jsonData.put("reviews", jsonRevArray);



		} catch (Exception e) {
			System.out.println("리뷰 더보기 Impl 캐치잡혔따");
			e.printStackTrace();
		} finally {
			if (rs != null) rs.close();
			if (rs2 != null) rs2.close();
			if (rs3!= null) rs3.close();
			if (pstmt != null) pstmt.close();
			if (pstmt2 != null) pstmt2.close();
			if (pstmt3 != null) pstmt3.close();

		}

		System.out.println(jsonData);
		return jsonData;
	}

	@Override
	public JSONObject deleteComment(Connection conn, int cmtId , int revId) throws SQLException {
		
		int rowCount = 0;
		int rowCount2 = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;  
		System.out.println("임플 넘어온 코멘트아이디: " + cmtId + "// 임플 리뷰 아이디 : " + revId);
		String sql = " delete from o_comment where cmt_id = ? ";
		String sql2 = "update o_review set rev_comment_count = rev_comment_count -1 WHERE rev_id = ? ";
		
		try{

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cmtId);
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setInt(1, revId);
			
			
			JSONObject jsonRevId = new JSONObject(); 
			jsonRevId.put("rev_id", revId);
			
			rowCount = pstmt.executeUpdate();
			rowCount2 = pstmt2.executeUpdate();
			if(rowCount ==1 ) {
				return jsonRevId;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("리뷰댓글삭제 Impl 캣치..");
		}finally{
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}


		return null;

	}


}
