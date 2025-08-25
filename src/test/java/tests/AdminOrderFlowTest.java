package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.AssigneeAPI;
import apis.AttachmentAPI;
import apis.DashboardAPI;
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
import pages.ProductFlowQueuedPage;
import utils.Config;
import utils.EmailUtil;
import utils.LoginUtil;
import utils.PayloadUtil;
/**
 * AdminOrderFlowTest performs a complete end-to-end test flow for the Admin user using API automation.
 *
 * Flow Overview:
 * - Admin logs in and initiates a session
 * - Creates a new order
 * - Splits the order (single order, not duplicated)
 * - Updates (TAT) for a specific stage
 * - Creates a new user (assignee) and assigns them to stages 2 to 7
 * - Adds remarks and attachments to the assigned stages
 * - Completes the assigned stages as admin
 * - Verifies that the order is moved to COMPLETED status
 * - Downloads reports for completed orders (on time and with delay)
 * - Updates the user profile after order completion
 * *
 * Dependencies:
 * - API endpoints must be active and functioning correctly
 * - Admin and assignee credentials should be valid
 *
 * Notes:
 * - All test methods are dependent on the successful execution of prior steps
 * - Tests will be skipped if a required preceding method fails
 *
 * Author: QA@47Billion
 */

@Listeners(ExtentTestNGListener.class)
public class AdminOrderFlowTest extends BaseTest {

	String adminEmail = "automation@yopmail.com";
	String adminPassword = "KestrelPro@123";
	
    String customerName = "Toshi Automation";
    //String assigneeEmail = "TestAutomation206@yopmail.com";
    //String firstName = "Test";
    String lastName = "Automation";
    String roleAssociate = "EMPLOYEE";
    String phone = "0123567891";
    String timeZone="(GMT+5:30) Kolkata, India";
    String firstName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like TestUser_1720090812345
    String assigneeEmail = firstName.toLowerCase() + "@yopmail.com"; // Ensure email is also unique
    String userId;

    Integer orderId;
    Integer stageIdToUpdate;
    List<Map<String, Object>> filteredStages1;
    
    LoginPageUi loginPage;
	String userName = "divyaadmin@yopmail.com";
	String password = "KestrelPro@123";

    @BeforeClass
    public void setupSession() {
    	System.out.println("\n=========================================================");
    	System.out.println("Starting Flow: ADMIN ORDER FLOW TEST");
    	System.out.println("=========================================================\n");
        String token = LoginUtil.performLogin(adminEmail,adminPassword);
        Config.setSessionToken(token);
        System.out.println("TEST PASSED: Admin logged in successfully with email: " +adminEmail);
        loginPage = new LoginPageUi(driver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
        System.out.println("TEST PASSED: UI Login done: " + userName);
    }

   
    @Test
    public void createOrder() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
    	//String payload = PayloadUtil.loadJsonAsString("payloads/create_order.json");
        String payload = "{"
            + "\"requestDate\":\"2025-06-17T09:00:00.000Z\","
            + "\"consigneeDetails\":\"Indore Warehouse A1\","
            + "\"palletTier\":\"2\","
            + "\"palletType\":\"Euro\","
            + "\"promiseDay\":\"2025-06-20T09:00:00.000Z\","
            + "\"grade\":\"A+\","
            + "\"salesCategory\":\"Industrial\","
            + "\"packagingType\":\"Shrink Wrap\","
            + "\"salesOrderLineNumber\":\"SO/9821\","
            + "\"destination\":\"Indore Distribution Center\","
            + "\"noOfRolls\":25,"
            + "\"od\":45,"
            + "\"coreId\":\"76\","
            + "\"width\":120,"
            + "\"length\":200,"
            + "\"soNumber\":\"ORD123456\","
            + "\"filmType\":\"LDPE\","
            + "\"singleRollW\":50,"
            + "\"soDate\":\"2025-06-17T09:00:00.000Z\","
            + "\"umo\":\"Kg\","
            + "\"soQuantity\":1000,"
            + "\"region\":\"Madhya Pradesh\","
            + "\"customerCode\":\"CUST7890\","
            + "\"customerOrganizationName\":\"Kestrel Industries Pvt. Ltd.\","
            + "\"customerName\":\"" + customerName + "\""
            + "}";

        Response createResponse = OrderAPI.createOrder(payload);
        Assert.assertEquals(createResponse.getStatusCode(), 201, "Order creation failed!");
        //System.out.println("Create Status Code: " + createResponse.getStatusCode());
        //System.out.println("Create Customer Response:\n" + createResponse.getBody().asString());
        orderId = OrderVerificationAPI.getOrderIdByCustomerName(customerName);
        Assert.assertNotNull(orderId);
        //System.out.println("Order ID: " + orderId); 
        //System.out.println("Customer found in queue. Customer Name: " + customerName);
        System.out.println("TEST PASSED: Order created and verified in 'Queued' status for customer: " +customerName);
        Thread.sleep(2000);
    }
    
