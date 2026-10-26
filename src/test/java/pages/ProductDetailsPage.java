package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private final By productName = By.cssSelector("div.product-name h1");
    private final By productPrice = By.cssSelector("div.product-price span");
    private final By productImage = By.cssSelector("div.gallery img");
    private final By quantityBox = By.cssSelector("input.qty-input");
    private final By addToCartButton = By.cssSelector("input.add-to-cart-button");
    private final By notification = By.cssSelector("#bar-notification p.content");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public boolean isProductImageDisplayed() {
        return isDisplayed(productImage);
    }

    public boolean isAddToCartButtonDisplayed() {
        return isDisplayed(addToCartButton);
    }

    @SuppressWarnings("deprecation")
	public String getQuantity() {
        return driver.findElement(quantityBox).getAttribute("value");
    }

    public void setQuantity(String quantity) {
        type(quantityBox, quantity);
    }

    public void clickAddToCart() {
        click(addToCartButton);
    }

    /** Green/red bar that slides in after "Add to cart". */
    public String getNotificationMessage() {
        return getText(notification);
    }
}
