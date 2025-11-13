package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCun;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 进销存全部数据
     */
//    public List<YhJinXiaoCun> getJxc(String company) {
//        base = new JxcBaseDao();
//        String sql = "select *,(jq_cpsl+mx_ruku_cpsl-mx_chuku_cpsl) as jc_sl,(jq_price+mx_ruku_price-mx_chuku_price) as jc_price from (select jj.sp_dm,jj.name,jj.lei_bie,ifnull(sum(jq.cpsl),0) as jq_cpsl,ifnull(sum(jq.cpsl*jq.cpsj),0) as jq_price,ifnull(mx_ruku.cpsl,0) as mx_ruku_cpsl,ifnull(mx_ruku.cp_price,0) as mx_ruku_price,ifnull(mx_chuku.cpsl,0) as mx_chuku_cpsl,ifnull(mx_chuku.cp_price,0) as mx_chuku_price from yh_jinxiaocun_jichuziliao as jj left join yh_jinxiaocun_qichushu as jq on jj.sp_dm = jq.cpid and jq.gs_name = ? left join (select jm.sp_dm,sum(jm.cpsl) as cpsl,sum(jm.cpsl*jm.cpsj) as cp_price from yh_jinxiaocun_mingxi as jm where jm.gs_name = ? and jm.mxtype = '入库' group by jm.sp_dm) as mx_ruku on mx_ruku.sp_dm = jj.sp_dm left join (select jm.sp_dm,sum(jm.cpsl) as cpsl,sum(jm.cpsl*jm.cpsj) as cp_price from yh_jinxiaocun_mingxi as jm where jm.gs_name = ? and jm.mxtype = '出库' group by jm.sp_dm ) as mx_chuku on mx_chuku.sp_dm = jj.sp_dm where jj.gs_name = ? GROUP BY jj.sp_dm,jj.name,jj.lei_bie) as jxc";
//        List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company);
//        return list;
//    }
    public List<YhJinXiaoCun> getJxc(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                String sql = "select *,(jq_cpsl+mx_ruku_cpsl-mx_chuku_cpsl) as jc_sl,(jq_price+mx_ruku_price-mx_chuku_price) as jc_price from (select jj.sp_dm,jj.name,jj.lei_bie,isnull(sum(jq.cpsl),0) as jq_cpsl,isnull(sum(jq.cpsl*jq.cpsj),0) as jq_price,isnull(mx_ruku.cpsl,0) as mx_ruku_cpsl,isnull(mx_ruku.cp_price,0) as mx_ruku_price,isnull(mx_chuku.cpsl,0) as mx_chuku_cpsl,isnull(mx_chuku.cp_price,0) as mx_chuku_price from yh_jinxiaocun_jichuziliao_mssql as jj left join yh_jinxiaocun_qichushu_mssql as jq on jj.sp_dm = jq.cpid and jq.gs_name = ? left join (select jm.sp_dm,sum(jm.cpsl) as cpsl,sum(jm.cpsl*jm.cpsj) as cp_price from yh_jinxiaocun_mingxi_mssql as jm where jm.gs_name = ? and jm.mxtype = '入库' group by jm.sp_dm) as mx_ruku on mx_ruku.sp_dm = jj.sp_dm left join (select jm.sp_dm,sum(jm.cpsl) as cpsl,sum(jm.cpsl*jm.cpsj) as cp_price from yh_jinxiaocun_mingxi_mssql as jm where jm.gs_name = ? and jm.mxtype = '出库' group by jm.sp_dm ) as mx_chuku on mx_chuku.sp_dm = jj.sp_dm where jj.gs_name = ? GROUP BY jj.sp_dm,jj.name,jj.lei_bie) as jxc";
                List<YhJinXiaoCun> list = base2.query(YhJinXiaoCun.class, sql, company, company, company, company);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                String sql = "select *,(jq_cpsl+mx_ruku_cpsl-mx_chuku_cpsl) as jc_sl,(jq_price+mx_ruku_price-mx_chuku_price) as jc_price from (select jj.sp_dm,jj.name,jj.lei_bie,ifnull(sum(jq.cpsl),0) as jq_cpsl,ifnull(sum(jq.cpsl*jq.cpsj),0) as jq_price,ifnull(mx_ruku.cpsl,0) as mx_ruku_cpsl,ifnull(mx_ruku.cp_price,0) as mx_ruku_price,ifnull(mx_chuku.cpsl,0) as mx_chuku_cpsl,ifnull(mx_chuku.cp_price,0) as mx_chuku_price from yh_jinxiaocun_jichuziliao as jj left join yh_jinxiaocun_qichushu as jq on jj.sp_dm = jq.cpid and jq.gs_name = ? left join (select jm.sp_dm,sum(jm.cpsl) as cpsl,sum(jm.cpsl*jm.cpsj) as cp_price from yh_jinxiaocun_mingxi as jm where jm.gs_name = ? and jm.mxtype = '入库' group by jm.sp_dm) as mx_ruku on mx_ruku.sp_dm = jj.sp_dm left join (select jm.sp_dm,sum(jm.cpsl) as cpsl,sum(jm.cpsl*jm.cpsj) as cp_price from yh_jinxiaocun_mingxi as jm where jm.gs_name = ? and jm.mxtype = '出库' group by jm.sp_dm ) as mx_chuku on mx_chuku.sp_dm = jj.sp_dm where jj.gs_name = ? GROUP BY jj.sp_dm,jj.name,jj.lei_bie) as jxc";
                List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取进销存数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 进销存查询
     */
//    public List<YhJinXiaoCun> queryJxc(String company, String ks, String js, String spDm) {
//        base = new JxcBaseDao();
//        String sql = "select cpid as sp_dm,cpname as `name`,cplb as lei_bie,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,ifnull(bian_yuan.bianyuan,'') as bianyuan,mark1 from (select ifnull(link_rk.cpid,'') as cpid,ifnull(link_rk.cpname,'') as cpname,ifnull(link_rk.cplb,'') as cplb,ifnull(link_rk.cpsl,0) as qcsl,ifnull(link_rk.cpje,0) as qcje,ifnull(link_rk.rksl,0) as rksl,ifnull(link_rk.rkje,0) as rkje,ifnull(ck.cksl,0) as cksl,ifnull(ck.ckje,0) as ckje,ifnull(cpsl,0)+ifnull(rksl,0)-ifnull(cksl,0) as jcsl,ifnull(cpje,0)+ifnull(rkje,0)-ifnull(ckje,0) as jcje from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje from(select cp.cpid,cp.cpname,cp.cplb,qc.cpsl,qc.cpje from(select cpid,cpname,cplb from yh_jinxiaocun_qichushu where gs_name = ? union select sp_dm,cpname,cplb from  yh_jinxiaocun_mingxi where gs_name = ?) as cp left join (select cpid,cplb,cpname,sum(cpsl) as cpsl,sum(cpsj*cpsl) as cpje from yh_jinxiaocun_qichushu where gs_name = ? GROUP BY cpid,cpname,cplb) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb) as link_qc left join (select sp_dm,cpname,cplb,sum(cpsl) as rksl,sum(cpsl*cpsj) as rkje from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname  and rk.cplb = link_qc.cplb) as link_rk left join (select sp_dm,cpname,cplb,sum(cpsl) as cksl,sum(cpsl*cpsj) as ckje from yh_jinxiaocun_mingxi where mxtype = '出库' and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb) as jxc left join(select sp_dm,lei_bie,`name`,bianyuan,mark1 from yh_jinxiaocun_jichuziliao where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.`name` and jxc.cplb = bian_yuan.lei_bie  where cpid like '%' ? '%'";
//        List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company, ks, js, company, ks, js, company, spDm);
//        return list;
//    }
    public List<YhJinXiaoCun> queryJxc(String company, String ks, String js, String spDm) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 修复类型转换问题
                base2 = new JxcServerDao();
                String sql = "select cpid as sp_dm,cpname as name,cplb as lei_bie,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,isnull(bian_yuan.bianyuan,'') as bianyuan,mark1 from " +
                        "(select isnull(link_rk.cpid,'') as cpid,isnull(link_rk.cpname,'') as cpname,isnull(link_rk.cplb,'') as cplb,isnull(link_rk.cpsl,0) as qcsl,isnull(link_rk.cpje,0) as qcje,isnull(link_rk.rksl,0) as rksl,isnull(link_rk.rkje,0) as rkje,isnull(ck.cksl,0) as cksl,isnull(ck.ckje,0) as ckje, " +
                        "isnull(cpsl,0)+isnull(rksl,0)-isnull(cksl,0) as jcsl,isnull(cpje,0)+isnull(rkje,0)-isnull(ckje,0) as jcje " +
                        "from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje " +
                        "from(select cp.cpid,cp.cpname,cp.cplb,qc.cpsl,qc.cpje " +
                        "from(select cpid,cpname,cplb from yh_jinxiaocun_qichushu_mssql where gs_name = ? union select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi_mssql where gs_name = ?) as cp " +
                        "left join (select cpid,cplb,cpname,sum(CAST(cpsl AS DECIMAL(10,2))) as cpsl,sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))) as cpje from yh_jinxiaocun_qichushu_mssql where gs_name = ? GROUP BY cpid,cpname,cplb) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb) as link_qc " +
                        "left join (select sp_dm,cpname,cplb,sum(CAST(cpsl AS DECIMAL(10,2))) as rksl,sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))) as rkje from yh_jinxiaocun_mingxi_mssql where mxtype = '入库' and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname and rk.cplb = link_qc.cplb) as link_rk " +
                        "left join (select sp_dm,cpname,cplb,sum(CAST(cpsl AS DECIMAL(10,2))) as cksl,sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))) as ckje from yh_jinxiaocun_mingxi_mssql where mxtype = '出库' and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb) as jxc " +
                        "left join(select sp_dm,lei_bie,name,bianyuan,mark1 from yh_jinxiaocun_jichuziliao_mssql where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.name and jxc.cplb = bian_yuan.lei_bie " +
                        "where cpid like '%' + ? + '%'";
                List<YhJinXiaoCun> list = base2.query(YhJinXiaoCun.class, sql, company, company, company, company, ks, js, company, ks, js, company, spDm);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 保持不变
                base = new JxcBaseDao();
                String sql = "select cpid as sp_dm,cpname as `name`,cplb as lei_bie,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,ifnull(bian_yuan.bianyuan,'') as bianyuan,mark1 from (select ifnull(link_rk.cpid,'') as cpid,ifnull(link_rk.cpname,'') as cpname,ifnull(link_rk.cplb,'') as cplb,ifnull(link_rk.cpsl,0) as qcsl,ifnull(link_rk.cpje,0) as qcje,ifnull(link_rk.rksl,0) as rksl,ifnull(link_rk.rkje,0) as rkje,ifnull(ck.cksl,0) as cksl,ifnull(ck.ckje,0) as ckje,ifnull(cpsl,0)+ifnull(rksl,0)-ifnull(cksl,0) as jcsl,ifnull(cpje,0)+ifnull(rkje,0)-ifnull(ckje,0) as jcje from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje from(select cp.cpid,cp.cpname,cp.cplb,qc.cpsl,qc.cpje from(select cpid,cpname,cplb from yh_jinxiaocun_qichushu where gs_name = ? union select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name = ?) as cp left join (select cpid,cplb,cpname,sum(cpsl) as cpsl,sum(cpsj*cpsl) as cpje from yh_jinxiaocun_qichushu where gs_name = ? GROUP BY cpid,cpname,cplb) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb) as link_qc left join (select sp_dm,cpname,cplb,sum(cpsl) as rksl,sum(cpsl*cpsj) as rkje from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname and rk.cplb = link_qc.cplb) as link_rk left join (select sp_dm,cpname,cplb,sum(cpsl) as cksl,sum(cpsl*cpsj) as ckje from yh_jinxiaocun_mingxi where mxtype = '出库' and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb) as jxc left join(select sp_dm,lei_bie,`name`,bianyuan,mark1 from yh_jinxiaocun_jichuziliao where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.`name` and jxc.cplb = bian_yuan.lei_bie where cpid like concat('%', ?, '%')";
                List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company, ks, js, company, ks, js, company, spDm);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "查询进销存数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    /**
     * 进销存查询
     */
