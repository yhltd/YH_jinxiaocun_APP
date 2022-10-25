package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
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

}
