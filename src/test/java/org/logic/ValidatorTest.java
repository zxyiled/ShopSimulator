package org.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validator")
class ValidatorTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private Product makeProduct(String code, String name, double price, int quantity) {
        return new Product(code, name, price, quantity);
    }

    // ── Constructor ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        @DisplayName("Utility class cannot be instantiated — throws UnsupportedOperationException")
        void privateConstructorThrows() throws Exception {
            // Validator is a utility class whose private constructor throws intentionally.
            // This test gives JaCoCo coverage on that branch and documents the design decision.
            var constructor = Validator.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            assertThrows(java.lang.reflect.InvocationTargetException.class,
                    constructor::newInstance);
        }
    }

    // ── validateCode ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("validateCode()")
    class ValidateCode {

        @Test
        @DisplayName("Valid alphanumeric code returns true")
        void validCode() {
            assertTrue(Validator.validateCode("ABC123"));
        }

        @Test
        @DisplayName("Letters-only code returns true")
        void lettersOnly() {
            assertTrue(Validator.validateCode("PROD"));
        }

        @Test
        @DisplayName("Numbers-only code returns true")
        void numbersOnly() {
            assertTrue(Validator.validateCode("001"));
        }

        @Test
        @DisplayName("Single character code returns true")
            // Covers the truthy branch of the regex check with a minimal valid input
        void singleChar() {
            assertTrue(Validator.validateCode("A"));
        }

        @Test
        @DisplayName("Null code returns false")
        void nullCode() {
            assertFalse(Validator.validateCode(null));
        }

        @Test
        @DisplayName("Empty string returns false")
        void emptyCode() {
            assertFalse(Validator.validateCode(""));
        }

        @Test
        @DisplayName("Whitespace-only string returns false")
        void blankCode() {
            assertFalse(Validator.validateCode("   "));
        }

        @Test
        @DisplayName("Code with spaces returns false")
        void codeWithSpaces() {
            assertFalse(Validator.validateCode("ABC 123"));
        }

        @Test
        @DisplayName("Code with special characters returns false")
        void codeWithSpecialChars() {
            assertFalse(Validator.validateCode("ABC-123"));
            assertFalse(Validator.validateCode("ABC@123"));
            assertFalse(Validator.validateCode("ABC_123"));
        }
    }

    // ── validateName ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("validateName()")
    class ValidateName {

        @Test
        @DisplayName("Name with 3+ characters returns true")
        void validName() {
            assertTrue(Validator.validateName("Laptop"));
        }

        @Test
        @DisplayName("Name with exactly 3 characters returns true")
            // Covers the lower boundary of the length >= 3 condition
        void exactlyThreeChars() {
            assertTrue(Validator.validateName("CPU"));
        }

        @Test
        @DisplayName("Name with 2 characters returns false")
        void twoChars() {
            assertFalse(Validator.validateName("PC"));
        }

        @Test
        @DisplayName("Null name returns false")
        void nullName() {
            assertFalse(Validator.validateName(null));
        }

        @Test
        @DisplayName("Empty name returns false")
        void emptyName() {
            assertFalse(Validator.validateName(""));
        }

        @Test
        @DisplayName("Whitespace-only name returns false")
            // trim() reduces it to empty, so the length check fails
        void blankName() {
            assertFalse(Validator.validateName("   "));
        }
    }

    // ── validatePrice ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("validatePrice()")
    class ValidatePrice {

        @Test
        @DisplayName("Positive price returns true")
        void validPrice() {
            assertTrue(Validator.validatePrice(9.99));
        }

        @Test
        @DisplayName("Price of zero returns false")
        void zeroPrice() {
            assertFalse(Validator.validatePrice(0.0));
        }

        @Test
        @DisplayName("Negative price returns false")
        void negativePrice() {
            assertFalse(Validator.validatePrice(-1.0));
        }

        @Test
        @DisplayName("Very small positive price returns true")
            // Covers the lower boundary just above zero
        void minimalPrice() {
            assertTrue(Validator.validatePrice(0.01));
        }
    }

    // ── validateQuantity ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("validateQuantity()")
    class ValidateQuantity {

        @Test
        @DisplayName("Negative quantity returns true (is negative)")
        void negativeQuantity() {
            assertTrue(Validator.validateQuantity(-1));
        }

        @Test
        @DisplayName("Zero quantity returns false (is not negative)")
        void zeroQuantity() {
            assertFalse(Validator.validateQuantity(0));
        }

        @Test
        @DisplayName("Positive quantity returns false (is not negative)")
        void positiveQuantity() {
            assertFalse(Validator.validateQuantity(10));
        }
    }

    // ── isQuantityInvalid ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("isQuantityInvalid()")
    class IsQuantityInvalid {

        @Test
        @DisplayName("Zero quantity is invalid")
        void zero() {
            assertTrue(Validator.isQuantityInvalid(0));
        }

        @Test
        @DisplayName("Negative quantity is invalid")
        void negative() {
            assertTrue(Validator.isQuantityInvalid(-5));
        }

        @Test
        @DisplayName("Positive quantity is valid")
        void positive() {
            assertFalse(Validator.isQuantityInvalid(1));
        }
    }

    // ── validateProductNonExistent ────────────────────────────────────────────

    @Nested
    @DisplayName("validateProductNonExistent()")
    class ValidateProductNonExistent {

        @Test
        @DisplayName("Returns true when list is empty")
        void emptyList() {
            assertTrue(Validator.validateProductNonExistent(new ArrayList<>(), "ABC"));
        }

        @Test
        @DisplayName("Returns true when code is not in the list")
        void codeNotPresent() {
            List<Product> products = List.of(makeProduct("XYZ", "Widget", 1.0, 10));
            assertTrue(Validator.validateProductNonExistent(products, "ABC"));
        }

        @Test
        @DisplayName("Returns false when code already exists (same case)")
        void codeExists() {
            List<Product> products = List.of(makeProduct("ABC", "Widget", 1.0, 10));
            assertFalse(Validator.validateProductNonExistent(products, "ABC"));
        }

        @Test
        @DisplayName("Returns false when code exists with different case")
        void codeExistsDifferentCase() {
            List<Product> products = List.of(makeProduct("ABC", "Widget", 1.0, 10));
            assertFalse(Validator.validateProductNonExistent(products, "abc"));
        }

        @Test
        @DisplayName("Returns false when code exists in a list with multiple products")
            // Covers stream traversal past the first element before finding a match
        void codeExistsInLargerList() {
            List<Product> products = List.of(
                    makeProduct("AAA", "Item1", 1.0, 5),
                    makeProduct("BBB", "Item2", 1.0, 5),
                    makeProduct("CCC", "Item3", 1.0, 5)
            );
            assertFalse(Validator.validateProductNonExistent(products, "BBB"));
        }
    }

    // ── validateProductExistent ───────────────────────────────────────────────

    @Nested
    @DisplayName("validateProductExistent()")
    class ValidateProductExistent {

        @Test
        @DisplayName("Non-null product returns true")
        void productExists() {
            assertTrue(Validator.validateProductExistent(makeProduct("A1", "Item", 5.0, 10)));
        }

        @Test
        @DisplayName("Null product returns false")
        void nullProduct() {
            assertFalse(Validator.validateProductExistent(null));
        }
    }

    // ── validateEnoughStock ───────────────────────────────────────────────────

    @Nested
    @DisplayName("validateEnoughStock()")
    class ValidateEnoughStock {

        @Test
        @DisplayName("Returns true when stock equals required quantity")
        void exactStock() {
            Product p = makeProduct("A1", "Item", 1.0, 10);
            assertTrue(Validator.validateEnoughStock(p, 10));
        }

        @Test
        @DisplayName("Returns true when stock exceeds required quantity")
        void surplusStock() {
            Product p = makeProduct("A1", "Item", 1.0, 20);
            assertTrue(Validator.validateEnoughStock(p, 10));
        }

        @Test
        @DisplayName("Returns false when stock is less than required quantity")
        void insufficientStock() {
            Product p = makeProduct("A1", "Item", 1.0, 5);
            assertFalse(Validator.validateEnoughStock(p, 10));
        }

        @Test
        @DisplayName("Returns false when stock is zero")
        void zeroStock() {
            Product p = makeProduct("A1", "Item", 1.0, 0);
            assertFalse(Validator.validateEnoughStock(p, 1));
        }
    }

    // ── isStockLow ────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("isStockLow()")
    class IsStockLow {

        @Test
        @DisplayName("Stock at minimum threshold is low")
        void atMinimum() {
            Product p = makeProduct("A1", "Item", 1.0, Validator.MINIMUM_STOCK_ALERT);
            assertTrue(Validator.isStockLow(p));
        }

        @Test
        @DisplayName("Stock below minimum threshold is low")
        void belowMinimum() {
            Product p = makeProduct("A1", "Item", 1.0, Validator.MINIMUM_STOCK_ALERT - 1);
            assertTrue(Validator.isStockLow(p));
        }

        @Test
        @DisplayName("Zero stock is low")
        void zeroStock() {
            Product p = makeProduct("A1", "Item", 1.0, 0);
            assertTrue(Validator.isStockLow(p));
        }

        @Test
        @DisplayName("Stock above minimum threshold is not low")
        void aboveMinimum() {
            Product p = makeProduct("A1", "Item", 1.0, Validator.MINIMUM_STOCK_ALERT + 1);
            assertFalse(Validator.isStockLow(p));
        }
    }

    // ── getMsgError ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getMsgError()")
    class GetMsgError {

        @Test
        @DisplayName("Returns message for each known error type")
            // Covers every case branch in the switch expression
        void knownErrors() {
            assertNotNull(Validator.getMsgError(Validator.ERROR_CODE));
            assertNotNull(Validator.getMsgError(Validator.ERROR_NAME));
            assertNotNull(Validator.getMsgError(Validator.ERROR_PRICE));
            assertNotNull(Validator.getMsgError(Validator.ERROR_QUANTITY));
            assertNotNull(Validator.getMsgError(Validator.ERROR_EXISTENT));
            assertNotNull(Validator.getMsgError(Validator.ERROR_NON_EXISTENT));
            assertNotNull(Validator.getMsgError(Validator.ERROR_INSUFFICIENT_STOCK));
            assertNotNull(Validator.getMsgError(Validator.ERROR_OPERATION_QUANTITY));
        }

        @Test
        @DisplayName("Returns fallback message for unknown error type")
            // Covers the default branch of the switch expression
        void unknownError() {
            String msg = Validator.getMsgError("unknown_type");
            assertNotNull(msg);
            assertFalse(msg.isBlank());
        }
    }
}