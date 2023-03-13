package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianKeHu;
import com.example.myapplication.mendian.entity.YhMendianRijiao;

import java.util.List;

public class YhMendianRijiaoService {

    private MendianDao base;

    /**
     * 查询全部客户数据
     */
    public List<YhMendianRijiao> getList(String shou, String fu, String chi, String company) {
        String sql = "select * from day_trading right join customer on customer.id = day_trading.id where gongsi = ? and recipient like '%' ? '%' and cardholder like '%' ? '%' and drawee like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianRijiao> list = base.query(YhMendianRijiao.class, sql, company, shou,fu,chi);
        return list;
    }
}
