package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
/**
 * Page Object Model (POM) class for the Login Page of the application.
 * 
 * This class provides:
 * - Web element definitions for login form fields (username, password) and buttons.
 * - Reusable actions for logging in, signing out, and interacting with the login page.
 */

public class LoginPageUi {
    WebDriver driver;

    public LoginPageUi(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Locators for username, password and sign in button
    @FindBy(name = "identifier")
    WebElement usernameField;

    @FindBy(name = "password")
    WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' and text()='Sign in with password']")
    WebElement signInButton;
    
    @FindBy(xpath = "//div[text()='DA']")
    WebElement profileButtonAdmin;
    
    @FindBy(xpath = "//div[text()='DG']")
    WebElement profileButtonAsignee;
    
    @FindBy(xpath = "//li[@role='menuitem' and contains(., 'Log Out')]")
    WebElement signOutButton;

    // Actions
    public void enterUsername(String username) {
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void clickSignIn() {
        signInButton.click();
    }
    
    public void signIn(String username, String password) throws InterruptedException {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        Thread.sleep(500);
        signInButton.click();
    }   
    
    public void signOutAdmin() {
    	profileButtonAdmin.click();
    	signOutButton.click();
    }
    
}