package tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
import io.restassured.response.Response;
import utils.Config;
import utils.LoginUtil;

public class AdminOrderFlow {

    @Test
    public void createOrderWithSessionAndVerifyInQueue() throws InterruptedException {
        String customerName = "Vedansh Automation";
        String email = "TestAutomation20@yopmail.com";
        String firstName = "Test";
        String lastName = "Automation";
        String roleAssociate = "EMPLOYEE";
        String roleProjectManager = "PROJECT MANAGER";
        String phone = "0123567891";
       // String firstName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like TestUser_1720090812345
        String assigneeEmail = firstName.toLowerCase() + "@yopmail.com"; // Ensure email is also unique
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

        // Step 1: Create the order
        Response createResponse = OrderAPI.createOrder(payload);
        System.out.println("Create Status Code: " + createResponse.getStatusCode());
        System.out.println("Create Customer Response:\n" + createResponse.getBody().asString());
        Assert.assertEquals(createResponse.getStatusCode(), 201, "Order creation failed!");
              
       //Step 2: Get orderId
        Integer orderId = OrderVerificationAPI.getOrderIdByCustomerName(customerName);
        Assert.assertNotNull(orderId, "Order not found in QUEUED list for customer: " + customerName);
        System.out.println("Order found in queue. Order ID: " + orderId);
        System.out.println("Customer found in queue. Customer Name: " + customerName);
 
        Thread.sleep(3000);
        
        // Step 2.1: Split the order
        Response splitOrderResponse = SplitAPI.splitOrder(orderId, false);
        System.out.println("Split Order Status Code: " + splitOrderResponse.getStatusCode());

        int statusCode = splitOrderResponse.getStatusCode();
        Assert.assertTrue(statusCode == 200 || statusCode == 201, "Order split failed! Status code: " + statusCode);
      
        // Step 3: Get stageId
        Integer stageId = StageAPI.getFirstStageId(orderId);
        Assert.assertNotNull(stageId, "Stage ID not found for Order ID: " + orderId);
        System.out.println("Stage ID found: " + stageId);
        
       //Step 4: Get the stage with sequence = 2
        List<Map<String, Object>> stages = StageAPI.getStageList(orderId);
        Map<String, Object> matchedStage = stages.stream()
            .filter(stage -> ((Number) stage.get("sequence")).intValue() == 2)
            .findFirst()
            .orElse(null);

        Assert.assertNotNull(matchedStage, "Stage with sequence 2 not found!");
        Integer stageIdToUpdate = ((Number) matchedStage.get("id")).intValue();
        System.out.println("Stage ID for sequence 2: " + stageIdToUpdate);
        
        // Step 5: Update TAT
        int newDuration = 4;
        Response updateResponse = StageUpdateTATAPI.updateTAT(stageIdToUpdate, orderId, 2, newDuration);
        System.out.println("TAT Update Status Code: " + updateResponse.getStatusCode());
        //System.out.println("TAT Update Response: " + updateResponse.getBody().asString());
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "Failed to update TAT!");
        Thread.sleep(2000);        
        
        //Step 6: Add User          
        Response userResponse = UserAPI.createUserAssociate(email, firstName, lastName,roleAssociate,phone);
        //System.out.println("User creation response: " + userResponse.getBody().asPrettyString());
        Assert.assertEquals(userResponse.statusCode(), 200, "User creation failed!");

        String identityId = userResponse.jsonPath().getString("identity_id");
        System.out.println("New Member Identity ID: " + identityId);

        String userId = identityId;
       // String userId = "a25a8aca-3de6-4782-a7c3-599a13b2afe3"; // Active user(divyaas1@yopmail.com)
    
        //getting all stages id list from 2 to 7
        List<Map<String, Object>> stages1 = StageAPI.getStageList(orderId);

        List<Map<String, Object>> filteredStages1 = stages1.stream()
            .filter(stage -> {
                int seq = ((Number) stage.get("sequence")).intValue();
                return seq >= 2 && seq <= 7;
            })
            .toList();

      //Step 6: Add assignee to all stages stage 2 to stage 7
        int assigneeNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageIdToAssign = ((Number) stage.get("id")).intValue();
            //System.out.println("Order ID: " + orderId);
            //System.out.println("Stage ID: " + stageIdToAssign);
            //System.out.println("User ID: " + userId);
            Response resp = AssigneeAPI.assignStageToOrder(orderId, stageIdToAssign, List.of(userId));
            
            Assert.assertEquals(resp.getStatusCode(), 200, "Failed to assign user to stage " + stageIdToAssign);
            System.out.println("Assigned user to stage: " + assigneeNumber + ", Email: " + email);
            assigneeNumber++;
        }  
        
      //Step 6: Add Remarks to all stages stage 2 to stage 7
        int remarkNumber = 2; // Start from sequence 2

        for (Map<String, Object> stage : filteredStages1) {
            int stageId1 = ((Number) stage.get("id")).intValue();
            String comment = "Test Remark Stage " + remarkNumber;

            Response remarkResponse = RemarkAPI.addRemark(orderId, stageId1, comment);

            int status = remarkResponse.getStatusCode();
            Assert.assertTrue(status == 200 || status == 201,
                "Failed to add remark to stage " + stageId1 + " — Status: " + status);

            //System.out.println("Added remark to stage: " + stageId1);
            System.out.println("Added remark to stage: " + remarkNumber);
            remarkNumber++;
        }
        //Step 7: Add Attachment to all stages stage 2 to stage 7
        int attachmentNumber = 2; // Starting from sequence 2

        for (Map<String, Object> stage : filteredStages1) {
            int stageId1 = ((Number) stage.get("id")).intValue();
            String attachmentText = "Test Attachment Stage " + attachmentNumber;

            Response attachmentResponse = AttachmentAPI.addAttachment(orderId, stageId1, attachmentText);

            int status = attachmentResponse.getStatusCode();
            Assert.assertTrue(status == 200 || status == 201,
                "Failed to add attachment to stage " + stageId1 + " — Status: " + status);

            //System.out.println("Added attachment to stage: " + stageId1);
            System.out.println("Added attachment to stage: " + attachmentNumber);
            attachmentNumber++;
        }
    }
    
    @BeforeClass
    public void setupSession() {
        String token = LoginUtil.performLogin("automation@yopmail.com", "KestrelPro@123");
        Config.setSessionToken(token);
    }
}