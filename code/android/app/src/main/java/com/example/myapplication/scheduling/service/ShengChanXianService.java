package com.example.myapplication.scheduling.service;

import android.util.Log;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.ShengChanXian;

import java.util.List;

public class ShengChanXianService {
    private SchedulingDao base;

    /**
     * 查询所有生产线（带条件查询）
     */
    public List<ShengChanXian> getList(String company, String gongxu, String mingcheng, String xiaolv) {
        base = new SchedulingDao();
        String sql = "select * from shengchanxian where gongsi = ? " +
                "and gongxu like '%' + ? + '%' " +
                "and mingcheng like '%' + ? + '%' " +
                "and xiaolv like '%' + ? + '%' " +
                "order by id";
        List<ShengChanXian> list = base.query(ShengChanXian.class, sql,
                company, gongxu, mingcheng, xiaolv);
        return list;
    }

    /**
     * 获取所有生产线（不分页，返回所有数据）
     */
//    public List<ShengChanXian> getAll(String company) {
//        base = new SchedulingDao();
//        String sql = "select * from shengchanxian where gongsi = ? order by id";
//        List<ShengChanXian> list = base.query(ShengChanXian.class, sql, company);
//        return list;
//    }
    public List<ShengChanXian> getAll(String company) {
        base = new SchedulingDao();
        String sql = "select * from shengchanxian where gongsi = ? order by id";

        // 添加调试日志
        Log.e("DB_DEBUG", "执行查询: " + sql);
        Log.e("DB_DEBUG", "参数 company: " + company);
        Log.e("DB_DEBUG", "base对象: " + (base != null ? "not null" : "null"));

        List<ShengChanXian> list = base.query(ShengChanXian.class, sql, company);

        // 添加结果日志
        Log.e("DB_DEBUG", "查询结果: " + (list != null ? list.size() + " 条记录" : "null"));
        if (list != null && !list.isEmpty()) {
            for (ShengChanXian item : list) {
                Log.e("DB_DEBUG", "记录: id=" + item.getId() +
                        ", mingcheng=" + item.getMingcheng() +
                        ", gongxu=" + item.getGongxu() +
                        ", gongsi=" + item.getGongsi());
            }
        } else {
            Log.e("DB_DEBUG", "查询结果为空");
        }

        return list;
    }

    /**
     * 新增生产线
     */
    public boolean insert(ShengChanXian shengChanXian) {
        String sql = "insert into shengchanxian(mingcheng, gongxu, gongsi, xiaolv) values(?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql,
                shengChanXian.getMingcheng(),
                shengChanXian.getGongxu(),
                shengChanXian.getGongsi(),
                shengChanXian.getXiaolv());
        return result > 0;
    }

    /**
     * 修改生产线
     */
    public boolean update(ShengChanXian shengChanXian) {
        String sql = "update shengchanxian set mingcheng=?, gongxu=?, xiaolv=? where id=? and gongsi=?";
        base = new SchedulingDao();
        boolean result = base.execute(sql,
                shengChanXian.getMingcheng(),
                shengChanXian.getGongxu(),
                shengChanXian.getXiaolv(),
                shengChanXian.getId(),
                shengChanXian.getGongsi());
        return result;
    }

    /**
     * 删除生产线
     */
    public boolean delete(int id, String company) {
        String sql = "delete from shengchanxian where id=? and gongsi=?";
        base = new SchedulingDao();
        return base.execute(sql, id, company);
    }
}