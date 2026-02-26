package org.logic;
import java.util.List;
import java.util.Optional;

public class Validator {

    private static final int minimum_stock_alert = 5;

    public static boolean validateCode(String code) {
     return code != null && !code.trim().isEmpty() && code.matches("[A-Za-z0-9]+");
    }

    public static boolean validateName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 3;
    }

    public static boolean validateQuantity(int quantity) {
        return quantity >= 0;
    }

    public static boolean validatePrice(double price) {
        return price > 0;
    }

    public static boolean validateProductNonExistent(List<Product> products, String code) {
        return products.stream().noneMatch(p -> p.getCode().equals(code));
    }

    public static boolean validateEnoughStock(Product product, int requiredQuantity) {
        return product.getQuantity() >= requiredQuantity;
    }

    public static boolean validateQuantityOperation(int quantity) {
        return quantity > 0;
    }

    public static boolean isStockLow(Product product) {
        return product.getQuantity() <= minimum_stock_alert;
    }

    //Get custom error message

}
