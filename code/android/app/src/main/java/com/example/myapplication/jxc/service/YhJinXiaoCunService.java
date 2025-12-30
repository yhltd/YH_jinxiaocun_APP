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
    public List<YhJinXiaoCun> queryJxc(String company, String ks, String js, String spDm, String cangku) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本 - 修复类型转换问题
                base2 = new JxcServerDao();
                String sql = "select cpid as sp_dm,cpname as name,cplb as lei_bie,cangku as cangku,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,isnull(bian_yuan.bianyuan,'') as bianyuan,mark1, CASE WHEN (qcje + jcje) / 2 != 0 THEN ROUND((ckje / ((qcje + jcje) / 2)) * 100, 2) ELSE 0 END as zzl from " +
                        "(select isnull(link_rk.cpid,'') as cpid,isnull(link_rk.cpname,'') as cpname,isnull(link_rk.cplb,'') as cplb,isnull(link_rk.cangku,'') as cangku,isnull(link_rk.cpsl,0) as qcsl,isnull(link_rk.cpje,0) as qcje,isnull(link_rk.rksl,0) as rksl,isnull(link_rk.rkje,0) as rkje,isnull(ck.cksl,0) as cksl,isnull(ck.ckje,0) as ckje, " +
                        "isnull(cpsl,0)+isnull(rksl,0)-isnull(cksl,0) as jcsl,isnull(cpje,0)+isnull(rkje,0)-isnull(ckje,0) as jcje " +
                        "from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cangku,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje " +
                        "from(select cp.cpid,cp.cpname,cp.cplb,cp.cangku,qc.cpsl,qc.cpje " +
                        "from(select cpid,cpname,cplb,cangku from yh_jinxiaocun_qichushu where gs_name = ? union select sp_dm,cpname,cplb,cangku from yh_jinxiaocun_mingxi where gs_name = ?) as cp " +
                        "left join (select cpid,cplb,cpname,cangku,sum(cast(cpsl as decimal(10,2))) as cpsl,sum(cast(cpsl as decimal(10,2))*cast(cpsj as decimal(10,2))) as cpje from yh_jinxiaocun_qichushu where gs_name = ? GROUP BY cpid,cpname,cplb,cangku) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb and cp.cangku = qc.cangku) as link_qc " +
                        "left join (select sp_dm,cpname,cplb,cangku,sum(cast(cpsl as decimal(10,2))) as rksl,sum(cast(cpsl as decimal(10,2))*cast(cpsj as decimal(10,2))) as rkje from yh_jinxiaocun_mingxi where mxtype IN ('入库', '调拨入库', '盘盈入库') and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb,cangku) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname and rk.cplb = link_qc.cplb and rk.cangku = link_qc.cangku) as link_rk " +
                        "left join (select sp_dm,cpname,cplb,cangku,sum(cast(cpsl as decimal(10,2))) as cksl,sum(cast(cpsl as decimal(10,2))*cast(cpsj as decimal(10,2))) as ckje from yh_jinxiaocun_mingxi where mxtype IN ('出库', '调拨出库', '盘亏出库') and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb,cangku) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb and ck.cangku = link_rk.cangku) as jxc " +
                        "left join(select sp_dm,lei_bie,name,bianyuan,mark1 from yh_jinxiaocun_jichuziliao where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.name and jxc.cplb = bian_yuan.lei_bie " +
                        "where cpid like '%' + ? + '%' " +
                        "and jxc.cangku like '%' + ? + '%' " +
                        "group by cpid,cpname,cplb,cangku,bianyuan,mark1";
                List<YhJinXiaoCun> list = base2.query(YhJinXiaoCun.class, sql, company, company, company, company, ks, js, company, ks, js, company, spDm,cangku);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本 - 保持不变
                base = new JxcBaseDao();
                String sql = "select cpid as sp_dm,cpname as `name`,cplb as lei_bie,cangku as cangku,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,ifnull(bian_yuan.bianyuan,'') as bianyuan,mark1, CASE WHEN (qcje + jcje) / 2 != 0 THEN ROUND((ckje / ((qcje + jcje) / 2)) * 100, 2) ELSE 0 END as zzl from " +
                        "(select ifnull(link_rk.cpid,'') as cpid,ifnull(link_rk.cpname,'') as cpname,ifnull(link_rk.cplb,'') as cplb,ifnull(link_rk.cangku,'') as cangku,ifnull(link_rk.cpsl,0) as qcsl,ifnull(link_rk.cpje,0) as qcje,ifnull(link_rk.rksl,0) as rksl,ifnull(link_rk.rkje,0) as rkje,ifnull(ck.cksl,0) as cksl,ifnull(ck.ckje,0) as ckje, " +
                        "ifnull(cpsl,0)+ifnull(rksl,0)-ifnull(cksl,0) as jcsl,ifnull(cpje,0)+ifnull(rkje,0)-ifnull(ckje,0) as jcje " +
                        "from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cangku,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje " +
                        "from(select cp.cpid,cp.cpname,cp.cplb,cp.cangku,qc.cpsl,qc.cpje " +
                        "from(select cpid,cpname,cplb,cangku from yh_jinxiaocun_qichushu where gs_name = ? union select sp_dm,cpname,cplb,cangku from yh_jinxiaocun_mingxi where gs_name = ?) as cp " +
                        "left join (select cpid,cplb,cpname,cangku,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cpje from yh_jinxiaocun_qichushu where gs_name = ? GROUP BY cpid,cpname,cplb,cangku) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb and cp.cangku = qc.cangku) as link_qc " +
                        "left join (select sp_dm,cpname,cplb,cangku,sum(cpsl) as rksl,sum(cpsl*cpsj) as rkje from yh_jinxiaocun_mingxi where mxtype IN ('入库', '调拨入库', '盘盈入库') and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb,cangku) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname and rk.cplb = link_qc.cplb and rk.cangku = link_qc.cangku) as link_rk " +
                        "left join (select sp_dm,cpname,cplb,cangku,sum(cpsl) as cksl,sum(cpsl*cpsj) as ckje from yh_jinxiaocun_mingxi where mxtype IN ('出库', '调拨出库', '盘亏出库') and gs_name = ? and shijian between ? and ? group by sp_dm,cpname,cplb,cangku) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb and ck.cangku = link_rk.cangku) as jxc " +
                        "left join(select sp_dm,lei_bie,`name`,bianyuan,mark1 from yh_jinxiaocun_jichuziliao where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.`name` and jxc.cplb = bian_yuan.lei_bie " +
                        "where cpid like CONCAT('%', ?, '%') " +
                        "and jxc.cangku like CONCAT('%', ?, '%') " +
                        "group by cpid,cpname,cplb,cangku,bianyuan,mark1";
                List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company, ks, js, company, ks, js, company, spDm,cangku);
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
                String sql = "select cpid as sp_dm,cpname as name,cplb as lei_bie,cangku as cangku,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,isnull(bian_yuan.bianyuan,'') as bianyuan,mark1, " +
                        "CASE WHEN (qcje + jcje) / 2 != 0 THEN ROUND((ckje / ((qcje + jcje) / 2)) * 100, 2) ELSE 0 END as zzl " +
                        "from (select isnull(link_rk.cpid,'') as cpid,isnull(link_rk.cpname,'') as cpname,isnull(link_rk.cplb,'') as cplb,isnull(link_rk.cangku,'') as cangku,isnull(link_rk.cpsl,0) as qcsl,isnull(link_rk.cpje,0) as qcje,isnull(link_rk.rksl,0) as rksl,isnull(link_rk.rkje,0) as rkje,isnull(ck.cksl,0) as cksl,isnull(ck.ckje,0) as ckje,isnull(cpsl,0)+isnull(rksl,0)-isnull(cksl,0) as jcsl,isnull(cpje,0)+isnull(rkje,0)-isnull(ckje,0) as jcje " +
                        "from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cangku,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje " +
                        "from(select cp.cpid,cp.cpname,cp.cplb,cp.cangku,qc.cpsl,qc.cpje " +
                        "from(select cpid,cpname,cplb,cangku from yh_jinxiaocun_qichushu where gs_name = ? union select sp_dm,cpname,cplb,cangku from yh_jinxiaocun_mingxi where gs_name = ?) as cp " +
                        "left join (select cpid,cplb,cpname,cangku,sum(cpsl) as cpsl,sum(cpsj*cpsl) as cpje from yh_jinxiaocun_qichushu where gs_name = ? GROUP BY cpid,cpname,cplb,cangku) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb and cp.cangku = qc.cangku) as link_qc " +
                        "left join (select sp_dm,cpname,cplb,cangku,sum(cpsl) as rksl,sum(cpsl*cpsj) as rkje from yh_jinxiaocun_mingxi where mxtype IN ('入库', '盘盈入库', '调拨入库') and gs_name = ? group by sp_dm,cpname,cplb,cangku) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname and rk.cplb = link_qc.cplb and rk.cangku = link_qc.cangku) as link_rk " +
                        "left join (select sp_dm,cpname,cplb,cangku,sum(cpsl) as cksl,sum(cpsl*cpsj) as ckje from yh_jinxiaocun_mingxi where mxtype IN ('出库', '盘亏出库', '调拨出库') and gs_name = ? group by sp_dm,cpname,cplb,cangku) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb and ck.cangku = link_rk.cangku) as jxc " +
                        "left join(select sp_dm,lei_bie,name,bianyuan,mark1 from yh_jinxiaocun_jichuziliao where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.name and jxc.cplb = bian_yuan.lei_bie " + spDm;
                List<YhJinXiaoCun> list = base2.query(YhJinXiaoCun.class, sql, company, company, company, company, company, company);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                String sql = "select cpid as sp_dm,cpname as `name`,cplb as lei_bie,cangku as cangku,qcsl as jq_cpsl,qcje as jq_price,rksl as mx_ruku_cpsl,rkje as mx_ruku_price,cksl as mx_chuku_cpsl,ckje as mx_chuku_price,jcsl as jc_sl,jcje as jc_price,ifnull(bian_yuan.bianyuan,'') as bianyuan,mark1, " +
                        "CASE WHEN (qcje + jcje) / 2 != 0 THEN ROUND((ckje / ((qcje + jcje) / 2)) * 100, 2) ELSE 0 END as zzl " +
                        "from (select ifnull(link_rk.cpid,'') as cpid,ifnull(link_rk.cpname,'') as cpname,ifnull(link_rk.cplb,'') as cplb,ifnull(link_rk.cangku,'') as cangku,ifnull(link_rk.cpsl,0) as qcsl,ifnull(link_rk.cpje,0) as qcje,ifnull(link_rk.rksl,0) as rksl,ifnull(link_rk.rkje,0) as rkje,ifnull(ck.cksl,0) as cksl,ifnull(ck.ckje,0) as ckje,ifnull(cpsl,0)+ifnull(rksl,0)-ifnull(cksl,0) as jcsl,ifnull(cpje,0)+ifnull(rkje,0)-ifnull(ckje,0) as jcje " +
                        "from (select link_qc.cpid,link_qc.cpname,link_qc.cplb,link_qc.cangku,link_qc.cpsl,link_qc.cpje,rk.rksl,rk.rkje " +
                        "from(select cp.cpid,cp.cpname,cp.cplb,cp.cangku,qc.cpsl,qc.cpje " +
                        "from(select cpid,cpname,cplb,cangku from yh_jinxiaocun_qichushu where gs_name = ? union select sp_dm,cpname,cplb,cangku from yh_jinxiaocun_mingxi where gs_name = ?) as cp " +
                        "left join (select cpid,cplb,cpname,cangku,sum(cpsl) as cpsl,sum(cpsj*cpsl) as cpje from yh_jinxiaocun_qichushu where gs_name = ? GROUP BY cpid,cpname,cplb,cangku) as qc on cp.cpid = qc.cpid and cp.cpname = qc.cpname and cp.cplb = qc.cplb and cp.cangku = qc.cangku) as link_qc " +
                        "left join (select sp_dm,cpname,cplb,cangku,sum(cpsl) as rksl,sum(cpsl*cpsj) as rkje from yh_jinxiaocun_mingxi where mxtype IN ('入库', '盘盈入库', '调拨入库') and gs_name = ? group by sp_dm,cpname,cplb,cangku) as rk on rk.sp_dm = link_qc.cpid and rk.cpname = link_qc.cpname and rk.cplb = link_qc.cplb and rk.cangku = link_qc.cangku) as link_rk " +
                        "left join (select sp_dm,cpname,cplb,cangku,sum(cpsl) as cksl,sum(cpsl*cpsj) as ckje from yh_jinxiaocun_mingxi where mxtype IN ('出库', '盘亏出库', '调拨出库') and gs_name = ? group by sp_dm,cpname,cplb,cangku) as ck on ck.sp_dm = link_rk.cpid and ck.cpname = link_rk.cpname and ck.cplb = link_rk.cplb and ck.cangku = link_rk.cangku) as jxc " +
                        "left join(select sp_dm,lei_bie,`name`,bianyuan,mark1 from yh_jinxiaocun_jichuziliao where gs_name = ?) as bian_yuan on jxc.cpid = bian_yuan.sp_dm and jxc.cpname = bian_yuan.`name` and jxc.cplb = bian_yuan.lei_bie " + spDm;
                List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company, company, company);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "排序查询进销存数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * 期末统计
     */



    public List<YhJinXiaoCun> qimotongji_shaixuan(String company, String ks, String js, String cangku) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "SELECT " +
                        "cangku, " +
                        "FORMAT(shijian, 'yyyy/MM') as month, " +
                        "SUM(CASE WHEN mxtype IN ('入库', '调拨入库', '盘盈入库') THEN cpsl ELSE 0 END) as mx_ruku_cpsl, " +
                        "SUM(CASE WHEN mxtype IN ('入库', '调拨入库', '盘盈入库') THEN cpsl * cpsj ELSE 0 END) as mx_ruku_price, " +
                        "SUM(CASE WHEN mxtype IN ('出库', '调拨出库', '盘亏出库') THEN cpsl ELSE 0 END) as mx_chuku_cpsl, " +
                        "SUM(CASE WHEN mxtype IN ('出库', '调拨出库', '盘亏出库') THEN cpsl * cpsj ELSE 0 END) as mx_chuku_price, " +
                        "( " +
                        "    ISNULL((SELECT SUM(qc.cpsl) FROM yh_jinxiaocun_qichushu_mssql qc " +
                        "              WHERE qc.gs_name = ? " +
                        "             ), 0) + " +
                        "    SUM(CASE WHEN mxtype IN ('入库', '调拨入库', '盘盈入库') AND " +
                        "                 FORMAT(shijian, 'yyyy-MM') <= FORMAT(main.shijian, 'yyyy-MM') " +
                        "            THEN cpsl ELSE 0 END) - " +
                        "    SUM(CASE WHEN mxtype IN ('出库', '调拨出库', '盘亏出库') AND " +
                        "                 FORMAT(shijian, 'yyyy-MM') <= FORMAT(main.shijian, 'yyyy-MM') " +
                        "            THEN cpsl ELSE 0 END) " +
                        ") as jc_sl, " +
                        "( " +
                        "    ISNULL((SELECT SUM(qc.cpsl * qc.cpsj) FROM yh_jinxiaocun_qichushu_mssql qc " +
                        "              WHERE qc.gs_name = ? " +
                        "             ), 0) + " +
                        "    SUM(CASE WHEN mxtype IN ('入库', '调拨入库', '盘盈入库') AND " +
                        "                 FORMAT(shijian, 'yyyy-MM') <= FORMAT(main.shijian, 'yyyy-MM') " +
                        "            THEN cpsl * cpsj ELSE 0 END) - " +
                        "    SUM(CASE WHEN mxtype IN ('出库', '调拨出库', '盘亏出库') AND " +
                        "                 FORMAT(shijian, 'yyyy-MM') <= FORMAT(main.shijian, 'yyyy-MM') " +
                        "            THEN cpsl * cpsj ELSE 0 END) " +
                        ") as jc_price " +
                        "FROM yh_jinxiaocun_mingxi_mssql main " +
                        "WHERE gs_name = ? " +
                        "AND cangku LIKE '%' + ? + '%' " +  // SQL Server字符串连接
                        "AND shijian BETWEEN ? AND ? " +    // 时间范围筛选
                        "GROUP BY cangku, FORMAT(shijian, 'yyyy/MM') " +
                        "ORDER BY cangku, month";

                base2 = new JxcServerDao();
                Log.e("SQLDebug", "查询统计SQL: " + sql);
                List<YhJinXiaoCun> list = base2.query(YhJinXiaoCun.class, sql, company, company, company, cangku, ks, js);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "SELECT " +
                        "cangku, " +
                        "DATE_FORMAT(shijian, '%Y/%m') as month, " +
                        "SUM(CASE WHEN mxtype IN ('入库', '调拨入库', '盘盈入库') THEN cpsl ELSE 0 END) as mx_ruku_cpsl, " +
                        "SUM(CASE WHEN mxtype IN ('入库', '调拨入库', '盘盈入库') THEN cpsl * cpsj ELSE 0 END) as mx_ruku_price, " +
                        "SUM(CASE WHEN mxtype IN ('出库', '调拨出库', '盘亏出库') THEN cpsl ELSE 0 END) as mx_chuku_cpsl, " +
                        "SUM(CASE WHEN mxtype IN ('出库', '调拨出库', '盘亏出库') THEN cpsl * cpsj ELSE 0 END) as mx_chuku_price, " +
                        "( " +
                        "    COALESCE((SELECT SUM(qc.cpsl) FROM yh_jinxiaocun_qichushu qc " +
                        "              WHERE qc.gs_name = ? " +
                        "             ), 0) + " +
                        "    SUM(CASE WHEN mxtype IN ('入库', '调拨入库', '盘盈入库') AND " +
                        "                 DATE_FORMAT(shijian, '%Y-%m') <= DATE_FORMAT(main.shijian, '%Y-%m') " +
                        "            THEN cpsl ELSE 0 END) - " +
                        "    SUM(CASE WHEN mxtype IN ('出库', '调拨出库', '盘亏出库') AND " +
                        "                 DATE_FORMAT(shijian, '%Y-%m') <= DATE_FORMAT(main.shijian, '%Y-%m') " +
                        "            THEN cpsl ELSE 0 END) " +
                        ") as jc_sl, " +
                        "( " +
                        "    COALESCE((SELECT SUM(qc.cpsl * qc.cpsj) FROM yh_jinxiaocun_qichushu qc " +
                        "              WHERE qc.gs_name = ? " +
                        "             ), 0) + " +
                        "    SUM(CASE WHEN mxtype IN ('入库', '调拨入库', '盘盈入库') AND " +
                        "                 DATE_FORMAT(shijian, '%Y-%m') <= DATE_FORMAT(main.shijian, '%Y-%m') " +
                        "            THEN cpsl * cpsj ELSE 0 END) - " +
                        "    SUM(CASE WHEN mxtype IN ('出库', '调拨出库', '盘亏出库') AND " +
                        "                 DATE_FORMAT(shijian, '%Y-%m') <= DATE_FORMAT(main.shijian, '%Y-%m') " +
                        "            THEN cpsl * cpsj ELSE 0 END) " +
                        ") as jc_price " +
                        "FROM yh_jinxiaocun_mingxi main " +
                        "WHERE gs_name = ? " +
                        "AND cangku LIKE CONCAT('%', ?, '%') " +  // 添加仓库模糊匹配
                        "AND shijian BETWEEN ? AND ? " +          // 添加时间范围筛选
                        "GROUP BY cangku, DATE_FORMAT(shijian, '%Y/%m') " +
                        "ORDER BY cangku, month";

                base = new JxcBaseDao();  // 使用MySQL的DAO
                Log.e("SQLDebug", "查询统计SQL: " + sql);
                List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, cangku, ks, js);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取期末统计列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



    /**
     * 库存积压
     */



    public List<YhJinXiaoCun> kucunjiya(String company, String ks, String js, String jiyanum) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "SELECT " +
                        "jxc.cpid AS sp_dm, " +
                        "jxc.cpname AS name, " +
                        "jxc.cplb AS lei_bie, " +
                        "jxc.qcsl, jxc.qcje, jxc.rksl, jxc.rkje, " +
                        "jxc.cksl AS mx_chuku_cpsl, " +
                        "jxc.ckje, " +
                        "jxc.jcsl AS jc_sl, " +
                        "jxc.jcje AS jc_price, " +
                        "jxc.cangku, " +
                        "ISNULL(bian_yuan.bianyuan, '') AS bianyuan, " +
                        "ISNULL(bian_yuan.mark1, '') AS mark1, " +
                        "ISNULL(last_ck.cpsj, 0) AS yjdj, " +
                        "CASE " +
                        "  WHEN (ISNULL(jxc.qimo_sl, 0) > 0 AND ISNULL(period_ck.cksl, 0) > 0) " +
                        "  THEN ROUND(CAST(ISNULL(period_ck.cksl, 0) AS FLOAT) / ISNULL(jxc.qimo_sl, 0), 2) " +
                        "  ELSE 0 " +
                        "END AS zhouzhuan " +
                        "FROM ( " +
                        "  SELECT " +
                        "    ISNULL(link_rk.cpid, '') AS cpid, " +
                        "    ISNULL(link_rk.cpname, '') AS cpname, " +
                        "    ISNULL(link_rk.cplb, '') AS cplb, " +
                        "    ISNULL(link_rk.cpsl, 0) AS qcsl, " +
                        "    ISNULL(link_rk.cpje, 0) AS qcje, " +
                        "    ISNULL(link_rk.rksl, 0) AS rksl, " +
                        "    ISNULL(link_rk.rkje, 0) AS rkje, " +
                        "    ISNULL(ck.cksl, 0) AS cksl, " +
                        "    ISNULL(ck.ckje, 0) AS ckje, " +
                        "    ISNULL(link_rk.cpsl, 0) + ISNULL(link_rk.rksl, 0) - ISNULL(ck.cksl, 0) AS jcsl, " +
                        "    ISNULL(link_rk.cpje, 0) + ISNULL(link_rk.rkje, 0) - ISNULL(ck.ckje, 0) AS jcje, " +
                        "    ISNULL(cangku_info.cangku, '') AS cangku, " +
                        "    (ISNULL(link_rk.cpsl, 0) + ISNULL(all_rk.rksl, 0) - ISNULL(all_ck.cksl, 0)) AS qimo_sl " +
                        "  FROM ( " +
                        "    SELECT " +
                        "      link_qc.cpid, " +
                        "      link_qc.cpname, " +
                        "      link_qc.cplb, " +
                        "      link_qc.cpsl, " +
                        "      link_qc.cpje, " +
                        "      rk.rksl, " +
                        "      rk.rkje " +
                        "    FROM ( " +
                        "      SELECT " +
                        "        cp.cpid, " +
                        "        cp.cpname, " +
                        "        cp.cplb, " +
                        "        qc.cpsl, " +
                        "        qc.cpje " +
                        "      FROM ( " +
                        "        SELECT cpid, cpname, cplb FROM yh_jinxiaocun_qichushu_mssql WHERE gs_name = ? " +  // 参数1
                        "        UNION " +
                        "        SELECT sp_dm, cpname, cplb FROM yh_jinxiaocun_mingxi_mssql WHERE gs_name = ? " +  // 参数2
                        "      ) AS cp " +
                        "      LEFT JOIN ( " +
                        "        SELECT " +
                        "          cpid, " +
                        "          cplb, " +
                        "          cpname, " +
                        "          SUM(cpsl) AS cpsl, " +
                        "          SUM(cpsj * cpsl) AS cpje " +
                        "        FROM yh_jinxiaocun_qichushu_mssql " +
                        "        WHERE gs_name = ? " +  // 参数3
                        "        GROUP BY cpid, cpname, cplb " +
                        "      ) AS qc ON cp.cpid = qc.cpid AND cp.cpname = qc.cpname AND cp.cplb = qc.cplb " +
                        "    ) AS link_qc " +
                        "    LEFT JOIN ( " +
                        "      SELECT " +
                        "        sp_dm, " +
                        "        cpname, " +
                        "        cplb, " +
                        "        SUM(cpsl) AS rksl, " +
                        "        SUM(cpsl * cpsj) AS rkje " +
                        "      FROM yh_jinxiaocun_mingxi_mssql " +
                        "      WHERE (mxtype = '入库' OR mxtype = '调拨入库' OR mxtype = '盘盈入库') " +
                        "        AND gs_name = ? " +  // 参数4
                        "        AND shijian BETWEEN ? AND ? " +  // 参数5,6
                        "      GROUP BY sp_dm, cpname, cplb " +
                        "    ) AS rk ON rk.sp_dm = link_qc.cpid AND rk.cpname = link_qc.cpname AND rk.cplb = link_qc.cplb " +
                        "  ) AS link_rk " +
                        "  LEFT JOIN ( " +
                        "    SELECT " +
                        "      sp_dm, " +
                        "      cpname, " +
                        "      cplb, " +
                        "      SUM(cpsl) AS cksl, " +
                        "      SUM(cpsl * cpsj) AS ckje " +
                        "    FROM yh_jinxiaocun_mingxi_mssql " +
                        "    WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') " +
                        "      AND gs_name = ? " +  // 参数7
                        "      AND shijian BETWEEN ? AND ? " +  // 参数8,9
                        "    GROUP BY sp_dm, cpname, cplb " +
                        "  ) AS ck ON ck.sp_dm = link_rk.cpid AND ck.cpname = link_rk.cpname AND ck.cplb = link_rk.cplb " +
                        "  LEFT JOIN ( " +
                        "    SELECT sp_dm, cangku " +
                        "    FROM yh_jinxiaocun_mingxi_mssql " +
                        "    WHERE gs_name = ? " +  // 参数10
                        "    GROUP BY sp_dm, cangku " +
                        "  ) AS cangku_info ON link_rk.cpid = cangku_info.sp_dm " +
                        "  LEFT JOIN ( " +
                        "    SELECT " +
                        "      sp_dm, " +
                        "      cpname, " +
                        "      cplb, " +
                        "      SUM(cpsl) AS rksl " +
                        "    FROM yh_jinxiaocun_mingxi_mssql " +
                        "    WHERE (mxtype = '入库' OR mxtype = '调拨入库' OR mxtype = '盘盈入库') " +
                        "      AND gs_name = ? " +  // 参数11
                        "      AND shijian <= ? " +  // 参数12 (结束时间)
                        "    GROUP BY sp_dm, cpname, cplb " +
                        "  ) AS all_rk ON link_rk.cpid = all_rk.sp_dm AND link_rk.cpname = all_rk.cpname AND link_rk.cplb = all_rk.cplb " +
                        "  LEFT JOIN ( " +
                        "    SELECT " +
                        "      sp_dm, " +
                        "      cpname, " +
                        "      cplb, " +
                        "      SUM(cpsl) AS cksl " +
                        "    FROM yh_jinxiaocun_mingxi_mssql " +
                        "    WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') " +
                        "      AND gs_name = ? " +  // 参数13
                        "      AND shijian <= ? " +  // 参数14 (结束时间)
                        "    GROUP BY sp_dm, cpname, cplb " +
                        "  ) AS all_ck ON link_rk.cpid = all_ck.sp_dm AND link_rk.cpname = all_ck.cpname AND link_rk.cplb = all_ck.cplb " +
                        ") AS jxc " +
                        "LEFT JOIN ( " +
                        "  SELECT sp_dm, lei_bie, name, bianyuan, mark1 " +
                        "  FROM yh_jinxiaocun_jichuziliao_mssql " +
                        "  WHERE gs_name = ? " +  // 参数15
                        ") AS bian_yuan ON jxc.cpid = bian_yuan.sp_dm AND jxc.cpname = bian_yuan.name AND jxc.cplb = bian_yuan.lei_bie " +
                        "LEFT JOIN ( " +
                        "  SELECT " +
                        "    m1.sp_dm, " +
                        "    m1.cpname, " +
                        "    m1.cplb, " +
                        "    m1.cpsj " +
                        "  FROM yh_jinxiaocun_mingxi_mssql m1 " +
                        "  INNER JOIN ( " +
                        "    SELECT " +
                        "      sp_dm, " +
                        "      cpname, " +
                        "      cplb, " +
                        "      MAX(shijian) AS max_shijian " +
                        "    FROM yh_jinxiaocun_mingxi_mssql " +
                        "    WHERE mxtype = '出库' AND gs_name = ? " +  // 参数16
                        "    GROUP BY sp_dm, cpname, cplb " +
                        "  ) m2 ON m1.sp_dm = m2.sp_dm AND m1.cpname = m2.cpname AND m1.cplb = m2.cplb AND m1.shijian = m2.max_shijian " +
                        "  WHERE m1.mxtype = '出库' AND m1.gs_name = ? " +  // 参数17
                        ") AS last_ck ON jxc.cpid = last_ck.sp_dm AND jxc.cpname = last_ck.cpname AND jxc.cplb = last_ck.cplb " +
                        "LEFT JOIN ( " +
                        "  SELECT " +
                        "    sp_dm, " +
                        "    cpname, " +
                        "    cplb, " +
                        "    SUM(cpsl) AS cksl " +
                        "  FROM yh_jinxiaocun_mingxi_mssql " +
                        "  WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') " +
                        "    AND gs_name = ? " +  // 参数18
                        "    AND shijian BETWEEN ? AND ? " +  // 参数19,20
                        "  GROUP BY sp_dm, cpname, cplb " +
                        ") AS period_ck ON jxc.cpid = period_ck.sp_dm AND jxc.cpname = period_ck.cpname AND jxc.cplb = period_ck.cplb " +
                        "WHERE jxc.cksl < ? " +  // 参数21
                        "ORDER BY jxc.cpname, jxc.cangku";

                base2 = new JxcServerDao();
                Log.e("SQLDebug", "查询统计SQL: " + sql);
                List<YhJinXiaoCun> list = base2.query(YhJinXiaoCun.class, sql, company, company, company, company, ks, js,company,ks,js,company,company,js,company,js,company,company,company,company,ks,js,jiyanum);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                Log.d("SQLDebug", "执行查询 - 公司: " + company +
                        ", 开始: " + ks +
                        ", 结束: " + js +
                        ", 订单数: " + jiyanum);
                String sql = "SELECT " +
                        "jxc.cpid AS sp_dm, " +
                        "jxc.cpname AS name, " +
                        "jxc.cplb AS lei_bie, " +
                        "jxc.qcsl, jxc.qcje, jxc.rksl, jxc.rkje, " +
                        "jxc.cksl AS mx_chuku_cpsl, " +
                        "jxc.ckje, " +
                        "jxc.jcsl AS jc_sl, " +
                        "jxc.jcje AS jc_price, " +
                        "jxc.cangku, " +
                        "IFNULL(bian_yuan.bianyuan, '') AS bianyuan, " +
                        "IFNULL(bian_yuan.mark1, '') AS mark1, " +
                        "IFNULL(last_ck.cpsj, 0) AS yjdj, " +
                        "CASE " +
                        "  WHEN (IFNULL(jxc.qimo_sl, 0) > 0 AND IFNULL(period_ck.cksl, 0) > 0) " +
                        "  THEN ROUND(IFNULL(period_ck.cksl, 0) / IFNULL(jxc.qimo_sl, 0), 2) " +
                        "  ELSE 0 " +
                        "END AS zhouzhuan " +
                        "FROM ( " +
                        "  SELECT " +
                        "    IFNULL(link_rk.cpid, '') AS cpid, " +
                        "    IFNULL(link_rk.cpname, '') AS cpname, " +
                        "    IFNULL(link_rk.cplb, '') AS cplb, " +
                        "    IFNULL(link_rk.cpsl, 0) AS qcsl, " +
                        "    IFNULL(link_rk.cpje, 0) AS qcje, " +
                        "    IFNULL(link_rk.rksl, 0) AS rksl, " +
                        "    IFNULL(link_rk.rkje, 0) AS rkje, " +
                        "    IFNULL(ck.cksl, 0) AS cksl, " +
                        "    IFNULL(ck.ckje, 0) AS ckje, " +
                        "    IFNULL(link_rk.cpsl, 0) + IFNULL(link_rk.rksl, 0) - IFNULL(ck.cksl, 0) AS jcsl, " +
                        "    IFNULL(link_rk.cpje, 0) + IFNULL(link_rk.rkje, 0) - IFNULL(ck.ckje, 0) AS jcje, " +
                        "    IFNULL(cangku_info.cangku, '') AS cangku, " +
                        "    (IFNULL(link_rk.cpsl, 0) + IFNULL(all_rk.rksl, 0) - IFNULL(all_ck.cksl, 0)) AS qimo_sl " +
                        "  FROM ( " +
                        "    SELECT " +
                        "      link_qc.cpid, " +
                        "      link_qc.cpname, " +
                        "      link_qc.cplb, " +
                        "      link_qc.cpsl, " +
                        "      link_qc.cpje, " +
                        "      rk.rksl, " +
                        "      rk.rkje " +
                        "    FROM ( " +
                        "      SELECT " +
                        "        cp.cpid, " +
                        "        cp.cpname, " +
                        "        cp.cplb, " +
                        "        qc.cpsl, " +
                        "        qc.cpje " +
                        "      FROM ( " +
                        "        SELECT cpid, cpname, cplb FROM yh_jinxiaocun_qichushu WHERE gs_name = ? " +  // 参数1
                        "        UNION " +
                        "        SELECT sp_dm, cpname, cplb FROM yh_jinxiaocun_mingxi WHERE gs_name = ? " +  // 参数2
                        "      ) AS cp " +
                        "      LEFT JOIN ( " +
                        "        SELECT " +
                        "          cpid, " +
                        "          cplb, " +
                        "          cpname, " +
                        "          SUM(cpsl) AS cpsl, " +
                        "          SUM(cpsj * cpsl) AS cpje " +
                        "        FROM yh_jinxiaocun_qichushu " +
                        "        WHERE gs_name = ? " +  // 参数3
                        "        GROUP BY cpid, cpname, cplb " +
                        "      ) AS qc ON cp.cpid = qc.cpid AND cp.cpname = qc.cpname AND cp.cplb = qc.cplb " +
                        "    ) AS link_qc " +
                        "    LEFT JOIN ( " +
                        "      SELECT " +
                        "        sp_dm, " +
                        "        cpname, " +
                        "        cplb, " +
                        "        SUM(cpsl) AS rksl, " +
                        "        SUM(cpsl * cpsj) AS rkje " +
                        "      FROM yh_jinxiaocun_mingxi " +
                        "      WHERE (mxtype = '入库' OR mxtype = '调拨入库' OR mxtype = '盘盈入库') " +
                        "        AND gs_name = ? " +  // 参数4
                        "        AND shijian BETWEEN ? AND ? " +  // 参数5,6
                        "      GROUP BY sp_dm, cpname, cplb " +
                        "    ) AS rk ON rk.sp_dm = link_qc.cpid AND rk.cpname = link_qc.cpname AND rk.cplb = link_qc.cplb " +
                        "  ) AS link_rk " +
                        "  LEFT JOIN ( " +
                        "    SELECT " +
                        "      sp_dm, " +
                        "      cpname, " +
                        "      cplb, " +
                        "      SUM(cpsl) AS cksl, " +
                        "      SUM(cpsl * cpsj) AS ckje " +
                        "    FROM yh_jinxiaocun_mingxi " +
                        "    WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') " +
                        "      AND gs_name = ? " +  // 参数7
                        "      AND shijian BETWEEN ? AND ? " +  // 参数8,9
                        "    GROUP BY sp_dm, cpname, cplb " +
                        "  ) AS ck ON ck.sp_dm = link_rk.cpid AND ck.cpname = link_rk.cpname AND ck.cplb = link_rk.cplb " +
                        "  LEFT JOIN ( " +
                        "    SELECT sp_dm, cangku " +
                        "    FROM yh_jinxiaocun_mingxi " +
                        "    WHERE gs_name = ? " +  // 参数10
                        "    GROUP BY sp_dm, cangku " +
                        "  ) AS cangku_info ON link_rk.cpid = cangku_info.sp_dm " +
                        "  LEFT JOIN ( " +
                        "    SELECT " +
                        "      sp_dm, " +
                        "      cpname, " +
                        "      cplb, " +
                        "      SUM(cpsl) AS rksl " +
                        "    FROM yh_jinxiaocun_mingxi " +
                        "    WHERE (mxtype = '入库' OR mxtype = '调拨入库' OR mxtype = '盘盈入库') " +  // 添加调拨和盘盈条件
                        "      AND gs_name = ? " +  // 参数11
                        "      AND shijian <= ? " +  // 参数12 (结束时间)
                        "    GROUP BY sp_dm, cpname, cplb " +
                        "  ) AS all_rk ON link_rk.cpid = all_rk.sp_dm AND link_rk.cpname = all_rk.cpname AND link_rk.cplb = all_rk.cplb " +
                        "  LEFT JOIN ( " +
                        "    SELECT " +
                        "      sp_dm, " +
                        "      cpname, " +
                        "      cplb, " +
                        "      SUM(cpsl) AS cksl " +
                        "    FROM yh_jinxiaocun_mingxi " +
                        "    WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') " +  // 添加调拨和盘亏条件
                        "      AND gs_name = ? " +  // 参数13
                        "      AND shijian <= ? " +  // 参数14 (结束时间)
                        "    GROUP BY sp_dm, cpname, cplb " +
                        "  ) AS all_ck ON link_rk.cpid = all_ck.sp_dm AND link_rk.cpname = all_ck.cpname AND link_rk.cplb = all_ck.cplb " +
                        ") AS jxc " +
                        "LEFT JOIN ( " +
                        "  SELECT sp_dm, lei_bie, name, bianyuan, mark1 " +
                        "  FROM yh_jinxiaocun_jichuziliao " +
                        "  WHERE gs_name = ? " +  // 参数15
                        ") AS bian_yuan ON jxc.cpid = bian_yuan.sp_dm AND jxc.cpname = bian_yuan.name AND jxc.cplb = bian_yuan.lei_bie " +
                        "LEFT JOIN ( " +
                        "  SELECT " +
                        "    m1.sp_dm, " +
                        "    m1.cpname, " +
                        "    m1.cplb, " +
                        "    m1.cpsj " +
                        "  FROM yh_jinxiaocun_mingxi m1 " +
                        "  INNER JOIN ( " +
                        "    SELECT " +
                        "      sp_dm, " +
                        "      cpname, " +
                        "      cplb, " +
                        "      MAX(shijian) AS max_shijian " +
                        "    FROM yh_jinxiaocun_mingxi " +
                        "    WHERE mxtype = '出库' AND gs_name = ? " +  // 参数16
                        "    GROUP BY sp_dm, cpname, cplb " +
                        "  ) m2 ON m1.sp_dm = m2.sp_dm AND m1.cpname = m2.cpname AND m1.cplb = m2.cplb AND m1.shijian = m2.max_shijian " +
                        "  WHERE m1.mxtype = '出库' AND m1.gs_name = ? " +  // 参数17
                        ") AS last_ck ON jxc.cpid = last_ck.sp_dm AND jxc.cpname = last_ck.cpname AND jxc.cplb = last_ck.cplb " +
                        "LEFT JOIN ( " +
                        "  SELECT " +
                        "    sp_dm, " +
                        "    cpname, " +
                        "    cplb, " +
                        "    SUM(cpsl) AS cksl " +
                        "  FROM yh_jinxiaocun_mingxi " +
                        "  WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') " +
                        "    AND gs_name = ? " +  // 参数18
                        "    AND shijian BETWEEN ? AND ? " +  // 参数19,20
                        "  GROUP BY sp_dm, cpname, cplb " +
                        ") AS period_ck ON jxc.cpid = period_ck.sp_dm AND jxc.cpname = period_ck.cpname AND jxc.cplb = period_ck.cplb " +
                        "WHERE jxc.cksl < ? " +  // 参数21
                        "ORDER BY jxc.cpname, jxc.cangku";

                base = new JxcBaseDao();  // 使用MySQL的DAO
                Log.e("SQLDebug", "=== 完整SQL检查 ===");
                Log.e("SQLDebug", "SQL总长度: " + sql.length());

                // 检查SQL是否完整
                if (!sql.contains("ORDER BY jxc.cpname, jxc.cangku")) {
                    Log.e("SQLDebug", "错误: SQL不完整，可能被截断！");
                    // 重新定义完整的SQL

                }
                Log.e("SQLDebug", "查询统计SQL: " + sql);
                // 检查参数占位符数量
                int paramCount = countParameters(sql);
                Log.e("SQLDebug", "SQL中?占位符数量: " + paramCount);



                List<YhJinXiaoCun> list = base.query(YhJinXiaoCun.class, sql, company, company, company, company, ks, js,company,ks,js,company,company,js,company,js,company,company,company,company,ks,js,jiyanum);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取期末统计列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    private int countParameters(String sql) {
        int count = 0;
        for (int i = 0; i < sql.length(); i++) {
            if (sql.charAt(i) == '?') {
                count++;
            }
        }
        return count;
    }


}
