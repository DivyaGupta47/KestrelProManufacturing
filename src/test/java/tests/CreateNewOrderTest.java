package tests;

import apis.OrderAPI;
import apis.OrderVerificationAPI;
import apis.SplitAPI;
import apis.StageAPI;
import apis.StageUpdateTATAPI;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CreateNewOrderTest {

    @Test
    public void createOrderWithSessionAndVerifyInQueue() throws InterruptedException {
        String customerName = "moni";

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
        System.out.println("Create Response:\n" + createResponse.getBody().asString());
        Assert.assertEquals(createResponse.getStatusCode(), 201, "Order creation failed!");
              
     // Step 2: Get orderId
        Integer orderId = OrderVerificationAPI.getOrderIdByCustomerName(customerName);
        Assert.assertNotNull(orderId, "Order not found in QUEUED list for customer: " + customerName);
        System.out.println("Order found in queue. Order ID: " + orderId);
 
        Thread.sleep(3000);
        
        Response splitOrderResponse = SplitAPI.splitOrder(orderId, false);
        System.out.println("Split Order Status Code: " + splitOrderResponse.getStatusCode());
        System.out.println("Split Order Response: " + splitOrderResponse.getBody().asPrettyString());
        Assert.assertEquals(splitOrderResponse.getStatusCode(), 200, "Order split failed!");
       
        // Step 3: Get stageId
        Integer stageId = StageAPI.getFirstStageId(orderId);
        Assert.assertNotNull(stageId, "Stage ID not found for Order ID: " + orderId);
        System.out.println("Stage ID found: " + stageId);
        
     // Step 4: Get the stage with sequence = 2
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
        System.out.println("TAT Update Response: " + updateResponse.getBody().asString());
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "Failed to update TAT!");
        Thread.sleep(5000); 
   
 
    }
}