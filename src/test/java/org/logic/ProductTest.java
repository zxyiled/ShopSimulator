package org.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product")
class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("PROD01", "Laptop", 999.99, 10);
    }

    // ── Constructor & Getters ─────────────────────────────────────────────────

    @Nested
    @DisplayName("Constructor and getters")
    class ConstructorAndGetters {

        @Test
        @DisplayName("getCode() returns the code set in constructor")
        void getCode() {
            assertEquals("PROD01", product.getCode());
        }

        @Test
        @DisplayName("getName() returns the name set in constructor")
        void getName() {
            assertEquals("Laptop", product.getName());
        }

        @Test
        @DisplayName("getPrice() returns the price set in constructor")
        void getPrice() {
            assertEquals(999.99, product.getPrice());
        }

        @Test
        @DisplayName("getQuantity() returns the quantity set in constructor")
        void getQuantity() {
            assertEquals(10, product.getQuantity());
        }
    }

    // ── setQuantity ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("setQuantity()")
    class SetQuantity {

        @Test
        @DisplayName("Updates quantity to a positive value")
        void setPositiveQuantity() {
            product.setQuantity(25);
            assertEquals(25, product.getQuantity());
        }

        @Test
        @DisplayName("Updates quantity to zero")
        void setZeroQuantity() {
            product.setQuantity(0);
            assertEquals(0, product.getQuantity());
        }

        @Test
        @DisplayName("Quantity can be updated multiple times")
        void multipleUpdates() {
            product.setQuantity(5);
            product.setQuantity(50);
            assertEquals(50, product.getQuantity());
        }
    }

    // ── toString ──────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("toString()")
    class ToString {

        @Test
        @DisplayName("Contains the product code")
        void containsCode() {
            assertTrue(product.toString().contains("PROD01"));
        }

        @Test
        @DisplayName("Contains the product name")
        void containsName() {
            assertTrue(product.toString().contains("Laptop"));
        }

        @Test
        @DisplayName("Contains the quantity")
        void containsQuantity() {
            assertTrue(product.toString().contains("10"));
        }

        @Test
        @DisplayName("Contains the formatted price")
        void containsPrice() {
            assertTrue(product.toString().contains("999.99"));
        }
    }
}