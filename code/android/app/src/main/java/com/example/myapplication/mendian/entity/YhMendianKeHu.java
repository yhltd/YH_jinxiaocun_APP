package com.example.myapplication.mendian.entity;

public class YhMendianKeHu {

    private int id;
    private String recipient;
    private String cardholder;
    private String drawee;
    private String issuing_bank;
    private String bill_day;
    private String repayment_date;
    private String total;
    private String repayable;
    private String balance;
    private String loan;
    private String service_charge;
    private String telephone;
    private String password;
    private String staff;
    private String gongsi;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getDrawee() {
        return drawee;
    }

    public void setDrawee(String drawee) {
        this.drawee = drawee;
    }

    public String getIssuing_bank() {
        return issuing_bank;
    }

    public void setIssuing_bank(String issuing_bank) {
        this.issuing_bank = issuing_bank;
    }

    public String getBill_day() {
        return bill_day;
    }

    public void setBill_day(String bill_day) {
        this.bill_day = bill_day;
    }

    public String getRepayment_date() {
        return repayment_date;
    }

    public void setRepayment_date(String repayment_date) {
        this.repayment_date = repayment_date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRepayable() {
        return repayable;
    }

    public void setRepayable(String repayable) {
        this.repayable = repayable;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getGongsi() {
        return gongsi;
    }

    public void setGongsi(String gongsi) {
        this.gongsi = gongsi;
    }

    public YhMendianKeHu() {

    }

    public YhMendianKeHu(int id, String recipient, String cardholder, String drawee, String issuing_bank, String bill_day, String repayment_date, String total, String repayable, String balance, String loan, String service_charge, String telephone, String password, String staff, String gongsi) {
        this.id = id;
        this.recipient = recipient;
        this.cardholder = cardholder;
        this.drawee = drawee;
        this.issuing_bank = issuing_bank;
        this.bill_day = bill_day;
        this.repayment_date = repayment_date;
        this.total = total;
        this.repayable = repayable;
        this.balance = balance;
        this.loan = loan;
        this.service_charge = service_charge;
        this.telephone = telephone;
        this.password = password;
        this.staff = staff;
        this.gongsi = gongsi;
    }

}
