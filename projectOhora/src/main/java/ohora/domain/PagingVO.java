package ohora.domain;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.util.ConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ohora.persistence.ProductDAO;
import ohora.persistence.ProductDAOImpl;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingVO {
	
	private int currentPage = 1;        // 현재 페이지
    private int start;                  // 페이지 블록 시작 번호
    private int end;                    // 페이지 블록 끝 번호
    private boolean prev;               // 이전 페이지 존재 여부
    private boolean next;               // 다음 페이지 존재 여부
    private int numberPerPage = 10;     // 한 페이지당 게시글 수
    private int totalPages;             // 총 페이지 수

    
    // 카테고리 번호 기반 페이징 생성자
    public PagingVO(int currentPage, int numberPerPage, int numberOfPageBlock, int categoryNumber) throws NamingException, SQLException {
        this.currentPage = currentPage;
        this.numberPerPage = numberPerPage;
        
        try (Connection conn = ConnectionProvider.getConnection()) {
            ProductDAO dao = new ProductDAOImpl(conn);
            int totalRecords = dao.getTotalRecords(categoryNumber);  // 카테고리별 총 레코드 수
            this.totalPages = (totalRecords + numberPerPage - 1) / numberPerPage;

            // 페이지 블록 계산
            start = ((currentPage - 1) / numberOfPageBlock) * numberOfPageBlock + 1;
            end = Math.min(start + numberOfPageBlock - 1, totalPages);

            // 이전/다음 페이지 존재 여부 설정
            prev = (start > 1);
            next = (end < totalPages);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // 검색어 기반 페이징 생성자 (추가)
    public PagingVO(int currentPage, int numberPerPage, int numberOfPageBlock, String searchWord) throws NamingException, SQLException {
        this.currentPage = currentPage;
        this.numberPerPage = numberPerPage;

        try (Connection conn = ConnectionProvider.getConnection()) {
            ProductDAO dao = new ProductDAOImpl(conn);
            int totalRecords = dao.getTotalRecordsByProductName(searchWord);  // 검색어로 필터링된 총 레코드 수
            this.totalPages = (totalRecords + numberPerPage - 1) / numberPerPage;

            // 페이지 블록 계산
            start = ((currentPage - 1) / numberOfPageBlock) * numberOfPageBlock + 1;
            end = Math.min(start + numberOfPageBlock - 1, totalPages);

            // 이전/다음 페이지 존재 여부 설정
            prev = (start > 1);
            next = (end < totalPages);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
	
	
	
	public PagingVO(int currentPage, int numberPerPage, int numberOfPageBlock
			, String searchCondition,String searchWord) throws NamingException, SQLException {
		this.currentPage = currentPage;
		this.numberPerPage = numberPerPage;
		Connection conn = ConnectionProvider.getConnection();
		ProductDAO dao = new ProductDAOImpl(conn);
		try {
			int totalPages = dao.getTotalPages(numberPerPage
					, searchCondition,searchWord);
			
			start = ((currentPage - 1) / numberOfPageBlock) * numberOfPageBlock + 1;
			end = Math.min(start + numberOfPageBlock - 1, totalPages);			
			if( end > totalPages) end = totalPages;
			
			if( start != 1) this.prev = true;			
			if( end != totalPages) this.next = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	 * // 아울렛 상품용 생성자 public PagingVO(int currentPage, int numberPerPage, int
	 * numberOfPageBlock, boolean isOutlet) throws NamingException, SQLException {
	 * this.currentPage = currentPage; this.numberPerPage = numberPerPage;
	 * 
	 * try (Connection conn = ConnectionProvider.getConnection()) { OhoraDAO dao =
	 * new OhoraDAOImpl(conn);
	 * 
	 * // 아울렛 상품의 총 페이지 수를 계산 int totalRecords = dao.getTotalRecordsForOutlet();
	 * this.totalPages = (totalRecords + numberPerPage - 1) / numberPerPage;
	 * 
	 * // 페이지 블록 계산 start = ((currentPage - 1) / numberOfPageBlock) *
	 * numberOfPageBlock + 1; end = Math.min(start + numberOfPageBlock - 1,
	 * totalPages);
	 * 
	 * // 이전/다음 페이지 존재 여부 설정 prev = (start > 1); next = (end < totalPages); } catch
	 * (SQLException e) { e.printStackTrace(); throw e; } }
	 */

}