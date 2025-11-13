package com.example.myapplication.jiaowu.service;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.TeacherInfo;

import java.util.List;

public class TeacherInfoService {
    private JiaowuBaseDao base;
    private JiaowuServerDao base1;

    /**
     * 查询全部数据
     */
    public List<TeacherInfo> getList(String company, String t_name) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        // 根据状态执行不同的业务逻辑
        if (shujukuValue == 1) {
            // SQL Server 版本（老版兼容）
            String sql = "select * from teacherinfo where company = ? and t_name like '%' + ? + '%'";
            base1 = new JiaowuServerDao();
            List<TeacherInfo> list = base1.query(TeacherInfo.class, sql, company, t_name);
            return list;

        } else {
            // MySQL 版本（原内容不动）
            String sql = "select * from teacherinfo where company =? and t_name like '%' ? '%'";
            base = new JiaowuBaseDao();
            List<TeacherInfo> list = base.query(TeacherInfo.class, sql, company, t_name);
            return list;
        }
    }

    /**
     * 新增
     */
    public boolean insert(TeacherInfo teacherInfo) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        // 统一的插入SQL语句，两种数据库语法相同
        String sql = "insert into teacherinfo(t_name,sex,id_code,minzu,birthday,post,education,phone,rz_riqi,state,shebao,address,company) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        long result;
        if (shujukuValue == 1) {
            // SQL Server 版本
            base1 = new JiaowuServerDao();
            result = base1.executeOfId(sql, teacherInfo.getT_name(), teacherInfo.getSex(), teacherInfo.getId_code(),
                    teacherInfo.getMinzu(), teacherInfo.getBirthday(), teacherInfo.getPost(), teacherInfo.getEducation(),
                    teacherInfo.getPhone(), teacherInfo.getRz_riqi(), teacherInfo.getState(), teacherInfo.getShebao(),
                    teacherInfo.getAddress(), teacherInfo.getCompany());
        } else {
            // MySQL 版本
            base = new JiaowuBaseDao();
            result = base.executeOfId(sql, teacherInfo.getT_name(), teacherInfo.getSex(), teacherInfo.getId_code(),
                    teacherInfo.getMinzu(), teacherInfo.getBirthday(), teacherInfo.getPost(), teacherInfo.getEducation(),
                    teacherInfo.getPhone(), teacherInfo.getRz_riqi(), teacherInfo.getState(), teacherInfo.getShebao(),
                    teacherInfo.getAddress(), teacherInfo.getCompany());
        }
        return result > 0;
    }

    /**
     * 修改
     */
//    public boolean update(TeacherInfo teacherInfo) {
//        String sql = "update teacherinfo set t_name=?,sex=?,id_code=?,minzu=?,birthday=?,post=?,education=?,phone=?,rz_riqi=?,state=?,shebao=?,address=? where id=? ";
//        base = new JiaowuBaseDao();
//        boolean result = base.execute(sql, teacherInfo.getT_name(), teacherInfo.getSex(), teacherInfo.getId_code(), teacherInfo.getMinzu(), teacherInfo.getBirthday(), teacherInfo.getPost(), teacherInfo.getEducation(),teacherInfo.getPhone(),teacherInfo.getRz_riqi(),teacherInfo.getState(),teacherInfo.getShebao(),teacherInfo.getAddress(),teacherInfo.getId());
//        return result;
//    }

    public boolean update(TeacherInfo teacherInfo) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        String sql = "update teacherinfo set t_name=?,sex=?,id_code=?,minzu=?,birthday=?,post=?,education=?,phone=?,rz_riqi=?,state=?,shebao=?,address=? where id=?";

        boolean result;
        if (shujukuValue == 1) {
            // SQL Server 版本
            base1 = new JiaowuServerDao();
            result = base1.execute(sql, teacherInfo.getT_name(), teacherInfo.getSex(), teacherInfo.getId_code(),
                    teacherInfo.getMinzu(), teacherInfo.getBirthday(), teacherInfo.getPost(), teacherInfo.getEducation(),
                    teacherInfo.getPhone(), teacherInfo.getRz_riqi(), teacherInfo.getState(), teacherInfo.getShebao(),
                    teacherInfo.getAddress(), teacherInfo.getId());
        } else {
            // MySQL 版本
            base = new JiaowuBaseDao();
            result = base.execute(sql, teacherInfo.getT_name(), teacherInfo.getSex(), teacherInfo.getId_code(),
                    teacherInfo.getMinzu(), teacherInfo.getBirthday(), teacherInfo.getPost(), teacherInfo.getEducation(),
                    teacherInfo.getPhone(), teacherInfo.getRz_riqi(), teacherInfo.getState(), teacherInfo.getShebao(),
                    teacherInfo.getAddress(), teacherInfo.getId());
        }
        return result;
    }

    /**
     * 删除
     */
//    public boolean delete(int id) {
//        String sql = "delete from teacherinfo where ID = ?";
//        base = new JiaowuBaseDao();
//        return base.execute(sql, id);
//    }
    public boolean delete(int id) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        String sql = "delete from teacherinfo where ID = ?";

        boolean result;
        if (shujukuValue == 1) {
            // SQL Server 版本（老版兼容）
            base1 = new JiaowuServerDao();
            result = base1.execute(sql, id);
        } else {
            // MySQL 版本（原版不动）
            base = new JiaowuBaseDao();
            result = base.execute(sql, id);
        }
        return result;
    }

}
