package com.example.myapplication.finance.entity;

import java.math.BigDecimal;

public class YhFinanceXianJinLiuLiang {

    private String expenditure;
    private BigDecimal money_month;
    private BigDecimal money_year;

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public BigDecimal getMoney_month() {
        return money_month;
    }

    public void setMoney_month(BigDecimal money_month) {
        this.money_month = money_month;
    }

    public BigDecimal getMoney_year() {
        return money_year;
    }

    public void setMoney_year(BigDecimal money_year) {
        this.money_year = money_year;
    }
}
