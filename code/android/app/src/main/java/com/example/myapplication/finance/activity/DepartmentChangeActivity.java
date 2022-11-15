package com.example.myapplication.finance.activity;

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
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.activity.DepartmentActivity;
import com.example.myapplication.finance.service.YhFinanceDepartmentService;
import com.example.myapplication.jxc.activity.JiChuZiLiaoChangeActivity;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.utils.ToastUtil;

public class DepartmentChangeActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceDepartment yhFinanceDepartment;
    private YhFinanceDepartmentService yhFinanceDepartmentService;

    private EditText department;
    private EditText man;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceDepartmentService = new YhFinanceDepartmentService();

        department = findViewById(R.id.department);
        man = findViewById(R.id.man);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhFinanceDepartment = new YhFinanceDepartment();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhFinanceDepartment = (YhFinanceDepartment) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            department.setText(yhFinanceDepartment.getDepartment());
            man.setText(yhFinanceDepartment.getMan());
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
                    ToastUtil.show(DepartmentChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(DepartmentChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceDepartmentService.insert(yhFinanceDepartment);
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
                    ToastUtil.show(DepartmentChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(DepartmentChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceDepartmentService.update(yhFinanceDepartment);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() {
        if (department.getText().toString().equals("")) {
            ToastUtil.show(DepartmentChangeActivity.this, "请输部门名称");
            return false;
        } else {
            yhFinanceDepartment.setDepartment(department.getText().toString());
        }

        if (man.getText().toString().equals("")) {
            ToastUtil.show(DepartmentChangeActivity.this, "请输负责人");
            return false;
        } else {
            yhFinanceDepartment.setMan(man.getText().toString());
        }

        yhFinanceDepartment.setCompany(yhFinanceUser.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
