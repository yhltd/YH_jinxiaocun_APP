package com.example.myapplication.mendian.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.GeRen;

import java.util.List;

public class GeRenService {

    private MendianDao base;
    private Context context;

    public GeRenService(Context context) {
        this.context = context;
    }

    public List<GeRen> getList() {
        base = new MendianDao();

        // 从缓存读取公司名称、账号和密码
        SharedPreferences sharedPref = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
        String companyName = sharedPref.getString("companyName", "云合未来"); // 默认值
        String userAccount = sharedPref.getString("userAccount", ""); // 账号
        String userPassword = sharedPref.getString("userPassword", ""); // 密码

        // 构建SQL查询语句
        String sql = "SELECT touxiang FROM users WHERE company=? AND password=? AND account=?";

        // 执行查询，传入三个参数
        List<GeRen> list = base.query(GeRen.class, sql, companyName, userPassword, userAccount);

        return list;
    }
}