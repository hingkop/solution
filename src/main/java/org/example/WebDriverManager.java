package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class WebDriverManager {

    public static WebDriver createWebDriver(String chromeDriverPath) {
        if (chromeDriverPath == null || chromeDriverPath.isEmpty()) {
            throw new RuntimeException("Chrome driver path is null or empty.");
        }
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        // 실제 창 띄워서 보고싶으면 옵션 제거
        options.addArguments("headless");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    public static void quitWebDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}
