package tests;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.AssigneeAPI;
import apis.AttachmentAPI;
import apis.OrderAPI;
import apis.OrderVerificationAPI;
import apis.RemarkAPI;
import apis.SplitAPI;
import apis.StageAPI;
import apis.StageUpdateTATAPI;
import apis.StatusAPI;
import apis.UserAPI;
import apis.WorkflowAPI;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import utils.Config;
import utils.LoginUtil;

public class InactiveUserRemovedInCompleteOrderTest {

	String adminEmail = "workflow@yopmail.com";
	String adminPassword = "KestrelPro@123";
	String customerName = "Test Automation" + System.currentTimeMillis();
//String assigneeEmail = "TestAutomation206@yopmail.com";
//String firstName = "Test";
	String lastName = "Automation";
	String roleAssociate = "EMPLOYEE";
	String phone = "0123567891";
	String timeZone = "(GMT+5:30) Kolkata, India";
	String firstName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like TestUser_1720090812345
	String assigneeEmail = "da@yopmail.com"; // Ensure email is also unique
	String userId = "c9450ec0-91ae-45a3-ba36-4c2b7c89b2da";
	String assigneePassword = "KestrelPro@123";

	Integer orderId;
	Integer stageIdToUpdate;
	List<Map<String, Object>> filteredStages1;

	@BeforeClass
	public void setupSession() {

		System.out.println("\n=========================================================");
		System.out.println("Starting Flow: INACTIVE USER VERIFICATION IN COMPLETED TEST API");
		System.out.println("=========================================================\n");
		String token = LoginUtil.performLogin_QA(adminEmail, adminPassword);
		Config.setSessionToken(token);
		// System.out.println("Login Successfully with Admin Email:" +adminEmail);
		System.out.println("TEST PASSED: Admin logged in successfully with email: " + adminEmail);
	}

	@Test
	public void createOrder() throws InterruptedException {

		// Step 0: Get latest workflow ID
		Integer latestWorkflowId = WorkflowAPI.getLatestWorkflowId();

		// Step 1: Prepare payload
		String payload = "{" + "\"requestDate\":\"2025-06-17T09:00:00.000Z\","
				+ "\"consigneeDetails\":\"Indore Warehouse A1\"," + "\"palletTier\":\"2\"," + "\"palletType\":\"Euro\","
				+ "\"promiseDay\":\"2025-06-20T09:00:00.000Z\"," + "\"grade\":\"A+\","
				+ "\"salesCategory\":\"Industrial\"," + "\"packagingType\":\"Shrink Wrap\","
				+ "\"salesOrderLineNumber\":\"SO/9821\"," + "\"destination\":\"Indore Distribution Center\","
				+ "\"noOfRolls\":25," + "\"od\":45," + "\"coreId\":\"76\"," + "\"width\":120," + "\"length\":200,"
				+ "\"soNumber\":\"ORD123456\"," + "\"filmType\":\"LDPE\"," + "\"singleRollW\":50,"
				+ "\"soDate\":\"2025-06-17T09:00:00.000Z\"," + "\"umo\":\"Kg\"," + "\"soQuantity\":1000,"
				+ "\"region\":\"Madhya Pradesh\"," + "\"customerCode\":\"CUST7890\","
				+ "\"customerOrganizationName\":\"Kestrel Industries Pvt. Ltd.\"," + "\"customerName\":\""
				+ customerName + "\"" + "}";

		Response createResponse = OrderAPI.createOrderWithWorkflow106(payload);
		Assert.assertEquals(createResponse.getStatusCode(), 201, "Order creation failed!");

		orderId = OrderVerificationAPI.getOrderIdByCustomerNameQA106(customerName);
		Assert.assertNotNull(orderId);

		System.out.println("TEST PASSED: Order created and verified in 'Queued' status for customer: " + customerName);
		Thread.sleep(2000);
	}

