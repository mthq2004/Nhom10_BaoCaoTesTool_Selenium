package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected ChromeDriver driver;
    protected String baseUrl;
    protected String adminUser;
    protected String adminPass;
    protected String customerUser;
    protected String customerPass;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // configurable via -D system properties
        baseUrl = System.getProperty("baseUrl", "http://localhost:8085");
        adminUser = System.getProperty("admin.username", "admin");
        adminPass = System.getProperty("admin.password", "123");
        customerUser = System.getProperty("customer.username", "customer");
        customerPass = System.getProperty("customer.password", "111");
    }

    protected void loginAs(String username, String password) throws InterruptedException {
        driver.get(baseUrl + "/login");
        WebElement inputUser = driver.findElement(By.name("username"));
        inputUser.clear();
        inputUser.sendKeys(username);

        WebElement inputPass = driver.findElement(By.name("password"));
        inputPass.clear();
        inputPass.sendKeys(password);

        WebElement submit = driver.findElement(By.cssSelector("button[type='submit']"));
        submit.click();

        // small wait — tests keep this simple like the sample
        Thread.sleep(500);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
        }
    }
}
