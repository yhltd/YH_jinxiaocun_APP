package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<Department> getList(String company, String department_name, String view_name) {
        base = new SchedulingDao();
        String sql = "select * from department where company=? and department_name like '%' + ? + '%' and view_name like '%' + ? + '%' ";
        List<Department> list = base.query(Department.class, sql, company, department_name, view_name);
        return list;
    }

    public List<String> getDepartment(String company) {
        base = new SchedulingDao();
        String sql = "select department_name from department where company=? group by department_name ";
        List<Department> list = base.query(Department.class, sql, company);
        List<String> getList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            getList.add(list.get(i).getDepartment_name());
        }
        return getList;
    }

    /**
     * 新增
     */
    public boolean insert(Department department) {
        String sql = "insert into department(department_name,[add],del,upd,sel,view_name,company) values(?,?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, department.getDepartment_name(), department.getAdd(), department.getDel(), department.getUpd(), department.getSel(), department.getView_name(), department.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(Department department) {
        String sql = "update department set department_name=?,[add]=?,del=?,upd=?,sel=?,view_name=?,company=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, department.getDepartment_name(), department.getAdd(), department.getDel(), department.getUpd(), department.getSel(), department.getView_name(), department.getCompany(), department.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from department where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }
}
