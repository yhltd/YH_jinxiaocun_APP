package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiLizhiShenQing;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YhRenShiLizhiShenQingService {
    private renshiBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhRenShiLizhiShenQing> getList(String company, String name, String start_date, String stop_date) {
        String sql = "select * from gongzi_lizhishenpi where gongsi = ? and xingming like '%' + ? + '%' and convert(date,tijiaoriqi) >= convert(date,?) and convert(date,tijiaoriqi) <= convert(date,?)";
        base = new renshiBaseDao();
        List<YhRenShiLizhiShenQing> list = base.query(YhRenShiLizhiShenQing.class, sql, company, name, start_date, stop_date);
        return list;
    }

    /**
     * 获取部门列表
     */
    public List<String> getDepartments(String company) {
        String sql = "SELECT DISTINCT bumen FROM gongzi_peizhi WHERE gongsi = ? AND bumen IS NOT NULL AND bumen != '' ORDER BY bumen";
        base = new renshiBaseDao();
        List<String> departments = new ArrayList<>();

        // 查询返回YhRenShiPeiZhiBiao对象的列表
        List<YhRenShiPeiZhiBiao> results = base.query(YhRenShiPeiZhiBiao.class, sql, company);

        for (YhRenShiPeiZhiBiao item : results) {
            String dept = item.getBumen();
            if (dept != null && !dept.trim().isEmpty()) {
                departments.add(dept);
            }
        }

        return departments;
    }

    /**
     * 新增
     */
    public boolean insert(YhRenShiLizhiShenQing yhRenShiLizhiShenQing) {
        String sql = "insert into gongzi_lizhishenpi(bumen,xingming,tijiaoriqi,shenqingyuanyin,shenpijieguo,shenpiyuanyin,gongsi) values(?,?,?,?,?,?,?)";
        base = new renshiBaseDao();
        long result = base.executeOfId(sql, yhRenShiLizhiShenQing.getBumen(), yhRenShiLizhiShenQing.getXingming(), yhRenShiLizhiShenQing.getTijiaoriqi(), yhRenShiLizhiShenQing.getShenqingyuanyin(), yhRenShiLizhiShenQing.getShenpijieguo(), yhRenShiLizhiShenQing.getShenpiyuanyin(), yhRenShiLizhiShenQing.getGongsi());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhRenShiLizhiShenQing yhRenShiLizhiShenQing) {
        String sql = "update gongzi_lizhishenpi set bumen=?,xingming=?,tijiaoriqi=?,shenqingyuanyin=?,shenpijieguo=?,shenpiyuanyin=?,gongsi=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, yhRenShiLizhiShenQing.getBumen(), yhRenShiLizhiShenQing.getXingming(), yhRenShiLizhiShenQing.getTijiaoriqi(), yhRenShiLizhiShenQing.getShenqingyuanyin(), yhRenShiLizhiShenQing.getShenpijieguo(), yhRenShiLizhiShenQing.getShenpiyuanyin(), yhRenShiLizhiShenQing.getGongsi(), yhRenShiLizhiShenQing.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from gongzi_lizhishenpi where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }

    /**
     * 更新审批结果
     */
    public boolean updateShenpi(int id, String shenpijieguo, String shenpiyuanyin) {
        String sql = "update gongzi_lizhishenpi set shenpijieguo=?,shenpiyuanyin=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, shenpijieguo, shenpiyuanyin, id);
        return result;
    }

    /**
     * 根据ID获取单条记录
     */
    public YhRenShiLizhiShenQing getById(int id) {
        String sql = "select * from gongzi_lizhishenpi where id = ?";
        base = new renshiBaseDao();
        List<YhRenShiLizhiShenQing> list = base.query(YhRenShiLizhiShenQing.class, sql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<YhRenShiLizhiShenQing> getListWithCondition(String company, String name,
                                                        String bumen, String start_date, String stop_date) {
        // 构建SQL：同时查询姓名和部门
        String sql = "select * from gongzi_lizhishenpi where gongsi = ? " +
                "and xingming like '%' + ? + '%' " +
                "and bumen like '%' + ? + '%' " +
                "and convert(date,tijiaoriqi) >= convert(date,?) " +
                "and convert(date,tijiaoriqi) <= convert(date,?)";
        base = new renshiBaseDao();
        return base.query(YhRenShiLizhiShenQing.class, sql, company, name, bumen, start_date, stop_date);
    }
}