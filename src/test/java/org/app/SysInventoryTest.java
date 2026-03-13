package org.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.logic.Product;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SysInventory")
class SysInventoryTest {

    private SysInventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new SysInventory();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    /** Registers a valid product and asserts it succeeded. */
    private void registerValid(String code, String name, double price, int qty) {
        assertTrue(inventory.registerProduct(code, name, price, qty),
                "Expected registration of '" + code + "' to succeed");
    }

    // ── registerProduct ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("registerProduct()")
    class RegisterProduct {

        @Test
        @DisplayName("Valid product is registered successfully")
        void validRegistration() {
            assertTrue(inventory.registerProduct("PROD01", "Laptop", 999.99, 10));
            assertEquals(1, inventory.getTotalProducts());
        }

        @Test
        @DisplayName("Product with zero quantity is registered successfully")
        void zeroQuantityAllowed() {
            assertTrue(inventory.registerProduct("PROD01", "Laptop", 999.99, 0));
        }

        @Test
        @DisplayName("Code is stored in uppercase")
        void codeStoredUppercase() {
            inventory.registerProduct("prod01", "Laptop", 10.0, 5);
            Optional<Product> found = inventory.searchProductByCode("PROD01");
            assertTrue(found.isPresent());
        }

        @Test
        @DisplayName("Duplicate code returns false and is not added")
        void duplicateCode() {
            registerValid("PROD01", "Laptop", 999.99, 10);
            assertFalse(inventory.registerProduct("PROD01", "Other", 1.0, 1));
            assertEquals(1, inventory.getTotalProducts());
        }

        @Test
        @DisplayName("Duplicate code is case-insensitive")
        void duplicateCodeCaseInsensitive() {
            registerValid("PROD01", "Laptop", 999.99, 10);
            assertFalse(inventory.registerProduct("prod01", "Other", 1.0, 1));
        }

        @Test
        @DisplayName("Invalid code (special chars) returns false")
        void invalidCode() {
            assertFalse(inventory.registerProduct("PROD-01", "Laptop", 10.0, 5));
            assertEquals(0, inventory.getTotalProducts());
        }

        @Test
        @DisplayName("Null code returns false")
        void nullCode() {
            assertFalse(inventory.registerProduct(null, "Laptop", 10.0, 5));
        }

        @Test
        @DisplayName("Name shorter than 3 characters returns false")
        void shortName() {
            assertFalse(inventory.registerProduct("PROD01", "PC", 10.0, 5));
        }

        @Test
        @DisplayName("Null name returns false")
        void nullName() {
            assertFalse(inventory.registerProduct("PROD01", null, 10.0, 5));
        }

        @Test
        @DisplayName("Zero price returns false")
        void zeroPrice() {
            assertFalse(inventory.registerProduct("PROD01", "Laptop", 0.0, 5));
        }

        @Test
        @DisplayName("Negative price returns false")
        void negativePrice() {
            assertFalse(inventory.registerProduct("PROD01", "Laptop", -1.0, 5));
        }

        @Test
        @DisplayName("Negative quantity returns false")
        void negativeQuantity() {
            assertFalse(inventory.registerProduct("PROD01", "Laptop", 10.0, -1));
        }

        @Test
        @DisplayName("Low stock alert is generated when quantity is at threshold")
        void lowStockAlertOnRegister() {
            registerValid("PROD01", "Laptop", 10.0, 5);
            assertEquals(1, inventory.getTotalAlerts());
        }

        @Test
        @DisplayName("No alert when quantity is above threshold")
        void noAlertAboveThreshold() {
            registerValid("PROD01", "Laptop", 10.0, 10);
            assertEquals(0, inventory.getTotalAlerts());
        }
    }

    // ── augmentStock ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("augmentStock()")
    class AugmentStock {

        @BeforeEach
        void registerProduct() {
            registerValid("PROD01", "Laptop", 999.99, 10);
        }

        @Test
        @DisplayName("Stock increases by the given amount")
        void stockIncreases() {
            assertTrue(inventory.augmentStock("PROD01", 5));
            assertEquals(15, inventory.searchProductByCode("PROD01").get().getQuantity());
        }

        @Test
        @DisplayName("Augmenting by 1 unit works")
        void augmentByOne() {
            assertTrue(inventory.augmentStock("PROD01", 1));
            assertEquals(11, inventory.searchProductByCode("PROD01").get().getQuantity());
        }

        @Test
        @DisplayName("Non-existent code returns false")
        void nonExistentCode() {
            assertFalse(inventory.augmentStock("FAKE", 5));
        }

        @Test
        @DisplayName("Zero quantity returns false")
        void zeroQuantity() {
            assertFalse(inventory.augmentStock("PROD01", 0));
        }

        @Test
        @DisplayName("Negative quantity returns false")
        void negativeQuantity() {
            assertFalse(inventory.augmentStock("PROD01", -1));
        }
    }

    // ── reduceStock ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("reduceStock()")
    class ReduceStock {

        @BeforeEach
        void registerProduct() {
            registerValid("PROD01", "Laptop", 999.99, 10);
        }

        @Test
        @DisplayName("Stock decreases by the given amount")
        void stockDecreases() {
            assertTrue(inventory.reduceStock("PROD01", 4));
            assertEquals(6, inventory.searchProductByCode("PROD01").get().getQuantity());
        }

        @Test
        @DisplayName("Reducing to zero is allowed")
        void reduceToZero() {
            assertTrue(inventory.reduceStock("PROD01", 10));
            assertEquals(0, inventory.searchProductByCode("PROD01").get().getQuantity());
        }

