package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class WorkflowOpenStagesPage {
	
	 WebDriver driver;

	    public WorkflowOpenStagesPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);  // Initialize Page Factory
//workflow@yopmail.com
	        //workflowasignee@yopmail.com
	    }
	    
	    @FindBy(xpath = "//a[@title='Workflows']//span[text()='Workflows']")
		WebElement workflowsLink;
		
		@FindBy(xpath = "//button[normalize-space()='New Workflow']")
		WebElement newWorkflowBtn;
		
		@FindBy(xpath = "//button[normalize-space()='Add Stage']")
		WebElement addStageBtn;
		
		@FindBy(xpath = "//input[@placeholder='Enter Stage Name' and @name='name']")
		WebElement enterStageName;
		
		@FindBy(xpath = "//input[@placeholder='Enter Details' and @name='description']")
		WebElement enterDetails;
		
		@FindBy(xpath = "//span[normalize-space()='Open (Editable)']/ancestor::label")
		WebElement openRadioBtn;		
		
		@FindBy(xpath = "//span[text()='Open (Editable)']/ancestor::div[contains(@class,'flex justify-between')]//button[@data-slot='trigger']")
		private WebElement dropdownforOpenDays;		
		
		@FindBy(xpath = "//span[normalize-space()='Time']/preceding::button[@data-slot='trigger'][1]")
		private WebElement dropdownforOpenTime;
		
		@FindBy(xpath = "//ul[@role='menu']//li[@role='menuitemradio']//span[text()='2']")
		private WebElement option2;		
		
		@FindBy(xpath = "//div[@data-slot='trigger' and .//span[normalize-space()='Assign User *']]")
		private WebElement assignee;
		
		@FindBy(xpath = "//input[@aria-label='Search...']")
		private WebElement searchInput;		
		
		@FindBy(xpath = "//label[.//span[contains(text(),'Rejection Settings')]]")
		private WebElement rejectionSettionBtn;
		
		
		@FindBy(xpath = "//button[.//span[text()='Select stage']]")
		private WebElement selectStageBtn;
		
		
		@FindBy(xpath = "//ul[@aria-label='Select rejection stage']//li//p[text()='Stage 2']")
		private WebElement stage2Btn;
		
		@FindBy(xpath = "//button[@type='submit' and normalize-space()='Save']")
		WebElement saveBtn;
		
		@FindBy(xpath = "//input[@placeholder='Workflow name']")
		WebElement workflowNameInput;
		
		@FindBy(xpath = "//button[text()='Save Workflow']")
		WebElement workflowSavedBtn;
				
		@FindBy(xpath = "//input[@aria-label='Search']")
		private WebElement searchInputWorkflow;
		
		//String workflowName = "Workflow Open Test 2";

		public void WorkflowsOpen(String workflowName) throws InterruptedException {
		    Thread.sleep(1000);
		    workflowsLink.click();
		    Thread.sleep(3000);
		    newWorkflowBtn.click();
		    addStageBtn.click();
			enterStageName.sendKeys("Stage 2");
			enterDetails.sendKeys("Added Stage 2");
			openRadioBtn.click();
			Thread.sleep(2000);
			dropdownforOpenDays.click();
			Thread.sleep(2000);
			option2.click();
			Thread.sleep(2000);
			dropdownforOpenTime.click();
			Thread.sleep(2000);
			option2.click();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebDriverWait waitLong = new WebDriverWait(driver, Duration.ofSeconds(10)); // Reduced to 15 seconds
			Actions actions = new Actions(driver);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			assignCustomerToUser("workflowasignee@yopmail.com", wait, waitLong, actions, js);
			Thread.sleep(2000);
			saveBtn.click();
			Thread.sleep(2000); 
		   
		    for (int i = 3; i <= 5; i++) {
		        addStageBtn.click();

		        // Clear and enter new stage name and details dynamically
		        enterStageName.clear();
		        enterStageName.sendKeys("Stage " + i);
		        
		        enterDetails.clear();
		        enterDetails.sendKeys("Added Stage " + i);

		        openRadioBtn.click();
		        Thread.sleep(2000);

		        dropdownforOpenDays.click();
		        Thread.sleep(2000);
		        option2.click();
		        Thread.sleep(2000);

		        dropdownforOpenTime.click();
		        Thread.sleep(2000);
		        option2.click();


		        assignCustomerToUser("workflowasignee@yopmail.com", wait, waitLong, actions, js);
		        Thread.sleep(2000);
		        rejectionSettionBtn.click();
		        Thread.sleep(1000);
		        selectStageBtn.click();
		        Thread.sleep(1000);
		        stage2Btn.click();
		        Thread.sleep(1500);
		        saveBtn.click();
		        Thread.sleep(3000);  // wait a bit longer to save before next iteration
		        
		    }
		    WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));

		 // Wait until the workflow name input is clickable
		 wait1.until(ExpectedConditions.elementToBeClickable(workflowNameInput));
		 workflowNameInput.clear();
		 workflowNameInput.sendKeys(workflowName);

		 // Wait until the save button is clickable
		 wait1.until(ExpectedConditions.elementToBeClickable(workflowSavedBtn));
		 workflowSavedBtn.click();
		    
		    //workflowNameInput.sendKeys(workflowName);
		   // workflowSavedBtn.click();
		    Thread.sleep(1000);
		}

		
		public void assignCustomerToUser(String email, WebDriverWait wait, WebDriverWait waitLong, Actions actions,
				JavascriptExecutor js) {
			try {
				// Step 1: Click the 'No Assignee' or 'Assign' button
				WebElement button = wait.until(ExpectedConditions.elementToBeClickable(assignee));
				actions.moveToElement(button).pause(Duration.ofMillis(200)).click().perform();

				// Step 3: Enter the email in search input
				WebElement inputField = waitLong.until(ExpectedConditions.elementToBeClickable(searchInput));
				inputField.click();
				inputField.clear();
				inputField.sendKeys(email);

				// Step 4: Locate the email in the list and click it
				By userEmailLocator = By.xpath("//span[@class='text-sm text-gray-500' and text()='" + email
						+ "']/ancestor::div[contains(@class,'cursor-pointer')]");
				WebElement emailOption = wait.until(ExpectedConditions.elementToBeClickable(userEmailLocator));
				js.executeScript("arguments[0].click();", emailOption);

				actions.moveByOffset(10, 10).perform();


			} catch (Exception e) {
				System.out.println("Exception while assigning user: " + email);
				e.printStackTrace();
			}
		}
		
		public void verifyWorkflow(String workflowName) throws InterruptedException {
			Thread.sleep(3000);
		    // Click on workflows link
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		    wait.until(ExpectedConditions.elementToBeClickable(workflowsLink)).click();

		    // Wait for search input and enter workflow name
		    WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(searchInputWorkflow));
		    inputField.click();
		    inputField.clear();
		    inputField.sendKeys(workflowName);

		    // Wait a short time for search results to update
		    wait.until(ExpectedConditions.visibilityOfElementLocated(
		        By.xpath(String.format("//tbody/tr/td/div/h5[text()='%s']", workflowName))
		    ));

		    // Assert workflow is present
		    List<WebElement> workflowElements = driver.findElements(
		        By.xpath(String.format("//tbody/tr/td/div/h5[text()='%s']", workflowName))
		    );
		    
		    Thread.sleep(2000);
		    Assert.assertFalse(workflowElements.isEmpty(), "Workflow '" + workflowName + "' should be visible in UI after search");
		}

				
}
