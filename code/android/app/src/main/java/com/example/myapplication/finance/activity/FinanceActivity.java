package com.example.myapplication.finance.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceQuanXianService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinanceActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXianService yhFinanceQuanXianService;
    private List<YhFinanceQuanXian> list;
    private boolean pd;

    private Banner banner;
    private List<Integer> banner_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_main);

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();

        initData();
        banner = findViewById(R.id.main_banner);

        banner.setAdapter(new BannerImageAdapter<Integer>(banner_data) {

            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
            }
        });

        // 开启循环轮播
        banner.isAutoLoop(true);
        banner.setIndicator(new CircleIndicator(this));
        banner.setScrollBarFadeDuration(1000);
        // 设置指示器颜色(TODO 即选中时那个小点的颜色)
        banner.setIndicatorSelectedColor(Color.GREEN);
        // 开始轮播
        banner.start();

        init();

        LinearLayout bumenshezhi = findViewById(R.id.bumenshezhi);
        bumenshezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getBmszSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else{
                    Intent intent = new Intent(FinanceActivity.this, DepartmentActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout kaizhixiangmu = findViewById(R.id.kaizhixiangmu);
        kaizhixiangmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getKzxmSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, ExpenditureActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout kemuzongzhang = findViewById(R.id.kemuzongzhang);
        kemuzongzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getKmzzSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, KeMuZongZhangActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout pingzhenghuizong = findViewById(R.id.pingzhenghuizong);
        pingzhenghuizong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getPzhzSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, VoucherSummaryActivity.class);
                    startActivity(intent);
                }
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
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getJjtzSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, JiJianTaiZhangActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout jijianzongzhang = findViewById(R.id.jijianzongzhang);
        jijianzongzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getJjzzSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, JiJianZongZhangActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout xianjinliuliang = findViewById(R.id.xianjinliuliang);
        xianjinliuliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getXjllSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, XianJinLiuLiangActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout zichanfuzhai = findViewById(R.id.zichanfuzhai);
        zichanfuzhai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getZcfzSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, ZiChanFuZhaiActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout liyisunyi = findViewById(R.id.liyisunyi);
        liyisunyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getLysySelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, LiYiSunYiActivity.class);
                    startActivity(intent);
                }
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
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getZhglSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, ZhangHaoGuanLiActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout zhinengkanban = findViewById(R.id.zhinengkanban);
        zhinengkanban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (YhFinanceQuanXian yhFinanceQuanXian : list) {
                    if (yhFinanceQuanXian.getZnkbSelect().equals("是")) {
                        myApplication.setYhFinanceQuanXian(yhFinanceQuanXian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(FinanceActivity.this, "无权限！");
                }else {
                    Intent intent = new Intent(FinanceActivity.this, ZhiNengKanBanActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void init() {
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhFinanceQuanXianService = new YhFinanceQuanXianService();
                    list = yhFinanceQuanXianService.getList(yhFinanceUser.getBianhao());
                    if (list == null) return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = null;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    private long exitTime = 0;

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.finance_banner_01);
        banner_data.add(R.drawable.finance_banner_01);
        banner_data.add(R.drawable.finance_banner_01);
    }

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
