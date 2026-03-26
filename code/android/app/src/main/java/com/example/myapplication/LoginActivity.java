package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.myapplication.entity.SoftTime;
import com.example.myapplication.fenquan.activity.FenquanActivity;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.jiaowu.activity.JiaowuActivity;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.TeacherService;
import com.example.myapplication.jxc.activity.JxcActivity;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.service.DatabaseSpaceService;
import com.example.myapplication.service.FenquanDatabaseSpaceService;
import com.example.myapplication.service.FinanceDatabaseSpaceService;
import com.example.myapplication.service.JiaowuDatabaseSpaceService;
import com.example.myapplication.service.JxcDatabaseSpaceService;
import com.example.myapplication.service.MendianDatabaseSpaceService;
import com.example.myapplication.service.PushNewsService;
import com.example.myapplication.jxc.service.YhJinXiaoCunUserService;
import com.example.myapplication.mendian.activity.MendianActivity;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianUserService;
import com.example.myapplication.renshi.activity.RenShiActivity;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiUserService;
import com.example.myapplication.scheduling.activity.SchedulingActivity;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.UserInfoService;
import com.example.myapplication.service.RenShiDatabaseSpaceService;
import com.example.myapplication.service.SystemService;
import com.example.myapplication.utils.InputUtil;
import com.example.myapplication.utils.ToastUtil;
import com.example.myapplication.finance.activity.FinanceActivity;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceUserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private YhJinXiaoCunUserService yhJinXiaoCunUserService;
    private YhFinanceUserService yhFinanceUserService;
    private YhRenShiUserService yhRenShiUserService;
    private UserInfoService userInfoService;
    private RenyuanService renyuanService;
    private TeacherService teacherService;
    private YhMendianUserService yhMendianUserService;
    private SystemService systemService;
    private PushNewsService pushNewsService;

    private Spinner system;
    private Spinner company;
    private Button signBtn;
    private EditText username;
    private EditText password;
    private String companyText;
    private String systemText;

    private SharedPreferences loginPreference;
    private CheckBox remember;
    List<SoftTime> softTimeList;

    private TextView welcomeText;
    private ObjectAnimator colorAnimator;
    private ObjectAnimator breathAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.login);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


        pushNewsService = new com.example.myapplication.service.PushNewsService(this);
        loadPushNewsData();


        systemService = new SystemService();
        //初始化控件
        system = findViewById(R.id.system);
        signBtn = findViewById(R.id.sign_in);
        company = findViewById(R.id.company);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        remember = findViewById(R.id.remember);
        ImageView logoImage = findViewById(R.id.logoImage);
        loginPreference = getSharedPreferences("login", MODE_PRIVATE);


        setupButtonEffects();
