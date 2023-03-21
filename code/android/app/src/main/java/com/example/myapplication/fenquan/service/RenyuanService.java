package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Renyuan;

import java.util.ArrayList;
import java.util.List;

public class RenyuanService {
    private FenquanDao base;

    public Renyuan login(String username, String password, String company) {
        String sql = "select * from baitaoquanxian_renyun where D = ? and E = ? and B = ? ";
        base = new FenquanDao();
        List<Renyuan> list = base.query(Renyuan.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<String> getCompany() {
        String sql = "select B from baitaoquanxian_renyun group by B";
        base = new FenquanDao();
        List<Renyuan> list = base.query(Renyuan.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getB());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    public List<Renyuan> getList(String company, String c) {
        String sql = "select * from baitaoquanxian_renyun where B=? and C like '%'+ ? +'%' ";
        base = new FenquanDao();
        List<Renyuan> list = base.query(Renyuan.class, sql, company, c);
        return list;
    }

    public boolean insert(Renyuan renyuan) {
        String sql = "insert into baitaoquanxian_renyun(B,C,D,E,renyuan_id,zhuangtai,email,phone,bianhao,bumen) values(?,?,?,?,?,?,?,?,?,?)";
        base = new FenquanDao();
        boolean result = base.execute(sql, renyuan.getB(), renyuan.getC(), renyuan.getD(), renyuan.getE(), renyuan.getRenyuan_id(), renyuan.getZhuangtai(), renyuan.getEmail(), renyuan.getPhone(), renyuan.getBianhao(), renyuan.getBumen());
        return result;
    }

    public boolean update(Renyuan renyuan) {
        String sql = "update baitaoquanxian_renyun set B=?,C=?,D=?,E=?,renyuan_id=?,zhuangtai=?,email=?,phone=?,bianhao=?,bumen=? where id=? ";
        base = new FenquanDao();
        boolean result = base.execute(sql, renyuan.getB(), renyuan.getC(), renyuan.getD(), renyuan.getE(), renyuan.getRenyuan_id(), renyuan.getZhuangtai(), renyuan.getEmail(), renyuan.getPhone(), renyuan.getBianhao(), renyuan.getBumen(), renyuan.getId());
        return result;
    }

    public boolean delete(int id) {
        String sql = "delete from baitaoquanxian_renyun where id = ?";
        base = new FenquanDao();
        return base.execute(sql, id);
    }
}
