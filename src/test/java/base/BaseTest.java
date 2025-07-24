package base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import pages.LoginPageUi;

public class BaseTest {

    protected WebDriver driver;

   /* @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://freetrial-mf.kestrelpro.ai/");
    }*/
    
    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  //  Required for GitHub Actions
        options.addArguments("--no-sandbox");  //  Prevents crashing in CI
        options.addArguments("--disable-dev-shm-usage");  //  Prevents memory issues
        options.addArguments("--disable-gpu");  //  Optional but safe
        options.addArguments("--remote-allow-origins=*");
        
       driver = new ChromeDriver(options);
        //driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://freetrial-mf.kestrelpro.ai/");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String takeScreenshot(String testName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destPath = System.getProperty("user.dir") + "/ExtentReports/screenshots/" + testName + "_" + timestamp + ".png";
        File dest = new File(destPath);

        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destPath;
    }
}