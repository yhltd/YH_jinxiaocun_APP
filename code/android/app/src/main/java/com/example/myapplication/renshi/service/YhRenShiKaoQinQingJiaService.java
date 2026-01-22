// YhRenShiQingJiaShenPiService.java
package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinQingJia;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class YhRenShiKaoQinQingJiaService {

    private renshiBaseDao base;

    public List<YhRenShiKaoQinQingJia> getList(String company, String name) {
        // 使用正确的字段名：jzqingjiashijan
        String sql = "select * from gongzi_qingjiashenpi where gongsi = ? and xingming = ? order by tijiaoshijian desc";
        base = new renshiBaseDao();
        List<YhRenShiKaoQinQingJia> list = base.query(YhRenShiKaoQinQingJia.class, sql, company.replace("_hr", ""), name);
        return list;
    }

    /**
     * 条件查询（按时间段）
     */
    public List<YhRenShiKaoQinQingJia> getList(String company, String name, String start_date, String stop_date) {
        String sql = "select * from gongzi_qingjiashenpi where gongsi = ? and xingming = ? and convert(date,qsqingjiashijian) >= convert(date,?) and convert(date,jzqingjiashijian) <= convert(date,?) order by tijiaoshijian desc";
        base = new renshiBaseDao();
        List<YhRenShiKaoQinQingJia> list = base.query(YhRenShiKaoQinQingJia.class, sql, company.replace("_hr", ""), name, start_date, stop_date);
        return list;
    }

    public boolean insert(YhRenShiKaoQinQingJia YhRenShiQingJiaShenPi) {
        // 使用正确的字段名：jzqingjiashijan（少一个"n"）
        String sql = "insert into gongzi_qingjiashenpi(bumen, xingming, tijiaoshijian, qsqingjiashijian, jzqingjiashijan, qingjiayuanyin, zhuangtai, shenpiyuanyin, gongsi) " +
                "values(?,?,?,?,?,?,?,?,?)";
        base = new renshiBaseDao();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());

        System.out.println("DEBUG insert请假: ");
        System.out.println("  表名: gongzi_qingjiashenpi");
        System.out.println("  字段: jzqingjiashijan (注意：少一个n)");
        System.out.println("  bumen: " + YhRenShiQingJiaShenPi.getBumen());
        System.out.println("  xingming: " + YhRenShiQingJiaShenPi.getXingming());
        System.out.println("  qsqingjiashijian: " + YhRenShiQingJiaShenPi.getQsqingjiashijian());
        System.out.println("  jzqingjiashijan: " + YhRenShiQingJiaShenPi.getJzqingjiashijian());
        System.out.println("  qingjiayuanyin: " + YhRenShiQingJiaShenPi.getQingjiayuanyin());
        System.out.println("  gongsi: " + YhRenShiQingJiaShenPi.getGongsi());

        try {
            long result = base.executeOfId(sql,
                    YhRenShiQingJiaShenPi.getBumen(),
                    YhRenShiQingJiaShenPi.getXingming(),
                    currentTime,
                    YhRenShiQingJiaShenPi.getQsqingjiashijian(),
                    YhRenShiQingJiaShenPi.getJzqingjiashijian(), // 这里实体类getter名字可以不变
                    YhRenShiQingJiaShenPi.getQingjiayuanyin(),
                    "待审批", // 默认状态
                    "", // 审批原因初始为空
                    YhRenShiQingJiaShenPi.getGongsi());

            System.out.println("DEBUG insert请假结果: " + (result > 0));
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG insert请假异常: " + e.getMessage());
            System.out.println("DEBUG SQL: " + sql);
            return false;
        }
    }

    /**
     * 修改请假申请
     */
    public boolean update(YhRenShiKaoQinQingJia YhRenShiKaoQinQingJia) {
        String sql = "update gongzi_qingjiashenpi set " +
                "bumen=?, xingming=?, qsqingjiashijian=?, jzqingjiashijian=?, " +
                "qingjiayuanyin=?, zhuangtai=?, shenpiyuanyin=? where id=?";
        base = new renshiBaseDao();

        boolean result = base.execute(sql,
                YhRenShiKaoQinQingJia.getBumen(),
                YhRenShiKaoQinQingJia.getXingming(),
                YhRenShiKaoQinQingJia.getQsqingjiashijian(),
                YhRenShiKaoQinQingJia.getJzqingjiashijian(),
                YhRenShiKaoQinQingJia.getQingjiayuanyin(),
                YhRenShiKaoQinQingJia.getZhuangtai(),
                YhRenShiKaoQinQingJia.getShenpiyuanyin(),
                YhRenShiKaoQinQingJia.getId());

        return result;
    }

    /**
     * 删除请假申请
     */
    public boolean delete(int id) {
        String sql = "delete from gongzi_qingjiashenpi where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }
}