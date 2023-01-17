package com.example.myapplication.renshi.activity;

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
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.renshi.service.YhRenShiPeiZhiBiaoService;
import com.example.myapplication.utils.ToastUtil;

public class BaoShuiChangeActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiGongZiMingXi yhRenShiGongZiMingXi;
    private YhRenShiGongZiMingXiService yhRenShiGongZiMingXiService;

    private EditText B;
    private EditText E;
    private EditText U;
    private EditText AI;
    private EditText AK;
    private EditText AN;
    private EditText AO;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoshui_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();

        B = findViewById(R.id.B);
        E = findViewById(R.id.E);
        U = findViewById(R.id.U);
        AI = findViewById(R.id.AI);
        AK = findViewById(R.id.AK);
        AN = findViewById(R.id.AN);
        AO = findViewById(R.id.AO);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhRenShiGongZiMingXi = new YhRenShiGongZiMingXi();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhRenShiGongZiMingXi = (YhRenShiGongZiMingXi) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            B.setText(yhRenShiGongZiMingXi.getB());
            E.setText(yhRenShiGongZiMingXi.getE());
            U.setText(yhRenShiGongZiMingXi.getU());
            AI.setText(yhRenShiGongZiMingXi.getAi());
            AK.setText(yhRenShiGongZiMingXi.getAk());
            AN.setText(yhRenShiGongZiMingXi.getAn());
            AO.setText(yhRenShiGongZiMingXi.getAo());
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
                    ToastUtil.show(BaoShuiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BaoShuiChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiGongZiMingXiService.insert(yhRenShiGongZiMingXi);
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
                    ToastUtil.show(BaoShuiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BaoShuiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiGongZiMingXiService.update(yhRenShiGongZiMingXi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() {

        yhRenShiGongZiMingXi.setB(B.getText().toString());
        yhRenShiGongZiMingXi.setE(E.getText().toString());
        yhRenShiGongZiMingXi.setU(U.getText().toString());
        yhRenShiGongZiMingXi.setAi(AI.getText().toString());
        yhRenShiGongZiMingXi.setAk(AK.getText().toString());
        yhRenShiGongZiMingXi.setAn(AN.getText().toString());
        yhRenShiGongZiMingXi.setAo(AO.getText().toString());
        yhRenShiGongZiMingXi.setBd(yhRenShiUser.getL().replace("_hr",""));
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
