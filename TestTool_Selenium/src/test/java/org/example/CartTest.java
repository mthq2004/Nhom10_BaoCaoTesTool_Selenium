package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test
    public void testAddToCartAndCheckoutAsCustomer() throws InterruptedException {
        loginAs(customerUser, customerPass);

        // go to product list
        driver.get(baseUrl + "/product");
        Thread.sleep(600);

        // try to find first Add to cart button (form submit)
        if (driver.findElements(By.cssSelector("button.btn-add-cart, form[action='/cart/add'] button")).size() == 0) {
            // If no add-to-cart (maybe role is not customer), fail fast with message
            Assert.fail("No add-to-cart button found; ensure customer user exists and products are available");
        }

        WebElement addBtn = driver.findElement(By.cssSelector("button.btn-add-cart, form[action='/cart/add'] button"));
        addBtn.click();

        Thread.sleep(500);

        // go to cart
        driver.get(baseUrl + "/cart");
        Thread.sleep(500);

        // click checkout button
        if (driver.findElements(By.cssSelector("button.btn-checkout, form[action='/order/checkout'] button"))
                .size() == 0) {
            Assert.fail("Checkout button not found in cart");
        }
        WebElement checkout = driver
                .findElement(By.cssSelector("button.btn-checkout, form[action='/order/checkout'] button"));
        checkout.click();

        Thread.sleep(800);

        // verify success page title or heading
        String title = driver.getTitle();
        Assert.assertTrue(
                title.contains("Đặt hàng thành công") || driver.getPageSource().contains("Đặt hàng thành công"),
                "Expected order success page after checkout");
    }
}
