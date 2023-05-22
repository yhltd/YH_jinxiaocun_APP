package com.example.myapplication.jiaowu.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccountManagement implements Serializable {
    private int id;
    @SerializedName("UserName")
    private String username;
    @SerializedName("Password")
    private String password;
    @SerializedName("RealName")
    private String realname;
    @SerializedName("UseType")
    private int usetype;
    @SerializedName("Age")
    private int age;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("Home")
    private String home;
    @SerializedName("Photo")
    private String photo;
    @SerializedName("Education")
    private String education;
    private String state;
    @SerializedName("Company")
    private String company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getUsetype() {
        return usetype;
    }

    public void setUsetype(int usetype) {
        this.usetype = usetype;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
