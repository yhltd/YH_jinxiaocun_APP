package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunCangKu;


import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunCangKuService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 查询全部数据
     */
    public List<YhJinXiaoCunCangKu> getList(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 使用 + 进行字符串拼接
                String sql = "select * from yh_jinxiaocun_cangku_mssql where gongsi = ?  order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunCangKu> list = base2.query(YhJinXiaoCunCangKu.class, sql, company);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 使用 CONCAT 函数
                String sql = "select * from yh_jinxiaocun_cangku where gongsi = ?  order by id";
                base = new JxcBaseDao();
                List<YhJinXiaoCunCangKu> list = base.query(YhJinXiaoCunCangKu.class, sql, company);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取期初数列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 新增
     */
    public boolean insert(YhJinXiaoCunCangKu yhJinXiaoCunCangKu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "insert into yh_jinxiaocun_cangku_mssql (gongsi,cangku) values(?,?)";
                base2 = new JxcServerDao();
                long result = base2.executeOfId(sql,yhJinXiaoCunCangKu.getGs_name(),yhJinXiaoCunCangKu.getcangku());
                return result > 0;

            } else {
                // MySQL 版本
                String sql = "insert into yh_jinxiaocun_cangku (gongsi,cangku) values(?,?)";
                base = new JxcBaseDao();
                long result = base.executeOfId(sql, yhJinXiaoCunCangKu.getGs_name(),yhJinXiaoCunCangKu.getcangku());
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "插入期初数数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    /**
     * 修改
     */

    public boolean update(YhJinXiaoCunCangKu yhJinXiaoCunCangKu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
            // 添加ID检查
            Log.e("UpdateDebug", "更新操作，ID: " + yhJinXiaoCunCangKu.get_id());
            Log.e("UpdateDebug", "更新数据: " + yhJinXiaoCunCangKu.getcangku());

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_cangku_mssql set cangku=? where id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunCangKu.getcangku(), yhJinXiaoCunCangKu.get_id());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_cangku set cangku=? where id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunCangKu.getcangku(), yhJinXiaoCunCangKu.get_id());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新期初数数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除
     */


    public boolean delete(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "delete from yh_jinxiaocun_cangku_mssql where id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_cangku where id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除期初数数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

