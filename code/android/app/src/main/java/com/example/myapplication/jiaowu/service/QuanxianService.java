package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Quanxian;

import java.util.List;

public class QuanxianService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<Quanxian> getList( String company) {
        String sql = "select p.id,t_id,view_name,`add`,del,upd,sel,RealName as s_name from power as p left join teacher as t on p.t_id=t.ID  where p.company = ?";
        base = new JiaowuBaseDao();
        List<Quanxian> list = base.query(AccountManagement.class, sql,company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(Quanxian quanxian) {
        String sql = "insert into power (t_id,view_name,`add`,del,upd,sel,company) values(?,?,?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, quanxian.getT_id(), quanxian.getView_name(), quanxian.getAdd(), quanxian.getDel(), quanxian.getUpd(), quanxian.getSel(),quanxian.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(Quanxian quanxian) {
        String sql = "update power set view_name= ? , `add`= ? ,del= ? ,upd= ? ,sel= ? where id= ? ";

        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, quanxian.getView_name(), quanxian.getAdd(), quanxian.getDel(), quanxian.getUpd(), quanxian.getSel(),quanxian.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from power where id = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
