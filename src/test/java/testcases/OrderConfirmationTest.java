package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.OrderConfirmationPage;
import utils.FlowHelper;

/** End-to-end: register -> add to cart -> checkout -> order confirmation. */
public class OrderConfirmationTest extends BaseTest {

    @Test(description = "Complete checkout flow ends with an order confirmation and an order number")
    public void testPlaceOrderSuccessfully() {
        FlowHelper.registerNewUser(driver);
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        OrderConfirmationPage confirmation = FlowHelper.checkoutFromCart(driver);

        Assert.assertEquals(confirmation.getSuccessMessage(), "Your order has been successfully processed!");
        String orderNumber = confirmation.getOrderNumber();
        Assert.assertFalse(orderNumber.isEmpty(), "An order number should be shown");

        // the finished order must be listed under My account > Orders
        Assert.assertTrue(confirmation.isOrderInHistory(orderNumber),
                "Order " + orderNumber + " not found in order history");
    }

    @Test(description = "After the order the cart is empty again")
    public void testCartIsEmptyAfterOrder() {
        FlowHelper.registerNewUser(driver);
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        OrderConfirmationPage confirmation = FlowHelper.checkoutFromCart(driver);
        Assert.assertEquals(confirmation.getSuccessMessage(), "Your order has been successfully processed!");
        confirmation.clickContinue();

        Assert.assertEquals(new HomePage(driver).getCartQuantity(), 0, "Cart counter should be 0 after ordering");
    }
}
