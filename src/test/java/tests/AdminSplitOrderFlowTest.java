package tests;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.*;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import utils.Config;
import utils.LoginUtil;

@Listeners(ExtentTestNGListener.class)
public class AdminSplitOrderFlowTest {

    String adminEmail = "splitautomation@yopmail.com";
    String adminPassword = "KestrelPro@123";
    String customerName = "Yadhi Automation";
    //String assigneeEmail = "TestAutomation258@yopmail.com";
    //String firstName = "Test";
    String lastName = "Automation";
    String roleAssociate = "EMPLOYEE";
    String phone = "0123567891";
    String timeZone = "(GMT+5:30) Kolkata, India";

    String firstName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like TestUser_1720090812345
    String assigneeEmail = firstName.toLowerCase() + "@yopmail.com"; // Ensure email is also unique
    String userId;
    Integer orderId;
    Integer stageIdToUpdate;
    List<Map<String, Object>> filteredStages1;

    @BeforeClass
    public void setupSession() {
    	System.out.println("\n=========================================================");
    	System.out.println("Starting Flow: ADMIN ORDER WITH SPLIT FLOW TEST");
    	System.out.println("=========================================================\n");
        String token = LoginUtil.performLogin(adminEmail, adminPassword);
        Config.setSessionToken(token);
        System.out.println("TEST PASSED: Admin logged in successfully with email: " +adminEmail);
    }

    @Test
    public void createOrder() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        String payload = "{" +
                "\"requestDate\":\"2025-06-17T09:00:00.000Z\"," +
                "\"consigneeDetails\":\"Indore Warehouse A1\"," +
                "\"palletTier\":\"2\"," +
                "\"palletType\":\"Euro\"," +
                "\"promiseDay\":\"2025-06-20T09:00:00.000Z\"," +
                "\"grade\":\"A+\"," +
                "\"salesCategory\":\"Industrial\"," +
                "\"packagingType\":\"Shrink Wrap\"," +
                "\"salesOrderLineNumber\":\"SO/9821\"," +
                "\"destination\":\"Indore Distribution Center\"," +
                "\"noOfRolls\":25," +
                "\"od\":45," +
                "\"coreId\":\"76\"," +
                "\"width\":120," +
                "\"length\":200," +
                "\"soNumber\":\"ORD123456\"," +
                "\"filmType\":\"LDPE\"," +
                "\"singleRollW\":50," +
                "\"soDate\":\"2025-06-17T09:00:00.000Z\"," +
                "\"umo\":\"Kg\"," +
                "\"soQuantity\":1000," +
                "\"region\":\"MADHYA PRADESH\"," +
                "\"customerCode\":\"CUST7890\"," +
                "\"customerOrganizationName\":\"Kestrel Industries Pvt. Ltd.\"," +
                "\"customerName\":\"" + customerName + "\"}";

        Response createResponse = OrderAPI.createOrder(payload);
        Assert.assertEquals(createResponse.getStatusCode(), 201);
        orderId = OrderVerificationAPI.getOrderIdByCustomerName(customerName);
        Assert.assertNotNull(orderId);
        //System.out.println("Original Order ID (before split): " + orderId);
        //System.out.println("Customer found in queue. Customer Name: " + customerName);
        System.out.println("TEST PASSED: Order created and verified in 'Queued' status for customer: " +customerName);
    }

    @Test(dependsOnMethods = "createOrder")
    public void splitOrder() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        // Call the split API
        Response splitResp = SplitAPI.splitOrderWithPayload(orderId);
        Assert.assertEquals(splitResp.getStatusCode(), 201, "Split API call failed!");

        // Fetch child orders created after split
        List<Integer> childOrders = OrderVerificationAPI.getQueuedOrdersByParentId(orderId);
        Assert.assertFalse(childOrders.isEmpty(), "No split child orders found!");

        // Use the first child order for further tests
        orderId = childOrders.get(0);
        //System.out.println("Using split child Order ID (Part 1): " + orderId);
        Response splitOrderResponse = SplitAPI.splitOrder(orderId, false);
        int statusCode = splitOrderResponse.getStatusCode();
        Assert.assertTrue(statusCode == 200 || statusCode == 201, "Split failed!");
        System.out.println("TEST PASSED: Split status is 'YES' and order split successfully");
    }


    @Test(dependsOnMethods = "splitOrder")
    public void updateTAT() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        List<Map<String, Object>> stages = StageAPI.getStageList(orderId);
        Map<String, Object> matchedStage = stages.stream()
                .filter(stage -> ((Number) stage.get("sequence")).intValue() == 2)
                .findFirst().orElse(null);
        Assert.assertNotNull(matchedStage);
        stageIdToUpdate = ((Number) matchedStage.get("id")).intValue();

        Response updateResponse = StageUpdateTATAPI.updateTAT(stageIdToUpdate, orderId, 2, 4);
        Assert.assertEquals(updateResponse.getStatusCode(), 200);
        System.out.println("TEST PASSED: TAT is updated successfully");
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
           // System.out.println("Assigned user to stage: " + assigneeNumber + ", Email: " + assigneeEmail);
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
           // System.out.println("Added remark from admin to stage: " + remarkNumber);
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

    @Test(dependsOnMethods = "addAndAssignUsers")
    public void completeStages() {
    	ExtentTest test = ExtentTestNGListener.getTest();
    	//System.out.println("Completing stages for orderId = " + orderId);
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            //System.out.println("Completing stages for orderId = " + orderId);
            Response response = StatusAPI.completeStage(orderId, stageId);
           
            Assert.assertEquals(response.getStatusCode(), 200);
        }
        System.out.println("TEST PASSED: Stages 2 to 7 marked as 'Completed' successfully");
    }

    @Test(dependsOnMethods = "completeStages")
    public void verifyCompletion() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        Integer orderIdCompleted = OrderVerificationAPI.getOrderIdByCustomerNameCompleted(customerName);
        Assert.assertNotNull(orderIdCompleted, "Order not found in COMPLETED list for customer: " + customerName);
        //System.out.println("Order found in completed. Customer Name: " + customerName);
        System.out.println("TEST PASSED: Order verified in 'Completed' list for customer:: " + customerName);
        Thread.sleep(2000);
        
        //Response countResponse = DashboardAPI.getDashboardCounts();
        //System.out.println("Final Dashboard Counts: " + countResponse.asPrettyString());
    }



    @Test
    public void downloadcompleteOnTimeReportTest() throws IOException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        Response response = ReportAPI.downloadCompleteOnTimeReport("test-output/completeOnTime-report.csv");
        Assert.assertEquals(response.statusCode(), 200);
        System.out.println("TEST PASSED: Complete on time report downloaded successfully");
    }

    @Test
    public void downloadcompleteWithDelayReportTest() throws IOException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        Response response = ReportAPI.downloadCompleteWithDelayReport("test-output/completeWithDelay-report.csv");
        Assert.assertEquals(response.statusCode(), 200);
        System.out.println("TEST PASSED: Complete with delay report downloaded successfully");
    }
    
    @Test(dependsOnMethods = {"addAndAssignUsers", "verifyCompletion"})
	public void updateUser() {
		ExtentTest test = ExtentTestNGListener.getTest();
	    String identityId = userId; // Or get it from user creation
	    Response response = UserAPI.updateUser(identityId, firstName, "AutomationUpdate", phone, timeZone);

	    Assert.assertEquals(response.getStatusCode(), 200, "User update failed!");
	    boolean isUpdated = response.jsonPath().getBoolean("is_updated");
	    Assert.assertTrue(isUpdated, "Update flag not true!");

	    System.out.println("TEST PASSED: User updated successfully");
	}
}