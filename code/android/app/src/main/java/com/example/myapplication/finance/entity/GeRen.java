package com.example.myapplication.finance.entity;

import com.google.gson.annotations.SerializedName;

public class GeRen {
    private int id;
    private String company;
    private String pwd;
    @SerializedName("do")
    private String doo;
    private String name;
    private String bianhao;
    private String touxiang;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public String getCompany() {
        return company;
    }

    public String getPwd() {
        return pwd;
    }

    public String getDoo() {
        return doo;
    }

    public void setDoo(String doo) {
        this.doo = doo;
    }

    public String getName() {
        return name;
    }

    public String getBianhao() {
        return bianhao;
    }

    public String getTouxiang () {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }
}