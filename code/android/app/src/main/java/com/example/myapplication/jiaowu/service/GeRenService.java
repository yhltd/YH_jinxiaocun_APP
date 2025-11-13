package com.example.myapplication.jiaowu.service;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.GeRen;

import java.util.List;

public class GeRenService {

    private JiaowuBaseDao base;
    private JiaowuServerDao base1;
    private Context context;

    public GeRenService(Context context) {
        this.context = context;
    }

//    public List<GeRen> getList() {
//        base = new JiaowuBaseDao();
//
//        // 从缓存读取公司名称、账号和密码
//        SharedPreferences sharedPref = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
//        String companyName = sharedPref.getString("companyName", "云合未来"); // 默认值
//        String userAccount = sharedPref.getString("userAccount", ""); // 账号
//        String userPassword = sharedPref.getString("userPassword", ""); // 密码
//
//        // 构建SQL查询语句
//        String sql = "SELECT touxiang FROM teacher WHERE Company=? AND Password=? AND UserName=?";
//
//        // 执行查询，传入三个参数
//        List<GeRen> list = base.query(GeRen.class, sql, companyName, userPassword, userAccount);
//
//        return list;
//    }

    public List<GeRen> getList() {
        // 从缓存读取公司名称、账号和密码
        SharedPreferences sharedPref = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
        String companyName = sharedPref.getString("companyName", "云合未来"); // 默认值
        String userAccount = sharedPref.getString("userAccount", ""); // 账号
        String userPassword = sharedPref.getString("userPassword", ""); // 密码

        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "SELECT touxiang FROM teacher WHERE Company=? AND Password=? AND UserName=?";
            base1 = new JiaowuServerDao();
            List<GeRen> list = base1.query(GeRen.class, sql, companyName, userPassword, userAccount);
            return list;
        } else {
            // MySQL 版本
            String sql = "SELECT touxiang FROM teacher WHERE Company=? AND Password=? AND UserName=?";
            base = new JiaowuBaseDao();
            List<GeRen> list = base.query(GeRen.class, sql, companyName, userPassword, userAccount);
            return list;
        }
    }
}