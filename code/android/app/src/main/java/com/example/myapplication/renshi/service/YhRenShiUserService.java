package com.example.myapplication.renshi.service;

import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiUser;

import java.util.ArrayList;
import java.util.List;

public class YhRenShiUserService {
    private renshiBaseDao base;

    public YhRenShiUser login(String username, String password, String company) {
        String sql = "select * from gongzi_renyuan where I = ? and J = ? and L = ? ";
        base = new renshiBaseDao();
        List<YhRenShiUser> list = base.query(YhRenShiUser.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<String> getCompany() {
        String sql = "select L from gongzi_renyuan group by L";
        base = new renshiBaseDao();
        List<YhRenShiUser> list = base.query(YhRenShiUser.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getL());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    /**
     * 查询全部数据
     */
    public List<YhFinanceUser> getList(String company, String username) {
        String sql = "select * from gongzi_renyuan where L = ? and name like '%'+ ? +'%'";
        base = new renshiBaseDao();
        List<YhFinanceUser> list = base.query(YhFinanceUser.class, sql,company,username);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceUser YhFinanceUser) {
        String sql = "insert into Account(name,pwd,[do],bianhao,company) values(?,?,?,?,?)";
        base = new renshiBaseDao();
        long result = base.executeOfId(sql, YhFinanceUser.getName(), YhFinanceUser.getPwd(),YhFinanceUser.get_do(),YhFinanceUser.getBianhao(),YhFinanceUser.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceUser YhFinanceUser) {
        String sql = "update Account set name=?,pwd=?,[do]=?,bianhao=?,company=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, YhFinanceUser.getName(), YhFinanceUser.getPwd(),YhFinanceUser.get_do(),YhFinanceUser.getBianhao(),YhFinanceUser.getCompany());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from Account where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }
}
