package com.example.myapplication.jxc.service;

import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunQiChuShu;

import java.util.List;

public class YhJinXiaoCunQiChuShuService {
    private JxcBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhJinXiaoCunQiChuShu> getList(String company, String cpname) {
        String sql = "select * from yh_jinxiaocun_qichushu where gs_name = ? and cpname like '%' ? '%' order by _id";
        base = new JxcBaseDao();
        List<YhJinXiaoCunQiChuShu> list = base.query(YhJinXiaoCunQiChuShu.class, sql, company, cpname);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhJinXiaoCunQiChuShu yhJinXiaoCunQiChuShu) {
        String sql = "insert into yh_jinxiaocun_qichushu(cpname,cpid,cplb,cpsj,cpsl,gs_name) values(?,?,?,?,?,?)";

        base = new JxcBaseDao();
        long result = base.executeOfId(sql, yhJinXiaoCunQiChuShu.getCpname(), yhJinXiaoCunQiChuShu.getCpid(), yhJinXiaoCunQiChuShu.getCplb(), yhJinXiaoCunQiChuShu.getCpsj(), yhJinXiaoCunQiChuShu.getCpsl(),yhJinXiaoCunQiChuShu.getGs_name());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhJinXiaoCunQiChuShu yhJinXiaoCunQiChuShu) {
        String sql = "update yh_jinxiaocun_qichushu set cpname=?,cpid=?,cplb=?,cpsj=?,cpsl=? where _id=? ";

        base = new JxcBaseDao();
        boolean result = base.execute(sql, yhJinXiaoCunQiChuShu.getCpname(), yhJinXiaoCunQiChuShu.getCpid(), yhJinXiaoCunQiChuShu.getCplb(), yhJinXiaoCunQiChuShu.getCpsj(), yhJinXiaoCunQiChuShu.getCpsl(), yhJinXiaoCunQiChuShu.get_id());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from yh_jinxiaocun_qichushu where _id = ?";
        base = new JxcBaseDao();
        return base.execute(sql, id);
    }
}
