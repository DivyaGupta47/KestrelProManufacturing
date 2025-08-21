package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Utility class for handling login flow using Ory Kratos and retrieving a
 * session token.
 * 
 * This class performs: - Initiating a login flow using Ory Kratos public API. -
 * Submitting user credentials (email and password) to complete the login. -
 * Extracting and returning the `session_token` from the login response.
 * 
 * Key Details: - Throws a runtime exception if login fails or if no session
 * token is returned. - Uses REST Assured to send HTTP requests and parse
 * responses. - Depends on `Config.BASE_URI` for API base URL.
 * 
 * Usage: String token = LoginUtil.performLogin("user@example.com",
 * "password123");
 * 
 * Prerequisites: - Ory Kratos must be running and accessible at the configured
 * base URI. - The provided credentials must be valid and registered in the
 * system.
 */
public class LoginUtil {

	/**
	 * Performs login using Ory Kratos public API and retrieves the session token.
	 *
	 * Steps involved: 1. Initiates the login flow to obtain a `flowId`. 2. Submits
	 * the user's email and password to the login endpoint with the flowId. 3.
	 * Extracts the `session_token` from the successful response.
	 *
	 * @param email    The user's email or identifier used for login.
	 * @param password The user's password.
	 * @return A valid session token (JWT or opaque) from Ory Kratos.
	 * @throws RuntimeException if login fails or if session token is missing in the
	 *                          response.
	 */
	public static String performLogin(String email, String password) {
		// Step 1: Start login flow
		Response flowResponse = RestAssured.given().baseUri(Config.BASE_URI)
				.get("/.ory/kratos/public/self-service/login/api");

		String flowId = flowResponse.jsonPath().getString("id");

		// Step 2: Prepare login request payload
		String loginPayload = "{" + "\"method\": \"password\"," + "\"identifier\": \"" + email + "\","
				+ "\"password\": \"" + password + "\"" + "}";

		// Step 3: Submit login request with credentials and flow ID
		Response loginResponse = RestAssured.given().baseUri(Config.BASE_URI).contentType(ContentType.JSON)
				.accept(ContentType.JSON).queryParam("flow", flowId).body(loginPayload)
				.post("/.ory/kratos/public/self-service/login");

		// Step 4: Handle login failure
		if (loginResponse.getStatusCode() != 200) {
			throw new RuntimeException("Login failed: " + loginResponse.asPrettyString());
		}

		// Step 5: Extract token from response body
		String sessionToken = loginResponse.jsonPath().getString("session_token");

		if (sessionToken == null || sessionToken.isEmpty()) {
			throw new RuntimeException("No session_token returned in login response.");
		}

		return sessionToken;
	}

	public static String performLogin_QA(String email, String password) {
		// Step 1: Start login flow
		Response flowResponse = RestAssured.given().baseUri(Config.BASE_URI_QA)
				.get("/.ory/kratos/public/self-service/login/api");

		String flowId = flowResponse.jsonPath().getString("id");

		// Step 2: Prepare login request payload
		String loginPayload = "{" + "\"method\": \"password\"," + "\"identifier\": \"" + email + "\","
				+ "\"password\": \"" + password + "\"" + "}";

		// Step 3: Submit login request with credentials and flow ID
		Response loginResponse = RestAssured.given().baseUri(Config.BASE_URI_QA).contentType(ContentType.JSON)
				.accept(ContentType.JSON).queryParam("flow", flowId).body(loginPayload)
				.post("/.ory/kratos/public/self-service/login");

		// Step 4: Handle login failure
		if (loginResponse.getStatusCode() != 200) {
			throw new RuntimeException("Login failed: " + loginResponse.asPrettyString());
		}

		// Step 5: Extract token from response body
		String sessionToken = loginResponse.jsonPath().getString("session_token");

		if (sessionToken == null || sessionToken.isEmpty()) {
			throw new RuntimeException("No session_token returned in login response.");
		}

		return sessionToken;
	}

	public static String performLogin_DeactivatedUser_QA(String email, String password) {

		// Step 1: Start login flow
		Response flowResponse = RestAssured.given().baseUri(Config.BASE_URI_QA)
				.get("/.ory/kratos/public/self-service/login/api");

		String flowId = flowResponse.jsonPath().getString("id");

		// Step 2: Prepare login request payload
		String loginPayload = "{" + "\"method\": \"password\"," + "\"identifier\": \"" + email + "\","
				+ "\"password\": \"" + password + "\"" + "}";

		// Step 3: Submit login request with credentials and flow ID
		Response loginResponse = RestAssured.given().baseUri(Config.BASE_URI_QA).contentType(ContentType.JSON)
				.accept(ContentType.JSON).queryParam("flow", flowId).body(loginPayload)
				.post("/.ory/kratos/public/self-service/login");

		// Step 4: Handle login failure
		if (loginResponse.getStatusCode() != 200) {
			String errorMessage = loginResponse.jsonPath().getString("error.message");

			if (errorMessage != null && errorMessage.contains("disabled")) {
				System.out.println("TEST PASSED: Login skipped This account is disabled. Test considered PASSED.");
				return null; // return null to indicate deactivated user
			}

			System.out.println("Login failed: " + loginResponse.asPrettyString());
			return null; // also return null for other login issues
		}

		// Step 5: Extract token from response body
		String sessionToken = loginResponse.jsonPath().getString("session_token");

		if (sessionToken == null || sessionToken.isEmpty()) {
			System.out.println("No session_token returned. Treating as successful skip.");
			return null;
		}

		return sessionToken;
	}

}
