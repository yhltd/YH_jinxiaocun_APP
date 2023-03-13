package com.example.myapplication.mendian.service;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianUser;

import java.util.ArrayList;
import java.util.List;

public class YhMendianUserService {
    private MendianDao base;

    public YhMendianUser login(String username, String password, String company) {
        String sql = "select * from users where account = ? and password = ? and company = ? ";
        base = new MendianDao();
        List<YhMendianUser> list = base.query(YhMendianUser.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<String> getCompany() {
        String sql = "select company from users group by company";
        base = new MendianDao();
        List<YhMendianUser> list = base.query(YhMendianUser.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getCompany());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    /**
     * 查询全部数据
     */
    public List<YhJinXiaoCunUser> getList(String company, String name) {
        String sql = "select * from yh_jinxiaocun_user where gongsi = ? and `name` like '%' ? '%' order by _id";
        base = new MendianDao();
        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, company, name);
        return list;
    }

    /**
     * 新增
     * @param yhMendianUser
     */
    public boolean insert(YhMendianUser yhMendianUser) {
        String sql = "insert into yhMendianUser (recipient,cardholder,drawee,issuing_bank,bill_day,repayment_date,total,repayable,balance,loan,service_charge,telephone,`password`,staff) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        base = new MendianDao();
        long result = base.executeOfId(sql, yhMendianUser.getRecipient(), yhMendianUser.getCardholder(), yhMendianUser.getDrawee(), yhMendianUser.getIssuing_bank(), yhMendianUser.getBill_day(), yhMendianUser.getRepayment_date(),yhMendianUser.getTotal(), yhMendianUser.getRepayable(), yhMendianUser.getBalance(),yhMendianUser.getLoan(),yhMendianUser.getService_charge(),yhMendianUser.getTelephone(),yhMendianUser.getPassword(),yhMendianUser.getStaff());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhMendianUser yhMendianUser) {
        String sql = "update customer set recipient=?,cardholder=?,drawee=?,issuing_bank=?,bill_day=?,repayment_date=?,total=?,repayable=?,balance=?,loan=?,service_charge=?,telephone=?,password=?,staff=? where id=? ";

        base = new MendianDao();
        boolean result = base.execute(sql, yhMendianUser.getRecipient(), yhMendianUser.getCardholder(), yhMendianUser.getDrawee(), yhMendianUser.getIssuing_bank(), yhMendianUser.getBill_day(), yhMendianUser.getRepayment_date(),yhMendianUser.getTotal(), yhMendianUser.getRepayable(), yhMendianUser.getBalance(),yhMendianUser.getLoan(),yhMendianUser.getService_charge(),yhMendianUser.getTelephone(),yhMendianUser.getPassword(),yhMendianUser.getStaff(),yhMendianUser.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(String id) {
        String sql = "delete from yh_jinxiaocun_user where _id = ?";
        base = new MendianDao();
        return base.execute(sql, id);
    }

    /**
     * 判断ID是否重复
     */
    public List<YhJinXiaoCunUser> getListById(String id) {
        String sql = "select * from yh_jinxiaocun_user where _id = ? ";
        base = new MendianDao();
        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, id);
        return list;
    }
}
