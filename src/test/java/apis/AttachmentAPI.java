package apis;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;

/**
 * API class for handling attachment operations in an order stage.
 *
 * This class provides a method to:
 * - Add an attachment to a specific stage of an order via API.
 *
 * Endpoint used: /api/v1/orders/attachment
 */

public class AttachmentAPI {

    public static Response addAttachment(int orderId, int stageId, String attachment) {
        return RestAssured.given()
            .baseUri(Config.BASE_URI)
            .header("Authorization", "Bearer " + Config.getSessionToken())
            .queryParam("orderId", orderId)
            .queryParam("orderStageId", stageId)
            .contentType("application/json")
            .body(Map.of("attachment", attachment))
        .when()
            .post("/api/v1/orders/attachment");
    }
}
