Feature: Show an Alert when the stock is low

  Scenario: Successful alert when stock is below minimum

    Given an alert scenario with product quantity 3
    When I validate the stock for quantity 5
    Then an alert should be shown

