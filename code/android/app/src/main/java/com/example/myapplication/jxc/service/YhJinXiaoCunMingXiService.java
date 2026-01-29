package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunQiChuShu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YhJinXiaoCunMingXiService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 客户下拉
     */
//    public List<String> getKehu(String company) {
//        String sql = "select shou_h from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
//        List<String> kehuList = new ArrayList<>();
//        kehuList.add("全部");
//        for (int i = 0; i < list.size(); i++) {
//            kehuList.add(list.get(i).getShou_h());
//        }
//        return kehuList != null && kehuList.size() > 0 ? kehuList : null;
//    }
    public List<String> getKehu(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select shou_h from yh_jinxiaocun_mingxi_mssql where gs_name = ? group by shou_h";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                List<String> kehuList = new ArrayList<>();
                kehuList.add("全部");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        kehuList.add(list.get(i).getShou_h());
                    }
                }
                return kehuList.size() > 0 ? kehuList : null;

            } else {
                // MySQL 版本
                String sql = "select shou_h from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h";
                base = new JxcBaseDao();
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                List<String> kehuList = new ArrayList<>();
                kehuList.add("全部");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        kehuList.add(list.get(i).getShou_h());
                    }
                }
                return kehuList.size() > 0 ? kehuList : null;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取客户列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            List<String> defaultList = new ArrayList<>();
            defaultList.add("全部");
            return defaultList;
        }
    }

    /**
     * 商品下拉
     */
//    public List<String> getProduct(String company) {
//        String sql = "select cpname from yh_jinxiaocun_mingxi where gs_name = ? group by cpname ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
//        List<String> productList = new ArrayList<>();
//        productList.add("全部");
//        for (int i = 0; i < list.size(); i++) {
//            productList.add(list.get(i).getCpname());
//        }
//        return productList != null && productList.size() > 0 ? productList : null;
//    }
    public List<String> getProduct(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select cpname from yh_jinxiaocun_mingxi_mssql where gs_name = ? group by cpname";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                List<String> productList = new ArrayList<>();
                productList.add("全部");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        productList.add(list.get(i).getCpname());
                    }
                }
                return productList.size() > 0 ? productList : null;

            } else {
                // MySQL 版本
                String sql = "select cpname from yh_jinxiaocun_mingxi where gs_name = ? group by cpname";
                base = new JxcBaseDao();
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                List<String> productList = new ArrayList<>();
                productList.add("全部");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        productList.add(list.get(i).getCpname());
                    }
                }
                return productList.size() > 0 ? productList : null;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取产品列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            List<String> defaultList = new ArrayList<>();
            defaultList.add("全部");
            return defaultList;
        }
    }

    /**
     * 商品进出查询
     */
