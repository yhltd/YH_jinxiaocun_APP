package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinJiLu;

import java.util.List;

public class YhRenShiGongZiMingXiService {
    private renshiBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhRenShiGongZiMingXi> getList(String company) {
        String sql = "select * from gongzi_gongzimingxi where BD = ?";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""));
        return list;
    }

    /**
     * 条件查询
     */
    public List<YhRenShiGongZiMingXi> queryList(String company,String yuangong_name) {
        String sql = "select * from gongzi_gongzimingxi where BD = ? and B like '%'+ ? +'%'";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""),yuangong_name);
        return list;
    }

    /**
     * 工资明细-条件查询
     */
    public List<YhRenShiGongZiMingXi> gongzimingxiQueryList(String company,String type,String tiaojian) {
        String sql = "";
        if(type.equals("姓名")){
            sql = "select * from gongzi_gongzimingxi where BD = ? and B like '%'+ ? +'%'";
        }else if(type.equals("部门")){
            sql = "select * from gongzi_gongzimingxi where BD = ? and C like '%'+ ? +'%'";
        }else if(type.equals("岗位")){
            sql = "select * from gongzi_gongzimingxi where BD = ? and D like '%'+ ? +'%'";
        }
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""),tiaojian);
        return list;
    }

    /**
     * 查询部门下拉
     */
    public List<YhRenShiGongZiMingXi> getBuMenList(String company) {
        String sql = "select C from gongzi_gongzimingxi where BD = ? group by C";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""));
        return list;
    }

    /**
     * 查询岗位下拉
     */
    public List<YhRenShiGongZiMingXi> getGangWeiList(String company) {
        String sql = "select D from gongzi_gongzimingxi where BD = ? group by D";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""));
        return list;
    }

    /**
     * 报盘-条件查询
     */
    public List<YhRenShiGongZiMingXi> baopanQueryList(String company,String start_date,String stop_date) {
        String sql = "SELECT * from gongzi_gongzimingxi where BD = ? and convert(date,BC)>=convert(date,?) and convert(date,BC)<=convert(date,?) ";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""),start_date,stop_date);
        return list;
    }

    /**
     * 工资条-条件查询
     */
    public List<YhRenShiGongZiMingXi> gongzitiaoQueryList(String company,String bumen,String gangwei,String this_date) {
        String sql = "select * from gongzi_gongzimingxi where C like '%' + ? + '%' and D like '%'+ ? +'%' and F like '%'+ ? +'%' and BD = ?";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,bumen,gangwei,this_date,company.replace("_hr",""));
        return list;
    }

    /**
     * 个人所得税-查询
     */
    public List<YhRenShiGongZiMingXi> gerensuodeshuiQueryList(String company) {
        String sql = "select AU as B,CONVERT(varchar,sum((cast(ATA as money)*cast(AU as money)))) AS C,convert(varchar,COUNT(id)) as D, convert(varchar,sum((cast(AW AS money)*cast(AU AS money)))) AS E FROM gongzi_gongzimingxi WHERE AU is not null and ([BD] = ?) GROUP BY AU";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""));
        return list;
    }

    /**
     * 社保-查询
     */
    public List<YhRenShiGongZiMingXi> shebaoQueryList(String company,String department) {
        String sql = "select C,sum(cast(Z as float))as D,sum(cast(AJ as float))as E,sum(cast(Z as float)+cast(AJ as float))as F,sum(cast(AA as float))as G,sum (CAST(AK as float))as H,sum(cast(AA as float)+cast(AK as float))as I,sum(cast(AC as float))as J,sum(cast(AD as float))as K,sum(cast(Z as float)+cast(AA as float)+cast(AC as float)+cast(AD as float))as L,sum(cast(AJ as float)+cast(AK as float))as M from gongzi_gongzimingxi where ([BD] = ? and C like '%'+ ? +'%')  group by C";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""),department);
        return list;
    }

    /**
     * 社保-查询部门下员工社保
     */
    public List<YhRenShiGongZiMingXi> shebaoItemQueryList(String company,String name,String department) {
        String sql = "select B,C,D,Z as E,AJ as F,(convert(float,Z)+convert(float,AJ))AS G,AA as H,AK as I,(convert(float,AA)+CONVERT(float,AK))AS J,AC as K,AD as L,(convert(float,Z)+convert(float,AA)+convert(float,AC)+convert(float,AD))AS M,(convert(float,AJ)+convert(float,AK))AS N FROM gongzi_gongzimingxi WHERE (([BD] = ?) AND ([B] like '%' + ? + '%') AND ([C] = ?))";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""),name,department);
        return list;
    }

    /**
     * 部门汇总-查询
     */
    public List<YhRenShiGongZiMingXi> bumenhuizongQueryList(String company,String department,String start_date,String stop_date) {
        String sql = "SELECT C,count(C) as D,SUM(CAST(G AS float)) AS G,SUM(CAST(H AS float)) AS H,SUM(CAST(I AS float)) AS I,SUM(CAST(J AS float)) AS J,SUM(CAST(K AS float)) AS K,SUM(CAST(L AS float)) AS  L,SUM(CAST(M AS float)) AS M,SUM(CAST(N AS float)) AS N,SUM(CAST(O AS float)) AS O,SUM(CAST(P AS float)) AS P,SUM(CAST(Q AS float)) AS Q,SUM(CAST(R AS float)) AS R,SUM(CAST(S AS float)) AS S,SUM(CAST(T AS float)) AS T,SUM(CAST(U AS float)) AS U,SUM(CAST(V AS float)) AS V,SUM(CAST(W AS float)) AS W,SUM(CAST(X AS float)) AS X,SUM(CAST(Y AS float)) AS Y,SUM(CAST(Z AS float)) AS Z,SUM(CAST(AA AS float)) AS AA,SUM(CAST(AB AS float)) AS AB,SUM(CAST(AC AS float)) AS AC,SUM(CAST(AD AS float)) AS AD,SUM(CAST(AE AS float)) AS AE,SUM(CAST(AF AS float)) AS AF,SUM(CAST(AG AS float)) AS AG,SUM(CAST(AH AS float)) AS AH,SUM(CAST(AI AS float)) AS AI,SUM(CAST(AJ AS float)) AS AJ,SUM(CAST(AK AS float)) AS AK,SUM(CAST(AL AS float)) AS AL,SUM(CAST(AM AS float)) AS AM,SUM(CAST(AN AS float)) AS AN,SUM(CAST(AO AS float)) AS AO,SUM(CAST(AP AS float)) AS AP,SUM(CAST(AQ AS float)) AS AQ,SUM(CAST(AR AS float)) AS AR,SUM(CAST(ASA AS float)) AS ASA,SUM(CAST(ATA AS float)) AS ATA,SUM(CAST(AU AS float)) AS AU,SUM(CAST(AV AS float)) AS AV,SUM(CAST(AW AS float)) AS AW,SUM(CAST(AX AS float)) AS AX,SUM(CAST(AY AS float)) AS AY FROM gongzi_gongzimingxi WHERE (([BD] = ?) AND ([C] like '%'+ ? +'%')) AND convert(date,BC)>=convert(date,?) and convert(date,BC)<=convert(date,?) GROUP BY C,BD";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""),department,start_date,stop_date);
        return list;
    }

    /**
     * 部门汇总-查询部门下员工工资
     */
    public List<YhRenShiGongZiMingXi> bumenhuizongItemQueryList(String company,String name,String department) {
        String sql = "SELECT * FROM [gongzi_gongzimingxi] WHERE (([BD] =?) AND ([C] = ?) AND ([B]like '%'+ ? +'%'))";
        base = new renshiBaseDao();
        List<YhRenShiGongZiMingXi> list = base.query(YhRenShiGongZiMingXi.class, sql,company.replace("_hr",""),department,name);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhRenShiGongZiMingXi YhRenShiGongZiMingXi) {
        String sql = "insert into gongzi_gongzimingxi(B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,ASA,ATA,AU,AV,AW,AX,AY,AZ,BA,BB,BC,BD) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new renshiBaseDao();
        long result = base.executeOfId(sql, YhRenShiGongZiMingXi.getB(), YhRenShiGongZiMingXi.getC(),YhRenShiGongZiMingXi.getD(),YhRenShiGongZiMingXi.getE(),YhRenShiGongZiMingXi.getF(),YhRenShiGongZiMingXi.getG(),YhRenShiGongZiMingXi.getH(),YhRenShiGongZiMingXi.getI(),YhRenShiGongZiMingXi.getJ(),YhRenShiGongZiMingXi.getK(),YhRenShiGongZiMingXi.getL(),YhRenShiGongZiMingXi.getM(),YhRenShiGongZiMingXi.getN(),YhRenShiGongZiMingXi.getO(),YhRenShiGongZiMingXi.getP(),YhRenShiGongZiMingXi.getQ(),YhRenShiGongZiMingXi.getR(),YhRenShiGongZiMingXi.getS(),YhRenShiGongZiMingXi.getT(),YhRenShiGongZiMingXi.getU(),YhRenShiGongZiMingXi.getV(),YhRenShiGongZiMingXi.getW(),YhRenShiGongZiMingXi.getX(),YhRenShiGongZiMingXi.getY(),YhRenShiGongZiMingXi.getZ(),YhRenShiGongZiMingXi.getAa(),YhRenShiGongZiMingXi.getAb(),YhRenShiGongZiMingXi.getAc(),YhRenShiGongZiMingXi.getAd(),YhRenShiGongZiMingXi.getAe(),YhRenShiGongZiMingXi.getAf(),YhRenShiGongZiMingXi.getAg(), YhRenShiGongZiMingXi.getAh(), YhRenShiGongZiMingXi.getAi(), YhRenShiGongZiMingXi.getAj(), YhRenShiGongZiMingXi.getAk(), YhRenShiGongZiMingXi.getAl(), YhRenShiGongZiMingXi.getAm(), YhRenShiGongZiMingXi.getAn(), YhRenShiGongZiMingXi.getAo(), YhRenShiGongZiMingXi.getAp(), YhRenShiGongZiMingXi.getAq(), YhRenShiGongZiMingXi.getAr(), YhRenShiGongZiMingXi.getAsa(), YhRenShiGongZiMingXi.getAta(), YhRenShiGongZiMingXi.getAu(), YhRenShiGongZiMingXi.getAv(), YhRenShiGongZiMingXi.getAw(), YhRenShiGongZiMingXi.getAx(), YhRenShiGongZiMingXi.getAy(), YhRenShiGongZiMingXi.getAz(), YhRenShiGongZiMingXi.getBa(), YhRenShiGongZiMingXi.getBb(), YhRenShiGongZiMingXi.getBc(), YhRenShiGongZiMingXi.getBd());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhRenShiGongZiMingXi YhRenShiGongZiMingXi) {
        String sql = "update gongzi_gongzimingxi set B=?,C=?,D=?,E=?,F=?,G=?,H=?,I=?,J=?,K=?,L=?,M=?,N=?,O=?,P=?,Q=?,R=?,S=?,T=?,U=?,V=?,W=?,X=?,Y=?,Z=?,AA=?,AB=?,AC=?,AD=?,AE=?,AF=?,AG=?,AH=?,AI=?,AJ=?,AK=?,AL=?,AM=?,AN=?,AO=?,AP=?,AQ=?,AR=?,ASA=?,ATA=?,AU=?,AV=?,AW=?,AX=?,AY=?,AZ=?,BA=?,BB=?,BC=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, YhRenShiGongZiMingXi.getB(), YhRenShiGongZiMingXi.getC(),YhRenShiGongZiMingXi.getD(),YhRenShiGongZiMingXi.getE(),YhRenShiGongZiMingXi.getF(),YhRenShiGongZiMingXi.getG(),YhRenShiGongZiMingXi.getH(),YhRenShiGongZiMingXi.getI(),YhRenShiGongZiMingXi.getJ(),YhRenShiGongZiMingXi.getK(),YhRenShiGongZiMingXi.getL(),YhRenShiGongZiMingXi.getM(),YhRenShiGongZiMingXi.getN(),YhRenShiGongZiMingXi.getO(),YhRenShiGongZiMingXi.getP(),YhRenShiGongZiMingXi.getQ(),YhRenShiGongZiMingXi.getR(),YhRenShiGongZiMingXi.getS(),YhRenShiGongZiMingXi.getT(),YhRenShiGongZiMingXi.getU(),YhRenShiGongZiMingXi.getV(),YhRenShiGongZiMingXi.getW(),YhRenShiGongZiMingXi.getX(),YhRenShiGongZiMingXi.getY(),YhRenShiGongZiMingXi.getZ(),YhRenShiGongZiMingXi.getAa(),YhRenShiGongZiMingXi.getAb(),YhRenShiGongZiMingXi.getAc(),YhRenShiGongZiMingXi.getAd(),YhRenShiGongZiMingXi.getAe(),YhRenShiGongZiMingXi.getAf(),YhRenShiGongZiMingXi.getAg(), YhRenShiGongZiMingXi.getAh(), YhRenShiGongZiMingXi.getAi(), YhRenShiGongZiMingXi.getAj(), YhRenShiGongZiMingXi.getAk(), YhRenShiGongZiMingXi.getAl(), YhRenShiGongZiMingXi.getAm(), YhRenShiGongZiMingXi.getAn(), YhRenShiGongZiMingXi.getAo(), YhRenShiGongZiMingXi.getAp(), YhRenShiGongZiMingXi.getAq(), YhRenShiGongZiMingXi.getAr(), YhRenShiGongZiMingXi.getAsa(), YhRenShiGongZiMingXi.getAta(), YhRenShiGongZiMingXi.getAu(), YhRenShiGongZiMingXi.getAv(), YhRenShiGongZiMingXi.getAw(), YhRenShiGongZiMingXi.getAx(), YhRenShiGongZiMingXi.getAy(), YhRenShiGongZiMingXi.getAz(), YhRenShiGongZiMingXi.getBa(), YhRenShiGongZiMingXi.getBb(), YhRenShiGongZiMingXi.getBc(), YhRenShiGongZiMingXi.getId());
        return result;
    }

    /**
     * 修改
     */
    public boolean updateBaoShui(YhRenShiGongZiMingXi YhRenShiGongZiMingXi) {
        String sql = "update gongzi_gongzimingxi set B=?,E=?,U=?,AI=?,AK=?,AN=?,AO=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, YhRenShiGongZiMingXi.getB(), YhRenShiGongZiMingXi.getE(),YhRenShiGongZiMingXi.getU(),YhRenShiGongZiMingXi.getAi(),YhRenShiGongZiMingXi.getAk(),YhRenShiGongZiMingXi.getAn(),YhRenShiGongZiMingXi.getAo(), YhRenShiGongZiMingXi.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from gongzi_gongzimingxi where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }
}
