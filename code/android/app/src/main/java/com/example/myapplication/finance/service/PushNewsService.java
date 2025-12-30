package com.example.myapplication.finance.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

        String sql = "SELECT tptop1,tptop2,tptop3,tptop4,tptop5,tptop6,topgao,xuankuan,xuangao,textbox,beizhu1 FROM product_pushnews WHERE gsname=? AND xtname='云合未来财务系统' AND ((qidate IS NULL OR GETUTCDATE() >= CONVERT(DATETIME, LEFT(qidate, 10), 120)) AND (zhidate IS NULL OR GETUTCDATE() <= CONVERT(DATETIME, LEFT(zhidate, 10), 120)))";
        // 1. 输出SQL语句和参数
        Log.d("SQL_DEBUG", "SQL语句: " + sql);
        Log.d("SQL_DEBUG", "参数 companyName: " + companyName);
        Log.d("SQL_DEBUG", "完整SQL: " + sql.replace("?", "'" + companyName + "'"));
        List<PushNews> list = base.query(PushNews.class, sql, companyName);
        // 2. 输出查询结果
        if (list == null) {
            Log.d("SQL_DEBUG", "查询结果: list is null");
        } else {
            Log.d("SQL_DEBUG", "查询结果条数: " + list.size());
        }
        return list;
    }
}
