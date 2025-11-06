package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {

    @Test
    public void testLoginLogoutAsAdmin() throws InterruptedException {
        loginAs(adminUser, adminPass);

        // after login, home should show logout button or user-name
        Thread.sleep(500);

        boolean hasLogout = driver.findElements(By.cssSelector(".btn-logout")).size() > 0
                || driver.findElements(By.cssSelector(".user-name")).size() > 0;

        Assert.assertTrue(hasLogout, "Expected to see logout/user-name after login");

        // click logout if available (form button)
        if (driver.findElements(By.cssSelector("form[action='/logout'] button, button.btn-logout")).size() > 0) {
            WebElement btn = driver.findElement(By.cssSelector("form[action='/logout'] button, button.btn-logout"));
            btn.click();
            Thread.sleep(400);

            // after logout, login link should appear
            boolean hasLogin = driver.findElements(By.cssSelector("a.btn-login, a[th\\:href*='/login']")).size() > 0
                    || driver.findElements(By.cssSelector("input[name='username']")).size() > 0;

            Assert.assertTrue(hasLogin, "Expected to see login after logout");
        }
    }
}
