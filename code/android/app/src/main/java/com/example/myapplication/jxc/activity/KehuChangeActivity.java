package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
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
import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.jxc.service.YhJinXiaoCunKeHuService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

public class KehuChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunKeHu yhJinXiaoCunKeHu;
    private YhJinXiaoCunKeHuService yhJinXiaoCunKeHuService;

    private EditText beizhu;
    private EditText lianxidizhi;
    private EditText lianxifangshi;

    private String type;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kehu_change);

        Intent intent = getIntent();
        type = intent.getStringExtra("kehu_type");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(type + "资料");
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunKeHuService = new YhJinXiaoCunKeHuService();

        beizhu = findViewById(R.id.beizhu);
        lianxidizhi = findViewById(R.id.lianxidizhi);
        lianxifangshi = findViewById(R.id.lianxifangshi);


        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhJinXiaoCunKeHu = new YhJinXiaoCunKeHu();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhJinXiaoCunKeHu = (YhJinXiaoCunKeHu) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            beizhu.setText(yhJinXiaoCunKeHu.getBeizhu());
            lianxidizhi.setText(yhJinXiaoCunKeHu.getLianxidizhi());
            lianxifangshi.setText(yhJinXiaoCunKeHu.getLianxifangshi());
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

    public void clearClick(View v) {
        beizhu.setText("");
        lianxidizhi.setText("");
        lianxifangshi.setText("");
    }

    public void insertClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KehuChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KehuChangeActivity.this, "保存失败，请稍后再试");
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                if (type.equals("客户")) {
                    msg.obj = yhJinXiaoCunKeHuService.insertByKehu(yhJinXiaoCunKeHu);
                } else if (type.equals("供应商")) {
                    msg.obj = yhJinXiaoCunKeHuService.insertByGys(yhJinXiaoCunKeHu);
                }

                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KehuChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KehuChangeActivity.this, "保存失败，请稍后再试");
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                if (type.equals("客户")) {
                    msg.obj = yhJinXiaoCunKeHuService.updateByKehu(yhJinXiaoCunKeHu);
                } else if (type.equals("供应商")) {
                    msg.obj = yhJinXiaoCunKeHuService.updateByGys(yhJinXiaoCunKeHu);
                }
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (beizhu.getText().toString().equals("")) {
            ToastUtil.show(KehuChangeActivity.this, "请输入公司名称");
            return false;
        } else {
            yhJinXiaoCunKeHu.setBeizhu(beizhu.getText().toString());
        }

        yhJinXiaoCunKeHu.setLianxidizhi(lianxidizhi.getText().toString());
        yhJinXiaoCunKeHu.setLianxifangshi(lianxifangshi.getText().toString());
        yhJinXiaoCunKeHu.setGongsi(yhJinXiaoCunUser.getGongsi());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
