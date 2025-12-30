package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceFaPiao;
import com.example.myapplication.finance.entity.YhFinanceXiangMu;

import java.util.List;

public class YhFinanceXiangMuService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceXiangMu> getList(String company, String xiangmumingcheng) {
        String sql = "select a.id,a.company,a.ysyf,a.xiangmumingcheng,a.ysyfkemu,a.jine from (select row_number() over(order by id) as rownum,* from ysyfpeizhi where company = ? and xiangmumingcheng like '%'+ ? +'%')  as a ";
        base = new financeBaseDao();
        List<YhFinanceXiangMu> list = base.query(YhFinanceXiangMu.class, sql, company,xiangmumingcheng);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceXiangMu YhFinanceXiangMu) {
        String sql = "insert into ysyfpeizhi(ysyf,xiangmumingcheng,ysyfkemu,jine,company) values(?,?,?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceXiangMu.getYsyf(),YhFinanceXiangMu.getxiangmumingcheng(),YhFinanceXiangMu.getYsyfkemu(),YhFinanceXiangMu.getJine(),YhFinanceXiangMu.getCompany());
        return result > 0;
    }

    public List<YhFinanceXiangMu> getYingshouList(String company, String xiangmumingcheng) {
        String sql = "select a.id,a.company,a.ysyf,a.xiangmumingcheng,a.ysyfkemu,a.jine " +
                "from (select row_number() over(order by id) as rownum,* " +
                "from ysyfpeizhi " +
                "where company = ? and xiangmumingcheng = ? and ysyf = '应收') as a";

        base = new financeBaseDao();
        List<YhFinanceXiangMu> list = base.query(YhFinanceXiangMu.class, sql, company, xiangmumingcheng);
        return list;
    }


    public List<YhFinanceXiangMu> getYingfuList(String company, String xiangmumingcheng) {
        String sql = "select a.id,a.company,a.ysyf,a.xiangmumingcheng,a.ysyfkemu,a.jine " +
                "from (select row_number() over(order by id) as rownum,* " +
                "from ysyfpeizhi " +
                "where company = ? and xiangmumingcheng = ? and ysyf = '应付') as a";

        base = new financeBaseDao();
        List<YhFinanceXiangMu> list = base.query(YhFinanceXiangMu.class, sql, company, xiangmumingcheng);
        return list;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceXiangMu YhFinanceXiangMu) {
        String sql = "update ysyfpeizhi set ysyf=?,xiangmumingcheng=?,ysyfkemu=?,jine=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceXiangMu.getYsyf(),YhFinanceXiangMu.getxiangmumingcheng(), YhFinanceXiangMu.getYsyfkemu(),YhFinanceXiangMu.getJine(), YhFinanceXiangMu.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from ysyfpeizhi where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
