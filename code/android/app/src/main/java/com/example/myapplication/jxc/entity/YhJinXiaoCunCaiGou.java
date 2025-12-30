package com.example.myapplication.jxc.entity;

import java.io.Serializable;

public class YhJinXiaoCunCaiGou implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String _openid;
    private String cpid;
    private String cpjj;
    private String cplb;
    private String cpname;
    private String cpsj;
    private String cpsl;
    private String finduser;
    private String gongsi;
    private String mxtype;
    private String orderid;
    private String shijian;
    private String spDm;
    private String shou_h;
    private String zhName;
    private String gsName;
    private String cangku;
    private String cangku2;
    private String jcsl;
    private String pankusl;
    private String ruku_num;
    private String pankuisl;
    private String qc_cpsj;
    private String rukuNum;
    private String rukuPrice;
    private String chukuNum;
    private String chukuPrice;
    private String ruku;  // 新增：是否入库字段

    public int get_id() {
        return id;
    }

    public void set_id(int id) {
        this.id = id;
    }

    public String get_openid() {
        return _openid;
    }

    public void set_openid(String _openid) {
        this._openid = _openid;
    }

    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    public String getCpjj() {
        return cpjj;
    }

    public void setCpjj(String cpjj) {
        this.cpjj = cpjj;
    }

    public String getCplb() {
        return cplb;
    }

    public void setCplb(String cplb) {
        this.cplb = cplb;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public String getCpsj() {
        return cpsj;
    }

    public void setCpsj(String cpsj) {
        this.cpsj = cpsj;
    }

    public String getCpsl() {
        return cpsl;
    }

    public void setCpsl(String cpsl) {
        this.cpsl = cpsl;
    }

    public String getFinduser() {
        return finduser;
    }

    public void setFinduser(String finduser) {
        this.finduser = finduser;
    }

    public String getGongsi() {
        return gongsi;
    }

    public void setGongsi(String gongsi) {
        this.gongsi = gongsi;
    }

    public String getMxtype() {
        return mxtype;
    }

    public void setMxtype(String mxtype) {
        this.mxtype = mxtype;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getShijian() {
        return shijian;
    }

    public void setShijian(String shijian) {
        this.shijian = shijian;
    }

    public String getSpDm() {
        return spDm;
    }

    public void setSpDm(String spDm) {
        this.spDm = spDm;
    }

    public String getShou_h() {
        return shou_h;
    }

    public void setShou_h(String shou_h) {
        this.shou_h = shou_h;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getGsName() {
        return gsName;
    }

    public void setGsName(String gsName) {
        this.gsName = gsName;
    }

    public String getRukuNum() {
        return rukuNum;
    }

    public void setRukuNum(String rukuNum) {
        this.rukuNum = rukuNum;
    }

    public String getRukuPrice() {
        return rukuPrice;
    }

    public void setRukuPrice(String rukuPrice) {
        this.rukuPrice = rukuPrice;
    }

    public String getChukuNum() {
        return chukuNum;
    }

    public void setChukuNum(String chukuNum) {
        this.chukuNum = chukuNum;
    }

    public String getChukuPrice() {
        return chukuPrice;
    }

    public void setChukuPrice(String chukuPrice) {
        this.chukuPrice = chukuPrice;
    }

    public String getcangku() {
        return cangku;
    }

    public void setcangku(String cangku) {
        this.cangku = cangku;
    }

    public String getcangku2() {
        return cangku2;
    }

    public void setcangku2(String cangku2) {
        this.cangku2 = cangku2;
    }

    public String getjcsl() {
        return jcsl;
    }

    public void setjcsl(String jcsl) {
        this.jcsl = jcsl;
    }

    public String getpankusl() {
        return pankusl;
    }

    public void setpankusl(String pankusl) {
        this.pankusl = pankusl;
    }

    public String getkucun_sl() {
        return ruku_num;
    }

    public void setkucun_sl(String ruku_num) {
        this.ruku_num = ruku_num;
    }

    public String getpankuisl() {
        return pankuisl;
    }

    public void setpankuisl(String pankuisl) {
        this.pankuisl = pankuisl;
    }

    public String getqc_cpsj() {
        return qc_cpsj;
    }

    public void setqc_cpsj(String qc_cpsj) {
        this.qc_cpsj = qc_cpsj;
    }

    // Getter和Setter
    public String getRuku() {
        return ruku;
    }

    public void setRuku(String ruku) {
        this.ruku = ruku;
    }
}
