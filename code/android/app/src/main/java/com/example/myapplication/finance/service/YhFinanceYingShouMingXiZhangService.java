package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceBaoBiao;
import com.example.myapplication.finance.entity.YhFinanceYingShouMingXiZhang;

import java.util.List;

public class YhFinanceYingShouMingXiZhangService {
    private financeBaseDao base;

    /**
     * 查询本期全部数据
     */
    public List<YhFinanceYingShouMingXiZhang> getList_shou(String company, String kehu, String start_date, String stop_date) {
        String sql = "select a.id,a.company,a.insert_date,a.project,a.kehu,a.receivable,a.receipts,a.receivable-a.receipts as weishou,a.cope,a.payment,a.cope-a.payment as weifu,a.accounting,a.zhaiyao from (select row_number() over(order by id) as rownum,* from SimpleData where company = ? and kehu = ? and (receivable-receipts)<>0 and insert_date>=? and insert_date<=? ) as a";
        base = new financeBaseDao();
        List<YhFinanceYingShouMingXiZhang> list = base.query(YhFinanceYingShouMingXiZhang.class, sql,company,kehu,start_date,stop_date);
        return list;
    }

    /**
     * 查询本期全部数据
     */
    public List<YhFinanceYingShouMingXiZhang> getList_fu(String company, String kehu, String start_date, String stop_date) {
        String sql = "select a.id,a.company,a.insert_date,a.project,a.kehu,a.receivable,a.receipts,a.receivable-a.receipts as weishou,a.cope,a.payment,a.cope-a.payment as weifu,a.accounting,a.zhaiyao from (select row_number() over(order by id) as rownum,* from SimpleData where company = ? and kehu = ? and (cope-payment)<>0 and insert_date>=? and insert_date<=?) as a";
        base = new financeBaseDao();
        List<YhFinanceYingShouMingXiZhang> list = base.query(YhFinanceYingShouMingXiZhang.class, sql,company,kehu,start_date,stop_date);
        return list;
    }

    /**
     * 查询上期全部数据
     */
    public List<YhFinanceYingShouMingXiZhang> getList_shouLast(String company,String kehu, String start_date, String stop_date) {
        String sql = "select 0 as id,'' as company,null as insert_date,'' as project,b.kehu,b.receivable,b.receipts,b.receivable-b.receipts as weishou,0.0 as cope,0.0 as payment,'' as accounting,'' as zhaiyao from (select kehu,sum(receivable) as receivable,sum(receipts) as receipts from SimpleData where company = ? and kehu = ? and insert_date<? group by kehu) as b";
        base = new financeBaseDao();
        List<YhFinanceYingShouMingXiZhang> list = base.query(YhFinanceYingShouMingXiZhang.class, sql,company,kehu,start_date);
        return list;
    }

    /**
     * 查询上期全部数据
     */
    public List<YhFinanceYingShouMingXiZhang> getList_fuLast(String company,String kehu, String start_date, String stop_date) {
        String sql = "select 0 as id,'' as company,null as insert_date,'' as project,b.kehu,0.0 as receivable,0.0 as receipts,b.cope,b.payment,b.cope-b.payment as weifu,'' as accounting,'' as zhaiyao from (select kehu,sum(cope) as cope,sum(payment) as payment from SimpleData where company = ? and kehu = ? and insert_date<? group by kehu) as b";
        base = new financeBaseDao();
        List<YhFinanceYingShouMingXiZhang> list = base.query(YhFinanceYingShouMingXiZhang.class, sql,company,kehu,start_date);
        return list;
    }

    /**
     * 查询上期全部数据
     */
    public List<YhFinanceYingShouMingXiZhang> getList_shouSecond(String company,String kehu, String start_date, String stop_date) {
        String sql = "select 0 as id,'' as company,null as insert_date,'' as project,b.kehu,b.receivable,b.receipts,b.receivable-b.receipts as weishou,0.0 as cope,0.0 as payment,'' as accounting,'' as zhaiyao from (select kehu,sum(receivable) as receivable,sum(receipts) as receipts from SimpleData where company = ? and kehu = ? and insert_date<=? group by kehu) as b";
        base = new financeBaseDao();
        List<YhFinanceYingShouMingXiZhang> list = base.query(YhFinanceYingShouMingXiZhang.class, sql,company,kehu,stop_date);
        return list;
    }

    /**
     * 查询上期全部数据
     */
    public List<YhFinanceYingShouMingXiZhang> getList_fuSecond(String company,String kehu, String start_date, String stop_date) {
        String sql = "select 0 as id,'' as company,null as insert_date,'' as project,b.kehu,0.0 as receivable,0.0 as receipts,b.cope,b.payment,b.cope-b.payment as weifu,'' as accounting,'' as zhaiyao from (select kehu,sum(cope) as cope,sum(payment) as payment from SimpleData where company = ? and kehu = ? and insert_date<=? group by kehu) as b";
        base = new financeBaseDao();
        List<YhFinanceYingShouMingXiZhang> list = base.query(YhFinanceYingShouMingXiZhang.class, sql,company,kehu,stop_date);
        return list;
    }


}
