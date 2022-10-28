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
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.jxc.service.YhJinXiaoCunZhengLiService;
import com.example.myapplication.utils.ToastUtil;

public class BiJiChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunZhengLi yhJinXiaoCunZhengLi;
    private YhJinXiaoCunZhengLiService yhJinXiaoCunZhengLiService;

    private EditText cpDm;
    private EditText name;
    private EditText leiBie;
    private EditText danWei;
    private EditText beizhu;

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
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunZhengLiService = new YhJinXiaoCunZhengLiService();

        cpDm = findViewById(R.id.cpDm);
        name = findViewById(R.id.name);
        leiBie = findViewById(R.id.leiBie);
        danWei = findViewById(R.id.danWei);
        beizhu = findViewById(R.id.beizhu);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhJinXiaoCunZhengLi = new YhJinXiaoCunZhengLi();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhJinXiaoCunZhengLi = (YhJinXiaoCunZhengLi) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            cpDm.setText(yhJinXiaoCunZhengLi.getSpDm());
            name.setText(yhJinXiaoCunZhengLi.getName());
            leiBie.setText(yhJinXiaoCunZhengLi.getLeiBie());
            danWei.setText(yhJinXiaoCunZhengLi.getDanWei());
            beizhu.setText(yhJinXiaoCunZhengLi.getBeizhu());
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
                    ToastUtil.show(BiJiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BiJiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunZhengLiService.insert(yhJinXiaoCunZhengLi);
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
                    ToastUtil.show(BiJiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BiJiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunZhengLiService.update(yhJinXiaoCunZhengLi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (cpDm.getText().toString().equals("")) {
            ToastUtil.show(BiJiChangeActivity.this, "请输入商品代码");
            return false;
        } else {
            yhJinXiaoCunZhengLi.setSpDm(cpDm.getText().toString());
        }

        if (name.getText().toString().equals("")) {
            ToastUtil.show(BiJiChangeActivity.this, "请输入商品名称");
            return false;
        } else {
            yhJinXiaoCunZhengLi.setName(name.getText().toString());
        }

        if (leiBie.getText().toString().equals("")) {
            ToastUtil.show(BiJiChangeActivity.this, "请输入商品类别");
            return false;
        } else {
            yhJinXiaoCunZhengLi.setLeiBie(leiBie.getText().toString());
        }

        if (danWei.getText().toString().equals("")) {
            ToastUtil.show(BiJiChangeActivity.this, "请输入商品单位");
            return false;
        } else {
            yhJinXiaoCunZhengLi.setDanWei(danWei.getText().toString());
        }

        yhJinXiaoCunZhengLi.setBeizhu(beizhu.getText().toString());
        yhJinXiaoCunZhengLi.setGsName(yhJinXiaoCunUser.getGongsi());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
