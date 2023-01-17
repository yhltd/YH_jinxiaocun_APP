package com.example.myapplication.renshi.service;

import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;

import java.util.List;

public class YhRenShiPeiZhiBiaoService {
    private renshiBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhRenShiPeiZhiBiao> getList(String company) {
        String sql = "select * from gongzi_peizhi where gongsi = ?";
        base = new renshiBaseDao();
        List<YhRenShiPeiZhiBiao> list = base.query(YhRenShiPeiZhiBiao.class, sql,company.replace("_hr",""));
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhRenShiPeiZhiBiao YhRenShiPeiZhiBiao) {
        String sql = "insert into gongzi_peizhi(kaoqin,kaoqin_peizhi,bumen,zhiwu,chidao_koukuan,geren_yiliao,qiye_yiliao,geren_shengyu,qiye_shengyu,geren_gongjijin,qiye_gongjijin,yiliao_jishu,geren_nianjin,qiye_nianjin,zhinajin,nianjin_jishu,lixi,geren_yanglao,qiye_yanglao,gangwei,gangwei_gongzi,geren_shiye,qiye_shiye,gongzi,shuilv,kuadu_gongzi,qiye_gongshang,jintie,nianjin1,jiabanfei,yansuangongshi,queqin_koukuan,gongsi) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new renshiBaseDao();
        long result = base.executeOfId(sql, YhRenShiPeiZhiBiao.getKaoqin(), YhRenShiPeiZhiBiao.getKaoqin_peizhi(),YhRenShiPeiZhiBiao.getBumen(),YhRenShiPeiZhiBiao.getZhiwu(),YhRenShiPeiZhiBiao.getChidao_koukuan(),YhRenShiPeiZhiBiao.getGeren_yiliao(),YhRenShiPeiZhiBiao.getQiye_yiliao(),YhRenShiPeiZhiBiao.getGeren_shengyu(),YhRenShiPeiZhiBiao.getQiye_shengyu(),YhRenShiPeiZhiBiao.getGeren_gongjijin(),YhRenShiPeiZhiBiao.getQiye_gongjijin(),YhRenShiPeiZhiBiao.getYiliao_jishu(),YhRenShiPeiZhiBiao.getGeren_nianjin(),YhRenShiPeiZhiBiao.getQiye_nianjin(),YhRenShiPeiZhiBiao.getZhinajin(),YhRenShiPeiZhiBiao.getNianjin_jishu(),YhRenShiPeiZhiBiao.getLixi(),YhRenShiPeiZhiBiao.getGeren_yanglao(),YhRenShiPeiZhiBiao.getQiye_yanglao(),YhRenShiPeiZhiBiao.getGangwei(),YhRenShiPeiZhiBiao.getGangwei_gongzi(),YhRenShiPeiZhiBiao.getGeren_shiye(),YhRenShiPeiZhiBiao.getQiye_shiye(),YhRenShiPeiZhiBiao.getGongzi(),YhRenShiPeiZhiBiao.getShuilv(),YhRenShiPeiZhiBiao.getKuadu_gongzi(),YhRenShiPeiZhiBiao.getQiye_gongshang(),YhRenShiPeiZhiBiao.getJintie(),YhRenShiPeiZhiBiao.getNianjin1(),YhRenShiPeiZhiBiao.getJiabanfei(),YhRenShiPeiZhiBiao.getYansuangongshi(),YhRenShiPeiZhiBiao.getQueqin_koukuan(), YhRenShiPeiZhiBiao.getGongsi());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhRenShiPeiZhiBiao YhRenShiPeiZhiBiao) {
        String sql = "update gongzi_peizhi set kaoqin=?,kaoqin_peizhi=?,bumen=?,zhiwu=?,chidao_koukuan=?,geren_yiliao=?,qiye_yiliao=?,geren_shengyu=?,qiye_shengyu=?,geren_gongjijin=?,qiye_gongjijin=?,yiliao_jishu=?,geren_nianjin=?,qiye_nianjin=?,zhinajin=?,nianjin_jishu=?,lixi=?,geren_yanglao=?,qiye_yanglao=?,gangwei=?,gangwei_gongzi=?,geren_shiye=?,qiye_shiye=?,gongzi=?,shuilv=?,kuadu_gongzi=?,qiye_gongshang=?,jintie=?,nianjin1=?,jiabanfei=?,yansuangongshi=?,queqin_koukuan=? where id=? ";
        base = new renshiBaseDao();
        boolean result = base.execute(sql, YhRenShiPeiZhiBiao.getKaoqin(), YhRenShiPeiZhiBiao.getKaoqin_peizhi(),YhRenShiPeiZhiBiao.getBumen(),YhRenShiPeiZhiBiao.getZhiwu(),YhRenShiPeiZhiBiao.getChidao_koukuan(),YhRenShiPeiZhiBiao.getGeren_yiliao(),YhRenShiPeiZhiBiao.getQiye_yiliao(),YhRenShiPeiZhiBiao.getGeren_shengyu(),YhRenShiPeiZhiBiao.getQiye_shengyu(),YhRenShiPeiZhiBiao.getGeren_gongjijin(),YhRenShiPeiZhiBiao.getQiye_gongjijin(),YhRenShiPeiZhiBiao.getYiliao_jishu(),YhRenShiPeiZhiBiao.getGeren_nianjin(),YhRenShiPeiZhiBiao.getQiye_nianjin(),YhRenShiPeiZhiBiao.getZhinajin(),YhRenShiPeiZhiBiao.getNianjin_jishu(),YhRenShiPeiZhiBiao.getLixi(),YhRenShiPeiZhiBiao.getGeren_yanglao(),YhRenShiPeiZhiBiao.getQiye_yanglao(),YhRenShiPeiZhiBiao.getGangwei(),YhRenShiPeiZhiBiao.getGangwei_gongzi(),YhRenShiPeiZhiBiao.getGeren_shiye(),YhRenShiPeiZhiBiao.getQiye_shiye(),YhRenShiPeiZhiBiao.getGongzi(),YhRenShiPeiZhiBiao.getShuilv(),YhRenShiPeiZhiBiao.getKuadu_gongzi(),YhRenShiPeiZhiBiao.getQiye_gongshang(),YhRenShiPeiZhiBiao.getJintie(),YhRenShiPeiZhiBiao.getNianjin1(),YhRenShiPeiZhiBiao.getJiabanfei(),YhRenShiPeiZhiBiao.getYansuangongshi(),YhRenShiPeiZhiBiao.getQueqin_koukuan(), YhRenShiPeiZhiBiao.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from gongzi_peizhi where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }
}
