package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceFaPiao;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceFaPiaoService;
import com.example.myapplication.finance.service.YhFinanceInvoicePeizhiService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceUserService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class ZhangHaoGuanLiChangeActivity extends AppCompatActivity {
    private YhFinanceUser yhFinanceUser;
    private YhFinanceUserService yhFinanceUserService;

    private EditText name;
    private EditText pwd;
    private EditText _do;

    List<YhFinanceUser> getList;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhanghaoguanli_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceUserService = new YhFinanceUserService();

        name = findViewById(R.id.name);
        pwd = findViewById(R.id.pwd);
        _do = findViewById(R.id._do);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhFinanceUser = (YhFinanceUser) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            name.setText(yhFinanceUser.getName());
            pwd.setText(yhFinanceUser.getPwd());
            _do.setText(yhFinanceUser.get_do());
        }else{
            yhFinanceUser = new YhFinanceUser();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
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
                    ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceUserService.update(yhFinanceUser);
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
                    ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceUserService.insert(yhFinanceUser);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {

        if (name.getText().toString().equals("")) {
            ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "请输入账号");
            return false;
        } else {
            yhFinanceUser.setName(name.getText().toString());
        }

        if (pwd.getText().toString().equals("")) {
            ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "请输入密码");
            return false;
        } else {
            yhFinanceUser.setPwd(pwd.getText().toString());
        }

        if (_do.getText().toString().equals("")) {
            ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "请输入操作密码");
            return false;
        } else {
            yhFinanceUser.set_do(_do.getText().toString());
        }

        yhFinanceUser.setCompany(yhFinanceUser.getCompany());

        return true;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
