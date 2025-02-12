package mvc.command;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.domain.PagingVO;
import ohora.domain.ProductDTO;
import ohora.persistence.ProductDAO;
import ohora.persistence.ProductDAOImpl;

public class ListHandler implements CommandHandler {

    private int currentPage = 1;
    private int numberPerPage = 12;
    private int defaultPageBlock = 10;     // 기본 페이지 블록 수
    private int numberOfPageBlock = defaultPageBlock; // 동적 변경 가능한 페이지 블록
    private int totalRecords = 0;
    private int categoryNumber = 0;

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 매 요청마다 numberOfPageBlock을 기본값으로 재설정
        numberOfPageBlock = defaultPageBlock;

        
        try {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        } catch (Exception e) {
            currentPage = 1;
        }

        try {
            categoryNumber = request.getParameter("catno") != null ? 
                             Integer.parseInt(request.getParameter("catno")) : 0;
        } catch (Exception e) {
            categoryNumber = 0;
        }

        String sort = request.getParameter("sort");
        String keyword = request.getParameter("keyword");

        Connection conn = null;
        ProductDAO dao = null;
        ArrayList<ProductDTO> list = null;

        try {
            conn = ConnectionProvider.getConnection();
            dao = new ProductDAOImpl(conn);

            if (keyword != null && !keyword.trim().isEmpty()) {
                totalRecords = dao.getTotalRecordsByProductName(keyword);
            } else if ("outlet".equals(sort)) {
                totalRecords = dao.getTotalRecordsForOutlet();
            } else {
                totalRecords = dao.getTotalRecords(categoryNumber);
            }

            int totalPages = (totalRecords + numberPerPage - 1) / numberPerPage;

            // totalPages에 맞춰 numberOfPageBlock을 동적으로 설정
            if ("outlet".equals(sort) && totalPages < numberOfPageBlock) {
                numberOfPageBlock = totalPages;
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                list = dao.selectByProductName(keyword, currentPage, numberPerPage);
            } else if ("new".equals(sort)) {
                list = dao.selectProductsByCreatedDate(currentPage, numberPerPage, categoryNumber);
            } else if ("sales".equals(sort)) {
                list = dao.selectProductsBySales(currentPage, numberPerPage, categoryNumber);
            } else if ("viewcount".equals(sort)) {
                list = dao.selectProductsByViewcount(currentPage, numberPerPage, categoryNumber);
            } else if ("outlet".equals(sort)) {
                list = dao.selectProductsOutlet(currentPage, numberPerPage);
            } else {
                list = dao.selectProducts(currentPage, numberPerPage, categoryNumber);
            }

            PagingVO pvo;
            if (keyword != null && !keyword.trim().isEmpty()) {
                pvo = new PagingVO(currentPage, numberPerPage, numberOfPageBlock, keyword);
            } else {
                pvo = new PagingVO(currentPage, numberPerPage, numberOfPageBlock, categoryNumber);
            }

            request.setAttribute("list", list);
            request.setAttribute("pvo", pvo);
            request.setAttribute("categoryNumber", categoryNumber);
            request.setAttribute("keyword", keyword);
            request.setAttribute("sort", sort);
 
        } finally {
            if (conn != null) conn.close();
        }

        return "/ohora/prd-nail-page.jsp";
    }
}
