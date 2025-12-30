package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceBaoBiao;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceLiRun;

import java.util.ArrayList;
import java.util.List;

public class YhFinanceLiRunService {

    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceLiRun> getList_shou(String company, String start_date, String stop_date) {
        // 提取年份用于本年累计计算
        String year_start = start_date.substring(0, 4) + "-01-01";
        String year_end = start_date.substring(0, 4) + "-12-31";
        String sql = "SELECT " +
                "  sub.xiangmu, " +
                "  sub.kemu, " +
                "  sub.benqijine, " +
                "  sub.bennianleiji, " +
                "  sub.shangqijine, " +
                "  SUM(sub.benqijine) OVER(PARTITION BY sub.xiangmu) AS zbenqijine, " +
                "  SUM(sub.bennianleiji) OVER(PARTITION BY sub.xiangmu) AS zbennianleiji, " +
                "  SUM(sub.shangqijine) OVER(PARTITION BY sub.xiangmu) AS zshangqijine " +
                "FROM (" +
                "  SELECT " +
                "    a.project AS xiangmu, " +
                "    a.accounting AS kemu, " +
                "    SUM(CASE WHEN a.insert_date >= ? AND a.insert_date <= ? THEN (a.receipts - a.payment) ELSE 0 END) AS benqijine, " +
                "    SUM(CASE WHEN a.insert_date >= ? AND a.insert_date <= ? THEN (a.receipts - a.payment) ELSE 0 END) AS bennianleiji, " +
                "    SUM(CASE WHEN a.insert_date < ? THEN (a.receipts - a.payment) ELSE 0 END) AS shangqijine " +
                "  FROM SimpleData a " +
                "  WHERE a.company = ? " +
                "    AND a.receipts IS NOT NULL " +
                "    AND a.payment IS NOT NULL " +
                "  GROUP BY a.project, a.accounting " +
                ") sub " +
                "ORDER BY sub.xiangmu, sub.kemu";

        base = new financeBaseDao();


        List<YhFinanceLiRun> list = base.query(YhFinanceLiRun.class, sql,start_date,stop_date,year_start,year_end,start_date,company);
        return list;
    }
}
