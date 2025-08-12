package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.OrderAPI;
import apis.OrderVerificationAPI;
import apis.WorkflowAPI;
import base.BaseTestWorkflow;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import pages.DashboardPage;
import pages.LoginPageUi;
import pages.WorkflowOpenStagesPage;
import utils.Config;
import utils.LoginUtil;

@Listeners(ExtentTestNGListener.class)
public class WorkflowOpenStagesTest extends BaseTestWorkflow{
	   LoginPageUi loginPage;
	    String userName = "workflow@yopmail.com";
	    String password = "KestrelPro@123";
	    String workflowName = "Workflow" + System.currentTimeMillis();

		public void login()    
	    {
	    	System.out.println("\n=========================================================");
	    	System.out.println("Starting Flow: WORKFLOW OPEN TEST UI");
	    	System.out.println("=========================================================\n");
	    	loginPage = new LoginPageUi(driver);
	        loginPage.enterUsername(userName);
	        loginPage.enterPassword(password);
	        loginPage.clickSignIn();
	        System.out.println("TEST PASSED: Admin logged in successfully with email: " +userName);
	    }
	    
	    @Test
	    public void workflowOpenForAllStages() throws InterruptedException {
	    	ExtentTest test = ExtentTestNGListener.getTest();
	        login();
	        WorkflowOpenStagesPage workflowOpenStagesPage = new WorkflowOpenStagesPage(driver);
	        Thread.sleep(3000);
	        
	        workflowOpenStagesPage.WorkflowsOpen(workflowName);
	        workflowOpenStagesPage.verifyWorkflow(workflowName);	   
	        System.out.println("TEST PASSED: Workflow added successfully Workflow Name: " +workflowName);
	        
	    }
	    
		
}
