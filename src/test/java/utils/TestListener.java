package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

/** Registered in testng.xml. Prints progress and takes a screenshot when a test fails. */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START  : " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASSED : " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED : " + result.getMethod().getMethodName() + "  ->  " + result.getThrowable());
        String path = ScreenshotUtils.capture(DriverFactory.getDriver(), result.getMethod().getMethodName());
        if (path != null) {
            System.out.println("Screenshot saved: " + path);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIPPED: " + result.getMethod().getMethodName());
    }
}
