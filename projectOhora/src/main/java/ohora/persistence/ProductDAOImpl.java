package ohora.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import com.util.ConnectionProvider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ohora.domain.DeptVO;
import ohora.domain.OptionDTO;
import ohora.domain.PagingVO;
import ohora.domain.ProductDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDAOImpl implements ProductDAO{
   private Connection conn = null;
   private PreparedStatement pstmt = null;
   private ResultSet rs = null;
   
   public ProductDAOImpl(Connection conn) {
      super();
      this.conn = conn;
   }

   @Override
   public ArrayList<DeptVO> selectTest() throws SQLException {
      int deptno;
      String dname;
      String loc;
      
      ArrayList<DeptVO> list = null;
      String sql = "SELECT * FROM dept";
      
      DeptVO dvo = null;
      
      try {
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         
         if (rs.next()) {
            list = new ArrayList<DeptVO>();
            do {

               deptno = rs.getInt("deptno");
               dname = rs.getString("dname");
               loc = rs.getString("loc");

               dvo = new DeptVO().builder()
                     .deptno(deptno)
                     .dname(dname)
                     .loc(loc)
                     .build();

               list.add(dvo);

            } while (rs.next());
            
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            rs.close();
            pstmt.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return list;
   }
   
   @Override
   public int getTotalRecords(int categoryNumber) throws SQLException {
       int totalRecords = 0;
       String sql;

       // 카테고리 번호가 0일 경우 전체 레코드 조회, 아닐 경우 카테고리별 레코드 조회
       if (categoryNumber == 0) {
           sql = "SELECT COUNT(*) FROM O_PRODUCT";  // 전체 상품
       } else {
           sql = "SELECT COUNT(*) FROM O_PRODUCT WHERE CAT_ID = ?";  // 카테고리별 상품
       }

       try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
           if (categoryNumber != 0) {
               pstmt.setInt(1, categoryNumber);  // 카테고리 값이 0이 아닌 경우 설정
           }
           try (ResultSet rs = pstmt.executeQuery()) {
               if (rs.next()) {
                   totalRecords = rs.getInt(1);  // 총 레코드 수 반환
               }
           }
       }

       return totalRecords;
   }

   @Override
   public int getTotalRecords(String searchCondition, String searchWord) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public int getTotalPages(int numberPerPage, int categoryNumber) throws SQLException {
       int totalRows = 0;
       String sql;
       
       // 카테고리 번호에 따라 쿼리 변경
       if (categoryNumber == 0) {
           sql = "SELECT COUNT(*) FROM O_PRODUCT";  // 전체 상품
       } else {
           sql = "SELECT COUNT(*) FROM O_PRODUCT WHERE CAT_ID = ?";  // 카테고리별 상품
       }

       // 디버깅용 출력: 카테고리 번호 확인
       System.out.println("Category Number: " + categoryNumber);
       
       try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
           if (categoryNumber != 0) {
               // 카테고리 번호 설정이 누락되지 않도록 확인
               pstmt.setInt(1, categoryNumber);  // 카테고리 번호가 0이 아니면 카테고리 값 설정
           }

           try (ResultSet rs = pstmt.executeQuery()) {
               if (rs.next()) {
                   totalRows = rs.getInt(1);  // 총 레코드 수
               }
           }
       }

       // 총 페이지 수 계산
       int totalPages = (totalRows + numberPerPage - 1) / numberPerPage;

       // 디버깅용 출력
       System.out.println("Total Rows: " + totalRows);  // 디버깅: 총 레코드 수 확인
       System.out.println("Total Pages: " + totalPages);  // 디버깅: 총 페이지 수 확인

       return totalPages;
   }
   
   
   @Override
   public int getTotalPagesForOutlet(int numberPerPage) throws SQLException {
       int totalRows = 0;
       String sql = "SELECT COUNT(*) FROM O_PRODUCT WHERE pdt_discount_rate >= 40";  // 할인율 40% 이상 상품만 대상

       // SQL 실행
       try (PreparedStatement pstmt = this.conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
           if (rs.next()) {
               totalRows = rs.getInt(1);  // 총 레코드 수
           }
       }

       // 총 페이지 수 계산
       int totalPages = (totalRows + numberPerPage - 1) / numberPerPage;

       // 디버깅용 출력
       System.out.println("Total Rows (Outlet): " + totalRows);  // 디버깅: 총 레코드 수 확인
       System.out.println("Total Pages (Outlet): " + totalPages);  // 디버깅: 총 페이지 수 확인

       return totalPages;
   }
   
   
   
   @Override
   public int getTotalRecordsForOutlet() throws SQLException {
       String sql = "SELECT COUNT(*) FROM O_PRODUCT WHERE pdt_discount_rate >= 40";
       int totalRecords = 0;
       try (PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
           if (rs.next()) {
              totalRecords = rs.getInt(1);
           }
       }
       return totalRecords;
   }



   @Override
   public int getTotalPages(int numberPerPage, String searchCondition, String searchWord) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
   }

   // 전체 목록
   @Override
   public ArrayList<ProductDTO> select(int currentPage, int numberPerPage) throws SQLException {
      int pdt_id;
      String pdt_name;
      int pdt_amount;
      int pdt_discount_rate;
      String pdt_img_url;
      String pdt_img_url2;
      int pdt_review_count;
      int pdt_discount_amount;
      ArrayList<ProductDTO> list = null;
      
      
      
      String sql = "SELECT * FROM ( "
            + "SELECT ROWNUM no, t.* FROM ("
            + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_adddate "
            + "FROM O_PRODUCT "
            // + "ORDER BY pdt_adddate DESC "
            + ") t "
            + ") b "
            + "WHERE no BETWEEN ? AND ? ";
      
      ProductDTO pdt = null;
      int start = (currentPage-1) * numberPerPage + 1;
      int end = start + numberPerPage -1;
      int totalRecords = getTotalRecords();
      if (end > totalRecords) end = totalRecords;
      
      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, start);
         pstmt.setInt(2, end);
         rs = pstmt.executeQuery();
         
         if (rs.next()) {
            list = new ArrayList<ProductDTO>();
            do {
               pdt_id = rs.getInt("pdt_id");
               pdt_name = rs.getString("pdt_name");
               pdt_amount = rs.getInt("pdt_amount");
               pdt_discount_rate = rs.getInt("pdt_discount_rate");
               pdt_img_url = rs.getString("pdt_img_url");
               pdt_img_url2 = rs.getString("pdt_img_url2");
               pdt_review_count = rs.getInt("pdt_review_count");
               
               if (pdt_discount_rate != 0) {
                  pdt_discount_amount = (int) (pdt_amount - (pdt_amount * (pdt_discount_rate / 100.0f)));                     
               } else {
                  pdt_discount_amount = pdt_amount;
               }

               pdt = new ProductDTO().builder()
                     .pdt_id(pdt_id)
                     .pdt_name(pdt_name)
                     .pdt_amount(pdt_amount)
                     .pdt_discount_rate(pdt_discount_rate)
                     .pdt_img_url(pdt_img_url)
                     .pdt_img_url(pdt_img_url2)
                     .pdt_review_count(pdt_review_count)
                     .pdt_discount_amount(pdt_discount_amount)
                     .build();

               list.add(pdt);

            } while (rs.next());
            
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            rs.close();
            pstmt.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return list;
   }
   
   
   @Override
   public ArrayList<ProductDTO> selectProducts(int currentPage, int numberPerPage, int categoryNumber) throws SQLException {

      int pdt_id;
       String pdt_name;
       int pdt_amount;
       int pdt_discount_rate;
       String pdt_img_url;
       String pdt_img_url2;
       int pdt_review_count;
       int pdt_discount_amount;
       
       ArrayList<ProductDTO> list = new ArrayList<>(); // 리스트 초기화

       // 카테고리 값에 따라 SQL 쿼리 분리
       String sql;
       if (categoryNumber == 0) {
           // 전체 상품 조회 쿼리 (카테고리 필터 없음)
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_adddate "
               + "FROM O_PRODUCT "
               + ") t "
               + ") b "
               + "WHERE no BETWEEN ? AND ?";
       } else {
           // 카테고리별 상품 조회 쿼리
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_adddate "
               + "FROM O_PRODUCT "
               + "WHERE CAT_ID = ? " // 카테고리 필터
               + ") t "
               + ") b "
               + "WHERE no BETWEEN ? AND ?";
       }

       // 페이징 값 설정
       int start = (currentPage - 1) * numberPerPage + 1;
       int end = start + numberPerPage - 1;
       int totalRecords = getTotalRecords(categoryNumber); // 총 레코드 수 가져오기
       if (end > totalRecords) end = totalRecords;

       // SQL 실행
       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           if (categoryNumber == 0) {
               // 전체 상품 조회일 때
               pstmt.setInt(1, start); // 페이징 시작
               pstmt.setInt(2, end);   // 페이징 끝
           } else {
               // 카테고리별 상품 조회일 때
               pstmt.setInt(1, categoryNumber); // 카테고리 값 설정
               pstmt.setInt(2, start);          // 페이징 시작
               pstmt.setInt(3, end);            // 페이징 끝
           }

           try (ResultSet rs = pstmt.executeQuery()) {
               while (rs.next()) {
                  pdt_id = rs.getInt("pdt_id");
                   pdt_name = rs.getString("pdt_name");
                   pdt_amount = rs.getInt("pdt_amount");
                   pdt_discount_rate = rs.getInt("pdt_discount_rate");
                   pdt_img_url = rs.getString("pdt_img_url");
                   pdt_img_url2 = rs.getString("pdt_img_url2");
                   pdt_review_count = rs.getInt("pdt_review_count");

                   if (pdt_discount_rate != 0) {
                       pdt_discount_amount = (int) (pdt_amount - (pdt_amount * (pdt_discount_rate / 100.0f)));
                   } else {
                       pdt_discount_amount = pdt_amount;
                   }

                   ProductDTO pdt = new ProductDTO().builder()
                      .pdt_id(pdt_id)
                       .pdt_name(pdt_name)
                       .pdt_amount(pdt_amount)
                       .pdt_discount_rate(pdt_discount_rate)
                       .pdt_img_url(pdt_img_url)
                       .pdt_img_url2(pdt_img_url2)
                       .pdt_review_count(pdt_review_count)
                       .pdt_discount_amount(pdt_discount_amount)
                       .build();

                   list.add(pdt);
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return list;
   }
   
   @Override
   public ArrayList<ProductDTO> selectByProductName(String searchWord, int currentPage, int numberPerPage) throws SQLException {
      
       ArrayList<ProductDTO> list = new ArrayList<>();
       String sql = "SELECT * FROM ( "
                  + "SELECT ROWNUM no, t.* FROM ("
                  + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count "
                  + "FROM O_PRODUCT "
                  + "WHERE pdt_name LIKE ? ORDER BY pdt_adddate DESC) t "
                  + ") b WHERE no BETWEEN ? AND ?";

       int start = (currentPage - 1) * numberPerPage + 1;
       int end = start + numberPerPage - 1;

       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setString(1, "%" + searchWord + "%");
           pstmt.setInt(2, start);
           pstmt.setInt(3, end);
           
           try (ResultSet rs = pstmt.executeQuery()) {
               while (rs.next()) {
                  ProductDTO product = new ProductDTO();
                   product.setPdt_id(rs.getInt("pdt_id"));
                   product.setPdt_name(rs.getString("pdt_name"));
                   product.setPdt_amount(rs.getInt("pdt_amount"));
                   product.setPdt_discount_rate(rs.getInt("pdt_discount_rate"));
                   product.setPdt_img_url(rs.getString("pdt_img_url"));
                   product.setPdt_img_url2(rs.getString("pdt_img_url2"));
                   product.setPdt_review_count(rs.getInt("pdt_review_count"));

                   // 할인 금액 계산
                   int discountAmount = (product.getPdt_discount_rate() != 0) 
                       ? (int) (product.getPdt_amount() - (product.getPdt_amount() * (product.getPdt_discount_rate() / 100.0f))) 
                       : product.getPdt_amount();
                   product.setPdt_discount_amount(discountAmount);

                   list.add(product);
               }
           }
       }
       return list;
   }
   

   @Override
   public int getTotalRecordsByProductName(String searchWord) throws SQLException {
       String sql = "SELECT COUNT(*) FROM O_PRODUCT WHERE pdt_name LIKE ?";
       int totalRecords = 0;
       
       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setString(1, "%" + searchWord + "%");
           
           try (ResultSet rs = pstmt.executeQuery()) {
               if (rs.next()) {
                   totalRecords = rs.getInt(1);
               }
           }
       }
       return totalRecords;
   }
   

   @Override
   public int getTotalRecords() throws SQLException {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public ArrayList<ProductDTO> selectProductsByCreatedDate(int currentPage, int numberPerPage, int categoryNumber) throws SQLException {
       ArrayList<ProductDTO> list = new ArrayList<>();
       String sql;

       // SQL 쿼리 구성 (카테고리 여부에 따라 구분)
       if (categoryNumber == 0) {
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_adddate "
               + "FROM O_PRODUCT ORDER BY pdt_adddate DESC) t "
               + ") b WHERE no BETWEEN ? AND ?";
       } else {
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_adddate "
               + "FROM O_PRODUCT WHERE CAT_ID = ? ORDER BY pdt_adddate DESC) t "
               + ") b WHERE no BETWEEN ? AND ?";
       }

       // 페이징 범위 설정
       int start = (currentPage - 1) * numberPerPage + 1;
       int end = start + numberPerPage - 1;

       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           if (categoryNumber != 0) {
               pstmt.setInt(1, categoryNumber);
               pstmt.setInt(2, start);
               pstmt.setInt(3, end);
           } else {
               pstmt.setInt(1, start);
               pstmt.setInt(2, end);
           }

           try (ResultSet rs = pstmt.executeQuery()) {
               while (rs.next()) {
                   ProductDTO product = new ProductDTO();
                   product.setPdt_id(rs.getInt("pdt_id"));
                   product.setPdt_name(rs.getString("pdt_name"));
                   product.setPdt_amount(rs.getInt("pdt_amount"));
                   product.setPdt_discount_rate(rs.getInt("pdt_discount_rate"));
                   product.setPdt_img_url(rs.getString("pdt_img_url"));
                   product.setPdt_img_url2(rs.getString("pdt_img_url2"));
                   product.setPdt_review_count(rs.getInt("pdt_review_count"));

                   // 할인 금액 계산
                   int discountAmount = (product.getPdt_discount_rate() != 0) 
                       ? (int) (product.getPdt_amount() - (product.getPdt_amount() * (product.getPdt_discount_rate() / 100.0f))) 
                       : product.getPdt_amount();
                   product.setPdt_discount_amount(discountAmount);

                   list.add(product);
               }
           }
       }

       return list;
   }

   @Override
   public ArrayList<ProductDTO> selectProductsBySales(int currentPage, int numberPerPage, int categoryNumber) throws SQLException {
       ArrayList<ProductDTO> list = new ArrayList<>();
       String sql;

       // SQL 쿼리 구성 (카테고리 여부에 따라 구분)
       if (categoryNumber == 0) {
           // 전체 상품 조회 (카테고리 필터 없음)
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_sales_count "
               + "FROM O_PRODUCT ORDER BY pdt_sales_count DESC) t "
               + ") b WHERE no BETWEEN ? AND ?";
       } else {
           // 카테고리별 상품 조회
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_sales_count "
               + "FROM O_PRODUCT WHERE CAT_ID = ? ORDER BY pdt_sales_count DESC) t "
               + ") b WHERE no BETWEEN ? AND ?";
       }

       // 페이징 범위 설정
       int start = (currentPage - 1) * numberPerPage + 1;
       int end = start + numberPerPage - 1;

       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           if (categoryNumber != 0) {
               pstmt.setInt(1, categoryNumber);
               pstmt.setInt(2, start);
               pstmt.setInt(3, end);
           } else {
               pstmt.setInt(1, start);
               pstmt.setInt(2, end);
           }

           try (ResultSet rs = pstmt.executeQuery()) {
               while (rs.next()) {
                   ProductDTO product = new ProductDTO();
                   product.setPdt_id(rs.getInt("pdt_id"));
                   product.setPdt_name(rs.getString("pdt_name"));
                   product.setPdt_amount(rs.getInt("pdt_amount"));
                   product.setPdt_discount_rate(rs.getInt("pdt_discount_rate"));
                   product.setPdt_img_url(rs.getString("pdt_img_url"));
                   product.setPdt_img_url2(rs.getString("pdt_img_url2"));
                   product.setPdt_review_count(rs.getInt("pdt_review_count"));
                   product.setPdt_sales_count(rs.getInt("pdt_sales_count"));

                   // 할인 금액 계산
                   int discountAmount = (product.getPdt_discount_rate() != 0) 
                       ? (int) (product.getPdt_amount() - (product.getPdt_amount() * (product.getPdt_discount_rate() / 100.0f))) 
                       : product.getPdt_amount();
                   product.setPdt_discount_amount(discountAmount);

                   list.add(product);
               }
           }
       }

       return list;
   }
   
   
   @Override
   public ArrayList<ProductDTO> selectProductsByViewcount(int currentPage, int numberPerPage, int categoryNumber) throws SQLException {
       ArrayList<ProductDTO> list = new ArrayList<>();
       String sql;

       // SQL 쿼리 구성 (카테고리 여부에 따라 구분)
       if (categoryNumber == 0) {
           // 전체 상품 조회 (카테고리 필터 없음)
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_sales_count, pdt_viewcount "
               + "FROM O_PRODUCT ORDER BY pdt_viewcount DESC) t "
               + ") b WHERE no BETWEEN ? AND ?";
       } else {
           // 카테고리별 상품 조회
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_sales_count, pdt_viewcount "
               + "FROM O_PRODUCT WHERE CAT_ID = ? ORDER BY pdt_viewcount DESC) t "
               + ") b WHERE no BETWEEN ? AND ?";
       }

       // 페이징 범위 설정
       int start = (currentPage - 1) * numberPerPage + 1;
       int end = start + numberPerPage - 1;

       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           if (categoryNumber != 0) {
               pstmt.setInt(1, categoryNumber);
               pstmt.setInt(2, start);
               pstmt.setInt(3, end);
           } else {
               pstmt.setInt(1, start);
               pstmt.setInt(2, end);
           }

           try (ResultSet rs = pstmt.executeQuery()) {
               while (rs.next()) {
                   ProductDTO product = new ProductDTO();
                   product.setPdt_id(rs.getInt("pdt_id"));
                   product.setPdt_name(rs.getString("pdt_name"));
                   product.setPdt_amount(rs.getInt("pdt_amount"));
                   product.setPdt_discount_rate(rs.getInt("pdt_discount_rate"));
                   product.setPdt_img_url(rs.getString("pdt_img_url"));
                   product.setPdt_img_url2(rs.getString("pdt_img_url2"));
                   product.setPdt_review_count(rs.getInt("pdt_review_count"));
                   product.setPdt_sales_count(rs.getInt("pdt_sales_count"));
                   product.setPdt_viewcount(rs.getInt("pdt_viewcount"));

                   // 할인 금액 계산
                   int discountAmount = (product.getPdt_discount_rate() != 0) 
                       ? (int) (product.getPdt_amount() - (product.getPdt_amount() * (product.getPdt_discount_rate() / 100.0f))) 
                       : product.getPdt_amount();
                   product.setPdt_discount_amount(discountAmount);

                   list.add(product);
               }
           }
       }

       return list;
   }
   
   // 아울렛 상품 조회
   @Override
   public ArrayList<ProductDTO> selectProductsOutlet(int currentPage, int numberPerPage) throws SQLException {
      int pdt_id;
       String pdt_name;
       int pdt_amount;
       int pdt_discount_rate;
       String pdt_img_url;
       String pdt_img_url2;
       int pdt_review_count;
       int pdt_discount_amount;
       
       ArrayList<ProductDTO> list = new ArrayList<>(); // 리스트 초기화

       String sql;
           // 할인률 40% 이상 상품 조회 쿼리 (카테고리 필터 없음)
           sql = "SELECT * FROM ( "
               + "SELECT ROWNUM no, t.* FROM ("
               + "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_img_url2, pdt_review_count, pdt_adddate "
               + "FROM O_PRODUCT WHERE pdt_discount_rate >= 40 "
               + ") t "
               + ") b "
               + "WHERE no BETWEEN ? AND ?";


       // 페이징 값 설정
       int start = (currentPage - 1) * numberPerPage + 1;
       int end = start + numberPerPage - 1;

       // SQL 실행
       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
               // 전체 상품 조회일 때
               pstmt.setInt(1, start); // 페이징 시작
               pstmt.setInt(2, end);   // 페이징 끝


           try (ResultSet rs = pstmt.executeQuery()) {
               while (rs.next()) {
                  pdt_id = rs.getInt("pdt_id");
                   pdt_name = rs.getString("pdt_name");
                   pdt_amount = rs.getInt("pdt_amount");
                   pdt_discount_rate = rs.getInt("pdt_discount_rate");
                   pdt_img_url = rs.getString("pdt_img_url");
                   pdt_img_url2 = rs.getString("pdt_img_url2");
                   pdt_review_count = rs.getInt("pdt_review_count");

                   if (pdt_discount_rate != 0) {
                       pdt_discount_amount = (int) (pdt_amount - (pdt_amount * (pdt_discount_rate / 100.0f)));
                   } else {
                       pdt_discount_amount = pdt_amount;
                   }

                   ProductDTO product = new ProductDTO().builder()
                      .pdt_id(pdt_id)
                       .pdt_name(pdt_name)
                       .pdt_amount(pdt_amount)
                       .pdt_discount_rate(pdt_discount_rate)
                       .pdt_img_url(pdt_img_url)
                       .pdt_img_url(pdt_img_url2)
                       .pdt_review_count(pdt_review_count)
                       .pdt_discount_amount(pdt_discount_amount)
                       .build();

                   list.add(product);
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return list;
   }
   
   

   // 추가구성상품 출력할 때, 추가구성상품이 선택한 상품의 카테고리에 따라 다르게 출력되게....
   @Override
   public ProductDTO selectProductById(int pdtId) throws SQLException {
       ProductDTO product = null;

       // 옵션이 있는지 여부를 확인할 수 있는 플래그
       boolean hasOption = checkIfOptionExists(pdtId);

       // 옵션이 있을 때와 없을 때 각각의 SQL 쿼리 준비
       String sqlWithOption = "SELECT P.*, O.opt_id, O.opt_name, O.opt_amount, O.opt_count "
                            + "FROM O_PRODUCT P "
                            + "JOIN O_PDTOPTION O ON P.pdt_id = O.pdt_id "
                            + "WHERE P.pdt_id = ?";
                            
       String sqlWithoutOption = "SELECT * FROM O_PRODUCT WHERE pdt_id = ?";

       // 조건에 따라 다른 SQL 쿼리를 사용
       String sql = hasOption ? sqlWithOption : sqlWithoutOption;

       try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setInt(1, pdtId);

           try (ResultSet rs = pstmt.executeQuery()) {
               List<OptionDTO> options = new ArrayList<>(); // 옵션 리스트 생성

               while (rs.next()) {
                   if (product == null) {  // 첫 번째 결과에서 ProductDTO 생성
                       product = new ProductDTO();
                       product.setPdt_id(rs.getInt("pdt_id"));
                       product.setPdt_name(rs.getString("pdt_name"));
                       product.setPdt_amount(rs.getInt("pdt_amount"));
                       product.setPdt_discount_rate(rs.getInt("pdt_discount_rate"));
                       product.setPdt_img_url(rs.getString("pdt_img_url"));
                       product.setPdt_img_url2(rs.getString("pdt_img_url2"));
                       product.setPdt_review_count(rs.getInt("pdt_review_count"));
                       product.setPdt_sales_count(rs.getInt("pdt_sales_count"));
                       product.setCat_id(rs.getInt("cat_id"));
                       product.setPdt_description(rs.getString("pdt_description"));

                       // 할인 금액 계산
                       int discountAmount = (int) (product.getPdt_amount() * (1 - product.getPdt_discount_rate() / 100.0));
                       product.setPdt_discount_amount(discountAmount);
                   }

                   // 옵션이 있는 경우 OptionDTO 객체 생성 및 추가
                   if (hasOption) {
                       OptionDTO option = new OptionDTO();
                       option.setOpt_id(rs.getInt("opt_id"));
                       option.setPdt_id(pdtId);
                       option.setOpt_name(rs.getString("opt_name"));
                       option.setOpt_amount(rs.getInt("opt_amount"));
                       option.setOpt_count(rs.getInt("opt_count"));
                       options.add(option); // 옵션 리스트에 추가
                   }
               }

               // 옵션이 있는 경우에만 ProductDTO에 옵션 리스트 설정
               if (product != null && hasOption) {
                   product.setOptions(options); // ProductDTO에 옵션 리스트 설정
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
           throw e;
       }

       return product;
   }

   // 옵션 존재 여부를 확인하는 메서드
   private boolean checkIfOptionExists(int pdtId) throws SQLException {
       String checkOptionSql = "SELECT COUNT(*) FROM O_PDTOPTION WHERE pdt_id = ?";
       
       try (PreparedStatement pstmt = conn.prepareStatement(checkOptionSql)) {
           pstmt.setInt(1, pdtId);
           
           try (ResultSet rs = pstmt.executeQuery()) {
               if (rs.next()) {
                   return rs.getInt(1) > 0; // 옵션이 존재하면 true 반환
               }
           }
       }
       
       return false; // 옵션이 없으면 false 반환
   }

   @Override
   public ArrayList<ProductDTO> getAllProducts() throws SQLException {
      int pdt_id;
      String pdt_name;
      int pdt_amount;
      int pdt_discount_rate;
      String pdt_img_url;
      String pdt_img_url2;
      int pdt_review_count;
      int pdt_discount_amount;
      
              String sql = "SELECT * FROM o_product"; // 상품 정보가 있는 테이블 이름으로 변경
              ArrayList<ProductDTO> productList = new ArrayList<>();

              try (PreparedStatement pstmt = conn.prepareStatement(sql);
                   ResultSet rs = pstmt.executeQuery()) {
                  while (rs.next()) {
                      ProductDTO product = new ProductDTO();
                      product.setPdt_id(rs.getInt("pdt_id"));
                       product.setPdt_name(rs.getString("pdt_name"));
                       product.setPdt_amount(rs.getInt("pdt_amount"));
                       product.setPdt_discount_rate(rs.getInt("pdt_discount_rate"));
                       product.setPdt_img_url(rs.getString("pdt_img_url"));
                       product.setPdt_img_url2(rs.getString("pdt_img_url2"));
                       product.setPdt_review_count(rs.getInt("pdt_review_count"));
                       product.setPdt_sales_count(rs.getInt("pdt_sales_count"));
                      // 필요한 다른 필드도 추가 설정
                       // 할인 금액 계산
                       pdt_discount_amount = (int) (product.getPdt_amount() * (1 - product.getPdt_discount_rate() / 100.0));
                       product.setPdt_discount_amount(pdt_discount_amount);
                      productList.add(product);
                  }
              }catch (SQLException e) {
                 e.printStackTrace();
                 throw e;
             }
              return productList;
      }
   
   
   // 상품 상세에서 장바구니 담기 버튼 시작
      @Override
      public boolean addCartBtn(int userId, String[] pdtIds, String[] pdtCounts, HttpServletRequest request) {
         
         boolean ischeck = true;
         
         // pdtIds와 pdtCounts를 각각 int 배열로 변환
          if (pdtIds == null || pdtCounts == null || pdtIds.length != pdtCounts.length) {
              return false; // 배열이 null이거나 길이가 다르면 작업을 수행하지 않음
          }

          int[] intPdtIds = new int[pdtIds.length];
          int[] intPdtCounts = new int[pdtCounts.length];

          // String -> int 변환
          for (int i = 0; i < pdtIds.length; i++) {
              try {
                  intPdtIds[i] = Integer.parseInt(pdtIds[i]);
                  intPdtCounts[i] = Integer.parseInt(pdtCounts[i]);
              } catch (NumberFormatException e) {
                  System.out.println("유효하지 않은 값: pdtId=" + pdtIds[i] + ", pdtCount=" + pdtCounts[i]);
                  return false; // 변환에 실패한 경우 작업 중단
              }
          }

          Connection conn = null;
          PreparedStatement checkStmt = null;
          PreparedStatement updateStmt = null;
          PreparedStatement insertStmt = null;
          PreparedStatement countStmt = null;
          ResultSet rs = null;

          try {
             
             conn = ConnectionProvider.getConnection(); // 실제 DB 연결 초기화
              if (conn == null) {
                  throw new RuntimeException("DB 연결에 실패했습니다.");
              }
             
              String checkSql = "SELECT clist_pdt_count FROM o_cartlist WHERE user_id = ? AND pdt_id = ?";
              String updateSql = "UPDATE o_cartlist SET clist_pdt_count = clist_pdt_count + ? WHERE user_id = ? AND pdt_id = ?";
              String insertSql = "INSERT INTO o_cartlist (clist_id, user_id, pdt_id, opt_id, clist_pdt_count, clist_adddate, clist_select) VALUES (addcart_seq.NEXTVAL, ?, ?, 0, ?, SYSDATE, 'Y')";
              String productCountSql = "SELECT COUNT(DISTINCT pdt_id) AS product_count FROM o_cartlist WHERE user_id = ?";

              checkStmt = conn.prepareStatement(checkSql);
              updateStmt = conn.prepareStatement(updateSql);
              insertStmt = conn.prepareStatement(insertSql);

              // 상품 수량 업데이트 또는 인서트
              for (int i = 0; i < intPdtIds.length; i++) {
                  int pdtId = intPdtIds[i];
                  int pdtCount = intPdtCounts[i];

                  // 존재 여부 확인
                  checkStmt.setInt(1, userId);
                  checkStmt.setInt(2, pdtId);
                  rs = checkStmt.executeQuery();

                  if (rs.next()) {
                      // 상품이 이미 존재하는 경우, 수량 업데이트
                      updateStmt.setInt(1, pdtCount);
                      updateStmt.setInt(2, userId);
                      updateStmt.setInt(3, pdtId);
                      updateStmt.executeUpdate();
                  } else {
                      // 상품이 존재하지 않는 경우, 새로 추가
                      insertStmt.setInt(1, userId);
                      insertStmt.setInt(2, pdtId);
                      insertStmt.setInt(3, pdtCount);
                      insertStmt.executeUpdate();
                      // 사용자 장바구니 상품 수 카운트
                      countStmt = conn.prepareStatement(productCountSql);
                      countStmt.setInt(1, userId);
                      rs = countStmt.executeQuery();
                      if (rs.next()) {
                         int productCount = rs.getInt("product_count");
                         // 세션에 저장
                         request.getSession().setAttribute("productCount", productCount);
                      }
                  }
              }
              
              return ischeck; // 모든 작업이 성공하면 true 반환
              
          } catch (SQLException | NamingException e) {
              System.out.println("DB 작업 실패: " + e.getMessage());
              return false;
          } finally {
              try {
                  if (rs != null) rs.close();
                  if (checkStmt != null) checkStmt.close();
                  if (updateStmt != null) updateStmt.close();
                  if (insertStmt != null) insertStmt.close();
                  if (countStmt != null) countStmt.close();
                  if (conn != null) conn.close();
              } catch (SQLException e) {
                  System.out.println("리소스 해제 실패: " + e.getMessage());
              }
          }
          
      }
      // 상품 상세에서 장바구니 담기 버튼 끝

}
