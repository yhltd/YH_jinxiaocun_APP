package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;

import java.util.List;

//极简配置-客户
public class YhFinanceKehuPeizhiService {
    private financeBaseDao base;
    /**
     * 查询全部数据
     */
    public List<YhFinanceJiJianPeiZhi> getList(String company) {
        String sql = "select id,kehu as peizhi,company from KehuPeizhi where company = ?";
        base = new financeBaseDao();
        List<YhFinanceJiJianPeiZhi> list = base.query(YhFinanceJiJianPeiZhi.class, sql, company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceJiJianPeiZhi YhFinanceJiJianPeiZhi) {
        String sql = "insert into KehuPeizhi(kehu,company) values(?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceJiJianPeiZhi.getPeizhi(), YhFinanceJiJianPeiZhi.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceJiJianPeiZhi YhFinanceJiJianPeiZhi) {
        String sql = "update KehuPeizhi set kehu=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceJiJianPeiZhi.getPeizhi(), YhFinanceJiJianPeiZhi.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from KehuPeizhi where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
