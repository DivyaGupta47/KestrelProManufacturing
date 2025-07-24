package apis;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import utils.Config;
/**
 * API class to retrieve dashboard-related data for the logged-in user.
 *
 * This class provides a method to:
 * - Fetch overall counts (e.g., total orders, completed, in-progress) displayed on the dashboard.
 *
 * Endpoint used: /api/v1/dashboard/counts
 */
public class DashboardAPI {

    public static Response getDashboardCounts() {
        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .basePath("/api/v1/dashboard/counts")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .get();
    }
}
