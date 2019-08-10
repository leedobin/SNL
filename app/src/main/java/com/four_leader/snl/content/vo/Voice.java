package com.four_leader.snl.content.vo;

import java.io.Serializable;

public class Voice implements Serializable {
    private String fileName;
    private String writer;
    private int heartCount;
    private String code;
    private boolean heartClick;
    private boolean itemClick;
    private String writerSeq;

    public Voice() {
        heartCount = 0;
        heartClick = false;
        itemClick = false;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHeartClick() {
        return heartClick;
    }

    public void setHeartClick(boolean heartClick) {
        this.heartClick = heartClick;
    }

    public boolean isItemClick() {
        return itemClick;
    }

    public void setItemClick(boolean itemClick) {
        this.itemClick = itemClick;
    }

    public String getWriterSeq() {
        return writerSeq;
    }

    public void setWriterSeq(String writerSeq) {
        this.writerSeq = writerSeq;
    }
}
