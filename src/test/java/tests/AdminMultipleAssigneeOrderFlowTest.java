package tests;
/**
 * Test class to automate the complete admin workflow for an order assigned to multiple assignees.
 *
 * Flow Overview:
 * - Logs in as an admin and creates a new order.
 * - Optionally splits the order and updates (TAT) for stage 2.
 * - Dynamically creates three users (assignees) and assigns them to multiple stages (2�7).
 * - Admin adds remarks and attachments to all stages in the filtered list.
 * - Each user accepts terms and performs their designated actions:
 *   - User 1 adds remarks.
 *   - User 2 adds attachments.
 *   - User 3 completes the stage.
 * Depends on:
 * - Working login API, order API, and user management APIs.
 * - Pre-configured roles and permissions for admin and assignees.
 *
 * Purpose:
 * - Validates the ability to assign multiple users to the same stages.
 * - Verifies role-based access and correct sequencing of stage-level actions by different users.
 * - Ensures backend workflow processing functions correctly with multiple assignees.
 */

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
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
import apis.TermConditionAPI;
import apis.UserAPI;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import utils.Config;
import utils.LoginUtil;
@Listeners(ExtentTestNGListener.class)
public class AdminMultipleAssigneeOrderFlowTest {

	String adminEmail = "multipleuseradmin@yopmail.com";
	String adminPassword = "KestrelPro@123";
	
    String customerName = "Kay Automation";
    //String assignee1Email = "user18@yopmail.com";
    //String assignee2Email = "user19@yopmail.com";
    //String assignee3Email = "user20@yopmail.com";
    
    String assignee_1Email = "AssigneeUser1_" + System.currentTimeMillis();
    String assignee_2Email = "AssigneeUser2_" + System.currentTimeMillis();
    String assignee_3Email = "AssigneeUser3_" + System.currentTimeMillis();
    
    String assignee1Email = assignee_1Email.toLowerCase() + "@yopmail.com";
    String assignee2Email = assignee_2Email.toLowerCase() + "@yopmail.com";
    String assignee3Email = assignee_3Email.toLowerCase() + "@yopmail.com";
    
    String assigneePassword = "KestrelPro@123";
    String firstName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like TestUser_1720090812345
    String assigneeEmail = firstName.toLowerCase() + "@yopmail.com"; // Ensure email is also unique
   // String firstName = "Test";
    String lastName = "Automation";
    String roleAssociate = "EMPLOYEE";
    String phone = "0123567891";
    String timeZone="(GMT+5:30) Kolkata, India";
    String userId;

    String assignee1Id;
    String assignee2Id;
    String assignee3Id;

    Integer orderId;
    Integer stageIdToUpdate;
    List<Map<String, Object>> filteredStages1;

    @BeforeClass
    public void setupSession() {
    	System.out.println("\n=========================================================");
    	System.out.println("Starting Flow: ADMIN MULTIPLE ASIGNEE ORDER FLOW TEST");
    	System.out.println("=========================================================\n");
        String token = LoginUtil.performLogin(adminEmail,adminPassword);
        Config.setSessionToken(token);
        System.out.println("Login Successfully with Admin Email: " +adminEmail);
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
        System.out.println("Create Status Code: " + createResponse.getStatusCode());
        System.out.println("Create Customer Response:\n" + createResponse.getBody().asString());
        orderId = OrderVerificationAPI.getOrderIdByCustomerName(customerName);
        Assert.assertNotNull(orderId);
        System.out.println("Order ID: " + orderId); 
        System.out.println("Customer found in queue. Customer Name: " + customerName);
        Thread.sleep(2000);
    }

