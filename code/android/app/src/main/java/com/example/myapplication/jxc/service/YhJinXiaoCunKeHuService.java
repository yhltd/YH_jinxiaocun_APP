package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunKeHuService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 查询全部客户数据
     */
//    public List<YhJinXiaoCunKeHu> getListByKehu(String company, String beizhu) {
//        String sql = "select * from yh_jinxiaocun_chuhuofang where gongsi = ? and beizhu like '%' ? '%' ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunKeHu> list = base.query(YhJinXiaoCunKeHu.class, sql, company, beizhu);
//        return list;
//    }
    public List<YhJinXiaoCunKeHu> getListByKehu(String company, String beizhu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 使用 + 进行字符串拼接
                String sql = "select * from yh_jinxiaocun_chuhuofang_mssql where gongsi = ? and beizhu like '%' + ? + '%'";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunKeHu> list = base2.query(YhJinXiaoCunKeHu.class, sql, company, beizhu);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 使用 CONCAT 函数
                String sql = "select * from yh_jinxiaocun_chuhuofang where gongsi = ? and beizhu like concat('%', ?, '%')";
                base = new JxcBaseDao();
                List<YhJinXiaoCunKeHu> list = base.query(YhJinXiaoCunKeHu.class, sql, company, beizhu);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "根据客户获取列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 新增客户
     */
//    public boolean insertByKehu(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
//        String sql = "insert into yh_jinxiaocun_chuhuofang (beizhu,lianxidizhi,lianxifangshi,gongsi) values(?,?,?,?)";
//        base = new JxcBaseDao();
//        long result = base.executeOfId(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.getGongsi());
//        return result > 0;
//    }

    public boolean insertByKehu(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "insert into yh_jinxiaocun_chuhuofang_mssql (beizhu,lianxidizhi,lianxifangshi,gongsi) values(?,?,?,?)";
                base2 = new JxcServerDao();
                long result = base2.executeOfId(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.getGongsi());
                return result > 0;

            } else {
                // MySQL 版本
                String sql = "insert into yh_jinxiaocun_chuhuofang (beizhu,lianxidizhi,lianxifangshi,gongsi) values(?,?,?,?)";
                base = new JxcBaseDao();
                long result = base.executeOfId(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.getGongsi());
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "插入客户数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改客户
     */
//    public boolean updateByKehu(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
//        String sql = "update yh_jinxiaocun_chuhuofang set beizhu=?,lianxidizhi=?,lianxifangshi=? where _id=? ";
//        base = new JxcBaseDao();
//        boolean result = base.execute(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.get_id());
//        return result;
//    }
    public boolean updateByKehu(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_chuhuofang_mssql set beizhu=?,lianxidizhi=?,lianxifangshi=? where _id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.get_id());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_chuhuofang set beizhu=?,lianxidizhi=?,lianxifangshi=? where _id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.get_id());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新客户数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除客户
     */
//    public boolean deleteByKehu(int id) {
//        String sql = "delete from yh_jinxiaocun_chuhuofang where _id = ?";
//        base = new JxcBaseDao();
//        return base.execute(sql, id);
//    }

    public boolean deleteByKehu(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "delete from yh_jinxiaocun_chuhuofang_mssql where _id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_chuhuofang where _id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除客户数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询全部供应商数据
     */
//    public List<YhJinXiaoCunKeHu> getListByGys(String company, String beizhu) {
//        String sql = "select * from yh_jinxiaocun_jinhuofang where gongsi = ? and beizhu like '%' ? '%' ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunKeHu> list = base.query(YhJinXiaoCunKeHu.class, sql, company, beizhu);
//        return list;
//    }

    public List<YhJinXiaoCunKeHu> getListByGys(String company, String beizhu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 使用 + 进行字符串拼接
                String sql = "select * from yh_jinxiaocun_jinhuofang_mssql where gongsi = ? and beizhu like '%' + ? + '%'";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunKeHu> list = base2.query(YhJinXiaoCunKeHu.class, sql, company, beizhu);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 使用 CONCAT 函数
                String sql = "select * from yh_jinxiaocun_jinhuofang where gongsi = ? and beizhu like concat('%', ?, '%')";
                base = new JxcBaseDao();
                List<YhJinXiaoCunKeHu> list = base.query(YhJinXiaoCunKeHu.class, sql, company, beizhu);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "根据供应商获取列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 新增供应商
     */
//    public boolean insertByGys(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
//        String sql = "insert into yh_jinxiaocun_jinhuofang (beizhu,lianxidizhi,lianxifangshi,gongsi) values(?,?,?,?)";
//        base = new JxcBaseDao();
//        long result = base.executeOfId(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.getGongsi());
//        return result > 0;
//    }

    public boolean insertByGys(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "insert into yh_jinxiaocun_jinhuofang_mssql (beizhu,lianxidizhi,lianxifangshi,gongsi) values(?,?,?,?)";
                base2 = new JxcServerDao();
                long result = base2.executeOfId(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.getGongsi());
                return result > 0;

            } else {
                // MySQL 版本
                String sql = "insert into yh_jinxiaocun_jinhuofang (beizhu,lianxidizhi,lianxifangshi,gongsi) values(?,?,?,?)";
                base = new JxcBaseDao();
                long result = base.executeOfId(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.getGongsi());
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "插入供应商数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改供应商
     */
//    public boolean updateByGys(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
//        String sql = "update yh_jinxiaocun_jinhuofang set beizhu=?,lianxidizhi=?,lianxifangshi=? where _id=? ";
//        base = new JxcBaseDao();
//        boolean result = base.execute(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.get_id());
//        return result;
//    }

    public boolean updateByGys(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_jinhuofang_mssql set beizhu=?,lianxidizhi=?,lianxifangshi=? where _id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.get_id());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_jinhuofang set beizhu=?,lianxidizhi=?,lianxifangshi=? where _id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.get_id());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新供应商数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除供应商
     */
//    public boolean deleteByGys(int id) {
//        String sql = "delete from yh_jinxiaocun_jinhuofang where _id = ?";
//        base = new JxcBaseDao();
//        return base.execute(sql, id);
//    }
    public boolean deleteByGys(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "delete from yh_jinxiaocun_jinhuofang_mssql where _id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_jinhuofang where _id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除供应商数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
