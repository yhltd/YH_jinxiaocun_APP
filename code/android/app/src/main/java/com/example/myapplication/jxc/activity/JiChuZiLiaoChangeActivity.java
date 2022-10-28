package com.example.myapplication.jxc.activity;

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
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.utils.ToastUtil;

public class JiChuZiLiaoChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;

    private EditText cpDm;
    private EditText name;
    private EditText leiBie;
    private EditText danWei;
    private EditText kehu;
    private EditText gongyingshang;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jichuziliao_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();

        cpDm = findViewById(R.id.cpDm);
        name = findViewById(R.id.name);
        leiBie = findViewById(R.id.leiBie);
        danWei = findViewById(R.id.danWei);
        kehu = findViewById(R.id.kehu);
        gongyingshang = findViewById(R.id.gongyingshang);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhJinXiaoCunJiChuZiLiao = new YhJinXiaoCunJiChuZiLiao();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhJinXiaoCunJiChuZiLiao = (YhJinXiaoCunJiChuZiLiao) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            cpDm.setText(yhJinXiaoCunJiChuZiLiao.getSpDm());
            name.setText(yhJinXiaoCunJiChuZiLiao.getName());
            leiBie.setText(yhJinXiaoCunJiChuZiLiao.getLeiBie());
            danWei.setText(yhJinXiaoCunJiChuZiLiao.getDanWei());
            kehu.setText(yhJinXiaoCunJiChuZiLiao.getShouHuo());
            gongyingshang.setText(yhJinXiaoCunJiChuZiLiao.getGongHuo());
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
                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunJiChuZiLiaoService.insert(yhJinXiaoCunJiChuZiLiao);
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
                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunJiChuZiLiaoService.update(yhJinXiaoCunJiChuZiLiao);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (cpDm.getText().toString().equals("")) {
            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输商品代码");
            return false;
        } else {
            yhJinXiaoCunJiChuZiLiao.setSpDm(cpDm.getText().toString());
        }

        if (name.getText().toString().equals("")) {
            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输商品名称");
            return false;
        } else {
            yhJinXiaoCunJiChuZiLiao.setName(name.getText().toString());
        }

        if (leiBie.getText().toString().equals("")) {
            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输入商品类别");
            return false;
        } else {
            yhJinXiaoCunJiChuZiLiao.setLeiBie(leiBie.getText().toString());
        }

        if (danWei.getText().toString().equals("")) {
            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输入商品单位");
            return false;
        } else {
            yhJinXiaoCunJiChuZiLiao.setDanWei(danWei.getText().toString());
        }

        yhJinXiaoCunJiChuZiLiao.setShouHuo(kehu.getText().toString());
        yhJinXiaoCunJiChuZiLiao.setGongHuo(gongyingshang.getText().toString());
        yhJinXiaoCunJiChuZiLiao.setGsName(yhJinXiaoCunUser.getGongsi());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
