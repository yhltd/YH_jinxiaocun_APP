package com.example.myapplication.scheduling.entity;

import java.io.Serializable;

public class ShengChanXian implements Serializable {
    private int id;
    private String mingcheng;
    private String gongxu;
    private String gongsi;
    private String xiaolv;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMingcheng() {
        return mingcheng;
    }

    public void setMingcheng(String mingcheng) {
        this.mingcheng = mingcheng;
    }

    public String getGongxu() {
        return gongxu;
    }

    public void setGongxu(String gongxu) {
        this.gongxu = gongxu;
    }

    public String getGongsi() {
        return gongsi;
    }

    public void setGongsi(String gongsi) {
        this.gongsi = gongsi;
    }

    public String getXiaolv() {
        return xiaolv;
    }

    public void setXiaolv(String xiaolv) {
        this.xiaolv = xiaolv;
    }
}