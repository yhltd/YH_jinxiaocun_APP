package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import com.example.myapplication.finance.entity.YhFinanceVoucherSummary;

import java.util.List;

public class YhFinanceJiJianTaiZhangService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceJiJianTaiZhang> getList(String company, String project, String start_date, String stop_date) {
        String sql = "select a.id,a.company,a.insert_date,a.project,a.kehu,a.receivable,a.receipts,a.cope,a.payment,a.accounting,a.nashuijine,a.yijiaoshuijine,a.zhaiyao from (select row_number() over(order by id) as rownum,* from SimpleData where company = ? and project like '%'+ ? +'%') as a where a.insert_date >= ? and insert_date <= ?";
        base = new financeBaseDao();
        List<YhFinanceJiJianTaiZhang> list = base.query(YhFinanceJiJianTaiZhang.class, sql,company,project,start_date,stop_date);
        return list;
    }

    public List<YhFinanceJiJianTaiZhang> getList1(String company) {
        String sql = "select a.id,a.company,a.insert_date,a.project,a.kehu,a.receivable,a.receipts,a.cope,a.payment,a.accounting,a.nashuijine,a.yijiaoshuijine,a.zhaiyao from (select row_number() over(order by id) as rownum,* from SimpleData where company = ?) as a ";
        base = new financeBaseDao();
        List<YhFinanceJiJianTaiZhang> list = base.query(YhFinanceJiJianTaiZhang.class, sql,company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceJiJianTaiZhang YhFinanceJiJianTaiZhang) {
        String sql = "insert into SimpleData(company,project,receivable,receipts,cope,payment,accounting,insert_date,kehu,zhaiyao,nashuijine,yijiaoshuijine) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceJiJianTaiZhang.getCompany(), YhFinanceJiJianTaiZhang.getProject(),YhFinanceJiJianTaiZhang.getReceivable(),YhFinanceJiJianTaiZhang.getReceipts(),YhFinanceJiJianTaiZhang.getCope(),YhFinanceJiJianTaiZhang.getPayment(),YhFinanceJiJianTaiZhang.getAccounting(),YhFinanceJiJianTaiZhang.getInsert_date(),YhFinanceJiJianTaiZhang.getKehu(),YhFinanceJiJianTaiZhang.getZhaiyao(),YhFinanceJiJianTaiZhang.getNashuijine(),YhFinanceJiJianTaiZhang.getYijiaoshuijine());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceJiJianTaiZhang YhFinanceJiJianTaiZhang) {
        String sql = "update SimpleData set company=?,project=?,receivable=?,receipts=?,cope=?,payment=?,accounting=?,insert_date=?,kehu=?,zhaiyao=?,nashuijine=?,yijiaoshuijine=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceJiJianTaiZhang.getCompany(), YhFinanceJiJianTaiZhang.getProject(),YhFinanceJiJianTaiZhang.getReceivable(),YhFinanceJiJianTaiZhang.getReceipts(),YhFinanceJiJianTaiZhang.getCope(),YhFinanceJiJianTaiZhang.getPayment(),YhFinanceJiJianTaiZhang.getAccounting(),YhFinanceJiJianTaiZhang.getInsert_date(),YhFinanceJiJianTaiZhang.getKehu(),YhFinanceJiJianTaiZhang.getZhaiyao(),YhFinanceJiJianTaiZhang.getNashuijine(),YhFinanceJiJianTaiZhang.getYijiaoshuijine(),YhFinanceJiJianTaiZhang.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from SimpleData where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
