package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceXianJinLiuLiang;

import java.util.List;

public class YhFinanceXianJinLiuLiangService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceXianJinLiuLiang> getList(String company, String start_date, String stop_date) {
        String sql = "select all_.expenditure,all_.money_month,all_.money_year from (select row_number() over(order by expenditure) as rownum,expenditure,isnull((select sum(s.money) from VoucherSummary as s where company = ? and voucherDate >= CONVERT(date,?) and voucherDate <= CONVERT(date,?) and s.expenditure = v.expenditure),0) as money_month,isnull((select sum(s.money) from VoucherSummary as s where company = ? and year(voucherDate) = year(CONVERT(date,?)) and s.expenditure = v.expenditure),0) as money_year from VoucherSummary as v where company = ? GROUP BY expenditure) as all_ ";
        base = new financeBaseDao();
        List<YhFinanceXianJinLiuLiang> list = base.query(YhFinanceXianJinLiuLiang.class, sql,company,start_date,stop_date,company,start_date,company);
        return list;
    }

}
