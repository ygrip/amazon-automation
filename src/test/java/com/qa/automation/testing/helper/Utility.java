package com.qa.automation.testing.helper;

import com.qa.automation.testing.data.ResponseData;
import com.qa.automation.testing.models.ProductDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("com.qa.automation.testing.helper.Utility")
public class Utility {
    public List<Map<String, Object>> getNthItems(Integer n) {
        ResponseData responseData = ResponseData.getInstance();
        List<Map<String, Object>> result = new ArrayList<>();

        List<ProductDetails> productDetailsList = responseData.getProductDetailsList();
        Integer limit = productDetailsList.size() - 1;

        for (int i = 0; i < n; i++) {
            if (i > limit) {
                break;
            } else {
                Map<String, Object> item = new HashMap<>();
                item.put("name", productDetailsList.get(i).getName());
                item.put("price", productDetailsList.get(i).getPrice());
                result.add(item);
            }
        }

        return result;
    }

    public List<ProductDetails> getItemsLowerThanPriceInYear(Double treshold, String year) {
        ResponseData responseData = ResponseData.getInstance();
        List<ProductDetails> productDetailsList = responseData.getProductDetailsList();
        List<ProductDetails> result = new ArrayList<>();

        for (ProductDetails item : productDetailsList) {
            if (item.getPrice() <= treshold && item.getModelYear().equals(year)) {
                result.add(item);
            }
        }

        return result;
    }
}
