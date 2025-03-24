package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
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
        String sql = "update baitaoquanxian set a最后修改日期=?,A=?,B=?,C=?,D=?,E=?,F=?,G=?,H=?,I=?,J=?,K=?,L=?,M=?,N=?,O=?,P=?,Q=?,R=?,S=?,T=?,U=?,V=?,W=?,X=?,Y=?,Z=?,AA=?,AB=?,AC=?,AD=?,AE=?,AF=?,AG=?,AH=?,AI=?,AJ=?,AK=?,AL=?,AM=?,AN=?,AO=?,AP=?,AQ=?,AR=?,ASS=?,AT=?,AU=?,AV=?,AW=?,AX=?,AY=?,AZ=?,BA=?,BB=?,BC=?,BD=?,BE=?,BF=?,BG=?,BH=?,BI=?,BJ=?,BK=?,BL=?,BM=?,BN=?,BO=?,BP=?,BQ=?,BR=?,BS=?,BT=?,BU=?,BV=?,BW=?,BX=?,BYY=?,BZ=?,CA=?,CB=?,CC=?,CD=?,CE=?,CF=?,CG=?,CH=?,CI=?,CJ=?,CK=?,CL=?,CM=?,CN=?,CO=?,CP=?,CQ=?,CR=?,CS=?,CT=?,CU=?,CV=? where id = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.getA最后修改日期(), workbench.getA(), workbench.getB(), workbench.getC(), workbench.getD(), workbench.getE(), workbench.getF(), workbench.getG(), workbench.getH(), workbench.getI(), workbench.getJ(), workbench.getK(), workbench.getL(), workbench.getM(), workbench.getN(), workbench.getO(), workbench.getP(), workbench.getQ(), workbench.getR(), workbench.getS(), workbench.getT(), workbench.getU(), workbench.getV(), workbench.getW(), workbench.getX(), workbench.getY(), workbench.getZ(), workbench.getAa(), workbench.getAb(), workbench.getAc(), workbench.getAd(), workbench.getAe(), workbench.getAf(), workbench.getAg(), workbench.getAh(), workbench.getAi(), workbench.getAj(), workbench.getAk(), workbench.getAl(), workbench.getAm(), workbench.getAn(), workbench.getAo(), workbench.getAp(), workbench.getAq(), workbench.getAr(), workbench.getAss(), workbench.getAt(), workbench.getAu(), workbench.getAv(), workbench.getAw(), workbench.getAx(), workbench.getAy(), workbench.getAz(), workbench.getBa(), workbench.getBb(), workbench.getBc(), workbench.getBd(), workbench.getBe(), workbench.getBf(), workbench.getBg(), workbench.getBh(), workbench.getBi(), workbench.getBj(), workbench.getBk(), workbench.getBl(), workbench.getBm(), workbench.getBn(), workbench.getBo(), workbench.getBp(), workbench.getBq(), workbench.getBr(), workbench.getBs(), workbench.getBt(), workbench.getBu(), workbench.getBv(), workbench.getBw(), workbench.getBx(), workbench.getByy(), workbench.getBz(), workbench.getCa(), workbench.getCb(), workbench.getCc(), workbench.getCd(), workbench.getCe(), workbench.getCf(), workbench.getCg(), workbench.getCh(), workbench.getCi(), workbench.getCj(), workbench.getCk(), workbench.getCl(), workbench.getCm(), workbench.getCn(), workbench.getCo(), workbench.getCp(), workbench.getCq(), workbench.getCr(), workbench.getCs(), workbench.getCt(), workbench.getCu(), workbench.getCv(),workbench.getId());
    }

    public boolean delete(int id) {
        String sql = "delete from baitaoquanxian where id = ?";
        base = new FenquanDao();
        return base.execute(sql, id);
    }

    public List<Workbench> getChart(String company,String start_date, String stop_date) {
        String sql = "select sum(case when isnull(A,'') !='' then 1 else 0 end ) as A,sum(case when isnull(B,'') !='' then 1 else 0 end ) as B,sum(case when isnull(C,'') !='' then 1 else 0 end ) as C,sum(case when isnull(D,'') !='' then 1 else 0 end ) as D,sum(case when isnull(E,'') !='' then 1 else 0 end ) as E,sum(case when isnull(F,'') !='' then 1 else 0 end ) as F,sum(case when isnull(G,'') !='' then 1 else 0 end ) as G,sum(case when isnull(H,'') !='' then 1 else 0 end ) as H,sum(case when isnull(I,'') !='' then 1 else 0 end ) as I,sum(case when isnull(J,'') !='' then 1 else 0 end ) as J,sum(case when isnull(K,'') !='' then 1 else 0 end ) as K,sum(case when isnull(L,'') !='' then 1 else 0 end ) as L,sum(case when isnull(M,'') !='' then 1 else 0 end ) as M,sum(case when isnull(N,'') !='' then 1 else 0 end ) as N,sum(case when isnull(O,'') !='' then 1 else 0 end ) as O,sum(case when isnull(P,'') !='' then 1 else 0 end ) as P,sum(case when isnull(Q,'') !='' then 1 else 0 end ) as Q,sum(case when isnull(R,'') !='' then 1 else 0 end ) as R,sum(case when isnull(S,'') !='' then 1 else 0 end ) as S,sum(case when isnull(T,'') !='' then 1 else 0 end ) as T,sum(case when isnull(U,'') !='' then 1 else 0 end ) as U,sum(case when isnull(V,'') !='' then 1 else 0 end ) as V,sum(case when isnull(W,'') !='' then 1 else 0 end ) as W,sum(case when isnull(X,'') !='' then 1 else 0 end ) as X,sum(case when isnull(Y,'') !='' then 1 else 0 end ) as Y,sum(case when isnull(Z,'') !='' then 1 else 0 end ) as Z,sum(case when isnull(AA,'') !='' then 1 else 0 end ) as AA,sum(case when isnull(AB,'') !='' then 1 else 0 end ) as AB,sum(case when isnull(AC,'') !='' then 1 else 0 end ) as AC,sum(case when isnull(AD,'') !='' then 1 else 0 end ) as AD,sum(case when isnull(AE,'') !='' then 1 else 0 end ) as AE,sum(case when isnull(AF,'') !='' then 1 else 0 end ) as AF,sum(case when isnull(AG,'') !='' then 1 else 0 end ) as AG,sum(case when isnull(AH,'') !='' then 1 else 0 end ) as AH,sum(case when isnull(AI,'') !='' then 1 else 0 end ) as AI,sum(case when isnull(AJ,'') !='' then 1 else 0 end ) as AJ,sum(case when isnull(AK,'') !='' then 1 else 0 end ) as AK,sum(case when isnull(AL,'') !='' then 1 else 0 end ) as AL" +
                ",sum(case when isnull(AM,'') !='' then 1 else 0 end ) as AM,sum(case when isnull(AN,'') !='' then 1 else 0 end ) as AN,sum(case when isnull(AO,'') !='' then 1 else 0 end ) as AO,sum(case when isnull(AP,'') !='' then 1 else 0 end ) as AP,sum(case when isnull(AQ,'') !='' then 1 else 0 end ) as AQ,sum(case when isnull(AR,'') !='' then 1 else 0 end ) as AR,sum(case when isnull(ASS,'') !='' then 1 else 0 end ) as ASS,sum(case when isnull(AT,'') !='' then 1 else 0 end ) as AT,sum(case when isnull(AU,'') !='' then 1 else 0 end ) as AU,sum(case when isnull(AV,'') !='' then 1 else 0 end ) as AV,sum(case when isnull(AW,'') !='' then 1 else 0 end ) as AW,sum(case when isnull(AX,'') !='' then 1 else 0 end ) as AX,sum(case when isnull(AY,'') !='' then 1 else 0 end ) as AY,sum(case when isnull(AZ,'') !='' then 1 else 0 end ) as AZ,sum(case when isnull(BA,'') !='' then 1 else 0 end ) as BA,sum(case when isnull(BB,'') !='' then 1 else 0 end ) as BB,sum(case when isnull(BC,'') !='' then 1 else 0 end ) as BC,sum(case when isnull(BD,'') !='' then 1 else 0 end ) as BD,sum(case when isnull(BE,'') !='' then 1 else 0 end ) as BE,sum(case when isnull(BF,'') !='' then 1 else 0 end ) as BF,sum(case when isnull(BG,'') !='' then 1 else 0 end ) as BG,sum(case when isnull(BH,'') !='' then 1 else 0 end ) as BH,sum(case when isnull(BI,'') !='' then 1 else 0 end ) as BI,sum(case when isnull(BJ,'') !='' then 1 else 0 end ) as BJ,sum(case when isnull(BK,'') !='' then 1 else 0 end ) as BK,sum(case when isnull(BL,'') !='' then 1 else 0 end ) as BL,sum(case when isnull(BM,'') !='' then 1 else 0 end ) as BM,sum(case when isnull(BN,'') !='' then 1 else 0 end ) as BN,sum(case when isnull(BO,'') !='' then 1 else 0 end ) as BO,sum(case when isnull(BP,'') !='' then 1 else 0 end ) as BP,sum(case when isnull(BQ,'') !='' then 1 else 0 end ) as BQ,sum(case when isnull(BR,'') !='' then 1 else 0 end ) as BR,sum(case when isnull(BS,'') !='' then 1 else 0 end ) as BS,sum(case when isnull(BT,'') !='' then 1 else 0 end ) as BT,sum(case when isnull(BU,'') !='' then 1 else 0 end ) as BU,sum(case when isnull(BV,'') !='' then 1 else 0 end ) as BV,sum(case when isnull(BW,'') !='' then 1 else 0 end ) as BW," +
                "sum(case when isnull(BX,'') !='' then 1 else 0 end ) as BX,sum(case when isnull(BYY,'') !='' then 1 else 0 end ) as BYY,sum(case when isnull(BZ,'') !='' then 1 else 0 end ) as BZ,sum(case when isnull(CA,'') !='' then 1 else 0 end ) as CA,sum(case when isnull(CB,'') !='' then 1 else 0 end ) as CB,sum(case when isnull(CC,'') !='' then 1 else 0 end ) as CC,sum(case when isnull(CD,'') !='' then 1 else 0 end ) as CD,sum(case when isnull(CE,'') !='' then 1 else 0 end ) as CE,sum(case when isnull(CF,'') !='' then 1 else 0 end ) as CF,sum(case when isnull(CG,'') !='' then 1 else 0 end ) as CG,sum(case when isnull(CH,'') !='' then 1 else 0 end ) as CH,sum(case when isnull(CI,'') !='' then 1 else 0 end ) as CI,sum(case when isnull(CJ,'') !='' then 1 else 0 end ) as CJ,sum(case when isnull(CK,'') !='' then 1 else 0 end ) as CK,sum(case when isnull(CL,'') !='' then 1 else 0 end ) as CL,sum(case when isnull(CM,'') !='' then 1 else 0 end ) as CM,sum(case when isnull(CN,'') !='' then 1 else 0 end ) as CN,sum(case when isnull(CO,'') !='' then 1 else 0 end ) as CO,sum(case when isnull(CP,'') !='' then 1 else 0 end ) as CP,sum(case when isnull(CQ,'') !='' then 1 else 0 end ) as CQ,sum(case when isnull(CR,'') !='' then 1 else 0 end ) as CR,sum(case when isnull(CS,'') !='' then 1 else 0 end ) as CS,sum(case when isnull(CT,'') !='' then 1 else 0 end ) as CT,sum(case when isnull(CU,'') !='' then 1 else 0 end ) as CU,sum(case when isnull(CV,'') !='' then 1 else 0 end ) as CV from baitaoquanxian" +
                " where 公司=? and 日期 >= convert(date,?) and 日期 <= convert(date,?)";
        base = new FenquanDao();
        List<Workbench> list = base.query(Workbench.class, sql, company, start_date,stop_date);
        return list;
    }

    public List<Workbench> getRenYuanChart(String company,String name,String start_date, String stop_date) {
        String sql = "select sum(case when isnull(A,'') !='' then 1 else 0 end ) as A,sum(case when isnull(B,'') !='' then 1 else 0 end ) as B,sum(case when isnull(C,'') !='' then 1 else 0 end ) as C,sum(case when isnull(D,'') !='' then 1 else 0 end ) as D,sum(case when isnull(E,'') !='' then 1 else 0 end ) as E,sum(case when isnull(F,'') !='' then 1 else 0 end ) as F,sum(case when isnull(G,'') !='' then 1 else 0 end ) as G,sum(case when isnull(H,'') !='' then 1 else 0 end ) as H,sum(case when isnull(I,'') !='' then 1 else 0 end ) as I,sum(case when isnull(J,'') !='' then 1 else 0 end ) as J,sum(case when isnull(K,'') !='' then 1 else 0 end ) as K,sum(case when isnull(L,'') !='' then 1 else 0 end ) as L,sum(case when isnull(M,'') !='' then 1 else 0 end ) as M,sum(case when isnull(N,'') !='' then 1 else 0 end ) as N,sum(case when isnull(O,'') !='' then 1 else 0 end ) as O,sum(case when isnull(P,'') !='' then 1 else 0 end ) as P,sum(case when isnull(Q,'') !='' then 1 else 0 end ) as Q,sum(case when isnull(R,'') !='' then 1 else 0 end ) as R,sum(case when isnull(S,'') !='' then 1 else 0 end ) as S,sum(case when isnull(T,'') !='' then 1 else 0 end ) as T,sum(case when isnull(U,'') !='' then 1 else 0 end ) as U,sum(case when isnull(V,'') !='' then 1 else 0 end ) as V,sum(case when isnull(W,'') !='' then 1 else 0 end ) as W,sum(case when isnull(X,'') !='' then 1 else 0 end ) as X,sum(case when isnull(Y,'') !='' then 1 else 0 end ) as Y,sum(case when isnull(Z,'') !='' then 1 else 0 end ) as Z,sum(case when isnull(AA,'') !='' then 1 else 0 end ) as AA,sum(case when isnull(AB,'') !='' then 1 else 0 end ) as AB,sum(case when isnull(AC,'') !='' then 1 else 0 end ) as AC,sum(case when isnull(AD,'') !='' then 1 else 0 end ) as AD,sum(case when isnull(AE,'') !='' then 1 else 0 end ) as AE,sum(case when isnull(AF,'') !='' then 1 else 0 end ) as AF,sum(case when isnull(AG,'') !='' then 1 else 0 end ) as AG,sum(case when isnull(AH,'') !='' then 1 else 0 end ) as AH,sum(case when isnull(AI,'') !='' then 1 else 0 end ) as AI,sum(case when isnull(AJ,'') !='' then 1 else 0 end ) as AJ,sum(case when isnull(AK,'') !='' then 1 else 0 end ) as AK,sum(case when isnull(AL,'') !='' then 1 else 0 end ) as AL" +
                ",sum(case when isnull(AM,'') !='' then 1 else 0 end ) as AM,sum(case when isnull(AN,'') !='' then 1 else 0 end ) as AN,sum(case when isnull(AO,'') !='' then 1 else 0 end ) as AO,sum(case when isnull(AP,'') !='' then 1 else 0 end ) as AP,sum(case when isnull(AQ,'') !='' then 1 else 0 end ) as AQ,sum(case when isnull(AR,'') !='' then 1 else 0 end ) as AR,sum(case when isnull(ASS,'') !='' then 1 else 0 end ) as ASS,sum(case when isnull(AT,'') !='' then 1 else 0 end ) as AT,sum(case when isnull(AU,'') !='' then 1 else 0 end ) as AU,sum(case when isnull(AV,'') !='' then 1 else 0 end ) as AV,sum(case when isnull(AW,'') !='' then 1 else 0 end ) as AW,sum(case when isnull(AX,'') !='' then 1 else 0 end ) as AX,sum(case when isnull(AY,'') !='' then 1 else 0 end ) as AY,sum(case when isnull(AZ,'') !='' then 1 else 0 end ) as AZ,sum(case when isnull(BA,'') !='' then 1 else 0 end ) as BA,sum(case when isnull(BB,'') !='' then 1 else 0 end ) as BB,sum(case when isnull(BC,'') !='' then 1 else 0 end ) as BC,sum(case when isnull(BD,'') !='' then 1 else 0 end ) as BD,sum(case when isnull(BE,'') !='' then 1 else 0 end ) as BE,sum(case when isnull(BF,'') !='' then 1 else 0 end ) as BF,sum(case when isnull(BG,'') !='' then 1 else 0 end ) as BG,sum(case when isnull(BH,'') !='' then 1 else 0 end ) as BH,sum(case when isnull(BI,'') !='' then 1 else 0 end ) as BI,sum(case when isnull(BJ,'') !='' then 1 else 0 end ) as BJ,sum(case when isnull(BK,'') !='' then 1 else 0 end ) as BK,sum(case when isnull(BL,'') !='' then 1 else 0 end ) as BL,sum(case when isnull(BM,'') !='' then 1 else 0 end ) as BM,sum(case when isnull(BN,'') !='' then 1 else 0 end ) as BN,sum(case when isnull(BO,'') !='' then 1 else 0 end ) as BO,sum(case when isnull(BP,'') !='' then 1 else 0 end ) as BP,sum(case when isnull(BQ,'') !='' then 1 else 0 end ) as BQ,sum(case when isnull(BR,'') !='' then 1 else 0 end ) as BR,sum(case when isnull(BS,'') !='' then 1 else 0 end ) as BS,sum(case when isnull(BT,'') !='' then 1 else 0 end ) as BT,sum(case when isnull(BU,'') !='' then 1 else 0 end ) as BU,sum(case when isnull(BV,'') !='' then 1 else 0 end ) as BV,sum(case when isnull(BW,'') !='' then 1 else 0 end ) as BW," +
                "sum(case when isnull(BX,'') !='' then 1 else 0 end ) as BX,sum(case when isnull(BYY,'') !='' then 1 else 0 end ) as BYY,sum(case when isnull(BZ,'') !='' then 1 else 0 end ) as BZ,sum(case when isnull(CA,'') !='' then 1 else 0 end ) as CA,sum(case when isnull(CB,'') !='' then 1 else 0 end ) as CB,sum(case when isnull(CC,'') !='' then 1 else 0 end ) as CC,sum(case when isnull(CD,'') !='' then 1 else 0 end ) as CD,sum(case when isnull(CE,'') !='' then 1 else 0 end ) as CE,sum(case when isnull(CF,'') !='' then 1 else 0 end ) as CF,sum(case when isnull(CG,'') !='' then 1 else 0 end ) as CG,sum(case when isnull(CH,'') !='' then 1 else 0 end ) as CH,sum(case when isnull(CI,'') !='' then 1 else 0 end ) as CI,sum(case when isnull(CJ,'') !='' then 1 else 0 end ) as CJ,sum(case when isnull(CK,'') !='' then 1 else 0 end ) as CK,sum(case when isnull(CL,'') !='' then 1 else 0 end ) as CL,sum(case when isnull(CM,'') !='' then 1 else 0 end ) as CM,sum(case when isnull(CN,'') !='' then 1 else 0 end ) as CN,sum(case when isnull(CO,'') !='' then 1 else 0 end ) as CO,sum(case when isnull(CP,'') !='' then 1 else 0 end ) as CP,sum(case when isnull(CQ,'') !='' then 1 else 0 end ) as CQ,sum(case when isnull(CR,'') !='' then 1 else 0 end ) as CR,sum(case when isnull(CS,'') !='' then 1 else 0 end ) as CS,sum(case when isnull(CT,'') !='' then 1 else 0 end ) as CT,sum(case when isnull(CU,'') !='' then 1 else 0 end ) as CU,sum(case when isnull(CV,'') !='' then 1 else 0 end ) as CV from baitaoquanxian" +
                " where 公司=? and 日期 >= convert(date,?) and 日期 <= convert(date,?) and 人员 = ?";
        base = new FenquanDao();
        List<Workbench> list = base.query(Workbench.class, sql, company, start_date,stop_date,name);
        return list;
    }

    public boolean updatezyA(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set A = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyA(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set A = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyB(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set B = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyB(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set B = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyC(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set C = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyC(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set C = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyD(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set D = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyD(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set D = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyE(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set E = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyE(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set E = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyF(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set F = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyF(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set F = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyG(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set G = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyG(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set G = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyH(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set H = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyH(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set H = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyI(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set I = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyI(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set I = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyJ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set J = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyJ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set J = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyK(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set K = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyK(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set K = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyL(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set L = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyL(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set L = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyM(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set M = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyM(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set M = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyN(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set N = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyN(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set N = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyO(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set O = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyO(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set O = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyP(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set P = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyP(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set P = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyQ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set Q = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyQ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set Q = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyR(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set R = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyR(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set R = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyS(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set S = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyS(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set S = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyT(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set T = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyT(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set T = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyU(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set U = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyU(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set U = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyV(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set V = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyV(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set V = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyW(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set W = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyW(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set W = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyX(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set X = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyX(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set X = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyY(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set Y = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyY(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set Y = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyZ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set Z = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyZ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set Z = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAA(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AA = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAA(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AA = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAB(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AB = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAB(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AB = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAC(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AC = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAC(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AC = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAD(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AD = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAD(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AD = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAE(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AE = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAE(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AE = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAF(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AF = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAF(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AF = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAG(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AG = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAG(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AG = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAH(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AH = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAH(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AH = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAI(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AI = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAI(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AI = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAJ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AJ = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAJ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AJ = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAK(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AK = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAK(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AK = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAL(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AL = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAL(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AL = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAM(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AM = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAM(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AM = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAN(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AN = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAN(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AN = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAO(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AO = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAO(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AO = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAP(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AP = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAP(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AP = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAQ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AQ = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAQ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AQ = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAR(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AR = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAR(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AR = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyASS(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set ASS = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyASS(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set ASS = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAT(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AT = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAT(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AT = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAU(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AU = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAU(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AU = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAV(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AV = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAV(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AV = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAW(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AW = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAW(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AW = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAX(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AX = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAX(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AX = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAY(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AY = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAY(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AY = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyAZ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AZ = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyAZ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set AZ = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBA(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BA = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBA(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BA = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBB(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BB = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBB(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BB = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBC(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BC = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBC(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BC = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBD(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BD = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBD(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BD = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBE(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BE = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBE(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BE = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBF(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BF = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBF(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BF = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBG(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BG = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBG(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BG = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBH(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BH = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBH(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BH = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBI(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BI = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBI(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BI = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBJ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BJ = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBJ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BJ = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBK(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BK = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBK(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BK = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBL(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BL = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBL(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BL = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBM(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BM = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBM(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BM = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBN(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BN = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBN(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BN = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBO(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BO = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBO(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BO = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBP(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BP = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBP(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BP = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBQ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BQ = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBQ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BQ = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBR(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BR = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBR(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BR = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBS(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BS = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBS(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BS = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBT(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BT = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBT(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BT = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBU(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BU = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBU(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BU = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBV(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BV = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBV(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BV = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBW(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BW = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBW(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BW = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBX(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BX = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBX(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BX = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBYY(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BYY = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBYY(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BYY = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyBZ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BZ = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyBZ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set BZ = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCA(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CA = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCA(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CA = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCB(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CB = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCB(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CB = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCC(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CC = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCC(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CC = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCD(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CD = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCD(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CD = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCE(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CE = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCE(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CE = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCF(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CF = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCF(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CF = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCG(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CG = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCG(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CG = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCH(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CH = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCH(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CH = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCI(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CI = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCI(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CI = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCJ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CJ = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCJ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CJ = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCK(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CK = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCK(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CK = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCL(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CL = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCL(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CL = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCM(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CM = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCM(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CM = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCN(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CN = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCN(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CN = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCO(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CO = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCO(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CO = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCP(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CP = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCP(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CP = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCQ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CQ = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCQ(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CQ = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCR(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CR = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCR(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CR = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCS(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CS = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCS(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CS = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCT(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CT = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCT(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CT = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCU(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CU = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCU(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CU = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updatezyCV(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CV = ? where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get人员(), workbench.get公司());
    }

    public boolean updateqxzyCV(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set CV = '' where 公司 = ?";
        base = new FenquanDao();
        return base.execute(sql, workbench.get公司());
    }

    public boolean updateClearAll(Workbench workbench) {
        String sql = "update baitaoquanxian_copy2 set A='',B='',C='',D='',E='',F='',G='',H='',I='',J='',K='',L='',M='',N='',O='',P='',Q='',R='',S='',T='',U='',V='',W='',X='',Y='',Z='',AA='',AB='',AC='',AD='',AE='',AF='',AG='',AH='',AI='',AJ='',AK='',AL='',AM='',AN='',AO='',AP='',AQ='',AR='',ASS='',AT='',AU='',AV='',AW='',AX='',AY='',AZ='',BA='',BB='',BC='',BD='',BE='',BF='',BG='',BH='',BI='',BJ='',BK='',BL='',BM='',BN='',BO='',BP='',BQ='',BR='',BS='',BT='',BU='',BV='',BW='',BX='',BYY='',BZ='',CA='',CB='',CC='',CD='',CE='',CF='',CG='',CH='',CI='',CJ='',CK='',CL='',CM='',CN='',CO='',CP='',CQ='',CR='',CS='',CT='',CU='',CV='' where 公司=?";
        base = new FenquanDao();
        boolean result = base.execute(sql,workbench.get公司());
        return result;
    }

}
