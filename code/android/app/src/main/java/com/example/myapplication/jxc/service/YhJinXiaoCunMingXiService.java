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
    public List<YhJinXiaoCunMingXi> getQuery(String company, String ks, String js) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                String sql = "select * from yh_jinxiaocun_mingxi_mssql where gs_name=? and shijian between ? and ? order by _id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunMingXi> list = base2.query(YhJinXiaoCunMingXi.class, sql, company, ks, js);
                return list != null ? list : new ArrayList<>();

            } else {
                // MySQL 版本
                String sql = "select * from yh_jinxiaocun_mingxi where gs_name=? and shijian between ? and ? order by _id";
                base = new JxcBaseDao();
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
                String sql = "update yh_jinxiaocun_mingxi_mssql set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,shou_h=? where _id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getShou_h(), yhJinXiaoCunMingXi.get_id());
                return result;

            } else {
                // MySQL 版本
                String sql = "update yh_jinxiaocun_mingxi set orderid=?,sp_dm=?,cpname=?,cplb=?,cpsj=?,cpsl=?,mxtype=?,shou_h=? where _id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunMingXi.getOrderid(), yhJinXiaoCunMingXi.getSpDm(), yhJinXiaoCunMingXi.getCpname(), yhJinXiaoCunMingXi.getCplb(), yhJinXiaoCunMingXi.getCpsj(), yhJinXiaoCunMingXi.getCpsl(), yhJinXiaoCunMingXi.getMxtype(), yhJinXiaoCunMingXi.getShou_h(), yhJinXiaoCunMingXi.get_id());
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
                    String sql = "insert into yh_jinxiaocun_mingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name) values(?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), list.get(i).getMxtype(), list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName());
                    if (result < 0) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_mingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name) values(?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), list.get(i).getMxtype(), list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName());
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

}
