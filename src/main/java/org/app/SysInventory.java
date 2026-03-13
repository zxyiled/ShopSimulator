package org.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import static org.Main.logger;
import org.logic.Product;
import org.logic.Validator;

public class SysInventory {

    private static final String PRODUCT_EXISTENCE_ERROR = "Product should exist after validation";

    private final List<Product> products;
    private final List<String> alerts;

    public SysInventory() {
        //Initialize empty lists for in-memory storage
        this.products = new ArrayList<>();
        this.alerts = new ArrayList<>();
        logger.info("System initialized with in-memory storage. Products loaded: 0");
    }

    // --- Public Methods ---

    //Register product with validations
    public boolean registerProduct(String code, String name, double price, int quantity) {

        if (!validateAndLogError(Validator.validateCode(code), Validator.ERROR_CODE)) {
            return false;
        }

        if (!validateAndLogError(Validator.validateName(name), Validator.ERROR_NAME)) {
            return false;
        }

        if (!validateAndLogError(Validator.validatePrice(price), Validator.ERROR_PRICE)) {
            return false;
        }

        if (quantity < 0) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(Validator.getMsgError(Validator.ERROR_QUANTITY));
            }
            return false;
        }

        if (!validateAndLogError(Validator.validateProductNonExistent(products, code), Validator.ERROR_EXISTENT)) {
            return false;
        }

        Product newProduct = new Product(code.toUpperCase(), name, price, quantity);
        products.add(newProduct);

        //Auto-save is disabled with in-memory storage

        logger.info("Product registered successfully: " + newProduct.getName() +
                " (Code: " + newProduct.getCode() + ")");

        verifyLowStock(newProduct);
        return true;
    }

    private boolean validateStockOperation(String code, int quantity) {
        if (!validateQuantity(quantity)) {
            return true;
        }

        Optional<Product> product = searchProductByCode(code);
        return product.isEmpty();
    }

    private Product getProductForStockOperation(String code) {
        return searchProductByCode(code)
                .orElseThrow(() -> new IllegalStateException(PRODUCT_EXISTENCE_ERROR));
    }

    //Augment the stock
    public boolean augmentStock(String code, int quantity) {
        if (validateStockOperation(code, quantity)) {
            return false;
        }

        Product product = getProductForStockOperation(code);
        int newStock = product.getQuantity() + quantity;
        return updateProductStock(product, newStock, "increased");
    }

    //Reduce the stock
    public boolean reduceStock(String code, int quantity) {
        if (validateStockOperation(code, quantity)) {
            return false;
        }

        Product product = getProductForStockOperation(code);

        if (!Validator.validateEnoughStock(product, quantity)) {
            logger.warning("{} (Current stock: {})");
            return false;
        }

        int newStock = product.getQuantity() - quantity;
        return updateProductStock(product, newStock, "reduced");
    }

    private boolean validateQuantity(int quantity) {
        if (Validator.isQuantityInvalid(quantity)) {
            String errorMessage = Validator.getMsgError(Validator.ERROR_OPERATION_QUANTITY);
            logger.warning(errorMessage);
            return false;
        }
        return true;
    }

    private boolean updateProductStock(Product product, int newStock, String action) {
        if (newStock < 0) {
            logger.warning("Error: Operation would result in negative stock");
            return false;
        }

        product.setQuantity(newStock);

        String logMessage = String.format(" Stock %s successfully. New stock of %s: %d",
                action, product.getName(), newStock);
        logger.info(logMessage);

        verifyLowStock(product);
        return true;
    }

    //Validate Inventory
    public boolean validateInventory(String code, int requiredQuantity) {
        Optional<Product> productOpt = searchProductByCode(code);

        if (productOpt.isEmpty()) {
            String errorMsg = Validator.getMsgError(Validator.ERROR_NON_EXISTENT);
            logger.warning(errorMsg);
            return false;
        }

        Product product = productOpt.get();

        String errorMessage = Validator.getMsgError(Validator.ERROR_OPERATION_QUANTITY);
        if (Validator.isQuantityInvalid(requiredQuantity)) {
            logger.warning(errorMessage);
            return false;
        }

        boolean enough = Validator.validateEnoughStock(product, requiredQuantity);

        if (enough) {
            logger.info("Inventory validated successfully. Current stock: {} | Required: {}"
            );

        } else {
            logger.warning("{} Current: {} | Required: {}"
            );
        }

        return enough;
    }

    //Show all products
    public void showAllProducts() {
        if (products.isEmpty()) {
            logger.info("Not registered products");
            return;
        }

        if (logger.isLoggable(Level.INFO)) {
            logger.info("=== Product list ===");
            logger.info(String.format("Total: %d products", products.size()));
            products.forEach(p -> {
                String lowStock = Validator.isStockLow(p) ? "Low stock " : "";
                logger.info(p + lowStock);
            });
        }
    }

    //Show alerts
    public void showAlerts() {
        if (alerts.isEmpty()) {
            logger.info("Non active alerts");
            return;
        }

        logger.info("=== Alerts of low stock ===");
        alerts.forEach(logger::info);
    }

    //Get products with low stock
    public List<Product> getProductsLowStock() {
        return products.stream()
                .filter(Validator::isStockLow).toList();
    }

    //Clear Alerts
    public void clearAlerts() {
        alerts.clear();
        logger.info("Alerts cleared");
    }

    // --- Private Aux methods ---

    private boolean validateAndLogError(boolean validationResult, String errorType) {
        if (!validationResult) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(Validator.getMsgError(errorType));
            }
            return false;
        }
        return true;
    }

    //Verify low stock and generate an alert
    private void verifyLowStock(Product product) {
        if (Validator.isStockLow(product)) {
            String alert = String.format("ALERT: Stock low for %s (Code: %s) - Current stock: %d",
                    product.getName(), product.getCode(), product.getQuantity());

            if (!alerts.contains(alert)) {
                alerts.add(alert);
                logger.info(alert);
            }
        }
    }

    //Public method for external product search
    public Optional<Product> searchProductByCode(String code) {
        return products.stream()
                .filter(p -> p.getCode()
                        .equalsIgnoreCase(code))
                .findFirst();
    }

    // --- Getters ---
    public int getTotalProducts() {
        return products.size();
    }

    public int getTotalAlerts() {
        return alerts.size();
    }

    public List<Product> getProducts() {
        return java.util.Collections.unmodifiableList(products);
    }
}