package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.PaibanRenyuan;

import java.util.List;

public class PaibanRenyuanService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<PaibanRenyuan> getList(String company, String staffName, String banci) {
        base = new SchedulingDao();
        String sql = "select * from paibanbiao_renyuan where company=? and staff_name like '%' + ? + '%' and banci like '%' + ? + '%' ";
        List<PaibanRenyuan> list = base.query(PaibanRenyuan.class, sql, company, staffName, banci);
        return list;
    }

    public List<PaibanRenyuan> getListByDepartment(String company, String department) {
        base = new SchedulingDao();
        String sql = "select * from paibanbiao_renyuan where company=? and department_name = ? ";
        List<PaibanRenyuan> list = base.query(PaibanRenyuan.class, sql, company, department);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(PaibanRenyuan paibanRenyuan) {
        String sql = "insert into paibanbiao_renyuan(staff_name,phone_number,banci,department_name,id_number,company) values(?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, paibanRenyuan.getStaff_name(), paibanRenyuan.getPhone_number(), paibanRenyuan.getBanci(), paibanRenyuan.getDepartment_name(), paibanRenyuan.getId_number(), paibanRenyuan.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(PaibanRenyuan paibanRenyuan) {
        String sql = "update paibanbiao_renyuan set staff_name=?,phone_number=?,banci=?,department_name=?,id_number=?,company=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, paibanRenyuan.getStaff_name(), paibanRenyuan.getPhone_number(), paibanRenyuan.getBanci(), paibanRenyuan.getDepartment_name(), paibanRenyuan.getId_number(), paibanRenyuan.getCompany(),paibanRenyuan.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from paibanbiao_renyuan where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }

}
