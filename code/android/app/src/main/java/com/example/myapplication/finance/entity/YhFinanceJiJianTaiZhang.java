package com.example.myapplication.finance.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class YhFinanceJiJianTaiZhang {

    private int id;
    private String company;
    private String project;
    private String bizhong;
    private BigDecimal nashuijine;
    private BigDecimal yijiaoshuijine;
    private BigDecimal receivable;
    private BigDecimal receipts;
    private BigDecimal cope;
    private BigDecimal payment;
    private String accounting;
    private Timestamp insert_date;
    private String kehu;
    private String zhaiyao;

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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }


    public BigDecimal getNashuijine() {
        return nashuijine;
    }

    public void setNashuijine(BigDecimal nashuijine) {
        this.nashuijine = nashuijine;
    }

    public BigDecimal getYijiaoshuijine() {
        return yijiaoshuijine;
    }

    public void setYijiaoshuijine(BigDecimal yijiaoshuijine) {
        this.yijiaoshuijine = yijiaoshuijine;
    }



    public BigDecimal getReceipts() {
        return receipts;
    }

    public void setReceipts(BigDecimal receipts) {
        this.receipts = receipts;
    }

    public BigDecimal getCope() {
        return cope;
    }

    public void setCope(BigDecimal cope) {
        this.cope = cope;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getAccounting() {
        return accounting;
    }

    public void setAccounting(String accounting) {
        this.accounting = accounting;
    }

    public Timestamp getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Timestamp insert_date) {
        this.insert_date = insert_date;
    }

    public String getKehu() {
        return kehu;
    }

    public void setKehu(String kehu) {
        this.kehu = kehu;
    }

    public String getZhaiyao() {
        return zhaiyao;
    }

    public void setZhaiyao(String zhaiyao) {
        this.zhaiyao = zhaiyao;
    }

    public String getBizhong() {
        return bizhong;
    }

    public void setBizhong(String bizhong) {
        this.bizhong = bizhong;
    }
}
