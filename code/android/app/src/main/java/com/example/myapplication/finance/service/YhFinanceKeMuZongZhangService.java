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
     * 年初余额图表查询全部数据
     */
    public List<YhFinanceKeMuZongZhang> getChartList1(String company) {
        String sql = "select sum(a.load) as load,sum(a.borrowed) as borrowed from(select code,load,borrowed,LEFT(code,1) as class from Accounting where company = ?) as a GROUP BY a.class;";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company);
        return list;
    }

    /**
     * 凭证金额图表查询全部数据
     */
    public List<YhFinanceKeMuZongZhang> getChartList3(String company) {
        String sql = "SELECT sum(a.load) +ISNULL(sum(v.money), 0) as load,sum(a.borrowed) as borrowed from Accounting as a LEFT JOIN VoucherSummary as v on v.code = a.code where a.company = ? GROUP BY left(a.code,1)";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company);
        return list;
    }

    /**
     * 凭证金额图表查询全部数据
     */
    public List<YhFinanceKeMuZongZhang> getChartList2(String company,String start_date,String stop_date) {
        String sql = "select isnull(sum(v.money),0) as load,a.direction from VoucherSummary as v,Accounting as a where a.company = ? and v.company = ? and a.code = v.code and v.voucherDate >= ? and v.voucherDate <= ? GROUP BY a.direction;";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,company,start_date,stop_date);
        return list;
    }

    /**
     * 资产负债图表查询资产类
     */
    public List<YhFinanceKeMuZongZhang> getChartList4_01(String company,String start_date) {
        String sql = "select isnull(sum(a.start_year),0) as [load],isnull(sum(a.end_year),0) as borrowed from (select v.company,sum(load-borrowed) as start_year,sum([load]-borrowed+ISNULL(v.money, 0)) as end_year from Accounting as a left join VoucherSummary as v on a.code = v.code WHERE left(a.code,1) = 1 and a.company = ? and year(v.voucherDate) = year(?) GROUP BY a.code,a.name,v.company) as a where a.company = ? or a.company is null";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,start_date,company);
        return list;
    }

    /**
     * 资产负债图表查询负债类
     */
    public List<YhFinanceKeMuZongZhang> getChartList4_02(String company,String start_date) {
        String sql = "select isnull(sum(a.start_year),0) as [load],isnull(sum(a.end_year),0) as borrowed from (select v.company,sum(borrowed-load) as start_year,sum(borrowed-[load]+ISNULL(v.money, 0)) as end_year from Accounting as a left join VoucherSummary as v on a.code = v.code WHERE left(a.code,1) = 2 and a.company = ? and year(v.voucherDate) = year(?) GROUP BY a.code,a.name,v.company) as a where a.company = ? or a.company is null";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,start_date,company);
        return list;
    }

    /**
     * 资产负债图表查询权益类
     */
    public List<YhFinanceKeMuZongZhang> getChartList4_03(String company,String start_date) {
        String sql = "select isnull(sum(a.start_year),0) as [load],isnull(sum(a.end_year),0) as borrowed from (select v.company,sum(borrowed-load) as start_year,sum(borrowed-[load]+ISNULL(v.money, 0)) as end_year from Accounting as a left join VoucherSummary as v on a.code = v.code WHERE left(a.code,1) = 3 and a.company = ? and year(v.voucherDate) = year(?) GROUP BY a.code,a.name,v.company) as a where a.company = ? or a.company is null";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,start_date,company);
        return list;
    }

    /**
     * 利润合计图表查询全部数据
     */
    public List<YhFinanceKeMuZongZhang> getChartList5(String company,String start_date) {
        String sql = "select isnull(sum(a.sum_month),0) as [load],isnull(sum(a.sum_year),0) as borrowed from (select y.sum_month,y.sum_year,a.direction from Accounting as a,(SELECT code,(SELECT sum(money) FROM VoucherSummary WHERE MONTH(voucherDate) = MONTH(?) AND code = y.code) AS sum_month,(SELECT sum(money) FROM VoucherSummary WHERE YEAR(voucherDate) = year(?) AND code = y.code) AS sum_year FROM VoucherSummary AS y WHERE company = ? and YEAR(voucherDate) = year(?) GROUP BY y.code) as y where a.code = y.code and a.company = ? and a.direction in (0,1)) as a GROUP BY a.direction";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,start_date,start_date,company,start_date,company);
        return list;
    }

    /**
     * 现金流量图表查询投资结余
     */
    public List<YhFinanceKeMuZongZhang> getChartList6_1(String company,String start_date) {
        String sql = "select ISNULL(sum(a.money_month), 0) as [load],ISNULL(sum(a.money_year), 0) as borrowed from (select expenditure,(select sum(s.money) from VoucherSummary as s where company = ? and year(voucherDate) = year(?) and month(voucherDate) = MONTH(?) and s.expenditure = v.expenditure) as money_month,(select sum(s.money) from VoucherSummary as s where company = ? and year(voucherDate) = year(?) and s.expenditure = v.expenditure) as money_year from VoucherSummary as v where company = ? GROUP BY expenditure) as a where a.expenditure in (select financingExpenditure from FinancingExpenditure) or a.expenditure in (select financingIncome from FinancingIncome)";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,start_date,start_date,company,start_date,company);
        return list;
    }

    /**
     * 现金流量图表查询筹资结余
     */
    public List<YhFinanceKeMuZongZhang> getChartList6_2(String company,String start_date) {
        String sql = "select ISNULL(sum(a.money_month), 0) as [load],ISNULL(sum(a.money_year), 0) as borrowed from (select expenditure,(select sum(s.money) from VoucherSummary as s where company = ? and year(voucherDate) = year(?) and month(voucherDate) = MONTH(?) and s.expenditure = v.expenditure) as money_month,(select sum(s.money) from VoucherSummary as s where company = ? and year(voucherDate) = year(?) and s.expenditure = v.expenditure) as money_year from VoucherSummary as v where company = ? GROUP BY expenditure) as a where a.expenditure in (select investmentExpenditure from InvestmentExpenditure) or a.expenditure in (select investmentIncome from InvestmentIncome)";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,start_date,start_date,company,start_date,company);
        return list;
    }

    /**
     * 现金流量图表查询筹资结余
     */
    public List<YhFinanceKeMuZongZhang> getChartList6_3(String company,String start_date) {
        String sql = "select ISNULL(sum(a.money_month), 0) as [load],ISNULL(sum(a.money_year), 0) as borrowed from (select expenditure,(select sum(s.money) from VoucherSummary as s where company = ? and year(voucherDate) = year(?) and month(voucherDate) =MONTH(?) and s.expenditure = v.expenditure) as money_month,(select sum(s.money) from VoucherSummary as s where company = ? and year(voucherDate) = year(?) and s.expenditure = v.expenditure) as money_year from VoucherSummary as v where company = ? GROUP BY expenditure) as a where a.expenditure in (select managementExpenditure from ManagementExpenditure) or a.expenditure in (select managementIncome from ManagementIncome)";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,start_date,start_date,company,start_date,company);
        return list;
    }

    /**
     * 查询科目代码
     */
    public List<YhFinanceKeMuZongZhang> getCodeList(String company,String code,int len) {
        String sql = "select * from Accounting where company = ? and left(code,1) = ? and len(code) = ?";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,code,len);
        return list;
    }

    /**
     * 查询科目代码
     */
    public List<YhFinanceKeMuZongZhang> getCodeListByCode(String company,String code,int len,String code2) {
        String sql = "select * from Accounting where company = ? and left(code,1) = ? and len(code) = ? and left(code,?) = ?";
        base = new financeBaseDao();
        List<YhFinanceKeMuZongZhang> list = base.query(YhFinanceKeMuZongZhang.class, sql,company,code,len,len-2,code2);
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
        String sql = "update Accounting set code=?,name=?,direction=?,load=?,borrowed=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceKeMuZongZhang.getCode(), YhFinanceKeMuZongZhang.getName(),YhFinanceKeMuZongZhang.isDirection(), YhFinanceKeMuZongZhang.getLoad(), YhFinanceKeMuZongZhang.getBorrowed(),YhFinanceKeMuZongZhang.getId());
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
