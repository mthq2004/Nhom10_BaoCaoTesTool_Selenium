package org.example;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerTest extends BaseTest {

    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    public void testAddCustomerAsAdmin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/customer/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));

        String uniqueName = "SeleniumTestCustomer-" + System.currentTimeMillis();
        String currentDate = LocalDate.now().format(DATE_FORMAT);

        WebElement nameInput = driver.findElement(By.name("name"));
        nameInput.clear();
        nameInput.sendKeys(uniqueName);

        WebElement dateInput = driver.findElement(By.name("customerSince"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", dateInput, currentDate);

        System.out.println("Date set to: " + dateInput.getAttribute("value"));

        WebElement saveBtn = driver.findElement(By.cssSelector("button.btn-save, button[type='submit']"));
        saveBtn.click();

        driver.get(baseUrl + "/customer");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));

        boolean found = driver.findElements(By.linkText(uniqueName)).size() > 0;
        Assert.assertTrue(found, "Khách hàng mới phải xuất hiện trong danh sách: " + uniqueName);
    }

    @Test
    public void testEditCustomerAsAdmin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/customer/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));

        String baseName = "ToEditCustomer-" + System.currentTimeMillis();
        String dateValue = LocalDate.now().minusDays(1).format(DATE_FORMAT);

        driver.findElement(By.name("name")).sendKeys(baseName);
        WebElement dateInput = driver.findElement(By.name("customerSince"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", dateInput, dateValue);
        driver.findElement(By.cssSelector("button.btn-save, button[type='submit']")).click();

        // Tìm và sửa
        driver.get(baseUrl + "/customer");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(baseName)));

        WebElement editLink = driver.findElement(By.xpath("//a[text()='" + baseName +
                "']/ancestor::tr//a[contains(@class,'action-edit')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editLink);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));
        WebElement nameInput = driver.findElement(By.name("name"));
        String newName = baseName + "-edited";
        nameInput.clear();
        nameInput.sendKeys(newName);

        driver.findElement(By.cssSelector("button.btn-save, button[type='submit']")).click();

        driver.get(baseUrl + "/customer");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));
        Assert.assertTrue(driver.findElements(By.linkText(newName)).size() > 0,
                "Không tìm thấy khách hàng sau khi sửa: " + newName);
    }

    @Test
    public void testDeleteCustomerAsAdmin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/customer/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));

        String baseName = "ToDeleteCustomer-" + System.currentTimeMillis();
        String dateValue = LocalDate.now().format(DATE_FORMAT);

        driver.findElement(By.name("name")).sendKeys(baseName);
        WebElement dateInput = driver.findElement(By.name("customerSince"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", dateInput, dateValue);
        driver.findElement(By.cssSelector("button.btn-save, button[type='submit']")).click();

        driver.get(baseUrl + "/customer");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(baseName)));

        WebElement deleteLink = driver.findElement(By.xpath("//a[text()='" + baseName +
                "']/ancestor::tr//a[contains(@class,'action-delete')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteLink);

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (NoAlertPresentException ignored) {}

        driver.navigate().refresh();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));
        boolean stillExists = driver.findElements(By.linkText(baseName)).size() > 0;
        Assert.assertFalse(stillExists, "Khách hàng vẫn tồn tại sau khi xóa: " + baseName);
    }
}
