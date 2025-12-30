package com.example.myapplication.finance.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class YhFinanceGongZiMingXi {
    private int id;
    private String company;
    private String renming;
    private BigDecimal weifu;
    private BigDecimal gongzi;
    private BigDecimal koukuan;
    private BigDecimal yifu;
    private String yinhangzhanghu;
    private Timestamp riqi;
    private String beizhu;

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

    public String getRenming() {
        return renming;
    }

    public void setRenming(String renming) {
        this.renming = renming;
    }

    public BigDecimal getWeifu() {
        return weifu;
    }

    public void setWeifu(BigDecimal weifu) {
        this.weifu = weifu;
    }

    public BigDecimal getYifu() {
        return yifu ;
    }

    public void setYifu(BigDecimal yifu) {
        this.yifu  = yifu;
    }

    public BigDecimal getKoukuan() {
        return koukuan;
    }

    public void setKoukuan(BigDecimal koukuan) {
        this.koukuan = koukuan;
    }

    public BigDecimal getGongzi() {
        return gongzi;
    }

    public void setGongzi(BigDecimal gongzi) {
        this.gongzi = gongzi;
    }

    public String getYinhangzhanghu() {
        return yinhangzhanghu;
    }

    public void setYinhangzhanghu(String yinhangzhanghu) {
        this.yinhangzhanghu = yinhangzhanghu;
    }

    public Timestamp getRiqi() {
        return riqi;
    }

    public void setRiqi(Timestamp riqi) {
        this.riqi = riqi;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

}