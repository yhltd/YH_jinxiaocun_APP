package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianReportForm;
import com.example.myapplication.mendian.entity.YhMendianRijiao;

import java.util.List;

public class YhMendianReportFormService {
    private MendianDao base;

    /**
     * 查询全部客户数据
     */
    public List<YhMendianReportForm> getList(String start_date, String end_date, String company) {
        String sql = "select ifnull(round(sum(ifnull(heji.xfje,0)),2),0) as xfje,ifnull(round(sum(ifnull(heji.ssje,0)),2),0) as ssje,ifnull(round(sum(ifnull(heji.yhje,0)),2),0) as yhje from orders as ord left join(select ddid,company,sum(convert(dj,float) * convert(gs,float)) as xfje,sum(convert(zhdj,float) * convert(gs,float)) as ssje,round(sum(convert(dj,float) * convert(gs,float)) - sum(convert(zhdj,float) * convert(gs,float)),2) as yhje from orders_details group by ddid) as heji on ord.ddh = heji.ddid and ord.company = heji.company where ord.company = ? and riqi >=? and riqi <= ?";
        base = new MendianDao();
        List<YhMendianReportForm> list = base.query(YhMendianReportForm.class, sql, company, start_date,end_date);
        return list;
    }

    /**
     * 查询全部客户数据
     */
    public List<YhMendianReportForm> getList2(String start_date, String end_date, String company) {
        String sql = "select count(*) as huiyuan_sum from member_info where company = ? union all select count(*) as xiadan_sum from(select hyzh from orders where company = ? and hyzh != '' and riqi >= ? and riqi <= ? group by hyzh) as xdhy union all select count(*) as dingdan_sum from orders where company = ? and riqi >= ? and riqi <= ?";
        base = new MendianDao();
        List<YhMendianReportForm> list = base.query(YhMendianReportForm.class, sql, company, company, start_date,end_date , company, start_date,end_date);
        return list;
    }

}
