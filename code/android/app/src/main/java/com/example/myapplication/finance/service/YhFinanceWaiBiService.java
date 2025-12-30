package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceWaiBi;

import java.math.BigDecimal;
import java.util.List;

public class YhFinanceWaiBiService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceWaiBi> getList(String company) {
        String sql = "select * from waibiPeizhi where company = ?";
        base = new financeBaseDao();
        List<YhFinanceWaiBi> list = base.query(YhFinanceWaiBi.class, sql, company);
        return list;
    }

    /**
     * 根据币种名称获取汇率
     */
    public BigDecimal getHuilvByBizhong(String company, String bizhong) {
        if (bizhong == null || bizhong.trim().isEmpty()) {
            return BigDecimal.ONE;
        }

        String sql = "select huilv from waibiPeizhi where company = ? and bizhong = ?";
        base = new financeBaseDao();

        // 使用查询实体类的方法，然后提取汇率
        List<YhFinanceWaiBi> resultList = base.query(YhFinanceWaiBi.class, sql, company, bizhong);

        if (resultList != null && !resultList.isEmpty()) {
            YhFinanceWaiBi waiBi = resultList.get(0);
            try {
                String huilvStr = waiBi.getHuilv();
                if (huilvStr != null && !huilvStr.trim().isEmpty()) {
                    return new BigDecimal(huilvStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return BigDecimal.ONE; // 默认汇率1
    }

    /**
     * 根据币种名称获取币种配置对象
     */
    public YhFinanceWaiBi getByBizhong(String company, String bizhong) {
        if (bizhong == null || bizhong.trim().isEmpty()) {
            return null;
        }

        String sql = "select * from waibiPeizhi where company = ? and bizhong = ?";
        base = new financeBaseDao();
        List<YhFinanceWaiBi> list = base.query(YhFinanceWaiBi.class, sql, company, bizhong);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceWaiBi YhFinanceWaiBi) {
        String sql = "insert into waibiPeizhi(bizhong,huilv,company) values(?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceWaiBi.getBizhong(), YhFinanceWaiBi.getHuilv(), YhFinanceWaiBi.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceWaiBi YhFinanceWaiBi) {
        String sql = "update waibiPeizhi set bizhong=?,huilv=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceWaiBi.getBizhong(), YhFinanceWaiBi.getHuilv(), YhFinanceWaiBi.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from waibiPeizhi where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
