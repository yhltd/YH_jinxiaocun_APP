package com.example.myapplication.jxc.entity;

import java.io.Serializable;
import java.util.List;

public class PrintData  implements Serializable {
    private String orderNumber;
    private String storeName;
    private String orderTime;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private double totalAmount;
    private double paidAmount;
    private String paymentMethod;
    private String cangku;
    private String shou_h;
    private String mxtype;
    private List<OrderItem> items;

    public PrintData() {}

    // Getter和Setter方法
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getCangku() { return cangku; }
    public void setCangku(String cangku) { this.cangku = cangku; }

    public String getShou_h() { return shou_h; }
    public void setShou_h(String shou_h) { this.shou_h = shou_h; }

    public String getmxtype() { return mxtype; }
    public void setmxtype(String mxtype) { this.mxtype = mxtype; }
}