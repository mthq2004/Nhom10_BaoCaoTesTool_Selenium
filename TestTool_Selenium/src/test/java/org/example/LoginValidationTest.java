package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginValidationTest extends BaseTest {

    /**
     * 1. Test: Đăng nhập sai thông tin hiển thị thông báo lỗi
     */
    @Test
    public void testInvalidLoginShowsErrorMessage() throws InterruptedException {
        driver.get(baseUrl + "/login");

        driver.findElement(By.name("username")).sendKeys("wronguser");
        driver.findElement(By.name("password")).sendKeys("wrongpass");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Thread.sleep(800);

        boolean hasError = driver.findElements(By.cssSelector(".alert-danger")).size() > 0
                || driver.getPageSource().contains("Sai tên đăng nhập")
                || driver.getPageSource().toLowerCase().contains("mật khẩu");

        Assert.assertTrue(hasError, "Không xuất hiện thông báo lỗi khi đăng nhập sai.");
    }

    /**
     * 2. Test: Khách hàng đăng nhập hợp lệ → về trang chủ /product
     */
    @Test
    public void testValidCustomerLoginRedirectsToHome() throws InterruptedException {
        driver.get(baseUrl + "/login");

        driver.findElement(By.name("username")).sendKeys(customerUser);
        driver.findElement(By.name("password")).sendKeys(customerPass);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Thread.sleep(1200);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Customer redirected to: " + currentUrl);

        boolean redirected = currentUrl.contains("/product")
                || currentUrl.equals(baseUrl + "/")
                || driver.getPageSource().toLowerCase().contains("danh sách sản phẩm")
                || driver.getPageSource().toLowerCase().contains("xin chào");

        Assert.assertTrue(redirected, "Không chuyển hướng đến trang chủ sau khi đăng nhập hợp lệ.");
    }

    /**
     * 🧪 3. Test: Admin đăng nhập → về trang quản trị (product list)
     */
    @Test
    public void testAdminLoginRedirectsToDashboard() throws InterruptedException {
        driver.get(baseUrl + "/login");

        driver.findElement(By.name("username")).sendKeys(adminUser);
        driver.findElement(By.name("password")).sendKeys(adminPass);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Thread.sleep(1200);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Admin redirected to: " + currentUrl);

        boolean isAdminDashboard = currentUrl.equals(baseUrl + "/")
                || currentUrl.contains("/product")
                || driver.getPageSource().toLowerCase().contains("danh sách sản phẩm")
                || driver.getPageSource().toLowerCase().contains("thêm sản phẩm");

        Assert.assertTrue(isAdminDashboard, "Admin không được chuyển đến trang quản trị sau khi đăng nhập.");
    }


    /**
     * 4. Test: Kiểm tra yêu cầu nhập username/password rỗng
     */
    @Test
    public void testEmptyFieldsShowValidationError() {
        driver.get(baseUrl + "/login");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        boolean hasRequiredAttr = driver.findElement(By.name("username")).getAttribute("required") != null
                && driver.findElement(By.name("password")).getAttribute("required") != null;

        Assert.assertTrue(hasRequiredAttr, "Form đăng nhập không yêu cầu nhập thông tin bắt buộc.");
    }
}
