package apis;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Config;
/**
 * API class for handling acceptance of terms and conditions for a user.
 *
 * This class provides functionality to:
 * - Submit the acceptance of terms and conditions for a specific identity/user.
 *
 * Endpoint used: /user/kratos/terms_conditions/{identityId}
 */

public class TermConditionAPI {

    public static Response acceptTerms(String identityId) {
        String endpoint = "/user/kratos/terms_conditions/" + identityId;

        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .basePath(endpoint)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .contentType(ContentType.JSON)
                .body("{\"terms_condition\": true}")
                .put();
    }
}
