package com.example.myapplication.fenquan.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
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
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Gongsi;
import com.example.myapplication.fenquan.entity.Jisuan;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.Copy1Service;
import com.example.myapplication.fenquan.service.DepartmentService;
import com.example.myapplication.fenquan.service.JisuanService;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GongZuoTaiGongShiChangeActivity extends AppCompatActivity {
    private Renyuan renyuan;
    private Jisuan jisuan;
    private JisuanService jisuanService;

    private EditText thiscolumn;
    private EditText gongshi;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongzuotai_gongshi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        jisuanService = new JisuanService();

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

        thiscolumn = findViewById(R.id.thiscolumn);
        gongshi = findViewById(R.id.gongshi);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            jisuan = new Jisuan();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            jisuan = (Jisuan) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            thiscolumn.setText(jisuan.getThiscolumn());
            gongshi.setText(jisuan.getGongshi());
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
                    ToastUtil.show(GongZuoTaiGongShiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZuoTaiGongShiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = jisuanService.insert(jisuan);
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
                    ToastUtil.show(GongZuoTaiGongShiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZuoTaiGongShiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = jisuanService.update(jisuan);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (thiscolumn.getText().toString().equals("")) {
            ToastUtil.show(GongZuoTaiGongShiChangeActivity.this, "请输入工作台列");
            return false;
        } else {
            jisuan.setThiscolumn(thiscolumn.getText().toString());
        }

        if (gongshi.getText().toString().equals("")) {
            ToastUtil.show(GongZuoTaiGongShiChangeActivity.this, "请输入计算公式");
            return false;
        } else {
            jisuan.setGongshi(gongshi.getText().toString());
        }

        jisuan.setCompany(renyuan.getB());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
