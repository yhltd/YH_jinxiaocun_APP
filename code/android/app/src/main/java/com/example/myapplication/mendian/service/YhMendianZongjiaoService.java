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
        String sql = "select b.*,sum(a.repayment) as repayment,\n" +
                "    sum(a.swipe) as swipe,\n" +
                "    sum(a.repayment)-sum(a.swipe) as balance_of_credit_card,\n" +
                "    sum(a.basics_service_charge)+sum(a.other_service_charge) as the_total_fee,\n" +
                "    sum(a.swipe)*(b.service_charge)+sum(a.repayment)-sum(a.swipe) as collected_amount,sum(a.swipe)*(b.service_charge)-sum(a.basics_service_charge)+sum(a.other_service_charge) as profit\n" +
                "    from day_trading as a,customer as b where gongsi = ? and recipient like '%' ? '%' and cardholder like '%' ? '%' and drawee like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianZongjiao> list = base.query(YhMendianZongjiao.class, sql, company, startdate,enddate);
        return list;
    }
}
