package org.logic;
import java.util.List;
import java.util.Optional;

public class Validator {

    private Validator() {
        // Utility class - prevent instantiation
    }

    public static final int MINIMUM_STOCK_ALERT = 5;

    // --- Public static constants for not use literal strings ---

    public static final String ERROR_EXISTENT = "existent";
    public static final String ERROR_CODE = "code";
    public static final String ERROR_NAME = "name";
    public static final String ERROR_QUANTITY = "quantity";
    public static final String ERROR_PRICE = "price";
    public static final String ERROR_NON_EXISTENT = "non_existent";
    public static final String ERROR_INSUFFICIENT_STOCK = "insufficient_stock";
    public static final String ERROR_OPERATION_QUANTITY = "operation_quantity";

    // --- Public static methods ---

    public static boolean validateCode(String code) {
     return code != null && !code.trim()
             .isEmpty() && code.matches("[A-Za-z0-9]+");
    }

    public static boolean validateName(String name) {
        return name != null && !name.trim()
                .isEmpty() && name.length() >= 3;
    }

    public static boolean validateQuantity(int quantity) {
        return quantity >= 0;
    }

    public static boolean validatePrice(double price) {
        return price > 0;
    }

    public static boolean validateProductNonExistent(List<Product> products, String code) {
        return products.stream()
                .noneMatch(p -> p.getCode()
                        .equalsIgnoreCase(code));
    }

    public static boolean validateProductExistent(Optional<Product> product) {
        return product.isPresent();
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
            case ERROR_CODE -> "Error: Invalid Code. Has to contain only letters and numbers";
            case ERROR_NAME -> "Error: Invalid Name. Minimum length is 3 characters";
            case ERROR_QUANTITY -> "Error: Invalid Quantity. Has to be greater than or equal to 0";
            case ERROR_PRICE -> "Error: Invalid Price. Has to be greater than 0";
            case ERROR_EXISTENT -> "Error: The product already exists";
            case ERROR_NON_EXISTENT -> "Error: The product does not exist or was not found";
            case ERROR_INSUFFICIENT_STOCK -> "Error: The product does not have enough stock";
            case ERROR_OPERATION_QUANTITY -> "Error: The quantity has to be greater than 0";
            default -> "Error: Failed to validate";
        };
    }
}
