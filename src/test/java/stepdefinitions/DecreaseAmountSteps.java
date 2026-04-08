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

    @Given("a product has {int} items in stock")
    public void a_product_has_items_in_stock(int quantity) {
        product = new Product("TEST001", "Test Product", 10.0, quantity);
    }

    @When("{int} items are sold from inventory")
    public void items_are_sold_from_inventory(int quantity) {
        this.requiredQuantity = quantity;
        validationResult = Validator.validateEnoughStock(product, quantity);
        
        product.setQuantity(product.getQuantity() - quantity);
    }

    @Then("the product should have {int} items remaining")
    public void the_product_should_have_items_remaining(int quantity) {
        assertEquals(quantity, product.getQuantity(),
                "Expected remaining stock: " + quantity + ", but actual stock: " + product.getQuantity());
    }
}
