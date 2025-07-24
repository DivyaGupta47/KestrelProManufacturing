package apis;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;
/**
 * API class for updating the status of a stage in an order workflow.
 *
 * This class provides functionality to:
 * - Mark a specific stage of an order as COMPLETED using a PUT request.
 * - Mark a specific stage of an order as REJECTED using a PUT request.
 * Endpoint used: /api/v1/stage/status
 */

public class StatusAPI {
	 public static Response completeStage(int orderId, int stageId) {
         return RestAssured.given()
             .baseUri(Config.BASE_URI)
             .header("Authorization", "Bearer " + Config.getSessionToken())
             .contentType("application/json")
             .queryParam("orderId", orderId)
             .queryParam("orderStageId", stageId)
             .body(Map.of("status", "COMPLETED", "comment", ""))
         .when()
             .put("/api/v1/stage/status");
 }
 
	 public static Response rejectStage(int orderId, int stageId) {
		 String payload = "{"
	                + "\"status\": \"REJECT\","
	                + "\"comment\": \"REJECT\""
	                + "}";
         return RestAssured.given()
             .baseUri(Config.BASE_URI)
             .header("Authorization", "Bearer " + Config.getSessionToken())
             .contentType("application/json")
             .queryParam("orderId", orderId)
             .queryParam("orderStageId", stageId)
             .body(payload)
         .when()
             .put("/api/v1/stage/status");
 }

}
