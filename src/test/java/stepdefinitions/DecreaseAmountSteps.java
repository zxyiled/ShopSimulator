package stepdefinitions;

import org.logic.Product;
import org.logic.Validator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecreaseAmountSteps {

    private Product product;
    private int requiredQuantity;
    private boolean validationResult;

    @Given("a product exists with quantity {int}")
    public void a_product_exists_with_quantity(int quantity) {
        product = new Product("TEST001", "Test Product", quantity, (int) 10.0);
    }

    @When("I decrease stock for quantity {int}")
    public void i_decrease_stock_for_quantity(int quantity) {
        this.requiredQuantity = quantity;
        validationResult = Validator.validateEnoughStock(product, quantity);
    }

    @Then("the stock should be {int}")
    public void the_stock_should_be(int quantity) {
        assertEquals(quantity, product.getQuantity());
    }
}
