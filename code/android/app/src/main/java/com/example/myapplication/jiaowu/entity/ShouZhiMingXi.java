package com.example.myapplication.jiaowu.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShouZhiMingXi implements Serializable {
    @SerializedName("ID")
    private int id;
    private String rgdate;
    private int money;
    private String msort;
    private String mremark;
    private int paid;
    private String psort;
    private String premark;
    private String handle;
    private String company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRgdate() {
        return rgdate;
    }

    public void setRgdate(String rgdate) {
        this.rgdate = rgdate;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMsort() {
        return msort;
    }

    public void setMsort(String msort) {
        this.msort = msort;
    }

    public String getMremark() {
        return mremark;
    }

    public void setMremark(String mremark) {
        this.mremark = mremark;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getPsort() {
        return psort;
    }

    public void setPsort(String psort) {
        this.psort = psort;
    }

    public String getPremark() {
        return premark;
    }

    public void setPremark(String premark) {
        this.premark = premark;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
