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

        //Register product with validations
    public boolean registerProduct(String code, String name, double price, int quantity) {

        if (!Validator.validateCode(code)) {
            logger.warning(Validator.getMsgError("code"));
            return false;
        }

        if (!Validator.validateName(name)) {
            logger.warning(Validator.getMsgError("name"));
            return false;
        }

        if (!Validator.validatePrice(price)) {
            logger.warning(Validator.getMsgError("price"));
            return false;
        }

        if (!Validator.validateQuantity(quantity)) {
            logger.warning(Validator.getMsgError("quantity"));
            return false;
        }

        if (!Validator.validateProductNonExistent(products, code)) {
            logger.warning(Validator.getMsgError("existent"));
            return false;
        }

        Product newProduct = new Product(code, name, price, quantity);
        products.add(newProduct);
        logger.info("Product registered successfully: " + newProduct.getName());

        verifyLowStock(newProduct);
        return true;
    }

    //Augment Stock
    public boolean augmentStock(String code, int quantity) {

        if (!Validator.validateQuantityOperation(quantity)) {
            logger.warning(Validator.getMsgError("operation_quantity"));
            return false;
        }

        Optional<Product> productOpt = searchProductbyCode(code);

        if (!Validator.validateProductExistent(productOpt)) {
            logger.warning(Validator.getMsgError("no_existent"));
            return false;
        }

        Product product = productOpt.get();
        int newQuantity = product.getQuantity() + quantity;
        product.setQuantity(newQuantity);

        logger.info("Stock augmented successfully: " + product.getName() + " | New Quantity: " + newQuantity);

        verifyLowStock(product);
        return true;
    }

    //Reduce stock
    public boolean reduceStock(String code, int quantity) {

        if (!Validator.validateQuantityOperation(quantity)) {
            logger.warning(Validator.getMsgError("operation_quantity"));
            return false;
        }
    }
}
