package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.AssigneeAPI;
import apis.AttachmentAPI;
import apis.OrderAPI;
import apis.OrderVerificationAPI;
import apis.RemarkAPI;
import apis.ReportAPI;
import apis.SplitAPI;
import apis.StageAPI;
import apis.StageUpdateTATAPI;
import apis.StatusAPI;
import apis.UserAPI;
import base.BaseTest;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import pages.LoginPageUi;
import pages.ProductFlowCompletePage;
import pages.ProductFlowOnTimePage;
import pages.ProductFlowQueuedPage;
import utils.Config;
import utils.LoginUtil;

public class AdminOrderRejectFlowTest extends BaseTest {

	String adminEmail = "rejectautomation@yopmail.com";
	String adminPassword = "KestrelPro@123";

	String customerName = "Keshav Automation";
	// String assigneeEmail = "TestAutomation2004@yopmail.com";
	// String firstName = "Test";
	String lastName = "Automation";
	String roleAssociate = "EMPLOYEE";
	String phone = "0123567891";
	String timeZone = "(GMT+5:30) Kolkata, India";

	String userId;

	String firstName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like TestUser_1720090812345
	String assigneeEmail = firstName.toLowerCase() + "@yopmail.com"; // Ensure email is also unique

	Integer orderId;
	Integer stageIdToUpdate;
	List<Map<String, Object>> filteredStages1;

	LoginPageUi loginPage;
	String userName = "divyaadmin@yopmail.com";
	String password = "KestrelPro@123";

	@BeforeClass
	public void setupSession() {
		System.out.println("\n=========================================================");
		System.out.println("Starting Flow: ADMIN ORDER REJECT FLOW TEST");
		System.out.println("=========================================================\n");
		String token = LoginUtil.performLogin(adminEmail, adminPassword);
		Config.setSessionToken(token);
		System.out.println("TEST PASSED: Admin logged in successfully with email: " + adminEmail);
		loginPage = new LoginPageUi(driver);
		loginPage.enterUsername(userName);
		loginPage.enterPassword(password);
		loginPage.clickSignIn();
		System.out.println("TEST PASSED: UI Login done: " + userName);
	}

	@Test
	public void createOrder() throws InterruptedException {
		ExtentTest test = ExtentTestNGListener.getTest();
		// String payload = PayloadUtil.loadJsonAsString("payloads/create_order.json");
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

		Response createResponse = OrderAPI.createOrder(payload);
		Assert.assertEquals(createResponse.getStatusCode(), 201, "Order creation failed!");
		// System.out.println("Create Status Code: " + createResponse.getStatusCode());
		// System.out.println("Create Customer Response:\n" +
		// createResponse.getBody().asString());
		orderId = OrderVerificationAPI.getOrderIdByCustomerName(customerName);
		Assert.assertNotNull(orderId);
		// System.out.println("Order ID: " + orderId);
		// System.out.println("Customer found in queue. Customer Name: " +
		// customerName);
		System.out.println("TEST PASSED: Order created and verified in 'Queued' status for customer: " + customerName);
		Thread.sleep(2000);
	}

	@Test(dependsOnMethods = "createOrder")
	public void verifyOrderInUI() throws InterruptedException {
		ProductFlowQueuedPage productFlowQueuedPage = new ProductFlowQueuedPage(driver);
		driver.navigate().refresh(); // or click the “Queued” tab again
		Thread.sleep(1000); // small pause
		Assert.assertTrue(productFlowQueuedPage.searchOrderinQueued(customerName),
				"Customer name does not match in UI!");
		System.out.println("TEST PASSED: Order verified in UI for Queued");
	}

	@Test(dependsOnMethods = "verifyOrderInUI")
	public void splitOrder() {
		ExtentTest test = ExtentTestNGListener.getTest();
		Response splitOrderResponse = SplitAPI.splitOrder(orderId, false);
		int statusCode = splitOrderResponse.getStatusCode();
		Assert.assertTrue(statusCode == 200 || statusCode == 201, "Split failed!");
		System.out.println("TEST PASSED: Split status is 'NO' for the order");
	}

