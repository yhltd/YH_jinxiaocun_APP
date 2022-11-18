package com.example.myapplication.finance.activity;

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
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceDepartmentService;
import com.example.myapplication.finance.service.YhFinanceKeMuZongZhangService;
import com.example.myapplication.utils.ToastUtil;

import java.math.BigDecimal;

public class KeMuZongZhangChangeActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceKeMuZongZhang yhFinanceKeMuZongZhang;
    private YhFinanceKeMuZongZhangService yhFinanceKeMuZongZhangService;

    private EditText code;
    private EditText name;
    private EditText load;
    private EditText borrowed;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kemuzongzhang_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();

        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        load = findViewById(R.id.load);
        borrowed = findViewById(R.id.borrowed);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhFinanceKeMuZongZhang = new YhFinanceKeMuZongZhang();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhFinanceKeMuZongZhang = (YhFinanceKeMuZongZhang) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            code.setText(yhFinanceKeMuZongZhang.getCode());
            name.setText(yhFinanceKeMuZongZhang.getName());
            load.setText(yhFinanceKeMuZongZhang.getLoad().toString());
            borrowed.setText(yhFinanceKeMuZongZhang.getBorrowed().toString());
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

    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KeMuZongZhangChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KeMuZongZhangChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceKeMuZongZhangService.update(yhFinanceKeMuZongZhang);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (code.getText().toString().equals("")) {
            ToastUtil.show(KeMuZongZhangChangeActivity.this, "请输科目代码");
            return false;
        } else {
            yhFinanceKeMuZongZhang.setCode(code.getText().toString());
        }

        if (name.getText().toString().equals("")) {
            ToastUtil.show(KeMuZongZhangChangeActivity.this, "请输科目名称");
            return false;
        } else {
            yhFinanceKeMuZongZhang.setName(name.getText().toString());
        }

        if (load.getText().toString().equals("")) {
            ToastUtil.show(KeMuZongZhangChangeActivity.this, "请输年初借金");
            return false;
        } else {
            BigDecimal bd = new BigDecimal(load.getText().toString());
            yhFinanceKeMuZongZhang.setLoad(bd);
        }

        if (borrowed.getText().toString().equals("")) {
            ToastUtil.show(KeMuZongZhangChangeActivity.this, "请输年初贷金");
            return false;
        } else {
            BigDecimal bd = new BigDecimal(borrowed.getText().toString());
            yhFinanceKeMuZongZhang.setBorrowed(bd);
        }

        yhFinanceKeMuZongZhang.setCompany(yhFinanceUser.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
