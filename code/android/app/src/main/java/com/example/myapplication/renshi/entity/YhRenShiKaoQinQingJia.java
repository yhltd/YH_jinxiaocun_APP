// YhRenShiQingJiaShenPi.java
package com.example.myapplication.renshi.entity;

public class YhRenShiKaoQinQingJia {

    private int id;
    private String bumen;            // 部门
    private String xingming;         // 姓名
    private String tijiaoshijian;    // 提交时间
    private String qsqingjiashijian; // 请假开始时间
    private String jzqingjiashijan; // 请假结束时间
    private String qingjiayuanyin;   // 请假原因
    private String zhuangtai;        // 状态：待审批/驳回/通过
    private String shenpiyuanyin;    // 审批原因
    private String gongsi;           // 公司

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBumen() {
        return bumen;
    }

    public void setBumen(String bumen) {
        this.bumen = bumen;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public String getTijiaoshijian() {
        return tijiaoshijian;
    }

    public void setTijiaoshijian(String tijiaoshijian) {
        this.tijiaoshijian = tijiaoshijian;
    }

    public String getQsqingjiashijian() {
        return qsqingjiashijian;
    }

    public void setQsqingjiashijian(String qsqingjiashijian) {
        this.qsqingjiashijian = qsqingjiashijian;
    }

    public String getJzqingjiashijian() {
        return jzqingjiashijan;
    }

    public void setJzqingjiashijian(String jzqingjiashijan) {
        this.jzqingjiashijan = jzqingjiashijan;
    }

    public String getQingjiayuanyin() {
        return qingjiayuanyin;
    }

    public void setQingjiayuanyin(String qingjiayuanyin) {
        this.qingjiayuanyin = qingjiayuanyin;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getShenpiyuanyin() {
        return shenpiyuanyin;
    }

    public void setShenpiyuanyin(String shenpiyuanyin) {
        this.shenpiyuanyin = shenpiyuanyin;
    }

    public String getGongsi() {
        return gongsi;
    }

    public void setGongsi(String gongsi) {
        this.gongsi = gongsi;
    }
}