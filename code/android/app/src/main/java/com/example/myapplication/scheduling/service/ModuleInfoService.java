package com.example.myapplication.scheduling.service;

import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.ModuleInfo;

import java.util.ArrayList;
import java.util.List;

public class ModuleInfoService {
    private SchedulingDao base;

    /**
     * 刷新
     */
    public List<ModuleInfo> getList(String company, String type) {
        base = new SchedulingDao();
        List<ModuleInfo> list;
        String sql = "";
        if (type.equals("全部")) {
            sql = "select mi.id,mt.name as type,mi.name,num,parent_id from module_info mi left join module_type mt on mi.type_id=mt.id where mi.company=? ";
            list = base.query(ModuleInfo.class, sql, company);
        } else {
            sql = "select mi.id,mt.name as type,mi.name,num,parent_id from module_info mi left join module_type mt on mi.type_id=mt.id where mi.company=? and mt.name=? ";
            list = base.query(ModuleInfo.class, sql, company, type);
        }
        if (list != null) {
            for (ModuleInfo moduleInfo1 : list) {
                for (ModuleInfo moduleInfo2 : list) {
                    if (moduleInfo1.getParent_id() == moduleInfo2.getId()) {
                        moduleInfo1.setParent(moduleInfo2.getName());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(ModuleInfo moduleInfo) {
        String sql = "insert into module_info(type_id,name,num,parent_id,company) values(?,?,?,?,?)";

        base = new SchedulingDao();
        long result = base.executeOfId(sql, moduleInfo.getType_id(), moduleInfo.getName(), moduleInfo.getNum(), moduleInfo.getParent_id(), moduleInfo.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(ModuleInfo moduleInfo) {
        String sql = "update module_info set type_id=?,name=?,num=?,parent_id=? where id=? ";

        base = new SchedulingDao();
        boolean result = base.execute(sql, moduleInfo.getType_id(), moduleInfo.getName(), moduleInfo.getNum(), moduleInfo.getParent_id(), moduleInfo.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from module_info where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }


}
