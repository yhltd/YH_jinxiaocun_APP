package com.example.myapplication.scheduling.activity;

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
import com.example.myapplication.jxc.activity.BiJiChangeActivity;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.scheduling.entity.BomInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.BomInfoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

public class BomChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private BomInfo bomInfo;
    private BomInfoService bomInfoService;

    private EditText code;
    private EditText name;
    private EditText type;
    private EditText norms;
    private EditText comment;
    private EditText size;
    private EditText unit;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bom_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        bomInfoService = new BomInfoService();

        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        norms = findViewById(R.id.norms);
        comment = findViewById(R.id.comment);
        size = findViewById(R.id.size);
        unit = findViewById(R.id.unit);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            bomInfo = new BomInfo();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            bomInfo = (BomInfo) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            code.setText(bomInfo.getCode());
            name.setText(bomInfo.getName());
            type.setText(bomInfo.getType());
            norms.setText(bomInfo.getNorms());
            comment.setText(bomInfo.getComment());
            String set_size = bomInfo.getSize() + "";
            size.setText(set_size);
            unit.setText(bomInfo.getUnit());
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
        code.setText("");
        name.setText("");
        type.setText("");
        norms.setText("");
        comment.setText("");
        size.setText("");
        unit.setText("");
    }

    public void insertClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(BomChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BomChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = bomInfoService.insert(bomInfo);
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
                    ToastUtil.show(BomChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BomChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = bomInfoService.update(bomInfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (code.getText().toString().equals("")) {
            ToastUtil.show(BomChangeActivity.this, "请输入物料编码");
            return false;
        } else {
            bomInfo.setCode(code.getText().toString());
        }

        if (name.getText().toString().equals("")) {
            ToastUtil.show(BomChangeActivity.this, "请输入物料名称");
            return false;
        } else {
            bomInfo.setName(name.getText().toString());
        }

        if (type.getText().toString().equals("")) {
            ToastUtil.show(BomChangeActivity.this, "请输入类别");
            return false;
        } else {
            bomInfo.setType(type.getText().toString());
        }

        bomInfo.setNorms(norms.getText().toString());
        bomInfo.setComment(comment.getText().toString());

        if (size.getText().toString().equals("")) {
            ToastUtil.show(BomChangeActivity.this, "请输入大小");
            return false;
        } else {
            bomInfo.setSize(Double.parseDouble(size.getText().toString()));
        }
        bomInfo.setUnit(unit.getText().toString());

        bomInfo.setCompany(userInfo.getCompany());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
