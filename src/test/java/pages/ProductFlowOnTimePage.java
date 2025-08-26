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
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import base.BasePage;

public class ProductFlowOnTimePage extends BasePage {

	WebDriver driver;

	public ProductFlowOnTimePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//button[.//span[contains(text(),'Products')]]")
	private WebElement product;

	@FindBy(xpath = "//a[@href='/product/on-time']//span[text()='On-Time']")
	private WebElement onTime;

	@FindBy(xpath = "//td[h5[text()='customerNameInput'] and span[text()='customerOrganizationNameInput']]")
	private WebElement recentAddedCustomer;

	@FindBy(xpath = "//button[normalize-space()='Details']")
	private WebElement detailsButton;
	
	@FindBy(xpath = "//tbody/tr/td//button[normalize-space(text())='On Time']")
	private WebElement onTimeButton;
	
	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 2']]//button[normalize-space()='On Time']")
	private WebElement onTimeButtonStage2;
	
	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 3']]//button[normalize-space()='On Time']")
	private WebElement onTimeButtonStage3;
	
	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 4']]//button[normalize-space()='On Time']")
	private WebElement onTimeButtonStage4;
	
	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 5']]//button[normalize-space()='On Time']")
	private WebElement onTimeButtonStage5;
	
	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 6']]//button[normalize-space()='On Time']")
	private WebElement onTimeButtonStage6;
	
	@FindBy(xpath = "//tr[td//div[normalize-space()='Stage 7']]//button[normalize-space()='On Time']")
	private WebElement onTimeButtonStage7;
	
	@FindBy(xpath = "//button[normalize-space()='Activity']")
	private WebElement activityButton;
	
	@FindBy(xpath = "//ul[@role='menu']//li[@role='menuitem']//span[contains(text(),'Completed')]")
	private WebElement completeButton;
	
	@FindBy(css = "header .items-center > button")
	private WebElement backButton;

	@FindBy(xpath = "//a[@title='Dashboard']//span[text()='Dashboard']")
	WebElement dashboardLink;
	
	@FindBy(css = "div.Toastify__toast--success")
	private WebElement success;
	
	public void completeOrderOnTimeFlow() throws InterruptedException {
		Thread.sleep(5000);
		clickElement(product);
		Thread.sleep(3000);
		clickElement(product);
		Thread.sleep(3000);
		clickElement(onTime);
		Thread.sleep(3000);
		clickElement(recentAddedCustomer);
		wait.until(ExpectedConditions.elementToBeClickable(detailsButton)).click();
		System.out.println("Details button clicked");

		Thread.sleep(3000);
		clickElement(onTimeButtonStage2);
		Thread.sleep(1000);
		System.out.println("Clicking completeButton now");
		clickElement(completeButton);
		System.out.println("Stage 2 completeButton button clicked");
		Thread.sleep(2000);
		clickElement(activityButton);
		Thread.sleep(2000);
		clickElement(detailsButton);
		
		Thread.sleep(3000);
		clickElement(onTimeButtonStage3);
		Thread.sleep(1000);
		System.out.println("Clicking completeButton now");
		clickElement(completeButton);
		System.out.println("Stage 3 completeButton button clicked");
		Thread.sleep(2000);
		clickElement(activityButton);
		Thread.sleep(2000);
		clickElement(detailsButton);
		
		Thread.sleep(3000);
		clickElement(onTimeButtonStage4);
		Thread.sleep(1000);
		clickElement(completeButton);
		System.out.println("Stage 4 completeButton button clicked");
		Thread.sleep(2000);
		clickElement(activityButton);
		Thread.sleep(2000);
		clickElement(detailsButton);
		
		Thread.sleep(3000);
		clickElement(onTimeButtonStage5);
		Thread.sleep(1000);
		clickElement(completeButton);
		System.out.println("Stage 5 completeButton button clicked");
		Thread.sleep(2000);
		clickElement(activityButton);
		Thread.sleep(2000);
		clickElement(detailsButton);
		
		Thread.sleep(3000);
		clickElement(onTimeButtonStage6);
		Thread.sleep(1000);
		clickElement(completeButton);
		System.out.println("Stage 6 completeButton button clicked");
		Thread.sleep(2000);
		clickElement(activityButton);
		Thread.sleep(2000);
		clickElement(detailsButton);
		
		Thread.sleep(3000);
		clickElement(onTimeButtonStage7);
		Thread.sleep(1000);
		clickElement(completeButton);
		System.out.println("Stage 7 completeButton button clicked");
		Thread.sleep(2000);
		clickElement(activityButton);
		Thread.sleep(2000);
		clickElement(detailsButton);
		
		Thread.sleep(2000);
		clickElement(backButton);
		Thread.sleep(2000);
		dashboardLink.click();
		Thread.sleep(3000);
		
	}
	
	public boolean searchOrderinOnTime(String customerName) throws InterruptedException {
	    Thread.sleep(500);
	    clickElement(product);      // Click product section
	    Thread.sleep(500);
	    clickElement(onTime);       // Click queued orders
	    Thread.sleep(9000);

	 // Dynamic XPath using String.format
	    String xpath = String.format("//td[h5[text()='%s'] and span[text()='Kestrel Industries Pvt. Ltd.']]", customerName);

	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
	        WebElement customerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

	        String recentCustomerName = customerElement.getText().split("\n")[0]; // Take first line only

	       // System.out.println("Recent customer found: " + recentCustomerName);

	        if (recentCustomerName.equals(customerName)) {
	        	System.out.println("TEST PASSED: Recent customer matches in On Time list_UI: " + customerName);
	            return true;
	        } else {
	            System.out.println("TEST FAILED: Expected customer: " + customerName + " but found: " + recentCustomerName);
	            return false;
	        }
	    } catch (Exception e) {
	        System.out.println("TEST FAILED: Customer not found in UI: " + customerName);
	        return false;
	    }
	}
	
}
