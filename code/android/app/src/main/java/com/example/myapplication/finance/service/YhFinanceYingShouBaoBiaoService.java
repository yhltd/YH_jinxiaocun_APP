package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceBaoBiao;
import com.example.myapplication.finance.entity.YhFinanceYingShouBaoBiao;

import java.util.List;

public class YhFinanceYingShouBaoBiaoService {
    private financeBaseDao base;

    /**
     * 查询全部应收数据
     */
    public List<YhFinanceYingShouBaoBiao> getList_yingshou(String company, String kehu, String start_date, String stop_date) {
        String sql = "select kehu,project,zhaiyao,receivable as jine1 from SimpleData where company=? and kehu=? and insert_date>=? and insert_date<=?";
        base = new financeBaseDao();
        List<YhFinanceYingShouBaoBiao> list = base.query(YhFinanceYingShouBaoBiao.class, sql,company,kehu,start_date,stop_date);
        return list;
    }

    /**
     * 查询全部销项发票数据
     */
    public List<YhFinanceYingShouBaoBiao> getList_xiaoxiang(String company,String kehu, String start_date, String stop_date) {
        String sql = "select unit,invoice_type,invoice_no,jine as jine2 from invoice where company=? and unit=? and type='销项发票' and convert(date,riqi)>=? and convert(date,riqi)<=? ";
        base = new financeBaseDao();
        List<YhFinanceYingShouBaoBiao> list = base.query(YhFinanceYingShouBaoBiao.class, sql,company,kehu,start_date,stop_date);
        return list;
    }

    /**
     * 查询全部应付数据
     */
    public List<YhFinanceYingShouBaoBiao> getList_yingfu(String company, String kehu, String start_date, String stop_date) {
        String sql = "select kehu,project,zhaiyao,cope as jine1 from SimpleData where company=? and kehu=? and insert_date>=? and insert_date<=?";
        base = new financeBaseDao();
        List<YhFinanceYingShouBaoBiao> list = base.query(YhFinanceYingShouBaoBiao.class, sql,company,kehu,start_date,stop_date);
        return list;
    }

    /**
     * 查询全部进项发票数据
     */
    public List<YhFinanceYingShouBaoBiao> getList_jinxiang(String company,String kehu, String start_date, String stop_date) {
        String sql = "select unit,invoice_type,invoice_no,jine as jine2 from invoice where company=? and unit=? and type='进项发票' and convert(date,riqi)>=? and convert(date,riqi)<=? ";
        base = new financeBaseDao();
        List<YhFinanceYingShouBaoBiao> list = base.query(YhFinanceYingShouBaoBiao.class, sql,company,kehu,start_date,stop_date);
        return list;
    }

}
