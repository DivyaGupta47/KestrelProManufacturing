package listeners;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import base.BaseTest;
import resource.ExtentManager;

/**
 * TestNG listener implementation for integrating ExtentReports into the test execution lifecycle.
 *
 * Responsibilities:
 * - Creates a new ExtentTest instance at the start of each test method.
 * - Logs the test result (pass, fail, skip) into the report.
 * - Captures and attaches a screenshot on test failure (if the test class extends BaseTest).
 * - Flushes the Extent report after all tests in the suite finish.
 *
 * Uses ThreadLocal to ensure parallel-safe reporting.
 * Relies on {@link base.BaseTestUi} for screenshot capture logic.
 *
 * Commonly attached in testng.xml or via @Listeners annotation for generating HTML reports.
 */

public class ExtentTestNGListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test passed");
    }

	/*
	 * @Override public void onTestFailure(ITestResult result) { WebDriver driver =
	 * ((BaseTest) result.getInstance()).getDriver(); // Ensure BaseTest has
	 * getDriver() String screenshotPath = ((BaseTest)
	 * result.getInstance()).takeScreenshot(result.getMethod().getMethodName());
	 * 
	 * test.get().fail(result.getThrowable());
	 * test.get().addScreenCaptureFromPath(screenshotPath); }
	 */
    
    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());

        if (result.getInstance() instanceof BaseTest) {
            BaseTest base = (BaseTest) result.getInstance();
            String screenshotPath = base.takeScreenshot(result.getMethod().getMethodName());
            test.get().addScreenCaptureFromPath(screenshotPath);
        } else {
            System.out.println("Test instance is not of type BaseTest — skipping screenshot.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
}