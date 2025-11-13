package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunZhengLiService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 查询全部数据
     */
//    public List<YhJinXiaoCunZhengLi> getList(String company, String name) {
//        String sql = "select * from yh_jinxiaocun_zhengli where gs_name = ? and name like '%' ? '%' ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunZhengLi> list = base.query(YhJinXiaoCunZhengLi.class, sql, company, name);
//        return list;
//    }
    public List<YhJinXiaoCunZhengLi> getList(String company, String name) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 使用 + 进行字符串拼接
                String sql = "select * from yh_jinxiaocun_zhengli_mssql where gs_name = ? and name like '%' + ? + '%'";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunZhengLi> list = base2.query(YhJinXiaoCunZhengLi.class, sql, company, name);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 使用 CONCAT 函数
                String sql = "select * from yh_jinxiaocun_zhengli where gs_name = ? and name like concat('%', ?, '%')";
                base = new JxcBaseDao();
                List<YhJinXiaoCunZhengLi> list = base.query(YhJinXiaoCunZhengLi.class, sql, company, name);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取整理列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 新增
     */
//    public boolean insert(YhJinXiaoCunZhengLi yhJinXiaoCunZhengLi) {
//        String sql = "insert into yh_jinxiaocun_zhengli(sp_dm,name,lei_bie,dan_wei,beizhu,gs_name) values(?,?,?,?,?,?)";
//
//        base = new JxcBaseDao();
//        long result = base.executeOfId(sql, yhJinXiaoCunZhengLi.getSpDm(), yhJinXiaoCunZhengLi.getName(), yhJinXiaoCunZhengLi.getLeiBie(), yhJinXiaoCunZhengLi.getDanWei(), yhJinXiaoCunZhengLi.getBeizhu(), yhJinXiaoCunZhengLi.getGsName());
//        return result > 0;
//    }
    public boolean insert(YhJinXiaoCunZhengLi yhJinXiaoCunZhengLi) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "insert into yh_jinxiaocun_zhengli_mssql (sp_dm,name,lei_bie,dan_wei,beizhu,gs_name) values(?,?,?,?,?,?)";
                base2 = new JxcServerDao();
                long result = base2.executeOfId(sql, yhJinXiaoCunZhengLi.getSpDm(), yhJinXiaoCunZhengLi.getName(), yhJinXiaoCunZhengLi.getLeiBie(), yhJinXiaoCunZhengLi.getDanWei(), yhJinXiaoCunZhengLi.getBeizhu(), yhJinXiaoCunZhengLi.getGsName());
                return result > 0;

            } else {
                // MySQL 版本
                String sql = "insert into yh_jinxiaocun_zhengli (sp_dm,name,lei_bie,dan_wei,beizhu,gs_name) values(?,?,?,?,?,?)";
                base = new JxcBaseDao();
                long result = base.executeOfId(sql, yhJinXiaoCunZhengLi.getSpDm(), yhJinXiaoCunZhengLi.getName(), yhJinXiaoCunZhengLi.getLeiBie(), yhJinXiaoCunZhengLi.getDanWei(), yhJinXiaoCunZhengLi.getBeizhu(), yhJinXiaoCunZhengLi.getGsName());
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "插入整理数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改
     */
//    public boolean update(YhJinXiaoCunZhengLi yhJinXiaoCunZhengLi) {
//        String sql = "update yh_jinxiaocun_zhengli set sp_dm=?,name=?,lei_bie=?,dan_wei=?,beizhu=? where id=? ";
//
//        base = new JxcBaseDao();
//        boolean result = base.execute(sql, yhJinXiaoCunZhengLi.getSpDm(), yhJinXiaoCunZhengLi.getName(), yhJinXiaoCunZhengLi.getLeiBie(), yhJinXiaoCunZhengLi.getDanWei(), yhJinXiaoCunZhengLi.getBeizhu(), yhJinXiaoCunZhengLi.getId());
//        return result;
//    }
    public boolean update(YhJinXiaoCunZhengLi yhJinXiaoCunZhengLi) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_zhengli_mssql set sp_dm=?,name=?,lei_bie=?,dan_wei=?,beizhu=? where id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunZhengLi.getSpDm(), yhJinXiaoCunZhengLi.getName(), yhJinXiaoCunZhengLi.getLeiBie(), yhJinXiaoCunZhengLi.getDanWei(), yhJinXiaoCunZhengLi.getBeizhu(), yhJinXiaoCunZhengLi.getId());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_zhengli set sp_dm=?,name=?,lei_bie=?,dan_wei=?,beizhu=? where id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunZhengLi.getSpDm(), yhJinXiaoCunZhengLi.getName(), yhJinXiaoCunZhengLi.getLeiBie(), yhJinXiaoCunZhengLi.getDanWei(), yhJinXiaoCunZhengLi.getBeizhu(), yhJinXiaoCunZhengLi.getId());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新整理数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除
     */
//    public boolean delete(int id) {
//        String sql = "delete from yh_jinxiaocun_zhengli where id = ?";
//        base = new JxcBaseDao();
//        return base.execute(sql, id);
//    }

    public boolean delete(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "delete from yh_jinxiaocun_zhengli_mssql where id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_zhengli where id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除整理数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
