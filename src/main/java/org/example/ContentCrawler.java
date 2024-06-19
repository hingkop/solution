package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ContentCrawler {
    private WebDriver driver;
    private DatabaseManager dbManager;

    public ContentCrawler(WebDriver driver, DatabaseManager dbManager) {
        this.driver = driver;
        this.dbManager = dbManager;
    }

    public void crawlArticleContent(int articleId, String url) {
        try {
            System.out.println("Crawling content from: " + url);

            // WebDriver로 기사 페이지 열기
            driver.get(url);

            // WebDriverWait로 대기 설정
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 기사 내용을 담고 있는 요소를 기다림
            WebElement contentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dic_area")));
            String content = contentElement.getText();

            // 기자명을 담고 있는 요소를 기다림
            WebElement journalistElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".byline_s")));
            String journalist = journalistElement.getText();

            // 결과 출력
            System.out.println("Article ID: " + articleId);
            System.out.println("Journalist: " + journalist);

            // 데이터베이스에 기사 내용과 기자명을 업데이트
            dbManager.updateArticleContentAndJournalist(articleId, content, journalist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}