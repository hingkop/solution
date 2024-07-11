package org.example;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public class ContentCrawler {
    private WebDriver driver;
    private DatabaseManager dbManager;
    private WebDriverWait wait;

    public ContentCrawler(WebDriver driver, DatabaseManager dbManager) {
        this.driver = driver;
        this.dbManager = dbManager;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void crawlArticleContent(int articleId, String url) {
        try {
            // WebDriver로 기사 페이지 열기
            driver.get(url);

            // 기사 내용 기다렸다가 크롤링
            WebElement contentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dic_area")));
            String content = contentElement.getText();

            // 기자명 기다렸다가 크롤링 
            WebElement journalistElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".byline_s")));
            String journalist = journalistElement.getText();

            // 데이터베이스에 기사 내용과 기자명을 업데이트
            dbManager.updateArticleContentAndJournalist(articleId, content, journalist);

        } catch (Exception e) {
            // 예외 상세 확인하고 싶을 시 주석 해제
            // e.printStackTrace();
            System.out.println(articleId + " 해당 기사의 내용이 없습니다.");
        }
    }
}