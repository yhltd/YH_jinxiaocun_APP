package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Copy2;

import java.util.List;

public class Copy2Service {
    private FenquanDao base;

    public List<Copy2> getList(String company) {
        String sql = "select * from baitaoquanxian_copy2 where 公司=?";
        base = new FenquanDao();
        List<Copy2> list = base.query(Copy2.class, sql, company);
        return list;
    }

    public boolean update(int id,String sql) {
        base = new FenquanDao();
        boolean result = base.execute(sql, id);
        return result;
    }

    public boolean updateAll(int id) {
        String sql = "update baitaoquanxian_copy2 set A='',B='',C='',D='',E='',F='',G='',H='',I='',J='',K='',L='',M='',N='',O='',P='',Q='',R='',S='',T='',U='',V='',W='',X='',Y='',Z='',AA='',AB='',AC='',AD='',AE='',AF='',AG='',AH='',AI='',AJ='',AK='',AL='',AM='',AN='',AO='',AP='',AQ='',AR='',ASS='',AT='',AU='',AV='',AW='',AX='',AY='',AZ='',BA='',BB='',BC='',BD='',BE='',BF='',BG='',BH='',BI='',BJ='',BK='',BL='',BM='',BN='',BO='',BP='',BQ='',BR='',BS='',BT='',BU='',BV='',BW='',BX='',BYY='',BZ='',CA='',CB='',CC='',CD='',CE='',CF='',CG='',CH='',CI='',CJ='',CK='',CL='',CM='',CN='',CO='',CP='',CQ='',CR='',CS='',CT='',CU='',CV='' where id=?";
        base = new FenquanDao();
        boolean result = base.execute(sql,id);
        return result;
    }
}
