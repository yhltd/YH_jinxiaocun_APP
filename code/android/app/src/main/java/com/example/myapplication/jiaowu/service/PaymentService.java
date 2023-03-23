package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.Payment;
import com.example.myapplication.jiaowu.entity.TeacherInfo;

import java.util.List;

public class PaymentService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<Payment> getList(String company, String start_date, String stop_date, String student_name) {
        String sql = "select * from payment where Company = ? and ksdate >= ? and ksdate <= ? and realname like '%' + ? + '%'";
        base = new JiaowuBaseDao();
        List<Payment> list = base.query(Payment.class, sql, company,start_date,stop_date,student_name);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(Payment payment) {
        String sql = "insert into payment(ksdate,realname,paid,money,paiment,keeper,remark,Company) values(?,?,?,?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, payment.getKsdate(), payment.getRealname(), payment.getPaid(), payment.getMoney(), payment.getPaiment(), payment.getKeeper(), payment.getRemark(),payment.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(Payment payment) {
        String sql = "update payment set ksdate=?,realname=?,paid=?,money=?,paiment=?,keeper=?,remark=? where id=? ";
        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, payment.getKsdate(), payment.getRealname(), payment.getPaid(), payment.getMoney(), payment.getPaiment(), payment.getKeeper(), payment.getRemark(),payment.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from student where id = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