// 创建上下浮动的属性动画
        ObjectAnimator floatAnimation = ObjectAnimator.ofFloat(logoImage, "translationY", -10f, 10f);
        floatAnimation.setDuration(1500); // 动画时长1秒
        floatAnimation.setRepeatCount(ValueAnimator.INFINITE); // 无限重复
        floatAnimation.setRepeatMode(ValueAnimator.REVERSE); // 往返运动
        floatAnimation.start();


        initView();
        startColorAnimation();
        startRotatingMask();




        boolean cheched = loginPreference.getBoolean("checked", false);
        if (cheched) {
            Map<String, Object> m = readLogin();
            if (m != null) {
                username.setText((CharSequence) m.get("userName"));
                password.setText((CharSequence) m.get("password"));
                remember.setChecked(cheched);
            }
        }

        //建立数据源
        String[] systemArray = getResources().getStringArray(R.array.system);
        //建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, systemArray);
        system.setAdapter(adapter);

        system.setOnItemSelectedListener(new systemItemSelectedListener());
        company.setOnItemSelectedListener(new companyItemSelectedListener());

        signBtn.setOnClickListener(onSignClick());

        checkNeedPermissions();

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

    private void initView() {
        welcomeText = findViewById(R.id.welcome_text);
    }

    private void startColorAnimation() {
        // 定义颜色数组 - 可以自定义你喜欢的颜色
        int[] colors = {
                Color.WHITE,                    // 白色
                Color.parseColor("#3F51B5"),    // 蓝色
                Color.parseColor("#4CAF50"),    // 绿色
                Color.parseColor("#FF9800"),    // 橙色
                Color.WHITE                     // 回到白色
        };

        // 创建 ObjectAnimator
        colorAnimator = ObjectAnimator.ofInt(
                welcomeText,
                "textColor",
                colors
        );

        // 设置动画属性
        colorAnimator.setDuration(8000); // 4秒完成所有颜色变化
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE); // 无限循环
        colorAnimator.setRepeatMode(ValueAnimator.RESTART); // 重新开始

        // 设置颜色评估器，实现平滑过渡
        colorAnimator.setEvaluator(new ArgbEvaluator());

        // 启动动画
        colorAnimator.start();
    }


    private void setupButtonEffects() {
        Log.d("Debug", "setupButtonEffects() 方法被调用了");
        // 1. 呼吸效果
        setupBreathingEffect();




    }

    private void setupBreathingEffect() {
        // 创建 X 轴缩放动画
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(signBtn, "scaleX", 1f, 1.05f, 1f);
        scaleXAnimator.setDuration(5000);
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);

        // 创建 Y 轴缩放动画
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(signBtn, "scaleY", 1f, 1.05f, 1f);
        scaleYAnimator.setDuration(5000);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setRepeatMode(ValueAnimator.RESTART);

        // 同时启动两个动画
        scaleXAnimator.start();
        scaleYAnimator.start();
    }

    private void animateButtonClick(View view) {
        // 停止呼吸动画
        if (breathAnimator != null) {
            breathAnimator.cancel();
        }

        // 点击动画序列
        view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(80)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(80)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 重新开始呼吸动画
                                        if (breathAnimator != null) {
                                            breathAnimator.start();
                                        }
                                    }
                                })
                                .start();
                    }
                })
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在 Activity 销毁时停止动画，避免内存泄漏
        if (colorAnimator != null && colorAnimator.isRunning()) {
            colorAnimator.cancel();
        }
    }

    private void checkNeedPermissions() {
        //判断是否开启权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限返回的结果参数，在onRequestPermissionResult可以得知申请结果
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, 1);
        }
    }

    private class systemItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            signBtn.setEnabled(false);
            //获取选择的项的值
            systemText = system.getItemAtPosition(position).toString();
            Handler systemHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    if (msg.obj != null) {
                        company.setAdapter((SpinnerAdapter) msg.obj);
                    }
                    signBtn.setEnabled(true);
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (systemText.equals("云合未来进销存系统")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhJinXiaoCunUserService = new YhJinXiaoCunUserService();
                            List<String> list = yhJinXiaoCunUserService.getCompany();
                            List<String> list2 = yhJinXiaoCunUserService.getCompany2();

                            // 合并两个列表并去重
                            Set<String> combinedSet = new HashSet<>();
                            if (list != null) combinedSet.addAll(list);
                            if (list2 != null) combinedSet.addAll(list2);

                            // 转换回List并排序（可选）
                            List<String> combinedList = new ArrayList<>(combinedSet);
                            Collections.sort(combinedList); // 按字母顺序排序

                            adapter = new ArrayAdapter<String>(LoginActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, combinedList);

                            if (combinedList.size() > 0) {
                                msg.obj = adapter;
                            } else {
                                msg.obj = null;
                            }
                            systemHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (systemText.equals("云合未来财务系统")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceUserService = new YhFinanceUserService();
                            List<String> list = yhFinanceUserService.getCompany();
                            adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                            if (list.size() > 0) {
                                msg.obj = adapter;
                            } else {
                                msg.obj = null;
                            }
                            systemHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (systemText.equals("云合排产管理系统")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            userInfoService = new UserInfoService();
                            List<String> list = userInfoService.getCompany();
                            adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                            if (list.size() > 0) {
                                msg.obj = adapter;
                            } else {
                                msg.obj = null;
                            }
                            systemHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (systemText.equals("云合分权编辑系统")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            renyuanService = new RenyuanService();
                            List<String> list = renyuanService.getCompany();

                            // 添加调试日志
                            Log.d("LoginActivity", "分权系统公司列表: " + (list != null ? list.size() : "null"));
                            if (list != null) {
                                for (String comp : list) {
                                    Log.d("LoginActivity", "公司: " + comp);
                                }
                            }
                            adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                            if (list.size() > 0) {
                                msg.obj = adapter;
                            } else {
                                msg.obj = null;
                            }
                            systemHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (systemText.equals("云合人事管理系统")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhRenShiUserService = new YhRenShiUserService();
                            List<String> list = yhRenShiUserService.getCompany();
                            adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                            if (list.size() > 0) {
                                msg.obj = adapter;
                            } else {
                                msg.obj = null;
                            }
                            systemHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (systemText.equals("云合教务管理系统")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            teacherService = new TeacherService();
                            List<String> list = teacherService.getCompany();
                            List<String> list2 = teacherService.getCompany1();
                            // 合并两个列表并去重
                            Set<String> combinedSet = new HashSet<>();
                            if (list != null) combinedSet.addAll(list);
                            if (list2 != null) combinedSet.addAll(list2);

                            // 转换回List并排序（可选）
                            List<String> combinedList = new ArrayList<>(combinedSet);
                            Collections.sort(combinedList); // 按字母顺序排序

                            adapter = new ArrayAdapter<String>(LoginActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, combinedList);

                            if (combinedList.size() > 0) {
                                msg.obj = adapter;
                            } else {
                                msg.obj = null;
                            }
                            systemHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (systemText.equals("云合智慧门店收银系统")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhMendianUserService = new YhMendianUserService();
                            List<String> list = yhMendianUserService.getCompany();
                            adapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                            if (list.size() > 0) {
                                msg.obj = adapter;
                            } else {
                                msg.obj = null;
                            }
                            systemHandler.sendMessage(msg);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class companyItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            companyText = company.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    public View.OnClickListener onSignClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performQueryByCompanyAndAccount();
                // 缩放动画
                animateButtonClick(v);
                if (!checkForm()) return;

                InputUtil.hideInput(LoginActivity.this);

                getSupportActionBar().setTitle("正在登录...");
                signBtn.setEnabled(false);

                saveSystemAndCompanyToCache();


                SharedPreferences testPref = getSharedPreferences("my_cache", MODE_PRIVATE);
                String testSystem = testPref.getString("systemName", "");
                String testCompany = testPref.getString("companyName", "");
                String testAccount = testPref.getString("userAccount", "");
                String testPassword = testPref.getString("userPassword", "");

                Log.d("CacheTest", "验证保存 - 系统: " + testSystem +
                        ", 公司: " + testCompany +
                        ", 账号: " + testAccount +
                        ", 密码: " + testPassword);


                Handler signHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        signBtn.setEnabled(true);
                        boolean panduan = true;
                        if (msg.obj != null) {
                            String thisNum = "";

                            Log.d("LoginActivity", "softTimeList: " + Arrays.toString(softTimeList.toArray()));

                            if(softTimeList == null){
                                panduan = false;
                                ToastUtil.show(LoginActivity.this, "工具到期，请联系我公司续费");
                            }else{
                                if(softTimeList.get(0).getMark5() == null || !softTimeList.get(0).getMark5().trim().contains("APP安卓")){
                                    panduan = false;
                                    ToastUtil.show(LoginActivity.this, "您没有当前使用端权限，请联系我公司续费或者购买系统");
                                }

                                String mark4 = softTimeList.get(0).getMark4();
                                if (mark4 != null && !mark4.isEmpty()) {
                                    // 保存到 CacheManager
                                    CacheManager.getInstance().put("mark4", mark4);

                                    // 保存到 Application
                                    MyApplication application = (MyApplication) getApplicationContext();
                                    application.setMark4(mark4);

                                    Log.d("LoginActivity", "保存 mark4 值: " + mark4);
                                }


                                if (!softTimeList.get(0).getMark1().trim().equals("a8xd2s")){
                                    if(softTimeList.get(0).getEndtime().trim().equals("1")){
                                        panduan = false;
                                        ToastUtil.show(LoginActivity.this, "工具到期，请联系我公司续费");
                                    }else if(softTimeList.get(0).getMark2().trim().equals("1")){
                                        panduan = false;
                                        ToastUtil.show(LoginActivity.this, "服务器到期，请联系我公司续费");
                                    }
                                    thisNum = softTimeList.get(0).getMark3().trim();
                                    if(!thisNum.equals("")){
                                        thisNum = thisNum.split(":")[1];
                                        thisNum = thisNum.replace("(","");
                                        thisNum = thisNum.replace(")","");
                                    }
                                }
                            }
                            System.out.println(thisNum);
                            if(panduan){
                                if (systemText.equals("云合未来进销存系统")) {
                                    YhJinXiaoCunUser user = (YhJinXiaoCunUser) msg.obj;
                                    if (user.getBtype().equals("正常")) {
                                        configLoginInfo(remember.isChecked());
                                        MyApplication application = (MyApplication) getApplicationContext();
                                        application.setYhJinXiaoCunUser(user);
                                        application.setUserNum(thisNum);
                                        ToastUtil.show(LoginActivity.this, "登录成功");
                                        Intent intent = new Intent(LoginActivity.this, JxcActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ToastUtil.show(LoginActivity.this, "账号已被禁用");
                                    }
                                } else if (systemText.equals("云合未来财务系统")) {
                                    configLoginInfo(remember.isChecked());
                                    YhFinanceUser user = (YhFinanceUser) msg.obj;
                                    MyApplication application = (MyApplication) getApplicationContext();
                                    application.setYhFinanceUser(user);
                                    application.setUserNum(thisNum);
                                    ToastUtil.show(LoginActivity.this, "登录成功");
                                    Intent intent = new Intent(LoginActivity.this, FinanceActivity.class);
                                    startActivity(intent);
                                } else if (systemText.equals("云合排产管理系统")) {
                                    UserInfo user = (UserInfo) msg.obj;
                                    if (user.getState().equals("正常")) {
                                        configLoginInfo(remember.isChecked());
                                        MyApplication application = (MyApplication) getApplicationContext();
                                        application.setUserInfo(user);
                                        application.setUserNum(thisNum);
                                        ToastUtil.show(LoginActivity.this, "登录成功");
                                        Intent intent = new Intent(LoginActivity.this, SchedulingActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ToastUtil.show(LoginActivity.this, "账号已被禁用");
                                    }
                                } else if (systemText.equals("云合分权编辑系统")) {
                                    Renyuan renyuan = (Renyuan) msg.obj;
                                    if (renyuan.getZhuangtai().equals("正常")) {
                                        configLoginInfo(remember.isChecked());
                                        MyApplication application = (MyApplication) getApplicationContext();
                                        application.setRenyuan(renyuan);
                                        application.setUserNum(thisNum);
                                        ToastUtil.show(LoginActivity.this, "登录成功");
                                        Intent intent = new Intent(LoginActivity.this, FenquanActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ToastUtil.show(LoginActivity.this, "账号已被禁用");
                                    }
                                }else if (systemText.equals("云合人事管理系统")) {
                                    configLoginInfo(remember.isChecked());
                                    YhRenShiUser user = (YhRenShiUser) msg.obj;
                                    MyApplication application = (MyApplication) getApplicationContext();
                                    application.setYhRenShiUser(user);
                                    application.setUserNum(thisNum);
                                    ToastUtil.show(LoginActivity.this, "登录成功");
                                    Intent intent = new Intent(LoginActivity.this, RenShiActivity.class);
                                    startActivity(intent);
                                }else if (systemText.equals("云合教务管理系统")) {
                                    Teacher user = (Teacher) msg.obj;
                                    if (user.getState().equals("正常")) {
                                        configLoginInfo(remember.isChecked());
                                        MyApplication application = (MyApplication) getApplicationContext();
                                        application.setTeacher(user);
                                        application.setUserNum(thisNum);
                                        ToastUtil.show(LoginActivity.this, "登录成功");
                                        Intent intent = new Intent(LoginActivity.this, JiaowuActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ToastUtil.show(LoginActivity.this, "账号已被禁用");
                                    }
                                }else if (systemText.equals("云合智慧门店收银系统")) {
                                    configLoginInfo(remember.isChecked());
                                    YhMendianUser user = (YhMendianUser) msg.obj;
                                    MyApplication application = (MyApplication) getApplicationContext();
                                    application.setYhMendianUser(user);
                                    application.setUserNum(thisNum);
                                    ToastUtil.show(LoginActivity.this, "登录成功");
                                    Intent intent = new Intent(LoginActivity.this, MendianActivity.class);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            ToastUtil.show(LoginActivity.this, "用户名密码错误");
                        }
                        getSupportActionBar().setTitle("云合未来一体化系统");
                        return true;
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        String formattedDate = formatter.format(date);
                        if (systemText.equals("云合未来进销存系统")) {
                            Message msg = new Message();
                            try {
                                yhJinXiaoCunUserService = new YhJinXiaoCunUserService();
                                softTimeList = systemService.getSoftTime(formattedDate,companyText,"进销存");

                                // 🆕 获取进销存系统数据库空间大小
                                JxcDatabaseSpaceService spaceService = new JxcDatabaseSpaceService();
                                double dbSizeKB = spaceService.getDatabaseSizeByCompany(companyText);

                                // 🆕 保存到 SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putFloat("dbSizeKB", (float) dbSizeKB);
                                editor.apply();

                                Log.d("SpaceCheck", "进销存系统 - 公司 " + companyText + " 数据库大小: " + dbSizeKB + " KB (" + (dbSizeKB / 1024) + " MB)");

                                msg.obj = yhJinXiaoCunUserService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        } else if (systemText.equals("云合未来财务系统")) {
                            Message msg = new Message();
                            try {
                                yhFinanceUserService = new YhFinanceUserService();
                                softTimeList = systemService.getSoftTime(formattedDate,companyText,"财务");

                                // 🆕 获取财务系统数据库空间大小
                                FinanceDatabaseSpaceService spaceService = new FinanceDatabaseSpaceService();
                                double dbSizeKB = spaceService.getDatabaseSizeByCompany(companyText);

                                // 🆕 保存到 SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putFloat("dbSizeKB", (float) dbSizeKB);
                                editor.apply();

                                Log.d("SpaceCheck", "财务系统 - 公司 " + companyText + " 数据库大小: " + dbSizeKB + " KB (" + (dbSizeKB / 1024) + " MB)");

                                msg.obj = yhFinanceUserService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        } else if (systemText.equals("云合排产管理系统")) {
                            Message msg = new Message();
                            try {
                                userInfoService = new UserInfoService();
                                softTimeList = systemService.getSoftTime(formattedDate,companyText,"排产");

                                // 🆕 获取数据库空间大小
                                DatabaseSpaceService spaceService = new DatabaseSpaceService();
                                double dbSizeKB = spaceService.getDatabaseSizeByCompany(companyText);

                                // 🆕 保存到 SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putFloat("dbSizeKB", (float) dbSizeKB);
                                editor.apply();

                                Log.d("SpaceCheck", "公司 " + companyText + " 数据库大小: " + dbSizeKB + " KB (" + (dbSizeKB / 1024) + " MB)");

                                msg.obj = userInfoService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        } else if (systemText.equals("云合分权编辑系统")) {
                            Message msg = new Message();
                            try {
                                renyuanService = new RenyuanService();
                                softTimeList = systemService.getSoftTime(formattedDate,companyText,"分权");

                                // 🆕 获取分权系统数据库空间大小
                                FenquanDatabaseSpaceService spaceService = new FenquanDatabaseSpaceService();
                                double dbSizeKB = spaceService.getDatabaseSizeByCompany(companyText);

                                // 🆕 保存到 SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putFloat("dbSizeKB", (float) dbSizeKB);
                                editor.apply();

                                Log.d("SpaceCheck", "分权系统 - 公司 " + companyText + " 数据库大小: " + dbSizeKB + " KB (" + (dbSizeKB / 1024) + " MB)");

                                msg.obj = renyuanService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        } else if (systemText.equals("云合人事管理系统")) {
                            Message msg = new Message();
                            try {
                                yhRenShiUserService = new YhRenShiUserService();
                                softTimeList = systemService.getSoftTime(formattedDate,companyText,"人事");

                                // 🆕 获取人事系统数据库空间大小
                                RenShiDatabaseSpaceService spaceService = new RenShiDatabaseSpaceService();
                                double dbSizeKB = spaceService.getDatabaseSizeByCompany(companyText);

                                // 🆕 保存到 SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putFloat("dbSizeKB", (float) dbSizeKB);
                                editor.apply();

                                Log.d("SpaceCheck", "人事系统 - 公司 " + companyText + " 数据库大小: " + dbSizeKB + " KB (" + (dbSizeKB / 1024) + " MB)");

                                msg.obj = yhRenShiUserService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        }else if (systemText.equals("云合教务管理系统")) {
                            Message msg = new Message();
                            try {
                                teacherService = new TeacherService();
                                softTimeList = systemService.getSoftTime(formattedDate,companyText,"教务");


                                // 🆕 获取教务系统数据库空间大小
                                JiaowuDatabaseSpaceService spaceService = new JiaowuDatabaseSpaceService();
                                double dbSizeKB = spaceService.getDatabaseSizeByCompany(companyText);

                                // 🆕 保存到 SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putFloat("dbSizeKB", (float) dbSizeKB);
                                editor.apply();

                                Log.d("SpaceCheck", "教务系统 - 公司 " + companyText + " 数据库大小: " + dbSizeKB + " KB (" + (dbSizeKB / 1024) + " MB)");

                                msg.obj = teacherService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        }else if (systemText.equals("云合智慧门店收银系统")) {
                            Message msg = new Message();
                            try {
                                yhMendianUserService = new YhMendianUserService();
                                softTimeList = systemService.getSoftTime(formattedDate,companyText,"门店");

                                // 🆕 获取门店系统数据库空间大小
                                MendianDatabaseSpaceService spaceService = new MendianDatabaseSpaceService();
                                double dbSizeKB = spaceService.getDatabaseSizeByCompany(companyText);

                                // 🆕 保存到 SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putFloat("dbSizeKB", (float) dbSizeKB);
                                editor.apply();

                                Log.d("SpaceCheck", "门店系统 - 公司 " + companyText + " 数据库大小: " + dbSizeKB + " KB (" + (dbSizeKB / 1024) + " MB)");

                                msg.obj = yhMendianUserService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        };
    }


    /**
     * 根据公司名称、账号、密码执行查询
     */
    private void performQueryByCompanyAndAccount() {
        String companyName = companyText;
        String account = username.getText().toString();
        String pwd = password.getText().toString();

        Log.d("QueryDebug", "开始查询 - 公司: " + companyName + ", 账号: " + account + ", 密码: " + pwd);

        // 根据不同的系统类型执行不同的查询
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Object result = null;

                    if (systemText.equals("云合未来进销存系统")) {
                        yhJinXiaoCunUserService = new YhJinXiaoCunUserService();
                        result = yhJinXiaoCunUserService.queryByCompanyAndAccount(companyName, account, pwd);
                    }

                    if (systemText.equals("云合教务管理系统")) {
                        teacherService = new TeacherService();
                        result = teacherService.queryByCompanyAndAccount(companyName, account, pwd);
                    }

                    // 🆕 使用缓存管理器存储 shujuku 字段和查询结果
                    int shujukuValue = (result != null) ? 1 : 0;
                    CacheManager.getInstance().setShujukuValue(shujukuValue);
                    CacheManager.getInstance().setQueryResult(result);

                    // 🆕 可选：保持原有的 SharedPreferences 存储（用于持久化）
                    SharedPreferences sharedPref = getSharedPreferences("my_cache", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("shujuku", shujukuValue);
                    editor.apply();

                    Log.d("QueryDebug", "存储 shujuku 值: " + shujukuValue);

                    // 处理查询结果
                    final Object finalResult = result;
                    final int finalShujukuValue = shujukuValue;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalResult != null) {
                                Log.d("QueryDebug", "查询成功: " + finalResult.toString());
                            } else {
                                Log.d("QueryDebug", "查询结果为空");
                            }

                            // 🆕 验证缓存的值
                            int cachedShujuku = CacheManager.getInstance().getShujukuValue();
                            Object cachedResult = CacheManager.getInstance().getQueryResult();

                            Log.d("QueryDebug", "缓存设置完成，时间: " + System.currentTimeMillis());
                            Log.d("CacheTest", "缓存验证 - shujuku: " + cachedShujuku +
                                    ", 结果: " + (cachedResult != null ? "非空" : "空"));
                        }
                    });

                } catch (Exception e) {
                    // 🆕 查询异常时也存储 shujuku 为 0
                    CacheManager.getInstance().setShujukuValue(0);
                    CacheManager.getInstance().setQueryResult(null);

                    // 可选：保持原有的 SharedPreferences 存储
                    SharedPreferences sharedPref = getSharedPreferences("my_cache", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("shujuku", 0);
                    editor.apply();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(LoginActivity.this, "查询失败: " + e.getMessage());

                            // 🆕 验证异常情况下的缓存
                            int cachedShujuku = CacheManager.getInstance().getShujukuValue();
                            Log.d("CacheTest", "异常情况 - shujuku: " + cachedShujuku);
                        }
                    });
                }
            }
        }).start();
    }

    public Map<String, Object> readLogin() {
        Map<String, Object> m = new HashMap<>();
        String userName = loginPreference.getString("userName", "");
        String password = loginPreference.getString("password", "");
        m.put("userName", userName);
        m.put("password", password);
        return m;
    }

    public void configLoginInfo(boolean checked) {
        SharedPreferences.Editor editor = loginPreference.edit();
        editor.putBoolean("checked", remember.isChecked());
        if (checked) {
            editor.putString("userName", username.getText().toString());
            editor.putString("password", password.getText().toString());
        } else {
            editor.remove("userName").remove("password");
        }
        editor.commit();
    }

    public boolean checkForm() {
        if (companyText == null || companyText.equals("")) {
            ToastUtil.show(LoginActivity.this, "请选择公司");
            return false;
        }

        if (username.getText().toString().equals("")) {
            ToastUtil.show(LoginActivity.this, "请输入用户名");
            return false;
        }

        if (password.getText().toString().equals("")) {
            ToastUtil.show(LoginActivity.this, "请输入密码");
            return false;
        }
        return true;
    }

    private void saveSystemAndCompanyToCache() {
        try {
            SharedPreferences sharedPref = getSharedPreferences("my_cache", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            // 保存系统名称
            if (systemText != null && !systemText.isEmpty()) {
                editor.putString("systemName", systemText);
            }

            // 保存公司名称
            if (companyText != null && !companyText.isEmpty()) {
                editor.putString("companyName", companyText);
            }

            // 🆕 新增：保存账号密码
            if (username.getText() != null && !username.getText().toString().isEmpty()) {
                editor.putString("userAccount", username.getText().toString());
            }

            if (password.getText() != null && !password.getText().toString().isEmpty()) {
                editor.putString("userPassword", password.getText().toString());
            }

            // 保存数据版本和时间戳
            editor.putInt("dataVersion", 1);
            editor.putLong("saveTime", System.currentTimeMillis());

            editor.apply();

            Log.d("Cache", "保存成功 - 系统: " + systemText + ", 公司: " + companyText +
                    ", 账号: " + username.getText().toString() + ", 密码: " + password.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Cache", "保存缓存失败: " + e.getMessage());
        }
    }


    private void loadPushNewsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<com.example.myapplication.entity.PushNews> result = pushNewsService.getList();

                    if (result != null && !result.isEmpty()) {
                        com.example.myapplication.entity.PushNews news = result.get(0);

                        // 处理 beizhu3 - 更新欢迎文本
                        if (news.getBeizhu3() != null && !news.getBeizhu3().trim().isEmpty()) {
                            final String beizhu3Value = news.getBeizhu3();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView welcomeText = findViewById(R.id.welcome_text);
                                    if (welcomeText != null) {
                                        welcomeText.setText(beizhu3Value);
                                    }
                                }
                            });

                            Log.d("PushNewsDebug", "beizhu3内容已设置: " + beizhu3Value);
                        }

                        // 处理 beizhu2 - 更新 logo 图片
                        if (news.getBeizhu2() != null && !news.getBeizhu2().trim().isEmpty()) {
                            final String beizhu2Value = news.getBeizhu2();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateLogoWithBase64(beizhu2Value);
                                }
                            });

                            Log.d("PushNewsDebug", "beizhu2内容已获取，准备设置logo");
                        }

                    }

                } catch (Exception e) {
                    Log.e("PushNewsDebug", "查询异常: " + e.getMessage(), e);
                }
            }
        }).start();
    }

    /**
     * 使用 base64 字符串更新 logo 图片
     */
    private void updateLogoWithBase64(String base64String) {
        ImageView logoImage = findViewById(R.id.logoImage);
        if (logoImage == null) {
            Log.e("PushNewsDebug", "未找到 logoImage ImageView");
            return;
        }

        try {
            // 🆕 关键：清除所有之前的图片设置
            logoImage.setImageDrawable(null);
            logoImage.setBackgroundResource(0);

            String cleanBase64 = base64String.trim().replaceAll("\\s", "");
            byte[] decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap != null) {
                logoImage.setImageBitmap(bitmap);
                Log.d("PushNewsDebug", "logo图片更新成功");
            } else {
                throw new Exception("Bitmap解码为null");
            }

        } catch (Exception e) {
            Log.e("PushNewsDebug", "设置logo失败: " + e.getMessage());
            // 🆕 失败时完全重置为默认图片
            logoImage.setImageDrawable(null);
            logoImage.setBackgroundResource(R.drawable.companylogo);
        }
    }

}