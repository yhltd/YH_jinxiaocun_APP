package com.example.myapplication.finance.service;


import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import java.util.List;

public class YhFinanceKeMuZongZhangService {
    private financeBaseDao base;
    /**
     * 查询全部数据
     */
    public List<YhFinanceKeMuZongZhang> getList(int this_class,String company,String code) {
        String sql = "select 'Y' as mingxi,case direction when 1 then '借' when 0 then '贷' end as direction_text,case len(code) when 4 then 'I' when 6 then 'II' when 8 then 'III' end as grade,*,isnull((SELECT SUM(money) FROM VoucherSummary WHERE VoucherSummary.code = a.code),0) as money,(select top 1 name from Accounting as ac where ac.code = LEFT(a.code,4)) as name1,(select top 1 name from Accounting as ac where ac.code = LEFT(a.code,6)) as name2,(select top 1 name from Accounting as ac where ac.code = LEFT(a.code,8)) as name3 from (select *,ROW_NUMBER() over(order by LEN(code),id) as ROW_ID from (select * from (SELECT *,LEFT(code, 1) AS class from Accounting) as t where t.class = ?) as c )as a where a.company = ? and a.code like '%' + ? + '%'";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,this_class,company,code);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceKeMuZongZhang YhFinanceKeMuZongZhang) {
        String sql = "insert into Accounting(code,name,direction,load,borrowed,company) values(?,?,?,?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceKeMuZongZhang.getCode(), YhFinanceKeMuZongZhang.getName(),YhFinanceKeMuZongZhang.isDirection(),YhFinanceKeMuZongZhang.getLoad(),YhFinanceKeMuZongZhang.getBorrowed(),YhFinanceKeMuZongZhang.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceKeMuZongZhang YhFinanceKeMuZongZhang) {
        String sql = "update Accounting set code=?,name=?,load=?,borrowed=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceKeMuZongZhang.getCode(), YhFinanceKeMuZongZhang.getName(), YhFinanceKeMuZongZhang.getLoad(), YhFinanceKeMuZongZhang.getBorrowed(),YhFinanceKeMuZongZhang.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from Accounting where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }

}
