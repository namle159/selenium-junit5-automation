package util;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScreenshotOnFailureExtension implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver driver = DriverManager.getDriver();
        if (driver instanceof TakesScreenshot) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dest = Path.of("target/screenshots",
                    context.getDisplayName().replaceAll("[^a-zA-Z0-9.-]", "_") + ".png");
            try {
                Files.createDirectories(dest.getParent());
                Files.copy(screenshot.toPath(), dest);
                System.out.println("📸 Screenshot saved: " + dest.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
    