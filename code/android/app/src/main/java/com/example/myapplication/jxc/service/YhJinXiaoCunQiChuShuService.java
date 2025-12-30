package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunQiChuShu;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunQiChuShuService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 查询全部数据
     */
//    public List<YhJinXiaoCunQiChuShu> getList(String company, String cpname) {
//        String sql = "select * from yh_jinxiaocun_qichushu where gs_name = ? and cpname like '%' ? '%' order by _id";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunQiChuShu> list = base.query(YhJinXiaoCunQiChuShu.class, sql, company, cpname);
//        return list;
//    }
    public List<YhJinXiaoCunQiChuShu> getList(String company, String cpname) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 使用 + 进行字符串拼接
                String sql = "select * from yh_jinxiaocun_qichushu_mssql where gs_name = ? and cpname like '%' + ? + '%' order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunQiChuShu> list = base2.query(YhJinXiaoCunQiChuShu.class, sql, company, cpname);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 使用 CONCAT 函数
                String sql = "select * from yh_jinxiaocun_qichushu where gs_name = ? and cpname like concat('%', ?, '%') order by _id";
                base = new JxcBaseDao();
                List<YhJinXiaoCunQiChuShu> list = base.query(YhJinXiaoCunQiChuShu.class, sql, company, cpname);
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
//    public boolean insert(YhJinXiaoCunQiChuShu yhJinXiaoCunQiChuShu) {
//        String sql = "insert into yh_jinxiaocun_qichushu(cpname,cpid,cplb,cpsj,cpsl,gs_name) values(?,?,?,?,?,?)";
//
//        base = new JxcBaseDao();
//        long result = base.executeOfId(sql, yhJinXiaoCunQiChuShu.getCpname(), yhJinXiaoCunQiChuShu.getCpid(), yhJinXiaoCunQiChuShu.getCplb(), yhJinXiaoCunQiChuShu.getCpsj(), yhJinXiaoCunQiChuShu.getCpsl(),yhJinXiaoCunQiChuShu.getGs_name());
//        return result > 0;
//    }
    public boolean insert(YhJinXiaoCunQiChuShu yhJinXiaoCunQiChuShu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "insert into yh_jinxiaocun_qichushu_mssql (cpname,cpid,cplb,cpsj,cpsl,gs_name,cangku) values(?,?,?,?,?,?,?)";
                base2 = new JxcServerDao();
                long result = base2.executeOfId(sql, yhJinXiaoCunQiChuShu.getCpname(), yhJinXiaoCunQiChuShu.getCpid(), yhJinXiaoCunQiChuShu.getCplb(), yhJinXiaoCunQiChuShu.getCpsj(), yhJinXiaoCunQiChuShu.getCpsl(), yhJinXiaoCunQiChuShu.getGs_name(),yhJinXiaoCunQiChuShu.getcangku());
                return result > 0;

            } else {
                // MySQL 版本
                String sql = "insert into yh_jinxiaocun_qichushu (cpname,cpid,cplb,cpsj,cpsl,gs_name,cangku) values(?,?,?,?,?,?,?)";
                base = new JxcBaseDao();
                long result = base.executeOfId(sql, yhJinXiaoCunQiChuShu.getCpname(), yhJinXiaoCunQiChuShu.getCpid(), yhJinXiaoCunQiChuShu.getCplb(), yhJinXiaoCunQiChuShu.getCpsj(), yhJinXiaoCunQiChuShu.getCpsl(), yhJinXiaoCunQiChuShu.getGs_name(),yhJinXiaoCunQiChuShu.getcangku());
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
//    public boolean update(YhJinXiaoCunQiChuShu yhJinXiaoCunQiChuShu) {
//        String sql = "update yh_jinxiaocun_qichushu set cpname=?,cpid=?,cplb=?,cpsj=?,cpsl=? where _id=? ";
//
//        base = new JxcBaseDao();
//        boolean result = base.execute(sql, yhJinXiaoCunQiChuShu.getCpname(), yhJinXiaoCunQiChuShu.getCpid(), yhJinXiaoCunQiChuShu.getCplb(), yhJinXiaoCunQiChuShu.getCpsj(), yhJinXiaoCunQiChuShu.getCpsl(), yhJinXiaoCunQiChuShu.get_id());
//        return result;
//    }
    public boolean update(YhJinXiaoCunQiChuShu yhJinXiaoCunQiChuShu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_qichushu_mssql set cpname=?,cpid=?,cplb=?,cpsj=?,cpsl=?,cangku=? where _id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunQiChuShu.getCpname(), yhJinXiaoCunQiChuShu.getCpid(), yhJinXiaoCunQiChuShu.getCplb(), yhJinXiaoCunQiChuShu.getCpsj(), yhJinXiaoCunQiChuShu.getCpsl(),yhJinXiaoCunQiChuShu.getcangku(), yhJinXiaoCunQiChuShu.get_id());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_qichushu set cpname=?,cpid=?,cplb=?,cpsj=?,cpsl=?,cangku=? where _id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunQiChuShu.getCpname(), yhJinXiaoCunQiChuShu.getCpid(), yhJinXiaoCunQiChuShu.getCplb(), yhJinXiaoCunQiChuShu.getCpsj(), yhJinXiaoCunQiChuShu.getCpsl(),yhJinXiaoCunQiChuShu.getcangku(), yhJinXiaoCunQiChuShu.get_id());
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
//    public boolean delete(int id) {
//        String sql = "delete from yh_jinxiaocun_qichushu where _id = ?";
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
                String sql = "delete from yh_jinxiaocun_qichushu_mssql where _id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_qichushu where _id = ?";
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