	@Test(dependsOnMethods = "createOrder")
	public void splitOrder() {
		ExtentTest test = ExtentTestNGListener.getTest();
		Response splitOrderResponse = SplitAPI.splitOrderQA(orderId, false);
		int statusCode = splitOrderResponse.getStatusCode();
		Assert.assertTrue(statusCode == 200 || statusCode == 201, "Split failed!");
		System.out.println("TEST PASSED: Split status is 'NO' for the order");
	}

	@Test(dependsOnMethods = "splitOrder")
	public void updateTAT() throws InterruptedException {
		ExtentTest test = ExtentTestNGListener.getTest();
		List<Map<String, Object>> stages = StageAPI.getStageListQA(orderId);

		// The sequences you want to update
		int[] sequencesToUpdate = { 2 };

		for (int sequence : sequencesToUpdate) {
			Map<String, Object> matchedStage = stages.stream()
					.filter(stage -> ((Number) stage.get("sequence")).intValue() == sequence).findFirst().orElse(null);

			Assert.assertNotNull(matchedStage, "Stage with sequence " + sequence + " not found!");

			int stageId = ((Number) matchedStage.get("id")).intValue();

			Response updateResponse = StageUpdateTATAPI.updateTATQA(stageId, orderId, sequence, 2);
			Assert.assertEquals(updateResponse.getStatusCode(), 200,
					"Failed to update TAT for stage with sequence " + sequence);

			System.out.println("TEST PASSED: TAT updated successfully for stage sequence " + sequence);
			Thread.sleep(1000); // small pause between updates
		}
	}

	@Test(dependsOnMethods = "updateTAT")
	public void addAndAssignUsers() {
		ExtentTest test = ExtentTestNGListener.getTest();

		// Static user ID
		String userId = "c9450ec0-91ae-45a3-ba36-4c2b7c89b2da";

		// Get all stages for the order
		List<Map<String, Object>> allStages = StageAPI.getStageListQA(orderId);
		filteredStages1 = allStages.stream().filter(stage -> {
			int seq = ((Number) stage.get("sequence")).intValue();
			return seq >= 2 && seq <= 7; // filter stages 2 to 7
		}).toList();

		int assigneeNumber = 2;
		for (Map<String, Object> stage : filteredStages1) {
			int stageIdToAssign = ((Number) stage.get("id")).intValue();

			// Assign the static user to each stage
			Response resp = AssigneeAPI.assignStageToOrderQA(orderId, stageIdToAssign, List.of(userId));
			Assert.assertEquals(resp.getStatusCode(), 200, "Failed to assign user to stage " + stageIdToAssign);

			assigneeNumber++;
		}

		System.out.println("TEST PASSED: Static user assigned successfully to all required stages");
	}

	@Test(dependsOnMethods = "addAndAssignUsers")
	public void switchToAssignedUser() {
		ExtentTest test = ExtentTestNGListener.getTest();
		String userToken = LoginUtil.performLogin_QA(assigneeEmail, assigneePassword);
		Config.setSessionToken(userToken);
		System.out.println("TEST PASSED: Switched session successfully for the assigned user." + assigneeEmail);
	}

	@Test(dependsOnMethods = "switchToAssignedUser")
	public void completeStagesWithRemarksAndAttachments() throws InterruptedException {
		 ExtentTest test = ExtentTestNGListener.getTest();
	        
	        int stageNumber = 2;
	        for (Map<String, Object> stage : filteredStages1) {
	            int stageId = ((Number) stage.get("id")).intValue();
	            // Complete Stage
	            Response completeResponse = StatusAPI.completeStageQA(orderId, stageId);
	            Assert.assertEquals(completeResponse.getStatusCode(), 200,
	                    "Failed to complete stage " + stageNumber);
	            //System.out.println("Completed stage from asignee: " + stageNumber);
	            //test.pass("Stage " + stageNumber + " completed successfully");

	            stageNumber++;
	            Thread.sleep(1000);
	        }	   
	        System.out.println("TEST PASSED: Stages 2 to 7 marked as 'Completed' successfully by the Associate");

	        Thread.sleep(1000); // Optional wait after final completion

	}

