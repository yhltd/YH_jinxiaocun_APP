package com.example.myapplication.fenquan.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.Copy1Service;
import com.example.myapplication.fenquan.service.DepartmentService;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuMenQuanXianChangeActivity extends AppCompatActivity {
    private Renyuan renyuan;
    private Department department;
    private DepartmentService departmentService;

    private EditText department_name;
    private Spinner view_name;
    private Spinner ins;
    private Spinner del;
    private Spinner upd;
    private Spinner sel;

    String[] view_typeArray;
    String[] quanxian_typeArray;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bumen_quanxian_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        departmentService = new DepartmentService();

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

        department_name = findViewById(R.id.department_name);
        view_name = findViewById(R.id.view_name);
        ins = findViewById(R.id.ins);
        del = findViewById(R.id.del);
        upd = findViewById(R.id.upd);
        sel = findViewById(R.id.sel);

        view_typeArray = getResources().getStringArray(R.array.view_name_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, view_typeArray);
        view_name.setAdapter(adapter);

        quanxian_typeArray = getResources().getStringArray(R.array.yes_no_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, quanxian_typeArray);
        ins.setAdapter(adapter);
        del.setAdapter(adapter);
        upd.setAdapter(adapter);
        sel.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            department = new Department();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            department = (Department) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            department_name.setText(department.getDepartment_name());
            view_name.setSelection(getViewNamePosition(department.getView_name()));
            ins.setSelection(getQuanXianTypePosition(department.getIns()));
            del.setSelection(getQuanXianTypePosition(department.getDel()));
            upd.setSelection(getQuanXianTypePosition(department.getUpd()));
            sel.setSelection(getQuanXianTypePosition(department.getSel()));

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
                    ToastUtil.show(BuMenQuanXianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BuMenQuanXianChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = departmentService.insert(department);
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
                    ToastUtil.show(BuMenQuanXianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BuMenQuanXianChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = departmentService.update(department);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (department_name.getText().toString().equals("")) {
            ToastUtil.show(BuMenQuanXianChangeActivity.this, "请输入部门");
            return false;
        } else {
            department.setDepartment_name(department_name.getText().toString());
        }

        if (view_name.getSelectedItem().toString().equals("")) {
            ToastUtil.show(BuMenQuanXianChangeActivity.this, "请输入页面名称");
            return false;
        } else {
            department.setView_name(view_name.getSelectedItem().toString());
        }

        if (ins.getSelectedItem().toString().equals("")) {
            ToastUtil.show(BuMenQuanXianChangeActivity.this, "请输入增");
            return false;
        } else {
            department.setIns(ins.getSelectedItem().toString());
        }

        if (del.getSelectedItem().toString().equals("")) {
            ToastUtil.show(BuMenQuanXianChangeActivity.this, "请输入删");
            return false;
        } else {
            department.setDel(del.getSelectedItem().toString());
        }

        if (upd.getSelectedItem().toString().equals("")) {
            ToastUtil.show(BuMenQuanXianChangeActivity.this, "请输入改");
            return false;
        } else {
            department.setUpd(upd.getSelectedItem().toString());
        }

        if (sel.getSelectedItem().toString().equals("")) {
            ToastUtil.show(BuMenQuanXianChangeActivity.this, "请输入查");
            return false;
        } else {
            department.setSel(sel.getSelectedItem().toString());
        }

        department.setCompany(renyuan.getB());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private int getQuanXianTypePosition(String param) {
        if (quanxian_typeArray != null) {
            for (int i = 0; i < quanxian_typeArray.length; i++) {
                if (param.equals(quanxian_typeArray[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getViewNamePosition(String param) {
        if (view_typeArray != null) {
            for (int i = 0; i < view_typeArray.length; i++) {
                if (param.equals(view_typeArray[i])) {
                    return i;
                }
            }
        }
        return 0;
    }


}
