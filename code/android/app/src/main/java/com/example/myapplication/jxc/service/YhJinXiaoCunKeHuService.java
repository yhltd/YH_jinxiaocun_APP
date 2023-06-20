package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;

import java.util.List;

public class YhJinXiaoCunKeHuService {
    private JxcBaseDao base;

    /**
     * 查询全部客户数据
     */
    public List<YhJinXiaoCunKeHu> getListByKehu(String company, String beizhu) {
        String sql = "select * from yh_jinxiaocun_chuhuofang where gongsi = ? and beizhu like '%' ? '%' ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunKeHu> list = base.query(YhJinXiaoCunKeHu.class, sql, company, beizhu);
        return list;
    }

    /**
     * 新增客户
     */
    public boolean insertByKehu(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
        String sql = "insert into yh_jinxiaocun_chuhuofang (beizhu,lianxidizhi,lianxifangshi,gongsi) values(?,?,?,?)";
        base = new JxcBaseDao();
        long result = base.executeOfId(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.getGongsi());
        return result > 0;
    }

    /**
     * 修改客户
     */
    public boolean updateByKehu(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
        String sql = "update yh_jinxiaocun_chuhuofang set beizhu=?,lianxidizhi=?,lianxifangshi=? where _id=? ";
        base = new JxcBaseDao();
        boolean result = base.execute(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.get_id());
        return result;
    }

    /**
     * 删除客户
     */
    public boolean deleteByKehu(int id) {
        String sql = "delete from yh_jinxiaocun_chuhuofang where _id = ?";
        base = new JxcBaseDao();
        return base.execute(sql, id);
    }

    /**
     * 查询全部供应商数据
     */
    public List<YhJinXiaoCunKeHu> getListByGys(String company, String beizhu) {
        String sql = "select * from yh_jinxiaocun_jinhuofang where gongsi = ? and beizhu like '%' ? '%' ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunKeHu> list = base.query(YhJinXiaoCunKeHu.class, sql, company, beizhu);
        return list;
    }

    /**
     * 新增供应商
     */
    public boolean insertByGys(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
        String sql = "insert into yh_jinxiaocun_jinhuofang (beizhu,lianxidizhi,lianxifangshi,gongsi) values(?,?,?,?)";
        base = new JxcBaseDao();
        long result = base.executeOfId(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.getGongsi());
        return result > 0;
    }

    /**
     * 修改供应商
     */
    public boolean updateByGys(YhJinXiaoCunKeHu yhJinXiaoCunKeHu) {
        String sql = "update yh_jinxiaocun_jinhuofang set beizhu=?,lianxidizhi=?,lianxifangshi=? where _id=? ";
        base = new JxcBaseDao();
        boolean result = base.execute(sql, yhJinXiaoCunKeHu.getBeizhu(), yhJinXiaoCunKeHu.getLianxidizhi(), yhJinXiaoCunKeHu.getLianxifangshi(), yhJinXiaoCunKeHu.get_id());
        return result;
    }

    /**
     * 删除供应商
     */
    public boolean deleteByGys(int id) {
        String sql = "delete from yh_jinxiaocun_jinhuofang where _id = ?";
        base = new JxcBaseDao();
        return base.execute(sql, id);
    }

}
