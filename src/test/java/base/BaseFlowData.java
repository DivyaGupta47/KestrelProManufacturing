package base;

import java.util.List;
import java.util.Map;

import apis.DashboardAPI;
import io.restassured.response.Response;
import utils.Config;
import utils.CredentialsUtil;
import utils.LoginUtil;
/**
 * Base data class for storing and initializing shared test data used across multiple flows.
 *
 * Responsibilities:
 * - Holds global variables like session token, order ID, user info, and stage data.
 * - Automatically logs in as Admin once and sets the session token for further API calls.
 * - Loads configuration and credentials for Assignee and Admin from external sources (via CredentialsUtil).
 * - Makes an initial call to the dashboard API to verify login and system readiness.
 *
 * Commonly used in:
 * - Test flows for initializing shared state/data.
 * - Avoiding repetitive login and setup logic across test classes.
 */

public class BaseFlowData {
	public static String sessionToken;
	public static Integer orderId;
	public static Integer stageIdToUpdate;
	public static String customerName = CredentialsUtil.get("customer.name");
	public static String userId;
	public static String assigneeEmail = CredentialsUtil.get("assignee.email");
	public static String assigneePassword = CredentialsUtil.get("assignee.password");
	public static String assigneeFirstName = CredentialsUtil.get("assignee.firstName");
	public static String assigneeLastName = CredentialsUtil.get("assignee.lastName");
	public static String assigneeAssociate = CredentialsUtil.get("assignee.role");
	public static String assigneePhone = CredentialsUtil.get("assignee.phone");
	public static String assigneeLastNameUpdated = "AutomationUpdated";
	public static String timeZone = CredentialsUtil.get("timezone");
	public static List<Map<String, Object>> filteredStages;

	static {
		String adminEmail = CredentialsUtil.get("admin.email");
		String adminPassword = CredentialsUtil.get("admin.password");
		sessionToken = LoginUtil.performLogin(adminEmail, adminPassword);
		Config.setSessionToken(sessionToken);
		System.out.println("Logged in ONCE as Admin: " + adminEmail);
		
        Response countResponse = DashboardAPI.getDashboardCounts();
        System.out.println("Final Dashboard Counts: " + countResponse.asPrettyString());
	}
}
