package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;

import java.util.ArrayList;
import java.util.List;

public class YhFinanceDepartmentService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceDepartment> getList(String company) {
        String sql = "select * from Department where company = ?";
        base = new financeBaseDao();
        List<YhFinanceDepartment> list = base.query(YhFinanceDepartment.class, sql, company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceDepartment YhFinanceDepartment) {
        String sql = "insert into Department(department,man,company) values(?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceDepartment.getDepartment(), YhFinanceDepartment.getMan(), YhFinanceDepartment.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceDepartment YhFinanceDepartment) {
        String sql = "update Department set department=?,man=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceDepartment.getDepartment(), YhFinanceDepartment.getMan(), YhFinanceDepartment.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from Department where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
