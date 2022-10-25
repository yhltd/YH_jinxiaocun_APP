package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;

import java.util.ArrayList;
import java.util.List;

public class YhJinXiaoCunMingXiService {
    private JxcBaseDao base;

    /**
     * 客户下拉
     */
    public List<String> getKehu(String company) {
        String sql = "select shou_h from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
        List<String> kehuList = new ArrayList<>();
        kehuList.add("全部");
        for (int i = 0; i < list.size(); i++) {
            kehuList.add(list.get(i).getShou_h());
        }
        return kehuList != null && kehuList.size() > 0 ? kehuList : null;
    }

    /**
     *  商品进出查询
     * */
    public List<YhJinXiaoCunMingXi> getProductQuery(String company,String cpname) {
        List<YhJinXiaoCunMingXi> list;
        base = new JxcBaseDao();
        if(cpname.equals("全部")){
            String sql = "select mx.sp_dm,mx.cpname,mx.cplb,ifnull(rk.cpsl,0) as ruku_num,ifnull(rk.cp_price,0) as ruku_price,ifnull(ck.cpsl,0) as chuku_num,ifnull(ck.cp_price,0) as chuku_price from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name =? group by sp_dm,cpname,cplb) as mx left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '1' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=rk.sp_dm";
            list = base.query(YhJinXiaoCunMingXi.class, sql, company, company, company);
        }else{
            String sql = "select mx.sp_dm,mx.cpname,mx.cplb,ifnull(rk.cpsl,0) as ruku_num,ifnull(rk.cp_price,0) as ruku_price,ifnull(ck.cpsl,0) as chuku_num,ifnull(ck.cp_price,0) as chuku_price from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name =? group by sp_dm,cpname,cplb) as mx left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '1' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=rk.sp_dm where mx.cpname = ? ";
            list = base.query(YhJinXiaoCunMingXi.class, sql, company, company, company, cpname);
        }
        return list;
    }

    /**
     *  客户供应商查询
     * */
    public List<YhJinXiaoCunMingXi> getKeHuQuery(String company,String kehu) {
        List<YhJinXiaoCunMingXi> list;
        base = new JxcBaseDao();
        if(kehu.equals("全部")){
            String sql = "select shou_h,sp_dm,cpname,cplb,ifnull(sum(cpsl),0) as ruku_num,ifnull(sum(cpsj),0) as ruku_price from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h,sp_dm,cpname,cplb having shou_h != '' order by shou_h";
            list = base.query(YhJinXiaoCunMingXi.class, sql, company);
        }else{
            String sql = "select shou_h,sp_dm,cpname,cplb,ifnull(sum(cpsl),0) as ruku_num,ifnull(sum(cpsj),0) as ruku_price from yh_jinxiaocun_mingxi where gs_name = ? and shou_h = ? group by shou_h,sp_dm,cpname,cplb having shou_h != '' order by shou_h";
            list = base.query(YhJinXiaoCunMingXi.class, sql, company, kehu);
        }
        return list;
    }




}
