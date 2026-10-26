package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;

import java.util.List;

public class SearchTest extends BaseTest {

    /** DATA-DRIVEN with a plain TestNG DataProvider: {search keyword, product that must be found}. */
    @DataProvider(name = "searchKeywords")
    public Object[][] searchKeywords() {
        return new Object[][]{
                {"Computing", "Computing and Internet"},
                {"Simple Computer", "Simple Computer"},
                {"computer", "Build your own computer"}
        };
    }

    @Test(dataProvider = "searchKeywords", description = "Search finds the expected product")
    public void testSearchWithValidKeyword(String keyword, String expectedProduct) {
        new HomePage(driver).search(keyword);
        List<String> names = new SearchResultsPage(driver).getProductNames();
        Assert.assertTrue(names.contains(expectedProduct),
                "Searching '" + keyword + "' should list '" + expectedProduct + "' but found " + names);
    }

    @Test(description = "NEGATIVE: keyword that matches nothing")
    public void testSearchWithNoMatch() {
        new HomePage(driver).search("xyzabc123");
        String message = new SearchResultsPage(driver).getMessage();
        Assert.assertTrue(message.contains("No products were found"), "Unexpected message: " + message);
    }

    @Test(description = "NEGATIVE: keyword shorter than 3 characters")
    public void testSearchWithTooShortKeyword() {
        new HomePage(driver).search("ab");
        String message = new SearchResultsPage(driver).getMessage();
        Assert.assertTrue(message.contains("minimum length is 3 characters"), "Unexpected message: " + message);
    }

    @Test(description = "NEGATIVE: empty search shows an alert")
    public void testSearchWithEmptyKeyword() {
        String alertText = new HomePage(driver).searchAndGetAlert("");
        Assert.assertNotNull(alertText, "An alert was expected for an empty search");
        Assert.assertTrue(alertText.contains("Please enter some search keyword"), "Alert text: " + alertText);
    }

    @Test(description = "FILTER: Books sorted by price low to high are in ascending order")
    public void testSortBooksByPriceLowToHigh() {
        HomePage homePage = new HomePage(driver);
        homePage.openTopMenu("Books");

        SearchResultsPage books = new SearchResultsPage(driver);
        books.sortBy("Price: Low to High", "orderby=10");

        List<Double> prices = books.getProductPrices();
        Assert.assertTrue(prices.size() > 1, "Need at least 2 products to check sorting");
        for (int i = 1; i < prices.size(); i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i - 1),
                    "Prices are not sorted low to high: " + prices);
        }
    }
}
