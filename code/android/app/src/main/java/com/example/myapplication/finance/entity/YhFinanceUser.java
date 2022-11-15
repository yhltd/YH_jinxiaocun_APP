package com.example.myapplication.finance.entity;

import com.google.gson.annotations.SerializedName;

public class YhFinanceUser {
    private String id;
    private String company;
    private String pwd;
    @SerializedName("do")
    private String _do;
    private String name;
    private String bianhao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void set_do(String _do) {
        this._do = _do;
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

    public String get_do() {
        return _do;
    }

    public String getName() {
        return name;
    }

    public String getBianhao() {
        return bianhao;
    }
}