    @Test(dependsOnMethods = "createOrder")
    public void splitOrder() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        Response splitOrderResponse = SplitAPI.splitOrder(orderId, false);
        int statusCode = splitOrderResponse.getStatusCode();
        Assert.assertTrue(statusCode == 200 || statusCode == 201, "Split failed!");
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
        System.out.println("TAT Update Status Code: " + updateResponse.getStatusCode());
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "Failed to update TAT!");
        Thread.sleep(2000);
    }
    
    @Test(dependsOnMethods = "updateTAT")
    public void createAndAssignMultipleUsers() {
        assignee1Id = createUser(assignee1Email, "User1");
        assignee2Id = createUser(assignee2Email, "User2");
        assignee3Id = createUser(assignee3Email, "User3");

        List<Map<String, Object>> allStages = StageAPI.getStageList(orderId);
        filteredStages1 = allStages.stream()
            .filter(stage -> {
                int seq = ((Number) stage.get("sequence")).intValue();
                return seq >= 2 && seq <= 7;
            }).toList();

        for (Map<String, Object> stage : filteredStages1) {
            int stageIdToAssign = ((Number) stage.get("id")).intValue();
            Response assignResponse = AssigneeAPI.assignStageToOrder(orderId, stageIdToAssign,
                List.of(assignee1Id, assignee2Id, assignee3Id));
            Assert.assertEquals(assignResponse.getStatusCode(), 200);
        }
    }

    private String createUser(String email, String firstName) {
        Response response = UserAPI.createUserAssociate(email, firstName, lastName, roleAssociate, phone);
        Assert.assertEquals(response.getStatusCode(), 200);
        return response.jsonPath().getString("identity_id");
    }
    
    @Test(dependsOnMethods = "createAndAssignMultipleUsers")
    public void addRemarks() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        int remarkNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            String comment = "Test Remark from admin Stage " + remarkNumber;
            Response response = RemarkAPI.addRemark(orderId, stageId, comment);
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
            System.out.println("Added remark from admin to stage: " + remarkNumber);
            remarkNumber++;
        }
    }

    @Test(dependsOnMethods = "addRemarks")
    public void addAttachments() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        int attachmentNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            String comment = "Test Attachment from admin Stage " + attachmentNumber;
            Response response = AttachmentAPI.addAttachment(orderId, stageId, comment);
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
            System.out.println("Added attachment from admin to stage: " + attachmentNumber);
            attachmentNumber++;
        }
    }

    @Test(dependsOnMethods = "addAttachments")
    public void acceptTermsForAllUsers() {
        acceptTerms(assignee1Email, assigneePassword, assignee1Id);
        acceptTerms(assignee2Email, assigneePassword, assignee2Id);
        acceptTerms(assignee3Email, assigneePassword, assignee3Id);
    }

    private void acceptTerms(String email, String pass, String userId) {
        String token = LoginUtil.performLogin(email, pass);
        Config.setSessionToken(token);
        Response resp = TermConditionAPI.acceptTerms(userId);
        Assert.assertEquals(resp.getStatusCode(), 200);
        System.out.println("Terms accepted for: " + email);
    }
    @Test(dependsOnMethods = "acceptTermsForAllUsers")
    public void performStageActionsAsMultipleUsers() throws InterruptedException {
        int stageNumber = 2;

        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();

            // User 1: Add remark
            switchUserSession(assignee1Email, assigneePassword);
            Response remarkResponse = RemarkAPI.addRemark(orderId, stageId, "Remark added by assignee1Email to Stage " + stageNumber);
            Assert.assertTrue(remarkResponse.getStatusCode() == 200 || remarkResponse.getStatusCode() == 201);
            System.out.println("assignee1Email added remark at stage: " + stageNumber);

            // User 2: Add attachment
            switchUserSession(assignee2Email, assigneePassword);
            Response attachmentResponse = AttachmentAPI.addAttachment(orderId, stageId, "Attachment added by assignee2Email to Stage " + stageNumber);
            Assert.assertTrue(attachmentResponse.getStatusCode() == 200 || attachmentResponse.getStatusCode() == 201);
            System.out.println("assignee2Email added attachment at stage: " + stageNumber);

            // User 3: Complete stage
            switchUserSession(assignee3Email, assigneePassword);
            Response completeResponse = StatusAPI.completeStage(orderId, stageId);
            Assert.assertEquals(completeResponse.getStatusCode(), 200);
            System.out.println("completed by assignee3Email stage: " + stageNumber);

            stageNumber++;
            Thread.sleep(1000);
        }
    }

    private void switchUserSession(String email, String pass) {
        String token = LoginUtil.performLogin(email, pass);
        Config.setSessionToken(token);
    }
   
}
