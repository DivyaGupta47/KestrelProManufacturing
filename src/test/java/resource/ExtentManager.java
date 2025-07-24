package resource;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
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

    /*public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
            spark.config().setReportName("KestrelPro Manufacturing Automation Results");
            spark.config().setDocumentTitle("KestrelPro Manufacturing Report");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Tester", "QA@47Billion");
        }
        return extent;
    }*/
    
    public static ExtentReports getInstance() {
        
   	 if (extent == null) {
            // Create Spark Reporter
            ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
            spark.config().setReportName("KestrelPro Manufacturing Automation Results");
            spark.config().setDocumentTitle("KestrelPro Manufacturing Report");
            spark.config().setTheme(Theme.DARK);

            // Create ExtentReports instance
            extent = new ExtentReports();
            extent.attachReporter(spark);

            // Add basic system info (used for dashboard UI)
            extent.setSystemInfo("Tester", "QA@47Billion");
            extent.setSystemInfo("Environment", "Staging");
            extent.setSystemInfo("Build Version", "1.0.2");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("Operating System", "Windows 10");

            // Add environment summary as a visible test block (for PDF support)
            ExtentTest summary = extent.createTest("Test Execution Summary");
            summary.info("Project Name: KestrelPro Manufacturing");
            summary.info("Automation Tester: Divya Gupta And Deepak Jodhwani");       
            summary.info("Environment: Staging");
            summary.info("Build Version: 1.0.2");
            summary.info("Browser: Chrome");
            summary.info("OS: Windows 10");
        }

       return extent;
   }
}