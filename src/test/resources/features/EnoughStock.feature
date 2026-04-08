Feature: Stock Availability Validation

  As a store manager
  I want to verify that enough stock exists before processing orders
  So I can prevent overselling and maintain customer satisfaction

  Scenario: System confirms sufficient stock for order
    Given a product has 10 items available in inventory
    When a customer requests 5 items
    Then the system should confirm the stock is sufficient

