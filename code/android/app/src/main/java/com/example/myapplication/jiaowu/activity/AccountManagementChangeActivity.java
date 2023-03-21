package com.example.myapplication.jiaowu.activity;

import android.accounts.Account;
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
import com.example.myapplication.finance.activity.ZhangHaoGuanLiChangeActivity;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceUserService;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.AccountManagementService;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.List;

public class AccountManagementChangeActivity extends AppCompatActivity {
    private AccountManagement accountManagement;
    private AccountManagementService accountManagementService;

    private EditText Username;
    private EditText Password;
    private EditText Realname;
    private EditText UseType;
    private EditText Age;
    private EditText Phone;
    private EditText Home;
    private EditText photo;
    private EditText Education;
    private EditText state;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountmanagement_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        accountManagement = myApplication.getAccountManagement();
        accountManagementService = new AccountManagementService();

        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        Realname = findViewById(R.id.Realname);
        UseType = findViewById(R.id.UseType);
        Age = findViewById(R.id.Age);
        Phone = findViewById(R.id.Phone);
        Home = findViewById(R.id.Home);
        photo = findViewById(R.id.photo);
        Education = findViewById(R.id.Education);
        state = findViewById(R.id.state);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            accountManagement = new AccountManagement();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            accountManagement = (AccountManagement) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            Username.setText(accountManagement.getUserName());
            Password.setText(accountManagement.getPassword());
            Realname.setText(accountManagement.getRealName());
            UseType.setText(accountManagement.getUseType());
            Age.setText(accountManagement.getAge());
            Phone.setText(accountManagement.getPhone());
            Home.setText(accountManagement.getHome());
            photo.setText(accountManagement.getPhoto());
            Education.setText(accountManagement.getEducation());
            state.setText(accountManagement.getState());
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

    public void insertClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(AccountManagementChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(AccountManagementChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = accountManagementService.insert(accountManagement);
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
                    ToastUtil.show(AccountManagementChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(AccountManagementChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = accountManagementService.update(accountManagement);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {

        accountManagement.setUserName(Username.getText().toString());
        accountManagement.setPassword(Password.getText().toString());
        accountManagement.setRealName(Realname.getText().toString());
        accountManagement.setUseType(UseType.getText().toString());
        accountManagement.setAge(Age.getText().toString());
        accountManagement.setPhone(Phone.getText().toString());
        accountManagement.setHome(Home.getText().toString());
        accountManagement.setPhoto(photo.getText().toString());
        accountManagement.setEducation(Education.getText().toString());
        accountManagement.setState(state.getText().toString());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
