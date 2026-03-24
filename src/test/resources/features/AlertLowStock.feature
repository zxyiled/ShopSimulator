Feature: Show an Alert when the stock is low


  Scenario: Successful alert when stock is below minimum

    Given a product exists with quantity 3
    When i validate stock for quantity 5
    Then an alert should be shown

