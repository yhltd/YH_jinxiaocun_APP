package com.example.myapplication.scheduling.entity;

public class OrderBom {
    private int id;
    private int order_id;
    private int bom_id;
    private int use_num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getBom_id() {
        return bom_id;
    }

    public void setBom_id(int bom_id) {
        this.bom_id = bom_id;
    }

    public int getUse_num() {
        return use_num;
    }

    public void setUse_num(int use_num) {
        this.use_num = use_num;
    }
}
