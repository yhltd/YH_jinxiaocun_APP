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
        String sql = "select id,riqi,ddh,hyzh,hyxm,hyjf,yhfa,heji.xfje,heji.ssje,heji.yhje,syy,ord.company from orders as ord left join(select ddid,company,sum(convert(dj,float) * convert(gs,float)) as xfje,sum(convert(zhdj,float) * convert(gs,float)) as ssje,round(sum(convert(dj,float) * convert(gs,float)) - sum(convert(zhdj,float) * convert(gs,float)),2) as yhje from orders_details group by ddid) as heji on ord.ddh = heji.ddid and ord.company = heji.company  where ord.company = ?  and ddh like '%' ? '%' and ifnull(syy,'') like '%' ? '%' and ifnull(hyxm,'') like '%' ? '%' and riqi>= ? and riqi<= ? order by id desc";
        base = new MendianDao();
        List<YhMendianOrders> list = base.query(YhMendianOrders.class, sql, company, ddh,syy,hyxm,startdate,enddate);
        return list;
    }

    /**
     * 查询当日最大订单号
     */
    public List<YhMendianOrders> getListDDH(String ddh, String company) {
        String sql = "select max(ddh) from orders where company = ? and ddh like ? '%' ";
        base = new MendianDao();
        List<YhMendianOrders> list = base.query(YhMendianOrders.class, sql, company, ddh);
        return list;
    }

    /**
     * 新增等级
     */
    public boolean insertByOrders(YhMendianOrders yhMendianOrders) {
        String sql = "insert into orders (riqi,ddh,hyzh,hyxm,yhfa,xfje,ssje,yhje,syy,company) values(?,?,?,?,?,?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql, yhMendianOrders.getRiqi(), yhMendianOrders.getDdh(), yhMendianOrders.getHyzh(), yhMendianOrders.getHyxm(), yhMendianOrders.getYhfa(), yhMendianOrders.getXfje(), yhMendianOrders.getSsje(), yhMendianOrders.getYhje(),yhMendianOrders.getSyy(), yhMendianOrders.getCompany());
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
