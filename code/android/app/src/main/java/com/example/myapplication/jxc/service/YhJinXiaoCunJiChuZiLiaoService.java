package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunJiChuZiLiaoService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 查询全部数据
     */
//    public List<YhJinXiaoCunJiChuZiLiao> getList(String company, String cpname) {
//        String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and name like '%' ? '%' order by id";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
//        return list;
//    }
    public List<YhJinXiaoCunJiChuZiLiao> getList(String company, String cpname) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 使用 + 进行字符串拼接
                String sql = "select * from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? and name like '%' + ? + '%' order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 使用 CONCAT 函数
                String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and name like concat('%', ?, '%') order by id";
                base = new JxcBaseDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取基础资料列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 商品代码下拉
     */
//    public List<String> getCpid(String company) {
//        String sql = "select sp_dm from yh_jinxiaocun_jichuziliao where gs_name = ? group by sp_dm";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
//        List<String> cpidList = new ArrayList<>();
//        if (cpidList!=null){
//            for (int i = 0; i < list.size(); i++) {
//                cpidList.add(list.get(i).getSpDm());
//            }
//        }
//        return cpidList != null && cpidList.size() > 0 ? cpidList : null;
//    }

    public List<String> getCpid(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select sp_dm from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? group by sp_dm";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
                List<String> cpidList = new ArrayList<>();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        cpidList.add(list.get(i).getSpDm());
                    }
                }
                return cpidList.size() > 0 ? cpidList : null;

            } else {
                // MySQL 版本
                String sql = "select sp_dm from yh_jinxiaocun_jichuziliao where gs_name = ? group by sp_dm";
                base = new JxcBaseDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
                List<String> cpidList = new ArrayList<>();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        cpidList.add(list.get(i).getSpDm());
                    }
                }
                return cpidList.size() > 0 ? cpidList : null;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取产品ID列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 根据商品代码查询
     */
//    public List<YhJinXiaoCunJiChuZiLiao> getListByCpid(String company, String cpid) {
//        String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and sp_dm=? ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
//        return list;
//    }

    public List<YhJinXiaoCunJiChuZiLiao> getListByCpid(String company, String cpid) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? and sp_dm=?";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and sp_dm=?";
                base = new JxcBaseDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "根据产品ID获取基础资料列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



    /**
     * 新增
     */
//    public boolean insert(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
//        String sql = "insert into yh_jinxiaocun_jichuziliao(sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";
//
//        base = new JxcBaseDao();
//        long result = base.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
//        return result > 0;
//    }

    public boolean insert(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "insert into yh_jinxiaocun_jichuziliao_mssql (sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";
                base2 = new JxcServerDao();
                long result = base2.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
                return result > 0;

            } else {
                // MySQL 版本
                String sql = "insert into yh_jinxiaocun_jichuziliao (sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";
                base = new JxcBaseDao();
                long result = base.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "插入基础资料数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改
     */
//    public boolean update(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
//        String sql = "update yh_jinxiaocun_jichuziliao set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=? ";
//
//        base = new JxcBaseDao();
//        boolean result = base.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
//        return result;
//    }
    public boolean update(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_jichuziliao_mssql set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_jichuziliao set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新基础资料数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除
     */
//    public boolean delete(int id) {
//        String sql = "delete from yh_jinxiaocun_jichuziliao where id = ?";
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
                String sql = "delete from yh_jinxiaocun_jichuziliao_mssql where id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_jichuziliao where id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除基础资料数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
