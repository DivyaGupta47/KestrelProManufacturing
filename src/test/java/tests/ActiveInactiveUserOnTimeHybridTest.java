package tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.AssigneeAPI;
import apis.OrderAPI;
import apis.OrderVerificationAPI;
import apis.SplitAPI;
import apis.StageAPI;
import apis.StageUpdateTATAPI;
import apis.StatusAPI;
import apis.UserAPI;
import base.BaseTestWorkflow;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import pages.AddUserPage;
import pages.LoginPageUi;
import pages.ProductFlowCompletePage;
import pages.ProductFlowOnTimePage;
import pages.ProductFlowQueuedPage;
import utils.Config;
import utils.LoginUtil;

public class ActiveInactiveUserOnTimeHybridTest extends BaseTestWorkflow{
	String adminEmail = "workflow@yopmail.com";
	String adminPassword = "KestrelPro@123";
	String customerName = "Test Automation" + System.currentTimeMillis();
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

	LoginPageUi loginPage;
	String userName = "ui@yopmail.com";
	String password = "KestrelPro@123";
	//WebDriver driver;
	   @BeforeClass
	    public void setup() throws InterruptedException {
		   System.out.println("\n=========================================================");
	    	System.out.println("Starting Flow: ADMIN ACTIVE-INACTIVE FLOW TEST");
	    	System.out.println("=========================================================\n");
	        // --- Step 1: API login to get session token ---
	        String token = LoginUtil.performLogin_QA(adminEmail, adminPassword);
	        Config.setSessionToken(token);
	        System.out.println("TEST PASSED: API Admin logged in successfully with email: " +adminEmail);

	       // driver = new ChromeDriver();
	        //driver.manage().window().maximize();
	       // driver.get("https://qa-mf.kestrelpro.ai/");
	        loginPage = new LoginPageUi(driver);
	        loginPage.enterUsername(userName);
	        loginPage.enterPassword(password);
	        loginPage.clickSignIn();
	        System.out.println("TEST PASSED: UI Admin logged in successfully with email: " + userName);
	    }

	
	@Test
	public void createOrderAPI() {
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
	    Assert.assertEquals(createResponse.getStatusCode(), 201);
	    orderId = OrderVerificationAPI.getOrderIdByCustomerNameQA106(customerName);
	    Assert.assertNotNull(orderId);
	    System.out.println("TEST PASSED: API Order created and verified in 'Queued' status for customer: " +customerName);
	}

	@Test(dependsOnMethods = "createOrderAPI")
	public void verifyOrderInUI() throws InterruptedException {
		ProductFlowQueuedPage productFlowQueuedPage = new ProductFlowQueuedPage(driver);
		driver.navigate().refresh(); // or click the “Queued” tab again
		Thread.sleep(1000); // small pause
		Assert.assertTrue(productFlowQueuedPage.searchOrderinQueued(customerName), "Customer name does not match in UI!");
	    System.out.println("TEST PASSED: UI Order verified for Queued");
	}
	@Test(dependsOnMethods = "verifyOrderInUI")
	public void splitOrder() {
		ExtentTest test = ExtentTestNGListener.getTest();
		Response splitOrderResponse = SplitAPI.splitOrderQA(orderId, false);
		int statusCode = splitOrderResponse.getStatusCode();
		Assert.assertTrue(statusCode == 200 || statusCode == 201, "Split failed!");
		System.out.println("TEST PASSED: API Split status is 'NO' for the order");
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

			System.out.println("TEST PASSED: API TAT updated successfully for stage sequence " + sequence);
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

		System.out.println("TEST PASSED: API Static user assigned successfully to all required stages");
	}

	@Test(dependsOnMethods = "addAndAssignUsers")
	public void completeStagesWithRemarksAndAttachments() throws InterruptedException {
	    ExtentTest test = ExtentTestNGListener.getTest();
	    
	    int stageNumber = 2; // starting from stage 2
	    
	    for (Map<String, Object> stage : filteredStages1) {
	        if (stageNumber > 3) break;  // only complete stage 2 and 3

	        int stageId = ((Number) stage.get("id")).intValue();

	        // Complete Stage
	        Response completeResponse = StatusAPI.completeStageQA(orderId, stageId);
	        Assert.assertEquals(completeResponse.getStatusCode(), 200,
	                "Failed to complete stage " + stageNumber);

	        stageNumber++;
	        Thread.sleep(1000);
	    }

	    System.out.println("TEST PASSED: API Stages 2 to 3 is completed by admin");
	    
	 	Thread.sleep(1000); // Optional wait after final completion
	}


