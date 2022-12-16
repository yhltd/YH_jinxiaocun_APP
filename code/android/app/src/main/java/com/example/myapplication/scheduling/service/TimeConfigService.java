package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.PaibanRenyuan;
import com.example.myapplication.scheduling.entity.TimeConfig;

import java.util.List;

public class TimeConfigService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<TimeConfig> getList(String company) {
        base = new SchedulingDao();
        String sql = "select * from time_config where company=? order by week ";
        List<TimeConfig> list = base.query(TimeConfig.class, sql, company);
        return list;
    }

    /**
     * 修改
     */
    public boolean update(TimeConfig timeConfig) {
        String sql = "update time_config set morning_start=?,morning_end=?,noon_start=?,noon_end=?,night_start=?,night_end=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, timeConfig.getMorning_start(), timeConfig.getMorning_end(), timeConfig.getNoon_start(), timeConfig.getNoon_end(), timeConfig.getNight_start(), timeConfig.getNight_end(),timeConfig.getId());
        return result;
    }
}
