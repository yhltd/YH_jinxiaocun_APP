package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.ShengChanXian;
import com.example.myapplication.scheduling.entity.WorkDetailDTO;

import java.util.List;

public class PaiChanService {
    private SchedulingDao base;

    /**
     * 查询所有生产线（带条件查询）
     */
    public List<WorkDetailDTO> getList(String company) {
        base = new SchedulingDao();
        String sql = "SELECT " +
                "w.id AS id, " +
                "w.work_start_date AS kaishishijian, " +
                "w.is_insert AS charu, " +
                "w.type AS dingdanleixing, " +
                "w.jiezhishijian AS jiezhishijian, " +
                "g.module_num * w.work_num AS gongxushuliang, " +
                "m.name AS gongxumingcheng, " +
                "m.num AS gongxuxiaolv, " +
                "o.order_id AS dingdanhao, " +
                "o.product_name AS dingdanmingcheng " +
                "FROM work_detail w " +
                "JOIN order_gongxu g ON w.order_id = g.order_id " +
                "JOIN module_info m ON g.module_id = m.id " +
                "JOIN order_info o ON w.order_id = o.id " +
                "WHERE w.company = ? " +
                "ORDER BY w.work_start_date DESC, w.row_num ASC";

        List<WorkDetailDTO> list = base.query(WorkDetailDTO.class, sql, company);
        return list;
    }

}