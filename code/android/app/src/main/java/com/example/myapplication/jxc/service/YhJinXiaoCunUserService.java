package com.example.myapplication.jxc.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunUserService {
    private JxcBaseDao base;
    private JxcServerDao base2;

//    public YhJinXiaoCunUser login(String username, String password, String company) {
//        String sql = "select * from yh_jinxiaocun_user where `name` = ? and `password` = ? and gongsi = ? ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, username, password, company);
//        return list != null && list.size() > 0 ? list.get(0) : null;
//    }


    public YhJinXiaoCunUser login(String username, String password, String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {

                String sql = "select * from yh_jinxiaocun_user_mssql where name = ? and password = ? and gongsi = ? ";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunUser> list = base2.query(YhJinXiaoCunUser.class, sql, username, password, company);
                return list != null && list.size() > 0 ? list.get(0) : null;

            } else {

                String sql = "select * from yh_jinxiaocun_user where `name` = ? and `password` = ? and gongsi = ? ";
                base = new JxcBaseDao();
                List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, username, password, company);
                return list != null && list.size() > 0 ? list.get(0) : null;

            }
        } catch (Exception e) {
            Log.e("SQLDebug", "登录过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public List<String> getCompany() {
        String sql = "select gongsi from yh_jinxiaocun_user group by gongsi";
        base = new JxcBaseDao();
        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getGongsi());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    public List<String> getCompany2() {
        String sql = "select gongsi from yh_jinxiaocun_user_mssql group by gongsi";
        base2 = new JxcServerDao();
        List<YhJinXiaoCunUser> list = base2.query(YhJinXiaoCunUser.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getGongsi());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }



    /**
     * 查询全部数据
     */
//    public List<YhJinXiaoCunUser> getList(String company, String name) {
//        String sql = "select * from yh_jinxiaocun_user where gongsi = ? and `name` like '%' ? '%' order by _id";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, company, name);
//        return list;
//    }

    public List<YhJinXiaoCunUser> getList(String company, String name) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 使用 + 进行字符串拼接
                String sql = "select * from yh_jinxiaocun_user_mssql where gongsi = ? and name like '%' + ? + '%' order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunUser> list = base2.query(YhJinXiaoCunUser.class, sql, company, name);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 使用 CONCAT 或直接拼接
                String sql = "select * from yh_jinxiaocun_user where gongsi = ? and name like concat('%', ?, '%') order by _id";
                base = new JxcBaseDao();
                List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, company, name);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取用户列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 新增
     */
