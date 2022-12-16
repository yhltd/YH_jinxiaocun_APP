package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceLiYiSunYi;

import java.util.List;

public class YhFinanceLiYiSunYiService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceLiYiSunYi> getList(String company, int this_class, String start_date, String stop_date) {
        String sql = "select name,sum_month,sum_year from (select name,y.sum_month,y.sum_year,row_number() over(order by name) as rownum from Accounting as a,(SELECT code,isnull((SELECT sum(money) FROM VoucherSummary WHERE voucherDate >= CONVERT(date,?) and voucherDate <= CONVERT(date,?) AND code = y.code),0) AS sum_month,isnull((SELECT sum(money) FROM VoucherSummary WHERE YEAR(voucherDate) = year(CONVERT(date,?)) AND code = y.code),0) AS sum_year FROM VoucherSummary AS y WHERE company = ? and YEAR(voucherDate) = year(CONVERT(date,?)) GROUP BY y.code) as y where a.code = y.code and a.company = ? and a.direction = ?) as t";
        base = new financeBaseDao();
        List<YhFinanceLiYiSunYi> list = base.query(YhFinanceLiYiSunYi.class, sql,start_date,stop_date,start_date,company,start_date,company,this_class);
        return list;
    }

}
