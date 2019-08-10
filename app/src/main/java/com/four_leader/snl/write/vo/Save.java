package com.four_leader.snl.write.vo;

import java.io.Serializable;

public class Save implements Serializable {
    String category1;
    String category2;
    String title;
    String content;
    String saveTime;
    String hashTag;

    public Save() {
        this.category1 = "";
        this.category2 = "";
        this.title = "";
        this.content = "";
        this.saveTime = "";
        this.hashTag = "";
    }

    public Save(String category1, String category2, String title, String content, String saveTime, String hashTag) {
        this.category1 = category1;
        this.category2 = category2;
        this.title = title;
        this.content = content;
        this.saveTime = saveTime;
        this.hashTag = hashTag;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }
}
