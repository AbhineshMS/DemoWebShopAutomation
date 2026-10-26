Demo Web Shop – Website Testing
----------------------------------

Project overview
--------------------
Demo Web Shop is an e-commerce web application that simulates an online shopping experience. Users can register, sign in, browse products, view product details, add items to the shopping cart, proceed through checkout, enter billing information, select a payment method, and complete an order. The application also includes contact form functionality. This project involves both manual and automation testing to validate key e-commerce workflows through positive and negative test scenarios. Manual testing is performed to verify application functionality, usability, and input validations, while automation testing is implemented using Java, Selenium WebDriver, TestNG, Apache POI, and the Page Object Model (POM) to improve test efficiency, reusability, and maintainability.

Application under test: https://demowebshop.tricentis.com/

**Stack:** Java 11+ · Selenium WebDriver 4 · Maven · TestNG · Page Object Model · Apache POI (Excel) · Explicit waits

---------------------------------------------------------------------
My Role
---------
Manual and Automation Tester
------------------------------
Prepared test plan and test cases.
Executed manual test cases.
Identified and documented defects.
Prepared test execution and summary reports.
Automated selected test scenarios using Selenium WebDriver, TestNG, Maven, Java and Apache POI.
Implemented Page Object Model for reusable and maintainable automation.

Testing Scope
---------------
Home Page
Product Detail Page
Authentication and User Account
Cart Page
Contact Us Form
Checkout Workflow

Tools andTechnologies
-----------------------
Manual Testing
---------------
Microsoft Excel

Automation Testing
-------------------
Eclipse
Java
Selenium WebDriver
POM
TestNG
Apache POI

Automation Framework
----------------------
The Demo Web Shop automation project is built using Java, Selenium WebDriver, TestNG, Maven, and the Page Object Model (POM). TestNG manages test execution and assertions, while Maven manages project dependencies and can run the test suite.
POM separates webpage interactions from test logic. Each page class represents a page or functional area of the application.

## 1. What you need installed
1. **JDK 11 or newer** (`java -version`)
2. **Maven** (`mvn -v`)
3. **Chrome** (or Firefox / Edge). Selenium 4 downloads the driver for you.
4. An IDE (IntelliJ IDEA Community or Eclipse) – optional but recommended.

## 2. Run the tests
```bash
cd DemoWebShopAutomation
mvn test                                  # whole suite (uses testng.xml)
mvn test -Dbrowser=firefox                # another browser
mvn test -Dheadless=true                  # no browser window
mvn test -Dtest=CartTest                  # one class only
mvn test -Dtest=LoginTest#testLogout      # one test only
```
In an IDE: right-click `testng.xml` → Run, or right-click any class in `testcases` → Run as TestNG.

Reports: `target/surefire-reports/index.html`. Screenshots of failed tests: `target/screenshots/`.

## 3. Project structure
```
DemoWebShopAutomation
├── pom.xml                      dependencies + surefire plugin
├── testng.xml                   which test classes run
└── src/test
    ├── java
    │   ├── base/BaseTest.java           opens browser before each test, closes after
    │   ├── pages/                       PAGE OBJECTS (locators + actions, NO assertions)
    │   │   ├── BasePage, HomePage, RegisterPage, LoginPage
    │   │   ├── SearchResultsPage, ProductDetailsPage
    │   │   └── CartPage, CheckoutPage, OrderConfirmationPage
    │   ├── testcases/                   TESTS (steps + assertions)
    │   │   ├── RegistrationTest   (data-driven, Excel)
    │   │   ├── LoginTest          (data-driven, Excel)
    │   │   ├── SearchTest         (data-driven, DataProvider; search + sort/filter)
    │   │   ├── ProductDetailsTest
    │   │   ├── CartTest           (add / update quantity / remove)
    │   │   ├── CheckoutTest       (negative checkout cases)
    │   │   └── OrderConfirmationTest (full end-to-end order)
    │   └── utils/
    │       ├── ConfigReader, ExcelUtils (Apache POI), WaitUtils (explicit waits)
    │       ├── DriverFactory, ScreenshotUtils, TestListener
    │       └── DataUtils, FlowHelper (reusable set-up flows)
    └── resources
        ├── config.properties            URL, browser, timeouts
        └── TestData.xlsx                sheets: RegistrationData, LoginData
```

## 4. How the pieces fit (the Page Object Model idea)
* A **page class** knows *where* things are on one page (locators) and *how* to use them (methods).
* A **test class** only says *what to do and what to expect* – it calls page methods and uses `Assert`.
* If a locator changes on the website you fix it in ONE page class, not in every test.

```java
new HomePage(driver).search("Computing");                       // action
List<String> names = new SearchResultsPage(driver).getProductNames();
Assert.assertTrue(names.contains("Computing and Internet"));     // assertion in the test
```

## 5. Data-driven testing
* **Excel + Apache POI** – `RegistrationTest` and `LoginTest`. Open `TestData.xlsx`, add a row, run again:
  a new row = a new test run, no Java change needed.
  * `RANDOM` in an email cell → a unique email is generated.
  * `REGISTERED_EMAIL` / `REGISTERED_PASSWORD` → replaced by an account the test registers itself
    (you can type your own real credentials there instead).
  * In `ExpectedMessage`, separate several accepted texts with `|`.
* **TestNG DataProvider** – `SearchTest.testSearchWithValidKeyword` (keywords are in the Java file).

## 6. Scenario coverage
| Scenario | Test class | Positive | Negative |
|---|---|---|---|
| Registration | RegistrationTest | valid users | missing names/email, bad email, short password, mismatch, duplicate email |
| Login | LoginTest | valid login, logout | wrong password, unknown user, blank fields, bad email format |
| Search / filter | SearchTest | 3 keywords, sort by price | no match, <3 chars, empty search |
| Product details | ProductDetailsTest | name, price, image, button, title | non-existent product URL |
| Add / Update / Remove cart | CartTest | add, contents, qty 3, remove | qty 0 on product page, qty 0 in cart, empty cart |
| Checkout | CheckoutTest | – | terms not accepted, guest sent to login page, empty billing form |
| Order confirmation | OrderConfirmationTest | full order + order history + empty cart | – |

## 7. If a test fails
1. Open the screenshot in `target/screenshots/`.
2. Read the assertion message – it says expected vs actual.
3. Open the site in Chrome → right-click the element → **Inspect** and compare with the locator in the page class.
   The site is a shared public demo; text/layout can change, and it can be slow at busy times.
4. Do not add `Thread.sleep()`. Add or adjust an explicit wait in `WaitUtils` instead.
