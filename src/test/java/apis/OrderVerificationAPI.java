package apis;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.testng.Assert;
/**
 * API class for verifying order statuses and retrieving order details by customer name.
 *
 * This class provides methods to:
 * - Fetch all queued or completed orders with sorting and pagination.
 * - Retrieve a specific order ID based on the customer's name from either queued or completed lists.
 *
 * Endpoints used:
 * - /api/v1/orders/list
 */

public class OrderVerificationAPI {

	//get all queued list
	public static Response getQueuedOrders(int limit, int offset, String sortKey, String sortValue) {
		return RestAssured.given().baseUri(Config.BASE_URI)
				// .header("Cookie", Config.getCookieHeader())
				.header("Authorization", "Bearer " + Config.getSessionToken()).queryParam("status", "QUEUED")
				.queryParam("sortKey", sortKey).queryParam("sortValue", sortValue).queryParam("limit", limit)
				.queryParam("offset", offset).when().get("/api/v1/orders/list");
	}

	public static Integer getOrderIdByCustomerName(String customerName) {
		Response response = getQueuedOrders(100, 0, "id", "DESC");

		if (response.getStatusCode() != 200) {
			System.err.println("Failed to fetch orders. Status: " + response.getStatusCode());
			return null;
		}

		List<Map<String, Object>> orders = response.jsonPath().getList("items");

		for (Map<String, Object> order : orders) {
			String name = (String) order.get("customerName");
			if (name != null && name.equalsIgnoreCase(customerName)) {
				return (Integer) order.get("id"); // Found the order ID
			}
		}

		return null; // No order matched
	}

	//get all completed list
	public static Response getCompletedOrders(int limit, int offset, String sortKey, String sortValue) {
		return RestAssured.given().baseUri(Config.BASE_URI)
				// .header("Cookie", Config.getCookieHeader())
				.header("Authorization", "Bearer " + Config.getSessionToken()).queryParam("status", "COMPLETED")
				.queryParam("sortKey", sortKey).queryParam("sortValue", sortValue).queryParam("limit", limit)
				.queryParam("offset", offset).when().get("/api/v1/orders/list");
	}

	public static Integer getOrderIdByCustomerNameCompleted(String customerName) {
		Response response = getCompletedOrders(100, 0, "id", "DESC");

		if (response.getStatusCode() != 200) {
			System.err.println("Failed to fetch orders. Status: " + response.getStatusCode());
			return null;
		}

		List<Map<String, Object>> orders = response.jsonPath().getList("items");

		for (Map<String, Object> order : orders) {
			String name = (String) order.get("customerName");
			if (name != null && name.equalsIgnoreCase(customerName)) {
				return (Integer) order.get("id"); // Found the order ID
			}
		}

		return null; // No order matched
	}
	
	
	public static List<Integer> getQueuedOrdersByParentId(int parentOrderId) {
	    Response response = getQueuedOrders(100, 0, "id", "DESC");

	    if (response.getStatusCode() != 200) {
	        System.err.println("Failed to fetch queued orders. Status: " + response.getStatusCode());
	        return Collections.emptyList();
	    }

	    List<Map<String, Object>> orders = response.jsonPath().getList("items");

	    return orders.stream()
	        .filter(order -> {
	            Object parent = order.get("parentId");
	            return parent != null && ((Number) parent).intValue() == parentOrderId;
	        })
	        .map(order -> {
	            Object idObj = order.get("id");
	            return idObj != null ? ((Number) idObj).intValue() : null;
	        })
	        .filter(Objects::nonNull)
	        .collect(Collectors.toList());
	}
	
	//get all completed list
		public static Response getOnTimeOrders(int limit, int offset, String sortKey, String sortValue) {
			return RestAssured.given().baseUri(Config.BASE_URI)
					// .header("Cookie", Config.getCookieHeader())
					.header("Authorization", "Bearer " + Config.getSessionToken()).queryParam("status", "ON_TIME")
					.queryParam("sortKey", sortKey).queryParam("sortValue", sortValue).queryParam("limit", limit)
					.queryParam("offset", offset).when().get("/api/v1/orders/list");
		}

