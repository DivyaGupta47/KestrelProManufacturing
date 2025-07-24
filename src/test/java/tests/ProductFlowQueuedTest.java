package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CreateOrderPage;
import pages.DashboardPage;
import pages.LoginPageUi;
import pages.ProductFlowQueuedPage;

public class ProductFlowQueuedTest extends BaseTest {
	 
     @Test
     public void verifyOrderFlowAfterNewOrder() throws InterruptedException {
    	 CreateOrderPage createOrder = new CreateOrderPage(driver);

         int beforeCount = createOrder.getTotalOrdersCount();
         System.out.println("Total Orders before: " + beforeCount);
         
         createOrder.clickNewOrder();
         createOrder.fillNewOrderFormAndCancel();
         
         int afterCount = createOrder.getTotalOrdersCount();
         System.out.println("Total Orders after: " + afterCount);

         Assert.assertEquals(afterCount, beforeCount + 1, "Total orders count did not increase by 1");
         ProductFlowQueuedPage productFlowQueuedPage = new ProductFlowQueuedPage(driver);
         productFlowQueuedPage.completeOrderQueuedFlow();
         
     }
}
