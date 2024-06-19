package org.example;

import org.openqa.selenium.WebDriver;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        String startDate = "20240401"; // 시작일 입력 ex) 20240401
        String endDate = "20240401"; // 종료일 입력 ex) 20240401
        String dbUrl = "jdbc:mysql://192.168.91.128:3306/news";
        String dbUsername = "sj";
        String dbPassword = "1388";
        try {
            Date start = DateHelper.parseDate(startDate);
            Date end = DateHelper.parseDate(endDate);

            WebDriver driver = WebDriverManager.createWebDriver();
            DatabaseManager dbManager = new DatabaseManager(dbUrl, dbUsername, dbPassword);
            ArticleCrawler crawler = new ArticleCrawler(driver, dbManager);

            Date currentDate = start;
            while (!DateHelper.isAfter(currentDate, end)) {
                String dateString = DateHelper.formatDate(currentDate);
                String url = "https://news.naver.com/breakingnews/section/101/258?date=" + dateString;
                crawler.crawl(url);

                currentDate = DateHelper.addDays(currentDate, 1);
            }

            WebDriverManager.quitWebDriver(driver);
            dbManager.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}