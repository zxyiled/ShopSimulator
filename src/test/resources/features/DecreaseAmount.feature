Feature: Decrease stock amount

  Scenario: Successful stock decrease
    Given a product exists with quantity 10
    When I decrease stock for quantity 5
    Then the stock should be 5

