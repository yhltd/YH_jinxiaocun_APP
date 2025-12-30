package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceGongZiMingXi;
import com.example.myapplication.finance.entity.YhFinanceShuiLv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class YhFinanceShuiLvService {

    financeBaseDao base;

    public List<YhFinanceShuiLv> getList(String company) {
        String sql = "select * from shuilvPeizhi where company = ? order by linjiezhi asc";
        base = new financeBaseDao();
        List<YhFinanceShuiLv> list = base.query(YhFinanceShuiLv.class, sql, company);
        return list;
    }

    public boolean insert(YhFinanceShuiLv shuiLv) {
        String sql = "insert into shuilvPeizhi(company, shuilv, linjiezhi) values(?,?,?)";
        base = new financeBaseDao();

        long result = base.executeOfId(sql, shuiLv.getCompany(), shuiLv.getShuilv(),shuiLv.getLinjiezhi());

        return result > 0;
    }


    public boolean update(YhFinanceShuiLv shuiLv) {
        String sql = "update shuilvPeizhi set company=?, shuilv=?, linjiezhi=? where id=?";
        base = new financeBaseDao();


        boolean result = base.execute(sql, shuiLv.getCompany(), shuiLv.getShuilv(),shuiLv.getLinjiezhi(),shuiLv.getId());
        return result;
    }



    public boolean delete(int id) {
        String sql = "delete from shuilvPeizhi where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }

}