package com.example.myapplication.fenquan.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.GeRen;

import java.util.List;

public class GeRenService {

    private FenquanDao base;
    private Context context;

    public GeRenService(Context context) {
        this.context = context;
    }

    public List<GeRen> getList() {
        base = new FenquanDao();

        // 从缓存读取公司名称、账号和密码
        SharedPreferences sharedPref = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
        String companyName = sharedPref.getString("companyName", "云合未来"); // 默认值
        String userAccount = sharedPref.getString("userAccount", ""); // 账号
        String userPassword = sharedPref.getString("userPassword", ""); // 密码

        // 构建SQL查询语句
        String sql = "SELECT touxiang FROM baitaoquanxian_renyun WHERE B=? AND D=? AND E=?";

        // 执行查询，传入三个参数
        List<GeRen> list = base.query(GeRen.class, sql, companyName, userPassword, userAccount);

        return list;
    }
}