package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailsPage;
import pages.SearchResultsPage;
import utils.FlowHelper;

/** Add to cart, remove from cart, update quantity. A guest can use the cart, so no login is needed. */
public class CartTest extends BaseTest {

    @Test(description = "Add a product to the cart from its details page")
    public void testAddToCart() {
        HomePage homePage = new HomePage(driver);
        homePage.search(FlowHelper.BOOK_NAME);
        new SearchResultsPage(driver).openProduct(FlowHelper.BOOK_NAME);

        ProductDetailsPage product = new ProductDetailsPage(driver);
        product.clickAddToCart();

        Assert.assertTrue(product.getNotificationMessage().contains("The product has been added to your shopping cart"));
        Assert.assertEquals(homePage.getCartQuantity(), 1, "Header cart counter should show 1");
    }

    @Test(description = "Cart shows the product, price, quantity and total that were added")
    public void testCartContents() {
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        CartPage cart = new CartPage(driver);
        Assert.assertTrue(cart.getProductNamesInCart().contains(FlowHelper.BOOK_NAME));
        Assert.assertEquals(cart.getUnitPrice(), "10.00");
        Assert.assertEquals(cart.getQuantity(), "1");
        Assert.assertEquals(cart.getLineTotal(), "10.00");
        Assert.assertEquals(cart.getOrderTotal(), "10.00");
    }

    @Test(description = "Update quantity to 3 -> line total becomes 30.00")
    public void testUpdateQuantity() {
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        CartPage cart = new CartPage(driver);
        cart.setQuantityAndUpdate("3");

        Assert.assertEquals(cart.getQuantity(), "3");
        Assert.assertEquals(cart.getLineTotal(), "30.00");
        Assert.assertEquals(cart.getOrderTotal(), "30.00");
    }

    @Test(description = "Remove the only item -> cart becomes empty")
    public void testRemoveFromCart() {
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        CartPage cart = new CartPage(driver);
        cart.removeFirstItem();

        Assert.assertTrue(cart.isCartEmpty(), "Cart should be empty after removing the only item");
    }

    @Test(description = "NEGATIVE/EDGE: setting quantity to 0 removes the item")
    public void testUpdateQuantityToZero() {
        FlowHelper.addBookToCart(driver);
        new HomePage(driver).openCart();

        CartPage cart = new CartPage(driver);
        cart.setQuantityAndUpdate("0");

        Assert.assertTrue(cart.isCartEmpty(), "Quantity 0 should remove the item");
    }

    @Test(description = "NEGATIVE: Add to cart with quantity 0 is rejected")
    public void testAddToCartWithZeroQuantity() {
        new HomePage(driver).search(FlowHelper.BOOK_NAME);
        new SearchResultsPage(driver).openProduct(FlowHelper.BOOK_NAME);

        ProductDetailsPage product = new ProductDetailsPage(driver);
        product.setQuantity("0");
        product.clickAddToCart();

        String message = product.getNotificationMessage();
        Assert.assertTrue(message.contains("Quantity should be positive"), "Unexpected message: " + message);
        Assert.assertEquals(new HomePage(driver).getCartQuantity(), 0, "Nothing should be in the cart");
    }

    @Test(description = "A new visitor sees an empty cart")
    public void testEmptyCart() {
        new HomePage(driver).openCart();
        Assert.assertTrue(new CartPage(driver).isCartEmpty());
    }
}
