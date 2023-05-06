package com.example.myapplication.scheduling.activity;

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
import com.example.myapplication.scheduling.entity.ModuleType;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.ModuleTypeService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;


public class ModuleTypeChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private ModuleType moduleType;
    private ModuleTypeService moduleTypeService;

    private EditText name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_type_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        moduleTypeService = new ModuleTypeService();

        name = findViewById(R.id.name);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            moduleType = new ModuleType();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            moduleType = (ModuleType) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            name.setText(moduleType.getName());
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
        name.setText("");
    }

    public void insertClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ModuleTypeChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ModuleTypeChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = moduleTypeService.insert(moduleType);
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
                    ToastUtil.show(ModuleTypeChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ModuleTypeChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = moduleTypeService.update(moduleType);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (name.getText().toString().equals("")) {
            ToastUtil.show(ModuleTypeChangeActivity.this, "请输入模块类别");
            return false;
        } else {
            moduleType.setName(name.getText().toString());
        }

        moduleType.setCompany(userInfo.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
