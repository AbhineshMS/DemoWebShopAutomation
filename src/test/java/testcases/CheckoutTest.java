package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import utils.FlowHelper;

/** Checkout flow - the negative cases. The full successful order is in OrderConfirmationTest. */
public class CheckoutTest extends BaseTest {

    @Test(description = "NEGATIVE: Checkout without accepting the terms of service is blocked")
    public void testCheckoutWithoutAcceptingTerms() {
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        String warning = new CartPage(driver).clickCheckoutAndGetTermsWarning();

        Assert.assertTrue(warning.contains("accept the terms of service"), "Unexpected warning: " + warning);
        Assert.assertTrue(driver.getCurrentUrl().contains("/cart"), "User should stay on the cart page");
    }

    @Test(description = "NEGATIVE: A guest is asked to log in / register / continue as guest")
    public void testGuestIsAskedToLoginAtCheckout() {
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        CartPage cart = new CartPage(driver);
        cart.acceptTerms();
        cart.clickCheckout();

        CheckoutPage checkout = new CheckoutPage(driver);
        Assert.assertTrue(driver.getCurrentUrl().contains("checkoutasguest"),
                "Expected the login / guest page but URL was " + driver.getCurrentUrl());
        Assert.assertTrue(checkout.isGuestCheckoutOptionDisplayed(), "'Checkout as Guest' button missing");
    }

    @Test(description = "NEGATIVE: Billing address with empty mandatory fields shows errors")
    public void testBillingAddressMandatoryFields() {
        FlowHelper.registerNewUser(driver);
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        CartPage cart = new CartPage(driver);
        cart.acceptTerms();
        cart.clickCheckout();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.clickBillingContinue();                       // nothing filled in

        String errors = checkout.getBillingErrors();
        Assert.assertTrue(errors.contains("City is required"), "Errors were: " + errors);
        Assert.assertTrue(errors.contains("Phone number is required"), "Errors were: " + errors);
    }
}
