package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.myapplication.jxc.activity.JxcActivity;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunUserService;
import com.example.myapplication.scheduling.activity.SchedulingActivity;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.UserInfoService;
import com.example.myapplication.utils.InputUtil;
import com.example.myapplication.utils.ToastUtil;
import com.example.myapplication.finance.activity.FinanceActivity;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceUserService;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private YhJinXiaoCunUserService yhJinXiaoCunUserService;
    private YhFinanceUserService yhFinanceUserService;
    private UserInfoService userInfoService;
    private Spinner system;
    private Spinner company;
    private Button signBtn;
    private EditText username;
    private EditText password;
    private String companyText;
    private String systemText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //初始化控件
        system = findViewById(R.id.system);
        signBtn = findViewById(R.id.sign_in);
        company = findViewById(R.id.company);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

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
                        if (msg.obj != null) {
                            if (systemText.equals("云合未来进销存系统")) {
                                YhJinXiaoCunUser user = (YhJinXiaoCunUser) msg.obj;
                                if (user.getBtype().equals("正常")) {
                                    MyApplication application = (MyApplication) getApplicationContext();
                                    application.setYhJinXiaoCunUser(user);
                                    ToastUtil.show(LoginActivity.this, "登录成功");
                                    Intent intent = new Intent(LoginActivity.this, JxcActivity.class);
                                    startActivity(intent);
                                } else {
                                    ToastUtil.show(LoginActivity.this, "账号已被禁用");
                                }
                            } else if (systemText.equals("云合未来财务系统")) {
                                YhFinanceUser user = (YhFinanceUser) msg.obj;
                                MyApplication application = (MyApplication) getApplicationContext();
                                application.setYhFinanceUser(user);
                                ToastUtil.show(LoginActivity.this, "登录成功");
                                Intent intent = new Intent(LoginActivity.this, FinanceActivity.class);
                                startActivity(intent);
                            } else if (systemText.equals("云合排产管理系统")) {
                                UserInfo user = (UserInfo) msg.obj;
                                if (user.getState().equals("正常")) {
                                    MyApplication application = (MyApplication) getApplicationContext();
                                    application.setUserInfo(user);
                                    ToastUtil.show(LoginActivity.this, "登录成功");
                                    Intent intent = new Intent(LoginActivity.this, SchedulingActivity.class);
                                    startActivity(intent);
                                } else {
                                    ToastUtil.show(LoginActivity.this, "账号已被禁用");
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
                        if (systemText.equals("云合未来进销存系统")) {
                            Message msg = new Message();
                            try {
                                yhJinXiaoCunUserService = new YhJinXiaoCunUserService();
                                msg.obj = yhJinXiaoCunUserService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        } else if (systemText.equals("云合未来财务系统")) {
                            Message msg = new Message();
                            try {
                                yhFinanceUserService = new YhFinanceUserService();
                                msg.obj = yhFinanceUserService.login(username.getText().toString(), password.getText().toString(), companyText);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            signHandler.sendMessage(msg);
                        } else if (systemText.equals("云合排产管理系统")) {
                            Message msg = new Message();
                            try {
                                userInfoService = new UserInfoService();
                                msg.obj = userInfoService.login(username.getText().toString(), password.getText().toString(), companyText);
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