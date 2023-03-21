package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherService {
    private JiaowuBaseDao base;

    public List<String> getCompany() {
        String sql = "select Company from teacher group by Company";
        base = new JiaowuBaseDao();
        List<Teacher> list = base.query(Teacher.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getCompany());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    public Teacher login(String username, String password, String company) {
        String sql = "select * from teacher where UserName = ? and Password = ? and Company = ?";
        base = new JiaowuBaseDao();
        List<Teacher> list = base.query(Teacher.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }


}
