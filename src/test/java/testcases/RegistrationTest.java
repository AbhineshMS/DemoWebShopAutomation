package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.RegisterPage;
import utils.DataUtils;
import utils.ExcelUtils;

public class RegistrationTest extends BaseTest {

    /** DATA-DRIVEN: every row of the "RegistrationData" sheet becomes one test run. */
    @DataProvider(name = "registrationData")
    public Object[][] registrationData() {
        return ExcelUtils.getSheetData("RegistrationData");
    }

    @Test(dataProvider = "registrationData",
          description = "Register with valid and invalid data read from Excel")
    public void testRegistration(String testCaseId, String gender, String firstName, String lastName,
                                 String email, String password, String confirmPassword,
                                 String expectedResult, String expectedMessage) {

        // "RANDOM" in Excel = generate a fresh email so the test can run again and again
        if (email.equals("RANDOM")) {
            email = DataUtils.uniqueEmail();
        }

        HomePage homePage = new HomePage(driver);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register(gender, firstName, lastName, email, password, confirmPassword);
        String message = registerPage.getResultMessage();

        if (expectedResult.equals("SUCCESS")) {
            Assert.assertTrue(message.contains(expectedMessage),
                    testCaseId + ": expected success message but got: " + message);
            registerPage.clickContinue();
            Assert.assertTrue(homePage.isLogoutLinkDisplayed(),
                    testCaseId + ": user should be logged in after registering");
            Assert.assertEquals(homePage.getLoggedInEmail(), email,
                    testCaseId + ": wrong email shown in header");
        } else {
            DataUtils.assertContainsAny(message, expectedMessage);
            Assert.assertFalse(message.contains("Your registration completed"),
                    testCaseId + ": registration must NOT succeed with this data");
        }
    }

    @Test(description = "Register page opens from the header link")
    public void testRegisterPageOpens() {
        new HomePage(driver).clickRegister();
        Assert.assertEquals(driver.getTitle(), "Demo Web Shop. Register");
        Assert.assertTrue(driver.getCurrentUrl().contains("/register"));
    }
}
