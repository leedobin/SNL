package com.four_leader.snl.main.vo;

import java.io.Serializable;

public class DefaultCategory implements Serializable {
    private String name;
    private String code;
    private Boolean isChecked;


    public DefaultCategory(String name, String code, Boolean isChecked) {
        this.name = name;
        this.code = code;
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
}
