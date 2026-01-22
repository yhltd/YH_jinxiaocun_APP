package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiLiZhiShenPi;

import java.util.List;

public class YhRenShiLiZhiShenPiService {
    private renshiBaseDao base;

    /**
     * 获取离职审批列表
     */
    public List<YhRenShiLiZhiShenPi> getList(String company) {
        String sql = "select * from gongzi_lizhishenpi where gongsi = ? order by id desc";
        base = new renshiBaseDao();
        return base.query(YhRenShiLiZhiShenPi.class, sql, company.replace("_hr",""));
    }

    /**
     * 根据审批结果筛选查询
     */
    public List<YhRenShiLiZhiShenPi> getListByStatus(String company, String status) {
        List<YhRenShiLiZhiShenPi> result;

        if (status == null || status.isEmpty() || status.equals("全部")) {
            // 查询所有记录
            String sql = "select * from gongzi_lizhishenpi where gongsi = ? order by id desc";
            base = new renshiBaseDao();
            result = base.query(YhRenShiLiZhiShenPi.class, sql, company.replace("_hr",""));
        } else {
            // 按状态筛选
            String sql = "select * from gongzi_lizhishenpi where gongsi = ? and shenpijieguo = ? order by id desc";
            base = new renshiBaseDao();
            result = base.query(YhRenShiLiZhiShenPi.class, sql, company.replace("_hr",""), status);
        }

        return result;
    }

    /**
     * 新增离职审批记录
     */
    public boolean insert(YhRenShiLiZhiShenPi entity) {
        String sql = "insert into gongzi_lizhishenpi (gongsi, bumen, xingming, tijiaoriqi, " +
                "shenqingyuanyin, shenpijieguo, shenpiyuanyin) " +
                "values(?, ?, ?, ?, ?, ?, ?)";
        base = new renshiBaseDao();
        return base.execute(sql,
                entity.getGongsi(),
                entity.getBumen(),
                entity.getXingming(),
                entity.getTijiaoriqi(),
                entity.getShenqingyuanyin(),
                entity.getShenpijieguo(),
                entity.getShenpiyuanyin());
    }

    /**
     * 更新离职审批记录
     */
    public boolean update(YhRenShiLiZhiShenPi entity) {
        String sql = "update gongzi_lizhishenpi set bumen=?, xingming=?, tijiaoriqi=?, " +
                "shenqingyuanyin=?, shenpijieguo=?, shenpiyuanyin=? where id=?";
        base = new renshiBaseDao();
        return base.execute(sql,
                entity.getBumen(),
                entity.getXingming(),
                entity.getTijiaoriqi(),
                entity.getShenqingyuanyin(),
                entity.getShenpijieguo(),
                entity.getShenpiyuanyin(),
                entity.getId());
    }

    /**
     * 删除离职审批记录
     */
    public boolean delete(int id) {
        String deletesql = "delete from gongzi_lizhishenpi where id = ?";
        base = new renshiBaseDao();
        return base.execute(deletesql, id);
    }

    /**
     * 更新审批结果
     */
    public boolean updateShenpijieguo(int id, String shenpijieguo, String shenpiyuanyin) {
        String sql = "update gongzi_lizhishenpi set shenpijieguo = ?, shenpiyuanyin = ? where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, shenpijieguo, shenpiyuanyin, id);
    }

    /**
     * 获取审批结果选项
     */
    public String[] getShenpijieguoOptions() {
        return new String[]{"待审批", "通过", "驳回"};
    }
}