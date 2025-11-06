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

public class ProductTest extends BaseTest {

    @Test
    public void testAddProductAsAdmin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        loginAs(adminUser, adminPass);

        // go to add product page
        driver.get(baseUrl + "/product/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));

        String uniqueName = "SeleniumTestProduct-" + System.currentTimeMillis();

        WebElement nameInput = driver.findElement(By.name("name"));
        nameInput.clear();
        nameInput.sendKeys(uniqueName);

        WebElement priceInput = driver.findElement(By.name("price"));
        priceInput.clear();
        priceInput.sendKeys("12345");

        // check inStock checkbox if present
        if (driver.findElements(By.name("inStock")).size() > 0) {
            WebElement chk = driver.findElement(By.name("inStock"));
            if (!chk.isSelected())
                chk.click();
        }

        // submit
        WebElement save = driver.findElement(By.cssSelector("button.btn-save, button[type='submit']"));
        save.click();

        // verify product shows up in product list
        driver.get(baseUrl + "/product");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));

        boolean found = driver.findElements(By.linkText(uniqueName)).size() > 0;
        Assert.assertTrue(found, "New product should appear in product list: " + uniqueName);
    }

    @Test
    public void testEditProductAsAdmin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        loginAs(adminUser, adminPass);

        // create a product to edit
        driver.get(baseUrl + "/product/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));
        String baseName = "ToEditProduct-" + System.currentTimeMillis();
        driver.findElement(By.name("name")).sendKeys(baseName);
        driver.findElement(By.name("price")).sendKeys("1000");
        driver.findElements(By.cssSelector("button.btn-save, button[type='submit']")).get(0).click();

        // go to product list and find the edit button in the same row
        driver.get(baseUrl + "/product");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(baseName)));

        WebElement editLink = driver.findElement(By.xpath("//a[text()='" + baseName
                + "']/ancestor::tr//a[contains(@class,'btn-edit') or contains(@class,'action-edit')]"));
        // click via JS in case of style
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editLink);

        // wait for form and change name
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));
        WebElement nameInput = driver.findElement(By.name("name"));
        String editedName = baseName + "-edited";
        nameInput.clear();
        nameInput.sendKeys(editedName);

        driver.findElement(By.cssSelector("button.btn-save, button[type='submit']")).click();

        // verify edited name in list
        driver.get(baseUrl + "/product");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(editedName)));
        Assert.assertTrue(driver.findElements(By.linkText(editedName)).size() > 0,
                "Edited product not found: " + editedName);
    }

    @Test
    public void testDeleteProductAsAdmin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        loginAs(adminUser, adminPass);

        // create a product to delete
        driver.get(baseUrl + "/product/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));
        String baseName = "ToDeleteProduct-" + System.currentTimeMillis();
        driver.findElement(By.name("name")).sendKeys(baseName);
        driver.findElement(By.name("price")).sendKeys("2000");
        driver.findElements(By.cssSelector("button.btn-save, button[type='submit']")).get(0).click();

        // go to product list and find delete link in same row
        driver.get(baseUrl + "/product");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(baseName)));

        WebElement deleteLink = driver.findElement(By.xpath("//a[text()='" + baseName
                + "']/ancestor::tr//a[contains(@class,'btn-delete') or contains(@class,'action-delete')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteLink);

        // handle confirm dialog if present
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException ignored) {
        }

        // verify product no longer in list
        driver.get(baseUrl + "/product");
        Thread.sleep(500); // small wait for deletion to complete on server
        Assert.assertTrue(driver.findElements(By.linkText(baseName)).isEmpty(),
                "Deleted product still present: " + baseName);
    }
}
