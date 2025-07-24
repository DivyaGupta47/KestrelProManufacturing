package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Utility class for handling login flow using Ory Kratos and retrieving a session token.
 * 
 * This class performs:
 * - Initiating a login flow using Ory Kratos public API.
 * - Submitting user credentials (email and password) to complete the login.
 * - Extracting and returning the `session_token` from the login response.
 * 
 * Key Details:
 * - Throws a runtime exception if login fails or if no session token is returned.
 * - Uses REST Assured to send HTTP requests and parse responses.
 * - Depends on `Config.BASE_URI` for API base URL.
 * 
 * Usage:
 *   String token = LoginUtil.performLogin("user@example.com", "password123");
 * 
 * Prerequisites:
 * - Ory Kratos must be running and accessible at the configured base URI.
 * - The provided credentials must be valid and registered in the system.
 */
public class LoginUtil {


    public static String performLogin(String email, String password) {
        // Step 1: Start login flow
        Response flowResponse = RestAssured
                .given()
                .baseUri(Config.BASE_URI)
                .get("/.ory/kratos/public/self-service/login/api");

        String flowId = flowResponse.jsonPath().getString("id");

        // Step 2: Login
        String loginPayload = "{"
                + "\"method\": \"password\","
                + "\"identifier\": \"" + email + "\","
                + "\"password\": \"" + password + "\""
                + "}";

        Response loginResponse = RestAssured
                .given()
                .baseUri(Config.BASE_URI)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("flow", flowId)
                .body(loginPayload)
                .post("/.ory/kratos/public/self-service/login");

        if (loginResponse.getStatusCode() != 200) {
            throw new RuntimeException("Login failed: " + loginResponse.asPrettyString());
        }

        //  Extract token from response body
        String sessionToken = loginResponse.jsonPath().getString("session_token");

        if (sessionToken == null || sessionToken.isEmpty()) {
            throw new RuntimeException("No session_token returned in login response.");
        }

        return sessionToken;
    }
}
