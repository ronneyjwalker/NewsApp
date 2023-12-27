package com.ronakjain.newsaggregator;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.Instant;

import android.annotation.SuppressLint;

import java.time.ZoneId;

public class ArticleDetails {
    private String imageLink;
    private String newsMatter;
    private String newsLink;
    private String newsWriter;
    private String newsName;
    private String newsTitle;
    private String newsDescription;
    private String newsDate;

    public ArticleDetails(String newsWriter, String newsTitle, String newsDescription, String newsLink, String imageLink,
                          String newsDate, String newsMatter, String newsName) {
        setNewsName(newsName);
        setImageLink(imageLink);
        setNewsWriter(newsWriter);
        setNewsTitle(newsTitle);
        setNewsDate(newsDate);
        setNewsDescription(newsDescription);
        setNewsMatter(newsMatter);
        setNewsLink(newsLink);
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setNewsDescription(String newsDescription) {
        if (newsWriter.equals("null")) {
            this.newsDescription = "No description available";
        } else {
            this.newsDescription = newsDescription;
        }
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public String getNewsWriter() {
        return newsWriter;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public void setNewsWriter(String newsWriter) {
        if (newsWriter.equals("null")) {
            this.newsWriter = newsName;
        } else {
            this.newsWriter = newsWriter;
        }

    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public void setNewsMatter(String newsMatter) {
        if (newsMatter.equals("null")) {
            this.newsMatter = "";
        } else {
            this.newsMatter = newsMatter;
        }
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    @Override
    public String toString() {
        return "Article{"
                + "newsWriter='" + newsWriter+ '\''
                + ", newsTitle='" + newsTitle + '\''
                + ", newsDescription='" + newsDescription + '\''
                + ", newsLink='" + newsLink + '\''
                + ", imageLink='" + imageLink + '\''
                + ", newsDate='" + newsDate + '\''
                + ", newsMatter='" + newsMatter + '\''
                + '}';
    }

    @SuppressLint("NewApi")
    public String dateTimeFormatter() {
        try {
            DateTimeFormatter parser = DateTimeFormatter.ISO_DATE_TIME;
            Instant instant = parser.parse(this.getNewsDate(), Instant::from);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm ");

            return localDateTime.format(dateTimeFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
