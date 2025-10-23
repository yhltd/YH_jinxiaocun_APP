package com.example.myapplication.mendian.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.entity.SystemBanner;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceQuanXianService;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.service.SystemService;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class GrzxActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;


    private Banner banner;
    private List<Integer> banner_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mendian_grzx);

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();

        // 设置欢迎文本
        setWelcomeText();

        String username = yhMendianUser.getAccount();
        String company = yhMendianUser.getCompany();
        String Bianhao = yhMendianUser.getPosition(); // 如果有角色字段
        String Yuangongname = yhMendianUser.getUname();
        // 在界面显示用户信息
        TextView tvUsername = findViewById(R.id.zhmc);
        TextView tvCompany = findViewById(R.id.ssgs);
        TextView tvYonghuming = findViewById(R.id.yonghuming);
        TextView tvBianhao = findViewById(R.id.ygsf);
        TextView tvYuangongname = findViewById(R.id.ygmc);

        tvUsername.setText(username);
        tvCompany.setText(company);
        tvBianhao.setText(Bianhao);
        tvYonghuming.setText(username);
        tvYuangongname.setText(Yuangongname);

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



    }

    /**
     * 设置欢迎文本：欢迎使用 + 公司名前4位 + 排产系统
     */
    private void setWelcomeText() {
        TextView welcomeText = findViewById(R.id.welcome_text); // 需要给TextView添加id

        // 从缓存读取公司名称
        SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
        String companyName = sharedPref.getString("companyName", "");

        String welcomeMessage;
        if (companyName != null && !companyName.isEmpty()) {
            // 截取公司名前4位，如果公司名称长度不足4位，则使用全称
            String shortCompanyName = companyName.length() >= 4 ?
                    companyName.substring(0, 4) : companyName;
            welcomeMessage = "欢迎使用" + shortCompanyName + "门店系统";
        } else {
            // 如果缓存中没有公司名称，使用默认文本
            welcomeMessage = "欢迎使用云合未来门店系统";
        }

        welcomeText.setText(welcomeMessage);
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
        banner_data.add(R.drawable.lunbo1);
        banner_data.add(R.drawable.lunbo2);
        banner_data.add(R.drawable.lunbo3);
    }










}