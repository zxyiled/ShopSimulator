package org.logic;
import java.util.List;

public class Validator {

    private Validator() {
        // Utility class - prevent instantiation
    }

    private static final int MINIMUM_STOCK_ALERT = 5;

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

    public static boolean validateProductExistent(List<Product> products, String code) {
        return products.stream().anyMatch(p -> p.getCode().equals(code));
    }

    public static boolean validateEnoughStock(Product product, int requiredQuantity) {
        return product.getQuantity() >= requiredQuantity;
    }

    public static boolean validateQuantityOperation(int quantity) {
        return quantity > 0;
    }

    public static boolean isStockLow(Product product) {
        return product.getQuantity() <= MINIMUM_STOCK_ALERT;
    }

    //Get custom error message
    public static String getMsgError(String typeError) {
        return switch (typeError) {
            case "code" -> "Error: Invalid Code. Has to contain only letters and numbers";
            case "name" -> "Error: Invalid Name. Minimum length is 3 characters";
            case "quantity" -> "Error: Invalid Quantity. Has to be greater than or equal to 0";
            case "price" -> "Error: Invalid Price. Has to be greater than 0";
            case "existent" -> "Error: The product already exists";
            case "no_existent" -> "Error: The product does not exist or was not found";
            case "insufficient_Stock" -> "Error: The product does not have enough stock";
            case "operation_quantity" -> "Error: The quantity has to be greater than 0";
            default -> "Error: Failed to validate";
        };
    }
}
