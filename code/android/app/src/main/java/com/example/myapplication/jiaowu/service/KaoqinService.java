package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Kaoqin;

import java.util.List;

public class KaoqinService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<Kaoqin> getList(String s_name, String company) {
        String sql = "select * from kaoqin where company = ? and s_name like ?";
        base = new JiaowuBaseDao();
        List<Kaoqin> list = base.query(AccountManagement.class, sql,s_name,company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(Kaoqin kaoqin) {
        String sql = "insert into kaoqin(s_name,nian,yue,ri1,ri2,ri3,ri4,ri5,ri6,ri7,ri8,ri9,ri10,ri11,ri12,ri13,ri14,ri15,ri16,ri17,ri18,ri19,ri20,ri21,ri22,ri23,ri24,ri25,ri26,ri27,ri28,ri29,ri30,ri31,company) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, kaoqin.getS_name(), kaoqin.getNian(), kaoqin.getYue(), kaoqin.getRi1(), kaoqin.getRi2(), kaoqin.getRi3(), kaoqin.getRi4(), kaoqin.getRi5(), kaoqin.getRi6(), kaoqin.getRi7(), kaoqin.getRi8(), kaoqin.getRi9(), kaoqin.getRi10(), kaoqin.getRi11(), kaoqin.getRi12(), kaoqin.getRi13(), kaoqin.getRi14(), kaoqin.getRi15(), kaoqin.getRi16(), kaoqin.getRi17(), kaoqin.getRi18(), kaoqin.getRi19(), kaoqin.getRi20(), kaoqin.getRi21(), kaoqin.getRi22(), kaoqin.getRi23(), kaoqin.getRi24(), kaoqin.getRi25(), kaoqin.getRi26(), kaoqin.getRi27(), kaoqin.getRi28(), kaoqin.getRi29(), kaoqin.getRi30(), kaoqin.getRi31(), kaoqin.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(Kaoqin kaoqin) {
        String sql = "update kaoqin set  s_name = ? ,nian = ? ,yue = ? ,ri1 = ? ,ri2 = ? ,ri3 = ? ,ri4 = ? ,ri5 = ? ,ri6 = ? ,ri7 = ? ,ri8 = ? ,ri9 = ? ,ri10 = ? ,ri11 = ? ,ri12 = ? ,ri13 = ? ,ri14 = ? ,ri15 = ? ,ri16 = ? ,ri17 = ? ,ri18 = ? ,ri19 = ? ,ri20 = ? ,ri21 = ? ,ri22 = ? ,ri23 = ? ,ri24 = ? ,ri25 = ? ,ri26 = ? ,ri27 = ? ,ri28 = ? ,ri29 = ? ,ri30 = ? ,ri31 = ?  where id= ? ";

        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, kaoqin.getS_name(), kaoqin.getNian(), kaoqin.getYue(), kaoqin.getRi1(), kaoqin.getRi2(), kaoqin.getRi3(), kaoqin.getRi4(), kaoqin.getRi5(), kaoqin.getRi6(), kaoqin.getRi7(), kaoqin.getRi8(), kaoqin.getRi9(), kaoqin.getRi10(), kaoqin.getRi11(), kaoqin.getRi12(), kaoqin.getRi13(), kaoqin.getRi14(), kaoqin.getRi15(), kaoqin.getRi16(), kaoqin.getRi17(), kaoqin.getRi18(), kaoqin.getRi19(), kaoqin.getRi20(), kaoqin.getRi21(), kaoqin.getRi22(), kaoqin.getRi23(), kaoqin.getRi24(), kaoqin.getRi25(), kaoqin.getRi26(), kaoqin.getRi27(), kaoqin.getRi28(), kaoqin.getRi29(), kaoqin.getRi30(), kaoqin.getRi31(), kaoqin.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from kaoqin where id = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
