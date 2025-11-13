package com.example.myapplication.jiaowu.service;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.entity.TeacherInfo;
import com.example.myapplication.jiaowu.entity.TeacherSal;

import java.util.List;

public class TeacherSalService {
    private JiaowuBaseDao base;
    private JiaowuServerDao base1;

    /**
     * 查询全部数据
     */


        public List<TeacherSal> getList(String start_date,String end_date,String teacher_name, String company) {

            int shujukuValue = CacheManager.getInstance().getShujukuValue();

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本（老版兼容）
                String sql = "select teacher_name,course,keshi,jine,keshi*jine as gongzihesuan from keshi_detail where riqi >= ? and riqi <= ? and teacher_name like '%' + ? + '%' and Company= ? ";
                base1 = new JiaowuServerDao();
                List<TeacherSal> list = base1.query(TeacherSal.class, sql,start_date,end_date,teacher_name, company);
                return list;

            } else {
                // MySQL 版本
                String sql = "select teacher_name,course,keshi,jine,keshi*jine as gongzihesuan from keshi_detail where riqi >= ? and riqi <= ? and teacher_name like '%' ? '%' and Company= ? ";
                base = new JiaowuBaseDao();
                List<TeacherSal> list = base.query(TeacherSal.class, sql,start_date,end_date,teacher_name, company);
                return list;
            }
        }


}
