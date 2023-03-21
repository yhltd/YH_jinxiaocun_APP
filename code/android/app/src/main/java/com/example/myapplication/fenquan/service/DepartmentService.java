package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Jisuan;

import java.util.List;

public class DepartmentService {
    private FenquanDao base;

    public List<Department> getDepartment(String company) {
        String sql = "select department_name from baitaoquanxian_department where company = ? group by department_name";
        base = new FenquanDao();
        List<Department> list = base.query(Department.class, sql,company);
        return list;
    }

    public List<Department> getList(String company, String department_name) {
        String sql = "select * from baitaoquanxian_department where company=? and department_name like '%'+?+'%'";
        base = new FenquanDao();
        List<Department> list = base.query(Department.class, sql, company,department_name);
        return list;
    }

    public boolean insert(Department Department) {
        String sql = "insert into baitaoquanxian_department(department_name,view_name,ins,del,upd,sel,company) values(?,?,?,?,?,?,?)";
        base = new FenquanDao();
        long result = base.executeOfId(sql, Department.getDepartment_name(), Department.getView_name(), Department.getIns(),Department.getDel(),Department.getUpd(),Department.getSel(),Department.getDepartment_name());
        return result > 0;
    }

    public boolean update(Department Department) {
        String sql = "update baitaoquanxian_department set department_name=?,view_name=?,ins=?,del=?,upd=?,sel=? where id = ?";
        base = new FenquanDao();
        return base.execute(sql, Department.getDepartment_name(), Department.getView_name(), Department.getIns(),Department.getDel(),Department.getUpd(),Department.getSel(),Department.getId());
    }

    public boolean delete(int id) {
        String sql = "delete from baitaoquanxian_department where id = ?";
        base = new FenquanDao();
        return base.execute(sql, id);
    }
}
