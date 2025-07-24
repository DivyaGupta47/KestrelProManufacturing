package utils;
/**
 * Configuration utility class for storing and accessing global constants and session-related data.
 * 
 * This class includes:
 * - Base URI for API endpoints.
 * - Static tokens for CSRF and ORY session (used for authentication headers).
 * - Thread-local session token storage for managing parallel execution safely.
 * 
 * Key Features:
 * - `BASE_URI`: The root URL used by all API endpoints.
 * - `CSRF_TOKEN` & `ORY_SESSION`: Used for building cookie headers when needed.
 * - `sessionToken` (ThreadLocal): Provides thread-safe session token storage, useful in parallel test execution.
 */
public class Config {

	// Base URL for API endpoints
	public static final String BASE_URI = "https://freetrial-mf.kestrelpro.ai";

	// Extracted CSRF token from Set-Cookie
	public static final String CSRF_TOKEN = "A0KnUgvqZS95HDUkMJNlFwx5I0fwufDs95AKuyRsHe8=";

	// Extracted session token from Set-Cookie
	public static final String ORY_SESSION = "MTc1MDMxMzYzOHxsOHlaRzFIUGxyU2Jtbl9ncnZDMEt1dTdGVENtNUMyRk51czI5UDBaNzFRbjhwUjM1dmUyR0o2UVlraTJpWm1mck81cXhJNDFCNG9pcTF5aEZhbXppc3Jub3VTMVJvd3Z2SDAwVExqQ0s5NXNxZFZyWEY5a08xZk5hOUVzNGNMRDZPZ2Exc0g1LVUyRXFDX01maWxfWm9HTGdPS2txRW1GUEhJY2k0QmVJQ2o2VHdES09od1dNUk4zLW04RzZ3dmN0UlAxSXNKdXUyVkhaVXpNUDRPMEZ2YnZQZWRTVHBac1BweXpFdXJmcm9RWTVqTDYxRjRkSjNqZ3RIa1lpN0QtRWZHQ19qQ0dlLVo1TnFTemQ0ZGV8uWfdH4atiOHUD1GFAgM2n1TJTeGKqbFQ6BIHcbM8UOA=";

	// Method to construct full cookie header for API requests
	/*
	 * public static String getCookieHeader() { return
	 * "csrf_token_9666476681fb4d35071f2904f5bd8d57022ecc87f13e6f4e113636671002348c="
	 * + CSRF_TOKEN + "; ory_kratos_session=" + ORY_SESSION; }
	 */
	/*
	 * public static String getCookieHeader() { return LoginUtil.getCookieHeader();
	 * }
	 */
	
	    //private static String sessionToken;
	    private static final ThreadLocal<String> sessionToken = new ThreadLocal<>();

	    public static void setSessionToken(String token) {
	        //sessionToken = token;
	    	sessionToken.set(token);
	    }

	    public static String getSessionToken() {
	        //return sessionToken;
	    	 return sessionToken.get();
	    }
}
