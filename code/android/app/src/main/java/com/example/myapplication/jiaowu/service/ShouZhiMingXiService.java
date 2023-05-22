package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.ShouZhiMingXi;
import com.example.myapplication.jiaowu.entity.Student;

import java.util.List;

public class ShouZhiMingXiService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<ShouZhiMingXi> getList(String start_date, String stop_date, String company) {
        String sql = "select * from income where rgdate >= ? and rgdate <= ? and Company= ?";
        base = new JiaowuBaseDao();
        List<ShouZhiMingXi> list = base.query(ShouZhiMingXi.class, sql, start_date,stop_date,company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(ShouZhiMingXi shouZhiMingXi) {
        String sql = "insert into income(rgdate,money,msort,mremark,paid,psort,premark,handle,Company) values(?,?,?,?,?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, shouZhiMingXi.getRgdate(), shouZhiMingXi.getMoney(), shouZhiMingXi.getMsort(), shouZhiMingXi.getMremark(), shouZhiMingXi.getPaid(), shouZhiMingXi.getPsort(), shouZhiMingXi.getPremark(),shouZhiMingXi.getHandle(),shouZhiMingXi.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(ShouZhiMingXi shouZhiMingXi) {
        String sql = "update income set rgdate=?,money=?,msort=?,mremark=?,paid=?,psort=?,premark=?,handle=? where ID=? ";
        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, shouZhiMingXi.getRgdate(), shouZhiMingXi.getMoney(), shouZhiMingXi.getMsort(), shouZhiMingXi.getMremark(), shouZhiMingXi.getPaid(), shouZhiMingXi.getPsort(), shouZhiMingXi.getPremark(),shouZhiMingXi.getHandle(),shouZhiMingXi.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from income where ID = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