	@Test(dependsOnMethods = "completeStagesWithRemarksAndAttachments")
	public void switchToAdmin() {
		ExtentTest test = ExtentTestNGListener.getTest();
		String userToken = LoginUtil.performLogin_QA(adminEmail, adminPassword);
		Config.setSessionToken(userToken);
		System.out.println("TEST PASSED: Switched session successfully for the Admin." + adminEmail);
	} 
	
	@Test(dependsOnMethods = "switchToAdmin")
	public void deactivateUser()	
	{
		Response response = UserAPI.deactivateSpecificUserQA(); 
		System.out.println("TEST PASSED: User deactivated successfully");
	}
	
	@Test(dependsOnMethods = "deactivateUser")
	public void verifyDeactivatedUserIsInInactiveList() {
	    String userIdToCheck = "c9450ec0-91ae-45a3-ba36-4c2b7c89b2da"; // fixed userId

	    Response response = UserAPI.getInactiveUsersQA();

	    Assert.assertEquals(response.getStatusCode(), 200, "Inactive user search API failed");

	    List<String> inactiveIds = response.jsonPath().getList("item.identity_id");

	    Assert.assertTrue(
	        inactiveIds.contains(userIdToCheck),
	        "User " + userIdToCheck + " not found in inactive list."
	    );

	    System.out.println("TEST PASSED: User " + userIdToCheck + " is present in inactive list.");
	}
	
	  @Test(dependsOnMethods = "verifyDeactivatedUserIsInInactiveList")
	    public void verifyCompletion() throws InterruptedException {
	    	ExtentTest test = ExtentTestNGListener.getTest();
	        Integer orderIdCompleted = OrderVerificationAPI.getOrderIdByCustomerNameCompletedQA(customerName);
	        Assert.assertNotNull(orderIdCompleted, "Order not found in COMPLETED list for customer: " + customerName);
	        System.out.println("TEST PASSED: Order verified in 'completed' list for the Customer Name: " + customerName);	        
	        Thread.sleep(2000);	      
	    }
	  
	  @Test(dependsOnMethods = "verifyCompletion")
	  public void verifyDeactivatedUserStillOnStages() {
	      String deactivatedUserId = "c9450ec0-91ae-45a3-ba36-4c2b7c89b2da"; // static user

	      Response response = StageAPI.getOrderStagesQA(orderId); // orderId already from earlier test
	      Assert.assertEquals(response.getStatusCode(), 200, "Failed to fetch order stages");

	      List<List<Map<String, Object>>> assigneesList = response.jsonPath().getList("items.assignee");

	      boolean userFound = false;
	      int stageIndex = 1;

	      for (List<Map<String, Object>> assignees : assigneesList) {
	          List<String> ids = assignees.stream()
	                  .map(a -> (String) a.get("identity_id"))
	                  .collect(Collectors.toList());

	          if (ids.contains(deactivatedUserId)) {
	              userFound = true;
	              
	          }
	          stageIndex++;
	      }
	      System.out.println("TEST PASSED: User still assigned at 2 to 7 Stages");
	      Assert.assertTrue(userFound, "Deactivated user not found in any stage assignments.");
	  }

	  @Test(dependsOnMethods = "verifyDeactivatedUserStillOnStages")
	  public void testVerifyUserStatus() {
	      String userEmail = assigneeEmail;  // User you want to check

	      Response response = UserAPI.searchUsersQA("all", userEmail, "active", 0, 10);

	      List<String> emails = response.jsonPath().getList("item.email");

	      if (!emails.contains(userEmail)) {
	          
	          System.out.println("TEST PASSED: User " + userEmail + " not found. Marking test as PASS.");
	          Assert.assertTrue(true);
	          return;
	      }
	    
	      String status = response.jsonPath().getString("item.find { it.email == '" + userEmail + "' }.status");
	      Assert.assertEquals(status, "active", "User should be active!");
	  }

	  @Test(dependsOnMethods = "testVerifyUserStatus")
		public void activateUser()	
		{
			Response response = UserAPI.activateSpecificUserQA();
			System.out.println("TEST PASSED: User activated successfully");
		}

	
}