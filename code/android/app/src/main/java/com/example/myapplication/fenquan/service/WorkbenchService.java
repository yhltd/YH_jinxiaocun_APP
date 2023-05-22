package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Copy1;
import com.example.myapplication.fenquan.entity.Workbench;

import java.util.List;

public class WorkbenchService {

    private FenquanDao base;

    public List<Workbench> getList(String company) {
        String sql = "select * from baitaoquanxian where 公司=? order by 日期 desc";
        base = new FenquanDao();
        List<Workbench> list = base.query(Workbench.class, sql, company);
        return list;
    }

    public List<Workbench> queryList(String company,String start_date, String stop_date) {
        String sql = "select * from baitaoquanxian where 公司=? and 日期 >= ?  and 日期 <= ? order by 日期 desc";
        base = new FenquanDao();
        List<Workbench> list = base.query(Workbench.class, sql, company, start_date,stop_date);
        return list;
    }

    public boolean insert(Workbench workbench) {
        String sql = "insert into baitaoquanxian(人员,公司,日期,a最后修改日期) values(?,?,?,?)";
        base = new FenquanDao();
        long result = base.executeOfId(sql, workbench.get人员(), workbench.get公司(), workbench.get日期(), workbench.getA最后修改日期());
        return result > 0;
    }

    public boolean update(Workbench workbench) {
        String sql = "update baitaoquanxian set A=?,B=?,C=?,D=?,E=?,F=?,G=?,H=?,I=?,J=?,K=?,L=?,M=?,N=?,O=?,P=?,Q=?,R=?,S=?,T=?,U=?,V=?,W=?,X=?,Y=?,Z=?,AA=?,AB=?,AC=?,AD=?,AE=?,AF=?,AG=?,AH=?,AI=?,AJ=?,AK=?,AL=?,AM=?,AN=?,AO=?,AP=?,AQ=?,AR=?,ASS=?,AT=?,AU=?,AV=?,AW=?,AX=?,AY=?,AZ=?,BA=?,BB=?,BC=?,BD=?,BE=?,BF=?,BG=?,BH=?,BI=?,BJ=?,BK=?,BL=?,BM=?,BN=?,BO=?,BP=?,BQ=?,BR=?,BS=?,BT=?,BU=?,BV=?,BW=?,BX=?,BYY=?,BZ=?,CA=?,CB=?,CC=?,CD=?,CE=?,CF=?,CG=?,CH=?,CI=?,CJ=?,CK=?,CL=?,CM=?,CN=?,CO=?,CP=?,CQ=?,CR=?,CS=?,CT=?,CU=?,CV=? where id = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.getA(), workbench.getB(), workbench.getC(), workbench.getD(), workbench.getE(), workbench.getF(), workbench.getG(), workbench.getH(), workbench.getI(), workbench.getJ(), workbench.getK(), workbench.getL(), workbench.getM(), workbench.getN(), workbench.getO(), workbench.getP(), workbench.getQ(), workbench.getR(), workbench.getS(), workbench.getT(), workbench.getU(), workbench.getV(), workbench.getW(), workbench.getX(), workbench.getY(), workbench.getZ(), workbench.getAa(), workbench.getAb(), workbench.getAc(), workbench.getAd(), workbench.getAe(), workbench.getAf(), workbench.getAg(), workbench.getAh(), workbench.getAi(), workbench.getAj(), workbench.getAk(), workbench.getAl(), workbench.getAm(), workbench.getAn(), workbench.getAo(), workbench.getAp(), workbench.getAq(), workbench.getAr(), workbench.getAss(), workbench.getAt(), workbench.getAu(), workbench.getAv(), workbench.getAw(), workbench.getAx(), workbench.getAy(), workbench.getAz(), workbench.getBa(), workbench.getBb(), workbench.getBc(), workbench.getBd(), workbench.getBe(), workbench.getBf(), workbench.getBg(), workbench.getBh(), workbench.getBi(), workbench.getBj(), workbench.getBk(), workbench.getBl(), workbench.getBm(), workbench.getBn(), workbench.getBo(), workbench.getBp(), workbench.getBq(), workbench.getBr(), workbench.getBs(), workbench.getBt(), workbench.getBu(), workbench.getBv(), workbench.getBw(), workbench.getBx(), workbench.getByy(), workbench.getBz(), workbench.getCa(), workbench.getCb(), workbench.getCc(), workbench.getCd(), workbench.getCe(), workbench.getCf(), workbench.getCg(), workbench.getCh(), workbench.getCi(), workbench.getCj(), workbench.getCk(), workbench.getCl(), workbench.getCm(), workbench.getCn(), workbench.getCo(), workbench.getCp(), workbench.getCq(), workbench.getCr(), workbench.getCs(), workbench.getCt(), workbench.getCu(), workbench.getCv(),workbench.getId());
    }

    public boolean delete(int id) {
        String sql = "delete from baitaoquanxian where id = ?";
        base = new FenquanDao();
        return base.execute(sql, id);
    }

}
