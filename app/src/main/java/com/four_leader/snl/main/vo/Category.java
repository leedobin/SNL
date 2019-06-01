package com.four_leader.snl.main.vo;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private String seq;
    private String step1;
    private String step2;
    private Boolean isChecked;

    public Category(String name, String seq, Boolean isChecked) {
        this.name = name;
        this.seq = seq;
        this.isChecked = isChecked;
    }

    public Category(String seq, String name, String step1, String step2) {
        this.seq = seq;
        this.name = name;
        this.step1 =step1;
        this.step2 = step2;
        this.isChecked = false;
    }
    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getStep1() {
        return step1;
    }

    public void setStep1(String step1) {
        this.step1 = step1;
    }

    public String getStep2() {
        return step2;
    }

    public void setStep2(String step2) {
        this.step2 = step2;
    }
}
