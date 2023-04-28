package com.example.myapplication.service;

import com.example.myapplication.dao.systemBaseDao;
import com.example.myapplication.entity.SystemBanner;
import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.SimpleData;

import java.util.List;

public class SystemService {
    private systemBaseDao base;

    public List<SystemBanner> getList(String system, String company) {
        String sql = "select * from system_banner where system = ? and company = ?";
        base = new systemBaseDao();
        List<SystemBanner> list = base.query(SystemBanner.class, sql,system, company);
        return list;
    }

    public List<SystemBanner> getTongYongList(String system) {
        String sql = "select * from system_banner where system = ? and company = '通用'";
        base = new systemBaseDao();
        List<SystemBanner> list = base.query(SystemBanner.class, sql,system);
        return list;
    }

}
