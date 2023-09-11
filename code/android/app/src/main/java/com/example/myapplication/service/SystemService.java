package com.example.myapplication.service;

import com.example.myapplication.dao.systemBaseDao;
import com.example.myapplication.entity.SoftTime;
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

    public List<SoftTime> getSoftTime(String now, String name, String soft_name) {
        String sql = "select CASE WHEN convert(date,endtime)< convert(date,?) THEN 1 ELSE 0 END as endtime,CASE WHEN convert(date,mark2)< convert(date,?) THEN 1 ELSE 0 END as mark2,mark1,isnull(mark3,'') as mark3 from control_soft_time where [name] = ? and soft_name = ?";
        base = new systemBaseDao();
        List<SoftTime> list = base.query(SoftTime.class, sql,now,now,name, soft_name);
        return list;
    }

    public List<SystemBanner> getTongYongList(String system) {
        String sql = "select * from system_banner where system = ? and company = '通用'";
        base = new systemBaseDao();
        List<SystemBanner> list = base.query(SystemBanner.class, sql,system);
        return list;
    }

}
