package com.four_leader.snl.notice.vo;

public class Notice {
    private String num; //글번호
    private String msg; // 알림 내용

    public Notice(String num, String msg) {
        this.num = num;
        this.msg = msg;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
