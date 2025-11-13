package com.example.myapplication.jxc.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.GeRen;
import com.example.myapplication.jxc.dao.JxcBaseDao;

import java.util.ArrayList;
import java.util.List;

public class GeRenService {

    private JxcBaseDao base;
    private JxcServerDao base2;
    private Context context;

    public GeRenService(Context context) {
        this.context = context;
    }

    public List<GeRen> getList() {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 从缓存读取公司名称、账号和密码
            SharedPreferences sharedPref = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
            String companyName = sharedPref.getString("companyName", "云合未来"); // 默认值
            String userAccount = sharedPref.getString("userAccount", ""); // 账号
            String userPassword = sharedPref.getString("userPassword", ""); // 密码

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "SELECT touxiang FROM yh_jinxiaocun_user_mssql WHERE gongsi=? AND password=? AND name=?";
                base2 = new JxcServerDao();
                List<GeRen> list = base2.query(GeRen.class, sql, companyName, userPassword, userAccount);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "SELECT touxiang FROM yh_jinxiaocun_user WHERE gongsi=? AND password=? AND name=?";
                base = new JxcBaseDao();
                List<GeRen> list = base.query(GeRen.class, sql, companyName, userPassword, userAccount);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取个人头像数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}