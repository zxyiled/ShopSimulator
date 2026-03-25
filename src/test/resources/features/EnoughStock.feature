Feature: Validate if Stock is Enough

  Scenario: Successful validation when stock is sufficient

    Given a test product exists with quantity 10
    When I validate stock for quantity 5
    Then the validation should be successful

