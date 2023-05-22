package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Copy1;
import com.example.myapplication.fenquan.entity.Gongsi;

import java.util.List;

public class GongsiService {

    private FenquanDao base;

    public List<Gongsi> getList(String company) {
        String sql = "select * from baitaoquanxian_gongsi where B=?";
        base = new FenquanDao();
        List<Gongsi> list = base.query(Gongsi.class, sql, company);
        return list;
    }

    public boolean update(Gongsi gongsi) {
        String sql = "update baitaoquanxian_gongsi set C=?,D=?,E=?,F=?,G=?,H=?,I=?,J=?,K=?,L=?,M=?,N=?,O=?,P=?,Q=?,R=?,S=?,T=?,U=?,V=?,W=?,X=?,Y=?,Z=?,AA=?,AB=?,AC=?,AD=?,AE=?,AF=?,AG=?,AH=?,AI=?,AJ=?,AK=?,AL=?,AM=?,AN=?,AO=?,AP=?,AQ=?,AR=?,ASS=?,AT=?,AU=?,AV=?,AW=?,AX=?,AY=?,AZ=?,BA=?,BB=?,BC=?,BD=?,BE=?,BF=?,BG=?,BH=?,BI=?,BJ=?,BK=?,BL=?,BM=?,BN=?,BO=?,BP=?,BQ=?,BR=?,BS=?,BT=?,BU=?,BV=?,BW=?,BX=?,BYY=?,BZ=?,CA=?,CB=?,CC=?,CD=?,CE=?,CF=?,CG=?,CH=?,CI=?,CJ=?,CK=?,CL=?,CM=?,CN=?,CO=?,CP=?,CQ=?,CR=?,CS=?,CT=?,CU=?,CV=?,CW=?,CX=? where B = ?";
        base = new FenquanDao();
        return base.execute(sql, gongsi.getC(), gongsi.getD(), gongsi.getE(), gongsi.getF(), gongsi.getG(), gongsi.getH(), gongsi.getI(), gongsi.getJ(), gongsi.getK(), gongsi.getL(), gongsi.getM(), gongsi.getN(), gongsi.getO(), gongsi.getP(), gongsi.getQ(), gongsi.getR(), gongsi.getS(), gongsi.getT(), gongsi.getU(), gongsi.getV(), gongsi.getW(), gongsi.getX(), gongsi.getY(), gongsi.getZ(), gongsi.getAa(), gongsi.getAb(), gongsi.getAc(), gongsi.getAd(), gongsi.getAe(), gongsi.getAf(), gongsi.getAg(), gongsi.getAh(), gongsi.getAi(), gongsi.getAj(), gongsi.getAk(), gongsi.getAl(), gongsi.getAm(), gongsi.getAn(), gongsi.getAo(), gongsi.getAp(), gongsi.getAq(), gongsi.getAr(), gongsi.getAss(), gongsi.getAt(), gongsi.getAu(), gongsi.getAv(), gongsi.getAw(), gongsi.getAx(), gongsi.getAy(), gongsi.getAz(), gongsi.getBa(), gongsi.getBb(), gongsi.getBc(), gongsi.getBd(), gongsi.getBe(), gongsi.getBf(), gongsi.getBg(), gongsi.getBh(), gongsi.getBi(), gongsi.getBj(), gongsi.getBk(), gongsi.getBl(), gongsi.getBm(), gongsi.getBn(), gongsi.getBo(), gongsi.getBp(), gongsi.getBq(), gongsi.getBr(), gongsi.getBs(), gongsi.getBt(), gongsi.getBu(), gongsi.getBv(), gongsi.getBw(), gongsi.getBx(), gongsi.getByy(), gongsi.getBz(), gongsi.getCa(), gongsi.getCb(), gongsi.getCc(), gongsi.getCd(), gongsi.getCe(), gongsi.getCf(), gongsi.getCg(), gongsi.getCh(), gongsi.getCi(), gongsi.getCj(), gongsi.getCk(), gongsi.getCl(), gongsi.getCm(), gongsi.getCn(), gongsi.getCo(), gongsi.getCp(), gongsi.getCq(), gongsi.getCr(), gongsi.getCs(), gongsi.getCt(), gongsi.getCu(), gongsi.getCv(), gongsi.getCw(), gongsi.getCx(),gongsi.getB());
    }

}
