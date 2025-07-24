package apis;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;

import java.util.Map;
/**
 * API class for managing remarks with specific order stages.
 *
 * This class provides functionality to:
 * - Add a textual remark to a specific stage of an order.
 *
 * Endpoint used: /api/v1/remark
 */

public class RemarkAPI {

    public static Response addRemark(int orderId, int stageId, String comment) {
        return RestAssured
            .given()
                .baseUri(Config.BASE_URI)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .queryParam("orderId", orderId)
                .queryParam("orderStageId", stageId)
                .contentType("application/json")
                .body(Map.of("comment", comment))
            .when()
                .post("/api/v1/remark");
    }
    
    
}