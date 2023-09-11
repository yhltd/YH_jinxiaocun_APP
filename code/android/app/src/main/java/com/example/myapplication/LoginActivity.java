package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.myapplication.entity.SoftTime;
import com.example.myapplication.fenquan.activity.FenquanActivity;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.jiaowu.activity.JiaowuActivity;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.TeacherService;
import com.example.myapplication.jxc.activity.JxcActivity;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
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
import com.example.myapplication.service.SystemService;
import com.example.myapplication.utils.InputUtil;
import com.example.myapplication.utils.ToastUtil;
import com.example.myapplication.finance.activity.FinanceActivity;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceUserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private YhJinXiaoCunUserService yhJinXiaoCunUserService;
    private YhFinanceUserService yhFinanceUserService;
    private YhRenShiUserService yhRenShiUserService;
    private UserInfoService userInfoService;
    private RenyuanService renyuanService;
    private TeacherService teacherService;
    private YhMendianUserService yhMendianUserService;
    private SystemService systemService;

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
        systemService = new SystemService();
        //初始化控件
        system = findViewById(R.id.system);
        signBtn = findViewById(R.id.sign_in);
        company = findViewById(R.id.company);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        remember = findViewById(R.id.remember);

        loginPreference = getSharedPreferences("login", MODE_PRIVATE);

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
                if (!checkForm()) return;

                InputUtil.hideInput(LoginActivity.this);

                getSupportActionBar().setTitle("正在登录...");
                signBtn.setEnabled(false);

                Handler signHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        signBtn.setEnabled(true);
                        boolean panduan = true;
                        if (msg.obj != null) {
                            String thisNum = "";
                            if(softTimeList == null){
                                panduan = false;
                                ToastUtil.show(LoginActivity.this, "工具到期，请联系我公司续费");
                            }else{
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

}