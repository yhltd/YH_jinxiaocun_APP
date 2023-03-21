package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianMemberlevel;
import com.example.myapplication.mendian.entity.YhMendianOrders;

import java.util.List;

public class YhMendianOrdersService {
    private MendianDao base;

    /**
     * 查询全部等级数据
     */
    public List<YhMendianOrders> getList(String ddh,String syy,String hyxm,String startdate,String enddate, String company) {
        String sql = "select * from orders where company = ? and ddh like '%' ? '%' and ddh like '%' ? '%' and syy like '%' ? '%' and hyxm like '%' ? '%' and riqi>=? and riqi <= ? ";
        base = new MendianDao();
        List<YhMendianOrders> list = base.query(YhMendianOrders.class, sql, company, ddh,syy,hyxm,startdate,enddate);
        return list;
    }

    /**
     * 新增等级
     */
    public boolean insertByOrders(YhMendianOrders yhMendianOrders) {
        String sql = "insert into orders (riqi,ddh,hyzh,hyxm,yhfa,xfje,ssje,yhje,syy,company) values(?,?,?,?,?,?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql, yhMendianOrders.getRiqi(), yhMendianOrders.getDdh(), yhMendianOrders.getHyzh(), yhMendianOrders.getHyxm(), yhMendianOrders.getYhfa(), yhMendianOrders.getXfje(),yhMendianOrders.getSyy(), yhMendianOrders.getCompany());
        return result > 0;
    }

    /**
     * 修改等级
     */
    public boolean updateByoOrders(YhMendianOrders yhMendianOrders) {
        String sql = "update orders set riqi=?,ddh=?,hyzh=?,hyxm=?,yhfa=?,syy=? where id=? ";
        base = new MendianDao();
        boolean result = base.execute(sql,yhMendianOrders.getRiqi(), yhMendianOrders.getDdh(), yhMendianOrders.getHyzh(), yhMendianOrders.getHyxm(), yhMendianOrders.getYhfa(), yhMendianOrders.getSyy(), yhMendianOrders.getId());
        return result;
    }

    /**
     * 删除等级
     */
    public boolean deleteByOrders(int id) {
        String sql = "delete from orders where id = ?";
        base = new MendianDao();
        return base.execute(sql, id);
    }
}
