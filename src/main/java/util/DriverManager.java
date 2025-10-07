package util;

import org.openqa.selenium.WebDriver;

public final class DriverManager {
    private static final ThreadLocal<WebDriver> TL = new ThreadLocal<>();

    private DriverManager() {}

    public static void setDriver(WebDriver driver) {
        TL.set(driver);
    }

    public static WebDriver getDriver() {
        return TL.get();
    }

    public static void unload() {
        TL.remove();
    }
}