//    public boolean insert(YhJinXiaoCunUser yhJinXiaoCunUser) {
//        String sql = "insert into yh_jinxiaocun_user (_id,AdminIS,Btype,gongsi,`name`,`password`) values(?,?,?,?,?,?)";
//
//        base = new JxcBaseDao();
//        long result = base.executeOfId(sql, yhJinXiaoCunUser.get_id(), yhJinXiaoCunUser.getAdminis(), yhJinXiaoCunUser.getBtype(), yhJinXiaoCunUser.getGongsi(), yhJinXiaoCunUser.getName(), yhJinXiaoCunUser.getPassword());
//        return result > 0;
//    }

    public boolean insert(YhJinXiaoCunUser yhJinXiaoCunUser) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "insert into yh_jinxiaocun_user_mssql (_id,AdminIS,Btype,gongsi,name,password) values(?,?,?,?,?,?)";
                base2 = new JxcServerDao();
                long result = base2.executeOfId(sql, yhJinXiaoCunUser.get_id(), yhJinXiaoCunUser.getAdminis(), yhJinXiaoCunUser.getBtype(), yhJinXiaoCunUser.getGongsi(), yhJinXiaoCunUser.getName(), yhJinXiaoCunUser.getPassword());
                return result > 0;

            } else {
                // MySQL 版本
                String sql = "insert into yh_jinxiaocun_user (_id,AdminIS,Btype,gongsi,`name`,`password`) values(?,?,?,?,?,?)";
                base = new JxcBaseDao();
                long result = base.executeOfId(sql, yhJinXiaoCunUser.get_id(), yhJinXiaoCunUser.getAdminis(), yhJinXiaoCunUser.getBtype(), yhJinXiaoCunUser.getGongsi(), yhJinXiaoCunUser.getName(), yhJinXiaoCunUser.getPassword());
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "插入用户过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 修改
     */
//    public boolean update(YhJinXiaoCunUser yhJinXiaoCunUser) {
//        String sql = "update yh_jinxiaocun_user set AdminIS=?,Btype=?,gongsi=?,`name`=?,`password`=? where _id=? ";
//
//        base = new JxcBaseDao();
//        boolean result = base.execute(sql, yhJinXiaoCunUser.getAdminis(), yhJinXiaoCunUser.getBtype(), yhJinXiaoCunUser.getGongsi(), yhJinXiaoCunUser.getName(), yhJinXiaoCunUser.getPassword(), yhJinXiaoCunUser.get_id());
//        return result;
//    }

    public boolean update(YhJinXiaoCunUser yhJinXiaoCunUser) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_user_mssql set AdminIS=?,Btype=?,gongsi=?,name=?,password=? where _id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunUser.getAdminis(), yhJinXiaoCunUser.getBtype(), yhJinXiaoCunUser.getGongsi(), yhJinXiaoCunUser.getName(), yhJinXiaoCunUser.getPassword(), yhJinXiaoCunUser.get_id());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_user set AdminIS=?,Btype=?,gongsi=?,`name`=?,`password`=? where _id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunUser.getAdminis(), yhJinXiaoCunUser.getBtype(), yhJinXiaoCunUser.getGongsi(), yhJinXiaoCunUser.getName(), yhJinXiaoCunUser.getPassword(), yhJinXiaoCunUser.get_id());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新用户过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 删除
     */
//    public boolean delete(String id) {
//        String sql = "delete from yh_jinxiaocun_user where _id = ?";
//        base = new JxcBaseDao();
//        return base.execute(sql, id);
//    }
    public boolean delete(String id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "delete from yh_jinxiaocun_user_mssql where _id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_user where _id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除用户过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断ID是否重复
     */
//    public List<YhJinXiaoCunUser> getListById(String id) {
//        String sql = "select * from yh_jinxiaocun_user where _id = ? ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, id);
//        return list;
//    }

    public List<YhJinXiaoCunUser> getListById(String id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_user_mssql where _id = ?";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunUser> list = base2.query(YhJinXiaoCunUser.class, sql, id);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_user where _id = ?";
                base = new JxcBaseDao();
                List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, id);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "根据ID获取用户列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public YhJinXiaoCunUser queryByCompanyAndAccount(String companyName, String account, String password) {
        String sql = "SELECT * FROM yh_jinxiaocun_user_mssql WHERE gongsi = ? AND name = ? AND password = ?";
        JxcServerDao baseDao = new JxcServerDao();
        List<YhJinXiaoCunUser> userList = baseDao.query(YhJinXiaoCunUser.class, sql, companyName, account, password);
        if (userList != null && !userList.isEmpty()) {
            return userList.get(0); // 返回第一个匹配的用户
        }else{
            return null;
        }
    }

//    public YhJinXiaoCunUser queryByCompanyAndAccount(String companyName, String account, String password) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本
//                String sql = "SELECT * FROM yh_jinxiaocun_user_mssql WHERE gongsi = ? AND name = ? AND password = ?";
//                base2 = new JxcServerDao();
//                List<YhJinXiaoCunUser> userList = base2.query(YhJinXiaoCunUser.class, sql, companyName, account, password);
//                return userList != null && !userList.isEmpty() ? userList.get(0) : null;
//
//            } else {
//                // MySQL 版本
//                String sql = "SELECT * FROM yh_jinxiaocun_user WHERE gongsi = ? AND `name` = ? AND `password` = ?";
//                base = new JxcBaseDao();
//                List<YhJinXiaoCunUser> userList = base.query(YhJinXiaoCunUser.class, sql, companyName, account, password);
//                return userList != null && !userList.isEmpty() ? userList.get(0) : null;
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "根据公司和账号查询用户过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }



}
