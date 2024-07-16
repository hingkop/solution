package org.example;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
            WebElement contentElement = null;
            try {
                contentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dic_area")));
            } catch (TimeoutException e) {
                log.warn("Content element not found for article {}", articleId);
            }
            String content = (contentElement != null) ? contentElement.getText() : "";

            // 기자명 기다렸다가 크롤링
            WebElement journalistElement = null;
            try {
                journalistElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".byline_s")));
            } catch (TimeoutException e) {
                log.warn("Journalist element not found for article {}", articleId);
            }
            String journalist = (journalistElement != null) ? journalistElement.getText() : "";

            // 날짜와 시간 기다렸다가 크롤링
            WebElement datetimeElement = null;
            try {
                datetimeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.media_end_head_info_datestamp_time._ARTICLE_DATE_TIME[data-date-time]")));
            } catch (TimeoutException e) {
                log.warn("Datetime element not found for article {}", articleId);
            }
            String datetime = (datetimeElement != null) ? datetimeElement.getText() : "";

            // 데이터베이스에 기사 내용과 기자명을 업데이트
            dbManager.updateArticleContentAndJournalist(articleId, content, journalist, datetime);

        } catch (Exception e) {
            // 예외 상세 확인하고 싶을 시 주석 해제
            e.printStackTrace();
            System.out.println(articleId + " 해당 기사의 내용이 없습니다.");
        }
    }

}