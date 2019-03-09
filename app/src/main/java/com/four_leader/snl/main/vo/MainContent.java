package com.four_leader.snl.main.vo;

import java.io.Serializable;

public class MainContent implements Serializable {
    private String code;
    private String title;
    private String category;
    private int endDate;
    private String content;
    private String userNicname;
    private String time;
    private int heartCount;
    private int voiceCount;
    private int bookmarkCount;
    private String fileName;
    private boolean isPlayLayoutOpen;
    private boolean isBookmarked;

    public MainContent() {
        isPlayLayoutOpen = false;
        isBookmarked = false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserNicname() {
        return userNicname;
    }

    public void setUserNicname(String userNicname) {
        this.userNicname = userNicname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public int getVoiceCount() {
        return voiceCount;
    }

    public void setVoiceCount(int voiceCount) {
        this.voiceCount = voiceCount;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isPlayLayoutOpen() {
        return isPlayLayoutOpen;
    }

    public void setPlayLayoutOpen(boolean playLayoutOpen) {
        isPlayLayoutOpen = playLayoutOpen;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
