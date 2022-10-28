package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;

import java.util.List;

public class YhJinXiaoCunZhengLiService {
    private JxcBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhJinXiaoCunZhengLi> getList(String company, String name) {
        String sql = "select * from yh_jinxiaocun_zhengli where gs_name = ? and name like '%' ? '%' ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunZhengLi> list = base.query(YhJinXiaoCunZhengLi.class, sql, company, name);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhJinXiaoCunZhengLi yhJinXiaoCunZhengLi) {
        String sql = "insert into yh_jinxiaocun_zhengli(sp_dm,name,lei_bie,dan_wei,beizhu,gs_name) values(?,?,?,?,?,?)";

        base = new JxcBaseDao();
        long result = base.executeOfId(sql, yhJinXiaoCunZhengLi.getSpDm(), yhJinXiaoCunZhengLi.getName(), yhJinXiaoCunZhengLi.getLeiBie(), yhJinXiaoCunZhengLi.getDanWei(), yhJinXiaoCunZhengLi.getBeizhu(), yhJinXiaoCunZhengLi.getGsName());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhJinXiaoCunZhengLi yhJinXiaoCunZhengLi) {
        String sql = "update yh_jinxiaocun_zhengli set sp_dm=?,name=?,lei_bie=?,dan_wei=?,beizhu=? where id=? ";

        base = new JxcBaseDao();
        boolean result = base.execute(sql, yhJinXiaoCunZhengLi.getSpDm(), yhJinXiaoCunZhengLi.getName(), yhJinXiaoCunZhengLi.getLeiBie(), yhJinXiaoCunZhengLi.getDanWei(), yhJinXiaoCunZhengLi.getBeizhu(), yhJinXiaoCunZhengLi.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from yh_jinxiaocun_zhengli where id = ?";
        base = new JxcBaseDao();
        return base.execute(sql, id);
    }


}
