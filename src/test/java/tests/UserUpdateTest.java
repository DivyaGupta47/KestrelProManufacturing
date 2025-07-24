package tests;

import apis.UserAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
/**
 * Test class to validate the user update functionality via API.
 * 
 * This class performs:
 * - Updating an existing user's details such as first name, last name, phone, and time zone.
 * - Verifying that the update API returns a successful response.
 * - Logging the updated response for visibility.
 */
public class UserUpdateTest extends BaseFlowData {

    @Test
    public void updateUser() {
    	String identityId = userId;
        Response response = UserAPI.updateUser(userId, assigneeFirstName, assigneeLastNameUpdated, assigneePhone, timeZone);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getBoolean("is_updated"));
        System.out.println("User updated successfully. Identity ID: " + identityId);
	    System.out.println("Updated Response for user" +response.asPrettyString());
    }
}
