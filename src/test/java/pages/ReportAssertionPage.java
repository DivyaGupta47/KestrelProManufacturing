package pages;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReportAssertionPage {
    WebDriver driver;

    public ReportAssertionPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);  // Initialize Page Factory
    }

    @FindBy(xpath = "//span[normalize-space()='Reports']/parent::div")
    public WebElement reportsMenu;

    @FindBy(xpath = "//input[@aria-label='Customer Name']")
    public WebElement customerNameFilter;

    @FindBy(xpath = "//ul[@role='listbox']")
    public WebElement customerNameList;

    // ===================== Dynamic Helper Methods =====================
    private WebElement getCell(String customerName, int colIndex) {
        String xpath = String.format("//tr[td[normalize-space()='%s']]/td[%d]", customerName, colIndex);
        return driver.findElement(By.xpath(xpath));
    }

    private WebElement getCellSpan(String customerName, int colIndex) {
        String xpath = String.format("//tr[td[normalize-space()='%s']]/td[%d]//span", customerName, colIndex);
        return driver.findElement(By.xpath(xpath));
    }

    // ===================== Main Data Fetch Method =====================
    public Map<String, String> getUIReportData(String customer) throws InterruptedException {
        Map<String, String> uiData = new LinkedHashMap<>();

        // Step 1: Open Reports menu
        Thread.sleep(5000);
        reportsMenu.click();
        Thread.sleep(1000);
        customerNameFilter.click();
        Thread.sleep(1000);
        customerNameFilter.sendKeys(customer);
        Thread.sleep(1000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(customerNameList));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[@role='option']//span[normalize-space()='" + customer + "']")
        )).click();
        Thread.sleep(2000);

        // Step 2: Extract UI Table Data Dynamically
        uiData.put("Customer Name", getElementText(getCell(customer, 1), "Customer Name"));
        uiData.put("Stage Hrs", getElementText(getCell(customer, 2), "Stage Hrs"));
        uiData.put("Stage 1", getElementText(getCellSpan(customer, 3), "Stage 1"));
        uiData.put("Stage 2", getElementText(getCellSpan(customer, 4), "Stage 2"));
        uiData.put("Stage 3", getElementText(getCellSpan(customer, 5), "Stage 3"));
        uiData.put("Stage 4", getElementText(getCellSpan(customer, 6), "Stage 4"));
        uiData.put("Stage 5", getElementText(getCellSpan(customer, 7), "Stage 5"));
        uiData.put("Stage 6", getElementText(getCellSpan(customer, 8), "Stage 6"));
        uiData.put("Stage 7", getElementText(getCellSpan(customer, 9), "Stage 7"));
        uiData.put("TAT Duration Hours", getElementText(getCell(customer, 10), "TAT Duration Hours"));
        uiData.put("Order Allocate Hours", getElementText(getCell(customer, 11), "Order Allocate Hours"));
        uiData.put("Difference Hours", getElementText(getCellSpan(customer, 12), "Difference Hours"));
        uiData.put("Customer Code", getElementText(getCell(customer, 13), "Customer Code"));
        uiData.put("SO Quantity", getElementText(getCell(customer, 14), "SO Quantity"));
        uiData.put("Film Type", getElementText(getCell(customer, 15), "Film Type"));
        uiData.put("SO Number", getElementText(getCell(customer, 16), "SO Number"));
        uiData.put("Width (mm)", getElementText(getCell(customer, 17), "Width (mm)"));
        uiData.put("Region", getElementText(getCell(customer, 18), "Region"));
        uiData.put("UOM", getElementText(getCell(customer, 19), "UOM"));
        uiData.put("Single Roll W", getElementText(getCell(customer, 20), "Single Roll W"));
        uiData.put("Length", getElementText(getCell(customer, 21), "Length"));
        uiData.put("Core Id (mm)", getElementText(getCell(customer, 22), "Core Id (mm)"));
        uiData.put("OD (mm)", getElementText(getCell(customer, 23), "OD (mm)"));
        uiData.put("No Of Rolls", getElementText(getCell(customer, 24), "No Of Rolls"));
        uiData.put("Destination", getElementText(getCellSpan(customer, 25), "Destination"));
        uiData.put("Consignee Details", getElementText(getCellSpan(customer, 26), "Consignee Details"));
        uiData.put("Sales Order Line Number", getElementText(getCell(customer, 27), "Sales Order Line Number"));
        uiData.put("Packaging Type", getElementText(getCell(customer, 28), "Packaging Type"));
        uiData.put("Sales Category", getElementText(getCell(customer, 29), "Sales Category"));
        uiData.put("Grade", getElementText(getCell(customer, 30), "Grade"));
        uiData.put("Promise Day", getElementText(getCell(customer, 31), "Promise Day"));
        uiData.put("Pallet Type", getElementText(getCell(customer, 32), "Pallet Type"));
        uiData.put("Pallet Tier", getElementText(getCell(customer, 33), "Pallet Tier"));

        return uiData;
    }

    // ===================== Utility Method =====================
    public String getElementText(WebElement element, String fieldLabel) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(element));

            String text = (String) js.executeScript(
                    "arguments[0].scrollIntoView(true); return arguments[0].innerText || arguments[0].textContent;", element);

            String fieldValue = text != null ? text.trim() : "";
            System.out.println("UI [" + fieldLabel + "]: " + fieldValue);
            return fieldValue;
        } catch (Exception e) {
            System.err.println("Failed to extract text for: " + fieldLabel);
            e.printStackTrace();
            return "";
        }
    }
}
