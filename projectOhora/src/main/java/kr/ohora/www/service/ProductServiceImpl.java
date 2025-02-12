package kr.ohora.www.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.ohora.www.domain.product.ProductDTO;
import kr.ohora.www.domain.product.ProductSearchDTO;
import kr.ohora.www.persistence.ProductMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<ProductDTO> getProductList(int pageNum, int numberPerPage, int categoryNumber, String sort, String keyword) {
        int start = (pageNum - 1) * numberPerPage + 1;
        int end = start + numberPerPage - 1;
        // 시간 체크해보자
        
        ProductSearchDTO searchDTO = new ProductSearchDTO();
        searchDTO.setCategoryNumber(categoryNumber);
        searchDTO.setSort(sort);
        searchDTO.setKeyword(keyword);
        searchDTO.setStart(start);
        searchDTO.setEnd(end);
        
        return productMapper.selectProducts(searchDTO);
    }

    public int getTotalRecords(int categoryNumber, String keyword) {
        ProductSearchDTO searchDTO = new ProductSearchDTO();
        searchDTO.setCategoryNumber(categoryNumber);
        searchDTO.setKeyword(keyword);

        return productMapper.getTotalRecords(categoryNumber, keyword);
    }

   @Override
   public ProductDTO getProductById(int productId) {
        // 상품 정보 반환
        return productMapper.selectDetailViewProduct(productId);
   }

   @Override
   public List<ProductDTO> getAdditionalProducts(int catId) {
      List<Integer> additionalProductIds;

        // 카테고리별 추가 구성 상품 ID 매핑
        switch (catId) {
            case 1:
                additionalProductIds = Arrays.asList(170, 174, 169, 172);
                break;
            case 2:
                additionalProductIds = Arrays.asList(170, 169);
                break;
            case 3:
                additionalProductIds = Arrays.asList(173, 175, 169);
                break;
            default:
                additionalProductIds = new ArrayList<>();
        }

        // 추가 구성 상품 정보 조회
        List<ProductDTO> additionalProducts = new ArrayList<>();
        for (int id : additionalProductIds) {
            ProductDTO product = productMapper.selectDetailViewProduct(id);
            if (product != null) {
                additionalProducts.add(product);
            }
        }
        return additionalProducts;
    }
   
   
   @Override
   public List<ProductDTO> getOutletProducts(int pageNum, int numberPerPage) {
       return productMapper.selectOutletProducts(pageNum, numberPerPage);
   }

   @Override
   public int getOutletTotalRecords() {
       return productMapper.getOutletTotalRecords();
   }
   

   @Override
    public int addProduct(ProductDTO product) {
        return productMapper.insertProduct(product);
    }
   
   @Override
   @Transactional
   public int deleteProduct(int productId, String uploadRealPath) {
       try {
           // 1. 삭제할 상품 정보 조회 (이미지 경로 포함)
           ProductDTO product = productMapper.selectDetailViewProduct(productId);
           if (product != null) {
               // 2. 업로드된 이미지 파일 삭제
               if (product.getPdtImgUrl() != null) {
                   File file1 = new File(uploadRealPath, product.getPdtImgUrl());
                   if (file1.exists() && file1.isFile()) {
                       if (file1.delete()) {
                           System.out.println("File 1 deleted: " + file1.getName());
                       } else {
                           System.out.println("Failed to delete File 1: " + file1.getName());
                       }
                   }
               }

               if (product.getPdtImgUrl2() != null) {
                   File file2 = new File(uploadRealPath, product.getPdtImgUrl2());
                   if (file2.exists() && file2.isFile()) {
                       if (file2.delete()) {
                           System.out.println("File 2 deleted: " + file2.getName());
                       } else {
                           System.out.println("Failed to delete File 2: " + file2.getName());
                       }
                   }
               }
           }

           // 3. 자식 테이블 데이터 삭제
           productMapper.deleteFromPdtColor(productId);
           productMapper.deleteFromPdtDesign(productId);
           productMapper.deleteFromPdtLineup(productId);
           productMapper.deleteFromPdtOption(productId);

           // 4. 부모 테이블 데이터 삭제
           return productMapper.deleteProduct(productId);

       } catch (Exception e) {
           // 예외 발생 시 롤백
           throw new RuntimeException("상품 삭제 중 오류 발생: " + productId, e);
       }
   }
   
   // 메인 페이지 ajax
    public List<ProductDTO> getProductsByCategory(int categoryNumber, String orderBy) {
        return productMapper.selectWeeklyBestProducts(categoryNumber, orderBy);
    }
    
    @Transactional
    public void updateProductWithCounts(
          @Param("product") ProductDTO product, 
          HttpServletRequest request,
          @Param("multipartFile1") MultipartFile multipartFile1, 
          @Param("multipartFile2") MultipartFile multipartFile2, 
          @Param("existingFile1") String existingFile1, 
          @Param("existingFile2") String existingFile2) throws Exception {
        
        // 최신 조회수 및 리뷰수 가져오기
        ProductDTO latestProduct = productMapper.selectDetailViewProduct(product.getPdtId());
        product.setPdtViewcount(latestProduct.getPdtViewcount());
        product.setPdtReviewCount(latestProduct.getPdtReviewCount());

        // 파일 업로드 경로 설정
        String uploadRealPath = request.getServletContext().getRealPath("/resources/images/product_image/");
        File uploadDir = new File(uploadRealPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 기존 이미지 삭제 및 새 이미지 업로드 처리
        // 이미지 1
        if (!multipartFile1.isEmpty()) {
            // 기존 이미지 삭제
            if (existingFile1 != null) {
                File oldFile1 = new File(uploadRealPath, existingFile1);
                if (oldFile1.exists() && oldFile1.isFile()) {
                    if (oldFile1.delete()) {
                        System.out.println("Deleted existing File 1: " + existingFile1);
                    } else {
                        System.out.println("Failed to delete existing File 1: " + existingFile1);
                    }
                }
            }
            // 새 이미지 저장
            String originalFilename1 = multipartFile1.getOriginalFilename();
            String filesystemName1 = UUID.randomUUID().toString() + "_" + originalFilename1;
            File dest1 = new File(uploadRealPath, filesystemName1);
            multipartFile1.transferTo(dest1);
            product.setPdtImgUrl(filesystemName1);
        } else {
            product.setPdtImgUrl(existingFile1);
        }

        // 이미지 2
        if (!multipartFile2.isEmpty()) {
            // 기존 이미지 삭제
            if (existingFile2 != null) {
                File oldFile2 = new File(uploadRealPath, existingFile2);
                if (oldFile2.exists() && oldFile2.isFile()) {
                    if (oldFile2.delete()) {
                        System.out.println("Deleted existing File 2: " + existingFile2);
                    } else {
                        System.out.println("Failed to delete existing File 2: " + existingFile2);
                    }
                }
            }
            // 새 이미지 저장
            String originalFilename2 = multipartFile2.getOriginalFilename();
            String filesystemName2 = UUID.randomUUID().toString() + "_" + originalFilename2;
            File dest2 = new File(uploadRealPath, filesystemName2);
            multipartFile2.transferTo(dest2);
            product.setPdtImgUrl2(filesystemName2);
        } else {
            product.setPdtImgUrl2(existingFile2);
        }

        try {
            // 상품 정보 업데이트
            productMapper.updateProduct(product);
            // 조회수 및 리뷰수 업데이트
            productMapper.updateViewAndReviewCounts(product.getPdtId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product: " + product.getPdtId(), e);
        }
    }


}
