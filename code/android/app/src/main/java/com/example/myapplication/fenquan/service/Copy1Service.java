package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Copy1;
import com.example.myapplication.fenquan.entity.Renyuan;

import java.util.List;

public class Copy1Service {

    private FenquanDao base;

    public List<Copy1> getList(String company) {
        String sql = "select * from baitaoquanxian_copy1 where quanxian=?";
        base = new FenquanDao();
        List<Copy1> list = base.query(Copy1.class, sql, company);
        return list;
    }

    public List<Copy1> getListById(String id) {
        String sql = "select * from baitaoquanxian_copy1 where renyuan_id=?";
        base = new FenquanDao();
        List<Copy1> list = base.query(Copy1.class, sql, id);
        return list;
    }

    public List<Copy1> queryList(String company,String chashanquanxian, String query) {
        String sql = "select * from baitaoquanxian_copy1 where quanxian=? and B like '%'+ ? +'%' and chashanquanxian like '%' + ? + '%' ";
        base = new FenquanDao();
        List<Copy1> list = base.query(Copy1.class, sql, company, query,chashanquanxian);
        return list;
    }

    public boolean insert(String company, String B,String renyuan_id,String chashanquanxian) {
        String sql = "insert into baitaoquanxian_copy1(quanxian,B,renyuan_id,chashanquanxian) values(?,?,?,?)";
        base = new FenquanDao();
        long result = base.executeOfId(sql, company, B, renyuan_id, chashanquanxian);
        return result > 0;
    }

    public boolean update(Copy1 Copy1) {
        String sql = "update baitaoquanxian_copy1 set C=?,D=?,E=?,F=?,G=?,H=?,I=?,J=?,K=?,L=?,M=?,N=?,O=?,P=?,Q=?,R=?,S=?,T=?,U=?,V=?,W=?,X=?,Y=?,Z=?,AA=?,AB=?,AC=?,AD=?,AE=?,AF=?,AG=?,AH=?,AI=?,AJ=?,AK=?,AL=?,AM=?,AN=?,AO=?,AP=?,AQ=?,AR=?,ASS=?,AT=?,AU=?,AV=?,AW=?,AX=?,AY=?,AZ=?,BA=?,BB=?,BC=?,BD=?,BE=?,BF=?,BG=?,BH=?,BI=?,BJ=?,BK=?,BL=?,BM=?,BN=?,BO=?,BP=?,BQ=?,BR=?,BS=?,BT=?,BU=?,BV=?,BW=?,BX=?,BYY=?,BZ=?,CA=?,CB=?,CC=?,CD=?,CE=?,CF=?,CG=?,CH=?,CI=?,CJ=?,CK=?,CL=?,CM=?,CN=?,CO=?,CP=?,CQ=?,CR=?,CS=?,CT=?,CU=?,CV=?,CW=?,CX=? where id = ?";
        base = new FenquanDao();
        return base.execute(sql, Copy1.getC(), Copy1.getD(), Copy1.getE(), Copy1.getF(), Copy1.getG(), Copy1.getH(), Copy1.getI(), Copy1.getJ(), Copy1.getK(), Copy1.getL(), Copy1.getM(), Copy1.getN(), Copy1.getO(), Copy1.getP(), Copy1.getQ(), Copy1.getR(), Copy1.getS(), Copy1.getT(), Copy1.getU(), Copy1.getV(), Copy1.getW(), Copy1.getX(), Copy1.getY(), Copy1.getZ(), Copy1.getAa(), Copy1.getAb(), Copy1.getAc(), Copy1.getAd(), Copy1.getAe(), Copy1.getAf(), Copy1.getAg(), Copy1.getAh(), Copy1.getAi(), Copy1.getAj(), Copy1.getAk(), Copy1.getAl(), Copy1.getAm(), Copy1.getAn(), Copy1.getAo(), Copy1.getAp(), Copy1.getAq(), Copy1.getAr(), Copy1.getAss(), Copy1.getAt(), Copy1.getAu(), Copy1.getAv(), Copy1.getAw(), Copy1.getAx(), Copy1.getAy(), Copy1.getAz(), Copy1.getBa(), Copy1.getBb(), Copy1.getBc(), Copy1.getBd(), Copy1.getBe(), Copy1.getBf(), Copy1.getBg(), Copy1.getBh(), Copy1.getBi(), Copy1.getBj(), Copy1.getBk(), Copy1.getBl(), Copy1.getBm(), Copy1.getBn(), Copy1.getBo(), Copy1.getBp(), Copy1.getBq(), Copy1.getBr(), Copy1.getBs(), Copy1.getBt(), Copy1.getBu(), Copy1.getBv(), Copy1.getBw(), Copy1.getBx(), Copy1.getByy(), Copy1.getBz(), Copy1.getCa(), Copy1.getCb(), Copy1.getCc(), Copy1.getCd(), Copy1.getCe(), Copy1.getCf(), Copy1.getCg(), Copy1.getCh(), Copy1.getCi(), Copy1.getCj(), Copy1.getCk(), Copy1.getCl(), Copy1.getCm(), Copy1.getCn(), Copy1.getCo(), Copy1.getCp(), Copy1.getCq(), Copy1.getCr(), Copy1.getCs(), Copy1.getCt(), Copy1.getCu(), Copy1.getCv(), Copy1.getCw(), Copy1.getCx(),Copy1.getId());
    }

    public boolean delete(String id) {
        String sql = "delete from baitaoquanxian_copy1 where renyuan_id = ?";
        base = new FenquanDao();
        return base.execute(sql, id);
    }

}
