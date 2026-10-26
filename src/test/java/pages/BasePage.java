package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * Parent of all page classes. Holds the reusable actions (click, type, read text)
 * and the HEADER links that appear on every page of the site.
 */
public class BasePage {

    protected WebDriver driver;

    // ---------- header locators (same on every page) ----------
    private final By registerLink = By.className("ico-register");
    private final By loginLink = By.className("ico-login");
    private final By logoutLink = By.className("ico-logout");
    private final By accountLink = By.cssSelector("div.header-links a.account");
    private final By cartLink = By.className("ico-cart");
    private final By cartQuantity = By.className("cart-qty");
    private final By searchBox = By.id("small-searchterms");
    private final By searchButton = By.cssSelector("input.search-box-button");

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- generic actions ----------
    protected void click(By locator) {
        WaitUtils.waitForClickable(driver, locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return WaitUtils.waitForVisible(driver, locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        return WaitUtils.isVisible(driver, locator, 5);
    }

    // ---------- header actions ----------
    public void clickRegister() {
        click(registerLink);
    }

    public void clickLogin() {
        click(loginLink);
    }

    public void clickLogout() {
        click(logoutLink);
    }

    public boolean isLogoutLinkDisplayed() {
        return isDisplayed(logoutLink);
    }

    public boolean isLoginLinkDisplayed() {
        return isDisplayed(loginLink);
    }

    /** The email shown at the top right once you are logged in. */
    public String getLoggedInEmail() {
        return getText(accountLink);
    }

    public void openCart() {
        click(cartLink);
    }

    /** Header text "Shopping cart (3)" -> 3 */
    public int getCartQuantity() {
        String text = getText(cartQuantity).replaceAll("[^0-9]", "");
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public void search(String keyword) {
        type(searchBox, keyword);
        click(searchButton);
    }

    /** Searching with an empty box shows a JavaScript alert; this returns the alert text. */
    public String searchAndGetAlert(String keyword) {
        type(searchBox, keyword);
        click(searchButton);
        return WaitUtils.getAlertTextAndAccept(driver, 5);
    }

    /** Click a main menu item such as "Books" or "Computers". */
    public void openTopMenu(String menuName) {
        click(By.xpath("//ul[@class='top-menu']//a[normalize-space()='" + menuName + "']"));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
