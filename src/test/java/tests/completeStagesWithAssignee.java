package tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import apis.AttachmentAPI;
import apis.RemarkAPI;
import apis.StatusAPI;
import base.BaseFlowData;
import io.restassured.response.Response;
/**
 * completeStagesWithAssignee handles the final flow where the assignee completes each assigned stage.
 *
 * Test Flow:
 * - Iterates through each assigned stage
 * - Adds remarks and attachments for each stage
 * - Marks each stage as completed
 *
 * Author: QA@47Billion
 */

public class completeStagesWithAssignee extends BaseFlowData {
@Test
	public void completeStagesWithDetails() throws InterruptedException {
		int stageNumber = 2;
		for (Map<String, Object> stage : filteredStages) {
			int stageId = ((Number) stage.get("id")).intValue();

			// Add Remark
			String remark = "Test Assignee Remark Stage " + stageNumber;
			Response remarkResponse = RemarkAPI.addRemark(orderId, stageId, remark);
			Assert.assertTrue(remarkResponse.getStatusCode() == 200 || remarkResponse.getStatusCode() == 201,
					"Failed to add remark to stage " + stageNumber);
			System.out.println("Added remark from asignee to stage: " + stageNumber);

			// Add Attachment
			String attachment = "Test Assignee Attachment Stage " + stageNumber;
			Response attachmentResponse = AttachmentAPI.addAttachment(orderId, stageId, attachment);
			Assert.assertTrue(attachmentResponse.getStatusCode() == 200 || attachmentResponse.getStatusCode() == 201,
					"Failed to add attachment to stage " + stageNumber);
			System.out.println("Added attachment from asignee to stage: " + stageNumber);

			// Complete Stage
			Response completeResponse = StatusAPI.completeStage(orderId, stageId);
			Assert.assertEquals(completeResponse.getStatusCode(), 200, "Failed to complete stage " + stageNumber);
			System.out.println("Completed stage from asignee: " + stageNumber);

			stageNumber++;
			Thread.sleep(1000);
		}

		Thread.sleep(1000); // Optional wait after final completion
	}

}
