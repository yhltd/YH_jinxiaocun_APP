package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.jxc.dao.JxcBaseDao;

import java.util.ArrayList;
import java.util.List;

public class YhFinanceUserService {
    private financeBaseDao base;

    public YhFinanceUser login(String username, String password, String company) {
        String sql = "select * from Account where name = ? and pwd = ? and company = ? ";
        base = new financeBaseDao();
        List<YhFinanceUser> list = base.query(YhFinanceUser.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<String> getCompany() {
        String sql = "select company from Account group by company";
        base = new financeBaseDao();
        List<YhFinanceUser> list = base.query(YhFinanceUser.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getCompany());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }
}
