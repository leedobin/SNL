package com.four_leader.snl.notice.vo;

import com.four_leader.snl.main.vo.MainContent;

public class Notice {

    private String seq;
    private String read; // 읽음 상태
    private String msg; // 알림 내용
    private MainContent content;

    public Notice(String seq, String read, String msg, MainContent content) {
        this.seq = seq;
        this.read = read;
        this.msg = msg;
        this.content = content;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MainContent getContent() {
        return content;
    }

    public void setContent(MainContent content) {
        this.content = content;
    }
}
