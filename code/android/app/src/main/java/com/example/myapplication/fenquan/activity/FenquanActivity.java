package com.example.myapplication.fenquan.activity;

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
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.scheduling.activity.ModuleActivity;
import com.example.myapplication.scheduling.activity.SchedulingActivity;
import com.example.myapplication.service.SystemService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.MarqueeTextView;
import com.example.myapplication.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FenquanActivity extends AppCompatActivity {
    private Renyuan renyuan;
    private List<Department> list;
    private SystemService systemService;

    private Banner banner;
    private List<SystemBanner> list1;
    private List<SystemBanner> list2;
    private List<Integer> banner_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fenquan_main);

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

        systemService = new SystemService();
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

        systeminit();

        LinearLayout renyuan = findViewById(R.id.renyuan);
        renyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, RenyuanActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout copy2 = findViewById(R.id.copy2);
        copy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, GongZuoTaiShiYongChangeActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout copy1 = findViewById(R.id.copy1);
        copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, RenyuanQuanXianActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout jisuan = findViewById(R.id.jisuan);
        jisuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, GongZuoTaiGongShiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout department = findViewById(R.id.department);
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, BuMenQuanXianActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout gongsi = findViewById(R.id.gongsi);
        gongsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, GongZuoTaiQuanXianChangeActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout workbench = findViewById(R.id.workbench);
        workbench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, GongZuoTaiActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout gongsi_chart = findViewById(R.id.gongsi_chart);
        gongsi_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, GongSiChartActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout renyuan_chart = findViewById(R.id.renyuan_chart);
        renyuan_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, RenYuanChartActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout grzx = findViewById(R.id.grzx);
        grzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FenquanActivity.this, GrzxActivity.class);
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

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.fenquan_banner_01);
        banner_data.add(R.drawable.fenquan_lunbo1);
        banner_data.add(R.drawable.fenquan_lunbo2);
    }

    private void systeminit() {

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MarqueeTextView marqueeTextView = findViewById(R.id.marquee);
                if(list1.size() > 0){
                    marqueeTextView.setText(list1.get(0).getText());
                }else if(list2.size() > 0){
                    marqueeTextView.setText(list2.get(0).getText());
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    systemService = new SystemService();
                    list1 = systemService.getList("分权",renyuan.getB());
                    list2 = systemService.getTongYongList("分权");
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
}
