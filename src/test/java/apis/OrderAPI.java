package apis;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Config;
/**
 * API class for managing order-related operations.
 *
 * This class provides functionality to:
 * - Create a new order by sending the required payload.
 *
 * Endpoint used: /api/v1/orders
 * 
 * A fixed workflow ID (5) is used during order creation.
 */
public class OrderAPI {

	/*
	 * public static Response createOrder(String payload) { return
	 * RestAssured.given() .baseUri(Config.BASE_URI) .basePath("/api/v1/orders")
	 * .queryParam("workflowId", "5") .contentType(ContentType.JSON)
	 * .header("Cookie", Config.getCookieHeader()) .header("X-CSRF-Token",
	 * Config.CSRF_TOKEN) .body(payload) .post(); }
	 */
    
    public static Response createOrder(String payload) {
        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .basePath("/api/v1/orders")
                .queryParam("workflowId", "5")
                .contentType(ContentType.JSON)
                //.header("Cookie", Config.getCookieHeader())
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .body(payload)
                .post();
    }
}
