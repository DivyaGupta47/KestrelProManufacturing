package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CreateOrderAdminPage;
import pages.LoginPageUi;
import pages.ProductFlowQueuedPage;

public class CreateOrderAdminTest extends BaseTest {

	LoginPageUi loginPage;

	@Test
	public void testFillNewOrderAndCancelviaAdmin() throws InterruptedException {

		loginPage = new LoginPageUi(driver);
		loginPage.enterUsername("divyaadmin@yopmail.com");
		loginPage.enterPassword("KestrelPro@123");
		loginPage.clickSignIn();
		CreateOrderAdminPage createOrderAdmin = new CreateOrderAdminPage(driver);

		int beforeCount = createOrderAdmin.getTotalOrdersCount();
		System.out.println("Total Orders before: " + beforeCount);

		createOrderAdmin.clickNewOrder();
		createOrderAdmin.fillNewOrderFormAndCancel();

		int afterCount = createOrderAdmin.getTotalOrdersCount();
		System.out.println("Total Orders after: " + afterCount);

		Assert.assertEquals(afterCount, beforeCount + 1, "Total orders count did not increase by 1");

		ProductFlowQueuedPage productFlowQueuedPage = new ProductFlowQueuedPage(driver);
		productFlowQueuedPage.completeOrderQueuedFlow();
		
		loginPage.signOutAdmin();
	}

}
