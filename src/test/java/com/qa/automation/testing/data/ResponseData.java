package com.qa.automation.testing.data;

import com.qa.automation.testing.models.ProductDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("com.qa.automation.testing.data.ResponseData")
public class ResponseData {

    private static ResponseData instance = null;
    private List<ProductDetails> productDetailsList;

    public static ResponseData getInstance() {
        if (instance == null) {
            instance = new ResponseData();
        }
        return instance;
    }

    public List<ProductDetails> getProductDetailsList() {
        return productDetailsList;
    }

    public void setProductDetailsList(List<ProductDetails> productDetailsList) {
        this.productDetailsList = productDetailsList;
    }
}
