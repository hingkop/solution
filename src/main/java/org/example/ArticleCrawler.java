package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ArticleCrawler {
    private WebDriver driver;
    private DatabaseManager dbManager;
    private ContentCrawler contentCrawler;

    public ArticleCrawler(WebDriver driver, DatabaseManager dbManager) {
        this.driver = driver;
        this.dbManager = dbManager;
        // contentCrawler 초기화를 추가합니다.
        this.contentCrawler = new ContentCrawler(driver, dbManager);
    }

    public void crawl(String url) {
        try {
            System.out.println("Connected to: " + url);

            // WebDriver로 페이지 열기
            driver.get(url);

            // WebDriverWait로 대기 설정
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            while (true) {
                try {
                    // 더보기 버튼을 찾아 클릭
                    WebElement loadMoreButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".section_more_inner._CONTENT_LIST_LOAD_MORE_BUTTON")));
                    loadMoreButton.click();

                    // 버튼 클릭 후 잠시 대기하여 페이지가 로드될 시간을 줌
                    Thread.sleep(2000);

                    // 더보기 버튼이 더 이상 표시되지 않는지 확인
                    if (!driver.findElement(By.cssSelector(".section_more_inner._CONTENT_LIST_LOAD_MORE_BUTTON")).isDisplayed()) {
                        System.out.println("마지막 페이지 입니다");

                        // 크롤링 코드 작성
                        List<WebElement> articles = driver.findElements(By.cssSelector(".sa_text"));
                        for (WebElement article : articles) {
                            WebElement linkElement = article.findElement(By.cssSelector("a.sa_text_title"));
                            String href = linkElement.getAttribute("href");
                            String title = linkElement.getText();

                            WebElement pressElement = article.findElement(By.cssSelector(".sa_text_press"));
                            String press = pressElement.getText();

                            WebElement datetimeElement = article.findElement(By.cssSelector(".sa_text_datetime b"));
                            String datetime = datetimeElement.getText();

                            System.out.println("Title: " + title);

                            dbManager.insertArticle(title, href, datetime, press); // 데이터베이스에 삽입
                        }

                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}