package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.SimpleData;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;

import java.util.List;

public class SimpleDataService {
    private financeBaseDao base;

    public List<SimpleData> getList(String company) {
        String sql = "select * from SimpleData where company = ?";
        base = new financeBaseDao();
        List<SimpleData> list = base.query(SimpleData.class, sql, company);
        return list;
    }

    public List<SimpleData> queryList(String company, String project, String ks, String js) {
        String sql = "select * from SimpleData where company = ? and project = ? and insert_date between ? and ? ";
        base = new financeBaseDao();
        List<SimpleData> list = base.query(SimpleData.class, sql, company);
        return list;
    }

    public boolean insert(SimpleData simpleData) {
        String sql = "insert into SimpleData (company,project,receivable,receipts,cope,payment,accounting,insert_date,kehu,zhaiyao) values(?,?,?,?,?,?,?,?,?,?)";

        base = new financeBaseDao();
        long result = base.executeOfId(sql, simpleData.getCompany(), simpleData.getProject(), simpleData.getReceivable(), simpleData.getReceipts(), simpleData.getCope(), simpleData.getPayment(), simpleData.getAccounting(), simpleData.getInsert_date(), simpleData.getKehu(), simpleData.getZhaiyao());
        return result > 0;
    }

//    public boolean update(SimpleData simpleData) {
//        String sql = "update SimpleData set company=?,project=?,receivable=?,receipts=?,cope=?,payment=?,accounting=?,insert_date=?,kehu=?,zhaiyao=? where id=? ";
//
//        base = new financeBaseDao();
//        boolean result = base.execute(sql, simpleData.getCompany(), simpleData.getProject(), simpleData.getCompany(), simpleData.getCompany(), simpleData.getShouHuo(), simpleData.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getId());
//        return result;
//    }

}
