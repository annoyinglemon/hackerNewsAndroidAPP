package com.example.lemon.hackernews;

/**
 * Created by Lemon on 10/1/2016.
 */

public class NewsObject {

    private long newsID;
    private String newsURL;
    private String newsTitle;
    private String newsAuthor;
    private int newsScore;
    private int commentCount;
    private long creationDate;

    public NewsObject(long newsID, String newsTitle, String newsURL, String newsAuthor, int newsScore, int commentCount, long creationDate) {
        this.newsID = newsID;
        this.newsTitle = newsTitle;
        this.newsURL = newsURL;
        this.newsAuthor = newsAuthor;
        this.newsScore = newsScore;
        this.commentCount = commentCount;
        this.creationDate = creationDate;
    }

    public NewsObject(String newsTitle, String newsURL, String newsAuthor, int newsScore, int commentCount, long creationDate) {
        this.newsTitle = newsTitle;
        this.newsURL = newsURL;
        this.newsAuthor = newsAuthor;
        this.newsScore = newsScore;
        this.commentCount = commentCount;
        this.creationDate = creationDate;
    }

    public long getNewsID() {
        return newsID;
    }

    public void setNewsID(long newsID) {
        this.newsID = newsID;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsURL() {
        return newsURL;
    }

    public void setNewsURL(String newsURL) {
        this.newsURL = newsURL;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    public void setNewsAuthor(String newsAuthor) {
        this.newsAuthor = newsAuthor;
    }

    public int getNewsScore() {
        return newsScore;
    }

    public void setNewsScore(int newsScore) {
        this.newsScore = newsScore;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
}
