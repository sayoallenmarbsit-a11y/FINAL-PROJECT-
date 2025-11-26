package com.act1.mytodolistapp;

public class Task {
    private String title;
    private String content;
    private String dateTime;  // Added date/time field

    public Task(String title, String content, String dateTime) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDateTime() {
        return dateTime;
    }
}
