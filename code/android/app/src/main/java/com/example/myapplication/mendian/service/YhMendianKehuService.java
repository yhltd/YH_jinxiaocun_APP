package com.example.myapplication.mendian.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;
import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianKeHu;

import java.util.List;

public class YhMendianKehuService {
    private MendianDao base;

    /**
     * 查询全部客户数据
     */
    public List<YhMendianKeHu> getList( String gongsi,String shouka, String fukuan, String chika) {
        String sql = "select * from customer where gongsi = ? and recipient like '%' ? '%' and cardholder like '%' ? '%' and drawee like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianKeHu> list = base.query(YhMendianKeHu.class, sql, gongsi, shouka,fukuan,chika);
        return list;
    }

    /**
     * 新增客户
     */
    public boolean insertByKehu(YhMendianKeHu yhMendianKeHu) {
        String sql = "insert into customer (recipient,cardholder,drawee,issuing_bank,bill_day,repayment_date,total,repayable,balance,loan,service_charge,telephone,password,staff,gongsi) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql, yhMendianKeHu.getRecipient(), yhMendianKeHu.getCardholder(), yhMendianKeHu.getDrawee(), yhMendianKeHu.getIssuing_bank(), yhMendianKeHu.getBill_day(), yhMendianKeHu.getRepayment_date(), yhMendianKeHu.getTotal(), yhMendianKeHu.getRepayable(), yhMendianKeHu.getBalance(), yhMendianKeHu.getLoan(), yhMendianKeHu.getService_charge(), yhMendianKeHu.getTelephone(), yhMendianKeHu.getStaff(), yhMendianKeHu.getGongsi());
        return result > 0;
    }

    /**
     * 修改客户
     */
    public boolean updateByKehu(YhMendianKeHu yhMendianKeHu) {
        String sql = "update customer set recipient=?,cardholder=?,drawee=?,issuing_bank=?,bill_day=?,repayment_date=?,total=?,repayable=?,balance=?,loan=?,service_charge=?,telephone=?,password=?,staff=? where id=? ";
        base = new MendianDao();
        boolean result = base.execute(sql, yhMendianKeHu.getRecipient(), yhMendianKeHu.getCardholder(), yhMendianKeHu.getDrawee(), yhMendianKeHu.getIssuing_bank(), yhMendianKeHu.getBill_day(), yhMendianKeHu.getRepayment_date(), yhMendianKeHu.getTotal(), yhMendianKeHu.getRepayable(), yhMendianKeHu.getBalance(), yhMendianKeHu.getLoan(), yhMendianKeHu.getService_charge(), yhMendianKeHu.getTelephone(), yhMendianKeHu.getStaff(), yhMendianKeHu.getId());
        return result;
    }

    /**
     * 删除客户
     */
    public boolean deleteByKehu(int id) {
        String sql = "delete from customer where id = ?";
        base = new MendianDao();
        return base.execute(sql, id);
    }

}
