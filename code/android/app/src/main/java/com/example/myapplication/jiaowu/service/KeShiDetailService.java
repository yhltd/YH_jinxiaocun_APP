package com.example.myapplication.jiaowu.service;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.KeShiDetail;
import com.example.myapplication.jiaowu.entity.TeacherInfo;

import java.util.List;

public class KeShiDetailService {
    private JiaowuBaseDao base;
    private JiaowuServerDao base1;

    /**
     * 查询全部数据
     */
//    public List<KeShiDetail> getList(String company, String teacher_name,String course,String start_date,String stop_date) {
//        String sql = "select * from keshi_detail where teacher_name like '%' ? '%' and course like '%' ? '%' and riqi >= ? and riqi <=? and Company= ?";
//        base = new JiaowuBaseDao();
//        List<KeShiDetail> list = base.query(KeShiDetail.class, sql, teacher_name,course,start_date,stop_date,company);
//        return list;
//    }

    public List<KeShiDetail> getList(String company, String teacher_name, String course, String start_date, String stop_date) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "select * from keshi_detail where teacher_name like '%' + ? + '%' and course like '%' + ? + '%' and riqi >= ? and riqi <= ? and Company= ?";
            base1 = new JiaowuServerDao();
            List<KeShiDetail> list = base1.query(KeShiDetail.class, sql, teacher_name, course, start_date, stop_date, company);
            return list;
        } else {
            // MySQL 版本
            String sql = "select * from keshi_detail where teacher_name like '%' ? '%' and course like '%' ? '%' and riqi >= ? and riqi <= ? and Company= ?";
            base = new JiaowuBaseDao();
            List<KeShiDetail> list = base.query(KeShiDetail.class, sql, teacher_name, course, start_date, stop_date, company);
            return list;
        }
    }

    /**
     * 新增
     */
//    public boolean insert(KeShiDetail keShiDetail) {
//        String sql = "insert into keshi_detail(riqi,student_name,course,keshi,teacher_name,jine,Company) values(?,?,?,?,?,?,?)";
//        base = new JiaowuBaseDao();
//        long result = base.executeOfId(sql, keShiDetail.getRiqi(), keShiDetail.getStudent_name(), keShiDetail.getCourse(), keShiDetail.getKeshi(), keShiDetail.getTeacher_name(), keShiDetail.getJine(), keShiDetail.getCompany());
//        return result > 0;
//    }
    public boolean insert(KeShiDetail keShiDetail) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "insert into keshi_detail(riqi,student_name,course,keshi,teacher_name,jine,Company) values(?,?,?,?,?,?,?)";
            base1 = new JiaowuServerDao();
            long result = base1.executeOfId(sql, keShiDetail.getRiqi(), keShiDetail.getStudent_name(), keShiDetail.getCourse(), keShiDetail.getKeshi(), keShiDetail.getTeacher_name(), keShiDetail.getJine(), keShiDetail.getCompany());
            return result > 0;
        } else {
            // MySQL 版本
            String sql = "insert into keshi_detail(riqi,student_name,course,keshi,teacher_name,jine,Company) values(?,?,?,?,?,?,?)";
            base = new JiaowuBaseDao();
            long result = base.executeOfId(sql, keShiDetail.getRiqi(), keShiDetail.getStudent_name(), keShiDetail.getCourse(), keShiDetail.getKeshi(), keShiDetail.getTeacher_name(), keShiDetail.getJine(), keShiDetail.getCompany());
            return result > 0;
        }
    }

    /**
     * 修改
     */
//    public boolean update(KeShiDetail keShiDetail) {
//        String sql = "update keshi_detail set riqi=?,student_name=?,course=?,keshi=?,teacher_name=?,jine=? where id=? ";
//        base = new JiaowuBaseDao();
//        boolean result = base.execute(sql, keShiDetail.getRiqi(), keShiDetail.getStudent_name(), keShiDetail.getCourse(), keShiDetail.getKeshi(), keShiDetail.getTeacher_name(), keShiDetail.getJine(),keShiDetail.getId());
//        return result;
//    }
    public boolean update(KeShiDetail keShiDetail) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "update keshi_detail set riqi=?,student_name=?,course=?,keshi=?,teacher_name=?,jine=? where id=?";
            base1 = new JiaowuServerDao();
            boolean result = base1.execute(sql, keShiDetail.getRiqi(), keShiDetail.getStudent_name(), keShiDetail.getCourse(), keShiDetail.getKeshi(), keShiDetail.getTeacher_name(), keShiDetail.getJine(), keShiDetail.getId());
            return result;
        } else {
            // MySQL 版本
            String sql = "update keshi_detail set riqi=?,student_name=?,course=?,keshi=?,teacher_name=?,jine=? where id=?";
            base = new JiaowuBaseDao();
            boolean result = base.execute(sql, keShiDetail.getRiqi(), keShiDetail.getStudent_name(), keShiDetail.getCourse(), keShiDetail.getKeshi(), keShiDetail.getTeacher_name(), keShiDetail.getJine(), keShiDetail.getId());
            return result;
        }
    }

    /**
     * 删除
     */
//    public boolean delete(int id) {
//        String sql = "delete from keshi_detail where id = ?";
//        base = new JiaowuBaseDao();
//        return base.execute(sql, id);
//    }
    public boolean delete(int id) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "delete from keshi_detail where id = ?";
            base1 = new JiaowuServerDao();
            return base1.execute(sql, id);
        } else {
            // MySQL 版本
            String sql = "delete from keshi_detail where id = ?";
            base = new JiaowuBaseDao();
            return base.execute(sql, id);
        }
    }
}
