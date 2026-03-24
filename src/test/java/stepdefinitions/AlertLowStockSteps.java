package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.logic.Product;
import org.logic.Validator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertLowStockSteps {

    private Product product;
    private int requiredQuantity;
    private boolean validationResult;

    @Given("a product exists with quantity {int}")
    public void a_product_exists_with_quantity(int quantity) {
        product = new Product("TEST001", "Test Product", quantity, (int) 10.0);
    }

    @When("I validate stock for quantity {int}")
    public void i_validate_stock_for_quantity(int quantity) {
        this.requiredQuantity = quantity;
        validationResult = Validator.validateEnoughStock(product, quantity);
    }

    @Then("an alert should be shown")
    public void an_alert_should_be_shown() {
        assertTrue(validationResult, "The expected validation should be successful" +
                product.getQuantity() + "and required quantity" + requiredQuantity);
    }
}
