package com.example.myapplication.finance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.jxc.activity.BiJiActivity;
import com.example.myapplication.jxc.activity.JiChuZiLiaoActivity;
import com.example.myapplication.jxc.activity.JinXiaoCunActivity;
import com.example.myapplication.jxc.activity.KehuActivity;
import com.example.myapplication.jxc.activity.KehuQueryActivity;
import com.example.myapplication.jxc.activity.ProductQueryActivity;
import com.example.myapplication.jxc.activity.QiChuActivity;
import com.example.myapplication.jxc.activity.RukuActivity;
import com.example.myapplication.jxc.activity.UserActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class FinanceActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_main);

        LinearLayout bumenshezhi = findViewById(R.id.bumenshezhi);
        bumenshezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, DepartmentActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout kaizhixiangmu = findViewById(R.id.kaizhixiangmu);
        kaizhixiangmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, ExpenditureActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout kemuzongzhang = findViewById(R.id.kemuzongzhang);
        kemuzongzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, KeMuZongZhangActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout pingzhenghuizong = findViewById(R.id.pingzhenghuizong);
        pingzhenghuizong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, VoucherSummaryActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout jijianpeizhi = findViewById(R.id.jijianpeizhi);
        jijianpeizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, JiJianPeiZhiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout jijiantaizhang = findViewById(R.id.jijiantaizhang);
        jijiantaizhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, JiJianTaiZhangActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout jijianzongzhang = findViewById(R.id.jijianzongzhang);
        jijianzongzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, JiJianZongZhangActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout xianjinliuliang = findViewById(R.id.xianjinliuliang);
        xianjinliuliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, XianJinLiuLiangActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout zichanfuzhai = findViewById(R.id.zichanfuzhai);
        zichanfuzhai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, ZiChanFuZhaiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout liyisunyi = findViewById(R.id.liyisunyi);
        liyisunyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, LiYiSunYiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout baobiao = findViewById(R.id.baobiao);
        baobiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, BaoBiaoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout yingshoubaobiao = findViewById(R.id.yingshoubaobiao);
        yingshoubaobiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, YingShouBaoBiaoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout yingfubaobiao = findViewById(R.id.yingfubaobiao);
        yingfubaobiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, YingFuBaoBiaoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout yingshoumingxizhang = findViewById(R.id.yingshoumingxizhang);
        yingshoumingxizhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, YingShouMingXiZhangActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout yingfumingxizhang = findViewById(R.id.yingfumingxizhang);
        yingfumingxizhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, YingFuMingXiZhangActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout fapiao = findViewById(R.id.fapiao);
        fapiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, FaPiaoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout zhanghaoguanli = findViewById(R.id.zhanghaoguanli);
        zhanghaoguanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, ZhangHaoGuanLiActivity.class);
                startActivity(intent);
            }
        });

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出到登录页面", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }




}
