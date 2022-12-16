package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.PaibanInfo;

import java.util.List;

public class PaibanInfoService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<PaibanInfo> getList(String company, String department, String plan) {
        base = new SchedulingDao();
        String sql = "select * from paibanbiao_info where remarks1=? and department_name like '%' + ? + '%' and plan_name like '%' + ? + '%' ";
        List<PaibanInfo> list = base.query(PaibanInfo.class, sql, company, department, plan);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(PaibanInfo paibanInfo) {
        String sql = "insert into paibanbiao_info(paibanbiao_detail_id,riqi,renshu,plan_name,department_name,remarks1,remarks2) values(?,?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, paibanInfo.getPaibanbiao_detail_id(), paibanInfo.getRiqi(), paibanInfo.getRenshu(), paibanInfo.getPlan_name(), paibanInfo.getDepartment_name(), paibanInfo.getRemarks1(), paibanInfo.getRemarks2());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(PaibanInfo paibanInfo) {
        String sql = "update paibanbiao_info set riqi=?,plan_name=?,renshu=?,department_name=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, paibanInfo.getRiqi(), paibanInfo.getPlan_name(), paibanInfo.getRenshu(), paibanInfo.getDepartment_name(), paibanInfo.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(String paibanbiao_detail_id) {
        String sql = "delete from paibanbiao_info where paibanbiao_detail_id = ?";
        base = new SchedulingDao();
        return base.execute(sql, paibanbiao_detail_id);
    }

}
