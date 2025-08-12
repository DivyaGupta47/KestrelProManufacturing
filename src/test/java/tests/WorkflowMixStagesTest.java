package tests;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import base.BaseTestWorkflow;
import listeners.ExtentTestNGListener;
import pages.LoginPageUi;
import pages.WorkflowMixStagesPage;
import pages.WorkflowOpenStagesPage;

public class WorkflowMixStagesTest extends BaseTestWorkflow{
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
	        WorkflowMixStagesPage workflowMixStagesPage = new WorkflowMixStagesPage(driver);
	        Thread.sleep(3000);
	        
	        workflowMixStagesPage.WorkflowsAlternateOpenStages(workflowName);
	        workflowMixStagesPage.verifyWorkflow(workflowName);	   
	        System.out.println("TEST PASSED: Workflow added successfully Workflow Name: " +workflowName);
	        
	    }
	    
		
}
