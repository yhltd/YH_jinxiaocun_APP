package com.example.myapplication.scheduling.entity;

import java.io.Serializable;

public class WorkDetail implements Serializable {
    private int id;
    private String orderId;
    private int work_num;
    private String work_start_date;
    private String company;
    private int row_num;
    private int is_insert;
    private String type;
    private int module_id;
    private String module;
    private double num;
    private String order_number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getWork_num() {
        return work_num;
    }

    public void setWork_num(int work_num) {
        this.work_num = work_num;
    }

    public String getWork_start_date() {
        return work_start_date;
    }

    public void setWork_start_date(String work_start_date) {
        this.work_start_date = work_start_date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getRow_num() {
        return row_num;
    }

    public void setRow_num(int row_num) {
        this.row_num = row_num;
    }

    public int getIs_insert() {
        return is_insert;
    }

    public void setIs_insert(int is_insert) {
        this.is_insert = is_insert;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
