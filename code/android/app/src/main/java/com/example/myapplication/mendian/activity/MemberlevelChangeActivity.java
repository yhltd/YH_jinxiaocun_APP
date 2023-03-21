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
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianMemberlevel;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianMemberinfoService;
import com.example.myapplication.mendian.service.YhMendianMemberlevelService;
import com.example.myapplication.mendian.service.YhMendianUserService;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.List;

public class MemberlevelChangeActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;
    private YhMendianUserService yhMendianUserService;

    private YhMendianMemberlevel yhMendianMemberlevel;
    private YhMendianMemberlevelService yhMendianMemberlevelService;

    private EditText jibie;
    private EditText menkan;
    private EditText bili;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberlevel_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianUserService = new YhMendianUserService();
        yhMendianMemberlevel = new YhMendianMemberlevel();
        yhMendianMemberlevelService = new YhMendianMemberlevelService();

        jibie = findViewById(R.id.jibie);
        menkan = findViewById(R.id.menkan);
        bili = findViewById(R.id.bili);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhMendianMemberlevel = new YhMendianMemberlevel();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }else if(id == R.id.update_btn) {
            yhMendianMemberlevel = (YhMendianMemberlevel) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            jibie.setText(yhMendianMemberlevel.getJibie());
            menkan.setText(yhMendianMemberlevel.getMenkan());
            bili.setText(yhMendianMemberlevel.getBili());
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
                    ToastUtil.show(MemberlevelChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(MemberlevelChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianMemberlevelService.updateBylevel(yhMendianMemberlevel);
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
                    ToastUtil.show(MemberlevelChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(MemberlevelChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianMemberlevelService.insertBylevel(yhMendianMemberlevel);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {

        if (jibie.getText().toString().equals("")) {
            ToastUtil.show(MemberlevelChangeActivity.this, "请输入级别");
            return false;
        } else {
            yhMendianMemberlevel.setJibie(jibie.getText().toString());
        }

        if (menkan.getText().toString().equals("")) {
            ToastUtil.show(MemberlevelChangeActivity.this, "请输入门槛金额");
            return false;
        } else {
            yhMendianMemberlevel.setMenkan(menkan.getText().toString());
        }
        if (bili.getText().toString().equals("")) {
            ToastUtil.show(MemberlevelChangeActivity.this, "请输入折扣比例");
            return false;
        } else {
            yhMendianMemberlevel.setBili(bili.getText().toString());
        }


        yhMendianMemberlevel.setCompany(yhMendianMemberlevel.getCompany());

        return true;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
