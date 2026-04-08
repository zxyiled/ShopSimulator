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

    @Given("a product has {int} items available in inventory")
    public void a_product_has_items_available_in_inventory(int quantity) {
        product = new Product("TEST001", "Test Product", 10.0, quantity);
    }

    @When("a customer requests {int} items")
    public void a_customer_requests_items(int quantity) {
        this.requiredQuantity = quantity;
        validationResult = Validator.validateEnoughStock(product, quantity);
    }

    @Then("the system should confirm the stock is sufficient")
    public void the_system_should_confirm_the_stock_is_sufficient() {
        assertTrue(validationResult, "Stock should be sufficient for customer request. " +
                "Available stock: " + product.getQuantity() + ", Requested: " + requiredQuantity);
    }
}
