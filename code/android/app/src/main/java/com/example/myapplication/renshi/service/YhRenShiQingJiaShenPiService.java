package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiQingJiaShenPi;

import java.util.List;

public class YhRenShiQingJiaShenPiService {
    private renshiBaseDao base;

    /**
     * 获取请假审批列表
     */
    public List<YhRenShiQingJiaShenPi> getList(String company) {
        String sql = "select * from gongzi_qingjiashenpi where gongsi = ? order by id desc";
        base = new renshiBaseDao();
        return base.query(YhRenShiQingJiaShenPi.class, sql,company.replace("_hr",""));
    }

    /**
     * 根据审批状态筛选查询
     */
    public List<YhRenShiQingJiaShenPi> getListByStatus(String company, String status) {
        List<YhRenShiQingJiaShenPi> result;

        if (status == null || status.isEmpty() || status.equals("全部")) {
            // 查询所有记录
            String sql = "select * from gongzi_qingjiashenpi where gongsi = ? order by id desc";
            base = new renshiBaseDao();
            result = base.query(YhRenShiQingJiaShenPi.class, sql, company.replace("_hr",""));
        } else {
            // 按状态筛选
            String sql = "select * from gongzi_qingjiashenpi where gongsi = ? and zhuangtai = ? order by id desc";
            base = new renshiBaseDao();
            result = base.query(YhRenShiQingJiaShenPi.class, sql, company.replace("_hr",""), status);
        }

        return result;
    }

    /**
     * 新增请假审批记录
     */
    public boolean insert(YhRenShiQingJiaShenPi entity) {
        String sql = "insert into gongzi_qingjiashenpi (gongsi, bumen, xingming, tijiaoshijian, " +
                "qsqingjiashijian, jzqingjiashijan, qingjiayuanyin, zhuangtai, shenpiyuanyin) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        base = new renshiBaseDao();
        return base.execute(sql,
                entity.getGongsi(),
                entity.getBumen(),
                entity.getXingming(),
                entity.getTijiaoshijian(),
                entity.getQsqingjiashijian(),
                entity.getJzqingjiashijan(),
                entity.getQingjiayuanyin(),
                entity.getZhuangtai(),
                entity.getShenpiyuanyin());
    }

    /**
     * 更新请假审批记录
     */
    public boolean update(YhRenShiQingJiaShenPi entity) {
        String sql = "update gongzi_qingjiashenpi set bumen=?, xingming=?, tijiaoshijian=?, " +
                "qsqingjiashijian=?, jzqingjiashijan=?, qingjiayuanyin=?, zhuangtai=?, shenpiyuanyin=? where id=?";
        base = new renshiBaseDao();
        return base.execute(sql,
                entity.getBumen(),
                entity.getXingming(),
                entity.getTijiaoshijian(),
                entity.getQsqingjiashijian(),
                entity.getJzqingjiashijan(),
                entity.getQingjiayuanyin(),
                entity.getZhuangtai(),
                entity.getShenpiyuanyin(),
                entity.getId());
    }

    /**
     * 删除请假审批记录
     */
    public boolean delete(int id) {
        String deletesql = "delete from gongzi_qingjiashenpi where id = ?";
        base = new renshiBaseDao();
        return base.execute(deletesql, id);
    }

    /**
     * 更新审批结果
     */
    public boolean updateShenpijieguo(int id, String zhuangtai, String shenpiyuanyin) {
        String sql = "update gongzi_qingjiashenpi set zhuangtai = ?, shenpiyuanyin = ? where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, zhuangtai, shenpiyuanyin, id);
    }

    /**
     * 获取审批状态选项
     */
    public String[] getZhuangtaiOptions() {
        return new String[]{"待审批", "通过", "驳回"};
    }

    /**
     * 获取筛选状态选项
     */
    public String[] getFilterOptions() {
        return new String[]{"全部", "待审批", "通过", "驳回"};
    }
}