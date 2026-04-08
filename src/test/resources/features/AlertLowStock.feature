Feature: Low Stock Alert System

  As a store manager
  I want to be notified when product stock is running low
  So I can reorder products before they run out

  Scenario: Alert is displayed when stock falls below minimum threshold
    Given a product for low stock alert has 3 items in stock
    When the system checks the stock level
    Then it should display a low stock alert

