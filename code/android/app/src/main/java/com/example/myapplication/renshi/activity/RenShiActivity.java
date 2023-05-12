package com.example.myapplication.renshi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.entity.SystemBanner;
import com.example.myapplication.finance.activity.DepartmentActivity;
import com.example.myapplication.finance.activity.FinanceActivity;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.service.SystemService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.MarqueeTextView;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RenShiActivity extends AppCompatActivity {

    private Banner banner;
    private List<Integer> banner_data;

    private YhRenShiUser yhRenShiUser;
    private SystemService systemService;
    private List<SystemBanner> list1;
    private List<SystemBanner> list2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renshi_main);
        systemService = new SystemService();
        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
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

        LinearLayout renyuanxinxiguanli = findViewById(R.id.renyuanxinxiguanli);
        renyuanxinxiguanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, RenYuanXinXiGuanLiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout peizhibiao = findViewById(R.id.peizhibiao);
        peizhibiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, PeiZhiBiaoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout kaoqinjilu = findViewById(R.id.kaoqinjilu);
        kaoqinjilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, KaoQinJiLuActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout baoshui = findViewById(R.id.baoshui);
        baoshui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, BaoShuiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout gongzitiao = findViewById(R.id.gongzitiao);
        gongzitiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, GongZiTiaoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout kaoqinbiao = findViewById(R.id.kaoqinbiao);
        kaoqinbiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, KaoQinBiaoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout gerensuodeshui = findViewById(R.id.gerensuodeshui);
        gerensuodeshui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, GeRenSuoDeShuiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout yuangongdangan = findViewById(R.id.yuangongdangan);
        yuangongdangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, YuanGongDangAnActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout shebao = findViewById(R.id.shebao);
        shebao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, SheBaoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout bumenhuizong = findViewById(R.id.bumenhuizong);
        bumenhuizong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, BuMenHuiZongActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout gongzimingxi = findViewById(R.id.gongzimingxi);
        gongzimingxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, GongZiMingXiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout shengritixing = findViewById(R.id.shengritixing);
        shengritixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, ShengRiTiXingActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout baopan = findViewById(R.id.baopan);
        baopan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenShiActivity.this, BaoPanActivity.class);
                startActivity(intent);
            }
        });

    }

    private long exitTime = 0;

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.renshi_banner_01);
        banner_data.add(R.drawable.renshi_banner_01);
        banner_data.add(R.drawable.renshi_banner_01);
    }

    private void systeminit() {
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MarqueeTextView marqueeTextView = findViewById(R.id.marquee);
                if(list1.size() > 0){
                    marqueeTextView.setText(list1.get(0).getText());
                }else if(list2.size() > 0){
                    marqueeTextView.setText(list2.get(0).getText());
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    systemService = new SystemService();
                    list1 = systemService.getList("进销存",yhRenShiUser.getL());
                    list2 = systemService.getTongYongList("进销存");
                    if (list1 == null && list2 == null) return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = null;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
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
