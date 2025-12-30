package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceGongZiMingXi;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;

import java.util.List;

public class YhFinanceGongZiMingXiService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceGongZiMingXi> getList(String company, String renming, String start_date, String stop_date) {
        String sql = "select a.id,a.company,a.renming,a.shijian as riqi,a.yinhangzhanghu,a.koukuan,a.gongzi,a.yifu,a.beizhu from (select row_number() over(order by id) as rownum,* from gongzimingxi where company = ? and renming like '%'+ ? +'%') as a where a.shijian >= ? and shijian <= ?";
        base = new financeBaseDao();
        List<YhFinanceGongZiMingXi> list = base.query(YhFinanceGongZiMingXi.class, sql,company,renming,start_date,stop_date);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceGongZiMingXi YhFinanceGongZiMingXi) {
        String sql = "insert into gongzimingxi(company,renming,shijian,yinhangzhanghu,koukuan,gongzi,yifu,beizhu) values(?,?,?,?,?,?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceGongZiMingXi.getCompany(), YhFinanceGongZiMingXi.getRenming(),YhFinanceGongZiMingXi.getRiqi(),YhFinanceGongZiMingXi.getYinhangzhanghu(),YhFinanceGongZiMingXi.getKoukuan(),YhFinanceGongZiMingXi.getGongzi(),YhFinanceGongZiMingXi.getYifu(),YhFinanceGongZiMingXi.getBeizhu());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceGongZiMingXi YhFinanceGongZiMingXi) {
        String sql = "update gongzimingxi set company=?,renming=?,shijian=?,yinhangzhanghu=?,koukuan=?,gongzi=?,yifu=?,beizhu=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceGongZiMingXi.getCompany(), YhFinanceGongZiMingXi.getRenming(),YhFinanceGongZiMingXi.getRiqi(),YhFinanceGongZiMingXi.getYinhangzhanghu(),YhFinanceGongZiMingXi.getKoukuan(),YhFinanceGongZiMingXi.getGongzi(),YhFinanceGongZiMingXi.getYifu(),YhFinanceGongZiMingXi.getBeizhu(),YhFinanceGongZiMingXi.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from gongzimingxi where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
