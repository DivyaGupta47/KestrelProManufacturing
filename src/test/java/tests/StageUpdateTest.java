package tests;

import apis.StageAPI;
import apis.StageUpdateTATAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
/**
 * Test class to validate updating (TAT) for a specific order stage via API.
 * 
 * This class performs:
 * - Fetching the list of stages for a given order.
 * - Identifying the stage with sequence number 2 (typically the second actionable stage).
 * - Updating the TAT for that stage using the StageUpdateTATAPI.
 */

public class StageUpdateTest extends BaseFlowData {

    @Test
    public void updateTAT() {
        List<Map<String, Object>> stages = StageAPI.getStageList(orderId);
        Map<String, Object> matchedStage = stages.stream()
            .filter(stage -> ((Number) stage.get("sequence")).intValue() == 2)
            .findFirst().orElse(null);

        Assert.assertNotNull(matchedStage);
        stageIdToUpdate = ((Number) matchedStage.get("id")).intValue();
        Response update = StageUpdateTATAPI.updateTAT(stageIdToUpdate, orderId, 2, 4);
        System.out.println("TAT Update Status Code: " + update.getStatusCode());
        Assert.assertEquals(update.getStatusCode(), 200);
    }
}
