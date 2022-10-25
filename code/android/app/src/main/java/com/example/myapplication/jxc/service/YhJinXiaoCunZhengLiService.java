package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;

import java.util.List;

public class YhJinXiaoCunZhengLiService {
    private JxcBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhJinXiaoCunZhengLi> getList(String company,String name) {
        String sql = "select * from yh_jinxiaocun_zhengli where gs_name = ? ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunZhengLi> list = base.query(YhJinXiaoCunZhengLi.class, sql, company);
        return list;
    }


}
