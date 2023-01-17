package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiBaoPanShenPi;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;

import java.util.List;

public class YhRenShiBaoPanShenPiService {
    private renshiBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhRenShiBaoPanShenPi> getList(String company,String name,String start_date,String stop_date) {
        String sql = "select * from gongzi_shenpi where gongsi = ? and shenpiren like '%' + ? + '%' and convert(date,riqi) >= convert(date,?) and convert(date,riqi) <= convert(date,?)";
        base = new renshiBaseDao();
        List<YhRenShiBaoPanShenPi> list = base.query(YhRenShiBaoPanShenPi.class, sql,company.replace("_hr",""),name,start_date,stop_date);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhRenShiBaoPanShenPi YhRenShiBaoPanShenPi) {
        String sql = "insert into gongzi_shenpi(shifa_gongzi,geren_zhichu,qiye_zhichu,yuangong_renshu,quanqin_tianshu,shenpiren,riqi,gongsi) values(?,?,?,?,?,?,?,?)";
        base = new renshiBaseDao();
        long result = base.executeOfId(sql, YhRenShiBaoPanShenPi.getShifa_gongzi(), YhRenShiBaoPanShenPi.getGeren_zhichu(),YhRenShiBaoPanShenPi.getQiye_zhichu(),YhRenShiBaoPanShenPi.getYuangong_renshu(),YhRenShiBaoPanShenPi.getQuanqin_tianshu(),YhRenShiBaoPanShenPi.getShenpiren(),YhRenShiBaoPanShenPi.getRiqi(),YhRenShiBaoPanShenPi.getGongsi());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhRenShiBaoPanShenPi YhRenShiBaoPanShenPi) {
        String sql = "update gongzi_shenpi set shifa_gongzi=?,geren_zhichu=?,qiye_zhichu=?,yuangong_renshu=?,quanqin_tianshu=?,shenpiren=?,riqi=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, YhRenShiBaoPanShenPi.getShifa_gongzi(), YhRenShiBaoPanShenPi.getGeren_zhichu(),YhRenShiBaoPanShenPi.getQiye_zhichu(),YhRenShiBaoPanShenPi.getYuangong_renshu(),YhRenShiBaoPanShenPi.getQuanqin_tianshu(),YhRenShiBaoPanShenPi.getShenpiren(),YhRenShiBaoPanShenPi.getRiqi(),YhRenShiBaoPanShenPi.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from gongzi_shenpi where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }
}
