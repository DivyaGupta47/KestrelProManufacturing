package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AddUserPage;
import pages.LoginPageUi;

public class AddUserTest extends BaseTest {

    LoginPageUi loginPage;
    String userName = "newuser@yopmail.com";
    String password = "KestrelPro@123";

    public void login()    
    {
    	System.out.println("\n=========================================================");
    	System.out.println("Starting Flow: ADDING NEW USER TEST");
    	System.out.println("=========================================================\n");
    	loginPage = new LoginPageUi(driver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
    }
    
	@Test
	public void testfillAddUser() throws InterruptedException {
		login();
		Thread.sleep(1000);
		AddUserPage AddUser = new AddUserPage(driver);		
		AddUser.Settings();
		AddUser.addMember();
		AddUser.fillAddUser();

	}
}
