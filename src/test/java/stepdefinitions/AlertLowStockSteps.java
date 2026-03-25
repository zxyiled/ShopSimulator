package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.logic.Product;
import org.logic.Validator;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertLowStockSteps {

    private Product product;
    private boolean validationResult;

    @Given("an alert scenario with product quantity {int}")
    public void an_alert_scenario_with_product_quantity(int quantity) {
        product = new Product("TEST001", "Test Product", 10.0, quantity);
    }

    @When("I check if stock is low")
    public void i_check_if_stock_is_low() {
        validationResult = Validator.isStockLow(product);
    }

    @Then("an alert should be shown")
    public void an_alert_should_be_shown() {
        assertTrue(validationResult, "Alert should show when stock is low. Current stock: " +
                product.getQuantity() + ", Minimum: " + Validator.MINIMUM_STOCK_ALERT);
    }
}
