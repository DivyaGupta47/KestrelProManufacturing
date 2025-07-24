package apis;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * API class for retrieving and working with stage-related data of an order.
 *
 * This class provides methods to:
 * - Fetch the list of all stages for a given order.
 * - Retrieve the first stage ID in the sequence.
 * - Get the raw list of stage data as a map.
 *
 * Endpoint used: /api/v1/stage/order/list
 */

public class StageAPI {

    public static Response getStages(Integer orderId) {
        return RestAssured
            .given()
                .baseUri(Config.BASE_URI)
                //.header("Cookie", Config.getCookieHeader())
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .queryParam("orderId", orderId)
                .queryParam("isAssignee", false)
                .queryParam("sortKey", "sequence")
                .queryParam("sortValue", "ASC")
            .when()
                .get("/api/v1/stage/order/list");
    }

    public static Integer getFirstStageId(Integer orderId) {
        Response response = getStages(orderId);

        if (response.getStatusCode() != 200) {
            System.err.println("Failed to fetch stage list. Status: " + response.getStatusCode());
            return null;
        }

        return response.jsonPath().getInt("items[1].id"); // Use "items" instead of "data"
    }

    public static List<Map<String, Object>> getStageList(Integer orderId) {
        Response response = getStages(orderId);
        return response.jsonPath().getList("items");
    }
 
}