//    public List<YhJinXiaoCunMingXi> getProductQuery(String company, String cpname) {
//        List<YhJinXiaoCunMingXi> list;
//        base = new JxcBaseDao();
//        if (cpname.equals("全部")) {
//            String sql = "select mx.sp_dm,mx.cpname,mx.cplb,ifnull(rk.cpsl,0) as ruku_num,ifnull(rk.cp_price,0) as ruku_price,ifnull(ck.cpsl,0) as chuku_num,ifnull(ck.cp_price,0) as chuku_price from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name =? group by sp_dm,cpname,cplb) as mx left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '出库' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=rk.sp_dm";
//            list = base.query(YhJinXiaoCunMingXi.class, sql, company, company, company);
//        } else {
//            String sql = "select mx.sp_dm,mx.cpname,mx.cplb,ifnull(rk.cpsl,0) as ruku_num,ifnull(rk.cp_price,0) as ruku_price,ifnull(ck.cpsl,0) as chuku_num,ifnull(ck.cp_price,0) as chuku_price from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name =? group by sp_dm,cpname,cplb) as mx left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '出库' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=rk.sp_dm where mx.cpname = ? ";
//            list = base.query(YhJinXiaoCunMingXi.class, sql, company, company, company, cpname);
//        }
//        return list;
//    }
    public List<YhJinXiaoCunMingXi> getProductQuery(String company, String cpname) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            List<YhJinXiaoCunMingXi> list;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                if (cpname.equals("全部")) {
                    String sql = "select mx.sp_dm,mx.cpname,mx.cplb," +
                            "isnull(rk.cpsl,0) as ruku_num," +
                            "isnull(rk.cp_price,0) as ruku_price," +
                            "isnull(ck.cpsl,0) as chuku_num," +
                            "isnull(ck.cp_price,0) as chuku_price " +
                            "from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi_mssql where gs_name =? group by sp_dm,cpname,cplb) as mx " +
                            "left join (select sp_dm,sum(CAST(cpsl AS DECIMAL(10,2))) as cpsl,sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))) as cp_price from yh_jinxiaocun_mingxi_mssql where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm " +
                            "left join (select sp_dm,sum(CAST(cpsl AS DECIMAL(10,2))) as cpsl,sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))) as cp_price from yh_jinxiaocun_mingxi_mssql where mxtype = '出库' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=mx.sp_dm";
                    list = base2.query(YhJinXiaoCunMingXi.class, sql, company, company, company);
                } else {
                    String sql = "select mx.sp_dm,mx.cpname,mx.cplb," +
                            "isnull(rk.cpsl,0) as ruku_num," +
                            "isnull(rk.cp_price,0) as ruku_price," +
                            "isnull(ck.cpsl,0) as chuku_num," +
                            "isnull(ck.cp_price,0) as chuku_price " +
                            "from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi_mssql where gs_name =? group by sp_dm,cpname,cplb) as mx " +
                            "left join (select sp_dm,sum(CAST(cpsl AS DECIMAL(10,2))) as cpsl,sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))) as cp_price from yh_jinxiaocun_mingxi_mssql where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm " +
                            "left join (select sp_dm,sum(CAST(cpsl AS DECIMAL(10,2))) as cpsl,sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))) as cp_price from yh_jinxiaocun_mingxi_mssql where mxtype = '出库' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=mx.sp_dm " +
                            "where mx.cpname = ?";
                    list = base2.query(YhJinXiaoCunMingXi.class, sql, company, company, company, cpname);
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                if (cpname.equals("全部")) {
                    String sql = "select mx.sp_dm,mx.cpname,mx.cplb,ifnull(rk.cpsl,0) as ruku_num,ifnull(rk.cp_price,0) as ruku_price,ifnull(ck.cpsl,0) as chuku_num,ifnull(ck.cp_price,0) as chuku_price from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name =? group by sp_dm,cpname,cplb) as mx left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '出库' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=rk.sp_dm";
                    list = base.query(YhJinXiaoCunMingXi.class, sql, company, company, company);
                } else {
                    String sql = "select mx.sp_dm,mx.cpname,mx.cplb,ifnull(rk.cpsl,0) as ruku_num,ifnull(rk.cp_price,0) as ruku_price,ifnull(ck.cpsl,0) as chuku_num,ifnull(ck.cp_price,0) as chuku_price from (select sp_dm,cpname,cplb from yh_jinxiaocun_mingxi where gs_name =? group by sp_dm,cpname,cplb) as mx left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '入库' and gs_name = ? group by sp_dm) as rk on mx.sp_dm=rk.sp_dm left join (select sp_dm,sum(cpsl) as cpsl,sum(cpsl*cpsj) as cp_price from yh_jinxiaocun_mingxi where mxtype = '出库' and gs_name = ? group by sp_dm) as ck on ck.sp_dm=rk.sp_dm where mx.cpname = ?";
                    list = base.query(YhJinXiaoCunMingXi.class, sql, company, company, company, cpname);
                }
            }
            return list != null ? list : new ArrayList<>();

        } catch (Exception e) {
            Log.e("SQLDebug", "获取产品查询数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public List<YhJinXiaoCunMingXi> getListByCpid1(String company, String cpid) {
//        String sql = "select * from yh_jinxiaocun_mingxi where gs_name = ? and sp_dm=? ";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, cpid);
//        return list;
//    }
public List<YhJinXiaoCunMingXi> getListByCpid1(String company, String cpid) {
    try {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();
        Log.e("MyService", "当前shujuku状态: " + shujukuValue);

        // 根据状态执行不同的业务逻辑
        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name = ? and sp_dm=?";
            base2 = new JxcServerDao();
            List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, cpid);
            return list != null ? list : new ArrayList<>();

        } else {
            // MySQL 版本
            String sql = "select * from yh_jinxiaocun_mingxi where gs_name = ? and sp_dm=?";
            base = new JxcBaseDao();
            List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, cpid);
            return list != null ? list : new ArrayList<>();
        }
    } catch (Exception e) {
        Log.e("SQLDebug", "根据产品ID获取明细列表过程发生异常: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    }
}

//    public List<String> getspid(String company) {
//        String sql = "select sp_dm from yh_jinxiaocun_mingxi where gs_name = ? group by sp_dm";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
//        List<String> cpidList = new ArrayList<>();
//        if (cpidList!=null){
//            for (int i = 0; i < list.size(); i++) {
//                cpidList.add(list.get(i).getSpDm());
//            }
//        }
//        return cpidList != null && cpidList.size() > 0 ? cpidList : null;
//    }

    public List<String> getspid(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select sp_dm from yh_jinxiaocun_mingxi_mssql where gs_name = ? group by sp_dm";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                List<String> cpidList = new ArrayList<>();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        cpidList.add(list.get(i).getSpDm());
                    }
                }
                return cpidList.size() > 0 ? cpidList : null;

            } else {
                // MySQL 版本
                String sql = "select sp_dm from yh_jinxiaocun_mingxi where gs_name = ? group by sp_dm";
                base = new JxcBaseDao();
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                List<String> cpidList = new ArrayList<>();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        cpidList.add(list.get(i).getSpDm());
                    }
                }
                return cpidList.size() > 0 ? cpidList : null;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取产品ID列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
//    public List<String> getshouh(String company) {
//        String sql = "select shou_h from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h";
//        base = new JxcBaseDao();
//        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
//        List<String> shouhList = new ArrayList<>();
//        if (shouhList!=null){
//            for (int i = 0; i < list.size(); i++) {
//                shouhList.add(list.get(i).getShou_h());
//            }
//        }
//        return shouhList != null && shouhList.size() > 0 ? shouhList : null;
//    }
    public List<String> getshouh(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select shou_h from yh_jinxiaocun_mingxi_mssql where gs_name = ? group by shou_h";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                List<String> shouhList = new ArrayList<>();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        shouhList.add(list.get(i).getShou_h());
                    }
                }
                return shouhList.size() > 0 ? shouhList : null;

            } else {
                // MySQL 版本
                String sql = "select shou_h from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h";
                base = new JxcBaseDao();
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                List<String> shouhList = new ArrayList<>();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        shouhList.add(list.get(i).getShou_h());
                    }
                }
                return shouhList.size() > 0 ? shouhList : null;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取收货方列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 客户供应商查询
     */
