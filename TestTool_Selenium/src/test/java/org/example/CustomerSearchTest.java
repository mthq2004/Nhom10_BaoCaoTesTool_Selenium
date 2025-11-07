package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * CustomerSearchTest
 * Kiểm thử chức năng tìm kiếm khách hàng trong trang /customer
 * Bao gồm các tình huống hợp lệ, không tồn tại, ký tự đặc biệt, và để trống.
 */
public class CustomerSearchTest extends BaseTest {

    /**
     * Kiểm tra tìm khách hàng có tồn tại (ví dụ: Karen)
     */
    @Test
    public void testSearchExistingCustomer() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/customer");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("keyword")));

        WebElement searchBox = driver.findElement(By.name("keyword"));
        searchBox.clear();
        searchBox.sendKeys("Karen");

        driver.findElement(By.cssSelector("form button[type='submit']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));

        boolean found = driver.getPageSource().contains("Karen");
        Assert.assertTrue(found, "Không tìm thấy khách hàng 'Karen' trong kết quả tìm kiếm.");
    }

    /**
     * Kiểm tra tìm khách hàng không tồn tại
     */
    @Test
    public void testSearchNonExistingCustomer() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/customer");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("keyword")));

        WebElement searchBox = driver.findElement(By.name("keyword"));
        searchBox.clear();
        searchBox.sendKeys("NguyenKhongTonTai123");
        driver.findElement(By.cssSelector("form button[type='submit']")).click();

        // Chờ có bảng hoặc thông báo "Không có khách hàng"
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Không') or contains(text(),'No')]"))
        ));

        String pageSource = driver.getPageSource().toLowerCase();
        boolean noResult = pageSource.contains("không tìm thấy")
                || pageSource.contains("không có")
                || driver.findElements(By.cssSelector("table tbody tr")).isEmpty();

        Assert.assertTrue(noResult, "Kết quả tìm kiếm không hợp lệ, đáng lẽ không có khách hàng nào được tìm thấy.");
    }

    /**
     * Kiểm tra tìm kiếm với ký tự đặc biệt
     */
    @Test
    public void testSearchWithSpecialCharacters() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/customer");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("keyword")));

        WebElement searchBox = driver.findElement(By.name("keyword"));
        searchBox.clear();
        searchBox.sendKeys("!@#$%^&*()");
        driver.findElement(By.cssSelector("form button[type='submit']")).click();

        // Chờ có bảng hoặc thông báo
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Không') or contains(text(),'No')]"))
        ));

        String source = driver.getPageSource().toLowerCase();
        boolean safeHandled = source.contains("không tìm thấy")
                || source.contains("không có")
                || driver.findElements(By.cssSelector("table tbody tr")).isEmpty();

        Assert.assertTrue(safeHandled, "Ứng dụng không xử lý đúng khi nhập ký tự đặc biệt.");
    }

    /**
     * Kiểm tra khi tìm kiếm rỗng (hiển thị toàn bộ danh sách)
     */
    @Test
    public void testSearchWithEmptyKeyword() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/customer");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("keyword")));

        WebElement searchBox = driver.findElement(By.name("keyword"));
        searchBox.clear();
        driver.findElement(By.cssSelector("form button[type='submit']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));

        int rowCount = driver.findElements(By.cssSelector("table tbody tr")).size();
        Assert.assertTrue(rowCount > 0, "Không hiển thị danh sách khách hàng khi tìm kiếm rỗng.");
    }
}
