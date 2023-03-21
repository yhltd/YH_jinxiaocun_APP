package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Jisuan;

import java.util.List;

public class JisuanService {

    private FenquanDao base;

    public List<Jisuan> getList(String company, String this_column) {
        String sql = "select * from baitaoquanxian_jisuan where company=? and thiscolumn like '%'+?+'%'";
        base = new FenquanDao();
        List<Jisuan> list = base.query(Jisuan.class, sql, company,this_column);
        return list;
    }

    public boolean insert(Jisuan Jisuan) {
        String sql = "insert into baitaoquanxian_jisuan(thiscolumn,gongshi,company) values(?,?,?)";
        base = new FenquanDao();
        long result = base.executeOfId(sql, Jisuan.getThiscolumn(), Jisuan.getGongshi(), Jisuan.getCompany());
        return result > 0;
    }

    public boolean update(Jisuan Jisuan) {
        String sql = "update baitaoquanxian_jisuan set thiscolumn=?,gongshi=? where id = ?";
        base = new FenquanDao();
        return base.execute(sql, Jisuan.getThiscolumn(),Jisuan.getGongshi(),Jisuan.getId());
    }

    public boolean delete(int id) {
        String sql = "delete from baitaoquanxian_jisuan where id = ?";
        base = new FenquanDao();
        return base.execute(sql, id);
    }

}
