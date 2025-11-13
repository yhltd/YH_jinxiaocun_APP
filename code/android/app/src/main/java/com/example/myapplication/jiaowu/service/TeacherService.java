package com.example.myapplication.jiaowu.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherService {
    private JiaowuBaseDao base;
    private JiaowuServerDao base1;

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

    public List<String> getCompany1() {
        String sql = "select Company from teacher group by Company";
        base1 = new JiaowuServerDao();
        List<Teacher> list = base1.query(Teacher.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getCompany());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    public Teacher queryByCompanyAndAccount(String companyName, String account, String password) {
        String sql = "SELECT * FROM teacher WHERE Company = ? AND UserName = ? AND Password = ?";
        JiaowuServerDao baseDao = new JiaowuServerDao();
        List<Teacher> userList = baseDao.query(Teacher.class, sql, companyName, account, password);
        if (userList != null && !userList.isEmpty()) {
            return userList.get(0); // 返回第一个匹配的用户
        }else{
            return null;
        }
    }


    public Teacher login(String username, String password, String company) {

        int waitCount = 0;
        while (CacheManager.getInstance().getShujukuValue() == 0 && waitCount < 30) {
            try {
                Thread.sleep(100); // 等待100ms
                waitCount++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

            int shujukuValue = CacheManager.getInstance().getShujukuValue();

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from teacher where UserName = ? and Password = ? and Company = ?";
                base1 = new JiaowuServerDao();
                List<Teacher> list = base1.query(Teacher.class, sql, username, password, company);
                return list != null && list.size() > 0 ? list.get(0) : null;

            } else {
                // MySQL 版本
                String sql = "select * from teacher where UserName = ? and Password = ? and Company = ?";
                base = new JiaowuBaseDao();
                List<Teacher> list = base.query(Teacher.class, sql, username, password, company);
                return list != null && list.size() > 0 ? list.get(0) : null;
            }

    }


}
