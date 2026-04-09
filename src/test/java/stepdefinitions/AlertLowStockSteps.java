package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.logic.Product;
import org.logic.Validator;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AlertLowStockSteps {

    private Product product;
    private boolean validationResult;

    @Given("a product for low stock alert has {int} items in stock")
    public void a_product_for_low_stock_alert_has_items_in_stock(int quantity) {

        if (quantity < 0) {
            throw new IllegalArgumentException("Cannot initialize product: Stock quantity cannot be negative (" + quantity + ").");
        }
        product = new Product("TEST001", "Test Product", 10.0, quantity);
    }

    @When("the system checks the stock level")
    public void the_system_checks_the_stock_level() {

        assertNotNull(product, "Product must be initialized before checking stock level.");
        validationResult = Validator.isStockLow(product);
    }

    @Then("it should display a low stock alert")
    public void it_should_display_a_low_stock_alert() {
        assertTrue(validationResult, "Low stock alert should trigger when stock is below minimum. " +
                "Current stock: " + product.getQuantity() + ", Minimum threshold: " + Validator.MINIMUM_STOCK_ALERT);
    }
}