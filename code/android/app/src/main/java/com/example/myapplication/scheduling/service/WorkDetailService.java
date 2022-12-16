package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;

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






}
