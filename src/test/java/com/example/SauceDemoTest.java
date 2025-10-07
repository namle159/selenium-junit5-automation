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
        wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // CI chậm hơn
        System.out.println("🧩 Running test: " + testInfo.getDisplayName() + " on " + browser);
    }

    // Positive Test Case: Login with valid user
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

    // Negative Test Case 1: Login with invalid username
    @Test
    @Order(2)
    @DisplayName("Login with invalid username")
    void testLoginWithInvalidUsername() {
        WebDriver driver = DriverManager.getDriver();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("invalid_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@data-test=\"error\"]")));
        Assertions.assertEquals("Epic sadface: Username and password do not match any user in this service", errorMessage.getText());
    }

    // Negative Test Case 2: Login with invalid password
    @Test
    @Order(3)
    @DisplayName("Login with invalid password")
    void testLoginWithInvalidPassword() {
        WebDriver driver = DriverManager.getDriver();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@data-test=\"error\"]")));
        Assertions.assertEquals("Epic sadface: Username and password do not match any user in this service", errorMessage.getText());
    }

    // Negative Test Case 3: Login with empty username and password
    @Test
    @Order(4)
    @DisplayName("Login with empty username and password")
    void testLoginWithEmptyCredentials() {
        WebDriver driver = DriverManager.getDriver();
        driver.findElement(By.id("login-button")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@data-test=\"error\"]")));
        Assertions.assertTrue(errorMessage.getText().contains("Epic sadface: Username is required"));
    }

    // Negative Test Case 4: Login with locked-out user
    @Test
    @Order(5)
    @DisplayName("Login with locked-out user")
    void testLoginWithLockedOutUser() {
        WebDriver driver = DriverManager.getDriver();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@data-test=\"error\"]")));
        Assertions.assertEquals("Epic sadface: Sorry, this user has been locked out.", errorMessage.getText());
    }

    // Negative Test Case 5: Login with invalid format username
    @Test
    @Order(6)
    @DisplayName("Login with invalid username format")
    void testLoginWithInvalidUsernameFormat() {
        WebDriver driver = DriverManager.getDriver();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("user!@#$%");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@data-test=\"error\"]")));
        Assertions.assertTrue(errorMessage.getText().contains("Epic sadface: Username and password do not match any user in this service"));
    }

    @Test
    @Order(7)   
    @DisplayName("Add item to cart and checkout")
    void testAddToCartAndCheckout() throws InterruptedException{
        WebDriver driver = DriverManager.getDriver();

        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        // Add specific item (ổn định hơn so với ".inventory_item button")
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();

        // Open cart
        // wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link"))).click();
        WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);
        
        // Wait for URL to change to cart page
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html")); // Increased wait for cart page loading

        // Checkout (presence + scroll + JS click để tránh overlay/headless issues)
        WebElement checkout = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkout);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkout);

        // Your info
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))).sendKeys("Nam");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))).sendKeys("Le");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))).sendKeys("10000");

        // Ensure the fields are populated before clicking continue
        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue")));

        // Wait for a second to make sure the element is ready
        Thread.sleep(5000);

        // Click the continue button
        WebElement continueButton = driver.findElement(By.id("continue"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);


        // Finish
        WebElement finishButton = driver.findElement(By.id("finish"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", finishButton);

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
