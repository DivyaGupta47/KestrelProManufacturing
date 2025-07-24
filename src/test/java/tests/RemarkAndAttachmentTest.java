package tests;

import apis.AttachmentAPI;
import apis.RemarkAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
/**
 * Test class to validate the addition of remarks and attachments to order stages via API.
 * 
 * This class performs:
 * - Adding remarks to each stage of a created order (starting from stage 2).
 * - Adding attachments to each stage of the same order.
 */
public class RemarkAndAttachmentTest extends BaseFlowData {

@Test
    public void addRemarks() {
        int stageNum = 2;
        for (Map<String, Object> stage : filteredStages) {
            int stageId = ((Number) stage.get("id")).intValue();
            String comment = "Test Remark from admin to Stage " + stageNum;
            Response response = RemarkAPI.addRemark(orderId, stageId, comment);
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
            System.out.println("Added remark from admin to stage: " + stageNum);
            stageNum++;
        }
    }

    @Test
    public void addAttachments() {
        int stageNum = 2;
        for (Map<String, Object> stage : filteredStages) {
            int stageId = ((Number) stage.get("id")).intValue();
            String comment = "Test Attachment from admin to Stage " + stageNum;
            Response response = AttachmentAPI.addAttachment(orderId, stageId, comment);
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
            System.out.println("Added attachment from admin to stage: " + stageNum);
            stageNum++;
        }
    }
}
