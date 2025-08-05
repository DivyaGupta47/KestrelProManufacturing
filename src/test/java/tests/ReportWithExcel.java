package tests;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import apis.ReportAPI;
import base.BaseTest;
import listeners.ExtentTestNGListener;
import pages.LoginPage;
import pages.ReportAssertionPage;
import utils.ExcelDownloader;
import utils.ExcelReaderAPI;
import utils.LoginUtil;
import utils.Config;
@Listeners(ExtentTestNGListener.class)
public class ReportWithExcel extends BaseTest {

    LoginPage loginPage;
    String userName = "reportautomation@yopmail.com";
    String password = "KestrelPro@123";

    public void loginUI() {
        System.out.println("\n=========================================================");
        System.out.println("Starting Flow: REPORT ASSERTION FLOW TEST");
        System.out.println("=========================================================\n");

        loginPage = new LoginPage(driver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
        System.out.println("TEST PASSED: Admin logged in successfully with email: " +userName);
    }

    @Test
    public void compareReportUIAndExcelData() throws InterruptedException {
        // Step 1: UI login first
        loginUI();

        // Step 2: Fetch UI data
        String customer = "Manisha Automation";
        ReportAssertionPage page = new ReportAssertionPage(driver);
        Map<String, String> uiData = page.getUIReportData(customer);
        System.out.println("TEST PASSED: UI data fetched successfully");
        // Step 3: Fresh API login (so token is valid)
        String sessionToken = LoginUtil.performLogin(userName, password);
        Config.setSessionToken(sessionToken);
        
     // Step 4: Download Excel via API helper
        File excelFile = ReportAPI.downloadReport(
                customer,
                "COMPLETED ON TIME",
                "2025-04-01",
                "2025-08-04"
        );
        System.out.println("TEST PASSED: Report downloaded via API successfully");


        // Step 5: Read Excel data
        Map<String, String> excelData = ExcelReaderAPI.getCustomerDataExcel(excelFile, customer);

        // Step 6: Compare UI vs Excel data
        List<String> skipFields = Arrays.asList("Current Stage", "Organization Name");

        for (String key : excelData.keySet()) {
            if (skipFields.contains(key)) continue;

            String expected = excelData.get(key);
            String actual = uiData.getOrDefault(key, "");

            //System.out.println("Comparing field: " + key + " | Expected: " + expected + " | Actual: " + actual);
            Assert.assertEquals(normalize(actual), normalize(expected), "Mismatch in: " + key);
        }

        System.out.println("TEST PASSED: All matched fields validated for customer: " + customer);
    }

    private String normalize(String value) {
        if (value == null || value.trim().equals("--")) return "0.00"; // Treat "--" as 0.00
        value = value.trim();

        // Try number normalization
        try {
            double d = Double.parseDouble(value);
            return String.format("%.2f", d); // Always 2 decimals
        } catch (NumberFormatException e) {
            // Not a number, try date normalization
        }

        // Try date normalization (dd/MM/yyyy or MM/dd/yyyy)
        try {
            List<String> dateFormats = Arrays.asList("dd/MM/yyyy", "MM/dd/yyyy");
            for (String format : dateFormats) {
                try {
                    java.time.LocalDate date = java.time.LocalDate.parse(
                        value, java.time.format.DateTimeFormatter.ofPattern(format)
                    );
                    return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception ignore) {}
            }
        } catch (Exception ignore) {}

        // Return as-is if neither number nor date
        return value;
    }

}
