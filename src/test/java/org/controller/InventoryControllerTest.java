package org.controller;
import org.app.SysInventory;
import org.dto.Dto.ApiResponse;
import org.dto.Dto.RegisterRequest;
import org.dto.Dto.StockRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import org.logic.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for InventoryController.
 *
 * No Spring context is loaded — the controller is instantiated directly
 * with a real SysInventory, keeping tests fast and dependency-free.
 * Each test focuses on the HTTP status code and the ApiResponse payload.
 */
@DisplayName("InventoryController")
class InventoryControllerTest {
    private SysInventory inventory;
    private InventoryController controller;
    @BeforeEach
    void setUp() {
        // Fresh inventory and controller for every test — no shared state
        inventory = new SysInventory();
        controller = new InventoryController(inventory);
    }
    // ── Helper ────────────────────────────────────────────────────────────────
    /** Registers a valid product directly through the inventory. */
    private void registerValid(String code, String name, double price, int qty) {
        inventory.registerProduct(code, name, price, qty);
    }
    // ── GET /api/products ─────────────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/products")
    class GetAllProducts {
        @Test
        @DisplayName("Returns 200 and empty list when no products exist")
        void emptyInventory() {
            ResponseEntity<ApiResponse<List<Product>>> response = controller.getAllProducts();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
            assertTrue(response.getBody().data().isEmpty());
        }
        @Test
        @DisplayName("Returns 200 and all registered products")
        void withProducts() {
            registerValid("P1", "Laptop", 10.0, 20);
            registerValid("P2", "Mouse",  5.0,  8);
            ResponseEntity<ApiResponse<List<Product>>> response = controller.getAllProducts();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, response.getBody().data().size());
        }
    }
    // ── GET /api/products/{code} ──────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/products/{code}")
    class GetProduct {
        @Test
        @DisplayName("Returns 200 and product when code exists")
        void productFound() {
            registerValid("PROD01", "Laptop", 999.99, 10);
            ResponseEntity<ApiResponse<Product>> response = controller.getProduct("PROD01");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
            assertEquals("PROD01", response.getBody().data().getCode());
        }
        @Test
        @DisplayName("Returns 404 when code does not exist")
            // Covers the orElseGet branch that returns 404
        void productNotFound() {
            ResponseEntity<ApiResponse<Product>> response = controller.getProduct("FAKE");
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertFalse(response.getBody().success());
        }
        @Test
        @DisplayName("Search is case-insensitive")
        void caseInsensitiveSearch() {
            registerValid("PROD01", "Laptop", 999.99, 10);
            ResponseEntity<ApiResponse<Product>> response = controller.getProduct("prod01");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
        }
    }
    // ── POST /api/products ────────────────────────────────────────────────────
    @Nested
    @DisplayName("POST /api/products")
    class RegisterProduct {
        @Test
        @DisplayName("Returns 201 when product is registered successfully")
        void validRegistration() {
            RegisterRequest req = new RegisterRequest("PROD01", "Laptop", 999.99, 10);
            ResponseEntity<ApiResponse<Void>> response = controller.registerProduct(req);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertTrue(response.getBody().success());
        }
        @Test
        @DisplayName("Returns 400 when registration fails due to invalid data")
            // Covers the badRequest branch when registerProduct returns false
        void invalidRegistration() {
            RegisterRequest req = new RegisterRequest("PROD-01", "Laptop", 999.99, 10);
            ResponseEntity<ApiResponse<Void>> response = controller.registerProduct(req);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().success());
        }
        @Test
        @DisplayName("Returns 400 when product code already exists")
        void duplicateCode() {
            registerValid("PROD01", "Laptop", 999.99, 10);
            RegisterRequest req = new RegisterRequest("PROD01", "Other", 1.0, 5);
            ResponseEntity<ApiResponse<Void>> response = controller.registerProduct(req);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().success());
        }
    }
    // ── PATCH /api/products/{code}/stock ──────────────────────────────────────
    @Nested
    @DisplayName("PATCH /api/products/{code}/stock")
    class UpdateStock {
        @BeforeEach
        void registerProduct() {
            registerValid("PROD01", "Laptop", 999.99, 10);
        }
        @Test
        @DisplayName("Returns 200 and updated product when augmenting stock")
        void augmentStock() {
            StockRequest req = new StockRequest("augment", 5);
            ResponseEntity<ApiResponse<Product>> response = controller.updateStock("PROD01", req);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
            assertEquals(15, response.getBody().data().getQuantity());
        }
        @Test
        @DisplayName("Returns 200 and updated product when reducing stock")
            // Covers the reduce branch and the ternary for the response message
        void reduceStock() {
            StockRequest req = new StockRequest("reduce", 3);
            ResponseEntity<ApiResponse<Product>> response = controller.updateStock("PROD01", req);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
            assertEquals(7, response.getBody().data().getQuantity());
        }
        @ParameterizedTest(name = "Returns 400 for: code={0}, operation={1}, qty={2}")
        @DisplayName("Returns 400 for all invalid stock operations")
        @MethodSource("invalidStockOperations")
        void invalidStockOperationsReturn400(String code, String operation, int quantity) {
            StockRequest req = new StockRequest(operation, quantity);
            ResponseEntity<ApiResponse<Product>> response = controller.updateStock(code, req);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assert response.getBody() != null;
            assertFalse(response.getBody().success());
        }

        static Stream<Arguments> invalidStockOperations() {
            return Stream.of(
                    Arguments.of("PROD01", "delete", 5),    // unknown operation
                    Arguments.of("PROD01", "reduce", 999),  // insufficient stock
                    Arguments.of("FAKE", "augment", 5)      // non-existent product
            );
        }
    }
    // ── GET /api/products/{code}/validate ─────────────────────────────────────
    @Nested
    @DisplayName("GET /api/products/{code}/validate")
    class ValidateStock {
        @BeforeEach
        void registerProduct() {
            registerValid("PROD01", "Laptop", 999.99, 10);
        }
        @Test
        @DisplayName("Returns 200 with success true when stock is sufficient")
        void sufficientStock() {
            ResponseEntity<ApiResponse<Void>> response = controller.validateStock("PROD01", 5);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
        }
        @Test
        @DisplayName("Returns 200 with success false when stock is insufficient")
            // Covers the error branch — note both cases return 200, result differs in body
        void insufficientStock() {
            ResponseEntity<ApiResponse<Void>> response = controller.validateStock("PROD01", 99);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().success());
        }
        @Test
        @DisplayName("Returns 200 with success false when product does not exist")
        void nonExistentProduct() {
            ResponseEntity<ApiResponse<Void>> response = controller.validateStock("FAKE", 1);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().success());
        }
    }
    // ── GET /api/alerts ───────────────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/alerts")
    class GetAlerts {
        @Test
        @DisplayName("Returns 200 and empty list when no alerts exist")
            // Covers the lowStock.isEmpty() ternary branch
        void noAlerts() {
            registerValid("P1", "Laptop", 10.0, 50); // above threshold, no alert
            ResponseEntity<ApiResponse<List<Product>>> response = controller.getAlerts();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
            assertTrue(response.getBody().data().isEmpty());
        }
        @Test
        @DisplayName("Returns 200 and low-stock products when alerts exist")
            // Covers the non-empty ternary branch for the message
        void withAlerts() {
            registerValid("P1", "Laptop", 10.0, 3); // below threshold, triggers alert
            ResponseEntity<ApiResponse<List<Product>>> response = controller.getAlerts();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().data().size());
        }
    }
    // ── DELETE /api/alerts ────────────────────────────────────────────────────
    @Nested
    @DisplayName("DELETE /api/alerts")
    class ClearAlerts {
        @Test
        @DisplayName("Returns 200 and clears all alerts")
        void clearsAlerts() {
            registerValid("P1", "Laptop", 10.0, 3); // triggers alert
            assertTrue(inventory.getTotalAlerts() > 0);
            ResponseEntity<ApiResponse<Void>> response = controller.clearAlerts();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
            assertEquals(0, inventory.getTotalAlerts());
        }
    }
    // ── GET /api/stats ────────────────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/stats")
    class GetStats {
        @Test
        @DisplayName("Returns 200 with correct stats when inventory is empty")
        void emptyInventory() {
            ResponseEntity<ApiResponse<Map<String, Object>>> response = controller.getStats();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().success());
            assertEquals(0, response.getBody().data().get("totalProducts"));
        }
        @Test
        @DisplayName("Returns correct totalProducts, lowStockCount and totalValue")
            // Covers both stream operations in getStats
        void withProducts() {
            registerValid("P1", "Laptop", 100.0, 20); // ok stock,  value = 2000
            registerValid("P2", "Mouse",   10.0,  3); // low stock, value =   30
            ResponseEntity<ApiResponse<Map<String, Object>>> response = controller.getStats();
            Map<String, Object> data = response.getBody().data();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2,    data.get("totalProducts"));
            assertEquals(1L,   data.get("lowStockCount"));
            assertEquals(2030.0, data.get("totalValue"));
        }
    }
}
