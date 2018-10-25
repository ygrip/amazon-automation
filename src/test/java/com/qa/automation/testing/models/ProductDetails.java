package com.qa.automation.testing.models;

import net.serenitybdd.core.pages.WebElementFacade;

public class ProductDetails {
    private String name;
    private Double price;
    private String modelYear;
    private String url;
    private String code;
    private WebElementFacade element;

    public ProductDetails() {

    }

    public WebElementFacade getElement() {
        return element;
    }

    public void setElement(WebElementFacade element) {
        this.element = element;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
