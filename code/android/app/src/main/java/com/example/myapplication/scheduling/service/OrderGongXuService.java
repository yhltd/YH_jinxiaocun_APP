package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.OrderGongXu;

import java.util.List;

public class OrderGongXuService {
    private SchedulingDao base;

    /**
     * 根据订单ID获取工序信息
     */
    public List<OrderGongXu> getListByOrderId(int orderId) {
        base = new SchedulingDao();
        String sql = "select * from order_gongxu where order_id = ?";
        List<OrderGongXu> list = base.query(OrderGongXu.class, sql, orderId);
        return list;
    }

    /**
     * 根据订单ID获取带工序信息的列表
     */
    public List<ModuleInfo> getModuleListByOrderId(int orderId) {
        base = new SchedulingDao();
        String sql = "select mi.*, og.module_num as estimated_time from module_info mi " +
                "left join order_gongxu og on mi.id = og.module_id and og.order_id = ?";
        List<ModuleInfo> list = base.query(ModuleInfo.class, sql, orderId);
        return list;
    }

    /**
     * 新增订单工序 - 单个插入
     */
    public boolean insert(OrderGongXu orderGongXu) {
        String sql = "insert into order_gongxu(order_id, module_id, module_num) values(?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, orderGongXu.getOrder_id(), orderGongXu.getModule_id(), orderGongXu.getModule_num());
        return result > 0;
    }

    /**
     * 批量插入订单工序
     */
    public boolean batchInsert(List<OrderGongXu> list) {
        base = new SchedulingDao();
        boolean result = true;
        for (OrderGongXu orderGongXu : list) {
            String sql = "insert into order_gongxu(order_id, module_id, module_num) values(?,?,?)";
            long insertResult = base.executeOfId(sql, orderGongXu.getOrder_id(), orderGongXu.getModule_id(), orderGongXu.getModule_num());
            if (insertResult <= 0) {
                result = false;
            }
        }
        return result;
    }


    /**
     * 修改订单工序
     */
    public boolean update(OrderGongXu orderGongXu) {
        String sql = "update order_gongxu set module_num = ? where order_id = ? and module_id = ?";
        base = new SchedulingDao();
        boolean result = base.execute(sql, orderGongXu.getModule_num(), orderGongXu.getOrder_id(), orderGongXu.getModule_id());
        return result;
    }

    /**
     * 删除订单所有工序
     */
    public boolean deleteByOrderId(int orderId) {
        String sql = "delete from order_gongxu where order_id = ?";
        base = new SchedulingDao();
        return base.execute(sql, orderId);
    }

    /**
     * 删除指定订单工序
     */
    public boolean delete(int orderId, int moduleId) {
        String sql = "delete from order_gongxu where order_id = ? and module_id = ?";
        base = new SchedulingDao();
        return base.execute(sql, orderId, moduleId);
    }
}