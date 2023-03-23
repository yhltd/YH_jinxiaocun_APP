package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.JiaoShiKeShi;

import java.util.List;

public class JiaoShiKeShiService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<JiaoShiKeShi> getList(String teacher_name, String start_date, String stop_date , String company) {
        String sql = "select teacher_name,course,keshi,jine,keshi*jine as gongzihesuan from keshi_detail where teacher_name like '%' + ? + '%'and riqi >= ? and riqi <= ? and Company= ?";
        base = new JiaowuBaseDao();
        List<JiaoShiKeShi> list = base.query(JiaoShiKeShi.class, sql,teacher_name,start_date, stop_date,company);
        return list;
    }
}