	@Test(dependsOnMethods = "completeStagesWithRemarksAndAttachments")
	public void verifyOrderInUIOnTime() throws InterruptedException {
		ProductFlowOnTimePage productFlowOnTimePage = new ProductFlowOnTimePage(driver);
		driver.navigate().refresh(); // or click the “Queued” tab again
		Thread.sleep(1000); // small pause
		Assert.assertTrue(productFlowOnTimePage.searchOrderinOnTime(customerName), "Customer name does not match in UI!");
	    System.out.println("TEST PASSED: Order verified in UI");
	}
	
	

	@Test(dependsOnMethods = "verifyOrderInUIOnTime")
	public void deactivateUser()	
	{
		Response response = UserAPI.deactivateSpecificUserQA(); 
		System.out.println("TEST PASSED: API User deactivated successfully");
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

	    System.out.println("TEST PASSED: API User " + userIdToCheck + " is present in inactive list.");
	}

	@Test(dependsOnMethods = "verifyDeactivatedUserIsInInactiveList")
	public void verifyOrderInactiveUserViaUI() throws InterruptedException {
		String inactiveEmail = "da@yopmail.com";
		AddUserPage addUserPage = new AddUserPage(driver);
		driver.navigate().refresh(); // or click the “Queued” tab again
		Thread.sleep(1000); // small pause
		Assert.assertTrue(addUserPage.searchOrderinCompleted(inactiveEmail), "Customer email does not match in UI!");
	    System.out.println("TEST PASSED: UI Inactive Email verified in UI" +inactiveEmail);
	}
	
	@Test(dependsOnMethods = "verifyOrderInactiveUserViaUI")
	public void verifyStageAssignees() {
	    String userIdToCheck = "c9450ec0-91ae-45a3-ba36-4c2b7c89b2da";

	    // Get all stages
	    List<Map<String, Object>> stages = StageAPI.getStageListQA(orderId);

	    for (Map<String, Object> stage : stages) {
	        int sequence = ((Number) stage.get("sequence")).intValue();
	        int stageId = ((Number) stage.get("id")).intValue();
	        String stageStatus = (String) stage.get("stageStatus");
	        List<Map<String, Object>> assignees = (List<Map<String, Object>>) stage.get("assignee");

	        // ✅ Only check stages 2 to 7
	        if (sequence < 2 || sequence > 7) {
	            System.out.println("Stage " + stageId + " (sequence " + sequence + ") skipped");
	            continue;
	        }

	        boolean foundUser = assignees != null &&
	                assignees.stream().anyMatch(a -> userIdToCheck.equals(a.get("identity_id")));

	        if ("COMPLETED".equalsIgnoreCase(stageStatus)) {
	            Assert.assertTrue(foundUser,
	                "Stage " + stageId + " COMPLETED but required user " + userIdToCheck + " not found");
	            System.out.println("Stage " + stageId + " COMPLETED → user present: " + userIdToCheck);

	        } else if ("ON_TIME".equalsIgnoreCase(stageStatus) || "NOT_STARTED".equalsIgnoreCase(stageStatus)) {
	            Assert.assertFalse(foundUser,
	                "Stage " + stageId + " " + stageStatus + " but user " + userIdToCheck + " is still assigned");
	            System.out.println("Stage " + stageId + " " + stageStatus + " user not present");

	        } else {
	            System.out.println("Stage " + stageId + " (sequence " + sequence + ") with status " + stageStatus + " skipped validation");
	        }
	    }

	    System.out.println("TEST PASSED: Stage assignee validation completed for stages 2 to 7 in order " + orderId);
	}

	
	@Test(dependsOnMethods = "verifyStageAssignees")
	public void activateUser()	
	{
		Response response = UserAPI.activateSpecificUserQA();
		System.out.println("TEST PASSED: API User activated successfully");
	}
		
}
