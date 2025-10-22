package com.example.myapplication.finance.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.entity.SystemBanner;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceQuanXianService;
import com.example.myapplication.service.SystemService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.MarqueeTextView;
import com.example.myapplication.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrzxActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private SystemService systemService;
    private YhFinanceQuanXianService yhFinanceQuanXianService;
    private List<SystemBanner> list1;
    private List<SystemBanner> list2;
    private List<YhFinanceQuanXian> list;
    private boolean pd;

    private Banner banner;
    private List<Integer> banner_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caiwu_grzx);

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();

            String username = yhFinanceUser.getName();
            String company = yhFinanceUser.getCompany();
            String Bianhao = yhFinanceUser.getBianhao(); // 如果有角色字段

            // 在界面显示用户信息
            TextView tvUsername = findViewById(R.id.zhmc);
            TextView tvCompany = findViewById(R.id.ssgs);
            TextView tvYonghuming = findViewById(R.id.yonghuming);
            TextView tvBianhao = findViewById(R.id.ygsf);

            tvUsername.setText(username);
            tvCompany.setText(company);
            tvBianhao.setText(Bianhao);
            tvYonghuming.setText(username);

        initData();
        banner = findViewById(R.id.main_banner);

        banner.setAdapter(new BannerImageAdapter<Integer>(banner_data) {

            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
            }
        });
        startRotatingMask();
        // 开启循环轮播
        banner.isAutoLoop(true);
        banner.setIndicator(new CircleIndicator(this));
        banner.setScrollBarFadeDuration(1000);
        // 设置指示器颜色(TODO 即选中时那个小点的颜色)
        banner.setIndicatorSelectedColor(Color.GREEN);
        // 开始轮播
        banner.start();

        // 绑定退出按钮 - 直接退出，不需要确认
        View exitButton = findViewById(R.id.tuichu);
        exitButton.setOnClickListener(v -> {
            exitToLogin();
        });


        LinearLayout zhanghaoguanli = findViewById(R.id.zhanghaoguanli);
        zhanghaoguanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(GrzxActivity.this, ZhangHaoGuanLiActivity.class);
                    startActivity(intent);

            }
        });
        LinearLayout bumenshezhi = findViewById(R.id.bumenshezhi);
        bumenshezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(GrzxActivity.this, DepartmentActivity.class);
                    startActivity(intent);

            }
        });



    }
    private void exitToLogin() {
        // 跳转到登录页面
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void startRotatingMask() {
        View rotatingMask = findViewById(R.id.rotating_mask);

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(
                rotatingMask,
                "rotation",
                0f, 360f
        );

        rotationAnimator.setDuration(6000);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.start();
    }

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.finance_banner_01);
        banner_data.add(R.drawable.caiwu_lunbo1);
        banner_data.add(R.drawable.caiwu_lunbo2);
    }








}
