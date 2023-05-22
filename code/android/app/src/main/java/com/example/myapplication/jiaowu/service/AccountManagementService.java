package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.AccountManagement;

import java.util.List;

public class AccountManagementService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<AccountManagement> getList(String Username, String Realname, String Phone ,String company) {
        String sql = "select * from teacher where Username like '%' ? '%' and Realname like '%' ? '%' and Phone like '%' ? '%' and Company = ?";
        base = new JiaowuBaseDao();
        List<AccountManagement> list = base.query(AccountManagement.class, sql,Username,Realname, Phone,company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(AccountManagement accountManagement) {
        String sql = "insert into teacher(UserName,Password,RealName,UseType,Age,Phone,Home,photo,Education,state,Company) values(?,?,?,?,?,?,?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, accountManagement.getUsername(), accountManagement.getPassword(), accountManagement.getRealname(), accountManagement.getUsetype(), accountManagement.getAge(), accountManagement.getPhone(), accountManagement.getHome(), accountManagement.getPhoto(), accountManagement.getEducation(), accountManagement.getState(), accountManagement.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(AccountManagement accountManagement) {
        String sql = "update teacher set UserName = ?, Password = ?,RealName = ?,UseType = ?,Age = ?,Phone=?,Home=?,photo=?,Education=?,state=? where id=? ";

        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, accountManagement.getUsername(), accountManagement.getPassword(), accountManagement.getRealname(), accountManagement.getUsetype(), accountManagement.getAge(), accountManagement.getPhone(), accountManagement.getHome(), accountManagement.getPhoto(), accountManagement.getEducation(), accountManagement.getState(), accountManagement.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from teacher where id = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
