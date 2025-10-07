package com.example;

import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.*;
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
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // CI chậm hơn
        System.out.println("🧩 Running test: " + testInfo.getDisplayName() + " on " + browser);
    }

    @Test
    @Order(1)
    @DisplayName("Login with valid user")
    void testLoginSuccess() {
        WebDriver driver = DriverManager.getDriver();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("inventory.html"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @Test
    @Order(2)
    @DisplayName("Add item to cart and checkout")
    void testAddToCartAndCheckout() {
        WebDriver driver = DriverManager.getDriver();

        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        // Add specific item (ổn định hơn so với ".inventory_item button")
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();

        // Open cart
        wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link"))).click();
        wait.until(ExpectedConditions.urlContains("cart.html"));

        // Checkout (presence + scroll + JS click để tránh overlay/headless issues)
        WebElement checkout = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkout);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkout);

        // Your info
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))).sendKeys("Nam");
        driver.findElement(By.id("last-name")).sendKeys("Le");
        driver.findElement(By.id("postal-code")).sendKeys("10000");
        driver.findElement(By.id("continue")).click();

        // Finish
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();

        // Verify by stable selector
        WebElement header = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.complete-header"))
        );
        Assertions.assertEquals("Thank you for your order!", header.getText());
    }

    @AfterEach
    void tearDown() {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) driver.quit();
        DriverManager.unload();
    }
}
