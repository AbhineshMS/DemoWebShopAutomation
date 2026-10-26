package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderConfirmationPage extends BasePage {

    private final By successMessage = By.cssSelector("div.order-completed div.title strong");
    private final By orderNumber = By.cssSelector("div.order-completed ul.details li:first-child");
    private final By continueButton = By.cssSelector("input.order-completed-continue-button");

    // "My account > Orders" page
    private final By orderHistoryNumbers = By.cssSelector("div.order-list div.order-item div.title strong");

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }

    /** Returns only the digits, e.g. "Order number: 12345" -> "12345". */
    public String getOrderNumber() {
        return getText(orderNumber).replaceAll("[^0-9]", "");
    }

    public void clickContinue() {
        click(continueButton);
    }

    public boolean isOrderInHistory(String number) {
        driver.get(driver.getCurrentUrl().replaceAll("(https?://[^/]+/).*", "$1") + "order/history");
        return isDisplayed(By.xpath("//div[@class='order-item']//strong[contains(.,'" + number + "')]"));
    }
}
