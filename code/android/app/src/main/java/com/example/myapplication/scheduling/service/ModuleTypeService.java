package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.ModuleType;

import java.util.ArrayList;
import java.util.List;

public class ModuleTypeService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<ModuleType> getList(String company) {
        String sql = "select * from module_type where company=?";
        base = new SchedulingDao();
        List<ModuleType> list = base.query(ModuleType.class, sql, company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(ModuleType moduleType) {
        String sql = "insert into module_type(name,company) values(?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, moduleType.getName(), moduleType.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(ModuleType moduleType) {
        String sql = "update module_type set name=? where id=? ";

        base = new SchedulingDao();
        boolean result = base.execute(sql, moduleType.getName(), moduleType.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from module_type where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }
}
