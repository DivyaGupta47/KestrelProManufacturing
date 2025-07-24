package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPageUi;
import pages.MyTaskFlowAssigneePage;

public class MyTaskFlowAssigneeTest extends BaseTest {

	LoginPageUi loginPage;

	@Test
	public void assigneeFlow() throws InterruptedException {
		loginPage = new LoginPageUi(driver);
		loginPage.enterUsername("divya_a@yopmail.com");
		loginPage.enterPassword("KestrelPro@123");
		loginPage.clickSignIn();

		System.out.println("Login As Asignee");
		Thread.sleep(3000);
		MyTaskFlowAssigneePage myTaskFlowAssigneePage = new MyTaskFlowAssigneePage(driver);
		myTaskFlowAssigneePage.completeOrderTillStage7AndLogout();
	}

}

