package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.LineChart;
import com.example.myapplication.scheduling.entity.OrderBom;
import com.example.myapplication.scheduling.entity.OrderInfo;

import java.util.List;

public class OrderInfoService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<OrderInfo> getList(String company, String product_name, String order_id) {
        base = new SchedulingDao();
        String sql = "select * from order_info where company=? and product_name like '%' + ? + '%' and order_id like '%' + ? + '%'  ";
        List<OrderInfo> list = base.query(OrderInfo.class, sql, company, product_name, order_id);
        return list;
    }

    /**
     * 刷新
     */
    public List<OrderInfo> getLast() {
        base = new SchedulingDao();
        String sql = "select top 1 * from order_info order by id desc ";
        List<OrderInfo> list = base.query(OrderInfo.class, sql);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(OrderInfo orderInfo) {
        String sql = "insert into order_info(code,product_name,norms,order_id,set_date,set_num,company,is_complete) values(?,?,?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, orderInfo.getCode(), orderInfo.getProduct_name(), orderInfo.getNorms(), orderInfo.getOrder_id(), orderInfo.getSet_date(), orderInfo.getSet_num(), orderInfo.getCompany(), orderInfo.getIs_complete());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(OrderInfo orderInfo) {
        String sql = "update order_info set code=?,product_name=?,norms=?,order_id=?,set_date=?,set_num=?,company=?,is_complete=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql, orderInfo.getCode(), orderInfo.getProduct_name(), orderInfo.getNorms(), orderInfo.getOrder_id(), orderInfo.getSet_date(), orderInfo.getSet_num(), orderInfo.getCompany(), orderInfo.getIs_complete(), orderInfo.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from order_info where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }

    /**
     * 新增
     */
    public boolean insertOrderBom(OrderBom orderBom) {
        String sql = "insert into order_bom(order_id,bom_id,use_num) values(?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, orderBom.getOrder_id(), orderBom.getBom_id(), orderBom.getUse_num());
        return result > 0;
    }

    /**
     * 删除
     */
    public boolean deleteOrderBom(int order_id) {
        String sql = "delete from order_bom where order_id = ?";
        base = new SchedulingDao();
        return base.execute(sql, order_id);
    }

    public List<OrderInfo> getOrderId(){
        String sql="select oi.id,oi.is_complete,oi.code,oi.product_name,oi.norms,oi.set_date,oi.company,oi.order_id,oi.set_num-sum(isnull(wd.work_num, 0)) as set_num from order_info as oi left join work_detail as wd on oi.id = wd.order_id group by oi.id,oi.code,oi.product_name,oi.norms,oi.set_date,oi.company,oi.order_id,oi.set_num,oi.is_complete having oi.set_num-sum(isnull(wd.work_num, 0)) > 0";
        base = new SchedulingDao();
        List<OrderInfo> list = base.query(OrderInfo.class, sql);
        return list;
    }

    public List<LineChart> getLineChart(String date, String company){
        String sql="select sum(case when set_date like ? + '-01%' then set_num else 0 end) as month1,sum(case when set_date like ? + '-02%' then set_num else 0 end) as month2,sum(case when set_date like ? + '-03%' then set_num else 0 end) as month3,sum(case when set_date like ? + '-04%' then set_num else 0 end) as month4,sum(case when set_date like ? + '-05%' then set_num else 0 end) as month5,sum(case when set_date like ? + '-06%' then set_num else 0 end) as month6,sum(case when set_date like ? + '-07%' then set_num else 0 end) as month7,sum(case when set_date like ? + '-08%' then set_num else 0 end) as month8,sum(case when set_date like ? + '-09%' then set_num else 0 end) as month9,sum(case when set_date like ? + '-10%' then set_num else 0 end) as month10,sum(case when set_date like ? + '-11%' then set_num else 0 end) as month11,sum(case when set_date like ? + '-12%' then set_num else 0 end) as month12 from order_info where company = ?";
        base = new SchedulingDao();
        List<LineChart> list = base.query(LineChart.class, sql,date,date,date,date,date,date,date,date,date,date,date,date,company);
        return list;
    }

}
