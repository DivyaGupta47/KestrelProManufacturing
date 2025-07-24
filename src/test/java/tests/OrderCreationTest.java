package tests;

import apis.OrderAPI;
import apis.OrderVerificationAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
/**
 * Test class responsible for creating an order via API and verifying its creation.
 * 
 * This class:
 * - Uses a static payload to send a POST request for order creation.
 * - Retrieves the created order ID using the customer name.
 * - Asserts that the API response and returned data are valid.
 */

public class OrderCreationTest extends BaseFlowData {


    @Test
    public void createOrder() {
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
        Response response = OrderAPI.createOrder(payload);
        Assert.assertEquals(response.getStatusCode(), 201);
        orderId = OrderVerificationAPI.getOrderIdByCustomerName(customerName);
        Assert.assertNotNull(orderId);
        System.out.println("Order Created, ID: " + orderId);
    }
}
