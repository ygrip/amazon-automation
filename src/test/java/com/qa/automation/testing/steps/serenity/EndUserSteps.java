package com.qa.automation.testing.steps.serenity;

import com.qa.automation.testing.helper.Utility;
import com.qa.automation.testing.pages.AmazonPage;
import net.thucydides.core.annotations.Step;
import org.springframework.beans.factory.annotation.Autowired;

public class EndUserSteps {
    @Autowired
    AmazonPage amazonPage;

    @Autowired
    Utility utility;

    @Step
    public void isInHomepage() {
        amazonPage.openAt("http://www.amazon.com");
    }

    @Step
    public void searchByDepartments(String department) {
        amazonPage.selectDepartmets(department);
    }

    @Step
    public Boolean isInCategoryPage(String category) {
        return amazonPage.isInCategoryPage(category);
    }

    @Step
    public void clickSubDepartment(String subDepartment) {
        amazonPage.clickSubDepartment(subDepartment);
    }

    @Step
    public Boolean isInSearchPage(String query) {
        return amazonPage.isInSearchPage(query);
    }

    @Step
    public void filterBy(String option, String value) {
        amazonPage.filterBy(option, value);
    }

    @Step
    public void sortBy(String sortOption) {
        amazonPage.sortResult(sortOption);
    }

    @Step
    public void populateSearchResult() {
        amazonPage.populateSearchResult();
    }

    @Step
    public void getNthData(Integer n) {
        amazonPage.getNthData(n);
    }

    @Step
    public void getItemWithPriceLowerThanAndModelYearIn(Double treshold, String modelYear) {
        amazonPage.getItemBelowPriceAndInYear(treshold, modelYear);
    }

    @Step
    public void selectFirstItemFromSearchResult() {
        amazonPage.selectFirstItemFromSearchResult();
    }

    @Step
    public String currentUrl() {
        return amazonPage.getDriver().getCurrentUrl();
    }

    @Step
    public void addProductToList() {
        amazonPage.addProductToList();
    }

    @Step
    public Boolean isInLoginPage() {
        return amazonPage.isInLoginPage();
    }

    @Step
    public void typeCredentials(String username, String password) {
        amazonPage.typeCredentials(username, password);
    }

    @Step
    public void hitBtnLogin() {
        amazonPage.hitBtnLogin();
    }
}