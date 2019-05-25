package com.four_leader.snl.onetime;

import java.io.Serializable;

public class DefaultCategory implements Serializable {
    private String name;
    private String code;
    private int step1;
    private int step2;
    private Boolean isChecked;


    public DefaultCategory(String code, String name, int step1, int step2, Boolean isChecked) {
        this.name = name;
        this.code = code;
        this.step1 = step1;
        this.step2 = step2;
        this.isChecked = isChecked;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStep1() {
        return step1;
    }

    public void setStep1(int step1) {
        this.step1 = step1;
    }

    public int getStep2() {
        return step2;
    }

    public void setStep2(int step2) {
        this.step2 = step2;
    }
}
