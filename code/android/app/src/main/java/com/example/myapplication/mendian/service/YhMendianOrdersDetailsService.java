package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianOrderDetail;
import com.example.myapplication.mendian.entity.YhMendianOrders;

import java.util.List;

public class YhMendianOrdersDetailsService {
    private MendianDao base;

    /**
     * 新增订单明细
     */
    public boolean insert(YhMendianOrderDetail yhMendianOrderDetail) {
        String sql = "insert into orders_details(ddid,cplx,cpmc,dw,dj,dzbl,zhdj,zhje,gs,company) values(?,?,?,?,?,?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql, yhMendianOrderDetail.getDdid(), yhMendianOrderDetail.getCplx(), yhMendianOrderDetail.getCpmc(), yhMendianOrderDetail.getDw(), yhMendianOrderDetail.getDj(), yhMendianOrderDetail.getDzbl(),yhMendianOrderDetail.getZhdj(), yhMendianOrderDetail.getZhje(), yhMendianOrderDetail.getGs(), yhMendianOrderDetail.getCompany());
        return result > 0;
    }

}
