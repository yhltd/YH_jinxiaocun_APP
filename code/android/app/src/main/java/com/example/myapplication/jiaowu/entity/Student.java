package com.example.myapplication.jiaowu.entity;

import java.io.Serializable;

public class Student implements Serializable {
    private int id;
    private String realName;
    private String sex;
    private String rgdate;
    private String course;
    private String teacher;
    private String classnum;
    private String phone;
    private String fee;
    private String mall;
    private String nocost;
    private String nall;
    private String nohour;
    private String allhour;
    private String type;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getMall() {
        return mall;
    }

    public void setMall(String mall) {
        this.mall = mall;
    }

    public String getNocost() {
        return nocost;
    }

    public void setNocost(String nocost) {
        this.nocost = nocost;
    }

    public String getNall() {
        return nall;
    }

    public void setNall(String nall) {
        this.nall = nall;
    }

    public String getNohour() {
        return nohour;
    }

    public void setNohour(String nohour) {
        this.nohour = nohour;
    }

    public String getAllhour() {
        return allhour;
    }

    public void setAllhour(String allhour) {
        this.allhour = allhour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
