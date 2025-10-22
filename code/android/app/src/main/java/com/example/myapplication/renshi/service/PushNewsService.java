package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.PushNewsDao;
import com.example.myapplication.renshi.entity.PushNews;

import java.util.List;

public class PushNewsService {

    private PushNewsDao base;

    public List<PushNews> getList() {
        base = new PushNewsDao();
        String sql = "SELECT tptop1,tptop2,tptop3,tptop4,tptop5,tptop6,topgao,xuankuan,xuangao,textbox,beizhu1  FROM product_pushnews WHERE gsname='云合未来' AND  xtname='云合人事管理系统' AND ((qidate IS NULL OR GETUTCDATE() >= CONVERT(DATETIME, LEFT(qidate, 10), 120)) AND (zhidate IS NULL OR GETUTCDATE() <= CONVERT(DATETIME, LEFT(zhidate, 10), 120)))";
        List<PushNews> list = base.query(PushNews.class, sql);
        return list;
    }
}
