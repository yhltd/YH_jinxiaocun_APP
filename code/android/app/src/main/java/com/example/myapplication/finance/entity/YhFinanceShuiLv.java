package com.example.myapplication.finance.entity;

import java.io.Serializable;

public class YhFinanceShuiLv implements Serializable {
    private Integer id;
    private String company;
    private String shuilv;
    private String linjiezhi;   // 临界值 - 改为String类型

    // 构造方法
    public YhFinanceShuiLv() {}

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

    public String getShuilv() {
        return shuilv;
    }

    public void setShuilv(String shuilv) {
        this.shuilv = shuilv;
    }

    public String getLinjiezhi() {
        return linjiezhi;
    }

    public void setLinjiezhi(String linjiezhi) {
        this.linjiezhi = linjiezhi;
    }

    // 添加转换方法，将String转换为Double（如果需要）
    public Double getLinjiezhiAsDouble() {
        if (linjiezhi == null || linjiezhi.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(linjiezhi);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // 格式化显示
    public String getLinjiezhiFormatted() {
        if (linjiezhi == null || linjiezhi.trim().isEmpty()) {
            return "0.00";
        }
        try {
            Double value = Double.parseDouble(linjiezhi);
            return String.format("%.2f", value);
        } catch (NumberFormatException e) {
            return linjiezhi;
        }
    }
}