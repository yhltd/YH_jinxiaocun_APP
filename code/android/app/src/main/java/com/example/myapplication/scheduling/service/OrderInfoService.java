package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.OrderBom;
import com.example.myapplication.scheduling.entity.OrderInfo;

import java.util.List;

public class OrderInfoService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<OrderInfo> getList(String company, String product_name, String order_id) {
        base = new SchedulingDao();
        String sql = "select * from order_info where company=? and product_name like '%' + ? + '%' and order_id like '%' + ? + '%'  ";
        List<OrderInfo> list = base.query(OrderInfo.class, sql, company, product_name, order_id);
        return list;
    }

    /**
     * 刷新
     */
    public List<OrderInfo> getLast() {
        base = new SchedulingDao();
        String sql = "select top 1 * from order_info order by id desc ";
        List<OrderInfo> list = base.query(OrderInfo.class, sql);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(OrderInfo orderInfo) {
        String sql = "insert into order_info(code,product_name,norms,order_id,set_date,set_num,company,is_complete) values(?,?,?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, orderInfo.getCode(), orderInfo.getProduct_name(), orderInfo.getNorms(), orderInfo.getOrder_id(), orderInfo.getSet_date(), orderInfo.getSet_num(), orderInfo.getCompany(), orderInfo.getIs_complete());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(OrderInfo orderInfo) {
        String sql = "update order_info set code=?,product_name=?,norms=?,order_id=?,set_date=?,set_num=?,company=?,is_complete=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, orderInfo.getCode(), orderInfo.getProduct_name(), orderInfo.getNorms(), orderInfo.getOrder_id(), orderInfo.getSet_date(), orderInfo.getSet_num(), orderInfo.getCompany(), orderInfo.getIs_complete(), orderInfo.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from order_info where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }

    /**
     * 新增
     */
    public boolean insertOrderBom(OrderBom orderBom) {
        String sql = "insert into order_bom(order_id,bom_id,use_num) values(?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, orderBom.getOrder_id(), orderBom.getBom_id(), orderBom.getUse_num());
        return result > 0;
    }

    /**
     * 删除
     */
    public boolean deleteOrderBom(int order_id) {
        String sql = "delete from order_bom where order_id = ?";
        base = new SchedulingDao();
        return base.execute(sql, order_id);
    }
}
