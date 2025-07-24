package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import base.BasePage;

public class MyTaskFlowAssigneePage extends BasePage {

	WebDriver driver;

	public MyTaskFlowAssigneePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//tbody/tr/td//button[normalize-space(text())='On Time']")
	private WebElement onTimeButton;

	@FindBy(xpath = "//ul[@role='menu']//li[@role='menuitem']//span[contains(text(),'Completed')]")
	private WebElement completeButton;

	@FindBy(xpath = "//span[@class='font-semibold'][normalize-space()='Completed']")
	private WebElement completed;

	@FindBy(xpath = "//button[.//span[contains(text(), 'My Tasks')]]")
	private WebElement myTaskButton;

	@FindBy(xpath = "//tbody/tr/td//button[normalize-space(text())='Completed']")
	private WebElement completeText;

	public void completeOrderTillStage7AndLogout() throws InterruptedException {
		for (int currentStage = 2; currentStage <= 7; currentStage++) {
			String currentStageText = "Stage " + currentStage;
			String currentStageXpath = "//tr[1]/td[2]/div[contains(normalize-space(.), '" + currentStageText + "')]";
			List<WebElement> currentStageElements = driver.findElements(By.xpath(currentStageXpath));

			if (!currentStageElements.isEmpty()) {
				System.out.println("Currently at: " + currentStageText);

				// Perform action
				onTimeButton.click();
				Thread.sleep(2000);
				completeButton.click();
				Thread.sleep(3000);

				// For stages 2 to 6, verify transition to next stage
				if (currentStage < 7) {
					String nextStageText = "Stage " + (currentStage + 1);
					String nextStageXpath = "//tr[1]/td[2]/div[contains(normalize-space(.), '" + nextStageText + "')]";
					WebElement nextStageElement = driver.findElement(By.xpath(nextStageXpath));
					String actualNextStage = nextStageElement.getText().trim();

					Assert.assertEquals(actualNextStage, nextStageText,
							"Stage did not update correctly from " + currentStageText);
				} else {
					System.out.println("Final Stage 7 completed.");

					// Step 1: Click on My Tasks
					myTaskButton.click();
					Thread.sleep(2000);

					// Step 2: Click on Completed tab
					completed.click();
					Thread.sleep(2000);

					// Step 3: Verify Stage 7 is completed
					String stage7CompletedXpath = "//tr[1]/td[2]/div[contains(normalize-space(.), 'Stage 7')]";
					List<WebElement> stage7CompletedElements = driver.findElements(By.xpath(stage7CompletedXpath));

					if (stage7CompletedElements.isEmpty()) {
						Assert.fail("Stage 7 not found on the Completed page.");
					} else {
						String completedText = completeText.getText().trim();
						Assert.assertEquals(completedText, "Completed", "Stage 7 is not marked as Completed");
					}

					// Step 4: Logout
					LoginPage login = new LoginPage(driver);
					login.signOutAsignee();
					System.out.println("Logout after verifying Stage 7 completion.");
				}

			}
		}

	}

}
