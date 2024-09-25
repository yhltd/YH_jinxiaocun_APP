package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunQiChuShu;

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
     * 商品下拉
     */
    public List<String> getProduct(String company) {
        String sql = "select cpname from yh_jinxiaocun_mingxi where gs_name = ? group by cpname ";
        base = new JxcBaseDao();
        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
        List<String> productList = new ArrayList<>();
        productList.add("全部");
        for (int i = 0; i < list.size(); i++) {
            productList.add(list.get(i).getCpname());
        }
        return productList != null && productList.size() > 0 ? productList : null;
    }

    /**
     * 商品进出查询
     */
    public List<YhJinXiaoCunMingXi> getProductQuery(String company, String cpname) {
        List<YhJinXiaoCunMingXi> list;
        base = new JxcBaseDao();
        if (cpname.equals("全部")) {
            String sql = "select mx.sp_dm,mx.cpname,mx.cplb,ifnull(rk.cpsl,0) as ruku_num,ifnull(rk.cp_price,0) as ruku_price,ifnull(ck.cpsl,0) as chuku_num,ifnull(ck.cp_price,0) as chuku_price from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name =? group by sp_dm,cpname,cplb) as mx left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '1' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=rk.sp_dm";
            list = base.query(YhJinXiaoCunMingXi.class, sql, company, company, company);
        } else {
            String sql = "select mx.sp_dm,mx.cpname,mx.cplb,ifnull(rk.cpsl,0) as ruku_num,ifnull(rk.cp_price,0) as ruku_price,ifnull(ck.cpsl,0) as chuku_num,ifnull(ck.cp_price,0) as chuku_price from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name =? group by sp_dm,cpname,cplb) as mx left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '1' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=rk.sp_dm where mx.cpname = ? ";
            list = base.query(YhJinXiaoCunMingXi.class, sql, company, company, company, cpname);
        }
        return list;
    }

    /**
     * 客户供应商查询
     */
    public List<YhJinXiaoCunMingXi> getKeHuQuery(String company, String kehu) {
        List<YhJinXiaoCunMingXi> list;
        base = new JxcBaseDao();
        if (kehu.equals("全部")) {
            String sql = "select shou_h,sp_dm,cpname,cplb,ifnull(sum(cpsl),0) as ruku_num,ifnull(sum(cpsj),0) as ruku_price from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h,sp_dm,cpname,cplb having shou_h != '' order by shou_h";
            list = base.query(YhJinXiaoCunMingXi.class, sql, company);
        } else {
            String sql = "select shou_h,sp_dm,cpname,cplb,ifnull(sum(cpsl),0) as ruku_num,ifnull(sum(cpsj),0) as ruku_price from yh_jinxiaocun_mingxi where gs_name = ? and shou_h = ? group by shou_h,sp_dm,cpname,cplb having shou_h != '' order by shou_h";
            list = base.query(YhJinXiaoCunMingXi.class, sql, company, kehu);
        }
        return list;
    }

    /**
     * 明细刷新
     */
    public List<YhJinXiaoCunMingXi> getList(String company) {
        base = new JxcBaseDao();
        String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? order by _id ";
        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
        return list;
    }

    /**
     * 明细查询
     */
    public List<YhJinXiaoCunMingXi> getQuery(String company, String ks, String js) {
        base = new JxcBaseDao();
        String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? order by _id;";

        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
        return list;
    }

    /**
     * 明细修改
     */
    public boolean update(YhJinXiaoCunMingXi yhJinXiaoCunMingXi) {
        String sql = "update yh_jinxiaocun_mingxi set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,shou_h=? where _id=? ";

        base = new JxcBaseDao();
        boolean result = base.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getShou_h(), yhJinXiaoCunMingXi.get_id());
        return result;
    }

    /**
     * 明细删除
     */
    public boolean delete(int id) {
        String sql = "delete from yh_jinxiaocun_mingxi where _id = ?";
        base = new JxcBaseDao();
        return base.execute(sql, id);
    }

    /**
     * 添加出入库
     */
    public boolean insert(List<YhJinXiaoCunMingXi> list) {
        boolean pd = true;
        for (int i = 0; i < list.size(); i++) {
            String sql = "insert into yh_jinxiaocun_mingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name) values(?,?,?,?,?,?,?,?,?,?,?)";
            base = new JxcBaseDao();
            long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), list.get(i).getMxtype(), list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName());
            if (result < 0) {
                pd = false;
            }
        }
        return pd;
    }

}
