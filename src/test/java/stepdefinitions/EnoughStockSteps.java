package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.logic.Product;
import org.logic.Validator;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnoughStockSteps {
    
    private Product product;
    private int requiredQuantity;
    private boolean validationResult;

    @Given("a test product exists with quantity {int}")
    public void a_test_product_exists_with_quantity(int quantity) {
        product = new Product("TEST001", "Test Product", 10.0, quantity);
    }

    @When("I validate stock for quantity {int}")
    public void i_validate_stock_for_quantity(int quantity) {
        this.requiredQuantity = quantity;
        validationResult = Validator.validateEnoughStock(product, quantity);
    }

    @Then("the validation should be successful")
    public void the_validation_should_be_successful() {
        assertTrue(validationResult, "The expected validation should be successful. Current stock: " +
                product.getQuantity() + ", Required: " + requiredQuantity);
    }
}
