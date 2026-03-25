package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.logic.Product;
import org.logic.Validator;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AlertLowStockSteps {

    private Product product;
    private int requiredQuantity;
    private boolean validationResult;

    @Given("an alert scenario with product quantity {int}")
    public void an_alert_scenario_with_product_quantity(int quantity) {
        product = new Product("TEST001", "Test Product", 10.0, quantity);
    }

    @When("I validate the stock for quantity {int}")
    public void i_validate_the_stock_for_quantity(int quantity) {
        this.requiredQuantity = quantity;
        validationResult = Validator.validateEnoughStock(product, quantity);
    }

    @Then("an alert should be shown")
    public void an_alert_should_be_shown() {
        assertFalse(validationResult, "Alert should show when stock is insufficient. Current stock: " +
                product.getQuantity() + ", Required: " + requiredQuantity);
    }
}
