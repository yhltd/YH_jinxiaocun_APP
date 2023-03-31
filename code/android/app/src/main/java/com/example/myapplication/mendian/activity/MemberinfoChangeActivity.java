package com.example.myapplication.mendian.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianMemberinfoService;
import com.example.myapplication.mendian.service.YhMendianUserService;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.List;

public class MemberinfoChangeActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;
    private YhMendianUserService yhMendianUserService;

    private YhMendianMemberinfo yhMendianMemberinfo;
    private YhMendianMemberinfoService yhMendianMemberinfoService;

    private EditText username;
    private EditText password;
    private EditText name;
    private EditText gender;
    private EditText state;
    private EditText phone;
    private EditText birthday;
    private EditText points;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberinfo_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianUserService = new YhMendianUserService();
        yhMendianMemberinfo = new YhMendianMemberinfo();
        yhMendianMemberinfoService = new YhMendianMemberinfoService();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        state = findViewById(R.id.state);
        phone = findViewById(R.id.phone);
        birthday = findViewById(R.id.birthday);
        points = findViewById(R.id.points);


        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhMendianMemberinfo = new YhMendianMemberinfo();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }else if(id == R.id.update_btn) {
            yhMendianMemberinfo = (YhMendianMemberinfo) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            username.setText(yhMendianMemberinfo.getUsername());
            password.setText(yhMendianMemberinfo.getPassword());
            name.setText(yhMendianMemberinfo.getName());
            gender.setText(yhMendianMemberinfo.getGender());
            state.setText(yhMendianMemberinfo.getState());
            phone.setText(yhMendianMemberinfo.getPhone());
            birthday.setText(yhMendianMemberinfo.getBirthday());
            points.setText(yhMendianMemberinfo.getPoints());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(MemberinfoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(MemberinfoChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianMemberinfoService.updateByMember(yhMendianMemberinfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(MemberinfoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(MemberinfoChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianMemberinfoService.insertByMember(yhMendianMemberinfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {

        if (username.getText().toString().equals("")) {
            ToastUtil.show(MemberinfoChangeActivity.this, "请输入账号");
            return false;
        } else {
            yhMendianMemberinfo.setUsername(username.getText().toString());
        }

        if (password.getText().toString().equals("")) {
            ToastUtil.show(MemberinfoChangeActivity.this, "请输入密码");
            return false;
        } else {
            yhMendianMemberinfo.setPassword(password.getText().toString());
        }
        if (name.getText().toString().equals("")) {
            ToastUtil.show(MemberinfoChangeActivity.this, "请输入姓名");
            return false;
        } else {
            yhMendianMemberinfo.setName(name.getText().toString());
        }
        if (gender.getText().toString().equals("")) {
            ToastUtil.show(MemberinfoChangeActivity.this, "请输入性别");
            return false;
        } else {
            yhMendianMemberinfo.setGender(gender.getText().toString());
        }
        if (state.getText().toString().equals("")) {
            ToastUtil.show(MemberinfoChangeActivity.this, "请输入账号状态");
            return false;
        } else {
            yhMendianMemberinfo.setState(state.getText().toString());
        }
        if (phone.getText().toString().equals("")) {
            ToastUtil.show(MemberinfoChangeActivity.this, "请输入电话");
            return false;
        } else {
            yhMendianMemberinfo.setPhone(phone.getText().toString());
        }

        if (birthday.getText().toString().equals("")) {
            ToastUtil.show(MemberinfoChangeActivity.this, "请输入生日");
            return false;
        } else {
            yhMendianMemberinfo.setBirthday(birthday.getText().toString());
        }

        yhMendianMemberinfo.setPoints(points.getText().toString());

        yhMendianMemberinfo.setCompany(yhMendianUser.getCompany());

        return true;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
