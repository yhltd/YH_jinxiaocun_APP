//package com.example.myapplication.jxc.service;
//
//import android.util.Log;
//
//import com.example.myapplication.CacheManager;
//import com.example.myapplication.jxc.dao.JxcBaseDao;
//import com.example.myapplication.jxc.dao.JxcServerDao;
//import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
//import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class YhJinXiaoCunJiChuZiLiaoService {
//    private JxcBaseDao base;
//    private JxcServerDao base2;
//
//    /**
//     * 查询全部数据
//     */
////    public List<YhJinXiaoCunJiChuZiLiao> getList(String company, String cpname) {
////        String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and name like '%' ? '%' order by id";
////        base = new JxcBaseDao();
////        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
////        return list;
////    }
//    public List<YhJinXiaoCunJiChuZiLiao> getList(String company, String cpname) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本 - 使用 + 进行字符串拼接
//                String sql = "select * from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? and name like '%' + ? + '%' order by id";
//                base2 = new JxcServerDao();
//                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
//                return list != null ? list : new ArrayList<>();
//
//            } else {
//                // MySQL 版本 - 使用 CONCAT 函数
//                String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and name like concat('%', ?, '%') order by id";
//                base = new JxcBaseDao();
//                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
//                return list != null ? list : new ArrayList<>();
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "获取基础资料列表过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
//
//    /**
//     * 商品代码下拉
//     */
////    public List<String> getCpid(String company) {
////        String sql = "select sp_dm from yh_jinxiaocun_jichuziliao where gs_name = ? group by sp_dm";
////        base = new JxcBaseDao();
////        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
////        List<String> cpidList = new ArrayList<>();
////        if (cpidList!=null){
////            for (int i = 0; i < list.size(); i++) {
////                cpidList.add(list.get(i).getSpDm());
////            }
////        }
////        return cpidList != null && cpidList.size() > 0 ? cpidList : null;
////    }
//
//    public List<String> getCpid(String company) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本
//                String sql = "select sp_dm from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? group by sp_dm";
//                base2 = new JxcServerDao();
//                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
//                List<String> cpidList = new ArrayList<>();
//                if (list != null) {
//                    for (int i = 0; i < list.size(); i++) {
//                        cpidList.add(list.get(i).getSpDm());
//                    }
//                }
//                return cpidList.size() > 0 ? cpidList : null;
//
//            } else {
//                // MySQL 版本
//                String sql = "select sp_dm from yh_jinxiaocun_jichuziliao where gs_name = ? group by sp_dm";
//                base = new JxcBaseDao();
//                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
//                List<String> cpidList = new ArrayList<>();
//                if (list != null) {
//                    for (int i = 0; i < list.size(); i++) {
//                        cpidList.add(list.get(i).getSpDm());
//                    }
//                }
//                return cpidList.size() > 0 ? cpidList : null;
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "获取产品ID列表过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
//    /**
//     * 根据商品代码查询
//     */
////    public List<YhJinXiaoCunJiChuZiLiao> getListByCpid(String company, String cpid) {
////        String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and sp_dm=? ";
////        base = new JxcBaseDao();
////        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
////        return list;
////    }
//
//    public List<YhJinXiaoCunJiChuZiLiao> getListByCpid(String company, String cpid) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本
//                String sql = "select * from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? and sp_dm=?";
//                base2 = new JxcServerDao();
//                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
//                return list != null ? list : new ArrayList<>();
//
//            } else {
//                // MySQL 版本
//                String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and sp_dm=?";
//                base = new JxcBaseDao();
//                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
//                return list != null ? list : new ArrayList<>();
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "根据产品ID获取基础资料列表过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
//
//
//
//    /**
//     * 新增
//     */
////    public boolean insert(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
////        String sql = "insert into yh_jinxiaocun_jichuziliao(sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";
////
////        base = new JxcBaseDao();
////        long result = base.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
////        return result > 0;
////    }
//
//    public boolean insert(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本
//                String sql = "insert into yh_jinxiaocun_jichuziliao_mssql (sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";
//                base2 = new JxcServerDao();
//                long result = base2.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
//                return result > 0;
//
//            } else {
//                // MySQL 版本
//                String sql = "insert into yh_jinxiaocun_jichuziliao (sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";
//                base = new JxcBaseDao();
//                long result = base.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
//                return result > 0;
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "插入基础资料数据过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 修改
//     */
////    public boolean update(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
////        String sql = "update yh_jinxiaocun_jichuziliao set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=? ";
////
////        base = new JxcBaseDao();
////        boolean result = base.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
////        return result;
////    }
//    public boolean update(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本
//                String sql = "update yh_jinxiaocun_jichuziliao_mssql set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=?";
//                base2 = new JxcServerDao();
//                boolean result = base2.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
//                return result;
//
//            } else {
//                // MySQL 版本
//                String sql = "update yh_jinxiaocun_jichuziliao set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=?";
//                base = new JxcBaseDao();
//                boolean result = base.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
//                return result;
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "更新基础资料数据过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    /**
//     * 删除
//     */
////    public boolean delete(int id) {
////        String sql = "delete from yh_jinxiaocun_jichuziliao where id = ?";
////        base = new JxcBaseDao();
////        return base.execute(sql, id);
////    }
//    public boolean delete(int id) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本
//                String sql = "delete from yh_jinxiaocun_jichuziliao_mssql where id = ?";
//                base2 = new JxcServerDao();
//                return base2.execute(sql, id);
//
//            } else {
//                // MySQL 版本
//                String sql = "delete from yh_jinxiaocun_jichuziliao where id = ?";
//                base = new JxcBaseDao();
//                return base.execute(sql, id);
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "删除基础资料数据过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//}
package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YhJinXiaoCunJiChuZiLiaoService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 查询全部数据
     */
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

    // ==================== 文件上传相关方法 ====================

    /**
     * 获取当前文件URL（mark1字段）
     */
    public String getCurrentFile(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            String selectSql = "select mark1 from " + (shujukuValue == 1 ? "yh_jinxiaocun_jichuziliao_mssql" : "yh_jinxiaocun_jichuziliao") + " where id = ?";

            if (shujukuValue == 1) {
                base2 = new JxcServerDao();
                List<YhJinXiaoCunJiChuZiLiao> result = base2.query(YhJinXiaoCunJiChuZiLiao.class, selectSql, id);
                if (result != null && result.size() > 0) {
                    return result.get(0).getMark1();
                }
            } else {
                base = new JxcBaseDao();
                List<YhJinXiaoCunJiChuZiLiao> result = base.query(YhJinXiaoCunJiChuZiLiao.class, selectSql, id);
                if (result != null && result.size() > 0) {
                    return result.get(0).getMark1();
                }
            }
            return null;
        } catch (Exception e) {
            Log.e("FileUpload", "获取当前文件失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 更新文件记录（mark1字段）
     */
    public boolean updateFileRecord(int id, String fileUrl) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            String sql = "update " + (shujukuValue == 1 ? "yh_jinxiaocun_jichuziliao_mssql" : "yh_jinxiaocun_jichuziliao") + " set mark1 = ? where id = ?";

            if (shujukuValue == 1) {
                base2 = new JxcServerDao();
                return base2.execute(sql, fileUrl, id);
            } else {
                base = new JxcBaseDao();
                return base.execute(sql, fileUrl, id);
            }
        } catch (Exception e) {
            Log.e("FileUpload", "更新文件记录失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 清空文件记录（mark1字段）
     */
    public boolean clearFileRecord(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            String sql = "update " + (shujukuValue == 1 ? "yh_jinxiaocun_jichuziliao_mssql" : "yh_jinxiaocun_jichuziliao") + " set mark1 = null where id = ?";

            if (shujukuValue == 1) {
                base2 = new JxcServerDao();
                return base2.execute(sql, id);
            } else {
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("FileUpload", "清空文件记录失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 上传文件到服务器
     */
    public void uploadFile(File file, String fileName, String path, String kongjian,
                           String recordId, String recordName, String userFileName,
                           UploadCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                FileInputStream fis = null;
                try {
                    // 1. 构建最终文件名
                    String finalFileName = fileName;
                    if (userFileName != null && !userFileName.trim().isEmpty()) {
                        String baseName = userFileName.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
                        if (fileName.contains(".")) {
                            String extension = fileName.substring(fileName.lastIndexOf('.'));
                            finalFileName = baseName + extension;
                        } else {
                            finalFileName = baseName;
                        }
                    }

                    // 2. 创建连接到9097端口
                    URL url = new URL("https://yhocn.cn:9097/file/upload");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(60000);

                    // 3. 设置边界
                    String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    // 4. 构建请求体
                    dos = new DataOutputStream(conn.getOutputStream());

                    // 5. 写入文件
                    dos.writeBytes("--" + boundary + "\r\n");
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + finalFileName + "\"\r\n");
                    dos.writeBytes("Content-Type: application/octet-stream\r\n\r\n");

                    fis = new FileInputStream(file);
                    byte[] buffer = new byte[8192];
                    int count;
                    while ((count = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, count);
                    }
                    dos.writeBytes("\r\n");

                    // 6. 写入表单参数
                    String jxcPath = "/jinxiaocun/";
                    writeFormField(dos, boundary, "name", finalFileName);
                    writeFormField(dos, boundary, "path", jxcPath);
                    writeFormField(dos, boundary, "kongjian", kongjian);
                    writeFormField(dos, boundary, "fileType",
                            fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1) : "");
                    writeFormField(dos, boundary, "recordId", recordId);
                    writeFormField(dos, boundary, "recordName", recordName);
                    writeFormField(dos, boundary, "userFileName", userFileName != null ? userFileName : "");
                    writeFormField(dos, boundary, "timestamp", String.valueOf(System.currentTimeMillis()));

                    // 结束请求体
                    dos.writeBytes("--" + boundary + "--\r\n");
                    dos.flush();

                    // 7. 处理响应
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject json = new JSONObject(response.toString());
                        if (json.optInt("code", -1) == 200) {
                            String fileUrl = "http://yhocn.cn:9088" + jxcPath + finalFileName;
                            callback.onSuccess(fileUrl);
                        } else {
                            callback.onFailure(json.optString("msg", "上传失败"));
                        }
                    } else {
                        callback.onFailure("服务器错误: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure("上传失败: " + e.getMessage());
                } finally {
                    try {
                        if (fis != null) fis.close();
                        if (dos != null) dos.close();
                        if (conn != null) conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 从服务器删除文件
     */
    public void deleteFileFromServer(String fileName, String path, DeleteCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                try {
                    // 清理文件名
                    String cleanFileName = fileName;
                    if (fileName.contains(".")) {
                        cleanFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    }

                    // 创建连接
                    URL url = new URL("https://yhocn.cn:9097/file/delete");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);

                    // 对中文字符进行URL编码
                    String encodedOrderNumber = URLEncoder.encode(cleanFileName, "UTF-8");
                    String encodedPath = URLEncoder.encode(path, "UTF-8");
                    String formData = "order_number=" + encodedOrderNumber + "&path=" + encodedPath;

                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.write(formData.getBytes("UTF-8"));
                    dos.flush();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject json = new JSONObject(response.toString());
                        int code = json.optInt("code", -1);
                        boolean success = json.optBoolean("success", false);

                        if (code == 200 || success) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(json.optString("msg", "删除失败"));
                        }
                    } else {
                        callback.onFailure("服务器错误: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure("删除失败: " + e.getMessage());
                } finally {
                    try {
                        if (dos != null) dos.close();
                        if (conn != null) conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void writeFormField(DataOutputStream dos, String boundary, String name, String value) throws Exception {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
        dos.write(value.getBytes("UTF-8"));
        dos.writeBytes("\r\n");
    }

    // 回调接口
    public interface UploadCallback {
        void onSuccess(String fileUrl);
        void onFailure(String error);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(String error);
    }
}