package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.TermConditionAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import utils.Config;
import utils.LoginUtil;
/**
 * AssigneeLogin handles login and terms acceptance for the assigned user.
 *
 * Test Flow:
 * - Performs login using assignee credentials and sets the session token
 * - Accepts terms and conditions using the user ID assigned during the admin flow
 *
 * Notes:
 * - Test methods are dependent; acceptTerms will only run if login is successful
 *
 * Author: QA@47Billion
 */

public class AssigneeLogin extends BaseFlowData {
	
	 @Test
	 public void loginAssignee() {
	        String token = LoginUtil.performLogin(assigneeEmail, assigneePassword);
	        Config.setSessionToken(token);
	        System.out.println("Switched session to assigned user." +assigneeEmail);
	    }
	 
	 @Test(dependsOnMethods = "loginAssignee")
	    public void acceptTerms() {
		 Assert.assertNotNull(userId, "userId is null. Make sure user was assigned in UserAssignmentTest.");    
	        Response resp = TermConditionAPI.acceptTerms(userId);
	        Assert.assertEquals(resp.getStatusCode(), 200);
	        System.out.println("Terms accepted");
	    }

}

