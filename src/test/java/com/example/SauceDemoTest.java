package com.example;

import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import util.DriverFactory;
import util.DriverManager;
import util.ScreenshotOnFailureExtension;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ScreenshotOnFailureExtension.class)
public class SauceDemoTest {

    private WebDriverWait wait;

    @BeforeEach
    void setup(TestInfo testInfo) {
        String browser = System.getProperty("browser", "chrome");
        WebDriver driver = DriverFactory.createDriver(browser);
        DriverManager.setDriver(driver);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        // Khởi tạo WebDriverWait (10s)
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("🧩 Running test: " + testInfo.getDisplayName() + " on " + browser);
    }

    @Test
    @Order(1)
    @DisplayName("Login with valid user")
    void testLoginSuccess() {
        WebDriver driver = DriverManager.getDriver();

        // Login steps
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Wait until inventory page loads
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @Test
    @Order(2)
    @DisplayName("Add item to cart and checkout")
    void testAddToCartAndCheckout() {
        WebDriver driver = DriverManager.getDriver();

        // Login first
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Wait for inventory to load and add item
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item button"))).click();

        // Open cart
        wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link"))).click();

        // Checkout process
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))).sendKeys("Nam");
        driver.findElement(By.id("last-name")).sendKeys("Le");
        driver.findElement(By.id("postal-code")).sendKeys("10000");
        driver.findElement(By.id("continue")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();

        // Verify success message
        Assertions.assertTrue(
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Thank you for your order!"))
        );
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
