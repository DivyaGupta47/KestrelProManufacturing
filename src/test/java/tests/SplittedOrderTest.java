package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.OrderVerificationAPI;
import apis.SplitAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;

public class SplittedOrderTest extends BaseFlowData{
	 @Test
	    public void splitOrder() {

	        // Call the split API
	        Response splitResp = SplitAPI.splitOrderWithPayload(orderId);
	        Assert.assertEquals(splitResp.getStatusCode(), 201, "Split API call failed!");

	        // Fetch child orders created after split
	        List<Integer> childOrders = OrderVerificationAPI.getQueuedOrdersByParentId(orderId);
	        Assert.assertFalse(childOrders.isEmpty(), "No split child orders found!");

	        // Use the first child order for further tests
	        orderId = childOrders.get(0);
	        System.out.println("Using split child Order ID (Part 1): " + orderId);
	        Response splitOrderResponse = SplitAPI.splitOrder(orderId, false);
	        int statusCode = splitOrderResponse.getStatusCode();
	        Assert.assertTrue(statusCode == 200 || statusCode == 201, "Split failed!");
	    }

}
