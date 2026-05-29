package e2e.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;

public class LoginPage {

    private static final String BASE_URL =
            System.getProperty("e2e.baseUrl", "http://localhost:8080");

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate(BASE_URL + "/login");
    }

    public void navigateTo(String path) {
        page.navigate(BASE_URL + path);
    }

    public void enterUsername(String username) {
        page.fill("#username", username);
    }

    public void enterPassword(String password) {
        page.fill("#password", password);
    }

    public void clickLoginSubmit() {
        page.click("#login-btn");
    }

    public boolean isDashboardVisible() {
        return waitVisible("#inventory-table");
    }

    public boolean isLoginPageVisible() {
        return waitVisible("#login-btn");
    }

    public void registerProduct(String code, String name, String price, String stock) {
        page.fill("#product-code", code);
        page.fill("#product-name", name);
        page.fill("#product-price", price);
        page.fill("#product-stock", stock);
        page.click("#add-product-btn");
        waitVisible("#message");
    }

    public boolean isProductInTable(String code) {
        return waitVisible("[data-testid='product-row-" + code + "']");
    }

    public void logout() {
        page.click("#logout-btn");
    }

    public void clearSession() {
        page.context().clearCookies();
    }

    public String getBodyText() {
        return page.textContent("body");
    }

    private boolean waitVisible(String selector) {
        try {
            page.locator(selector).first().waitFor(new com.microsoft.playwright.Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5000));
            return true;
        } catch (TimeoutError e) {
            return false;
        }
    }
}
