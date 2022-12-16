package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserInfoService {
    private SchedulingDao base;

    public UserInfo login(String username, String password, String company) {
        String sql = "select * from user_info where user_code = ? and password = ? and company = ? ";
        base = new SchedulingDao();
        List<UserInfo> list = base.query(UserInfo.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<String> getCompany() {
        String sql = "select company from user_info group by company";
        base = new SchedulingDao();
        List<UserInfo> list = base.query(UserInfo.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getCompany());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    /**
     * 刷新
     */
    public List<UserInfo> getList(String company, String user_code) {
        base = new SchedulingDao();
        String sql = "select * from user_info where company=? and user_code like '%' + ? + '%' ";
        List<UserInfo> list = base.query(UserInfo.class, sql, company, user_code);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(UserInfo userInfo) {
        String sql = "insert into user_info(user_code,password,department_name,state,company) values(?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, userInfo.getUser_code(), userInfo.getPassword(), userInfo.getDepartment_name(), userInfo.getState(), userInfo.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(UserInfo userInfo) {
        String sql = "update user_info set user_code=?,password=?,department_name=?,state=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, userInfo.getUser_code(), userInfo.getPassword(), userInfo.getDepartment_name(), userInfo.getState(), userInfo.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from user_info where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }


}
