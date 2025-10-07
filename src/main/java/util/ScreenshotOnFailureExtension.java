package util;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            WebDriver driver = DriverManager.getDriver(); 

            if (driver != null) {
                File screenshotDir = new File("target/screenshots");
                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }

                String testName = context.getTestMethod().get().getName();
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                
                FileUtils.copyFile(screenshot, new File(screenshotDir, testName + ".png"));
                System.out.println("🧩 Screenshot taken for test: " + testName);
            }
        }
    }
}
