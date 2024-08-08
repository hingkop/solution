package org.example;

import org.openqa.selenium.WebDriver;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        String startDate = System.getenv("START_DATE"); // 시작일 입력 ex) 20240401
        String endDate = System.getenv("END_DATE"); // 종료일 입력 ex) 20240401
        String dbUrl = System.getenv("DB_URL");
        String dbUsername = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");
        String chromeDriverPath = System.getenv("CHROME_DRIVER_PATH");

        // 일자별 크롤링 호출
        try {
            Date start = DateHelper.parseDate(startDate);
            Date end = DateHelper.parseDate(endDate);

            WebDriver driver = WebDriverManager.createWebDriver(chromeDriverPath);
            DatabaseManager dbManager = new DatabaseManager(dbUrl, dbUsername, dbPassword);
            ArticleCrawler articleCrawlercrawler = new ArticleCrawler(driver, dbManager);

            Date currentDate = start;
            while (!DateHelper.isAfter(currentDate, end)) {
                String dateString = DateHelper.formatDate(currentDate);
                String url = "https://news.naver.com/breakingnews/section/101/258?date=" + dateString;
                System.out.println(dateString + " 해당 날짜의 기사를 전부 가져오고 있습니다.");
                articleCrawlercrawler.crawl(url);

                currentDate = DateHelper.addDays(currentDate, 1);
            }

            System.out.println("기사 본문을 가져오고 있습니다");

            // 각 기사 내용, 기자명 크롤링
            try {
                dbManager = new DatabaseManager(dbUrl, dbUsername, dbPassword);

                // ContentCrawler 인스턴스 생성
                ContentCrawler contentCrawler = new ContentCrawler(driver, dbManager);

                // DB에서 모든 기사 URL과 ID 가져오기
                List<Article> articles = dbManager.getAllArticleUrls();

                // 각 기사를 크롤링하여 내용 업데이트
                for (Article article : articles) {
                    contentCrawler.crawlArticleContent(article.getId(), article.getUrl());
                }

                System.out.println("기사 본문 크롤링까지 모두 완료되었습니다.");
                System.out.println("감사합니다.");
                WebDriverManager.quitWebDriver(driver);
                dbManager.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}