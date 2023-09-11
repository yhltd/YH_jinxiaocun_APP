package com.example.myapplication.scheduling.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.scheduling.service.UserInfoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class UserChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private UserInfo newUserInfo;
    private UserInfoService userInfoService;
    private DepartmentService departmentService;


    private EditText user_code;
    private EditText password;
    private Spinner department;
    private Spinner state;

    private List<String> departmentList;
    private List<String> stateList;

    private String department_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pc_user_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        stateList = new ArrayList<>();
        stateList.add("正常");
        stateList.add("禁用");

        userInfoService = new UserInfoService();
        departmentService = new DepartmentService();

        user_code = findViewById(R.id.user_code);
        password = findViewById(R.id.password);
        department = findViewById(R.id.department);
        state = findViewById(R.id.state);

        SpinnerAdapter adapter = new ArrayAdapter<String>(UserChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, stateList);
        state.setAdapter(adapter);

        initList();

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            newUserInfo = new UserInfo();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            newUserInfo = (UserInfo) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            user_code.setText(newUserInfo.getUser_code());
            password.setText(newUserInfo.getPassword());
            if (newUserInfo.getState().equals("正常")) {
                state.setSelection(0);
            } else {
                state.setSelection(1);
            }
            department.setSelection(getDepartmentPosition(newUserInfo.getDepartment_name()));
        }

        department.setOnItemSelectedListener(new departmentSelectedListener());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearClick(View v) {
        user_code.setText("");
        password.setText("");
    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    department.setAdapter((SpinnerAdapter) msg.obj);
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                SpinnerAdapter adapter = null;
                try {
                    departmentService = new DepartmentService();
                    departmentList = new ArrayList<>();
                    departmentList = departmentService.getDepartment(userInfo.getCompany());
                    adapter = new ArrayAdapter<String>(UserChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, departmentList);
                    if (departmentList.size() > 0) {
                        msg.obj = adapter;
                    } else {
                        msg.obj = null;
                    }
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void insertClick(View v) {
        if (!checkForm()) return;
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(UserChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(UserChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = userInfoService.insert(newUserInfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(UserChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(UserChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = userInfoService.update(newUserInfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (user_code.getText().toString().equals("")) {
            ToastUtil.show(UserChangeActivity.this, "请输入账号");
            return false;
        } else {
            newUserInfo.setUser_code(user_code.getText().toString());
        }

        if (password.getText().toString().equals("")) {
            ToastUtil.show(UserChangeActivity.this, "请输入密码");
            return false;
        } else {
            newUserInfo.setPassword(password.getText().toString());
        }
        newUserInfo.setDepartment_name(department.getSelectedItem().toString());
        newUserInfo.setState(state.getSelectedItem().toString());
        newUserInfo.setCompany(userInfo.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private int getDepartmentPosition(String param) {
        if (departmentList != null) {
            for (int i = 0; i < departmentList.size(); i++) {
                if (param.equals(departmentList.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private class departmentSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            department_text = departmentList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }



}
