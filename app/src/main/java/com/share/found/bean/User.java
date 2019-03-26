package com.share.found.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;


public class User extends BmobUser {

    private String avatar;

    private String xueli;
    private String time;
    private int num;
    private BmobGeoPoint address;

    public BmobGeoPoint getAddress() {
        return address;
    }

    public void setAddress(BmobGeoPoint address) {
        this.address = address;
    }

    public User(){}

    public String getXueli() {
        return xueli;
    }

    public void setXueli(String xueli) {
        this.xueli = xueli;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
