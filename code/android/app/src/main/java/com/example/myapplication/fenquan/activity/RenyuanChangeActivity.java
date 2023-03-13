package com.example.myapplication.fenquan.activity;

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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.jxc.activity.BiJiChangeActivity;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RenyuanChangeActivity extends AppCompatActivity {
    private Renyuan renyuan;
    private Renyuan ry;
    private RenyuanService renyuanService;

    private EditText c;
    private EditText d;
    private EditText e;
    private Spinner bumen;
    private Spinner zhuangtai;
    private EditText email;
    private EditText phone;
    private EditText bianhao;

    private List<Department> departmentList;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biji_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        e = findViewById(R.id.e);
        bumen = findViewById(R.id.bumen);
        zhuangtai = findViewById(R.id.zhuangtai);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        bianhao = findViewById(R.id.bianhao);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            ry = new Renyuan();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            ry = (Renyuan) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            c.setText(ry.getC());
            d.setText(ry.getD());
            e.setText(ry.getE());
            email.setText(ry.getEmail());
            phone.setText(ry.getPhone());
            bianhao.setText(ry.getBianhao());

            if (ry.getZhuangtai().equals("正常")) {
                zhuangtai.setSelection(0);
            } else {
                zhuangtai.setSelection(1);
            }

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
                    ToastUtil.show(RenyuanChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(RenyuanChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = renyuanService.insert(ry);
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
                    ToastUtil.show(RenyuanChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(RenyuanChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = renyuanService.update(ry);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (c.getText().toString().equals("")) {
            ToastUtil.show(RenyuanChangeActivity.this, "请输入姓名");
            return false;
        } else {
            ry.setC(c.getText().toString());
        }

        if (d.getText().toString().equals("")) {
            ToastUtil.show(RenyuanChangeActivity.this, "请输入账号");
            return false;
        } else {
            ry.setD(d.getText().toString());
        }

        if (e.getText().toString().equals("")) {
            ToastUtil.show(RenyuanChangeActivity.this, "请输入密码");
            return false;
        } else {
            ry.setE(e.getText().toString());
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat spd = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        ry.setBumen(bumen.getSelectedItem().toString());
        ry.setZhuangtai(zhuangtai.getSelectedItem().toString());
        ry.setEmail(email.getText().toString());
        ry.setPhone(phone.getText().toString());
        ry.setBianhao(bianhao.getText().toString());
        ry.setRenyuan_id(spd.format(date));
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

}
