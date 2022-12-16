package com.example.myapplication.finance.entity;

import java.math.BigDecimal;

public class YhFinanceZiChanFuZhai {

    private String name;
    private BigDecimal load;
    private BigDecimal borrowed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLoad() {
        return load;
    }

    public void setLoad(BigDecimal load) {
        this.load = load;
    }

    public BigDecimal getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(BigDecimal borrowed) {
        this.borrowed = borrowed;
    }
}