        @Test
        @DisplayName("Reducing below zero returns false")
        void reduceBelowZero() {
            assertFalse(inventory.reduceStock("PROD01", 11));
            assertEquals(10, inventory.searchProductByCode("PROD01").get().getQuantity());
        }

        @Test
        @DisplayName("Non-existent code returns false")
        void nonExistentCode() {
            assertFalse(inventory.reduceStock("FAKE", 1));
        }

        @Test
        @DisplayName("Zero quantity returns false")
        void zeroQuantity() {
            assertFalse(inventory.reduceStock("PROD01", 0));
        }

        @Test
        @DisplayName("Negative quantity returns false")
        void negativeQuantity() {
            assertFalse(inventory.reduceStock("PROD01", -3));
        }

        @Test
        @DisplayName("Low stock alert is generated when quantity drops to threshold")
        void alertOnLowStock() {
            inventory.reduceStock("PROD01", 6); // 10 → 4, triggers alert
            assertTrue(inventory.getTotalAlerts() > 0);
        }
    }

    // ── validateInventory ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("validateInventory()")
    class ValidateInventory {

        @BeforeEach
        void registerProduct() {
            registerValid("PROD01", "Laptop", 999.99, 10);
        }

        @Test
        @DisplayName("Returns true when stock equals required quantity")
        void exactStock() {
            assertTrue(inventory.validateInventory("PROD01", 10));
        }

        @Test
        @DisplayName("Returns true when stock exceeds required quantity")
        void surplusStock() {
            assertTrue(inventory.validateInventory("PROD01", 5));
        }

        @Test
        @DisplayName("Returns false when stock is insufficient")
        void insufficientStock() {
            assertFalse(inventory.validateInventory("PROD01", 11));
        }

        @Test
        @DisplayName("Returns false for non-existent product")
        void nonExistentProduct() {
            assertFalse(inventory.validateInventory("FAKE", 1));
        }

        @Test
        @DisplayName("Returns false for zero required quantity")
        void zeroRequired() {
            assertFalse(inventory.validateInventory("PROD01", 0));
        }

        @Test
        @DisplayName("Returns false for negative required quantity")
        void negativeRequired() {
            assertFalse(inventory.validateInventory("PROD01", -1));
        }
    }

    // ── searchProductByCode ───────────────────────────────────────────────────

    @Nested
    @DisplayName("searchProductByCode()")
    class SearchProductByCode {

        @Test
        @DisplayName("Finds product by exact code")
        void foundByExactCode() {
            registerValid("PROD01", "Laptop", 10.0, 5);
            assertTrue(inventory.searchProductByCode("PROD01").isPresent());
        }

        @Test
        @DisplayName("Search is case-insensitive")
        void caseInsensitive() {
            registerValid("PROD01", "Laptop", 10.0, 5);
            assertTrue(inventory.searchProductByCode("prod01").isPresent());
        }

        @Test
        @DisplayName("Returns empty Optional for unknown code")
        void notFound() {
            assertTrue(inventory.searchProductByCode("FAKE").isEmpty());
        }
    }

    // ── getProductsLowStock ───────────────────────────────────────────────────

    @Nested
    @DisplayName("getProductsLowStock()")
    class GetProductsLowStock {

        @Test
        @DisplayName("Returns only products with low stock")
        void returnsLowStockOnly() {
            registerValid("P1", "ItemA", 10.0, 3);  // low
            registerValid("P2", "ItemB", 10.0, 20); // ok
            registerValid("P3", "ItemC", 10.0, 5);  // low (at threshold)

            List<Product> low = inventory.getProductsLowStock();
            assertEquals(2, low.size());
            assertTrue(low.stream().allMatch(p -> p.getQuantity() <= 5));
        }

        @Test
        @DisplayName("Returns empty list when no products have low stock")
        void emptyWhenNoLowStock() {
            registerValid("P1", "ItemA", 10.0, 50);
            assertTrue(inventory.getProductsLowStock().isEmpty());
        }

        @Test
        @DisplayName("Returns empty list when inventory is empty")
        void emptyInventory() {
            assertTrue(inventory.getProductsLowStock().isEmpty());
        }
    }

    // ── clearAlerts ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("clearAlerts()")
    class ClearAlerts {

        @Test
        @DisplayName("Alert count is zero after clearing")
        void alertsCleared() {
            registerValid("P1", "ItemA", 10.0, 3); // triggers alert
            assertTrue(inventory.getTotalAlerts() > 0);
            inventory.clearAlerts();
            assertEquals(0, inventory.getTotalAlerts());
        }

        @Test
        @DisplayName("Clearing an already empty alerts list does not throw")
        void clearEmptyAlerts() {
            assertDoesNotThrow(() -> inventory.clearAlerts());
        }
    }

    // ── getProducts ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getProducts()")
    class GetProducts {

        @Test
        @DisplayName("Returns empty list on new inventory")
        void emptyOnInit() {
            assertTrue(inventory.getProducts().isEmpty());
        }

        @Test
        @DisplayName("Returns all registered products")
        void returnsAllProducts() {
            registerValid("P1", "ItemA", 10.0, 5);
            registerValid("P2", "ItemB", 20.0, 8);
            assertEquals(2, inventory.getProducts().size());
        }

        @Test
        @DisplayName("Returned list is unmodifiable")
        void listIsUnmodifiable() {
            registerValid("P1", "ItemA", 10.0, 5);
            List<Product> products = inventory.getProducts();
            assertThrows(UnsupportedOperationException.class,
                    () -> products.add(new Product("XX", "Fake", 1.0, 1)));
        }
    }
}