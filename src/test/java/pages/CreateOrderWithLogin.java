package pages;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class CreateOrderWithLogin {

    @Test
    public void loginAndCreateOrder() {
    	String customerName = "Parek Soni";
        // Step 1: Start login flow
        Response flowResponse = RestAssured
                .given()
                .baseUri("https://freetrial-mf.kestrelpro.ai")
                .header("Accept", "application/json")
                .get("/.ory/kratos/public/self-service/login/api");

        String flowId = flowResponse.jsonPath().getString("id");
        //System.out.println("Flow ID: " + flowId);

        // Step 2: Submit login credentials
        String email = "divyaadmin@yopmail.com";
        String password = "KestrelPro@123";

        String loginPayload = "{"
                + "\"method\": \"password\","
                + "\"identifier\": \"" + email + "\","
                + "\"password\": \"" + password + "\""
                + "}";

        Response loginResponse = RestAssured
                .given()
                .baseUri("https://freetrial-mf.kestrelpro.ai")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("flow", flowId)
                .body(loginPayload)
                .post("/.ory/kratos/public/self-service/login");

        System.out.println("Login Status Code: " + loginResponse.getStatusCode());
       // System.out.println("Login Response Body:\n" + loginResponse.getBody().asPrettyString());

        if (loginResponse.getStatusCode() != 200) {
            System.out.println("Login failed.");
            return;
        }

        // Step 3: Extract session token
        String sessionToken = loginResponse.jsonPath().getString("session_token");
        if (sessionToken == null || sessionToken.isEmpty()) {
            System.out.println("No session token returned.");
            return;
        }

        System.out.println("Login successful. Session Token: " + sessionToken);
        
        // Step 4: Create order
        String payload = "{"
                + "\"requestDate\":\"2025-06-17T09:00:00.000Z\","
                + "\"consigneeDetails\":\"Indore Warehouse A1\","
                + "\"palletTier\":\"2\","
                + "\"palletType\":\"Euro\","
                + "\"promiseDay\":\"2025-06-20T09:00:00.000Z\","
                + "\"grade\":\"A+\","
                + "\"salesCategory\":\"Industrial\","
                + "\"packagingType\":\"Shrink Wrap\","
                + "\"salesOrderLineNumber\":\"SO/9821\","
                + "\"destination\":\"Indore Distribution Center\","
                + "\"noOfRolls\":25,"
                + "\"od\":45,"
                + "\"coreId\":\"76\","
                + "\"width\":120,"
                + "\"length\":200,"
                + "\"soNumber\":\"ORD123456\","
                + "\"filmType\":\"LDPE\","
                + "\"singleRollW\":50,"
                + "\"soDate\":\"2025-06-17T09:00:00.000Z\","
                + "\"umo\":\"Kg\","
                + "\"soQuantity\":1000,"
                + "\"region\":\"Madhya Pradesh\","
                + "\"customerCode\":\"CUST7890\","
                + "\"customerOrganizationName\":\"Kestrel Industries Pvt. Ltd.\","
                + "\"customerName\":\"" + customerName + "\""
                + "}";

        Response orderResponse = RestAssured
                .given()
                .baseUri("https://freetrial-mf.kestrelpro.ai")
                .basePath("/api/v1/orders")
                .queryParam("workflowId", "5")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + sessionToken)  //  Bearer token used here
                .body(payload)
                .post();

        System.out.println("Order API Status Code: " + orderResponse.getStatusCode());
        System.out.println("Order created for Customer Name" +customerName);
       // System.out.println("Order API Response:\n" + orderResponse.getBody().asPrettyString());

        if (orderResponse.getStatusCode() == 201) {
            System.out.println("Order created successfully!");
        } else {
            System.out.println("Order creation failed.");
        }
    }
}
