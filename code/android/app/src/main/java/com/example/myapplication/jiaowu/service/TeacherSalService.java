package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.TeacherInfo;
import com.example.myapplication.jiaowu.entity.TeacherSal;

import java.util.List;

public class TeacherSalService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<TeacherSal> getList(String start_date,String end_date,String teacher_name, String company) {
        String sql = "select teacher_name,course,keshi,jine,keshi*jine as gongzihesuan from keshi_detail where riqi >= ? and riqi <= ? and teacher_name like '%' ? '%' and Company= ? ";
        base = new JiaowuBaseDao();
        List<TeacherSal> list = base.query(TeacherSal.class, sql,start_date,end_date,teacher_name, company);
        return list;
    }
}
