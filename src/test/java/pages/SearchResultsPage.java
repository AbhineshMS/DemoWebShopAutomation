package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.DataUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for both the search results page and category pages (Books, Computers ...),
 * because they share the same product grid and sort drop-down.
 */
public class SearchResultsPage extends BasePage {

    private final By productTitles = By.cssSelector("div.product-item h2.product-title a");
    private final By productPrices = By.cssSelector("div.product-item span.price.actual-price");
    private final By message = By.cssSelector("div.search-results strong.result, div.warning");
    private final By sortDropdown = By.id("products-orderby");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getProductNames() {
        WaitUtils.waitForVisible(driver, productTitles);
        List<String> names = new ArrayList<>();
        for (WebElement element : driver.findElements(productTitles)) {
            names.add(element.getText().trim());
        }
        return names;
    }

    /** "No products were found..." or "Search term minimum length is 3 characters". */
    public String getMessage() {
        return getText(message);
    }

    public void openProduct(String productName) {
        click(By.xpath("//h2[@class='product-title']/a[normalize-space()='" + productName + "']"));
    }

    /** Choose e.g. "Price: Low to High" and wait for the page to reload with the new order. */
    public void sortBy(String optionText, String urlFragment) {
        WebElement dropdown = WaitUtils.waitForVisible(driver, sortDropdown);
        new Select(dropdown).selectByVisibleText(optionText);
        WaitUtils.waitForUrlContains(driver, urlFragment);
        WaitUtils.waitForVisible(driver, productPrices);
    }

    public List<Double> getProductPrices() {
        List<Double> prices = new ArrayList<>();
        for (WebElement element : driver.findElements(productPrices)) {
            prices.add(DataUtils.parsePrice(element.getText()));
        }
        return prices;
    }
}
