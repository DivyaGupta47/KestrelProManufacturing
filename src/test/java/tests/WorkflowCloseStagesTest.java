package tests;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import base.BaseTestWorkflow;
import listeners.ExtentTestNGListener;
import pages.LoginPageUi;
import pages.WorkflowCloseStagesPage;
import pages.WorkflowOpenStagesPage;

public class WorkflowCloseStagesTest extends BaseTestWorkflow{
	   LoginPageUi loginPage;
	    String userName = "workflow@yopmail.com";
	    String password = "KestrelPro@123";
	    String workflowName = "Workflow" + System.currentTimeMillis();

		public void login()    
	    {
	    	System.out.println("\n=========================================================");
	    	System.out.println("Starting Flow: WORKFLOW CLOSE TEST UI");
	    	System.out.println("=========================================================\n");
	    	loginPage = new LoginPageUi(driver);
	        loginPage.enterUsername(userName);
	        loginPage.enterPassword(password);
	        loginPage.clickSignIn();
	        System.out.println("TEST PASSED: Admin logged in successfully with email: " +userName);
	    }
	    
	    @Test
	    public void workflowCloseForAllStages() throws InterruptedException {
	    	ExtentTest test = ExtentTestNGListener.getTest();
	        login();
	        WorkflowCloseStagesPage workflowCloseStagesPage = new WorkflowCloseStagesPage(driver);
	        Thread.sleep(3000);
	        
	        workflowCloseStagesPage.WorkflowsClose(workflowName);
	        workflowCloseStagesPage.verifyWorkflow(workflowName);	   
	        System.out.println("TEST PASSED: Workflow added successfully Workflow Name: " +workflowName);
	        
	    }
	    
		
}
