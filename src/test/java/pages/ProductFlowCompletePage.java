package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import base.BasePage;

public class ProductFlowCompletePage extends BasePage {
	WebDriver driver;

	public ProductFlowCompletePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//button[.//span[contains(text(),'Products')]]")
	private WebElement product;

	@FindBy(xpath = "//a[@href='/product/completed']//span[text()='Completed']")
	private WebElement completed;

	@FindBy(xpath = "//div[text()='Completed Orders']/parent::li/h2")
	WebElement totalOrdersCount;

	@FindBy(xpath = "//td[h5[text()='customerNameInput'] and span[text()='customerOrganizationNameInput']]")
	private WebElement recentAddedCustomer;

	@FindBy(xpath = "//button[normalize-space()='Details']")
	private WebElement detailsButton;

	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 2']]//button[normalize-space()='Completed']")
	private WebElement completedButtonStage2;

	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 3']]//button[normalize-space()='Completed']")
	private WebElement completedButtonStage3;

	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 4']]//button[normalize-space()='Completed']")
	private WebElement completedButtonStage4;

	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 5']]//button[normalize-space()='Completed']")
	private WebElement completedButtonStage5;

	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 6']]//button[normalize-space()='Completed']")
	private WebElement completedButtonStage6;

	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 7']]//button[normalize-space()='Completed']")
	private WebElement completedButtonStage7;

	public void completeOrderCompleteFlow() throws InterruptedException {
		int completeCount = getCompleteOrdersCount();
		System.out.println(completeCount);
		Thread.sleep(5000);
		clickElement(product);
		Thread.sleep(3000);
		clickElement(product);
		Thread.sleep(3000);
		clickElement(completed);
		Thread.sleep(1000);
		driver.navigate().refresh();

		Thread.sleep(3000);
		recentAddedCustomer.getText();
		Thread.sleep(5000);

		WebElement completedBreadcrumb = driver
				.findElement(By.xpath("//nav[@aria-label='breadcrumb']//li[.//span[text()='Completed']]"));
		String completedText = completedBreadcrumb.getText(); // e.g., "Completed (9)"
		String numberOnly = completedText.replaceAll("\\D+", ""); // Extracts only digits

		int breadcrumbCount = Integer.parseInt(numberOnly);
		System.out.println("Completed Count from breadcrumb: " + breadcrumbCount);

		// Assertion or comparison
		if (completeCount == breadcrumbCount) {
			System.out.println("Order counts match.");
		} else {
			System.out.println("Mismatch: UI shows " + completeCount + ", breadcrumb shows " + breadcrumbCount);
		}

	}

	public int getCompleteOrdersCount() throws InterruptedException {
		Thread.sleep(15000);
		WebElement totalOrdersElement = totalOrdersCount;
		return Integer.parseInt(totalOrdersElement.getText());
	}

	public boolean searchOrderinCompleted(String customerName) throws InterruptedException {
		Thread.sleep(500);
		clickElement(product); // Click product section
		Thread.sleep(500);
		clickElement(completed); // Click queued orders
		Thread.sleep(9000);

		// Dynamic XPath using String.format
		String xpath = String.format("//td[h5[text()='%s'] and span[text()='Kestrel Industries Pvt. Ltd.']]",
				customerName);

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
			WebElement customerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

			String recentCustomerName = customerElement.getText().split("\n")[0]; // Take first line only

			// System.out.println("Recent customer found: " + recentCustomerName);

			if (recentCustomerName.equals(customerName)) {
				System.out.println("TEST PASSED: Recent customer matches in Completed list_UI: " + customerName);
				return true;
			} else {
				System.out.println(
						"TEST FAILED: Expected customer: " + customerName + " but found: " + recentCustomerName);
				return false;
			}
		} catch (Exception e) {
			System.out.println("TEST FAILED: Customer not found in UI: " + customerName);
			return false;
		}
	}

	public void statusIsCompleted(String customerName) throws InterruptedException {
		Thread.sleep(500);
		clickElement(product); // Click product section
		Thread.sleep(500);
		clickElement(completed); // Click queued orders
		Thread.sleep(9000);

		// Dynamic XPath using String.format
		String xpath = String.format("//td[h5[text()='%s'] and span[text()='Kestrel Industries Pvt. Ltd.']]",
				customerName);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
		WebElement customerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		customerElement.click();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(detailsButton)).click();
		System.out.println("Details button clicked");
		Thread.sleep(1000);

		String completed2 = completedButtonStage2.getText();
		Assert.assertTrue(completed2.equalsIgnoreCase("Completed"),
				"Expected text 'Completed' but found: " + completed2);
		
		String completed3 = completedButtonStage3.getText();
		Assert.assertTrue(completed3.equalsIgnoreCase("Completed"),
				"Expected text 'Completed' but found: " + completed3);
		
		String completed4 = completedButtonStage4.getText();
		Assert.assertTrue(completed4.equalsIgnoreCase("Completed"),
				"Expected text 'Completed' but found: " + completed4);
		
		String completed5 = completedButtonStage5.getText();
		Assert.assertTrue(completed5.equalsIgnoreCase("Completed"),
				"Expected text 'Completed' but found: " + completed5);
		
		String completed6 = completedButtonStage6.getText();
		Assert.assertTrue(completed6.equalsIgnoreCase("Completed"),
				"Expected text 'Completed' but found: " + completed6);
		
		String completed7 = completedButtonStage7.getText();
		Assert.assertTrue(completed7.equalsIgnoreCase("Completed"),
				"Expected text 'Completed' but found: " + completed7);
	}
}
