@e2e
Feature: User Authentication - E2E

  As a store manager
  I want to authenticate securely and manage inventory
  So that only authorized users can operate the system

  Scenario: Successful login, product registration and logout
    Given the user is on the login page
    When the user enters username "admin" and password "admin123"
    And the user submits the login form
    Then the user should see the inventory dashboard
    When the user registers a new product with code "E2EPROD", name "E2E Test Item", price "25.99" and stock "15"
    Then the product "E2EPROD" should appear in the inventory table
    When the user logs out
    Then the user should be redirected to the login page

  Scenario: Direct navigation to a protected route without login redirects to the login page
    Given the user is not authenticated
    When the user navigates directly to "/dashboard"
    Then the user should be redirected to the login page
