package tests;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import base.BaseTest;
import listeners.ExtentTestNGListener;
import pages.LoginPageUi;
import pages.ReportValidationPage;
import utils.ExcelReader;

@Listeners(ExtentTestNGListener.class)
public class ReportValidationTest extends BaseTest  {
	
    LoginPageUi loginPage;
    String userName = "reportautomation@yopmail.com";
    String password = "KestrelPro@123";

    public void login()    
    {
    	System.out.println("\n=========================================================");
    	System.out.println("Starting Flow: REPORT VALIDATION FLOW TEST");
    	System.out.println("=========================================================\n");
    	loginPage = new LoginPageUi(driver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
    }
	
  /* @Test
	public void ReportValidationTest() throws InterruptedException {
    	login();
		ReportValidationPage reportValidationPage = new ReportValidationPage(driver);				
		reportValidationPage.ReportTableValidation();
	}*/
    
    @Test
    public void compareReportUIAndExcelData() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        login();
        String customer = "Vasu Automation";

        ReportValidationPage page = new ReportValidationPage(driver);

        Map<String, String> uiData = page.getUIReportData(customer);
        Map<String, String> excelData = ExcelReader.getCustomerData(customer);

        // Fields to skip (not available in UI)
        List<String> skipFields = Arrays.asList("Current Stage", "Organization Name");

        for (String key : excelData.keySet()) {
            if (skipFields.contains(key)) continue;

            String expected = excelData.get(key);
            String actual = uiData.getOrDefault(key, "");

            System.out.println("Comparing field: " + key + " | Expected: " + expected + " | Actual: " + actual);

            Assert.assertEquals(normalize(actual), normalize(expected), "Mismatch in: " + key);
        }

        System.out.println("All matched fields validated for: " + customer);
    }

    private String normalize(String value) {
        if (value == null || value.trim().equals("--")) return "0.00";  // Treat "--" as 0.00
        value = value.trim();

        try {
            double d = Double.parseDouble(value);
            return String.format("%.2f", d); // Always format with 2 decimals
        } catch (Exception e) {
            return value;
        }
    }

}