package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.KeShiDetail;
import com.example.myapplication.jiaowu.entity.TeacherInfo;

import java.util.List;

public class KeShiDetailService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<KeShiDetail> getList(String company, String teacher_name,String course,String start_date,String stop_date) {
        String sql = "select * from keshi_detail where teacher_name like '%' ? '%' and course like '%' ? '%' and riqi >= ? and riqi <=? and Company= ?";
        base = new JiaowuBaseDao();
        List<KeShiDetail> list = base.query(KeShiDetail.class, sql, teacher_name,course,start_date,stop_date,company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(KeShiDetail keShiDetail) {
        String sql = "insert into keshi_detail(riqi,student_name,course,keshi,teacher_name,jine,Company) values(?,?,?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, keShiDetail.getRiqi(), keShiDetail.getStudent_name(), keShiDetail.getCourse(), keShiDetail.getKeshi(), keShiDetail.getTeacher_name(), keShiDetail.getJine(), keShiDetail.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(KeShiDetail keShiDetail) {
        String sql = "update keshi_detail set riqi=?,student_name=?,course=?,keshi=?,teacher_name=?,jine=? where id=? ";
        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, keShiDetail.getRiqi(), keShiDetail.getStudent_name(), keShiDetail.getCourse(), keShiDetail.getKeshi(), keShiDetail.getTeacher_name(), keShiDetail.getJine(),keShiDetail.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from keshi_detail where id = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
