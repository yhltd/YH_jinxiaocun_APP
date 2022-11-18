package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;

import java.util.List;

public class YhFinanceInvoicePeizhiService {
    private financeBaseDao base;
    /**
     * 查询全部数据
     */
    public List<YhFinanceJiJianPeiZhi> getList(String company) {
        String sql = "select id,invoice_type as peizhi,company from InvoicePeizhi where company = ?";
        base = new financeBaseDao();
        List<YhFinanceJiJianPeiZhi> list = base.query(YhFinanceJiJianPeiZhi.class, sql, company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceJiJianPeiZhi YhFinanceJiJianPeiZhi) {
        String sql = "insert into InvoicePeizhi(invoice_type,company) values(?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceJiJianPeiZhi.getPeizhi(), YhFinanceJiJianPeiZhi.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceJiJianPeiZhi YhFinanceJiJianPeiZhi) {
        String sql = "update InvoicePeizhi set invoice_type=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceJiJianPeiZhi.getPeizhi(), YhFinanceJiJianPeiZhi.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from InvoicePeizhi where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
