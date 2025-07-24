package tests;

import apis.ReportAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
/**
 * Test class for validating the download functionality of order completion reports via API.
 * 
 * This class performs:
 * - Downloading the "Complete On Time" report as a CSV file.
 * - Downloading the "Complete With Delay" report as a CSV file.
 * 
 * Key Points:
 * - Each report is saved to the local file system under the `test-output` directory.
 */
public class ReportDownloadTest extends BaseFlowData {

    @Test
    public void downloadCompleteOnTimeReport() throws IOException {
        Response response = ReportAPI.downloadCompleteOnTimeReport("test-output/completeOnTime-report.csv");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void downloadCompleteWithDelayReport() throws IOException {
        Response response = ReportAPI.downloadCompleteWithDelayReport("test-output/completeWithDelay-report.csv");
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
