package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceXianJinLiuLiang;
import com.example.myapplication.finance.entity.YhFinanceZiChanFuZhai;

import java.util.List;

public class YhFinanceZiChanFuZhaiService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceZiChanFuZhai> getList(String company, int this_class, String start_date, String stop_date) {
        String sql = "select name,[load],borrowed,CONVERT(Decimal,0) as money from (select row_number() over(order by a.name) as rownum, a.name,left(a.code,1) as class,v.company,(CASE ? WHEN 1 THEN sum(load-borrowed) ELSE sum(borrowed-load) END) as [load],(CASE ? WHEN 1 THEN sum([load]-borrowed+ISNULL(v.money, 0)) ELSE sum(borrowed-[load]+ISNULL(v.money, 0)) END) as borrowed from Accounting as a left join VoucherSummary as v on a.code = v.code WHERE left(a.code,1) = ? and a.company = ? and v.voucherDate >= convert(date,?) and v.voucherDate <= convert(date,?) GROUP BY a.code,a.name,v.company) as a where (a.company = ? or a.company is null)";
        base = new financeBaseDao();
        List<YhFinanceZiChanFuZhai> list = base.query(YhFinanceZiChanFuZhai.class, sql,this_class,this_class,this_class,company,start_date,stop_date,company);
        return list;
    }

}
