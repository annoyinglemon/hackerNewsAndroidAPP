package com.kurt.lemond.hackernews;

import java.io.Serializable;

/**
 * Created by Lemon on 10/1/2016.
 */

public class NewsObject implements Serializable{

    private long newsID;
    private String newsURL;
    private String newsTitle;
    private String newsAuthor;
    private int newsScore;
    private long creationDate;
    private String localPath;

    public NewsObject() {
    }

    public NewsObject(long newsID, String newsTitle, String newsURL, String newsAuthor, int newsScore, long creationDate) {
        this.newsID = newsID;
        this.newsTitle = newsTitle;
        this.newsURL = newsURL;
        this.newsAuthor = newsAuthor;
        this.newsScore = newsScore;
        this.creationDate = creationDate;
    }

    public NewsObject(String newsTitle, String newsURL, String newsAuthor, int newsScore, long creationDate) {
        this.newsTitle = newsTitle;
        this.newsURL = newsURL;
        this.newsAuthor = newsAuthor;
        this.newsScore = newsScore;
        this.creationDate = creationDate;
    }

    public NewsObject(long newsID, String newsTitle, String newsURL,  String newsAuthor, int newsScore, long creationDate, String localPath) {
        this.newsID = newsID;
        this.newsURL = newsURL;
        this.newsTitle = newsTitle;
        this.newsAuthor = newsAuthor;
        this.newsScore = newsScore;
        this.creationDate = creationDate;
        this.localPath = localPath;
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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
