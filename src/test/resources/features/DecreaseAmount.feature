Feature: Stock Management System

  As a store manager
  I want to decrease product stock when items are sold
  So I can maintain accurate inventory records

  Scenario: Stock is successfully decreased after a sale
    Given a product has 10 items in stock
    When 5 items are sold from inventory
    Then the product should have 5 items remaining

