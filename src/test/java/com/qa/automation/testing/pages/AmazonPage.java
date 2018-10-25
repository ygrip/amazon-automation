package com.qa.automation.testing.pages;

import com.qa.automation.testing.data.ResponseData;
import com.qa.automation.testing.helper.Utility;
import com.qa.automation.testing.models.ProductDetails;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component("com.qa.automation.testing.pages.AmazonPage")
public class AmazonPage extends PageObject {
    public void selectDepartmets(String department) {
        WebElementFacade departmentTab = find(By.xpath("//select[@id='searchDropdownBox']"));
        WebElementFacade toasterDialog = find(By.xpath("//input[@data-action-type='DISMISS' and @class='a-button-input' and @type='submit']"));

        if (toasterDialog.isCurrentlyVisible()) {
            withAction().moveToElement(toasterDialog).click().perform();
        }

        this.getDriver().switchTo().defaultContent();

        ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].scrollIntoView(true);", departmentTab);

        Select selectOption = new Select(departmentTab);
        selectOption.selectByVisibleText(department);
        this.getDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        submitSearch();
    }

    public void submitSearch() {
        WebElementFacade btnSubmit = find(By.xpath("//input[@value='Go']"));
        btnSubmit.click();
    }

    public Boolean isInCategoryPage(String category) {
        String locator = "//h1[contains(text(),'" + category + "')]";
        ((JavascriptExecutor) this.getDriver()).executeScript("window.scrollTo(0,0);");

        return find(By.xpath(locator)).isCurrentlyVisible();
    }

    public void clickSubDepartment(String subDepartment) {
        String locator = "//ul[contains(@class,'a-unordered-list')]//span[contains(.,'" + subDepartment + "')]";
        WebElementFacade specifiedDepartment = find(By.xpath(locator));

        ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].scrollIntoView(true);", specifiedDepartment);

        withAction().moveToElement(specifiedDepartment).click().perform();
    }

    public Boolean isInSearchPage(String query) {
        String locator = "//span[@class='a-color-state a-text-bold'][contains(text(),'" + query + "')]";
        ((JavascriptExecutor) this.getDriver()).executeScript("window.scrollTo(0,0);");

        this.getDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        return find(By.xpath(locator)).isCurrentlyVisible();
    }

    public void filterBy(String option, String value) {
        String locator = "//div[@id='leftNav']//h4[contains(text(),'" + option + "')]/following-sibling::ul//span[contains(text(),'" + value + "')]";
        WebElementFacade searchFilter = find(By.xpath(locator));

        ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].scrollIntoView(true);", searchFilter);

        withAction().moveToElement(searchFilter).click().perform();

        this.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void sortResult(String sortOption) {
        String locator = "//select[@id='sort']";
        WebElementFacade sorter = find(By.xpath(locator));

        ((JavascriptExecutor) this.getDriver()).executeScript("window.scrollTo(0,0);");

        this.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        withAction().moveToElement(sorter).click()
                .moveToElement(sorter.then().find(By.xpath("//option"))).perform();

        this.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Select selectOption = new Select(sorter);

        selectOption.selectByVisibleText(sortOption);
        this.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void populateSearchResult() {
        ResponseData responseData = ResponseData.getInstance();
        String locator = "//ul[@id='s-results-list-atf']//li[contains(@class,'s-result-item')]//div[contains(@class,'a-col-right')]";
        List<WebElementFacade> listProductResult = findAll(By.xpath(locator));
        List<ProductDetails> result = new ArrayList<>();

        for (int i = 0; i< listProductResult.size(); i++) {
            WebElementFacade item = listProductResult.get(i);
            ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].scrollIntoView(true);", item);
            this.getDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            ProductDetails productDetails = new ProductDetails();
            String code = item.findElement(By.xpath("..//..//..//..")).getAttribute("data-asin");
            productDetails.setCode(code);
            WebElement title = item.findElement(By.xpath("//div[contains(@class,'a-row')]//div[contains(@class,'a-row')]/*[descendant::h2]//a"));
            productDetails.setElement(title);
            String name = title.getAttribute("title");
            productDetails.setName(name);
            String url = title.getAttribute("href");
            productDetails.setUrl(url);
            WebElementFacade whole = item.then().find(By.xpath("//span[contains(@class,'sx-price-whole')]"));
            Double wholePrice = 0.0;
            if (whole.isCurrentlyVisible()) {
                wholePrice = Double.valueOf(whole.getText());
            }
            WebElementFacade fract = item.then().find(By.xpath("//sup[contains(@class,'sx-price-fractional')]"));
            Double fractional = 0.0;
            if (fract.isCurrentlyVisible()) {
                fractional = Double.valueOf(fract.getText());
            }
            Double price = wholePrice + (fractional * 0.01);
            productDetails.setPrice(price);
            List<WebElementFacade> infos = item.thenFindAll(By.cssSelector("dl.a-definition-list > li > span > span.a-letter-space"));
            Map<String, String> mappedInfo = new HashMap<>();

            for (WebElementFacade info : infos) {
                String key = info.then().findElement(By.xpath("//preceding-sibling::span")).getText();
                String value = info.then().findElement(By.xpath("//following-sibling::span")).getText();
                mappedInfo.put(key, value);
            }

            String modelYear = "null";
            if (mappedInfo.containsKey("Model Year:")) {
                modelYear = mappedInfo.get("Model Year:");
            }
            productDetails.setModelYear(modelYear);

            result.add(productDetails);
        }
        try {
            if (responseData.getProductDetailsList().isEmpty()) {
                responseData.setProductDetailsList(result);
            } else {
                responseData.getProductDetailsList().addAll(result);
            }
        } catch (NullPointerException e) {
            responseData.setProductDetailsList(result);
        }
    }

    public List<Map<String, Object>> getNthData(Integer nth) {
        Utility utility = new Utility();
        List<Map<String, Object>> result = utility.getNthItems(nth);

        for (int i = 0; i < result.size(); i++) {
            Map<String, Object> item = result.get(i);
            System.out.print((i + 1) + ".");
            System.out.println(" Product Name : \t" + item.get("name"));
            System.out.println("\t Product Price : \t$" + item.get("price"));
        }
        return result;
    }

    public List<ProductDetails> getItemBelowPriceAndInYear(Double treshold, String modelYear) {
        Utility utility = new Utility();
        List<ProductDetails> result = utility.getItemsLowerThanPriceInYear(treshold, modelYear);

        for (int i = 0; i < result.size(); i++) {
            ProductDetails item = result.get(i);
            System.out.print((i + 1) + ".");
            System.out.println(" Product Code : \t" + item.getCode());
            System.out.println("\t Product Name : \t$" + item.getName());
            System.out.println("\t Product Price : \t$" + item.getPrice());
            System.out.println("\t Product Model Year : \t$" + item.getModelYear());
            System.out.println("\t Product Url : \t$" + item.getUrl());
        }
        return result;
    }

    public void selectFirstItemFromSearchResult() {
        ResponseData responseData = ResponseData.getInstance();
        if (!responseData.getProductDetailsList().isEmpty()) {
            WebElement product = responseData.getProductDetailsList().get(0).getElement();

            ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].scrollIntoView(true);", product);

            withAction().moveToElement(product).click().perform();
        }
    }

    public void addProductToList() {
        String locator = "//input[@id='add-to-wishlist-button-submit']";
        WebElementFacade btnAddToList = find(By.xpath(locator));

        ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].scrollIntoView(true);", btnAddToList);

        withAction().moveToElement(btnAddToList).doubleClick().perform();
        this.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public Boolean isInLoginPage() {
        String locator = "//form[@name='signIn']";
        WebElementFacade signInForm = find(By.xpath(locator));
        this.getDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        return signInForm.isCurrentlyVisible();
    }

    public void typeCredentials(String username, String password) {
        WebElementFacade usernameField = find(By.xpath("//input[@id='ap_email']"));
        WebElementFacade passwordField = find(By.xpath("//input[@id='ap_password']"));

        usernameField.type(username);
        passwordField.type(password);
    }

    public void hitBtnLogin() {
        WebElementFacade btnLogin = find(By.xpath("//input[@id='signInSubmit']"));
        ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].scrollIntoView(true);", btnLogin);

        withAction().moveToElement(btnLogin).click().perform();
    }
}
