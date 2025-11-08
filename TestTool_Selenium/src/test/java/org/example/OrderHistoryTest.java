package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class OrderHistoryTest extends BaseTest {


//    Khách hàng có thể xem lịch sử đơn hàng của mình
    @Test
    public void testCustomerCanViewOrderHistory() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        loginAs(customerUser, customerPass); // giả sử user này map tới customer “Steve”

        driver.get(baseUrl + "/order");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));

        boolean hasOrder = driver.findElements(By.cssSelector("table tbody tr")).size() > 0;
        Assert.assertTrue(hasOrder, "Không có đơn hàng nào hiển thị trong bảng lịch sử.");
    }


//    Tìm kiếm đơn hàng bởi tên khách hàng

    @Test
    public void testSearchOrderByCustomerName() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/order");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("keyword")));

        WebElement searchBox = driver.findElement(By.name("keyword"));
        searchBox.clear();
        searchBox.sendKeys("Tina");
        driver.findElement(By.cssSelector("form button[type='submit']")).click();

        Thread.sleep(1000);

        boolean hasResult = driver.findElements(By.cssSelector("table tbody tr")).size() > 0;
        Assert.assertTrue(hasResult, "Không tìm thấy hóa đơn chứa tên khách hàng 'Tina'.");
    }

    @Test
    public void testEmptyOrderListShowsMessage() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));

        // Nếu hệ thống chỉ cho phép login customer có đơn hàng,
        // test này có thể dùng adminUser để xem tình huống trống.
        loginAs(adminUser, adminPass);

        driver.get(baseUrl + "/order/search?keyword=ZZZ"); // tìm tên không tồn tại
        Thread.sleep(600);

        boolean hasEmpty = driver.getPageSource().contains("Không có hóa đơn nào")
                || driver.findElements(By.cssSelector(".empty-state")).size() > 0
                || driver.getPageSource().contains("📭");

        Assert.assertTrue(hasEmpty, " Không hiển thị thông báo trống khi không có kết quả tìm kiếm.");
    }
}
