package com.example.myapplication.finance.entity;

import java.math.BigDecimal;

public class YhFinanceLiYiSunYi {

    private String name;
    private BigDecimal sum_month;
    private BigDecimal sum_year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSum_month() {
        return sum_month;
    }

    public void setSum_month(BigDecimal sum_month) {
        this.sum_month = sum_month;
    }

    public BigDecimal getSum_year() {
        return sum_year;
    }

    public void setSum_year(BigDecimal sum_year) {
        this.sum_year = sum_year;
    }
}
