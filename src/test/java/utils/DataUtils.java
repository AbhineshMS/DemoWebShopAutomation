package utils;

import org.testng.Assert;

/** Small helpers for generating data and checking text. */
public class DataUtils {

    /** The demo site rejects an email that is already registered, so we make a new one every time. */
    public static String uniqueEmail() {
        return "autotest" + System.currentTimeMillis() + "@example.com";
    }

    /**
     * Passes if 'actual' contains ANY of the expected texts.
     * Expected texts are separated with | , e.g. "Wrong email|Login was unsuccessful".
     * Comparison ignores upper/lower case.
     */
    public static void assertContainsAny(String actual, String expectedOptions) {
        String actualLower = actual == null ? "" : actual.toLowerCase();
        for (String option : expectedOptions.split("\\|")) {
            if (!option.isBlank() && actualLower.contains(option.trim().toLowerCase())) {
                return;
            }
        }
        Assert.fail("Expected message to contain one of [" + expectedOptions + "] but was: [" + actual + "]");
    }

    /** "1,200.00" -> 1200.00 */
    public static double parsePrice(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }
}
