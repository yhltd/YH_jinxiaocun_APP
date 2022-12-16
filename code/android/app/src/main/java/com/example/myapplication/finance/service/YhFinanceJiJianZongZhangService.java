package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;

import java.util.List;

public class YhFinanceJiJianZongZhangService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceJiJianTaiZhang> getList(String company, String kehu, String project) {
        String sql = "select kehu,project,sum(receivable) as receivable,sum(receipts) as receipts,sum(cope) as cope,sum(payment) as payment  from SimpleData where company=? and kehu like '%'+ ? +'%' and project like '%'+ ? +'%' group by kehu,project";
        base = new financeBaseDao();
        List<YhFinanceJiJianTaiZhang> list = base.query(YhFinanceJiJianTaiZhang.class, sql,company,kehu,project);
        return list;
    }

}