	@Test(dependsOnMethods = "splitOrder")
	public void updateTAT() throws InterruptedException {
		ExtentTest test = ExtentTestNGListener.getTest();
		List<Map<String, Object>> stages = StageAPI.getStageList(orderId);
		Map<String, Object> matchedStage = stages.stream()
				.filter(stage -> ((Number) stage.get("sequence")).intValue() == 2).findFirst().orElse(null);

		Assert.assertNotNull(matchedStage, "Stage with sequence 2 not found!");
		stageIdToUpdate = ((Number) matchedStage.get("id")).intValue();

		Response updateResponse = StageUpdateTATAPI.updateTAT(stageIdToUpdate, orderId, 2, 4);
		// System.out.println("TAT Update Status Code: " +
		// updateResponse.getStatusCode());
		System.out.println("TEST PASSED: TAT is updated successfully");
		Assert.assertEquals(updateResponse.getStatusCode(), 200, "Failed to update TAT!");
		Thread.sleep(2000);
	}

	@Test(dependsOnMethods = "updateTAT")
	public void addAndAssignUsers() {
		ExtentTest test = ExtentTestNGListener.getTest();
		Response userResponse = UserAPI.createUserAssociate(assigneeEmail, firstName, lastName, roleAssociate, phone);
		Assert.assertEquals(userResponse.getStatusCode(), 200, "User creation failed!");

		userId = userResponse.jsonPath().getString("identity_id");
		// System.out.println("New Member User ID: " + userId);
		System.out.println("TEST PASSED: User added successfully");
		List<Map<String, Object>> allStages = StageAPI.getStageList(orderId);
		filteredStages1 = allStages.stream().filter(stage -> {
			int seq = ((Number) stage.get("sequence")).intValue();
			return seq >= 2 && seq <= 7;
		}).toList();

		int assigneeNumber = 2;
		for (Map<String, Object> stage : filteredStages1) {
			int stageIdToAssign = ((Number) stage.get("id")).intValue();
			Response resp = AssigneeAPI.assignStageToOrder(orderId, stageIdToAssign, List.of(userId));
			Assert.assertEquals(resp.getStatusCode(), 200);
			// System.out.println("Assigned user to stage: " + assigneeNumber + ", Email: "
			// + assigneeEmail);
			assigneeNumber++;
		}
		System.out.println("TEST PASSED: Associate assigned successfully");
	}

	@Test(dependsOnMethods = "addAndAssignUsers")
	public void completeStages() throws InterruptedException {
		ExtentTest test = ExtentTestNGListener.getTest();

		for (Map<String, Object> stage : filteredStages1) {
			int sequence = ((Number) stage.get("sequence")).intValue();
			int stageId = ((Number) stage.get("id")).intValue();

			if (sequence == 2) {
				// Complete stage 2
				Response response = StatusAPI.completeStage(orderId, stageId);
				Assert.assertEquals(response.getStatusCode(), 200, "Failed to complete stage 2");
				// System.out.println("Completed Stage 2");
				System.out.println("TEST PASSED: Stage 2 completed successfully");

			} else if (sequence == 3) {
				// Reject stage 3
				Response rejectResponse = StatusAPI.rejectStage(orderId, stageId);
				Assert.assertEquals(rejectResponse.getStatusCode(), 200, "Failed to reject stage 3");
				System.out.println("TEST PASSED: Stage 3 rejected successfully with reason");

			}
		}

		Thread.sleep(2000); // optional wait
	}

	@Test(dependsOnMethods = "completeStages")
	public void verifyRejectOrder() throws InterruptedException {
		ExtentTest test = ExtentTestNGListener.getTest();
		Integer orderIdCompleted = OrderVerificationAPI.getOrderIdByCustomerNameOnTime(customerName);
		Assert.assertNotNull(orderIdCompleted, "Order not found in OnTIME list for customer: " + customerName);
		// System.out.println("Order found in on time. Customer Name: " + customerName);
		System.out
				.println("TEST PASSED: Order rejected and verified in 'On Time' status for customer: " + customerName);

		Thread.sleep(2000);

	}
	
	 @Test(dependsOnMethods = "verifyRejectOrder")
		public void verifyOrderInUIRejected() throws InterruptedException {
		 ProductFlowOnTimePage productFlowOnTimePage = new ProductFlowOnTimePage(driver);
			driver.navigate().refresh(); // or click the “Queued” tab again
			Thread.sleep(1000); // small pause
			Assert.assertTrue(productFlowOnTimePage.searchOrderinOnTime(customerName), "Customer name does not match in UI!");
		    System.out.println("TEST PASSED: Order verified in UI for rejected");
		}

}
