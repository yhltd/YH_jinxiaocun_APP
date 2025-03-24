package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianRijiao;
import com.example.myapplication.mendian.entity.YhMendianYuejiao;

import java.util.List;

public class YhMendianYuejiaoService {
    private MendianDao base;

    /**
     * 查询全部客户数据
     */
    public List<YhMendianYuejiao> getList(String shou, String fu, String chi,String start_date,String end_date, String company) {
//        String sql = "select b.*,sum(a.repayment) as repayment,sum(a.swipe) as swipe,sum(a.repayment)-sum(a.swipe) as balance_of_credit_card,sum(a.basics_service_charge)+sum(a.other_service_charge) as the_total_fee,sum(a.swipe)*(b.service_charge)+sum(a.repayment)-sum(a.swipe) as collected_amount,sum(a.swipe)*(b.service_charge)-sum(a.basics_service_charge)+sum(a.other_service_charge) as profitfrom from day_trading as a,customer as b where a.id=b.id and a.gongsi = ? and recipient like '%' ? '%' and cardholder like '%' ? '%' and drawee like '%' ? '%' and a.date_time  >= ? and a.date_time <= ?";
        String sql = "select b.*,sum(a.repayment) as repayment,sum(a.swipe) as swipe,sum(a.repayment)-sum(a.swipe) as balance_of_credit_card,sum(a.basics_service_charge)+sum(a.other_service_charge) as the_total_fee,sum(a.swipe)*(b.service_charge)+sum(a.repayment)-sum(a.swipe) as collected_amount,sum(a.swipe)*(b.service_charge)-sum(a.basics_service_charge)+sum(a.other_service_charge) as profitfrom from customer as b left join day_trading as a on b.id = a.id where b.gongsi= ? and b.recipient like '%' ? '%' and b.cardholder like '%' ? '%' and b.drawee like '%' ? '%' and a.date_time >= ? and a.date_time <= ? group by b.id";
        base = new MendianDao();
        List<YhMendianYuejiao> list = base.query(YhMendianYuejiao.class, sql, company, shou,fu,chi,start_date,end_date);
        return list;
    }
}
