package com.example.myapplication.scheduling.entity;

public class OrderInfo {
    private int id;
    private String code;
    private String product_name;
    private String norms;
    private String order_id;
    private String set_date;
    private int set_num;
    private String company;
    private String is_complete;
    private boolean check;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getNorms() {
        return norms;
    }

    public void setNorms(String norms) {
        this.norms = norms;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSet_date() {
        return set_date;
    }

    public void setSet_date(String set_date) {
        this.set_date = set_date;
    }

    public int getSet_num() {
        return set_num;
    }

    public void setSet_num(int set_num) {
        this.set_num = set_num;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIs_complete() {
        return is_complete;
    }

    public void setIs_complete(String is_complete) {
        this.is_complete = is_complete;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
