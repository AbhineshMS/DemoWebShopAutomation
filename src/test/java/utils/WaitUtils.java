package utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Explicit-wait helpers. We wait for a CONDITION (visible, clickable ...)
 * instead of using Thread.sleep(), so tests are fast and stable.
 */
public class WaitUtils {

    private static WebDriverWait wait(WebDriver driver, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    private static int defaultTimeout() {
        return ConfigReader.getInt("explicit.wait.seconds");
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return wait(driver, defaultTimeout()).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return wait(driver, defaultTimeout()).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitForUrlContains(WebDriver driver, String fragment) {
        wait(driver, defaultTimeout()).until(ExpectedConditions.urlContains(fragment));
    }

    /** Wait until an element from the OLD page/DOM is gone (used after a page reload). */
    public static void waitForStale(WebDriver driver, WebElement oldElement) {
        wait(driver, defaultTimeout()).until(ExpectedConditions.stalenessOf(oldElement));
    }

    /** Short wait that answers true/false instead of throwing. */
    public static boolean isVisible(WebDriver driver, By locator, int seconds) {
        try {
            wait(driver, seconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Waits for a JavaScript alert, returns its text and accepts it. Returns null if none appears. */
    public static String getAlertTextAndAccept(WebDriver driver, int seconds) {
        try {
            Alert alert = wait(driver, seconds).until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            alert.accept();
            return text;
        } catch (TimeoutException e) {
            return null;
        }
    }
}
