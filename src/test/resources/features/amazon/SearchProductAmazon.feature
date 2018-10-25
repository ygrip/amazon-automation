@AmazonFeature
Feature: feature to test when user want to  search product and do some filtering and sorting then add it to list in amazon website

  @Positive
  Scenario: user open amazon then search product and add it to list
    Given a user open amazon homepage
    When a user want to search product by department "Electronics"
    Then a user should see "Electronics" category page
    When a user want to search product by sub department "Computers & Accessories"
    Then a user should see "Computers & Accessories" search page
    When a user want to search product by department "Electronics"
    Then a user should see "Electronics" category page
    When a user want to search product by sub department "Office Electronics"
    Then a user should see "Office Electronics" search page
    When a user want to search product by department "Electronics"
    Then a user should see "Electronics" category page
    When a user want to search product by sub department "Television & Video"
    Then a user should see "Television & Video" search page
    When a user want to search product by sub department "Televisions"
    Then a user should see "Televisions" search page
    When a user want to filter search result by "Screen Size" with option "32 Inches & Under"
    And a user want to sort result to "Price: High to Low"
    And a user want to sort result to "Price: Low to High"
    Then a system should populate search result
    And a system should return "10" nth items
    And a system should return all items with price below "150" and model year is "2017"
    When a user want to click one product from search result
    Then a user should see specified product details page
    When a user want to add product to list
    Then a user should see a sign in notification
    When a user type username "random" and password "random"
    And a user hit the sign in button
