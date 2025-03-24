package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Kaoqin;
import com.example.myapplication.jiaowu.entity.TeacherCurriculum;

import java.util.List;

public class TeacherCurriculumService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<TeacherCurriculum> getList(String teacher,String course, String company) {
        String sql = "select * from course where teacher like '%' ? '%' and course like '%' ? '%' and company = ?";
        base = new JiaowuBaseDao();
        List<TeacherCurriculum> list = base.query(TeacherCurriculum.class, sql,teacher,course,company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(TeacherCurriculum teacherCurriculum) {
        String sql = "insert into course(teacher,course,riqi,xingqi,company) values(?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, teacherCurriculum.getTeacher(), teacherCurriculum.getCourse(), teacherCurriculum.getRiqi(), teacherCurriculum.getXingqi(), teacherCurriculum.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(TeacherCurriculum teacherCurriculum) {
        String sql = "update course set teacher= ?,course= ?,riqi= ?,xingqi= ? where id= ? ";

        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, teacherCurriculum.getTeacher(), teacherCurriculum.getCourse(), teacherCurriculum.getRiqi(), teacherCurriculum.getXingqi(), teacherCurriculum.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from course where id = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
