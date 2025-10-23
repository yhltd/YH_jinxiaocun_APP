package com.example.myapplication.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.dao.PushNewsDao;
import com.example.myapplication.entity.PushNews;

import java.util.List;

public class PushNewsService {

    private PushNewsDao base;
    private Context context;

    public PushNewsService(Context context) {
        this.context = context;
    }

    public List<PushNews> getList() {
        base = new PushNewsDao();

        // 从缓存读取公司名称和系统名称
        SharedPreferences sharedPref = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
        String companyName = sharedPref.getString("companyName", "云合未来"); // 默认值
        String systemName = sharedPref.getString("systemName", "教务管理系统"); // 默认值

        String sql = "SELECT tptop1,tptop2,tptop3,tptop4,tptop5,tptop6,topgao,xuankuan,xuangao,textbox,beizhu1,beizhu2,beizhu3 FROM product_pushnews WHERE gsname=? AND xtname=?";
        List<PushNews> list = base.query(PushNews.class, sql, companyName, systemName);
        return list;
    }
}