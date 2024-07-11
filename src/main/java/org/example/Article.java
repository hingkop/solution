package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Article {
    private int id;
    private String title;
    private String href;
    private String datetime;
    private String content;
    private String press;
    private String journalist;

    public String getUrl() {
        return href;
    }
}