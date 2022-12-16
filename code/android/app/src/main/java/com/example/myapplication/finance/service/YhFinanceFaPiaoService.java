package com.example.myapplication.finance.service;

import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import com.example.myapplication.finance.entity.YhFinanceFaPiao;

import java.util.List;


//经营支出
public class YhFinanceFaPiaoService {
    private financeBaseDao base;

    /**
     * 查询全部数据
     */
    public List<YhFinanceFaPiao> getList(String company,String kehu,String start_date,String stop_date) {
        String sql = "select a.id,a.company,a.type,a.riqi,a.zhaiyao,a.unit,a.invoice_type,a.invoice_no,a.jine,a.remarks  from (select row_number() over(order by id) as rownum,* from Invoice where company = ? and unit like '%'+ ? +'%' and convert(date,riqi)>=? and convert(date,riqi)<=?) as a ";
        base = new financeBaseDao();
        List<YhFinanceFaPiao> list = base.query(YhFinanceFaPiao.class, sql, company,kehu,start_date,stop_date);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceFaPiao YhFinanceFaPiao) {
        String sql = "insert into Invoice(type,riqi,zhaiyao,unit,invoice_type,invoice_no,jine,remarks,company) values(?,?,?,?,?,?,?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql, YhFinanceFaPiao.getType(),YhFinanceFaPiao.getRiqi(),YhFinanceFaPiao.getZhaiyao(),YhFinanceFaPiao.getUnit(),YhFinanceFaPiao.getInvoice_type(),YhFinanceFaPiao.getInvoice_no(),YhFinanceFaPiao.getJine(),YhFinanceFaPiao.getRemarks(), YhFinanceFaPiao.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceFaPiao YhFinanceFaPiao) {
        String sql = "update Invoice set type=?,riqi=?,zhaiyao=?,unit=?,invoice_type=?,invoice_no=?,jine=?,remarks=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql, YhFinanceFaPiao.getType(), YhFinanceFaPiao.getRiqi(), YhFinanceFaPiao.getZhaiyao(), YhFinanceFaPiao.getUnit(), YhFinanceFaPiao.getInvoice_type(), YhFinanceFaPiao.getInvoice_no(), YhFinanceFaPiao.getJine(), YhFinanceFaPiao.getRemarks(), YhFinanceFaPiao.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from Invoice where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
    }
}
