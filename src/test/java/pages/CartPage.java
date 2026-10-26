package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    private final By cartTable = By.cssSelector("table.cart");
    private final By productNames = By.cssSelector("table.cart a.product-name");
    private final By unitPrice = By.cssSelector("table.cart span.product-unit-price");
    private final By quantityBox = By.cssSelector("table.cart input.qty-input");
    private final By lineTotal = By.cssSelector("table.cart span.product-subtotal");
    private final By removeCheckbox = By.cssSelector("table.cart input[name='removefromcart']");
    private final By updateCartButton = By.name("updatecart");
    private final By emptyCartMessage = By.cssSelector("div.order-summary-content");
    private final By orderTotal = By.cssSelector("tr.order-total span.product-price");
    private final By termsCheckbox = By.id("termsofservice");
    private final By checkoutButton = By.id("checkout");
    private final By termsPopupBox = By.id("terms-of-service-warning-box");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getProductNamesInCart() {
        WaitUtils.waitForVisible(driver, productNames);
        List<String> names = new ArrayList<>();
        for (WebElement element : driver.findElements(productNames)) {
            names.add(element.getText().trim());
        }
        return names;
    }

    public String getUnitPrice() {
        return getText(unitPrice);
    }

    @SuppressWarnings("deprecation")
	public String getQuantity() {
        return WaitUtils.waitForVisible(driver, quantityBox).getAttribute("value");
    }

    public String getLineTotal() {
        return getText(lineTotal);
    }

    public String getOrderTotal() {
        return getText(orderTotal);
    }

    public void setQuantityAndUpdate(String quantity) {
        type(quantityBox, quantity);
        clickUpdateAndWaitForReload();
    }

    public void removeFirstItem() {
        click(removeCheckbox);
        clickUpdateAndWaitForReload();
    }

    /** The cart page reloads after "Update shopping cart"; wait for the old table to disappear. */
    private void clickUpdateAndWaitForReload() {
        WebElement oldTable = driver.findElement(cartTable);
        click(updateCartButton);
        WaitUtils.waitForStale(driver, oldTable);
    }

    public boolean isCartEmpty() {
        return getText(emptyCartMessage).contains("Your Shopping Cart is empty!");
    }

    public void acceptTerms() {
        click(termsCheckbox);
    }

    public void clickCheckout() {
        click(checkoutButton);
    }

    /**
     * Click Checkout WITHOUT accepting the terms and return the warning text.
     * Older nopCommerce versions use a JS alert, newer ones a pop-up box - we handle both.
     */
    public String clickCheckoutAndGetTermsWarning() {
        click(checkoutButton);
        String alertText = WaitUtils.getAlertTextAndAccept(driver, 3);
        if (alertText != null) {
            return alertText;
        }
        return getText(termsPopupBox);
    }
}
