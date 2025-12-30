package com.example.myapplication.jxc.service;

import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunCaiGou;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;

import java.util.List;

public class YhJinXiaoCunCaiGouService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    /**
     * 添加调拨出入库
     */

    public boolean caigou(List<YhJinXiaoCunCaiGou> list) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            boolean pd = true;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_tuihuomingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku,ruku) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), "采购", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku(),list.get(i).getRuku());
                    if (result < 0) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_tuihuomingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku,ruku) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), "采购", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku(),list.get(i).getRuku());
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


    public boolean tuihuo(List<YhJinXiaoCunMingXi> list) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            boolean pd = true;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_tuihuomingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku,ruku) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), "采购退货", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku(),list.get(i).getRuku());
                    if (result < 0) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_tuihuomingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku,ruku) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), "采购退货", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku(),list.get(i).getRuku());
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


    public boolean xiaoshoutuihuo(List<YhJinXiaoCunMingXi> list) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            boolean pd = true;

            // 根据状态执行不同的业务逻辑
            if (shujukuValue == 1) {
                // SQL Server 版本
                base2 = new JxcServerDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_tuihuomingxi_mssql (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku,ruku) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base2.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), "销售退货", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku(),list.get(i).getRuku());
                    if (result < 0) {
                        pd = false;
                    }
                }

            } else {
                // MySQL 版本
                base = new JxcBaseDao();
                for (int i = 0; i < list.size(); i++) {
                    String sql = "insert into yh_jinxiaocun_tuihuomingxi (cplb,cpname,cpsj,cpsl,mxtype,orderid,shijian,sp_dm,shou_h,zh_name,gs_name,cangku,ruku) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    long result = base.executeOfId(sql, list.get(i).getCplb(), list.get(i).getCpname(), list.get(i).getCpsj(), list.get(i).getCpsl(), "销售退货", list.get(i).getOrderid(), list.get(i).getShijian(), list.get(i).getSpDm(), list.get(i).getShou_h(), list.get(i).getZhName(), list.get(i).getGsName(),list.get(i).getcangku(),list.get(i).getRuku());
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
