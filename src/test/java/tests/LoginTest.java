package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import pages.LoginPageUi;

public class LoginTest {
    WebDriver driver;
    LoginPageUi loginPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://freetrial-mf.kestrelpro.ai/");
        loginPage = new LoginPageUi(driver);
    }

    @Test
    public void validLoginTest() {
        loginPage.enterUsername("divyaadmin@yopmail.com");
        loginPage.enterPassword("KestrelPro@123");
        loginPage.clickSignIn();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}

