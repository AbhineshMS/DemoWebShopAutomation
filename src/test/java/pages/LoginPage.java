package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class LoginPage extends BasePage {

    private final By email = By.id("Email");
    private final By password = By.id("Password");
    private final By loginButton = By.cssSelector("input.login-button");
    private final By errors = By.cssSelector("div.validation-summary-errors, span.field-validation-error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String emailAddress, String pwd) {
        type(email, emailAddress);
        type(password, pwd);
        click(loginButton);
    }

    /** All error texts on the page joined with " | ". */
    public String getErrorMessage() {
        WaitUtils.waitForVisible(driver, errors);
        StringBuilder text = new StringBuilder();
        for (WebElement element : driver.findElements(errors)) {
            if (element.isDisplayed()) {
                text.append(element.getText().trim()).append(" | ");
            }
        }
        return text.toString();
    }
}
