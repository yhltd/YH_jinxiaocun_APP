package com.example.myapplication.finance.entity;

import java.math.BigDecimal;

public class YhFinanceKeMuZongZhang {

    private int id;
    private String name;
    private String code;
    private String grade;
    private String company;
    private String name1;
    private String name2;
    private String name3;
    private boolean direction;
    private String direction_text;
    private String mingxi;
    private String money;
    private BigDecimal load;
    private BigDecimal borrowed;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public String getDirection_text() {
        return direction_text;
    }

    public void setDirection_text(String direction_text) {
        this.direction_text = direction_text;
    }

    public String getMingxi() {
        return mingxi;
    }

    public void setMingxi(String mingxi) {
        this.mingxi = mingxi;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public BigDecimal getLoad() {
        return load;
    }

    public void setLoad(BigDecimal load) {
        this.load = load;
    }

    public BigDecimal getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(BigDecimal borrowed) {
        this.borrowed = borrowed;
    }
}
