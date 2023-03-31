package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianMemberlevel;

import java.util.List;

public class YhMendianMemberlevelService {
    private MendianDao base;

    /**
     * 查询全部等级数据
     */
    public List<YhMendianMemberlevel> getList(String jibie, String company) {
        String sql = "select * from member_jibie where company = ? and jibie like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianMemberlevel> list = base.query(YhMendianMemberlevel.class, sql, company, jibie);
        return list;
    }

    /**
     * 新增等级
     */
    public boolean insertBylevel(YhMendianMemberlevel yhMendianMemberlevel) {
        String sql = "insert into member_jibie (company,jibie,menkan,bili) values(?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql, yhMendianMemberlevel.getCompany() ,yhMendianMemberlevel.getJibie(), yhMendianMemberlevel.getMenkan(), yhMendianMemberlevel.getBili());
        return result > 0;
    }

    /**
     * 修改等级
     */
    public boolean updateBylevel(YhMendianMemberlevel yhMendianMemberlevel) {
        String sql = "update member_jibie set jibie=?,menkan=?,bili=? where id=? ";
        base = new MendianDao();
        boolean result = base.execute(sql,yhMendianMemberlevel.getJibie(), yhMendianMemberlevel.getMenkan(), yhMendianMemberlevel.getBili(), yhMendianMemberlevel.getId());
        return result;
    }

    /**
     * 删除等级
     */
    public boolean deleteBylevel(int id) {
        String sql = "delete from member_jibie where id = ?";
        base = new MendianDao();
        return base.execute(sql, id);
    }
}
