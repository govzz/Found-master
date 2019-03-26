package com.share.found.bean;


import cn.bmob.v3.BmobObject;

public class Symptom extends BmobObject{
    private String type;
    private String score;
    private String Department;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
