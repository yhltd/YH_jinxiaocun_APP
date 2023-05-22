package com.example.myapplication.jiaowu.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class Student implements Serializable {
    private int id;
    @SerializedName("RealName")
    private String realname;
    private String sex;
    private String rgdate;
    private String course;
    private String teacher;
    private String classnum;
    private String phone;
    private int fee;
    private BigDecimal mall;
    private float nocost;
    private BigDecimal nall;
    private float nohour;
    private int allhour;
    private String type;
    @SerializedName("Company")
    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRgdate() {
        return rgdate;
    }

    public void setRgdate(String rgdate) {
        this.rgdate = rgdate;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassnum() {
        return classnum;
    }

    public void setClassnum(String classnum) {
        this.classnum = classnum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }


    public BigDecimal getMall() {
        return mall;
    }

    public void setMall(BigDecimal mall) {
        this.mall = mall;
    }

    public BigDecimal getNall() {
        return nall;
    }

    public void setNall(BigDecimal nall) {
        this.nall = nall;
    }

    public float getNocost() {
        return nocost;
    }

    public void setNocost(float nocost) {
        this.nocost = nocost;
    }

    public float getNohour() {
        return nohour;
    }

    public void setNohour(float nohour) {
        this.nohour = nohour;
    }

    public int getAllhour() {
        return allhour;
    }

    public void setAllhour(int allhour) {
        this.allhour = allhour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
