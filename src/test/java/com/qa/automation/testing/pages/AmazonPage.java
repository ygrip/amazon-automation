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

        this.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        selectOption.selectByVisibleText(sortOption);
    }

    public void populateSearchResult() {
        ResponseData responseData = ResponseData.getInstance();
        String locator = "//ul[@id='s-results-list-atf']//li[contains(@class,'s-result-item')]//div[contains(@class,'a-col-right')]";
        List<WebElementFacade> listProductResult = findAll(By.xpath(locator));
        List<ProductDetails> result = new ArrayList<>();

        for (int i = 0; i< listProductResult.size(); i++) {
            ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].scrollIntoView(true);", listProductResult.get(i));
            this.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            ProductDetails productDetails = new ProductDetails();
            String code = listProductResult.get(i).findElement(By.xpath("..//..//..//..")).getAttribute("data-asin");
            productDetails.setCode(code);
            WebElement title = listProductResult.get(i).findElement(By.className("s-access-detail-page"));
            productDetails.setElement(title);
            String name = title.getAttribute("title");
            productDetails.setName(name);
            String url = title.getAttribute("href");
            productDetails.setUrl(url);
            String rawPrice = listProductResult.get(i).findElement(By.className("a-color-base")).getText();
            rawPrice = rawPrice.replace("$","");
            Double wholePrice = 0.0;
            Double fractional = 0.0;
            String[] compositePrice = rawPrice.split(" ");
            if(compositePrice.length==1){
                wholePrice = Double.valueOf(compositePrice[0]);
            }else if(compositePrice.length==2){
                wholePrice = Double.valueOf(compositePrice[0]);
                fractional = Double.valueOf(compositePrice[1]);
            }else{
                wholePrice = Double.valueOf(compositePrice[1]);
                fractional = Double.valueOf(compositePrice[2]);
            }
            Double price = wholePrice + (fractional * 0.01);
            productDetails.setPrice(price);
            List<WebElementFacade> infos = listProductResult.get(i).find(By.tagName("dl")).thenFindAll(By.cssSelector(".a-letter-space"));
            Map<String, String> mappedInfo = new HashMap<>();

            for (WebElementFacade info : infos) {
                String key = info.find(By.xpath("..//..")).thenFindAll(By.cssSelector("span > span")).get(0).getText();
                String value = info.find(By.xpath("..//..")).thenFindAll(By.cssSelector("span > span")).get(2).getText();
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
            System.out.println("  Product Name : \t" + item.get("name"));
            System.out.println("\tProduct Price : \t$" + item.get("price"));
        }
        return result;
    }

    public List<ProductDetails> getItemBelowPriceAndInYear(Double treshold, String modelYear) {
        Utility utility = new Utility();
        List<ProductDetails> result = utility.getItemsLowerThanPriceInYear(treshold, modelYear);

        for (int i = 0; i < result.size(); i++) {
            ProductDetails item = result.get(i);
            System.out.print((i + 1) + ".");
            System.out.println("  Product Code \t: \t" + item.getCode());
            System.out.println("\tProduct Name \t: \t" + item.getName());
            System.out.println("\tProduct Price \t: \t$" + item.getPrice());
            System.out.println("\tProduct Model Year : \t" + item.getModelYear());
            System.out.println("\tProduct Url \t: \t" + item.getUrl());
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
        this.getDriver().manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        this.getDriver().close();
    }
}
