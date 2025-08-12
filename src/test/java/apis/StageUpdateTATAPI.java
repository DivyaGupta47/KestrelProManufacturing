package apis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;
/**
 * API class for updating (TAT) of specific stages within an order.
 *
 * This class provides a method to:
 * - Update the sequence and duration (TAT) of a given stage using PATCH request.
 *
 * Endpoint used: /api/v1/stage/order
 */

public class StageUpdateTATAPI {

    public static Response updateTAT(Integer stageId, Integer orderId, Integer sequence, Integer duration) {
        // JSON body must include both sequence and duration
        String payload = String.format("{\"sequence\": %d, \"duration\": %d}", sequence, duration);

        // Only id and orderId should be query params
		/*
		 * System.out.println("PATCH URL: " + Config.BASE_URI +
		 * "/api/v1/stage/order?id=" + stageId + "&orderId=" + orderId);
		 * System.out.println("Request Payload: " + payload);
		 */

        return RestAssured
            .given()
                .baseUri(Config.BASE_URI)
                .header("Content-Type", "application/json")
                //.header("Cookie", Config.getCookieHeader())
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .body(payload)
                .queryParam("id", stageId)
                .queryParam("orderId", orderId)
            .when()
                .patch("/api/v1/stage/order");
    }
    
    public static Response updateTATQA(Integer stageId, Integer orderId, Integer sequence, Integer duration) {
        // JSON body must include both sequence and duration
        String payload = String.format("{\"sequence\": %d, \"duration\": %d}", sequence, duration);

        // Only id and orderId should be query params
		/*
		 * System.out.println("PATCH URL: " + Config.BASE_URI +
		 * "/api/v1/stage/order?id=" + stageId + "&orderId=" + orderId);
		 * System.out.println("Request Payload: " + payload);
		 */

        return RestAssured
            .given()
                .baseUri(Config.BASE_URI_QA)
                .header("Content-Type", "application/json")
                //.header("Cookie", Config.getCookieHeader())
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .body(payload)
                .queryParam("id", stageId)
                .queryParam("orderId", orderId)
            .when()
                .patch("/api/v1/stage/order");
    }
}
