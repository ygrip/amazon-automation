package com.qa.automation.testing.steps;

import com.qa.automation.testing.data.ResponseData;
import com.qa.automation.testing.steps.serenity.EndUserSteps;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DefinitionSteps {

    @Steps
    EndUserSteps user;

    @Given("^a user open amazon homepage$")
    public void aUserOpenAmazonHomepage() {
        user.isInHomepage();
    }

    @When("^a user want to search product by department \"([^\"]*)\"$")
    public void aUserWantToSearchProductByDepartment(String department) {
        user.searchByDepartments(department);
    }

    @Then("^a user should see \"([^\"]*)\" category page$")
    public void aUserShouldSeeCategoryPage(String category) {
        Boolean actual = user.isInCategoryPage(category);
        assertThat(String.format("user is not redirected to %s category page", category), actual, equalTo(true));
    }

    @When("^a user want to search product by sub department \"([^\"]*)\"$")
    public void aUserWantToSearchProductBySubDepartment(String subDepartments) {
        user.clickSubDepartment(subDepartments);
    }

    @Then("^a user should see \"([^\"]*)\" search page$")
    public void aUserShouldSeeSearchPage(String query) {
        Boolean actual = user.isInSearchPage(query);
        assertThat(String.format("user is not redirected to %s search result page", query), actual, equalTo(true));
    }

    @When("^a user want to filter search result by \"([^\"]*)\" with option \"([^\"]*)\"$")
    public void aUserWantToFilterSearchResultByWithOption(String option, String value) {
        user.filterBy(option, value);
    }

    @And("^a user want to sort result to \"([^\"]*)\"$")
    public void aUserWantToSortResultTo(String sortOption) {
        user.sortBy(sortOption);
    }

    @Then("^a system should populate search result$")
    public void aSystemShouldPopulateSearchResult() {
        user.populateSearchResult();
    }

    @And("^a system should return \"([^\"]*)\" nth items$")
    public void aSystemShouldReturnNthItems(Integer n) {
        user.getNthData(n);
    }

    @And("^a system should return all items with price below \"([^\"]*)\" and model year is \"([^\"]*)\"$")
    public void aSystemShouldReturnAllItemsWithPriceBelowAndModelYearIs(Double treshold, String modelYear) {
        user.getItemWithPriceLowerThanAndModelYearIn(treshold, modelYear);
    }

    @When("^a user want to click one product from search result$")
    public void aUserWantToClickOneProductFromSearchResult() {
        user.selectFirstItemFromSearchResult();
    }

    @Then("^a user should see specified product details page$")
    public void aUserShouldSeeSpecifiedProductDetailsPage() {
        ResponseData responseData = ResponseData.getInstance();
        String expected = responseData.getProductDetailsList().get(0).getUrl();
        String actual = user.currentUrl();
        assertThat(String.format("user not redirected to %s", expected), actual, equalTo(expected));
    }

    @When("^a user want to add product to list$")
    public void aUserWantToAddProductToList() {
        user.addProductToList();
    }

    @Then("^a user should see a sign in notification$")
    public void aUserShouldSeeASignInNotification() {
        Boolean actual = user.isInLoginPage();
        assertThat(String.format("user is not redirected to Login page"), actual, equalTo(true));
    }

    @When("^a user type username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void aUserTypeUsernameAndPassword(String username, String password) {
        user.typeCredentials(username, password);
    }

    @And("^a user hit the sign in button$")
    public void aUserHitTheSignInButton() {
        user.hitBtnLogin();
    }
}