//    public List<YhJinXiaoCunMingXi> getKeHuQuery(String company, String kehu) {
//        List<YhJinXiaoCunMingXi> list;
//        base = new JxcBaseDao();
//        if (kehu.equals("全部")) {
//            String sql = "select shou_h,sp_dm,cpname,cplb,ifnull(sum(cpsl),0) as ruku_num,ifnull(sum(cpsj*cpsl),0) as ruku_price from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h,sp_dm,cpname,cplb having shou_h != '' order by shou_h";
//            list = base.query(YhJinXiaoCunMingXi.class, sql, company);
//        } else {
//            String sql = "select shou_h,sp_dm,cpname,cplb,ifnull(sum(cpsl),0) as ruku_num,ifnull(sum(cpsj*cpsl),0) as ruku_price from yh_jinxiaocun_mingxi where gs_name = ? and shou_h = ? group by shou_h,sp_dm,cpname,cplb having shou_h != '' order by shou_h";
//            list = base.query(YhJinXiaoCunMingXi.class, sql, company, kehu);
//        }
//        return list;
//    }
    public List<YhJinXiaoCunMingXi> getKeHuQuery(String company, String kehu) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            List<YhJinXiaoCunMingXi> list;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                if (kehu.equals("全部")) {
                    String sql = "select shou_h,sp_dm,cpname,cplb," +
                            "isnull(sum(CAST(cpsl AS DECIMAL(10,2))),0) as ruku_num," +
                            "isnull(sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))),0) as ruku_price " +
                            "from yh_jinxiaocun_mingxi_mssql " +
                            "where gs_name = ? " +
                            "group by shou_h,sp_dm,cpname,cplb " +
                            "having shou_h != '' " +
                            "order by shou_h";
                    list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                } else {
                    String sql = "select shou_h,sp_dm,cpname,cplb," +
                            "isnull(sum(CAST(cpsl AS DECIMAL(10,2))),0) as ruku_num," +
                            "isnull(sum(CAST(cpsl AS DECIMAL(10,2))*CAST(cpsj AS DECIMAL(10,2))),0) as ruku_price " +
                            "from yh_jinxiaocun_mingxi_mssql " +
                            "where gs_name = ? and shou_h = ? " +
                            "group by shou_h,sp_dm,cpname,cplb " +
                            "having shou_h != '' " +
                            "order by shou_h";
                    list = base2.query(YhJinXiaoCunMingXi.class, sql, company, kehu);
                }


            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                if (kehu.equals("全部")) {
                    String sql = "select shou_h,sp_dm,cpname,cplb,ifnull(sum(cpsl),0) as ruku_num,ifnull(sum(cpsj*cpsl),0) as ruku_price from yh_jinxiaocun_mingxi where gs_name = ? group by shou_h,sp_dm,cpname,cplb having shou_h != '' order by shou_h";
                    list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                } else {
                    String sql = "select shou_h,sp_dm,cpname,cplb,ifnull(sum(cpsl),0) as ruku_num,ifnull(sum(cpsj*cpsl),0) as ruku_price from yh_jinxiaocun_mingxi where gs_name = ? and shou_h = ? group by shou_h,sp_dm,cpname,cplb having shou_h != '' order by shou_h";
                    list = base.query(YhJinXiaoCunMingXi.class, sql, company, kehu);
                }
            }
            return list != null ? list : new ArrayList<>();

        } catch (Exception e) {
            Log.e("SQLDebug", "获取客户查询数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 明细刷新
     */
//    public List<YhJinXiaoCunMingXi> getList(String company) {
//        base = new JxcBaseDao();
//        String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? order by _id ";
//        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
//        return list;
//    }

    public List<YhJinXiaoCunMingXi> getList(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? order by _id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询所有SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取明细列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<YhJinXiaoCunMingXi> getListdbtj(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and mxtype in ('调拨入库', '调拨出库') order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and mxtype in ('调拨入库', '调拨出库') order by _id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询调拨所有SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取明细列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 明细查询
     */
//    public List<YhJinXiaoCunMingXi> getQuery(String company, String ks, String js) {
//        base = new JxcBaseDao();
//        String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? order by _id;";
//
//        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
//        return list;
//    }
//    public List<YhJinXiaoCunMingXi> getQuery(String company, String ks, String js) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本
//                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and shijian between ? and ? and mxtype in ('入库', '出库') order by _id";
//                base2 = new JxcServerDao();
//                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
//                return list != null ? list : new ArrayList<>();
//
//            } else {
//                // MySQL 版本
//                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? and mxtype in ('入库', '出库') order by _id";
//                base = new JxcBaseDao();
//                Log.e("SQLDebug", "查询明细SQL: " + sql);
//                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
//                return list != null ? list : new ArrayList<>();
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "获取查询明细过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
    public List<YhJinXiaoCunMingXi> getQuery(String company, String ks, String js) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 简单处理：如果日期不包含时间，就加上时间
            String queryStartDate = ks;
            String queryEndDate = js;

            if (ks != null && !ks.contains(":")) {
                queryStartDate = ks + " 00:00:00";
            }
            if (js != null && !js.contains(":")) {
                queryEndDate = js + " 23:59:59";
            }

            Log.e("SQLDebug", "查询参数 - 开始: " + queryStartDate + ", 结束: " + queryEndDate);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and shijian between ? and ? and mxtype in ('入库', '出库') order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, queryStartDate, queryEndDate);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? and mxtype in ('入库', '出库') order by _id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, queryStartDate, queryEndDate);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取查询明细过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<YhJinXiaoCunMingXi> getQuerycgwrk(String company, String ks, String js) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi_mssql where gs_name=? and shijian between ? and ? and mxtype='采购' and ruku=? order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, ks, js,"");
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi where gs_name=? and shijian between ? and ? and mxtype='采购' and ruku=? order by id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js,"");
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取查询明细过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<YhJinXiaoCunMingXi> getQuerycgth(String company, String ks, String js) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi_mssql where gs_name=? and shijian between ? and ? and mxtype='采购'  order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi where gs_name=? and shijian between ? and ? and mxtype='采购'  order by id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取查询明细过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<YhJinXiaoCunMingXi> getQueryxsthtongji(String company, String ks, String js) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi_mssql where gs_name=? and shijian between ? and ? and mxtype='销售退货'  order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi where gs_name=? and shijian between ? and ? and mxtype='销售退货'  order by id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取查询明细过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<YhJinXiaoCunMingXi> getQueryjcmingxitongji(String company, String ks, String js) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and shijian between ? and ? and mxtype in ('入库', '出库', '调拨入库', '调拨出库', '盘亏出库', '盘盈入库')  order by cpsl";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? and mxtype in ('入库', '出库', '调拨入库', '调拨出库', '盘亏出库', '盘盈入库') order by cpsl";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取查询明细过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<YhJinXiaoCunMingXi> getQuerycgthtongji(String company, String ks, String js) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi_mssql where gs_name=? and shijian between ? and ? and mxtype='采购退货'  order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi where gs_name=? and shijian between ? and ? and mxtype='采购退货'  order by id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取查询明细过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 明细修改
     */
