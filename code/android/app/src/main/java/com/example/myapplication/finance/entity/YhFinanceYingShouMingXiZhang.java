package com.example.myapplication.finance.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class YhFinanceYingShouMingXiZhang {

    private Timestamp insert_date;
    private String kehu;
    private String accounting;
    private String project;
    private BigDecimal receivable;
    private BigDecimal receipts;
    private BigDecimal weishou;
    private BigDecimal cope;
    private BigDecimal payment;
    private BigDecimal weifu;

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

    public String getAccounting() {
        return accounting;
    }

    public void setAccounting(String accounting) {
        this.accounting = accounting;
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

    public BigDecimal getReceipts() {
        return receipts;
    }

    public void setReceipts(BigDecimal receipts) {
        this.receipts = receipts;
    }

    public BigDecimal getWeishou() {
        return weishou;
    }

    public void setWeishou(BigDecimal weishou) {
        this.weishou = weishou;
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

    public BigDecimal getWeifu() {
        return weifu;
    }

    public void setWeifu(BigDecimal weifu) {
        this.weifu = weifu;
    }
}
