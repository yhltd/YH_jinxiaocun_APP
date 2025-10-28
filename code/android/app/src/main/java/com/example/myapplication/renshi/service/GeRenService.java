package com.example.myapplication.renshi.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.renshi.entity.GenRen;
import com.example.myapplication.renshi.dao.renshiBaseDao;

import java.util.List;

public class GeRenService {

    private renshiBaseDao base;
    private Context context;

    public GeRenService(Context context) {
        this.context = context;
    }

    public List<GenRen> getList() {
        base = new renshiBaseDao();

        // 从缓存读取公司名称、账号和密码
        SharedPreferences sharedPref = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
        String companyName = sharedPref.getString("companyName", "云合未来"); // 默认值
        String userAccount = sharedPref.getString("userAccount", ""); // 账号
        String userPassword = sharedPref.getString("userPassword", ""); // 密码

        // 构建SQL查询语句
        String sql = "SELECT touxiang FROM gongzi_renyuan WHERE L=? AND I=? AND J=?";

        // 执行查询，传入三个参数
        List<GenRen> list = base.query(GenRen.class, sql, companyName, userPassword, userAccount);

        return list;
    }
}