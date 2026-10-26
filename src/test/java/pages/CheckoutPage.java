package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtils;

/**
 * The one-page checkout. It has 6 steps that open one after another:
 * Billing address -> Shipping address -> Shipping method -> Payment method -> Payment info -> Confirm.
 */
public class CheckoutPage extends BasePage {

    // Step 1 - billing
    private final By billingCountry = By.id("BillingNewAddress_CountryId");
    private final By billingCity = By.id("BillingNewAddress_City");
    private final By billingAddress1 = By.id("BillingNewAddress_Address1");
    private final By billingZip = By.id("BillingNewAddress_ZipPostalCode");
    private final By billingPhone = By.id("BillingNewAddress_PhoneNumber");
    private final By billingContinue = By.cssSelector("#billing-buttons-container input.new-address-next-step-button");
    private final By billingErrors = By.cssSelector("#billing-new-address-form span.field-validation-error");

    // Step 2 - shipping address
    private final By shippingContinue = By.cssSelector("#shipping-buttons-container input.new-address-next-step-button");

    // Step 3 - shipping method
    private final By shippingMethodRadio = By.id("shippingoption_0");
    private final By shippingMethodContinue = By.cssSelector("#shipping-method-buttons-container input.shipping-method-next-step-button");

    // Step 4 - payment method
    private final By paymentMethodRadio = By.id("paymentmethod_0");
    private final By paymentMethodContinue = By.cssSelector("#payment-method-buttons-container input.payment-method-next-step-button");

    // Step 5 - payment info
    private final By paymentInfoContinue = By.cssSelector("#payment-info-buttons-container input.payment-info-next-step-button");

    // Step 6 - confirm
    private final By confirmOrderButton = By.cssSelector("#confirm-order-buttons-container input.confirm-order-next-step-button");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    /** Fills the billing form if it is shown (a brand-new user has no saved address). */
    public void fillBillingAddress(String country, String city, String address, String zip, String phone) {
        WaitUtils.waitForVisible(driver, billingContinue);
        if (WaitUtils.isVisible(driver, billingCity, 2)) {
            new Select(driver.findElement(billingCountry)).selectByVisibleText(country);
            type(billingCity, city);
            type(billingAddress1, address);
            type(billingZip, zip);
            type(billingPhone, phone);
        }
    }

    public void clickBillingContinue() {
        click(billingContinue);
    }

    /** Error messages under the billing fields, joined with " | ". */
    public String getBillingErrors() {
        WaitUtils.waitForVisible(driver, billingErrors);
        StringBuilder text = new StringBuilder();
        for (WebElement element : driver.findElements(billingErrors)) {
            if (element.isDisplayed()) {
                text.append(element.getText().trim()).append(" | ");
            }
        }
        return text.toString();
    }

    public void continueShippingAddress() {
        click(shippingContinue);
    }

    public void continueShippingMethod() {
        WaitUtils.waitForVisible(driver, shippingMethodContinue);
        if (WaitUtils.isVisible(driver, shippingMethodRadio, 2)) {
            click(shippingMethodRadio);
        }
        click(shippingMethodContinue);
    }

    public void continuePaymentMethod() {
        WaitUtils.waitForVisible(driver, paymentMethodContinue);
        if (WaitUtils.isVisible(driver, paymentMethodRadio, 2)) {
            click(paymentMethodRadio);
        }
        click(paymentMethodContinue);
    }

    public void continuePaymentInfo() {
        click(paymentInfoContinue);
    }

    public void confirmOrder() {
        click(confirmOrderButton);
    }

    public boolean isGuestCheckoutOptionDisplayed() {
        return isDisplayed(By.cssSelector("input.checkout-as-guest-button"));
    }
}
