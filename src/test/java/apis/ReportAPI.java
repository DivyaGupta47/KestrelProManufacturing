package apis;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.Config;
import utils.ExcelDownloader;
/**
 * API class for exporting and downloading order completion reports.
 *
 * This class provides methods to:
 * - Download a report of orders completed on time.
 * - Download a report of orders completed with delay.
 *
 * Endpoint used: /api/v1/report/export
 *
 * Each method builds a request with specific search filters (status, startDate, endDate),
 * triggers the report download, and saves the response as a file to the specified path.
 */

public class ReportAPI {

    public static Response downloadCompleteOnTimeReport(String filePath) throws IOException {
        Response response = RestAssured.given()
            .baseUri(Config.BASE_URI)
            .basePath("/api/v1/report/export")
            .header("Authorization", "Bearer " + Config.getSessionToken())
            .contentType(ContentType.JSON)
            .queryParam("searchKeys", "status")
            .queryParam("searchKeys", "startDate")
            .queryParam("searchKeys", "endDate")
            .queryParam("searchValues", "COMPLETED ON TIME")
            .queryParam("searchValues", "2025-04-01")
            .queryParam("searchValues", "2025-06-24")
        .when()
            .get();

        // Save file if response is successful
        if (response.getStatusCode() == 200) {
            byte[] bytes = response.asByteArray();
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(bytes);
            }
           // System.out.println("CompleteOnTime Report downloaded successfully to: " + filePath);
        } else {
            System.err.println("Failed to download report. Status Code: " + response.getStatusCode());
        }

        return response;
    }
    
    public static Response downloadCompleteWithDelayReport(String filePath) throws IOException {
        Response response = RestAssured.given()
            .baseUri(Config.BASE_URI)
            .basePath("/api/v1/report/export")
            .header("Authorization", "Bearer " + Config.getSessionToken())
            .contentType(ContentType.JSON)
            .queryParam("searchKeys", "status")
            .queryParam("searchKeys", "startDate")
            .queryParam("searchKeys", "endDate")
            .queryParam("searchValues", "COMPLETED WITH DELAY")
            .queryParam("searchValues", "2025-04-01")
            .queryParam("searchValues", "2025-06-24")
        .when()
            .get();

        // Save file if response is successful
        if (response.getStatusCode() == 200) {
            byte[] bytes = response.asByteArray();
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(bytes);
            }
            //System.out.println("CompleteWithDelay Report downloaded successfully to: " + filePath);
        } else {
            System.err.println("Failed to download report. Status Code: " + response.getStatusCode());
        }

        return response;
    }
    
    /**
     * Downloads the report for a specific customer and status within the given date range.
     *
     * @param filePath      Location to save the downloaded Excel file.
     * @param customerName  Name of the customer (case-sensitive).
     * @param status        Order status filter ("COMPLETED ON TIME", "COMPLETED WITH DELAY", etc.).
     * @param startDate     Start date in YYYY-MM-DD format.
     * @param endDate       End date in YYYY-MM-DD format.
     * @return              Response object from the API call.
     * @throws IOException  If there is an error saving the file.
     */
    public static File downloadReport(String customerName, String status, String startDate, String endDate) {
        try {
            String encodedCustomer = customerName.replace(" ", "%20");
            String encodedStatus = status.replace(" ", "%20");

            String apiUrl = "https://freetrial-mf.kestrelpro.ai/api/v1/report/export"
                    + "?&searchKeys=customerName"
                    + "&searchKeys=status"
                    + "&searchKeys=startDate"
                    + "&searchKeys=endDate"
                    + "&searchValues=" + encodedCustomer
                    + "&searchValues=" + encodedStatus
                    + "&searchValues=" + startDate
                    + "&searchValues=" + endDate;

            return ExcelDownloader.downloadExcelFromAPI(apiUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download report for customer: " + customerName, e);
        }
    }
}

