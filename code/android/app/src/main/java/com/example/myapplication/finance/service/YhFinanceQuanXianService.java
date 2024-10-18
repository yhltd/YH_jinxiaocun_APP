package com.example.myapplication.finance.service;

import android.util.Log;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;

import java.util.ArrayList;
import java.util.List;

public class YhFinanceQuanXianService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceQuanXian> getList(String bianhao) {
        String sql = "select * from quanxian where bianhao =?";
        base = new financeBaseDao();
        List<YhFinanceQuanXian> list = base.query(YhFinanceQuanXian.class, sql,bianhao);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceQuanXian YhFinanceQuanXian) {
        String sql = "insert into quanxian(kmzz_add,kmzz_delete,kmzz_update,kmzz_select,kzxm_add,kzxm_delete,kzxm_update,kzxm_select,bmsz_add,bmsz_delete,bmsz_update,bmsz_select,pzhz_add,pzhz_delete,pzhz_update,pzhz_select,znkb_select,xjll_select,zcfz_select,lysy_select,jjtz_add,jjtz_delete,jjtz_update,jjtz_select,jjzz_add,jjzz_delete,jjzz_update,jjzz_select,zhgl_add,zhgl_delete,zhgl_update,zhgl_select,bianhao) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceQuanXian.getKmzzAdd(), YhFinanceQuanXian.getKmzzDelete(),YhFinanceQuanXian.getKmzzUpdate(),YhFinanceQuanXian.getKmzzSelect(),YhFinanceQuanXian.getKzxmAdd(),YhFinanceQuanXian.getKzxmDelete(),YhFinanceQuanXian.getKzxmUpdate(),YhFinanceQuanXian.getKzxmSelect(),YhFinanceQuanXian.getBmszAdd(),YhFinanceQuanXian.getBmszDelete(),YhFinanceQuanXian.getBmszUpdate(),YhFinanceQuanXian.getBmszSelect(),YhFinanceQuanXian.getPzhzAdd(),YhFinanceQuanXian.getPzhzDelete(),YhFinanceQuanXian.getPzhzUpdate(),YhFinanceQuanXian.getPzhzSelect(),YhFinanceQuanXian.getZnkbSelect(),YhFinanceQuanXian.getXjllSelect(),YhFinanceQuanXian.getZcfzSelect(),YhFinanceQuanXian.getLysySelect(),YhFinanceQuanXian.getJjtzAdd(),YhFinanceQuanXian.getJjtzDelete(),YhFinanceQuanXian.getJjtzUpdate(),YhFinanceQuanXian.getJjtzSelect(),YhFinanceQuanXian.getJjzzAdd(),YhFinanceQuanXian.getJjzzDelete(),YhFinanceQuanXian.getJjzzUpdate(),YhFinanceQuanXian.getJjzzSelect(),YhFinanceQuanXian.getZhglAdd(),YhFinanceQuanXian.getZhglDelete(),YhFinanceQuanXian.getZhglUpdate(),YhFinanceQuanXian.getZhglSelect(),YhFinanceQuanXian.getBianhao());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceQuanXian YhFinanceQuanXian) {
        String sql = "update quanxian set kmzz_add=?,kmzz_delete=?,kmzz_update=?,kmzz_select=?,kzxm_delete=?,kzxm_update=?,kzxm_select=?,bmsz_add=?,bmsz_delete=?,bmsz_update=?,bmsz_select=?,pzhz_add=?,pzhz_delete=?,pzhz_update=?,pzhz_select=?,znkb_select=?,kzxm_add=?,xjll_select=?,zcfz_select=?,lysy_select=?,jjtz_add=?,jjtz_delete=?,jjtz_update=?,jjtz_select=?,jjzz_add=?,jjzz_delete=?,jjzz_update=?,jjzz_select=?,zhgl_add=?,zhgl_delete=?,zhgl_update=?,zhgl_select=? where bianhao=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceQuanXian.getKmzzAdd(), YhFinanceQuanXian.getKmzzDelete(),YhFinanceQuanXian.getKmzzUpdate(),YhFinanceQuanXian.getKmzzSelect(),YhFinanceQuanXian.getKzxmAdd(),YhFinanceQuanXian.getKzxmDelete(),YhFinanceQuanXian.getKzxmUpdate(),YhFinanceQuanXian.getKzxmSelect(),YhFinanceQuanXian.getBmszAdd(),YhFinanceQuanXian.getBmszDelete(),YhFinanceQuanXian.getBmszUpdate(),YhFinanceQuanXian.getBmszSelect(),YhFinanceQuanXian.getPzhzAdd(),YhFinanceQuanXian.getPzhzDelete(),YhFinanceQuanXian.getPzhzUpdate(),YhFinanceQuanXian.getPzhzSelect(),YhFinanceQuanXian.getZnkbSelect(),YhFinanceQuanXian.getXjllSelect(),YhFinanceQuanXian.getZcfzSelect(),YhFinanceQuanXian.getLysySelect(),YhFinanceQuanXian.getJjtzAdd(),YhFinanceQuanXian.getJjtzDelete(),YhFinanceQuanXian.getJjtzUpdate(),YhFinanceQuanXian.getJjtzSelect(),YhFinanceQuanXian.getJjzzAdd(),YhFinanceQuanXian.getJjzzDelete(),YhFinanceQuanXian.getJjzzUpdate(),YhFinanceQuanXian.getJjzzSelect(),YhFinanceQuanXian.getZhglAdd(),YhFinanceQuanXian.getZhglDelete(),YhFinanceQuanXian.getZhglUpdate(),YhFinanceQuanXian.getZhglSelect(),YhFinanceQuanXian.getBianhao());
        Log.d("bianhao",YhFinanceQuanXian.getBianhao());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(String bianhao) {
        String sql = "delete from quanxian where bianhao = ?";
        base = new financeBaseDao();
        return base.execute(sql, bianhao);
    }
}
