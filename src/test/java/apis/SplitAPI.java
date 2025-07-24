package apis;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Config;
/**
 * API class for handling the split operation on orders.
 *
 * This class provides a method to:
 * - Mark an order as split or not split using a boolean flag.
 *
 * Endpoint used: /api/v1/orders/split
 */

public class SplitAPI {

	//Split order-NO
    public static Response splitOrder(int orderId, boolean isSplitted) {
        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .contentType("application/json")
                .queryParam("orderId", orderId)
                .queryParam("isSplitted", isSplitted)
                .when()
                .post("/api/v1/orders/split");
    }
    
    //Split order-YES
    public static Response splitMultipleOrder(int orderId, boolean isSplitted, String payloadJson) {

    	 return RestAssured.given()
                 .baseUri(Config.BASE_URI)
                 .contentType("application/json")
                 .header("Authorization", "Bearer " + Config.getSessionToken())
                 .queryParam("orderId", orderId)
                 .queryParam("isSplitted", isSplitted)
                 .body(payloadJson)
                 .post("/api/v1/orders/split");
     }
    
    public static Response splitOrderWithPayload(Integer orderId) {
        String payload = """
            {
              "splitOrders": [
                { "quantity": 100, "part": 1 },
                { "quantity": 100, "part": 2 },
                { "quantity": 800, "part": 3 }
              ],
              "splitPart": 3
            }
            """;

        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .contentType("application/json")
                .queryParam("orderId", orderId)
                .queryParam("isSplitted", true)
                .body(payload)
                .post("/api/v1/orders/split");
    }

}
