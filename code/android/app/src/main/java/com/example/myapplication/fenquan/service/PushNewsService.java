package com.example.myapplication.fenquan.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.renshi.dao.PushNewsDao;
import com.example.myapplication.renshi.entity.PushNews;

import java.util.List;

public class PushNewsService {

    private PushNewsDao base;
    private Context context;

    public PushNewsService(Context context) {
        this.context = context;
    }

    public List<PushNews> getList() {
        base = new PushNewsDao();

        // 从缓存读取公司名称
        SharedPreferences sharedPref = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
        String companyName = sharedPref.getString("companyName", "云合未来"); // 默认值

        String sql = "SELECT tptop1,tptop2,tptop3,tptop4,tptop5,tptop6,topgao,xuankuan,xuangao,textbox,beizhu1 FROM product_pushnews WHERE gsname=? AND xtname='分权编辑系统' AND ((qidate IS NULL OR GETUTCDATE() >= CONVERT(DATETIME, LEFT(qidate, 10), 120)) AND (zhidate IS NULL OR GETUTCDATE() <= CONVERT(DATETIME, LEFT(zhidate, 10), 120)))";

        List<PushNews> list = base.query(PushNews.class, sql, companyName);
        return list;
    }
}
