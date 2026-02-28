package org.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.Main.logger;
import org.logic.Product;
import org.logic.Validator;

public class SysInventory {
    private List<Product> products;
    private List<String> alerts;

    public SysInventory() {
        this.products = new ArrayList<>();
        this.alerts = new ArrayList<>();
    }

    // --- Public Methods ---

    //Register product with validations
    public boolean registerProduct(String code, String name, double price, int quantity) {

        if (!Validator.validateCode(code)) {
            logger.warning(Validator.getMsgError(Validator.ERROR_CODE));
            return false;
        }

        if (!Validator.validateName(name)) {
            logger.warning(Validator.getMsgError(Validator.ERROR_NAME));
            return false;
        }

        if (!Validator.validatePrice(price)) {
            logger.warning(Validator.getMsgError(Validator.ERROR_PRICE));
            return false;
        }

        if (!Validator.validateQuantity(quantity)) {
            logger.warning(Validator.getMsgError(Validator.ERROR_QUANTITY));
            return false;
        }

        if (!Validator.validateProductNonExistent(products, code)) {
            logger.warning(Validator.getMsgError(Validator.ERROR_NON_EXISTENT));
            return false;
        }

        Product newProduct = new Product(code, name, price, quantity);
        products.add(newProduct);
        logger.info("Product registered successfully: " + newProduct.getName());

        verifyLowStock(newProduct);
        return true;
    }

    //Augment Stock (Wrapper Method)
    public boolean augmentStock(String code, int quantity) {
        return modifyStock(code, quantity, true);
    }

    //Reduce stock (Wrapper Method)
    public boolean reduceStock(String code, int quantity) {
        return modifyStock(code, quantity, false);
    }

    private boolean modifyStock(String code, int quantity, boolean isIncrement) {

        if (!Validator.validateQuantityOperation(quantity)) {
            logger.warning(Validator.getMsgError(Validator.ERROR_OPERATION_QUANTITY));
            return false;
        }

        //Search product
        Optional<Product> productOpt = searchProductByCode(code);
        if (!validateProductExists(productOpt)) {
            return false;
        }

        Product product = productOpt.get();
        int currentStock = product.getQuantity();
        int newStock;

        //Calculate new stock according to operation
        if (isIncrement) {
            newStock = currentStock + quantity;
        } else {
            //Validate enough stock for reduction
            if (!Validator.validateEnoughStock(product, quantity)) {
                logger.warning(Validator.getMsgError(Validator.ERROR_INSUFFICIENT_STOCK) +
                        "(Current stock: " + currentStock + ")");
                return false;
            }
            newStock = currentStock - quantity;
        }

        //Validates the new stock isn't negative
        if (!Validator.validateQuantity(newStock)) {
            logger.warning("Error: Operation would result in negative stock");
            return false;
        }

        //Actualize the stock
        product.setQuantity(newStock);
        String action = isIncrement ? "increased" : "reduced";
        logger.info(" Stock " + action + " successfully. New stock of " +
                product.getName() + ": " + newStock);

        verifyLowStock(product);
        return true;
    }

    //Validate Inventory
    public boolean validateInventory(String code, int requiredQuantity) {
        Optional<Product> productOpt = searchProductByCode(code);

        if (!validateProductExists(productOpt)) {
            return false;
        }

        Product product = productOpt.get();

        if (!Validator.validateQuantityOperation(requiredQuantity)) {
            logger.warning(Validator.getMsgError(Validator.ERROR_OPERATION_QUANTITY));
            return false;
        }

        boolean enough = Validator.validateEnoughStock(product, requiredQuantity);

        if (enough) {
            logger.info("Inventory validated successfully. Current stock: " +
                    product.getQuantity() + " | Required: " + requiredQuantity);

        } else {
            logger.warning(Validator.getMsgError(Validator.ERROR_INSUFFICIENT_STOCK) +
                    " Current: " + product.getQuantity() + " | Required: " + requiredQuantity);
        }

        return enough;
    }

    //Show all products
    public void showAllProducts() {
        if (products.isEmpty()) {
            logger.info("Not registered products");
            return;
        }

        logger.info("=== Product list ===");
        logger.info("Total: " + products.size() + "products\n");
        products.forEach(p -> {
            String lowStock = Validator.isStockLow(p) ? "Low stock " : "";
            logger.info(p.toString() + lowStock);
        });
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
        return products.stream().filter(Validator::isStockLow).toList();
    }

    //Clear Alerts
    public void clearAlerts() {
        alerts.clear();
        logger.info("Alerts cleared");
    }

    // --- Private Aux methods ---

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

    //Search product by code
    public Optional<Product> searchProductByCode(String code) {
        return products.stream().filter(p -> p.getCode().equalsIgnoreCase(code)).findFirst();
    }

    //Validate product exist
    private boolean validateProductExists(Optional<Product> productOpt) {
        if (!Validator.validateProductExistent(productOpt)) {
            logger.warning(Validator.getMsgError(Validator.ERROR_NON_EXISTENT));
            return false;
        }
        return true;
    }

    // --- Getters ---
    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public List<String> getAlerts() {
        return new ArrayList<>(alerts);
    }

    public int getTotalProducts() {
        return products.size();
    }

    public int getTotalAlerts() {
        return alerts.size();
    }
}

