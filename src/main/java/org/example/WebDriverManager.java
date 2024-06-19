package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class WebDriverManager {
    private static final String CHROME_DRIVER_PATH = "C:/Users/sjpde/biz/02.tools/chromedriver-win64/chromedriver.exe";
    public static WebDriver createWebDriver() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("headless"); // Headless 모드를 사용할 경우 주석 해제
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