package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.PaibanDetail;

import java.util.List;

public class PaibanDetailService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<PaibanDetail> getList(String company, String name, String banci) {
        base = new SchedulingDao();
        String sql = "select * from paibanbiao_detail where company=? and staff_name like '%' + ? + '%' and banci like '%' + ? + '%' ";
        List<PaibanDetail> list = base.query(PaibanDetail.class, sql, company, name, banci);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(PaibanDetail paibanDetail) {
        String sql = "insert into paibanbiao_detail(staff_name,phone_number,banci,department_name,id_number,company,b,c,e,f) values(?,?,?,?,?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, paibanDetail.getStaff_name(), paibanDetail.getPhone_number(), paibanDetail.getBanci(), paibanDetail.getDepartment_name(), paibanDetail.getId_number(), paibanDetail.getCompany(), paibanDetail.getB(), paibanDetail.getC(), paibanDetail.getE(), paibanDetail.getF());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(PaibanDetail paibanDetail) {
        String sql = "update paibanbiao_detail set staff_name=?,phone_number=?,banci=?,department_name=?,id_number=?,company=?,b=?,c=?,e=?,f=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, paibanDetail.getStaff_name(), paibanDetail.getPhone_number(), paibanDetail.getBanci(), paibanDetail.getDepartment_name(), paibanDetail.getId_number(), paibanDetail.getCompany(), paibanDetail.getB(), paibanDetail.getC(), paibanDetail.getE(), paibanDetail.getF(), paibanDetail.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from paibanbiao_detail where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }

    /**
     * 删除
     */
    public boolean deleteByDetailId(String e) {
        String sql = "delete from paibanbiao_detail where e = ?";
        base = new SchedulingDao();
        return base.execute(sql, e);
    }
}
