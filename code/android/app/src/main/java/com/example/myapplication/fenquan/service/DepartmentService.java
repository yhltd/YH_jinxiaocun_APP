package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Department;
import java.util.List;

public class DepartmentService {
    private FenquanDao base;

    public List<Department> getDepartment() {
        String sql = "select department_name from baitaoquanxian_department group by department_name";
        base = new FenquanDao();
        List<Department> list = base.query(Department.class, sql);
        return list;
    }
}
