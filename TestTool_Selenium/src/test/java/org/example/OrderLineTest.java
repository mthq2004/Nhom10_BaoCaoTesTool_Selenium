package org.example;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class OrderLineTest extends BaseTest {

    // --- CÁC ID CƠ SỞ CẦN PHẢI TỒN TẠI TRONG DB TRƯỚC KHI CHẠY TEST ---
    private final String TEST_PRODUCT_ID = "2";

    /**
     * Phương thức trợ giúp để lấy ID của Order mới nhất trong hệ thống.
     * Order mới nhất được giả định là Order ở dòng đầu tiên trên trang /order.
     * @throws InterruptedException
     */
    private Integer getLatestOrderId(WebDriverWait wait) throws InterruptedException {
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/order");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));

        WebElement lastOrderIdElement = driver.findElement(By.xpath("//tbody/tr[last()]/td[1]/span")); // <-- ĐÃ SỬA
        Integer latestOrderId = Integer.parseInt(lastOrderIdElement.getText().trim());

        return latestOrderId;
    }

    @Test
    public void testAddOrderLineToLatestOrder() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));

        Integer orderId = getLatestOrderId(wait);
        System.out.println("--- BẮT ĐẦU TEST: testAddOrderLineToLatestOrder - Order ID: " + orderId + " ---"); // Console log
        String initialOrderURL = baseUrl + "/order/" + orderId;

        String expectedAmount = "15";

        driver.get(initialOrderURL + "/orderline/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("amount")));

        Select productSelect = new Select(driver.findElement(By.name("product.id")));
        productSelect.selectByValue(TEST_PRODUCT_ID);
        String expectedProductName = productSelect.getFirstSelectedOption().getText();

        driver.findElement(By.name("amount")).sendKeys(expectedAmount);
        driver.findElement(By.name("purchasePrice")).sendKeys("50000");

        driver.findElement(By.cssSelector("button.btn-save")).click();

        wait.until(ExpectedConditions.urlToBe(initialOrderURL));

        String xpath = String.format("//td[text()='%s']/following-sibling::td[1][text()='%s']",
                expectedProductName, expectedAmount);

        boolean found = driver.findElements(By.xpath(xpath)).size() > 0;
        Assert.assertTrue(found, "OrderLine mới phải xuất hiện với tên: " + expectedProductName);
    }

    @Test
    public void testEditOrderLineInLatestOrder() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));

        Integer orderId = getLatestOrderId(wait);
        System.out.println("--- BẮT ĐẦU TEST: testEditOrderLineInLatestOrder - Order ID: " + orderId + " ---");
        String initialOrderURL = baseUrl + "/order/" + orderId;

        driver.get(initialOrderURL + "/orderline/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("amount")));

        Select productSelect = new Select(driver.findElement(By.name("product.id")));
        productSelect.selectByValue(TEST_PRODUCT_ID);
        driver.findElement(By.name("amount")).sendKeys("5");
        driver.findElement(By.name("purchasePrice")).sendKeys("10000");
        driver.findElement(By.cssSelector("button.btn-save")).click();
        wait.until(ExpectedConditions.urlToBe(initialOrderURL));

        String orderLineId = driver.findElement(
                By.xpath("//div[@class='table-wrapper']//tbody/tr[last()]/td[1]")
        ).getText().trim();

        WebElement editLink = driver.findElement(By.xpath(
                String.format("//td[text()='%s']/following-sibling::td//a[contains(@class,'btn-edit')]", orderLineId)
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editLink);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("amount")));
        String editedAmount = "3";

        WebElement amountInput = driver.findElement(By.name("amount"));
        amountInput.clear();
        amountInput.sendKeys(editedAmount);

        WebElement priceInput = driver.findElement(By.name("purchasePrice"));
        priceInput.clear();
        priceInput.sendKeys("1500");

        driver.findElement(By.cssSelector("button.btn-save")).click();

        wait.until(ExpectedConditions.urlToBe(initialOrderURL));

        String editedAmountXPath = String.format(
                "//td[text()='%s']/following-sibling::td[2][text()='%s']",
                orderLineId, editedAmount
        );

        boolean editedAmountFound = driver.findElements(By.xpath(editedAmountXPath)).size() > 0;
        Assert.assertTrue(editedAmountFound, "Số lượng đã chỉnh sửa (" + editedAmount + ") phải xuất hiện.");
    }

    @Test
    public void testDeleteOrderLineInLatestOrder() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));

        Integer orderId = getLatestOrderId(wait);
        System.out.println("--- BẮT ĐẦU TEST: testDeleteOrderLineInLatestOrder - Order ID: " + orderId + " ---"); // Console log
        String initialOrderURL = baseUrl + "/order/" + orderId;

        driver.get(initialOrderURL + "/orderline/add");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("amount")));

        Select productSelect = new Select(driver.findElement(By.name("product.id")));
        productSelect.selectByValue(TEST_PRODUCT_ID);
        driver.findElement(By.name("amount")).sendKeys("1");
        driver.findElement(By.name("purchasePrice")).sendKeys("100");
        driver.findElement(By.cssSelector("button.btn-save")).click();
        wait.until(ExpectedConditions.urlToBe(initialOrderURL));

        String orderLineIdToDelete = driver.findElement(By.xpath("//div[@class='table-wrapper']//tbody/tr[last()]/td[1]")).getText().trim();

        WebElement deleteLink = driver.findElement(By.xpath(String.format("//td[text()='%s']/following-sibling::td//a[contains(@class,'btn-delete')]", orderLineIdToDelete)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteLink);

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (NoAlertPresentException ignored) {
        }

        wait.until(ExpectedConditions.urlToBe(initialOrderURL));

        String deletedOrderLineXPath = String.format("//div[@class='table-wrapper']//tbody/tr/td[text()='%s']", orderLineIdToDelete);
        boolean isDeleted = driver.findElements(By.xpath(deletedOrderLineXPath)).isEmpty();

        Assert.assertTrue(isDeleted, "OrderLine ID " + orderLineIdToDelete + " phải được xóa.");
    }
}