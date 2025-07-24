package apis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;
import io.restassured.http.ContentType;

/**
 * API class responsible for handling stage assignment operations within an order workflow.
 *
 * This class provides utility methods to:
 * - Assign multiple users to a stage.
 *
 * Endpoint used: /api/v1/orders/assign-unassign
 *
 * All requests are authenticated using a Bearer token.
 * Used primarily in Admin-Assignee automation flows to simulate backend stage assignments.
 */

public class AssigneeAPI {
    
    public static Response assignStageToOrder(int orderId, int stageId, List<String> userIds) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("assigneeIds", userIds);
        payload.put("unassigneeIds", new ArrayList<>()); // Always send an array

        return RestAssured
            .given()
                .baseUri(Config.BASE_URI)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .header("Content-Type", "application/json")
                .queryParam("orderId", orderId)
                .queryParam("orderStageId", stageId)
                .body(payload)
            .when()
                .put("/api/v1/orders/assign-unassign");
    }

}
