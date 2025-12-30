package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunCangKu;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;

import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunCangKuService;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;

import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.List;

public class CangKuChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunCangKu yhJinXiaoCunCangKu;
    private YhJinXiaoCunCangKuService yhJinXiaoCunCangKuService;

    List<YhJinXiaoCunCangKu> getList;


    private EditText cangku;



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cangku_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunCangKuService = new YhJinXiaoCunCangKuService();

        cangku = findViewById(R.id.cangku);
        init();

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

        cangku.setText("");
    }

    public void init() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MyApplication myApplication = (MyApplication) getApplication();
                Intent intent = getIntent();
                int id = intent.getIntExtra("type", 0);
                if (id == R.id.insert_btn) {
                    yhJinXiaoCunCangKu = new YhJinXiaoCunCangKu();
                    Button btn = findViewById(id);
                    btn.setVisibility(View.VISIBLE);
                } else if (id == R.id.update_btn) {
                    yhJinXiaoCunCangKu = (YhJinXiaoCunCangKu) myApplication.getObj();
                    Button btn = findViewById(id);
                    btn.setVisibility(View.VISIBLE);

                    cangku.setText(yhJinXiaoCunCangKu.getcangku());
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {

                    MyApplication myApplication = (MyApplication) getApplication();
                    Intent intent = getIntent();
                    int id = intent.getIntExtra("type", 0);

                    if (id == R.id.update_btn) {
                        yhJinXiaoCunCangKu = (YhJinXiaoCunCangKu) myApplication.getObj();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void insertClick(View v) {
        if (!checkForm()) return;
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(CangKuChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(CangKuChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunCangKuService.insert(yhJinXiaoCunCangKu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;
        Log.e("UpdateDebug", "准备更新，ID: " + yhJinXiaoCunCangKu.get_id());
        Log.e("UpdateDebug", "仓库名称: " + yhJinXiaoCunCangKu.getcangku());
        Log.e("UpdateDebug", "公司名称: " + yhJinXiaoCunCangKu.getGs_name());
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(CangKuChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(CangKuChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunCangKuService.update(yhJinXiaoCunCangKu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {

        if (cangku.getText().toString().equals("")) {
            ToastUtil.show(CangKuChangeActivity.this, "请输入仓库");
            return false;
        } else {
            yhJinXiaoCunCangKu.setcangku(cangku.getText().toString());
        }


        yhJinXiaoCunCangKu.setGs_name(yhJinXiaoCunUser.getGongsi());


        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }




}
