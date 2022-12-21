package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.entity.WorkModule;

import java.util.List;

public class WorkDetailService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<WorkDetail> getList(String company, String orderId) {
        base = new SchedulingDao();
        String sql = "select wd.id,wd.orderId as order_number,wd.work_num,wd.work_start_date,wd.company,wd.row_num,wd.is_insert,wd.type,module_id from (select row_number() over(order by wd.id) as rownum,wd.*,oi.order_id as orderId from work_detail as wd left join order_info as oi on wd.order_id = oi.id) as wd left join work_module wm on wd.id= wm.work_id where wd.company = ? and wd.orderId like '%' + ? + '%' order by wd.row_num,wd.is_insert asc ";
        List<WorkDetail> list = base.query(WorkDetail.class, sql, company, orderId);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(WorkDetail workDetail) {
        String sql = "insert into work_detail(order_id,work_num,work_start_date,company,is_insert,type) values(?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, workDetail.getOrder_id(), workDetail.getWork_num(), workDetail.getWork_start_date(), workDetail.getCompany(), workDetail.getIs_insert(), workDetail.getType());
        return result > 0;
    }

    /**
     * 新增
     */
    public boolean insertModule(WorkModule workModule) {
        String sql = "insert into work_module(work_id,module_id) values(?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, workModule.getWork_id(), workModule.getModule_id());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(WorkDetail workDetail) {
        String sql = "update work_detail set row_num=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, workDetail.getRow_num(), workDetail.getId());
        return result;
    }

    /**
     * 刷新
     */
    public List<WorkDetail> getLastList(String company) {
        base = new SchedulingDao();
        String sql = "select top 1 * from work_detail where company=? order by id desc";
        List<WorkDetail> list = base.query(WorkDetail.class, sql, company);
        return list;
    }

    /**
     * 删除
     */
    public boolean delete(String order_id, String company) {
        String sql = "delete from work_detail where order_id =(select top 1 id from order_info where order_id=? ) and company=? ";
        base = new SchedulingDao();
        return base.execute(sql, order_id, company);
    }

}
