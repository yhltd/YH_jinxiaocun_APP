package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceBaoBiao;
import com.example.myapplication.finance.entity.YhFinanceLiYiSunYi;

import java.util.List;

public class YhFinanceBaoBiaoService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceBaoBiao> getList_shou(String company, String start_date, String stop_date) {
        String sql = "select a.id,a.zhaiyao,a.kehu,a.receivable from (select id,zhaiyao,kehu,(convert(Decimal,receivable)-convert(Decimal,receipts)) as receivable,insert_date from SimpleData where company= ? and (convert(Decimal,receivable)-convert(Decimal,receipts))<>0 and insert_date>= ? and insert_date<= ?) as a";
        base = new financeBaseDao();
        List<YhFinanceBaoBiao> list = base.query(YhFinanceBaoBiao.class, sql,company,start_date,stop_date);
        return list;
    }

    /**
     * 查询全部数据
     */
    public List<YhFinanceBaoBiao> getList_zhi(String company, String start_date, String stop_date) {
        String sql = "select a.id,a.zhaiyao as zhaiyao2,a.kehu as kehu2,a.cope from (select id,zhaiyao,kehu,(convert(Decimal,cope)-convert(Decimal,payment)) as cope,insert_date from SimpleData where company= ? and (convert(Decimal,cope)-convert(Decimal,payment))<>0 and insert_date>= ? and insert_date<= ?) as a";
        base = new financeBaseDao();
        List<YhFinanceBaoBiao> list = base.query(YhFinanceBaoBiao.class, sql,company,start_date,stop_date);
        return list;
    }

}
