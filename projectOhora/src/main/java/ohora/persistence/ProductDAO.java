package ohora.persistence;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import ohora.domain.DeptVO;
import ohora.domain.ProductDTO;

public interface ProductDAO {

	ArrayList<DeptVO> selectTest() throws SQLException;

	ArrayList<ProductDTO> select(int currentPage, int numberPerPage) throws SQLException;

	// 1-3. 총 레코드 수
	int getTotalRecords() throws SQLException;
	// 메인페이지에 뿌릴 상품 정보 가져오는...
	ArrayList<ProductDTO> getAllProducts() throws SQLException;

	int getTotalRecords(int categoryNumber) throws SQLException;

	int getTotalRecords(String searchCondition, String searchWord) throws SQLException;
	// 1-4. 총 페이지 수
	int getTotalPages(int numberPerPage, int categoryNumber) throws SQLException;

	// 검색시 총페이지
	int getTotalPages(int numberPerPage, String searchCondition, String keyword) throws SQLException;

	// 카테고리 적용해서 조회
	ArrayList<ProductDTO> selectProducts(int currentPage, int numberPerPage, int categoryNumber) throws SQLException;

	int getTotalRecordsByProductName(String searchWord) throws SQLException;

	ArrayList<ProductDTO> selectByProductName(String searchWord, int currentPage, int numberPerPage) throws SQLException;

	/*
	    SELECT *
		FROM O_PRODUCT
		WHERE PDT_DATE >= SYSDATE - 30;
	 */
	ArrayList<ProductDTO> selectProductsByCreatedDate(int currentPage, int numberPerPage, int categoryNumber) throws SQLException;

	ArrayList<ProductDTO> selectProductsBySales(int currentPage, int numberPerPage, int categoryNumber) throws SQLException;

	ArrayList<ProductDTO> selectProductsByViewcount(int currentPage, int numberPerPage, int categoryNumber) throws SQLException;

	ProductDTO selectProductById(int pdtId) throws SQLException;

	ArrayList<ProductDTO> selectProductsOutlet(int currentPage, int numberPerPage) throws SQLException;

	int getTotalRecordsForOutlet() throws SQLException;
	// 아울렛 용 총 페이지
	int getTotalPagesForOutlet(int numberPerPage) throws SQLException;

	// 상세 페이지 장바구니 담기 처리
	boolean addCartBtn(int userId, String[] pdtIds, String[] pdtCounts, HttpServletRequest request);


}
