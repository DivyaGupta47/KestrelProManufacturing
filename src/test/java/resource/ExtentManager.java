package resource;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
/**
 * Singleton utility class for managing the ExtentReports instance.
 *
 * Responsibilities:
 * - Initializes and configures the ExtentReports object with a SparkReporter.
 * - Sets up the report file path, name, and metadata (like document title and tester info).
 * - Provides a globally accessible instance to be used across test listeners and reports.
 *
 * Report Output: test-output/ExtentReport.html
 *
 * Commonly used by ExtentTestNGListener to generate HTML test execution reports.
 */

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
            spark.config().setReportName("KestrelPro Manufacturing Automation Results");
            spark.config().setDocumentTitle("KestrelPro Manufacturing Report");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Tester", "QA@47Billion");
        }
        return extent;
    }
}