//    public List<YhJinXiaoCun> orderQueryJxc(String company, String spDm) {
//        base = new JxcBaseDao();
//        String sql = "select cpid as sp_dm,cpname as `name`,cplb as lei_bie,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,ifnull(bian_yuan.bianyuan,'') as bianyuan,mark1 from (select ifnull(link_rk.cpid,'') as cpid,ifnull(link_rk.cpname,'') as cpname,ifnull(link_rk.cplb,'') as cplb,ifnull(link_rk.cpsl,0) as qcsl,ifnull(link_rk.cpje,0) as qcje,ifnull(link_rk.rksl,0) as rksl,ifnull(link_rk.rkje,0) as rkje,ifnull(ck.cksl,0) as cksl,ifnull(ck.ckje,0) as ckje,ifnull(cpsl,0)+ifnull(rksl,0)-ifnull(cksl,0) as jcsl,ifnull(cpje,0)+ifnull(rkje,0)-ifnull(ckje,0) as jcje from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje from(select cp.cpid,cp.cpname,cp.cplb,qc.cpsl,qc.cpje from(select cpid,cpname,cplb from yh_jinxiaocun_qichushu where gs_name = ? union select sp_dm,cpname,cplb from  yh_jinxiaocun_mingxi where gs_name = ?) as cp left join (select cpid,cplb,cpname,sum(cpsl) as cpsl,sum(cpsj*cpsl) as cpje from yh_jinxiaocun_qichushu where gs_name = ? GROUP BY cpid,cpname,cplb) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb) as link_qc left join (select sp_dm,cpname,cplb,sum(cpsl) as rksl,sum(cpsl*cpsj) as rkje from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm,cpname,cplb) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname  and rk.cplb = link_qc.cplb) as link_rk left join (select sp_dm,cpname,cplb,sum(cpsl) as cksl,sum(cpsl*cpsj) as ckje from yh_jinxiaocun_mingxi where mxtype = '出库' and gs_name = ? group by sp_dm,cpname,cplb) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb) as jxc left join(select sp_dm,lei_bie,`name`,bianyuan,mark1 from yh_jinxiaocun_jichuziliao where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.`name` and jxc.cplb = bian_yuan.lei_bie " + spDm;
//        List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company, company, company);
//        return list;
//    }
    public List<YhJinXiaoCun> orderQueryJxc(String company, String spDm) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                String sql = "select cpid as sp_dm,cpname as name,cplb as lei_bie,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,isnull(bian_yuan.bianyuan,'') as bianyuan,mark1 from (select isnull(link_rk.cpid,'') as cpid,isnull(link_rk.cpname,'') as cpname,isnull(link_rk.cplb,'') as cplb,isnull(link_rk.cpsl,0) as qcsl,isnull(link_rk.cpje,0) as qcje,isnull(link_rk.rksl,0) as rksl,isnull(link_rk.rkje,0) as rkje,isnull(ck.cksl,0) as cksl,isnull(ck.ckje,0) as ckje,isnull(cpsl,0)+isnull(rksl,0)-isnull(cksl,0) as jcsl,isnull(cpje,0)+isnull(rkje,0)-isnull(ckje,0) as jcje from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje from(select cp.cpid,cp.cpname,cp.cplb,qc.cpsl,qc.cpje from(select cpid,cpname,cplb from yh_jinxiaocun_qichushu_mssql where gs_name = ? union select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi_mssql where gs_name = ?) as cp left join (select cpid,cplb,cpname,sum(cpsl) as cpsl,sum(cpsj*cpsl) as cpje from yh_jinxiaocun_qichushu_mssql where gs_name = ? GROUP BY cpid,cpname,cplb) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb) as link_qc left join (select sp_dm,cpname,cplb,sum(cpsl) as rksl,sum(cpsl*cpsj) as rkje from yh_jinxiaocun_mingxi_mssql where mxtype = '入库' and gs_name = ? group by sp_dm,cpname,cplb) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname and rk.cplb = link_qc.cplb) as link_rk left join (select sp_dm,cpname,cplb,sum(cpsl) as cksl,sum(cpsl*cpsj) as ckje from yh_jinxiaocun_mingxi_mssql where mxtype = '出库' and gs_name = ? group by sp_dm,cpname,cplb) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb) as jxc left join(select sp_dm,lei_bie,name,bianyuan,mark1 from yh_jinxiaocun_jichuziliao_mssql where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.name and jxc.cplb = bian_yuan.lei_bie " + spDm;
                List<YhJinXiaoCun> list = base2.query(YhJinXiaoCun.class, sql, company, company, company, company, company, company);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                String sql = "select cpid as sp_dm,cpname as `name`,cplb as lei_bie,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,ifnull(bian_yuan.bianyuan,'') as bianyuan,mark1 from (select ifnull(link_rk.cpid,'') as cpid,ifnull(link_rk.cpname,'') as cpname,ifnull(link_rk.cplb,'') as cplb,ifnull(link_rk.cpsl,0) as qcsl,ifnull(link_rk.cpje,0) as qcje,ifnull(link_rk.rksl,0) as rksl,ifnull(link_rk.rkje,0) as rkje,ifnull(ck.cksl,0) as cksl,ifnull(ck.ckje,0) as ckje,ifnull(cpsl,0)+ifnull(rksl,0)-ifnull(cksl,0) as jcsl,ifnull(cpje,0)+ifnull(rkje,0)-ifnull(ckje,0) as jcje from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje from(select cp.cpid,cp.cpname,cp.cplb,qc.cpsl,qc.cpje from(select cpid,cpname,cplb from yh_jinxiaocun_qichushu where gs_name = ? union select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name = ?) as cp left join (select cpid,cplb,cpname,sum(cpsl) as cpsl,sum(cpsj*cpsl) as cpje from yh_jinxiaocun_qichushu where gs_name = ? GROUP BY cpid,cpname,cplb) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb) as link_qc left join (select sp_dm,cpname,cplb,sum(cpsl) as rksl,sum(cpsl*cpsj) as rkje from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm,cpname,cplb) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname and rk.cplb = link_qc.cplb) as link_rk left join (select sp_dm,cpname,cplb,sum(cpsl) as cksl,sum(cpsl*cpsj) as ckje from yh_jinxiaocun_mingxi where mxtype = '出库' and gs_name = ? group by sp_dm,cpname,cplb) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb) as jxc left join(select sp_dm,lei_bie,`name`,bianyuan,mark1 from yh_jinxiaocun_jichuziliao where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.`name` and jxc.cplb = bian_yuan.lei_bie " + spDm;
                List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company, company, company);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "排序查询进销存数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
