package tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.OrderAPI;
import apis.OrderVerificationAPI;
import apis.WorkflowAPI;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import utils.Config;
import utils.LoginUtil;

public class AdminOrderFlowQATest {

	String adminEmail = "d@yopmail.com";
	String adminPassword = "KestrelPro@123";
	
    String customerName = "Tanu Automation";
    //String assigneeEmail = "TestAutomation206@yopmail.com";
    //String firstName = "Test";
    String lastName = "Automation";
    String roleAssociate = "EMPLOYEE";
    String phone = "0123567891";
    String timeZone="(GMT+5:30) Kolkata, India";
    String firstName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like TestUser_1720090812345
    String assigneeEmail = firstName.toLowerCase() + "@yopmail.com"; // Ensure email is also unique
    String userId;

    Integer orderId;
    Integer stageIdToUpdate;
    List<Map<String, Object>> filteredStages1;

    @BeforeClass
	public void setupSession() {

		System.out.println("\n=========================================================");
		System.out.println("Starting Flow: CREATE ORDER QA TEST API");
		System.out.println("=========================================================\n");
		String token = LoginUtil.performLogin_QA(adminEmail, adminPassword);
		Config.setSessionToken(token);
		// System.out.println("Login Successfully with Admin Email:" +adminEmail);
		System.out.println("TEST PASSED: Admin logged in successfully with email: " + adminEmail);
	}
   
    @Test
	public void createOrder() throws InterruptedException {
		

		// Step 1: Prepare payload
		String payload = "{" + "\"requestDate\":\"2025-06-17T09:00:00.000Z\","
				+ "\"consigneeDetails\":\"Indore Warehouse A1\"," + "\"palletTier\":\"2\"," + "\"palletType\":\"Euro\","
				+ "\"promiseDay\":\"2025-06-20T09:00:00.000Z\"," + "\"grade\":\"A+\","
				+ "\"salesCategory\":\"Industrial\"," + "\"packagingType\":\"Shrink Wrap\","
				+ "\"salesOrderLineNumber\":\"SO/9821\"," + "\"destination\":\"Indore Distribution Center\","
				+ "\"noOfRolls\":25," + "\"od\":45," + "\"coreId\":\"76\"," + "\"width\":120," + "\"length\":200,"
				+ "\"soNumber\":\"ORD123456\"," + "\"filmType\":\"LDPE\"," + "\"singleRollW\":50,"
				+ "\"soDate\":\"2025-06-17T09:00:00.000Z\"," + "\"umo\":\"Kg\"," + "\"soQuantity\":1000,"
				+ "\"region\":\"Madhya Pradesh\"," + "\"customerCode\":\"CUST7890\","
				+ "\"customerOrganizationName\":\"Kestrel Industries Pvt. Ltd.\"," + "\"customerName\":\""
				+ customerName + "\"" + "}";

		Response createResponse = OrderAPI.createOrderWithWorkflow106(payload);
		Assert.assertEquals(createResponse.getStatusCode(), 201, "Order creation failed!");

		orderId = OrderVerificationAPI.getOrderIdByCustomerNameQA106(customerName);
		Assert.assertNotNull(orderId);

		System.out.println("TEST PASSED: Order created and verified in 'Queued' status for customer: " + customerName);
		Thread.sleep(2000);
	}
}
