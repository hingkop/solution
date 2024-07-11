package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ContentCrawlerTest {
    public static void main(String[] args) {
        final String CHROME_DRIVER_PATH = "C:/Users/sjpde/biz/02.tools/chromedriver-win64/chromedriver.exe";

        // WebDriver 설정 (ChromeDriver 사용 예제)
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("headless"); // Headless 모드를 사용할 경우 주석 해제

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // DatabaseManager 설정 (데이터베이스 연결 정보 제공)
        String dbUrl = "jdbc:mysql://192.168.91.128:3306/news";
        String dbUser = "sj";
        String dbPassword = "1388";
        DatabaseManager dbManager = null;

        try {
            dbManager = new DatabaseManager(dbUrl, dbUser, dbPassword);

            // ContentCrawler 인스턴스 생성
            ContentCrawler crawler = new ContentCrawler(driver, dbManager);

            // DB에서 모든 기사 URL과 ID 가져오기
            List<Article> articles = dbManager.getAllArticleUrls();

            // 각 기사를 크롤링하여 내용 업데이트
            for (Article article : articles) {
                crawler.crawlArticleContent(article.getId(), article.getUrl());
            }

        } catch (SQLException e) {
            // 예외 상세 확인하고 싶을 시 주석 해제
            // e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
            // DatabaseManager 종료
            if (dbManager != null) {
                try {
                    dbManager.close();
                } catch (SQLException e) {
                    // 예외 상세 확인하고 싶을 시 주석 해제
                    // e.printStackTrace();
                }
            }
        }
    }
}