//    public boolean update(YhJinXiaoCunMingXi yhJinXiaoCunMingXi) {
//        String sql = "update yh_jinxiaocun_mingxi set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,shou_h=? where _id=? ";
//
//        base = new JxcBaseDao();
//        boolean result = base.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getShou_h(), yhJinXiaoCunMingXi.get_id());
//        return result;
//    }
    public boolean update(YhJinXiaoCunMingXi yhJinXiaoCunMingXi) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_mingxi_mssql set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,shou_h=?,cangku=? where _id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getShou_h(),yhJinXiaoCunMingXi.getcangku(), yhJinXiaoCunMingXi.get_id());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_mingxi set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,shou_h=?,cangku=? where _id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getShou_h(),yhJinXiaoCunMingXi.getcangku(), yhJinXiaoCunMingXi.get_id());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新明细数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean updatecgwrk(YhJinXiaoCunMingXi yhJinXiaoCunMingXi) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_tuihuomingxi_mssql set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,shou_h=?,cangku=?,ruku=? where id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getShou_h(),yhJinXiaoCunMingXi.getcangku(),yhJinXiaoCunMingXi.getRuku(), yhJinXiaoCunMingXi.getid());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_tuihuomingxi set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,shou_h=?,cangku=?,ruku=? where id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getShou_h(),yhJinXiaoCunMingXi.getcangku(),yhJinXiaoCunMingXi.getRuku(), yhJinXiaoCunMingXi.getid());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新明细数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 明细删除
     */
