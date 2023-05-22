package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.TeacherInfo;

import java.util.List;

public class TeacherInfoService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<TeacherInfo> getList(String company, String t_name) {
        String sql = "select * from teacherinfo where company =? and t_name like '%' ? '%'";
        base = new JiaowuBaseDao();
        List<TeacherInfo> list = base.query(TeacherInfo.class, sql, company,t_name);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(TeacherInfo teacherInfo) {
        String sql = "insert into teacherinfo(t_name,sex,id_code,minzu,birthday,post,education,phone,rz_riqi,state,shebao,address,company) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, teacherInfo.getT_name(), teacherInfo.getSex(), teacherInfo.getId_code(), teacherInfo.getMinzu(), teacherInfo.getBirthday(), teacherInfo.getPost(), teacherInfo.getEducation(),teacherInfo.getPhone(),teacherInfo.getRz_riqi(),teacherInfo.getState(),teacherInfo.getShebao(),teacherInfo.getAddress(),teacherInfo.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(TeacherInfo teacherInfo) {
        String sql = "update teacherinfo set t_name=?,sex=?,id_code=?,minzu=?,birthday=?,post=?,education=?,phone=?,rz_riqi=?,state=?,shebao=?,address=? where id=? ";
        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, teacherInfo.getT_name(), teacherInfo.getSex(), teacherInfo.getId_code(), teacherInfo.getMinzu(), teacherInfo.getBirthday(), teacherInfo.getPost(), teacherInfo.getEducation(),teacherInfo.getPhone(),teacherInfo.getRz_riqi(),teacherInfo.getState(),teacherInfo.getShebao(),teacherInfo.getAddress(),teacherInfo.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from teacherinfo where ID = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
