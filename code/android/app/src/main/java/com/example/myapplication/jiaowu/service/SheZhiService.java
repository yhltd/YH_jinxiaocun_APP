package com.example.myapplication.jiaowu.service;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;

import java.util.ArrayList;
import java.util.List;

public class SheZhiService {
    private JiaowuBaseDao base;
    private JiaowuServerDao base1;

    /**
     * 查询全部数据
     */
//    public List<SheZhi> getList(String company) {
//        String sql = "select * from shezhi where Company = ?";
//        base = new JiaowuBaseDao();
//        List<SheZhi> list = base.query(SheZhi.class, sql, company);
//        return list;
//    }
    public List<SheZhi> getList(String company) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "select * from shezhi where Company = ?";
            base1 = new JiaowuServerDao();
            List<SheZhi> list = base1.query(SheZhi.class, sql, company);
            return list;
        } else {
            // MySQL 版本
            String sql = "select * from shezhi where Company = ?";
            base = new JiaowuBaseDao();
            List<SheZhi> list = base.query(SheZhi.class, sql, company);
            return list;
        }
    }

//    public List<SheZhi> getjffs(String company) {
//        String sql = "select distinct paiment from shezhi where Company = ?";
//        base = new JiaowuBaseDao();
//        List<SheZhi> list = base.query(SheZhi.class, sql, company);
//        return list;
//    }

    public List<SheZhi> getjffs(String company) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "select distinct paiment from shezhi where Company = ?";
            base1 = new JiaowuServerDao();
            List<SheZhi> list = base1.query(SheZhi.class, sql, company);
            return list;
        } else {
            // MySQL 版本
            String sql = "select distinct paiment from shezhi where Company = ?";
            base = new JiaowuBaseDao();
            List<SheZhi> list = base.query(SheZhi.class, sql, company);
            return list;
        }
    }

    /**
     * 新增
     */
//    public boolean insert(SheZhi sheZhi) {
//        String sql = "insert into shezhi(course,teacher,type,paiment,msort,psort,Company) values(?,?,?,?,?,?,?)";
//
//        base = new JiaowuBaseDao();
//        long result = base.executeOfId(sql, sheZhi.getCourse(), sheZhi.getTeacher(), sheZhi.getType(), sheZhi.getPaiment(), sheZhi.getMsort(), sheZhi.getPsort(), sheZhi.getCompany());
//        return result > 0;
//    }
    public boolean insert(SheZhi sheZhi) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "insert into shezhi(course,teacher,type,paiment,msort,psort,Company) values(?,?,?,?,?,?,?)";
            base1 = new JiaowuServerDao();
            long result = base1.executeOfId(sql, sheZhi.getCourse(), sheZhi.getTeacher(), sheZhi.getType(), sheZhi.getPaiment(), sheZhi.getMsort(), sheZhi.getPsort(), sheZhi.getCompany());
            return result > 0;
        } else {
            // MySQL 版本
            String sql = "insert into shezhi(course,teacher,type,paiment,msort,psort,Company) values(?,?,?,?,?,?,?)";
            base = new JiaowuBaseDao();
            long result = base.executeOfId(sql, sheZhi.getCourse(), sheZhi.getTeacher(), sheZhi.getType(), sheZhi.getPaiment(), sheZhi.getMsort(), sheZhi.getPsort(), sheZhi.getCompany());
            return result > 0;
        }
    }

    /**
     * 修改
     */
//    public boolean update(SheZhi sheZhi) {
//        String sql = "update shezhi set course=?,teacher=?,type=?,paiment=?,msort=?,psort=? where id=? ";
//
//        base = new JiaowuBaseDao();
//        boolean result = base.execute(sql, sheZhi.getCourse(), sheZhi.getTeacher(), sheZhi.getType(), sheZhi.getPaiment(), sheZhi.getMsort(), sheZhi.getPsort(), sheZhi.getId());
//        return result;
//    }

    public boolean update(SheZhi sheZhi) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "update shezhi set course=?,teacher=?,type=?,paiment=?,msort=?,psort=? where id=?";
            base1 = new JiaowuServerDao();
            boolean result = base1.execute(sql, sheZhi.getCourse(), sheZhi.getTeacher(), sheZhi.getType(), sheZhi.getPaiment(), sheZhi.getMsort(), sheZhi.getPsort(), sheZhi.getId());
            return result;
        } else {
            // MySQL 版本
            String sql = "update shezhi set course=?,teacher=?,type=?,paiment=?,msort=?,psort=? where id=?";
            base = new JiaowuBaseDao();
            boolean result = base.execute(sql, sheZhi.getCourse(), sheZhi.getTeacher(), sheZhi.getType(), sheZhi.getPaiment(), sheZhi.getMsort(), sheZhi.getPsort(), sheZhi.getId());
            return result;
        }
    }

    /**
     * 删除
     */
//    public boolean delete(int id) {
//        String sql = "delete from shezhi where id = ?";
//        base = new JiaowuBaseDao();
//        return base.execute(sql, id);
//    }
    public boolean delete(int id) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "delete from shezhi where id = ?";
            base1 = new JiaowuServerDao();
            return base1.execute(sql, id);
        } else {
            // MySQL 版本
            String sql = "delete from shezhi where id = ?";
            base = new JiaowuBaseDao();
            return base.execute(sql, id);
        }
    }


}
