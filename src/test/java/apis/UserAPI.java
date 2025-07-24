package apis;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Config;
/**
 * API class for managing user creation and updates via the identity (Kratos) service.
 *
 * This class provides methods to:
 * - Create a new user with the "Associate" role.
 * - Create a new user with the "PM" role.
 * - Update user details such as name, phone number, and region.
 *
 * Endpoints used:
 * - POST /user/kratos/identity
 * - PUT  /user/kratos/identity/{identityId}
 */

public class UserAPI {

    public static Response createUserAssociate(String email, String firstName, String lastName,String role, String phone) {
        String payload = "{"
                + "\"email\": \"" + email + "\","
                + "\"first_name\": \"" + firstName + "\","
                + "\"last_name\": \"" + lastName + "\","
                + "\"role\": \"" + role + "\","
                + "\"phone\": \"" + phone + "\","
                + "\"status\": \"PENDING\","
                + "\"region\": \"(GMT+5:30) Kolkata, India\""
                + "}";

        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/user/kratos/identity");
    }
    
    public static Response createUserPM(String email, String firstName, String lastName,String role, String phone) {
        String payload = "{"
                + "\"email\": \"" + email + "\","
                + "\"first_name\": \"" + firstName + "\","
                + "\"last_name\": \"" + lastName + "\","
                + "\"role\": \"" + role + "\","
                + "\"phone\": \"" + phone + "\","
                + "\"status\": \"PENDING\","
                + "\"region\": \"(GMT+5:30) Kolkata, India\""
                + "}";

        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/user/kratos/identity");
    }
    
    public static Response updateUser(String identityId, String firstName, String lastName, String phone, String region) {
        String payload = "{"
                + "\"first_name\": \"" + firstName + "\","
                + "\"last_name\": \"" + lastName + "\","
                + "\"phone\": \"" + phone + "\","
                + "\"region\": \"" + region + "\""
                + "}";

        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .contentType(ContentType.JSON)
                .body(payload)
                .put("/user/kratos/identity/" + identityId);
    }

}
