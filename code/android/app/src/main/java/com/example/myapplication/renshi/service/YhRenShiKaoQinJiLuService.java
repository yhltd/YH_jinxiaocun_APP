package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinJiLu;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;

import java.util.List;

public class YhRenShiKaoQinJiLuService {
    private renshiBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhRenShiKaoQinJiLu> getList(String company) {
        String sql = "select * from gongzi_kaoqinjilu where AO = ? order by year desc,moth desc";
        base = new renshiBaseDao();
        List<YhRenShiKaoQinJiLu> list = base.query(YhRenShiKaoQinJiLu.class, sql,company.replace("_hr",""));
        return list;
    }

    /**
     * 条件查询
     */
    public List<YhRenShiKaoQinJiLu> queryList(String company,String start_date,String stop_date) {
        String sql = "select * from gongzi_kaoqinjilu where AO = ? and year+moth >= ? and year+moth <= ? order by year desc,moth desc";
        base = new renshiBaseDao();
        List<YhRenShiKaoQinJiLu> list = base.query(YhRenShiKaoQinJiLu.class, sql,company.replace("_hr",""),start_date,stop_date);
        return list;
    }

    /**
     * 条件查询
     */
    public List<YhRenShiKaoQinJiLu> nameMonthList(String company,String name,String this_date) {
        String sql = "select * from gongzi_kaoqinjilu where AO = ? and year+moth = ? and name = ?";
        base = new renshiBaseDao();
        List<YhRenShiKaoQinJiLu> list = base.query(YhRenShiKaoQinJiLu.class, sql,company.replace("_hr",""),this_date,name);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhRenShiKaoQinJiLu YhRenShiKaoQinJiLu) {
        String sql = "insert into gongzi_kaoqinjilu(year,moth,name,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new renshiBaseDao();
        long result = base.executeOfId(sql, YhRenShiKaoQinJiLu.getYear(), YhRenShiKaoQinJiLu.getMoth(),YhRenShiKaoQinJiLu.getName(),YhRenShiKaoQinJiLu.getE(),YhRenShiKaoQinJiLu.getF(),YhRenShiKaoQinJiLu.getG(),YhRenShiKaoQinJiLu.getH(),YhRenShiKaoQinJiLu.getI(),YhRenShiKaoQinJiLu.getJ(),YhRenShiKaoQinJiLu.getK(),YhRenShiKaoQinJiLu.getL(),YhRenShiKaoQinJiLu.getM(),YhRenShiKaoQinJiLu.getN(),YhRenShiKaoQinJiLu.getO(),YhRenShiKaoQinJiLu.getP(),YhRenShiKaoQinJiLu.getQ(),YhRenShiKaoQinJiLu.getR(),YhRenShiKaoQinJiLu.getS(),YhRenShiKaoQinJiLu.getT(),YhRenShiKaoQinJiLu.getU(),YhRenShiKaoQinJiLu.getV(),YhRenShiKaoQinJiLu.getW(),YhRenShiKaoQinJiLu.getX(),YhRenShiKaoQinJiLu.getY(),YhRenShiKaoQinJiLu.getZ(),YhRenShiKaoQinJiLu.getAa(),YhRenShiKaoQinJiLu.getAb(),YhRenShiKaoQinJiLu.getAc(),YhRenShiKaoQinJiLu.getAd(),YhRenShiKaoQinJiLu.getAe(),YhRenShiKaoQinJiLu.getAf(),YhRenShiKaoQinJiLu.getAg(), YhRenShiKaoQinJiLu.getAh(), YhRenShiKaoQinJiLu.getAi(), YhRenShiKaoQinJiLu.getAj(), YhRenShiKaoQinJiLu.getAk(), YhRenShiKaoQinJiLu.getAl(), YhRenShiKaoQinJiLu.getAm(), YhRenShiKaoQinJiLu.getAn(), YhRenShiKaoQinJiLu.getAo());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhRenShiKaoQinJiLu YhRenShiKaoQinJiLu) {
        String sql = "update gongzi_kaoqinjilu set year=?,moth=?,name=?,E=?,F=?,G=?,H=?,I=?,J=?,K=?,L=?,M=?,N=?,O=?,P=?,Q=?,R=?,S=?,T=?,U=?,V=?,W=?,X=?,Y=?,Z=?,AA=?,AB=?,AC=?,AD=?,AE=?,AF=?,AG=?,AH=?,AI=?,AJ=?,AK=?,AL=?,AM=?,AN=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, YhRenShiKaoQinJiLu.getYear(), YhRenShiKaoQinJiLu.getMoth(),YhRenShiKaoQinJiLu.getName(),YhRenShiKaoQinJiLu.getE(),YhRenShiKaoQinJiLu.getF(),YhRenShiKaoQinJiLu.getG(),YhRenShiKaoQinJiLu.getH(),YhRenShiKaoQinJiLu.getI(),YhRenShiKaoQinJiLu.getJ(),YhRenShiKaoQinJiLu.getK(),YhRenShiKaoQinJiLu.getL(),YhRenShiKaoQinJiLu.getM(),YhRenShiKaoQinJiLu.getN(),YhRenShiKaoQinJiLu.getO(),YhRenShiKaoQinJiLu.getP(),YhRenShiKaoQinJiLu.getQ(),YhRenShiKaoQinJiLu.getR(),YhRenShiKaoQinJiLu.getS(),YhRenShiKaoQinJiLu.getT(),YhRenShiKaoQinJiLu.getU(),YhRenShiKaoQinJiLu.getV(),YhRenShiKaoQinJiLu.getW(),YhRenShiKaoQinJiLu.getX(),YhRenShiKaoQinJiLu.getY(),YhRenShiKaoQinJiLu.getZ(),YhRenShiKaoQinJiLu.getAa(),YhRenShiKaoQinJiLu.getAb(),YhRenShiKaoQinJiLu.getAc(),YhRenShiKaoQinJiLu.getAd(),YhRenShiKaoQinJiLu.getAe(),YhRenShiKaoQinJiLu.getAf(),YhRenShiKaoQinJiLu.getAg(), YhRenShiKaoQinJiLu.getAh(), YhRenShiKaoQinJiLu.getAi(), YhRenShiKaoQinJiLu.getAj(), YhRenShiKaoQinJiLu.getAk(), YhRenShiKaoQinJiLu.getAl(), YhRenShiKaoQinJiLu.getAm(), YhRenShiKaoQinJiLu.getAn(), YhRenShiKaoQinJiLu.getId());
        return result;
    }

    /**
     * 修改
     */
    public boolean update_xiujia(String sql) {
        base = new renshiBaseDao();
        boolean result = base.execute(sql);
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from gongzi_kaoqinjilu where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }
}
