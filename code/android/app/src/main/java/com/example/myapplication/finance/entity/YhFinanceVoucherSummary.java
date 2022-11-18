package com.example.myapplication.finance.entity;

import com.google.gson.annotations.SerializedName;

import net.sourceforge.jtds.jdbc.DateTime;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class YhFinanceVoucherSummary {
    private int id;
    private String full_name;
    private String word;
    private String no;
    @SerializedName("abstract")
    private String _abstract;
    private int code;
    private String department;
    private String expenditure;
    private String note;
    private String man;
    private String name;
    private BigDecimal money;
    private Timestamp voucherDate;
    private String company;
    private BigDecimal real;
    private BigDecimal load;
    private BigDecimal borrowed;
    private BigDecimal not_get;
    private String zhaiyao;

    private Timestamp insert_date;

    public Timestamp getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Timestamp insert_date) {
        this.insert_date = insert_date;
    }

    public String getZhaiyao() {
        return zhaiyao;
    }

    public void setZhaiyao(String zhaiyao) {
        this.zhaiyao = zhaiyao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String get_abstract() {
        return _abstract;
    }

    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Timestamp getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(Timestamp voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public BigDecimal getReal() {
        return real;
    }

    public void setReal(BigDecimal real) {
        this.real = real;
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

    public BigDecimal getNot_get() {
        return not_get;
    }

    public void setNot_get(BigDecimal not_get) {
        this.not_get = not_get;
    }
}
