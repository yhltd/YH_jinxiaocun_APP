package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunJiChuZiLiaoService {
    private JxcBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhJinXiaoCunJiChuZiLiao> getList(String company) {
        String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
        return list;
    }

    /**
     * 商品下拉
     */
    public List<String> getProduct(String company) {
        String sql = "select `name` from yh_jinxiaocun_jichuziliao where gs_name = ? group by `name` ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
        List<String> productList = new ArrayList<>();
        productList.add("全部");
        for (int i = 0; i < list.size(); i++) {
            productList.add(list.get(i).getName());
        }
        return productList != null && productList.size() > 0 ? productList : null;
    }


}