		public static Integer getOrderIdByCustomerNameOnTime(String customerName) {
			Response response = getOnTimeOrders(100, 0, "id", "DESC");

			if (response.getStatusCode() != 200) {
				System.err.println("Failed to fetch orders. Status: " + response.getStatusCode());
				return null;
			}

			List<Map<String, Object>> orders = response.jsonPath().getList("items");

			for (Map<String, Object> order : orders) {
				String name = (String) order.get("customerName");
				if (name != null && name.equalsIgnoreCase(customerName)) {
					return (Integer) order.get("id"); // Found the order ID
				}
			}

			return null; // No order matched
		}
		
		public static Response getQueuedOrdersQA(int limit, int offset, String sortKey, String sortValue) {
			return RestAssured.given().baseUri(Config.BASE_URI_QA)
					// .header("Cookie", Config.getCookieHeader())
					.header("Authorization", "Bearer " + Config.getSessionToken()).queryParam("status", "QUEUED")
					.queryParam("sortKey", sortKey).queryParam("sortValue", sortValue).queryParam("limit", limit)
					.queryParam("offset", offset).when().get("/api/v1/orders/list");
		}
		
		public static Integer getOrderIdByCustomerNameQA(String customerName) {
			Response response = getQueuedOrdersQA(100, 0, "id", "DESC");

			if (response.getStatusCode() != 200) {
				System.err.println("Failed to fetch orders. Status: " + response.getStatusCode());
				return null;
			}

			List<Map<String, Object>> orders = response.jsonPath().getList("items");

			for (Map<String, Object> order : orders) {
				String name = (String) order.get("customerName");
				if (name != null && name.equalsIgnoreCase(customerName)) {
					return (Integer) order.get("id"); // Found the order ID
				}
			}

			return null; // No order matched
		}
		
		public static Integer getOrderIdByCustomerNameCompletedQA(String customerName) {
			Response response = getCompletedOrdersQA(100, 0, "id", "DESC");

			if (response.getStatusCode() != 200) {
				System.err.println("Failed to fetch orders. Status: " + response.getStatusCode());
				return null;
			}

			List<Map<String, Object>> orders = response.jsonPath().getList("items");

			for (Map<String, Object> order : orders) {
				String name = (String) order.get("customerName");
				if (name != null && name.equalsIgnoreCase(customerName)) {
					return (Integer) order.get("id"); // Found the order ID
				}
			}

			return null; // No order matched
		}
		
		//get all completed list
		public static Response getCompletedOrdersQA(int limit, int offset, String sortKey, String sortValue) {
			return RestAssured.given().baseUri(Config.BASE_URI_QA)
					// .header("Cookie", Config.getCookieHeader())
					.header("Authorization", "Bearer " + Config.getSessionToken()).queryParam("status", "COMPLETED")
					.queryParam("sortKey", sortKey).queryParam("sortValue", sortValue).queryParam("limit", limit)
					.queryParam("offset", offset).when().get("/api/v1/orders/list");
		}
		
		public static Response getQueuedOrdersQA106(int limit, int offset, String sortKey, String sortValue) {
			return RestAssured.given().baseUri(Config.BASE_URI_QA)
					// .header("Cookie", Config.getCookieHeader())
					.header("Authorization", "Bearer " + Config.getSessionToken()).queryParam("status", "QUEUED")
					.queryParam("sortKey", sortKey).queryParam("sortValue", sortValue).queryParam("limit", limit)
					.queryParam("offset", offset).when().get("/api/v1/orders/list");
		}
		
		public static Integer getOrderIdByCustomerNameQA106(String customerName) {
			Response response = getQueuedOrdersQA106(100, 0, "id", "DESC");

			if (response.getStatusCode() != 200) {
				System.err.println("Failed to fetch orders. Status: " + response.getStatusCode());
				return null;
			}

			List<Map<String, Object>> orders = response.jsonPath().getList("items");

			for (Map<String, Object> order : orders) {
				String name = (String) order.get("customerName");
				if (name != null && name.equalsIgnoreCase(customerName)) {
					return (Integer) order.get("id"); // Found the order ID
				}
			}

			return null; // No order matched
		}

}
