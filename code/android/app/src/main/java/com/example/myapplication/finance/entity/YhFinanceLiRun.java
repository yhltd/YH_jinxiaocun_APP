package com.example.myapplication.finance.entity;

import java.math.BigDecimal;

public class YhFinanceLiRun {
    private int id;
    private String company;
    private String xiangmu;
    private String kemu;
    private BigDecimal zbenqijine;
    private BigDecimal zbennianleiji;
    private BigDecimal zshangqijine;
    private BigDecimal benqijine;
    private BigDecimal bennianleiji;
    private BigDecimal shangqijine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public String getXiangmu() {
        return xiangmu;
    }

    public void setXiangmu(String xiangmu) {
        this.xiangmu = xiangmu;
    }

    public String getKemu() {
        return kemu;
    }

    public void setKemu(String kemu) {
        this.kemu = kemu;
    }

    public BigDecimal getZbenqijine() {
        return zbenqijine;
    }

    public void setZbenqijine(BigDecimal zbenqijine) {
        this.zbenqijine = zbenqijine;
    }

    public BigDecimal getZbennianleiji() {
        return zbennianleiji;
    }

    public void setZbennianleiji(BigDecimal zbennianleiji) {
        this.zbennianleiji = zbennianleiji;
    }

    public BigDecimal getZshangqijine() {
        return zshangqijine;
    }

    public void setZshangqijine(BigDecimal zshangqijine) {
        this.zshangqijine = zshangqijine;
    }



    public BigDecimal getBenqijine() {
        return benqijine;
    }

    public void setBenqijine(BigDecimal benqijine) {
        this.benqijine = benqijine;
    }

    public BigDecimal getBennianleiji() {
        return bennianleiji;
    }

    public void setBennianleiji(BigDecimal bennianleiji) {
        this.bennianleiji = bennianleiji;
    }

    public BigDecimal getShangqijine() {
        return shangqijine;
    }

    public void setShangqijine(BigDecimal shangqijine) {
        this.shangqijine = shangqijine;
    }
}
