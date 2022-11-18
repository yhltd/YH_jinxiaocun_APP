package com.example.myapplication.finance.service;


import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import java.util.List;


//筹资收入
public class YhFinanceInvestmentIncomeService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceExpenditure> getList(String company) {
        String sql = "select id,investmentIncome as shouru,company from InvestmentIncome where company = ?";
        base = new financeBaseDao();
        List<YhFinanceExpenditure> list = base.query(YhFinanceExpenditure.class, sql, company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceExpenditure YhFinanceExpenditure) {
        String sql = "insert into InvestmentIncome(investmentIncome,company) values(?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceExpenditure.getShouru(), YhFinanceExpenditure.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceExpenditure YhFinanceExpenditure) {
        String sql = "update InvestmentIncome set investmentIncome=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceExpenditure.getShouru(), YhFinanceExpenditure.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from InvestmentIncome where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
