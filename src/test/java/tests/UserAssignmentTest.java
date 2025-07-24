package tests;

import apis.AssigneeAPI;
import apis.StageAPI;
import apis.UserAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
/**
 * Test class to validate creation of a new user and assignment of stages to that user via API.
 * 
 * This class performs:
 * - Creating an associate user (assignee) using `UserAPI`.
 * - Retrieving all stages for the specified order using `StageAPI`.
 * - Filtering stages based on sequence range (stage 2 to stage 7).
 * - Assigning the selected stages to the newly created user using `AssigneeAPI`.
 */

public class UserAssignmentTest extends BaseFlowData {

    @Test
    public void createUserAndAssignStages() {
        Response response = UserAPI.createUserAssociate(assigneeEmail, assigneeFirstName, assigneeLastName, assigneeAssociate, assigneePhone);
        Assert.assertEquals(response.getStatusCode(), 200);
        userId = response.jsonPath().getString("identity_id");

        List<Map<String, Object>> allStages = StageAPI.getStageList(orderId);
        filteredStages = allStages.stream()
            .filter(stage -> {
                int seq = ((Number) stage.get("sequence")).intValue();
                return seq >= 2 && seq <= 7;
            }).toList();

        int number = 2;
        for (Map<String, Object> stage : filteredStages) {
            int stageId = ((Number) stage.get("id")).intValue();
            Response assignResp = AssigneeAPI.assignStageToOrder(orderId, stageId, List.of(userId));
            Assert.assertEquals(assignResp.getStatusCode(), 200);
            System.out.println("Assigned stage " + number + " to: " + assigneeEmail);
            number++;
        }
    }
}
