package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianUsers;

import java.util.List;

public class YhMendianUsersService {
    private MendianDao base;

    /**
     * 查询全部员工数据
     */
    public List<YhMendianUsers> getList(String uname, String position,String account, String company) {
        String sql = "select * from users where company = ? and uname like '%' ? '%' and position like '%' ? '%' and account like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianUsers> list = base.query(YhMendianMemberinfo.class, sql, company, uname,position,account);
        return list;
    }

    /**
     * 新增员工
     */
    public boolean insertByUsers(YhMendianUsers yhMendianUsers) {
        String sql = "insert into users(company,position,uname,account,password) values(?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql,yhMendianUsers.getCompany(), yhMendianUsers.getPosition(), yhMendianUsers.getUname(), yhMendianUsers.getAccount(), yhMendianUsers.getPassword());
        return result > 0;
    }

    /**
     * 修改员工
     */
    public boolean updateByUsers(YhMendianUsers yhMendianUsers) {
        String sql = "update users set position=?,uname=?,account=?,password=? where id=? ";
        base = new MendianDao();
        boolean result = base.execute(sql,yhMendianUsers.getPosition(), yhMendianUsers.getUname(), yhMendianUsers.getAccount(), yhMendianUsers.getPassword(),yhMendianUsers.getId());
        return result;
    }

    /**
     * 删除员工
     */
    public boolean deleteByUsers(int id) {
        String sql = "delete from users where id = ?";
        base = new MendianDao();
        return base.execute(sql, id);
    }
}
