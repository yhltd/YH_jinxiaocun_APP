package com.example.myapplication.jiaowu.entity;

import java.io.Serializable;

public class ShouZhiMingXi implements Serializable {

    private int id;
    private String rgdate;
    private String money;
    private String msort;
    private String mremark;
    private String paid;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
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

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
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
