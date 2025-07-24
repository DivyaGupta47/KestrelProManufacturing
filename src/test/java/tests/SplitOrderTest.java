package tests;

import apis.SplitAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
/**
 * Test class to validate the order splitting functionality via API.
 * 
 * This class performs:
 * - Splitting an already created order using its `orderId`.
 * - Verifying that the split operation returns a successful response (HTTP 200 or 201).
 */
public class SplitOrderTest extends BaseFlowData {
	   @Test
    public void splitOrder() {
        //Response response = SplitAPI.splitOrder(orderId, false);
        //Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
        
        Assert.assertNotNull(orderId, "orderId not found. Did OrderCreationTest run first?");
        Response response = SplitAPI.splitOrder(orderId, false);
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201, "Split failed");
        System.out.println("Split successful for order ID: " + orderId);
    }
}
