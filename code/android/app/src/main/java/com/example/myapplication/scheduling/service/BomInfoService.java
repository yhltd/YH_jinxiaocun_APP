package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.BomInfo;

import java.util.List;

public class BomInfoService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<BomInfo> getList(String company, String code, String name, String type) {
        base = new SchedulingDao();
        String sql = "select * from (select b.id,b.code,b.name,b.type,b.norms,b.comment,b.[size],b.unit,isnull((select sum(o.use_num*oi.set_num) from order_bom as o left join order_info as oi on o.order_id = oi.id where o.bom_id = b.id), 0) as use_num from bom_info as b where b.company = ? and code like '%' + ? + '%'  and name like '%' + ? + '%' and type like '%' + ? + '%') as bom ";
        List<BomInfo> list = base.query(BomInfo.class, sql, company, code, name, type);
        return list;
    }

    /**
     * 刷新
     */
    public List<BomInfo> getAddList(String company) {
        base = new SchedulingDao();
        String sql = "select * from bom_info where company=?";
        List<BomInfo> list = base.query(BomInfo.class, sql, company);
        return list;
    }

    /**
     * 刷新
     */
    public List<BomInfo> getUpdOrderBom(int id) {
        base = new SchedulingDao();
        String sql = "select bi.id,code,name,comment,use_num from bom_info bi left join order_bom ob on ob.bom_id=bi.id where order_id=?";
        List<BomInfo> list = base.query(BomInfo.class, sql, id);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(BomInfo bomInfo) {
        String sql = "insert into bom_info(code,name,norms,comment,unit,[size],company,type) values(?,?,?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, bomInfo.getCode(), bomInfo.getName(), bomInfo.getNorms(), bomInfo.getComment(), bomInfo.getUnit(), bomInfo.getSize(), bomInfo.getCompany(), bomInfo.getType());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(BomInfo bomInfo) {
        String sql = "update bom_info set code=?,name=?,norms=?,comment=?,unit=?,[size]=?,company=?,type=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, bomInfo.getCode(), bomInfo.getName(), bomInfo.getNorms(), bomInfo.getComment(), bomInfo.getUnit(), bomInfo.getSize(), bomInfo.getCompany(), bomInfo.getType(), bomInfo.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from bom_info where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }

}
