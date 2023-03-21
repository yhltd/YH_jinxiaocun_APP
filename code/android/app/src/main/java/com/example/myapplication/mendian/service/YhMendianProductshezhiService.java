package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUsers;

import java.util.List;

public class YhMendianProductshezhiService {
    private MendianDao base;

    /**
     * 查询全部员工数据
     */
    public List<YhMendianProductshezhi> getList(String produce_name, String type,String company) {
        String sql = "select product_bianma,type,product_name,unit,price,chengben,tingong from product where company = ? and produce_name like '%' ? '%' and type like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company, produce_name,type);
        return list;
    }

    /**
     * 新增员工
     */
    public boolean insertByProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
        String sql = "insert into product(company,product_bianhao,type,product_name,unit,price,chengben,specifications,practice,tingyong,photo) values(?,?,?,?,?,?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql,yhMendianProductshezhi.getCompany(), yhMendianProductshezhi.getProduct_bianhao(), yhMendianProductshezhi.getType(), yhMendianProductshezhi.getProduct_name(), yhMendianProductshezhi.getUnit(), yhMendianProductshezhi.getPrice(), yhMendianProductshezhi.getChengben(), yhMendianProductshezhi.getSpecifications(), yhMendianProductshezhi.getPractice(), yhMendianProductshezhi.getTingyong(), yhMendianProductshezhi.getPhoto());
        return result > 0;
    }

    /**
     * 修改员工
     */
    public boolean updateByProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
        String sql = "update product set product_bianhao=?,type=?,product_name=?,unit=?,price=?,chengben=?,specifications=?,practice=?,tingyong=?,photo=? where id=? ";
        base = new MendianDao();
        boolean result = base.execute(sql, yhMendianProductshezhi.getProduct_bianhao(), yhMendianProductshezhi.getType(), yhMendianProductshezhi.getProduct_name(), yhMendianProductshezhi.getUnit(), yhMendianProductshezhi.getPrice(), yhMendianProductshezhi.getChengben(), yhMendianProductshezhi.getSpecifications(), yhMendianProductshezhi.getPractice(), yhMendianProductshezhi.getTingyong(), yhMendianProductshezhi.getPhoto(),yhMendianProductshezhi.getId());
        return result;
    }

    /**
     * 删除员工
     */
    public boolean deleteByProductshezhi(int id) {
        String sql = "delete from product where id = ?";
        base = new MendianDao();
        return base.execute(sql, id);
    }
}
