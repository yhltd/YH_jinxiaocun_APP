package com.example.myapplication.scheduling.entity;

public class Holiday {
    private int id;
    private String day_or_reset;
    private String company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay_or_reset() {
        return day_or_reset;
    }

    public void setDay_or_reset(String day_or_reset) {
        this.day_or_reset = day_or_reset;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
