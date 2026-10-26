package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.DataUtils;
import utils.ExcelUtils;
import utils.FlowHelper;

public class LoginTest extends BaseTest {

    // An account created once by the first test that needs it and reused by the rest.
    private static String registeredEmail;
    private static String registeredPassword;

    private void ensureUserExists() {
        if (registeredEmail == null) {
            String[] credentials = FlowHelper.registerNewUser(driver);
            registeredEmail = credentials[0];
            registeredPassword = credentials[1];
            new HomePage(driver).clickLogout();       // leave the browser logged out
        }
    }

    /** DATA-DRIVEN: rows come from the "LoginData" sheet of TestData.xlsx. */
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtils.getSheetData("LoginData");
    }

    @Test(dataProvider = "loginData",
          description = "Login with multiple credentials read from Excel")
    public void testLogin(String testCaseId, String email, String password,
                          String expectedResult, String expectedMessage) {

        ensureUserExists();

        // Excel placeholders are swapped for the account created above
        if (email.equals("REGISTERED_EMAIL")) {
            email = registeredEmail;
        }
        if (password.equals("REGISTERED_PASSWORD")) {
            password = registeredPassword;
        }

        HomePage homePage = new HomePage(driver);
        homePage.clickLogin();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        if (expectedResult.equals("SUCCESS")) {
            Assert.assertTrue(homePage.isLogoutLinkDisplayed(),
                    testCaseId + ": Log out link should be visible after a valid login");
            Assert.assertEquals(homePage.getLoggedInEmail(), email);
        } else {
            DataUtils.assertContainsAny(loginPage.getErrorMessage(), expectedMessage);
            Assert.assertFalse(homePage.isLogoutLinkDisplayed(),
                    testCaseId + ": user must NOT be logged in with these credentials");
        }
    }

    @Test(description = "Logging out returns the user to the guest state")
    public void testLogout() {
        ensureUserExists();
        HomePage homePage = new HomePage(driver);
        homePage.clickLogin();
        new LoginPage(driver).login(registeredEmail, registeredPassword);
        Assert.assertTrue(homePage.isLogoutLinkDisplayed());

        homePage.clickLogout();
        Assert.assertTrue(homePage.isLoginLinkDisplayed(), "Log in link should be back after logout");
    }

    @Test(description = "Login page opens from the header link")
    public void testLoginPageOpens() {
        new HomePage(driver).clickLogin();
        Assert.assertEquals(driver.getTitle(), "Demo Web Shop. Login");
    }
}
