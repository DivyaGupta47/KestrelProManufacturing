package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class CreateOrderAdminPage {
    WebDriver driver;

    public CreateOrderAdminPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);  // Initialize Page Factory
    }

    @FindBy(xpath = "//div//nav//header//button[text()='New Order']")
    WebElement newOrderButton;

    @FindBy(name = "customerName") WebElement customerNameInput;
    @FindBy(name = "customerOrganizationName") WebElement customerOrganizationNameInput;
    @FindBy(name = "customerCode") WebElement customerCodeInput;
    @FindBy(name = "region") WebElement customerRegionInput;
    @FindBy(name = "soQuantity") WebElement soQuantityInput;
    @FindBy(name = "umo") WebElement umoInput;
    @FindBy(name = "soDate") WebElement soDateInput;
    @FindBy(name = "singleRollW") WebElement singleRollWInput;
    @FindBy(name = "filmType") WebElement filmTypeInput;
    @FindBy(name = "soNumber") WebElement soNumberInput;
    @FindBy(name = "length") WebElement lengthInput;
    @FindBy(name = "width") WebElement widthInput;
    @FindBy(name = "coreId") WebElement coreIdInput;
    @FindBy(name = "od") WebElement odInput;
    @FindBy(name = "noOfRolls") WebElement noOfRollsInput;
    @FindBy(name = "workflowId") WebElement workflowDropdown;
    @FindBy(name = "destination") WebElement destinationInput;
    @FindBy(name = "salesOrderLineNumber") WebElement salesOrderLineNumberInput;
    @FindBy(name = "packagingType") WebElement packagingTypeInput;
    @FindBy(name = "salesCategory") WebElement salesCategoryInput;
    @FindBy(name = "grade") WebElement gradeInput;
    @FindBy(name = "promiseDay") WebElement promiseDayInput;
    @FindBy(name = "palletType") WebElement palletTypeInput;
    @FindBy(name = "palletTier") WebElement palletTierInput;
    @FindBy(name = "consigneeDetails") WebElement consigneeDetailsInput;
    @FindBy(name = "requestDate") WebElement requestDateInput;

    @FindBy(xpath = "//form//button[text()='Cancel']")
    WebElement cancelButton;

    @FindBy(xpath = "//form//button[text()='Create Order']")
    WebElement CreateOrderButton;

    @FindBy(xpath = "//div[@role='alert']//div[text()='Order created successfully!']")
    WebElement toastMessage;
    
    @FindBy(xpath = "//div[text()='Total Orders']/parent::li/h2")
    WebElement totalOrdersCount;
    
    @FindBy(xpath = "//a[@title='Dashboard']//span[text()='Dashboard']")
    WebElement dashboardLink;
    
    public void clickNewOrder() throws InterruptedException {
    	Thread.sleep(3000);
        newOrderButton.click();
    }

    public void fillNewOrderFormAndCancel() throws InterruptedException {
        Thread.sleep(3000); // Prefer WebDriverWait in real projects

        customerNameInput.sendKeys("customerNameInput");
        customerOrganizationNameInput.sendKeys("customerOrganizationNameInput");
        customerCodeInput.sendKeys("customerCodeInput");
        customerRegionInput.sendKeys("customerRegionInput");
        soQuantityInput.sendKeys("2");
        umoInput.sendKeys("umoInput");
        soDateInput.sendKeys("11/11/1992");
        singleRollWInput.sendKeys("1");
        filmTypeInput.sendKeys("filmTypeInput");
        soNumberInput.sendKeys("1");
        lengthInput.sendKeys("1");
        widthInput.sendKeys("1");
        coreIdInput.sendKeys("1/coreIdInput");
        odInput.sendKeys("1");
        noOfRollsInput.sendKeys("1");

        //new Select(workflowDropdown).selectByVisibleText("DJWF");
        new Select(workflowDropdown).selectByVisibleText("default");

        destinationInput.sendKeys("destinationInput");
        salesOrderLineNumberInput.sendKeys("1");
        packagingTypeInput.sendKeys("packagingTypeInput");
        salesCategoryInput.sendKeys("salesCategoryInput");
        gradeInput.sendKeys("gradeInput");
        promiseDayInput.sendKeys("11/11/1992");
        palletTypeInput.sendKeys("palletTypeInput");
        palletTierInput.sendKeys("palletTierInput");
        consigneeDetailsInput.sendKeys("consigneeDetailsInput");
        requestDateInput.sendKeys("11/11/1992");

        //cancelButton.click();  
		CreateOrderButton.click();
		Thread.sleep(1000);
	    System.out.println("Toast Message Displayed: " + toastMessage.getText());
	    Assert.assertEquals(toastMessage.getText(), "Order created successfully!");
	    dashboardLink.click();
    }
    
    public int getTotalOrdersCount() throws InterruptedException {
    	 Thread.sleep(5000); 
        WebElement totalOrdersElement = totalOrdersCount;
        return Integer.parseInt(totalOrdersElement.getText());
    }
}