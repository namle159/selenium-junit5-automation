package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import util.DriverFactory;
import util.DriverManager;
import util.ScreenshotOnFailureExtension;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ScreenshotOnFailureExtension.class)
public class SauceDemoTest {

    @BeforeEach
    void setup(TestInfo testInfo) {
        String browser = System.getProperty("browser", "chrome");
        WebDriver driver = DriverFactory.createDriver(browser);
        DriverManager.setDriver(driver);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        System.out.println("🧩 Running test: " + testInfo.getDisplayName() + " on " + browser);
    }

    @Test
    @Order(1)
    @DisplayName("Login with valid user")
    void testLoginSuccess() {
        WebDriver driver = DriverManager.getDriver();
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @Test
    @Order(2)
    @DisplayName("Add item to cart and checkout")
    void testAddToCartAndCheckout() {
        WebDriver driver = DriverManager.getDriver();
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.cssSelector(".inventory_item button")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("Nam");
        driver.findElement(By.id("last-name")).sendKeys("Le");
        driver.findElement(By.id("postal-code")).sendKeys("10000");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Thank you for your order!"));
    }

    @AfterEach
    void tearDown() {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            driver.quit();
        }
        DriverManager.unload();
    }
}
