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
import com.example.myapplication.mendian.entity.YhMendianMemberlevel;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.entity.YhMendianUsers;
import com.example.myapplication.mendian.service.YhMendianMemberlevelService;
import com.example.myapplication.mendian.service.YhMendianUserService;
import com.example.myapplication.mendian.service.YhMendianUsersService;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.List;

public class UsersChangeActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;
    private YhMendianUsers yhMendianUsers;
    private YhMendianUsersService yhMendianUsersService;

    private EditText uname;
    private EditText position;
    private EditText account;
    private EditText password;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianUsersService = new YhMendianUsersService();

        uname = findViewById(R.id.uname);
        position = findViewById(R.id.position);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhMendianUsers = new YhMendianUsers();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }else if(id == R.id.update_btn) {
            yhMendianUsers = (YhMendianUsers) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            uname.setText(yhMendianUsers.getUname());
            position.setText(yhMendianUsers.getPosition());
            account.setText(yhMendianUsers.getAccount());
            password.setText(yhMendianUsers.getPassword());
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
                    ToastUtil.show(UsersChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(UsersChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianUsersService.updateByUsers(yhMendianUsers);
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
                    ToastUtil.show(UsersChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(UsersChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianUsersService.insertByUsers(yhMendianUsers);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {

        if (uname.getText().toString().equals("")) {
            ToastUtil.show(UsersChangeActivity.this, "请输入姓名");
            return false;
        } else {
            yhMendianUsers.setUname(uname.getText().toString());
        }

        if (position.getText().toString().equals("")) {
            ToastUtil.show(UsersChangeActivity.this, "请输入岗位");
            return false;
        } else {
            yhMendianUsers.setPosition(position.getText().toString());
        }

        if (account.getText().toString().equals("")) {
            ToastUtil.show(UsersChangeActivity.this, "请输入账号");
            return false;
        } else {
            yhMendianUsers.setAccount(account.getText().toString());
        }

        if (password.getText().toString().equals("")) {
            ToastUtil.show(UsersChangeActivity.this, "请输入密码");
            return false;
        } else {
            yhMendianUsers.setPassword(password.getText().toString());
        }

        yhMendianUsers.setCompany(yhMendianUser.getCompany());

        return true;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
