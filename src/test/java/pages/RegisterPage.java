package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class RegisterPage extends BasePage {

    private final By genderMale = By.id("gender-male");
    private final By genderFemale = By.id("gender-female");
    private final By firstName = By.id("FirstName");
    private final By lastName = By.id("LastName");
    private final By email = By.id("Email");
    private final By password = By.id("Password");
    private final By confirmPassword = By.id("ConfirmPassword");
    private final By registerButton = By.id("register-button");
    private final By continueButton = By.cssSelector("input.register-continue-button");

    // success text and every kind of error message the page can show
    private final By resultOrErrors =
            By.cssSelector("div.result, span.field-validation-error, div.validation-summary-errors");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void register(String gender, String first, String last, String emailAddress,
                         String pwd, String confirmPwd) {
        if ("F".equalsIgnoreCase(gender)) {
            click(genderFemale);
        } else if ("M".equalsIgnoreCase(gender)) {
            click(genderMale);
        }
        type(firstName, first);
        type(lastName, last);
        type(email, emailAddress);
        type(password, pwd);
        type(confirmPassword, confirmPwd);
        click(registerButton);
    }

    /** Text of the success message OR all error messages (joined with " | "). */
    public String getResultMessage() {
        WaitUtils.waitForVisible(driver, resultOrErrors);
        StringBuilder text = new StringBuilder();
        for (WebElement element : driver.findElements(resultOrErrors)) {
            if (element.isDisplayed()) {
                text.append(element.getText().trim()).append(" | ");
            }
        }
        return text.toString();
    }

    public void clickContinue() {
        click(continueButton);
    }
}
