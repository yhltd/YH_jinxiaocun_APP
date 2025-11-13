package com.example.myapplication.mendian.service;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUsers;

import java.util.List;

public class YhMendianProductshezhiService {
    private MendianDao base;

    /**
     * 查询全部商品信息
     */
    public List<YhMendianProductshezhi> getList(String produce_name, String type,String company) {
        String sql = "select * from product where company = ? and product_name like '%' ? '%' and type like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company, produce_name,type);
        return list;
    }

    /**
     * 查询商品详情
     */

    public List<YhMendianProductshezhi> getProduct(String produce_name) {
        String sql = "select * from product where product_name = ? ";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql,produce_name);
        return list;
    }

    /**
     * 查询全部商品类别
     */
    public List<YhMendianProductshezhi> getTypeList(String company) {
        String sql = "select type from product where company = ? group by type";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company);
        return list;
    }

    /**
     * 查询单种类别商品
     */
    public List<YhMendianProductshezhi> getListByType(String company,String type) {
        String sql = "select * from product where company = ? and type = ?";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company,type);
        return list;
    }

    /**
     * 新增员工
     */
    public boolean insertByProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
        String sql = "insert into product(company,product_bianhao,type,product_name,unit,price,chengben,specifications,practice,tingyong,xiangqing,photo,photo1,photo2,beizhu1) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql,
                yhMendianProductshezhi.getCompany(),
                yhMendianProductshezhi.getProduct_bianhao(),
                yhMendianProductshezhi.getType(),
                yhMendianProductshezhi.getProduct_name(),
                yhMendianProductshezhi.getUnit(),
                yhMendianProductshezhi.getPrice(),
                yhMendianProductshezhi.getChengben(),
                yhMendianProductshezhi.getSpecifications(),
                yhMendianProductshezhi.getPractice(),
                yhMendianProductshezhi.getTingyong(),
                yhMendianProductshezhi.getXiangqing(),  // 新增详情字段
                yhMendianProductshezhi.getPhoto(),      // 新增图片1字段
                yhMendianProductshezhi.getPhoto1(),     // 新增图片2字段
                yhMendianProductshezhi.getPhoto2() ,
                yhMendianProductshezhi.getBeizhu1()
                );
        return result > 0;
    }

    /**
     * 修改商品设置
     */
    public boolean updateByProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
        String sql = "update product set product_bianhao=?,type=?,product_name=?,unit=?,price=?,chengben=?,specifications=?,practice=?,tingyong=?,xiangqing=?,photo=?,photo1=?,photo2=?,beizhu1=? where id=? ";
        base = new MendianDao();
        boolean result = base.execute(sql,
                yhMendianProductshezhi.getProduct_bianhao(),
                yhMendianProductshezhi.getType(),
                yhMendianProductshezhi.getProduct_name(),
                yhMendianProductshezhi.getUnit(),
                yhMendianProductshezhi.getPrice(),
                yhMendianProductshezhi.getChengben(),
                yhMendianProductshezhi.getSpecifications(),
                yhMendianProductshezhi.getPractice(),
                yhMendianProductshezhi.getTingyong(),
                yhMendianProductshezhi.getXiangqing(),  // 新增详情字段
                yhMendianProductshezhi.getPhoto(),      // 新增图片1字段
                yhMendianProductshezhi.getPhoto1(),     // 新增图片2字段
                yhMendianProductshezhi.getPhoto2(),     // 新增图片3字段
                yhMendianProductshezhi.getBeizhu1(),
                yhMendianProductshezhi.getId()
        );
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
