package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class AddUserPage {
	WebDriver driver;

	public AddUserPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this); // Initialize Page Factory
	}

	@FindBy(xpath = "//a[@href='/settings']//span[text()='Settings']")
	WebElement Settings;

	@FindBy(xpath = "//button[contains(., 'Add Member')]")
	WebElement addMember;

	@FindBy(name = "firstName")
	WebElement firstName;
	@FindBy(name = "lastName")
	WebElement lastName;
	@FindBy(name = "email")
	WebElement email;
	@FindBy(name = "phone")
	WebElement phone;

	@FindBy(xpath = "//button[contains(., 'Select Role')]")
	WebElement cickselectrole;

	@FindBy(xpath = "//span[text()='Client Admin']")
	WebElement selectclientadmin;

	@FindBy(xpath = "//form//button[text()='Close']")
	WebElement clickcancelButton;

	@FindBy(xpath = "//form//button[text()='Add']")
	WebElement clickaddButton;

	@FindBy(xpath = "//div[contains(@class,'Toastify')]//div[contains(text(),'User created successfully')]")
	WebElement toastMessage;

	public void Settings() throws InterruptedException {
		Thread.sleep(5000);
		Settings.click();
	}

	public void addMember() throws InterruptedException {
		Thread.sleep(5000);
		addMember.click();
	}

	public void fillAddUser() throws InterruptedException {
		Thread.sleep(3000); // Prefer WebDriverWait in real projects
		String userName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like
																	// TestUser_1720090812345
		String assigneeEmail = userName.toLowerCase() + "@yopmail.com"; // Ensure email is also unique

		firstName.sendKeys("Test");
		lastName.sendKeys("trya");
		email.sendKeys(assigneeEmail);
		phone.sendKeys("1111122222");

		cickselectrole.click();
		selectclientadmin.click();
		clickaddButton.click();

		//System.out.println("User Added Successfully: " +assigneeEmail);
		Thread.sleep(1000);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.visibilityOf(toastMessage));

		Assert.assertTrue(toastMessage.getText().contains("User created successfully"));

	}

}