package utils;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.*;

/**
 * Reusable multi-step flows (register a user, put the book in the cart, finish checkout).
 * Tests call these as SET-UP so each test can focus on the one thing it verifies.
 */
public class FlowHelper {

    public static final String BOOK_NAME = "Computing and Internet";
    public static final String DEFAULT_PASSWORD = "Test@1234";

    /** Registers a brand-new user and leaves the browser logged in. Returns {email, password}. */
    public static String[] registerNewUser(WebDriver driver) {
        String email = DataUtils.uniqueEmail();
        new HomePage(driver).clickRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register("M", "Auto", "Tester", email, DEFAULT_PASSWORD, DEFAULT_PASSWORD);
        Assert.assertTrue(registerPage.getResultMessage().contains("Your registration completed"),
                "Set-up failed: could not register a new user");
        registerPage.clickContinue();
        return new String[]{email, DEFAULT_PASSWORD};
    }

    /** Searches for the book, opens it and adds it to the cart. */
    public static void addBookToCart(WebDriver driver) {
        new HomePage(driver).search(BOOK_NAME);
        new SearchResultsPage(driver).openProduct(BOOK_NAME);
        ProductDetailsPage product = new ProductDetailsPage(driver);
        product.clickAddToCart();
        Assert.assertTrue(product.getNotificationMessage().contains("added to your shopping cart"),
                "Set-up failed: product was not added to the cart");
    }

    /** From the cart page: accept terms, checkout, go through all 6 steps and confirm the order. */
    public static OrderConfirmationPage checkoutFromCart(WebDriver driver) {
        CartPage cart = new CartPage(driver);
        cart.acceptTerms();
        cart.clickCheckout();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.fillBillingAddress("India", "Test City", "12 Test Street", "600001", "9876543210");
        checkout.clickBillingContinue();
        checkout.continueShippingAddress();
        checkout.continueShippingMethod();
        checkout.continuePaymentMethod();
        checkout.continuePaymentInfo();
        checkout.confirmOrder();
        return new OrderConfirmationPage(driver);
    }
}
