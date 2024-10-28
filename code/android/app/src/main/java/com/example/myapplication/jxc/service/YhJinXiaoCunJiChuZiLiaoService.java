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
    public List<YhJinXiaoCunJiChuZiLiao> getList(String company, String cpname) {
        String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and name like '%' ? '%' order by id";
        base = new JxcBaseDao();
        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
        return list;
    }

    /**
     * 商品代码下拉
     */
    public List<String> getCpid(String company) {
        String sql = "select sp_dm from yh_jinxiaocun_jichuziliao where gs_name = ? group by sp_dm";
        base = new JxcBaseDao();
        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
        List<String> cpidList = new ArrayList<>();
        if (cpidList!=null){
            for (int i = 0; i < list.size(); i++) {
                cpidList.add(list.get(i).getSpDm());
            }
        }
        return cpidList != null && cpidList.size() > 0 ? cpidList : null;
    }
    /**
     * 根据商品代码查询
     */
    public List<YhJinXiaoCunJiChuZiLiao> getListByCpid(String company, String cpid) {
        String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and sp_dm=? ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
        return list;
    }



    /**
     * 新增
     */
    public boolean insert(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
        String sql = "insert into yh_jinxiaocun_jichuziliao(sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";

        base = new JxcBaseDao();
        long result = base.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
        String sql = "update yh_jinxiaocun_jichuziliao set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=? ";

        base = new JxcBaseDao();
        boolean result = base.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from yh_jinxiaocun_jichuziliao where id = ?";
        base = new JxcBaseDao();
        return base.execute(sql, id);
    }


}