//    public boolean delete(int id) {
//        String sql = "delete from yh_jinxiaocun_mingxi where _id = ?";
//        base = new JxcBaseDao();
//        return base.execute(sql, id);
//    }
    public boolean delete(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "delete from yh_jinxiaocun_mingxi_mssql where _id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_mingxi where _id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除明细数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletecgwrk(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "delete from yh_jinxiaocun_tuihuomingxi_mssql where id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                // MySQL 版本
                String sql = "delete from yh_jinxiaocun_tuihuomingxi where id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除明细数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加出入库
     */
//    public boolean insert(List<YhJinXiaoCunMingXi> list) {
//        boolean pd = true;
//        for (int i = 0; i < list.size(); i++) {
//            String sql = "insert into yh_jinxiaocun_mingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name) values(?,?,?,?,?,?,?,?,?,?,?)";
//            base = new JxcBaseDao();
//            long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), list.get(i).getMxtype(), list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName());
//            if (result < 0) {
//                pd = false;
//            }
//        }
//        return pd;
//    }
    public boolean insert(List<YhJinXiaoCunMingXi> list) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            boolean pd = true;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_mingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), list.get(i).getMxtype(), list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku());
                    if (result < 0) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    YhJinXiaoCunMingXi item = list.get(i);
                    String cangku = item.getcangku();
                    Log.e("CangkuDebug", "MySQL - 第" + i + "条记录的cangku值: " + cangku + ", 商品: " + item.getCpname());
                    String sql = "insert into yh_jinxiaocun_mingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), list.get(i).getMxtype(), list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku());
                    if (result < 0) {
                        pd = false;
                    }
                }
            }
            return pd;

        } catch (Exception e) {
            Log.e("SQLDebug", "批量插入明细数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean insert_caigou(List<YhJinXiaoCunMingXi> list) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            boolean pd = true;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_mingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(),"入库", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku());
                    if (result < 0) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    YhJinXiaoCunMingXi item = list.get(i);
                    String cangku = item.getcangku();
                    Log.e("CangkuDebug", "MySQL - 第" + i + "条记录的cangku值: " + cangku + ", 商品: " + item.getCpname());
                    String sql = "insert into yh_jinxiaocun_mingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), "入库", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku());
                    if (result < 0) {
                        pd = false;
                    }
                }
            }
            return pd;

        } catch (Exception e) {
            Log.e("SQLDebug", "批量插入明细数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean insert_tuihuo(List<YhJinXiaoCunMingXi> list) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            boolean pd = true;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_mingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(),"出库", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku());
                    if (result < 0) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    YhJinXiaoCunMingXi item = list.get(i);
                    String cangku = item.getcangku();
                    Log.e("CangkuDebug", "MySQL - 第" + i + "条记录的cangku值: " + cangku + ", 商品: " + item.getCpname());
                    String sql = "insert into yh_jinxiaocun_mingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), "出库", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku());
                    if (result < 0) {
                        pd = false;
                    }
                }
            }
            return pd;

        } catch (Exception e) {
            Log.e("SQLDebug", "批量插入明细数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public List<YhJinXiaoCunMingXi> getListmingxi(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and mxtype in ('入库', '出库') order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and mxtype in ('入库', '出库') order by _id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细所有SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取明细列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



    public List<YhJinXiaoCunMingXi> getListcgwrk(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi_mssql where gs_name=?  and mxtype='采购' and ruku=? order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company,"");
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi where gs_name=? and mxtype='采购' and ruku=? order by id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细所有SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company,"");
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取明细列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<YhJinXiaoCunMingXi> getListcgth(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi_mssql where gs_name=?  and mxtype='采购'  order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_tuihuomingxi where gs_name=? and mxtype='采购'  order by id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细所有SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取明细列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 添加调拨出入库
     */

    public boolean diaobo(List<YhJinXiaoCunMingXi> list) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            boolean pd = true;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_mingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
//                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), list.get(i).getMxtype(), list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku());
                    // 出库记录
                    long result1 =     base2.executeOfId_diaobo(sql,
                            list.get(i).getCplb(),list.get(i).getCpname(),list.get(i).getCpsj(),
                            list.get(i).getCpsl(), "调拨出库", // 类型：出库
                            list.get(i).getOrderid(),list.get(i).getShijian(), list.get(i).getSpDm(),
                            list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),
                            list.get(i).getcangku() // 源仓库
                    );

                    // 入库记录
                    long result2 =  base2.executeOfId_diaobo(sql,
                            list.get(i).getCplb(),list.get(i).getCpname(),list.get(i).getCpsj(),
                            list.get(i).getCpsl(), "调拨入库", // 类型：出库
                            list.get(i).getOrderid(),list.get(i).getShijian(), list.get(i).getSpDm(),
                            list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),
                            list.get(i).getcangku2() // 目标仓库
                    );
                    base2.close();
                    if (result1 < 0 || result2 < 0) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    YhJinXiaoCunMingXi item = list.get(i);
                    String cangku2 = item.getcangku2();
                    Log.e("CangkuDebug", "MySQL - 第" + i + "条记录的cangku2值: " + cangku2+ ", 商品: " + item.getCpname());
                    String sql = "insert into yh_jinxiaocun_mingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    // 出库记录
                    long result2 =  base.executeOfId_diaobo(sql,
                            list.get(i).getCplb(),list.get(i).getCpname(),list.get(i).getCpsj(),
                            list.get(i).getCpsl(), "调拨入库", // 类型：出库
                            list.get(i).getOrderid(),list.get(i).getShijian(), list.get(i).getSpDm(),
                            list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),
                            list.get(i).getcangku2() // 目标仓库
                    );
                    Log.e("SQLDebug", "入库记录结果: " + result2);


                    long result1 = base.executeOfId_diaobo(sql,
                            list.get(i).getCplb(),list.get(i).getCpname(),list.get(i).getCpsj(),
                            list.get(i).getCpsl(), "调拨出库", // 类型：出库
                            list.get(i).getOrderid(),list.get(i).getShijian(), list.get(i).getSpDm(),
                            list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),
                            list.get(i).getcangku() // 源仓库
                    );
                    Log.e("SQLDebug", "出库记录结果: " + result1);
                    // 入库记录
                    base.close();
                    if (result1 < 0 || result2 < 0) {
                        pd = false;
                    }
                }
            }
            return pd;

        } catch (Exception e) {
            Log.e("SQLDebug", "批量插入调拨数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    /**
     * 调拨统计查询
     */
//    public List<YhJinXiaoCunMingXi> getQuery(String company, String ks, String js) {
//        base = new JxcBaseDao();
//        String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? order by _id;";
//
//        List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
//        return list;
//    }
//    public List<YhJinXiaoCunMingXi> dbtjquery(String company, String ks, String js) {
//        try {
//            int shujukuValue = CacheManager.getInstance().getShujukuValue();
//            Log.e("MyService", "当前shujuku状态: " + shujukuValue);
//
//            // 根据状态执行不同的业务逻辑
//            if (shujukuValue == 1) {
//                // SQL Server 版本
//                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and shijian between ? and ? and mxtype in ('调拨入库', '调拨出库') order by _id";
//                base2 = new JxcServerDao();
//                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
//                return list != null ? list : new ArrayList<>();
//
//            } else {
//                // MySQL 版本
//                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? and mxtype in ('调拨入库', '调拨出库') order by _id";
//                base = new JxcBaseDao();
//                Log.e("SQLDebug", "查询SQL: " + sql);
//                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
//                return list != null ? list : new ArrayList<>();
//            }
//        } catch (Exception e) {
//            Log.e("SQLDebug", "获取查询调拨统计过程发生异常: " + e.getMessage());
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
    public List<YhJinXiaoCunMingXi> dbtjquery(String company, String ks, String js) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 简单处理：如果日期不包含时间，就加上时间
            String queryStartDate = ks;
            String queryEndDate = js;

            if (ks != null && !ks.contains(":")) {
                queryStartDate = ks + " 00:00:00";
            }
            if (js != null && !js.contains(":")) {
                queryEndDate = js + " 23:59:59";
            }

            Log.e("SQLDebug", "调拨查询日期 - 开始: " + queryStartDate + ", 结束: " + queryEndDate);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and shijian between ? and ? and mxtype in ('调拨入库', '调拨出库') order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, queryStartDate, queryEndDate);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? and mxtype in ('调拨入库', '调拨出库') order by _id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company, queryStartDate, queryEndDate);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取查询调拨统计过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    /**
     * 调拨修改
     */

    public boolean updatedbtj(YhJinXiaoCunMingXi yhJinXiaoCunMingXi) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "update yh_jinxiaocun_mingxi_mssql set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,cangku=? where _id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getcangku(), yhJinXiaoCunMingXi.get_id());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_mingxi set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,cangku=? where _id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getcangku(), yhJinXiaoCunMingXi.get_id());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新调拨数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 库存盘点查询
     */

    public List<YhJinXiaoCunMingXi> kcpdQuery(String company, String js, String product_name, String wareHouse) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            if (shujukuValue == 1) {
                // SQL Server 版本 - 修复WHERE条件位置
                String baseSql = "SELECT mx.sp_dm, mx.cpname, mx.cplb, mx.cangku, \n" +
                        "       ISNULL(jc.jcsl,0) AS ruku_num, ISNULL(rk.cp_price,0) AS ruku_price, \n" +
                        "       ISNULL(ck.cpsl,0) AS chuku_num, ISNULL(ck.cp_price,0) AS chuku_price, \n" +
                        "       ISNULL(qc.cpsj,0) AS qc_cpsj \n" +
                        "FROM (SELECT sp_dm, cpname, cplb, cangku FROM yh_jinxiaocun_mingxi_mssql WHERE gs_name = @gongsi GROUP BY sp_dm, cpname, cplb, cangku) AS mx \n" +
                        "LEFT JOIN (SELECT sp_dm, cangku, SUM(cpsl) AS cpsl, SUM(cpsl*cpsj) AS cp_price \n" +
                        "           FROM yh_jinxiaocun_mingxi_mssql \n" +
                        "           WHERE (mxtype = '入库' OR mxtype = '调拨入库' OR mxtype = '盘盈入库') AND gs_name = @gongsi \n" +
                        "           AND shijian <= @stop_date \n" +
                        "           GROUP BY sp_dm, cangku) AS rk ON mx.sp_dm = rk.sp_dm AND mx.cangku = rk.cangku \n" +
                        "LEFT JOIN (SELECT sp_dm, cangku, SUM(cpsl) AS cpsl, SUM(cpsl*cpsj) AS cp_price \n" +
                        "           FROM yh_jinxiaocun_mingxi_mssql \n" +
                        "           WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') AND gs_name = @gongsi \n" +
                        "           AND shijian <= @stop_date \n" +
                        "           GROUP BY sp_dm, cangku) AS ck ON ck.sp_dm = rk.sp_dm AND ck.cangku = rk.cangku \n" +
                        "LEFT JOIN (SELECT cpid, cpname, cplb, cangku, ISNULL(cpsl,0)+ISNULL(rksl,0)-ISNULL(cksl,0) AS jcsl \n" +
                        "           FROM (SELECT link_rk.cpid, link_rk.cpname, link_rk.cplb, link_rk.cangku, \n" +
                        "                        ISNULL(link_rk.cpsl,0) AS cpsl, ISNULL(link_rk.rksl,0) AS rksl, ISNULL(ck.cksl,0) AS cksl \n" +
                        "                 FROM (SELECT link_qc.cpid, link_qc.cpname, link_qc.cplb, link_qc.cangku, link_qc.cpsl, rk.rksl \n" +
                        "                       FROM (SELECT cp.cpid, cp.cpname, cp.cplb, cp.cangku, qc.cpsl \n" +
                        "                             FROM (SELECT cpid, cpname, cplb, cangku FROM yh_jinxiaocun_qichushu_mssql WHERE gs_name = @gongsi \n" +
                        "                                   UNION \n" +
                        "                                   SELECT sp_dm, cpname, cplb, cangku FROM yh_jinxiaocun_mingxi_mssql WHERE gs_name = @gongsi) AS cp \n" +
                        "                             LEFT JOIN (SELECT cpid, cplb, cpname, cangku, SUM(cpsl) AS cpsl \n" +
                        "                                        FROM yh_jinxiaocun_qichushu_mssql WHERE gs_name = @gongsi \n" +
                        "                                        GROUP BY cpid, cpname, cplb, cangku) AS qc \n" +
                        "                             ON cp.cpid = qc.cpid AND cp.cpname = qc.cpname AND cp.cplb = qc.cplb AND cp.cangku = qc.cangku) AS link_qc \n" +
                        "                       LEFT JOIN (SELECT sp_dm, cpname, cplb, cangku, SUM(cpsl) AS rksl \n" +
                        "                                  FROM yh_jinxiaocun_mingxi_mssql \n" +
                        "                                  WHERE (mxtype = '入库' OR mxtype = '调拨入库' OR mxtype = '盘盈入库') AND gs_name = @gongsi \n" +
                        "                                  AND shijian <= @stop_date \n" +
                        "                                  GROUP BY sp_dm, cpname, cplb, cangku) AS rk \n" +
                        "                       ON rk.sp_dm = link_qc.cpid AND rk.cpname = link_qc.cpname AND rk.cplb = link_qc.cplb AND rk.cangku = link_qc.cangku) AS link_rk \n" +
                        "                 LEFT JOIN (SELECT sp_dm, cpname, cplb, cangku, SUM(cpsl) AS cksl \n" +
                        "                            FROM yh_jinxiaocun_mingxi_mssql \n" +
                        "                            WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') AND gs_name = @gongsi \n" +
                        "                            AND shijian <= @stop_date \n" +
                        "                            GROUP BY sp_dm, cpname, cplb, cangku) AS ck \n" +
                        "                 ON ck.sp_dm = link_rk.cpid AND ck.cpname = link_rk.cpname AND ck.cplb = link_rk.cplb AND ck.cangku = link_rk.cangku) AS jc_data) AS jc \n" +
                        "           ON mx.sp_dm = jc.cpid AND mx.cpname = jc.cpname AND mx.cplb = jc.cplb AND mx.cangku = jc.cangku \n" +
                        "LEFT JOIN (SELECT cpid, cpname, cplb, cangku, cpsj \n" +
                        "           FROM yh_jinxiaocun_qichushu_mssql WHERE gs_name = @gongsi \n" +
                        "           GROUP BY cpid, cpname, cplb, cangku, cpsj) AS qc \n" +
                        "           ON mx.sp_dm = qc.cpid AND mx.cpname = qc.cpname AND mx.cplb = qc.cplb AND mx.cangku = qc.cangku";

                // 构建条件
                List<String> conditions = new ArrayList<>();

                if (product_name != null && !product_name.isEmpty()) {
                    conditions.add("mx.cpname = @product_name");
                }

                if (wareHouse != null && !wareHouse.isEmpty()) {
                    conditions.add("mx.cangku = @warehouse");
                }

                // 添加WHERE条件（在正确位置）
                StringBuilder sqlBuilder = new StringBuilder(baseSql);

                if (!conditions.isEmpty()) {
                    sqlBuilder.append(" WHERE ");
                    for (int i = 0; i < conditions.size(); i++) {
                        if (i > 0) {
                            sqlBuilder.append(" AND ");
                        }
                        sqlBuilder.append(conditions.get(i));
                    }
                }

                // 添加ORDER BY
                sqlBuilder.append(" ORDER BY mx.cpname, mx.cangku");

                String sql = sqlBuilder.toString();
                System.out.println("生成的SQL: " + sql);

                // 准备参数
                List<Object> params = new ArrayList<>();
                Collections.addAll(params, company, company, js, company, js,
                        company, company, company, company, js, company, js, company);

                if (product_name != null && !product_name.isEmpty()) {
                    params.add(product_name);
                }
                if (wareHouse != null && !wareHouse.isEmpty()) {
                    params.add(wareHouse);
                }

                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, params.toArray());
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL版本 - 使用相同的修复逻辑
                String baseSql = "SELECT mx.sp_dm, mx.cpname, mx.cplb, mx.cangku, \n" +
                        "       IFNULL(jc.jcsl,0) AS ruku_num, IFNULL(rk.cp_price,0) AS ruku_price, \n" +
                        "       IFNULL(ck.cpsl,0) AS chuku_num, IFNULL(ck.cp_price,0) AS chuku_price, \n" +
                        "       IFNULL(qc.cpsj,0) AS qc_cpsj \n" +
                        "FROM (SELECT sp_dm, cpname, cplb, cangku FROM yh_jinxiaocun_mingxi WHERE gs_name = ? GROUP BY sp_dm, cpname, cplb, cangku) AS mx \n" +
                        "LEFT JOIN (SELECT sp_dm, cangku, SUM(cpsl) AS cpsl, SUM(cpsl*cpsj) AS cp_price \n" +
                        "           FROM yh_jinxiaocun_mingxi \n" +
                        "           WHERE (mxtype = '入库' OR mxtype = '调拨入库' OR mxtype = '盘盈入库') AND gs_name = ? \n" +
                        "           AND shijian <= ? \n" +
                        "           GROUP BY sp_dm, cangku) AS rk ON mx.sp_dm = rk.sp_dm AND mx.cangku = rk.cangku \n" +
                        "LEFT JOIN (SELECT sp_dm, cangku, SUM(cpsl) AS cpsl, SUM(cpsl*cpsj) AS cp_price \n" +
                        "           FROM yh_jinxiaocun_mingxi \n" +
                        "           WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') AND gs_name = ? \n" +
                        "           AND shijian <= ? \n" +
                        "           GROUP BY sp_dm, cangku) AS ck ON ck.sp_dm = rk.sp_dm AND ck.cangku = rk.cangku \n" +
                        "LEFT JOIN (SELECT cpid, cpname, cplb, cangku, IFNULL(cpsl,0)+IFNULL(rksl,0)-IFNULL(cksl,0) AS jcsl \n" +
                        "           FROM (SELECT link_rk.cpid, link_rk.cpname, link_rk.cplb, link_rk.cangku, \n" +
                        "                        IFNULL(link_rk.cpsl,0) AS cpsl, IFNULL(link_rk.rksl,0) AS rksl, IFNULL(ck.cksl,0) AS cksl \n" +
                        "                 FROM (SELECT link_qc.cpid, link_qc.cpname, link_qc.cplb, link_qc.cangku, link_qc.cpsl, rk.rksl \n" +
                        "                       FROM (SELECT cp.cpid, cp.cpname, cp.cplb, cp.cangku, qc.cpsl \n" +
                        "                             FROM (SELECT cpid, cpname, cplb, cangku FROM yh_jinxiaocun_qichushu WHERE gs_name = ? \n" +
                        "                                   UNION \n" +
                        "                                   SELECT sp_dm, cpname, cplb, cangku FROM yh_jinxiaocun_mingxi WHERE gs_name = ?) AS cp \n" +
                        "                             LEFT JOIN (SELECT cpid, cplb, cpname, cangku, SUM(cpsl) AS cpsl \n" +
                        "                                        FROM yh_jinxiaocun_qichushu WHERE gs_name = ? \n" +
                        "                                        GROUP BY cpid, cpname, cplb, cangku) AS qc \n" +
                        "                             ON cp.cpid = qc.cpid AND cp.cpname = qc.cpname AND cp.cplb = qc.cplb AND cp.cangku = qc.cangku) AS link_qc \n" +
                        "                       LEFT JOIN (SELECT sp_dm, cpname, cplb, cangku, SUM(cpsl) AS rksl \n" +
                        "                                  FROM yh_jinxiaocun_mingxi \n" +
                        "                                  WHERE (mxtype = '入库' OR mxtype = '调拨入库' OR mxtype = '盘盈入库') AND gs_name = ? \n" +
                        "                                  AND shijian <= ? \n" +
                        "                                  GROUP BY sp_dm, cpname, cplb, cangku) AS rk \n" +
                        "                       ON rk.sp_dm = link_qc.cpid AND rk.cpname = link_qc.cpname AND rk.cplb = link_qc.cplb AND rk.cangku = link_qc.cangku) AS link_rk \n" +
                        "                 LEFT JOIN (SELECT sp_dm, cpname, cplb, cangku, SUM(cpsl) AS cksl \n" +
                        "                            FROM yh_jinxiaocun_mingxi \n" +
                        "                            WHERE (mxtype = '出库' OR mxtype = '调拨出库' OR mxtype = '盘亏出库') AND gs_name = ? \n" +
                        "                            AND shijian <= ? \n" +
                        "                            GROUP BY sp_dm, cpname, cplb, cangku) AS ck \n" +
                        "                 ON ck.sp_dm = link_rk.cpid AND ck.cpname = link_rk.cpname AND ck.cplb = link_rk.cplb AND ck.cangku = link_rk.cangku) AS jc_data) AS jc \n" +
                        "           ON mx.sp_dm = jc.cpid AND mx.cpname = jc.cpname AND mx.cplb = jc.cplb AND mx.cangku = jc.cangku \n" +
                        "LEFT JOIN (SELECT cpid, cpname, cplb, cangku, cpsj \n" +
                        "           FROM yh_jinxiaocun_qichushu WHERE gs_name = ? \n" +
                        "           GROUP BY cpid, cpname, cplb, cangku, cpsj) AS qc \n" +
                        "           ON mx.sp_dm = qc.cpid AND mx.cpname = qc.cpname AND mx.cplb = qc.cplb AND mx.cangku = qc.cangku";

                // 构建条件
                List<String> conditions = new ArrayList<>();

                if (product_name != null && !product_name.isEmpty()) {
                    conditions.add("mx.cpname = ?");
                }

                if (wareHouse != null && !wareHouse.isEmpty()) {
                    conditions.add("mx.cangku = ?");
                }

                // 添加WHERE条件（在正确位置）
                StringBuilder sqlBuilder = new StringBuilder(baseSql);

                if (!conditions.isEmpty()) {
                    sqlBuilder.append(" WHERE ");
                    for (int i = 0; i < conditions.size(); i++) {
                        if (i > 0) {
                            sqlBuilder.append(" AND ");
                        }
                        sqlBuilder.append(conditions.get(i));
                    }
                }

                // 添加ORDER BY
                sqlBuilder.append(" ORDER BY mx.cpname, mx.cangku");

                String sql = sqlBuilder.toString();
                System.out.println("生成的SQL: " + sql);

                // 准备参数
                List<Object> params = new ArrayList<>();
                Collections.addAll(params, company, company, js, company, js,
                        company, company, company, company, js, company, js, company);

                if (product_name != null && !product_name.isEmpty()) {
                    params.add(product_name);
                }
                if (wareHouse != null && !wareHouse.isEmpty()) {
                    params.add(wareHouse);
                }

                base = new JxcBaseDao();
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, params.toArray());
                return list != null ? list : new ArrayList<>();
            }

        } catch (Exception e) {
            Log.e("SQLDebug", "获取库存盘点查询数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public boolean add_kucunpandian(List<YhJinXiaoCunMingXi> list) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            boolean pd = true;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_mingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
//                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), list.get(i).getMxtype(), list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku());
                    long result1 =     base2.executeOfId(sql,
                            list.get(i).getCplb(),list.get(i).getCpname(),list.get(i).getqc_cpsj(),
                            list.get(i).getCpsl(), list.get(i).getMxtype(), // 类型：出库
                            list.get(i).getOrderid(),list.get(i).getShijian(), list.get(i).getSpDm(),
                            list.get(i).getZhName(), list.get(i).getGsName(),
                            list.get(i).getcangku() // 源仓库
                    );


                    if (result1 < 0 ) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    YhJinXiaoCunMingXi item = list.get(i);
                    String cangku2 = item.getcangku2();
                    Log.e("CangkuDebug", "MySQL - 第" + i + "条记录的cangku2值: " + cangku2+ ", 商品: " + item.getCpname());
                    String sql = "insert into yh_jinxiaocun_mingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result1 = base.executeOfId(sql,
                            list.get(i).getCplb(),list.get(i).getCpname(),list.get(i).getqc_cpsj(),
                            list.get(i).getCpsl(), list.get(i).getMxtype(), // 类型：出库
                            list.get(i).getOrderid(),list.get(i).getShijian(), list.get(i).getSpDm(),
                            list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),
                            list.get(i).getcangku() // 源仓库
                    );
                    Log.e("SQLDebug", "出库记录结果: " + result1);
                    // 入库记录
                    base.close();
                    if (result1 < 0 ) {
                        pd = false;
                    }
                }
            }
            return pd;

        } catch (Exception e) {
            Log.e("SQLDebug", "批量插入盘库数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    public List<YhJinXiaoCunMingXi> getdingdan(String company,String printType ) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select orderid from yh_jinxiaocun_mingxi_mssql where gs_name=? and mxtype =? order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company,printType);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select orderid from yh_jinxiaocun_mingxi where gs_name=? and mxtype =? order by _id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细所有SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company,printType);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取明细列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<YhJinXiaoCunMingXi> getchuruku(String company,String ddh) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and orderid =? order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company,ddh);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and orderid =?  order by _id";
                base = new JxcBaseDao();
                Log.e("SQLDebug", "查询明细所有SQL: " + sql);
                List<YhJinXiaoCunMingXi> list = base.query(YhJinXiaoCunMingXi.class, sql, company,ddh);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取明细列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
