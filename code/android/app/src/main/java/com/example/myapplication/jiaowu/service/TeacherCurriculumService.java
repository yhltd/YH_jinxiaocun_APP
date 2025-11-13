package com.example.myapplication.jiaowu.service;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Kaoqin;
import com.example.myapplication.jiaowu.entity.TeacherCurriculum;

import java.util.List;

public class TeacherCurriculumService {
    private JiaowuBaseDao base;
    private JiaowuServerDao base1;

    /**
     * 查询全部数据
     */
    public List<TeacherCurriculum> getList(String teacher,String course, String company) {


        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本（老版兼容）
            String sql = "select * from course where teacher like '%' + ? + '%' and course like '%' + ? + '%' and company = ?";
            base1 = new JiaowuServerDao();
            List<TeacherCurriculum> list = base1.query(TeacherCurriculum.class, sql,teacher,course,company);
            return list;
        } else {
            // MySQL 版本（原版不动）
            String sql = "select * from course where teacher like '%' ? '%' and course like '%' ? '%' and company = ?";
            base = new JiaowuBaseDao();
            List<TeacherCurriculum> list = base.query(TeacherCurriculum.class, sql,teacher,course,company);
            return list;
        }

    }

    /**
     * 新增
     */
    public boolean insert(TeacherCurriculum teacherCurriculum) {

        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本（老版兼容）
            String sql = "insert into course(teacher,course,riqi,xingqi,company) values(?,?,?,?,?)";
            base1 = new JiaowuServerDao();
            long result = base1.executeOfId(sql, teacherCurriculum.getTeacher(), teacherCurriculum.getCourse(), teacherCurriculum.getRiqi(), teacherCurriculum.getXingqi(), teacherCurriculum.getCompany());
            return result > 0;

        } else {
            // MySQL 版本（原版不动）
            String sql = "insert into course(teacher,course,riqi,xingqi,company) values(?,?,?,?,?)";
            base = new JiaowuBaseDao();
            long result = base.executeOfId(sql, teacherCurriculum.getTeacher(), teacherCurriculum.getCourse(), teacherCurriculum.getRiqi(), teacherCurriculum.getXingqi(), teacherCurriculum.getCompany());
            return result > 0;

        }


    }

    /**
     * 修改
     */
    public boolean update(TeacherCurriculum teacherCurriculum) {

        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本（老版兼容）
            String sql = "update course set teacher= ?,course= ?,riqi= ?,xingqi= ? where id= ? ";
            base1 = new JiaowuServerDao();
            boolean result = base1.execute(sql, teacherCurriculum.getTeacher(), teacherCurriculum.getCourse(), teacherCurriculum.getRiqi(), teacherCurriculum.getXingqi(), teacherCurriculum.getId());
            return result;

        } else {
            // MySQL 版本（原版不动）
            String sql = "update course set teacher= ?,course= ?,riqi= ?,xingqi= ? where id= ? ";
            base = new JiaowuBaseDao();
            boolean result = base.execute(sql, teacherCurriculum.getTeacher(), teacherCurriculum.getCourse(), teacherCurriculum.getRiqi(), teacherCurriculum.getXingqi(), teacherCurriculum.getId());
            return result;
        }

    }

    /**
     * 删除
     */
    public boolean delete(int id) {

        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本（老版兼容）
            String sql = "delete from course where id = ?";
            base1 = new JiaowuServerDao();
            return base1.execute(sql, id);

        } else {
            // MySQL 版本（原版不动）
            String sql = "delete from course where id = ?";
            base = new JiaowuBaseDao();
            return base.execute(sql, id);
        }

    }
}
