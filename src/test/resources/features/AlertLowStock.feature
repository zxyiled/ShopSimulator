Feature: Show an Alert when the stock is low

  Scenario: Successful alert when stock is below minimum

    Given an alert scenario with product quantity 3
    When I check if stock is low
    Then an alert should be shown

