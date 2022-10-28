package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunUserService {
    private JxcBaseDao base;

    public YhJinXiaoCunUser login(String username, String password, String company) {
        String sql = "select * from yh_jinxiaocun_user where `name` = ? and `password` = ? and gongsi = ? ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<String> getCompany() {
        String sql = "select gongsi from yh_jinxiaocun_user group by gongsi";
        base = new JxcBaseDao();
        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getGongsi());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    /**
     * 查询全部数据
     */
    public List<YhJinXiaoCunUser> getList(String company, String name) {
        String sql = "select * from yh_jinxiaocun_user where gongsi = ? and `name` like '%' ? '%' order by _id";
        base = new JxcBaseDao();
        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, company, name);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhJinXiaoCunUser yhJinXiaoCunUser) {
        String sql = "insert into yh_jinxiaocun_user (_id,AdminIS,Btype,gongsi,`name`,`password`) values(?,?,?,?,?,?)";

        base = new JxcBaseDao();
        long result = base.executeOfId(sql, yhJinXiaoCunUser.get_id(), yhJinXiaoCunUser.getAdminis(), yhJinXiaoCunUser.getBtype(), yhJinXiaoCunUser.getGongsi(), yhJinXiaoCunUser.getName(), yhJinXiaoCunUser.getPassword());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhJinXiaoCunUser yhJinXiaoCunUser) {
        String sql = "update yh_jinxiaocun_user set AdminIS=?,Btype=?,gongsi=?,`name`=?,`password`=? where _id=? ";

        base = new JxcBaseDao();
        boolean result = base.execute(sql, yhJinXiaoCunUser.getAdminis(), yhJinXiaoCunUser.getBtype(), yhJinXiaoCunUser.getGongsi(), yhJinXiaoCunUser.getName(), yhJinXiaoCunUser.getPassword(), yhJinXiaoCunUser.get_id());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(String id) {
        String sql = "delete from yh_jinxiaocun_user where _id = ?";
        base = new JxcBaseDao();
        return base.execute(sql, id);
    }

    /**
     * 判断ID是否重复
     */
    public List<YhJinXiaoCunUser> getListById(String id) {
        String sql = "select * from yh_jinxiaocun_user where _id = ? ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunUser> list = base.query(YhJinXiaoCunUser.class, sql, id);
        return list;
    }


}
