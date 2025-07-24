package tests;

import apis.StatusAPI;
import base.BaseFlowData;
import apis.DashboardAPI;
import apis.OrderVerificationAPI;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
/**
 * Test class to validate stage completion and order lifecycle transition via API.
 * 
 * This class performs:
 * - Completing all stages of an order sequentially using the stage IDs.
 * - Verifying that the order appears in the completed state after all stages are done.
 * - Fetching and displaying dashboard counts for confirmation.
 * 
 */
public class StageCompletionTest extends BaseFlowData {

@Test
    public void completeStages() {
        int number = 2;
        for (Map<String, Object> stage : filteredStages) {
            int stageId = ((Number) stage.get("id")).intValue();
            Response response = StatusAPI.completeStage(orderId, stageId);
            Assert.assertEquals(response.getStatusCode(), 200);
            System.out.println("Completed from admin to stage: " + number);
            number++;
        }
    }

    @Test(dependsOnMethods = "completeStages")
    public void verifyCompletion() throws InterruptedException {
        Integer completedId = OrderVerificationAPI.getOrderIdByCustomerNameCompleted(customerName);
        Assert.assertNotNull(completedId);
        System.out.println("Order found in completed. Customer Name: " + customerName);
        
        Thread.sleep(2000);
        Response countResponse = DashboardAPI.getDashboardCounts();
        System.out.println("Final Dashboard Counts: " + countResponse.asPrettyString());
    }
}
