package com.example.myapplication.finance.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class YhFinanceWaiBi {
    private int id;
    private String bizhong;
    private String huilv;
    private String company;

    // 构造方法
    public YhFinanceWaiBi() {}

    // Getter和Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBizhong() {
        return bizhong;
    }

    public void setBizhong(String bizhong) {
        this.bizhong = bizhong;
    }

    public String getHuilv() {
        return huilv;
    }

    public void setHuilv(String huilv) {
        this.huilv = huilv;
    }

    // 获取汇率为BigDecimal类型
    public BigDecimal getHuilvAsBigDecimal() {
        try {
            if (huilv != null && !huilv.trim().isEmpty()) {
                return new BigDecimal(huilv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ONE; // 默认汇率1
    }

    @Override
    public String toString() {
        return "YhFinanceWaiBi{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", bizhong='" + bizhong + '\'' +
                ", huilv='" + huilv + '\'' +
                '}';
    }

}
