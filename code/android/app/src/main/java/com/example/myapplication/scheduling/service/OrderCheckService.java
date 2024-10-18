package com.example.myapplication.scheduling.service;

import android.util.Log;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.BomInfo;
import com.example.myapplication.scheduling.entity.OrderCheck;

import java.util.List;

public class OrderCheckService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<OrderCheck> getList(String company, String order_number, String moudle, String ks, String js) {
        base = new SchedulingDao();
        if (ks.equals("")) {
            ks = "1900-01-01";
        }
        if (js.equals("")) {
            js = "2200-01-01";
        }
        String sql = "select * from order_check where company=? and order_number like '%' + ? + '%' and moudle like '%' + ? + '%' and CONVERT(date,riqi) between ? and ? ";

        List<OrderCheck> list = base.query(OrderCheck.class, sql, company, order_number, moudle, ks, js);
        Log.d("ksksksksksk",ks);
        Log.d("ksksksksksk",js);
        Log.d("sql",sql);
        Log.d("sql",company);
        Log.d("sql",order_number);
        Log.d("sql",moudle);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(OrderCheck orderCheck) {
        String sql = "insert into order_check(order_number,moudle,riqi,num,company) values(?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, orderCheck.getOrder_number(), orderCheck.getMoudle(), orderCheck.getRiqi(), orderCheck.getNum(), orderCheck.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(OrderCheck orderCheck) {
        String sql = "update order_check set order_number=?,moudle=?,riqi=?,num=?,company=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, orderCheck.getOrder_number(), orderCheck.getMoudle(), orderCheck.getRiqi(), orderCheck.getNum(), orderCheck.getCompany(), orderCheck.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from order_check where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }
}
