package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianRijiao;
import com.example.myapplication.mendian.entity.YhMendianYuejiao;
import com.example.myapplication.mendian.entity.YhMendianZongjiao;

import java.util.List;

public class YhMendianZongjiaoService {
    private MendianDao base;

    /**
     * 查询全部客户数据
     */
    public List<YhMendianZongjiao> getList(String startdate, String enddate, String company) {
        String sql = "select a.date_time,sum(a.repayment) as repayment,sum(a.swipe) as swipe,(sum(a.basics_service_charge)+sum(a.other_service_charge)) as the_total_fee,sum(a.swipe)*sum(b.service_charge)-sum(a.basics_service_charge)+sum(a.other_service_charge) as profit from day_trading as a,customer as b where a.gongsi = ? and a.date_time between ? and ? group by a.date_time ";
        base = new MendianDao();
        List<YhMendianZongjiao> list = base.query(YhMendianZongjiao.class, sql, company, startdate,enddate);
        return list;
    }
}
