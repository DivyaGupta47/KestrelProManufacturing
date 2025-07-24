package tests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import base.BaseTest;
import listeners.ExtentTestNGListener;
import pages.DashboardPage;
import pages.LoginPageUi;

/**
 * Test class to validate dashboard metrics before and after an order flow via UI.
 * 
 * This class performs:
 * - Logging into the application as an admin user.
 * - Capturing and printing dashboard metrics: total orders, completed, on-time, and queued.
 * - Executing checks both before and after the order workflow to verify impact.
 * 
 * Key Features:
 * - Uses `LoginPage` for handling login and logout actions.
 * - Uses `DashboardPage` to fetch count values displayed on the dashboard.
 * - Logs all count values to the console for comparison and verification.
 */

@Listeners(ExtentTestNGListener.class)
public class DashboardTest extends BaseTest {


    LoginPageUi loginPage;
    String userName = "divyaadmin@yopmail.com";
    String password = "KestrelPro@123";

    public void login()    
    {
    	System.out.println("\n=========================================================");
    	System.out.println("Starting Flow: DASHBOARD COUNT TEST");
    	System.out.println("=========================================================\n");
    	loginPage = new LoginPageUi(driver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
    }
    
    @Test
    public void testDashboardCountsBefore() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        login();
        DashboardPage dashboard = new DashboardPage(driver);
        Thread.sleep(5000);
        // Capture before counts
        int totalBefore = dashboard.getTotalOrdersCount();
        int completedBefore = dashboard.getCompletedOrdersCount();
        int onTimeBefore = dashboard.getOnTimeOrdersCount();
        int queuedBefore = dashboard.getQueuedOrdersCount();

        System.out.println("===== Dashboard Counts BEFORE Order Flow =====");
        System.out.println("Total Orders: " + totalBefore);
        System.out.println("Completed Orders: " + completedBefore);
        System.out.println("On Time Orders: " + onTimeBefore);
        System.out.println("Queued Orders: " + queuedBefore);;

        loginPage.signOutAdmin();
    }
    
    @Test
    public void testDashboardCountsAfter() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
    	login();
        DashboardPage dashboard = new DashboardPage(driver);
        Thread.sleep(5000);
        // Capture after counts
        int totalAfter = dashboard.getTotalOrdersCount();
        int completedAfter = dashboard.getCompletedOrdersCount();
        int onTimeAfter = dashboard.getOnTimeOrdersCount();
        int queuedAfter = dashboard.getQueuedOrdersCount();

        System.out.println("===== Dashboard Counts AFTER Order Flow =====");
        System.out.println("Total Orders: " + totalAfter);
        System.out.println("Completed Orders: " + completedAfter);
        System.out.println("On Time Orders: " + onTimeAfter);
        System.out.println("Queued Orders: " + queuedAfter);

        loginPage.signOutAdmin();
    }
    
    
}
