package e2e.steps;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import e2e.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    private static Playwright playwright;
    private static Browser browser;

    private Page page;
    private LoginPage loginPage;

    @BeforeAll
    public static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    public static void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Before
    public void createPage() {
        page = browser.newPage();
        loginPage = new LoginPage(page);
    }

    @After
    public void closePage() {
        if (page != null) page.close();
    }

    @Given("the user is on the login page")
    public void userIsOnLoginPage() {
        loginPage.navigate();
    }

    @When("the user enters username {string} and password {string}")
    public void userEntersCredentials(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("the user submits the login form")
    public void userSubmitsLoginForm() {
        loginPage.clickLoginSubmit();
    }

    @Then("the user should see the inventory dashboard")
    public void userSeesDashboard() {
        assertTrue(loginPage.isDashboardVisible(), "Inventory dashboard should be visible");
    }

    @When("the user registers a new product with code {string}, name {string}, price {string} and stock {string}")
    public void userRegistersProduct(String code, String name, String price, String stock) {
        loginPage.registerProduct(code, name, price, stock);
    }

    @Then("the product {string} should appear in the inventory table")
    public void productAppearsInTable(String code) {
        assertTrue(loginPage.isProductInTable(code), "Product " + code + " should appear in the table");
    }

    @When("the user logs out")
    public void userLogsOut() {
        loginPage.logout();
    }

    @Then("the user should be redirected to the login page")
    public void userIsRedirectedToLogin() {
        assertTrue(loginPage.isLoginPageVisible(), "Login page should be visible");
    }

    @Given("the user is not authenticated")
    public void userIsNotAuthenticated() {
        loginPage.navigate();
        loginPage.clearSession();
    }

    @When("the user navigates directly to {string}")
    public void userNavigatesDirectlyTo(String path) {
        loginPage.navigateTo(path);
    }
}
