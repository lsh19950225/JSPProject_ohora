package com.util;

import java.util.List;
import ohora.domain.ProductDTO;

public class JsonConverter {

    public static String convertProductsToJson(List<ProductDTO> additionalProducts) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");

        for (int i = 0; i < additionalProducts.size(); i++) {
            ProductDTO product = additionalProducts.get(i);

            jsonBuilder.append("{")
                .append("\"pdt_id\":").append(product.getPdt_id()).append(",")
                .append("\"pdt_name\":\"").append(product.getPdt_name()).append("\",")
                .append("\"pdt_amount\":").append(product.getPdt_amount()).append(",")
                .append("\"pdt_img_url\":\"").append(product.getPdt_img_url()).append("\",")
                .append("\"pdt_description\":\"").append(product.getPdt_description()).append("\"")
                .append("}");

            // 마지막 항목이 아닌 경우에만 쉼표 추가
            if (i < additionalProducts.size() - 1) {
                jsonBuilder.append(",");
            }
        }

        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
