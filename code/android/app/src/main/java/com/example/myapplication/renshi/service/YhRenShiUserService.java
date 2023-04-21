package com.example.myapplication.renshi.service;

import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiUser;

import java.util.ArrayList;
import java.util.List;

public class YhRenShiUserService {
    private renshiBaseDao base;

    public YhRenShiUser login(String username, String password, String company) {
        String sql = "select * from gongzi_renyuan where I = ? and J = ? and L = ? ";
        base = new renshiBaseDao();
        List<YhRenShiUser> list = base.query(YhRenShiUser.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<String> getCompany() {
        String sql = "select L from gongzi_renyuan group by L";
        base = new renshiBaseDao();
        List<YhRenShiUser> list = base.query(YhRenShiUser.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getL());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    /**
     * 查询全部数据
     */
    public List<YhRenShiUser> getList(String company, String username,String phone) {
        String sql = "select * from gongzi_renyuan where L = ? and B like '%'+ ? +'%' and O like '%' + ? + '%'";
        base = new renshiBaseDao();
        List<YhRenShiUser> list = base.query(YhRenShiUser.class, sql,company,username,phone);
        return list;
    }

    /**
     * 查询当天过生日的员工
     */
    public List<YhRenShiUser> birthdayList(String company, String this_date) {
        String sql = "select * from gongzi_renyuan where L = ? and Q = ?";
        base = new renshiBaseDao();
        List<YhRenShiUser> list = base.query(YhRenShiUser.class, sql,company,this_date);
        return list;
    }

    /**
     * 查询当月过生日的员工
     */
    public List<YhRenShiUser> monthBirthdayList(String company, String month) {
        String sql = "SELECT id,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,AA,AB,AC,AD,convert(varchar,day(convert(date,Q))) as Q from gongzi_renyuan where L = ? and month(convert(date,Q))=?";
        base = new renshiBaseDao();
        List<YhRenShiUser> list = base.query(YhRenShiUser.class, sql,company,month);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhRenShiUser YhRenShiUser) {
        String sql = "insert into gongzi_renyuan(B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,AA,AB,AC,AD) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new renshiBaseDao();
        long result = base.executeOfId(sql, YhRenShiUser.getB(), YhRenShiUser.getC(),YhRenShiUser.getD(),YhRenShiUser.getE(),YhRenShiUser.getF(),YhRenShiUser.getG(),YhRenShiUser.getH(),YhRenShiUser.getI(),YhRenShiUser.getJ(),YhRenShiUser.getK(),YhRenShiUser.getL(),YhRenShiUser.getM(),YhRenShiUser.getN(),YhRenShiUser.getO(),YhRenShiUser.getP(),YhRenShiUser.getQ(),YhRenShiUser.getR(),YhRenShiUser.getS(),YhRenShiUser.getT(),YhRenShiUser.getU(),YhRenShiUser.getV(),YhRenShiUser.getW(),YhRenShiUser.getX(),YhRenShiUser.getY(),YhRenShiUser.getZ(),YhRenShiUser.getAa(),YhRenShiUser.getAb(),YhRenShiUser.getAc(),YhRenShiUser.getAd());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhRenShiUser YhRenShiUser) {
        String sql = "update gongzi_renyuan set B=?,C=?,D=?,E=?,F=?,G=?,H=?,I=?,J=?,K=?,L=?,M=?,N=?,O=?,P=?,Q=?,R=?,S=?,T=?,U=?,V=?,W=?,X=?,Y=?,Z=?,AA=?,AB=?,AC=?,AD=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, YhRenShiUser.getB(), YhRenShiUser.getC(),YhRenShiUser.getD(),YhRenShiUser.getE(),YhRenShiUser.getF(),YhRenShiUser.getG(),YhRenShiUser.getH(),YhRenShiUser.getI(),YhRenShiUser.getJ(),YhRenShiUser.getK(),YhRenShiUser.getL(),YhRenShiUser.getM(),YhRenShiUser.getN(),YhRenShiUser.getO(),YhRenShiUser.getP(),YhRenShiUser.getQ(),YhRenShiUser.getR(),YhRenShiUser.getS(),YhRenShiUser.getT(),YhRenShiUser.getU(),YhRenShiUser.getV(),YhRenShiUser.getW(),YhRenShiUser.getX(),YhRenShiUser.getY(),YhRenShiUser.getZ(),YhRenShiUser.getAa(),YhRenShiUser.getAb(),YhRenShiUser.getAc(),YhRenShiUser.getAd(),YhRenShiUser.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from gongzi_renyuan where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }
}
