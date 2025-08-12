package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import base.BasePage;

public class ProductFlowQueuedPage extends BasePage {

	WebDriver driver;

	public ProductFlowQueuedPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// @FindBy(xpath = "//span[normalize-space(text())='Products (0)']")
	@FindBy(xpath = "//button[.//span[contains(text(),'Products')]]")
	private WebElement product;

	@FindBy(xpath = "//a[@href='/product/queued']//span[text()='Queued']")
	private WebElement queued;

	@FindBy(xpath = "//td[h5[text()='customerNameInput'] and span[text()='customerOrganizationNameInput']]")
	private WebElement recentAddedCustomer;

	@FindBy(xpath = "//button[text()='Ok']")
	private WebElement splitPopupOk;

	@FindBy(xpath = ".//button[contains(@id, 'react-aria') and @data-slot='trigger']")
	private WebElement dropdown;

	@FindBy(xpath = "//ul[@role='menu']//li[@role='menuitemradio']//span[text()='2']")
	private WebElement option2;

	@FindBy(xpath = "//div[@role='alert' and contains(., 'Your workflow days have been updated')]")
	private WebElement toastMessage;

	@FindBy(xpath = "//button[normalize-space()='Details']")
	private WebElement detailsButton;

	@FindBy(css = "td > div.flex.justify-center > svg[width='22'][height='22']")
	private WebElement remarksIcon;

	@FindBy(xpath = "//input[@placeholder='Add a remark...']")
	private WebElement remarkInput;

	@FindBy(xpath = "//button[text()='Send']")
	private WebElement sendBtn;

	@FindBy(css = "tr.row-on-time td:nth-child(7) button")
	private WebElement attachIcon;

	@FindBy(xpath = "//textarea[@aria-label='Paste here']")
	private WebElement attachInput;

	@FindBy(xpath = "//button[text()='Save']")
	private WebElement saveButton;

	@FindBy(xpath = "(//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'justify-between') and span[normalize-space()='No Assignee']]/button[contains(@class, 'assign-btn')])[1]")
	private WebElement assignButton;

	@FindBy(xpath = "(//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'justify-between') and span[normalize-space()='No Assignee']]/button[contains(@class, 'assign-btn')])[1]")
	private WebElement assignButton1;
	@FindBy(xpath = "//input[@aria-label='Search']")
	private WebElement searchInput;

	@FindBy(css = "div.flex.cursor-pointer.items-center.justify-between.p-2")
	private WebElement assignCustomer;

	@FindBy(xpath = "//div[contains(@class,'box-border') and contains(., 'test associate')]")
	private WebElement assignPopup;

	@FindBy(css = "header .items-center > button")
	private WebElement backButton;

	@FindBy(xpath = "//a[@title='Dashboard']//span[text()='Dashboard']")
	WebElement dashboardLink;
	

	@FindBy(xpath = "//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'justify-between') and span[normalize-space()='No Assignee']]//button[contains(@class, 'assign-btn')]")
	private List<WebElement> assignButtons;

	@FindBy(xpath = "//button[normalize-space()='Activity']")
	private WebElement activityButton;

	public void completeOrderQueuedFlow() throws InterruptedException {
		Thread.sleep(500);
		clickElement(product);
		Thread.sleep(500);
		clickElement(queued);
		Thread.sleep(2000);
		clickElement(recentAddedCustomer);
		Thread.sleep(2000);
		clickElement(splitPopupOk);
		Thread.sleep(2000);
		clickElement(dropdown);
		Thread.sleep(2000);
		clickElement(option2);
		System.out.println("clicked");
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOf(toastMessage));
		Assert.assertTrue(toastMessage.getText().contains("Your workflow days have been updated"));
		wait.until(ExpectedConditions.elementToBeClickable(detailsButton)).click();
		System.out.println("Details button clicked");
		Thread.sleep(500);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebDriverWait waitLong = new WebDriverWait(driver, Duration.ofSeconds(10)); // Reduced to 15 seconds
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		for (int i = 0; i < 6; i++) {
			try {
				assignCustomerToUser1("divya_a@yopmail.com", wait, waitLong, actions, js);
				System.out.println("Iteration: " + i);
				// Wait a bit more to let UI settle properly
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Thread.sleep(1000);
		clickElement(remarksIcon);
		Thread.sleep(1000);
		remarkInput.clear();
		remarkInput.sendKeys("Test Remark");
		sendBtn.click();
		System.out.println("Remark sent");

		Thread.sleep(1000);
		clickElement(attachIcon);
		Thread.sleep(1000);
		attachInput.clear();
		attachInput.sendKeys("test");
		saveButton.click();
		System.out.println("Attachment save");
		Thread.sleep(2000);
		clickElement(backButton);
		Thread.sleep(2000);
		dashboardLink.click();
		Thread.sleep(3000);

	}

	public void assignCustomerToUser1(String email, WebDriverWait wait, WebDriverWait waitLong, Actions actions,
			JavascriptExecutor js) {
		try {
			// Step 1: Click the 'No Assignee' or 'Assign' button
			WebElement button = wait.until(ExpectedConditions.elementToBeClickable(assignButton));
			actions.moveToElement(button).pause(Duration.ofMillis(200)).click().perform();

			// Step 2: Wait for the modal to appear
			By modalLocator = By.xpath("//div[contains(@class,'box-border') and contains(., 'test associate')]");
			wait.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));

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

			// Step 6: Wait for toast message to disappear
			By toastLocator = By.cssSelector("div.Toastify__toast-container");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(toastLocator));

			// Step 7: Wait for the modal to disappear
			wait.until(ExpectedConditions.invisibilityOfElementLocated(modalLocator));

			System.out.println("Assigned user: " + email);

		} catch (Exception e) {
			System.out.println("Exception while assigning user: " + email);
			e.printStackTrace();
		}
	}

}
