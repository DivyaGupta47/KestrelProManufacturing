package apis;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;

import java.util.List;
import java.util.Map;

public class WorkflowAPI {

    public static Integer getLatestWorkflowId() {
        String endpoint = "/org/v1/workflow/list"; // Added /org/v1

        Response response = RestAssured.given()
                .baseUri(Config.BASE_URI_QA) // example: https://qa-mf.kestrelpro.ai
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .queryParam("limit", 1)
                .queryParam("offset", 0)
                .queryParam("searchKey", "name")
                .queryParam("searchValue", "")
                .when()
                .get(endpoint);

        if (response.getStatusCode() != 200) {
           
            throw new RuntimeException("Failed to fetch workflows. Status: " + response.getStatusCode());
        }

        List<Map<String, Object>> workflows = response.jsonPath().getList("items");

        if (workflows.isEmpty()) {
            throw new RuntimeException("No workflows found!");
        }

        Integer latestWorkflowId = (Integer) workflows.get(0).get("id");
        String latestWorkflowIdName = (String) workflows.get(0).get("name");
        System.out.println("Latest Workflow ID: " + latestWorkflowId + ", Workflow Name: " + latestWorkflowIdName);


        return latestWorkflowId;
    }
}
