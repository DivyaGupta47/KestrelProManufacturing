package pages;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReportValidationPage {
	 WebDriver driver;

	    public ReportValidationPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);  // Initialize Page Factory
	    }
    
	    @FindBy(xpath = "//span[normalize-space()='Reports']/parent::div")
	    public WebElement reportsMenu;	    
	    
	    @FindBy(xpath = "//input[@aria-label='Customer Name']")
	    public WebElement customerNameFilter;
	     
	    @FindBy(xpath = "//ul[@role='listbox']")
	    public WebElement customerNameList;
	    
	    ////div[@role='option' and normalize-space(text())='" + customerName + "']
	    @FindBy(xpath = "//li[@role='option']//span[text()='Vasu Automation']")
	    public WebElement customerNameOption;
	    
	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[1]")
	    public WebElement customerName;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[2]")
	    public WebElement stagesHrs;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[3]/span")
	    public WebElement stage1;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[4]/span")
	    public WebElement stage2;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[5]/span")
	    public WebElement stage3;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[6]/span")
	    public WebElement stage4;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[7]/span")
	    public WebElement stage5;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[8]/span")
	    public WebElement stage6;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[9]/span")
	    public WebElement stage7;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[10]")
	    public WebElement tatHrs;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[11]")
	    public WebElement allocateHrs;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[12]/span")
	    public WebElement diffHrs;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[13]")
	    public WebElement customerCode;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[14]")
	    public WebElement soQuantity;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[15]")
	    public WebElement filmType;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[16]")
	    public WebElement soNumber;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[17]")
	    public WebElement widthMm;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[18]")
	    public WebElement region;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[19]")
	    public WebElement uom;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[20]")
	    public WebElement singleRollW;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[21]")
	    public WebElement length;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[22]")
	    public WebElement coreIdMm;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[23]")
	    public WebElement oDMm;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[24]")
	    public WebElement noOfRolls;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[25]/span")
	    public WebElement destination;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[26]/span")
	    public WebElement consigneeDetails;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[27]")
	    public WebElement salesOrderLineNo;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[28]")
	    public WebElement packingType;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[29]")
	    public WebElement saleCategory;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[30]")
	    public WebElement grade;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[31]")
	    public WebElement promiseDate;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[32]")
	    public WebElement palletType;

	    @FindBy(xpath = "//tr[td[normalize-space()='Vasu Automation']]/td[33]")
	    public WebElement palletTier;
	    
	    public void ReportTableValidation() throws InterruptedException
	    {	
	    	Thread.sleep(5000);
	    	reportsMenu.click();
	    	Thread.sleep(1000);
	    	customerNameFilter.click();
	    	Thread.sleep(1000);
	    	customerNameFilter.sendKeys("Vasu Automation");
	    	Thread.sleep(1000);
	    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    	wait.until(ExpectedConditions.visibilityOf(customerNameList));
	    	wait.until(ExpectedConditions.elementToBeClickable(customerNameOption));
	    	customerNameOption.click();
	    	Thread.sleep(1000);
	    	
	    	String selectedCustomer = customerName.getText();
	    	System.out.println("customerName: " + selectedCustomer);

	    	String selectedstagesHrs = stagesHrs.getText();
	    	System.out.println("stagesHrs: " + selectedstagesHrs);
	    	
	    	String selectedstage1 = stage1.getText();
	    	System.out.println("stage1: " + selectedstage1);

	    	String selectedstage2 = stage2.getText();
	    	System.out.println("stage2: " + selectedstage2);
	    	
	    	String selectedstage3 = stage3.getText();
	    	System.out.println("stage3: " + selectedstage3);
	    	
	    	String selectedstage4 = stage4.getText();
	    	System.out.println("stage4: " + selectedstage4);
	    	
	    	String selectedstage5 = stage5.getText();
	    	System.out.println("stage5: " + selectedstage5);

	    	String selectedstage6 = stage6.getText();
	    	System.out.println("stage6: " + selectedstage6);
	    	
	    	String selectedstage7 = stage7.getText();
	    	System.out.println("stage7: " + selectedstage7);
	    	
	    	String selectedtatHrs = tatHrs.getText();
	    	System.out.println("tatHrs: " + selectedtatHrs);
	    	
	    	String selectedallocateHrs = allocateHrs.getText();
	    	System.out.println("allocateHrs: " + selectedallocateHrs);
	    	
	    	String selecteddiffHrs = diffHrs.getText();
	    	System.out.println("diffHrs: " + selecteddiffHrs);
	    	
	    	String selectedcustomerCode = customerCode.getText();
	    	System.out.println("customerCode: " + selectedcustomerCode);
	    	
	    	String selectedsoQuantity = soQuantity.getText();
	    	System.out.println("soQuantity: " + selectedsoQuantity);
	   
	    	String selectedfilmType = filmType.getText();
	    	System.out.println("filmType: " + selectedfilmType);
	    	
	    	String selectedsoNumber = soNumber.getText();
	    	System.out.println("soNumber: " + selectedsoNumber);
	    	
	    	String selectedwidthMm = widthMm.getText();
	    	System.out.println("widthMm: " + selectedwidthMm);
	    	
	    	String selectedregion = region.getText();
	    	System.out.println("region: " + selectedregion);
	    	
	    	String selecteduom = uom.getText();
	    	System.out.println("uom: " + selecteduom);
	    	
	    	String selectedsingleRollW = singleRollW.getText();
	    	System.out.println("singleRollW: " + selectedsingleRollW);
	    	
	    	String selectedlength = length.getText();
	    	System.out.println("length: " + selectedlength);
	    	
	    	String selectedcoreIdMm = coreIdMm.getText();
	    	System.out.println("coreIdMm: " + selectedcoreIdMm);
	    	
	    	String selectedoDMm = oDMm.getText();
	    	System.out.println("oDMm: " + selectedoDMm);
	    	
	    	String selectednoOfRolls = noOfRolls.getText();
	    	System.out.println("noOfRolls: " + selectednoOfRolls);
	    	
	    	String selecteddestination = destination.getText();
	    	System.out.println("destination: " + selecteddestination);
	    	
	    	String selectedconsigneeDetails = consigneeDetails.getText();
	    	System.out.println("consigneeDetails: " + selectedconsigneeDetails);
	    	
	    	String selectedsalesOrderLineNo = salesOrderLineNo.getText();
	    	System.out.println("salesOrderLineNo: " + selectedsalesOrderLineNo);
	    	
	    	String selectedpackingType = packingType.getText();
	    	System.out.println("packingType: " + selectedpackingType);
	    	
	    	String selectedsaleCategory = saleCategory.getText();
	    	System.out.println("saleCategory: " + selectedsaleCategory);
	    	
	    	String selectedgrade = grade.getText();
	    	System.out.println("grade: " + selectedgrade);
	    	
	    	String selectedpromiseDate = promiseDate.getText();
	    	System.out.println("promiseDate: " + selectedpromiseDate);
	    	
	    	String selectedpalletType = palletType.getText();
	    	System.out.println("palletType: " + selectedpalletType);
		    
	    	String selectedpalletTier = palletTier.getText();
	    	System.out.println("palletTier: " + selectedpalletTier);
	    	
	    }
	    
	    public Map<String, String> getUIReportData(String customer) throws InterruptedException {
	        Map<String, String> uiData = new LinkedHashMap<>();

	        Thread.sleep(5000);
	        reportsMenu.click();
	        Thread.sleep(1000);
	        customerNameFilter.click();
	        Thread.sleep(1000);
	        customerNameFilter.sendKeys("Vasu Automation");
	        Thread.sleep(1000);
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.visibilityOf(customerNameList));
	        wait.until(ExpectedConditions.elementToBeClickable(customerNameOption));
	        customerNameOption.click();
	        Thread.sleep(2000);

	        uiData.put("Customer Name", getElementText(customerName, "Customer Name"));
	        uiData.put("Stage Hrs", getElementText(stagesHrs, "Stage Hrs"));
	        uiData.put("Stage 1", getElementText(stage1, "Stage 1"));
	        uiData.put("Stage 2", getElementText(stage2, "Stage 2"));
	        uiData.put("Stage 3", getElementText(stage3, "Stage 3"));
	        uiData.put("Stage 4", getElementText(stage4, "Stage 4"));
	        uiData.put("Stage 5", getElementText(stage5, "Stage 5"));
	        uiData.put("Stage 6", getElementText(stage6, "Stage 6"));
	        uiData.put("Stage 7", getElementText(stage7, "Stage 7"));
	        uiData.put("TAT Duration Hours", getElementText(tatHrs, "TAT Duration Hours"));
	        uiData.put("Order Allocate Hours", getElementText(allocateHrs, "Order Allocate Hours"));
	        uiData.put("Difference Hours", getElementText(diffHrs, "Difference Hours"));
	        uiData.put("Customer Code", getElementText(customerCode, "Customer Code"));
	        uiData.put("SO Quantity", getElementText(soQuantity, "SO Quantity"));
	        uiData.put("Film Type", getElementText(filmType, "Film Type"));
	        uiData.put("SO Number", getElementText(soNumber, "SO Number"));
	        uiData.put("Width (mm)", getElementText(widthMm, "Width (mm)"));
	        uiData.put("Region", getElementText(region, "Region"));
	        uiData.put("UOM", getElementText(uom, "UOM"));
	        uiData.put("Single Roll W", getElementText(singleRollW, "Single Roll W"));
	        uiData.put("Length", getElementText(length, "Length"));
	        uiData.put("Core Id (mm)", getElementText(coreIdMm, "Core Id (mm)"));
	        uiData.put("OD (mm)", getElementText(oDMm, "OD (mm)"));
	        uiData.put("No Of Rolls", getElementText(noOfRolls, "No Of Rolls"));
	        uiData.put("Destination", getElementText(destination, "Destination"));
	        uiData.put("Consignee Details", getElementText(consigneeDetails, "Consignee Details"));
	        uiData.put("Sales Order Line Number", getElementText(salesOrderLineNo, "Sales Order Line Number"));
	        uiData.put("Packaging Type", getElementText(packingType, "Packaging Type"));
	        uiData.put("Sales Category", getElementText(saleCategory, "Sales Category"));
	        uiData.put("Grade", getElementText(grade, "Grade"));
	        uiData.put("Promise Day", getElementText(promiseDate, "Promise Day"));
	        uiData.put("Pallet Type", getElementText(palletType, "Pallet Type"));
	        uiData.put("Pallet Tier", getElementText(palletTier, "Pallet Tier"));


	    return uiData;
	}

	    public String getElementText(WebElement element, String fieldLabel) {
	        try {
	            JavascriptExecutor js = (JavascriptExecutor) driver;

	            new WebDriverWait(driver, Duration.ofSeconds(5))
	                .until(ExpectedConditions.visibilityOf(element));

	            String text = (String) js.executeScript(
	                "arguments[0].scrollIntoView(true); return arguments[0].innerText || arguments[0].textContent;", element);

	            String fieldName = text != null ? text.trim() : "";
	            System.out.println("UI [" + fieldLabel + "]: " + fieldName);
	            return fieldName;
	        } catch (Exception e) {
	            System.err.println("Failed to extract JS text for: " + fieldLabel);
	            e.printStackTrace();
	            return "";
	        }
	    }



	}