package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.Summary;

import java.util.List;

public class SummaryService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<Summary> getList(String company, String order_number, int typeId) {
        base = new SchedulingDao();
        String sql = "";
        List<Summary> list;
        if (typeId == 0) {
            sql = "select mt.name as type,mi.name as name,mi.num as num,(select name from module_info where id = mi.parent_id) as parent_name,sum(wd.work_num) as work_num from work_module as wm left join module_info as mi on wm.module_id = mi.id left join module_type as mt on mi.type_id = mt.id left join work_detail as wd on wm.work_id = wd.id left join order_info as o on wd.order_id = o.id where work_num is not null and wd.company=? and o.order_id like '%' + ? + '%' group by mt.name,mi.name,mi.num,mi.parent_id ";
            list = base.query(Summary.class, sql, company, order_number);
        } else {
            sql = "select mt.name as type,mi.name as name,mi.num as num,(select name from module_info where id = mi.parent_id) as parent_name,sum(wd.work_num) as work_num from work_module as wm left join module_info as mi on wm.module_id = mi.id left join module_type as mt on mi.type_id = mt.id left join work_detail as wd on wm.work_id = wd.id left join order_info as o on wd.order_id = o.id where work_num is not null and wd.company=? and o.order_id like '%' + ? + '%' and mi.type_id = ? group by mt.name,mi.name,mi.num,mi.parent_id";
            list = base.query(Summary.class, sql, company, order_number, typeId);
        }
        return list;
    }
}