    @Test(dependsOnMethods = "createOrder")
	public void verifyOrderInUI() throws InterruptedException {
		ProductFlowQueuedPage productFlowQueuedPage = new ProductFlowQueuedPage(driver);
		driver.navigate().refresh(); // or click the “Queued” tab again
		Thread.sleep(1000); // small pause
		Assert.assertTrue(productFlowQueuedPage.searchOrderinQueued(customerName), "Customer name does not match in UI!");
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
            .filter(stage -> ((Number) stage.get("sequence")).intValue() == 2)
            .findFirst().orElse(null);

        Assert.assertNotNull(matchedStage, "Stage with sequence 2 not found!");
        stageIdToUpdate = ((Number) matchedStage.get("id")).intValue();

        Response updateResponse = StageUpdateTATAPI.updateTAT(stageIdToUpdate, orderId, 2, 4);
        //System.out.println("TAT Update Status Code: " + updateResponse.getStatusCode());
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
        //System.out.println("New Member User ID: " + userId);
        System.out.println("TEST PASSED: User added successfully");
        List<Map<String, Object>> allStages = StageAPI.getStageList(orderId);
        filteredStages1 = allStages.stream()
            .filter(stage -> {
                int seq = ((Number) stage.get("sequence")).intValue();
                return seq >= 2 && seq <= 7;
            }).toList();

        int assigneeNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageIdToAssign = ((Number) stage.get("id")).intValue();
            Response resp = AssigneeAPI.assignStageToOrder(orderId, stageIdToAssign, List.of(userId));
            Assert.assertEquals(resp.getStatusCode(), 200);
            //System.out.println("Assigned user to stage: " + assigneeNumber + ", Email: " + assigneeEmail);
            assigneeNumber++;
        }
        System.out.println("TEST PASSED: Associate assigned successfully");
    }

    @Test(dependsOnMethods = "addAndAssignUsers")
    public void addRemarks() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        int remarkNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            String comment = "Test Remark from admin to Stage " + remarkNumber;
            Response response = RemarkAPI.addRemark(orderId, stageId, comment);
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
            //System.out.println("Added remark from admin to stage: " + remarkNumber);
            remarkNumber++;
        }
        System.out.println("TEST PASSED: Remarks for stages 2 to 7 added successfully by Admin");
    }

    @Test(dependsOnMethods = "addRemarks")
    public void addAttachments() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        int attachmentNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            String comment = "Test Attachment from admin to Stage " + attachmentNumber;
            Response response = AttachmentAPI.addAttachment(orderId, stageId, comment);
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
            //System.out.println("Added attachment from admin to stage: " + attachmentNumber);
            attachmentNumber++;
        }
        System.out.println("TEST PASSED: Attachments for stages 2 to 7 added successfully by Admin");
    }

    @Test(dependsOnMethods = "addAttachments")
    public void completeStages() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        int completeNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            Response response = StatusAPI.completeStage(orderId, stageId);
            Assert.assertEquals(response.getStatusCode(), 200);
            //System.out.println("Completed from admin to stage: " + completeNumber);
            completeNumber++;
        }
        Thread.sleep(2000);
        System.out.println("TEST PASSED: All 2 to 7 stages status set to completed successfully");
    }

    @Test(dependsOnMethods = "completeStages")
    public void verifyCompletion() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        Integer orderIdCompleted = OrderVerificationAPI.getOrderIdByCustomerNameCompleted(customerName);
        Assert.assertNotNull(orderIdCompleted, "Order not found in COMPLETED list for customer: " + customerName);
        System.out.println("TEST PASSED: Order verified in 'completed' list for the Customer Name: " + customerName);
        
        Thread.sleep(2000);
        
        //Response countResponse = DashboardAPI.getDashboardCounts();
        //System.out.println("Final Dashboard Counts: " + countResponse.asPrettyString());
    }
    
    @Test(dependsOnMethods = "verifyCompletion")
	public void verifyOrderInUICompleted() throws InterruptedException {
		ProductFlowCompletePage productFlowCompletePage = new ProductFlowCompletePage(driver);
		driver.navigate().refresh(); // or click the “Queued” tab again
		Thread.sleep(1000); // small pause
		Assert.assertTrue(productFlowCompletePage.searchOrderinCompleted(customerName), "Customer name does not match in UI!");
	    System.out.println("TEST PASSED: Order verified in UI for completed");
	}
	
	@Test(dependsOnMethods = {"addAndAssignUsers", "verifyOrderInUICompleted"})
	public void updateUser() {
		ExtentTest test = ExtentTestNGListener.getTest();
	    String identityId = userId; // Or get it from user creation
	    Response response = UserAPI.updateUser(identityId, firstName, "AutomationUpdate", phone, timeZone);

	    Assert.assertEquals(response.getStatusCode(), 200, "User update failed!");
	    boolean isUpdated = response.jsonPath().getBoolean("is_updated");
	    Assert.assertTrue(isUpdated, "Update flag not true!");

	    System.out.println("TEST PASSED: User updated successfully");
	}
	
	  
	 @Test(dependsOnMethods = "updateUser")
	public void downloadcompleteOnTimeReportTest() throws IOException {
    	ExtentTest test = ExtentTestNGListener.getTest();
	    String filePath = "test-output/completeOnTime-report.csv";
	    Response response = ReportAPI.downloadCompleteOnTimeReport(filePath);
	    Assert.assertEquals(response.statusCode(), 200, "Report download failed!");
	    System.out.println("TEST PASSED: Complete on time report downloaded successfully");
	}
	 
	 @Test(dependsOnMethods = "downloadcompleteOnTimeReportTest")
	public void downloadcompleteWithDelayReportTest() throws IOException {
		ExtentTest test = ExtentTestNGListener.getTest();
	    String filePath = "test-output/completeWithDelay-report.csv";
	    Response response = ReportAPI.downloadCompleteWithDelayReport(filePath);
	    Assert.assertEquals(response.statusCode(), 200, "Report download failed!");
	    System.out.println("TEST PASSED: Complete with delay report downloaded successfully");
	}
	
}
