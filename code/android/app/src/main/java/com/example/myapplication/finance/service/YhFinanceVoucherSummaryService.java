package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceVoucherSummary;

import java.util.List;
public class YhFinanceVoucherSummaryService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceVoucherSummary> getList(String this_word, String company, String start_date,String stop_date) {
        String sql = "select *,abstract as zhaiyao,voucherDate as insert_date from (select isnull((select name from Accounting where code = LEFT (vs.code, 4)),'')+isnull((select top 1 '-'+name from Accounting where code = LEFT (vs.code, 6) and code != LEFT (vs.code, 4)),'')+isnull((select top 1 '-'+name from Accounting where code = LEFT (vs.code, 8) and code != LEFT (vs.code, 6)),'') as full_name,vs.id,vs.word,vs.[no],voucherDate,vs.abstract,vs.code,vs.department,vs.expenditure,vs.note,vs.man,ac.name,isnull(ac.load,0) as load,isnull(ac.borrowed,0) as borrowed,vs.money,vs.real,ROW_NUMBER() over(order by vs.id) rownum from VoucherSummary as vs left join Accounting as ac on vs.code = ac.code and ac.company = ? where vs.company = ?) t where t.word like '%'+ ? +'%' and t.voucherDate >= convert(date,?) and t.voucherDate <= convert(date,?)";
        base = new financeBaseDao();
        List<YhFinanceVoucherSummary> list = base.query(YhFinanceVoucherSummary.class, sql,company,company,this_word,start_date,stop_date);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceVoucherSummary YhFinanceVoucherSummary) {
        String sql = "insert into VoucherSummary(word,no,abstract,code,department,expenditure,note,man,money,company,voucherDate,real) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceVoucherSummary.getWord(), YhFinanceVoucherSummary.getNo(),YhFinanceVoucherSummary.get_abstract(),YhFinanceVoucherSummary.getCode(),YhFinanceVoucherSummary.getDepartment(),YhFinanceVoucherSummary.getExpenditure(),YhFinanceVoucherSummary.getNote(),YhFinanceVoucherSummary.getMan(),YhFinanceVoucherSummary.getMoney(),YhFinanceVoucherSummary.getCompany(),YhFinanceVoucherSummary.getVoucherDate(),YhFinanceVoucherSummary.getReal());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceVoucherSummary YhFinanceVoucherSummary) {
        String sql = "update VoucherSummary set word=?,no=?,abstract=?,code=?,department=?,expenditure=?,note=?,man=?,money=?,voucherDate=?,real=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceVoucherSummary.getWord(), YhFinanceVoucherSummary.getNo(),YhFinanceVoucherSummary.get_abstract(),YhFinanceVoucherSummary.getCode(),YhFinanceVoucherSummary.getDepartment(),YhFinanceVoucherSummary.getExpenditure(),YhFinanceVoucherSummary.getNote(),YhFinanceVoucherSummary.getMan(),YhFinanceVoucherSummary.getMoney(),YhFinanceVoucherSummary.getVoucherDate(),YhFinanceVoucherSummary.getReal(),YhFinanceVoucherSummary.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from VoucherSummary where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
