package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.Holiday;
import com.example.myapplication.scheduling.entity.ModuleType;

import java.util.List;

public class HolidayService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<Holiday> getList(String company) {
        String sql = "select * from holiday_config where company=?";
        base = new SchedulingDao();
        List<Holiday> list = base.query(Holiday.class, sql, company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(Holiday holiday) {
        String sql = "insert into holiday_config(day_or_reset,company) values(?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, holiday.getDay_or_reset(), holiday.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(Holiday holiday) {
        String sql = "update holiday_config set day_or_reset=? where id=? ";

        base = new SchedulingDao();
        boolean result = base.execute(sql, holiday.getDay_or_reset(), holiday.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from holiday_config where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }
}
