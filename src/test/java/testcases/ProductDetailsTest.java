package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailsPage;
import pages.SearchResultsPage;
import utils.FlowHelper;

public class ProductDetailsTest extends BaseTest {

    private ProductDetailsPage openBookDetails() {
        new HomePage(driver).search(FlowHelper.BOOK_NAME);
        new SearchResultsPage(driver).openProduct(FlowHelper.BOOK_NAME);
        return new ProductDetailsPage(driver);
    }

    @Test(description = "Product page shows correct name, price, image and Add to cart button")
    public void testProductDetailsAreCorrect() {
        ProductDetailsPage product = openBookDetails();

        Assert.assertEquals(product.getProductName(), "Computing and Internet");
        Assert.assertEquals(product.getProductPrice(), "10.00");
        Assert.assertTrue(product.isProductImageDisplayed(), "Product image should be visible");
        Assert.assertTrue(product.isAddToCartButtonDisplayed(), "Add to cart button should be visible");
        Assert.assertEquals(product.getQuantity(), "1", "Default quantity should be 1");
    }

    @Test(description = "Browser tab title contains the product name")
    public void testProductPageTitle() {
        openBookDetails();
        Assert.assertTrue(driver.getTitle().contains("Computing and Internet"),
                "Title was: " + driver.getTitle());
    }

    @Test(description = "NEGATIVE: a product URL that does not exist shows a 'not found' page")
    public void testNonExistentProductPage() {
        driver.get(driver.getCurrentUrl() + "this-product-does-not-exist-xyz");
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("not found"),
                "Expected a 'page not found' message");
    }
}
