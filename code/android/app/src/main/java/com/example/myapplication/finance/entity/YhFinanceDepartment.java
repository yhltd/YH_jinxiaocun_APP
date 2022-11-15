package com.example.myapplication.finance.entity;

public class YhFinanceDepartment {

    private int id;
    private String department;
    private String man;
    private String company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public String getMan() {
        return man;
    }

    public String getCompany() {
        return company;
    }

}
