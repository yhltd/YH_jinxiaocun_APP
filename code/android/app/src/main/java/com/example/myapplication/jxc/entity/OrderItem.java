package com.example.myapplication.jxc.entity;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String productName;
    private String specification;
    private int quantity;
    private double price;
    private double subtotal;
    private String cangku;
    private String shou_h;
    private String spdm;


    // 正确的构造函数
    public OrderItem(String productName,String cangku,String shou_h,String spdm, String specification, int quantity, double price) {
        this.productName = productName;
        this.cangku = cangku;
        this.shou_h = shou_h;
        this.spdm = spdm;
        this.specification = specification;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = quantity * price;
    }

    // Getter方法
    public String getProductName() { return productName; }
    public String getShou_h() { return shou_h; }
    public String getSpdm() { return spdm; }
    public String getCangku() { return cangku; }
    public String getSpecification() { return specification; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getSubtotal() { return subtotal; }
}