package com.example.myapplication.mendian.service;

import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;
import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianKeHu;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;

import java.util.List;

public class YhMendianMemberinfoService {
    private MendianDao base;

    /**
     * 查询全部客户数据
     */
    public List<YhMendianMemberinfo> getList(String name, String phone, String company) {
        String sql = "select * from member_info where company = ? and name like '%' ? '%' and phone like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianMemberinfo> list = base.query(YhMendianMemberinfo.class, sql, company, name,phone);
        return list;
    }

    /**
     * 查询全部客户数据
     */
    public List<YhMendianMemberinfo> getListThree(String company, String this_where) {
        String sql = "select * from member_info where company = ? and (phone like '%' ? '%' or username like '%' ? '%' or `name` like '%' ? '%') ";
        base = new MendianDao();
        List<YhMendianMemberinfo> list = base.query(YhMendianMemberinfo.class, sql, company, this_where,this_where,this_where);
        return list;
    }

    /**
     * 新增客户
     */
    public boolean insertByMember(YhMendianMemberinfo yhMendianMemberinfo) {
        String sql = "insert into member_info (username,password,name,gender,state,phone,birthday,company,points) values(?,?,?,?,?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql, yhMendianMemberinfo.getUsername(), yhMendianMemberinfo.getPassword(), yhMendianMemberinfo.getName(), yhMendianMemberinfo.getGender(), yhMendianMemberinfo.getState(), yhMendianMemberinfo.getPhone(), yhMendianMemberinfo.getBirthday(),yhMendianMemberinfo.getCompany(), yhMendianMemberinfo.getPoints());
        return result > 0;
    }

    /**
     * 修改客户
     */
    public boolean updateByMember(YhMendianMemberinfo yhMendianMemberinfo) {
        String sql = "update member_info set username=?,password=?,name=?,gender=?,state=?,phone=?,birthday=?,points=? where id=? ";
        base = new MendianDao();
        boolean result = base.execute(sql,yhMendianMemberinfo.getUsername(), yhMendianMemberinfo.getPassword(), yhMendianMemberinfo.getName(), yhMendianMemberinfo.getGender(), yhMendianMemberinfo.getState(), yhMendianMemberinfo.getPhone(), yhMendianMemberinfo.getBirthday(), yhMendianMemberinfo.getPoints(), yhMendianMemberinfo.getId());
        return result;
    }

    /**
     * 删除客户
     */
    public boolean deleteByMember(int id) {
        String sql = "delete from member_info where id = ?";
        base = new MendianDao();
        return base.execute(sql, id);
    }

}
