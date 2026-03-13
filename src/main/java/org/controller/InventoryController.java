package org.controller;

import org.app.SysInventory;
import org.dto.Dto.*;
import org.logic.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * InventoryController — exposes the SysInventory logic as a REST API.
 *
 * Base path: /api
 *
 * Endpoints:
 *   GET    /api/products                          → list all products
 *   POST   /api/products                          → register new product
 *   GET    /api/products/{code}                   → find product by code
 *   PATCH  /api/products/{code}/stock             → augment or reduce stock
 *   GET    /api/products/{code}/validate?qty=N    → validate enough stock
 *   GET    /api/alerts                            → list active alerts
 *   DELETE /api/alerts                            → clear alerts
 */
@RestController
@RequestMapping("/api")
public class InventoryController {

    private final SysInventory inventory;

    public InventoryController(SysInventory inventory) {
        this.inventory = inventory;
    }

    // ── Products ──────────────────────────────────────────────────────────────

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = inventory.getProducts();
        return ResponseEntity.ok(
                ApiResponse.ok("Products retrieved successfully", products)
        );
    }

    @GetMapping("/products/{code}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable String code) {
        Optional<Product> product = inventory.searchProductByCode(code);
        return product
                .map(p -> ResponseEntity.ok(ApiResponse.ok("Product found", p)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(ApiResponse.error("Product with code '" + code + "' not found")));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<Void>> registerProduct(@RequestBody RegisterRequest req) {
        boolean ok = inventory.registerProduct(req.code(), req.name(), req.price(), req.quantity());
        if (ok) {
            return ResponseEntity.status(201)
                    .body(ApiResponse.ok("Product '" + req.name() + "' registered successfully"));
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Could not register product. Check the fields and try again."));
    }

    // ── Stock ─────────────────────────────────────────────────────────────────

    @PatchMapping("/products/{code}/stock")
    public ResponseEntity<ApiResponse<Product>> updateStock(
            @PathVariable String code,
            @RequestBody StockRequest req) {

        boolean ok = switch (req.operation().toLowerCase()) {
            case "augment" -> inventory.augmentStock(code, req.quantity());
            case "reduce"  -> inventory.reduceStock(code, req.quantity());
            default -> false;
        };

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(
                            "Stock operation failed. Check the code, quantity, and available stock."));
        }

        Product updated = inventory.searchProductByCode(code).orElseThrow();
        String msg = req.operation().equals("augment")
                ? "Stock increased by " + req.quantity() + " units"
                : "Stock reduced by " + req.quantity() + " units";

        return ResponseEntity.ok(ApiResponse.ok(msg, updated));
    }

    @GetMapping("/products/{code}/validate")
    public ResponseEntity<ApiResponse<Void>> validateStock(
            @PathVariable String code,
            @RequestParam int qty) {

        boolean sufficient = inventory.validateInventory(code, qty);
        if (sufficient) {
            return ResponseEntity.ok(
                    ApiResponse.ok("Sufficient stock available (" + qty + " units)"));
        }
        return ResponseEntity.ok(
                ApiResponse.error("Insufficient stock or product not found"));
    }

    // ── Alerts ────────────────────────────────────────────────────────────────

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<Product>>> getAlerts() {
        List<Product> lowStock = inventory.getProductsLowStock();
        String msg = lowStock.isEmpty()
                ? "No active alerts"
                : lowStock.size() + " product(s) with low stock";
        return ResponseEntity.ok(ApiResponse.ok(msg, lowStock));
    }

    @DeleteMapping("/alerts")
    public ResponseEntity<ApiResponse<Void>> clearAlerts() {
        inventory.clearAlerts();
        return ResponseEntity.ok(ApiResponse.ok("Alerts cleared"));
    }

    // ── Stats (bonus endpoint for the dashboard) ──────────────────────────────

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<java.util.Map<String, Object>>> getStats() {
        List<Product> products = inventory.getProducts();
        long lowStockCount = products.stream()
                .filter(p -> p.getQuantity() <= org.logic.Validator.MINIMUM_STOCK_ALERT)
                .count();
        double totalValue = products.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();

        java.util.Map<String, Object> stats = java.util.Map.of(
                "totalProducts", products.size(),
                "lowStockCount", lowStockCount,
                "totalValue",    totalValue,
                "totalAlerts",   inventory.getTotalAlerts()
        );
        return ResponseEntity.ok(ApiResponse.ok("Stats retrieved", stats));
    }
}