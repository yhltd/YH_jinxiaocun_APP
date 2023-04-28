package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
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

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class DepartmentChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private Department department;
    private DepartmentService departmentService;

    private EditText department_name;
    private EditText view_name;
    private Spinner add;
    private Spinner del;
    private Spinner upd;
    private Spinner sel;

    private String addText;
    private String delText;
    private String updText;
    private String selText;

    private List<String> xiala;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_pc_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        xiala = new ArrayList<>();
        xiala.add("是");
        xiala.add("否");

        departmentService = new DepartmentService();

        department_name = findViewById(R.id.department_name_text);
        view_name = findViewById(R.id.view_name_text);

        add = findViewById(R.id.add);
        del = findViewById(R.id.del);
        upd = findViewById(R.id.upd);
        sel = findViewById(R.id.sel);

        SpinnerAdapter adapter = new ArrayAdapter<String>(DepartmentChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, xiala);

        add.setAdapter(adapter);
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
            view_name.setText(department.getView_name());
            if (department.getAdd().equals("是")) {
                add.setSelection(0);
            } else {
                add.setSelection(1);
            }
            if (department.getDel().equals("是")) {
                del.setSelection(0);
            } else {
                del.setSelection(1);
            }
            if (department.getUpd().equals("是")) {
                upd.setSelection(0);
            } else {
                upd.setSelection(1);
            }
            if (department.getSel().equals("是")) {
                sel.setSelection(0);
            } else {
                sel.setSelection(1);
            }
        }

        add.setOnItemSelectedListener(new addSelectedListener());
        del.setOnItemSelectedListener(new delSelectedListener());
        upd.setOnItemSelectedListener(new updSelectedListener());
        sel.setOnItemSelectedListener(new selSelectedListener());
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
        department_name.setText("");
        view_name.setText("");
    }

    public void insertClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(DepartmentChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(DepartmentChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = departmentService.insert(department);
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
                    ToastUtil.show(DepartmentChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(DepartmentChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = departmentService.update(department);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (department_name.getText().toString().equals("")) {
            ToastUtil.show(DepartmentChangeActivity.this, "请输入部门");
            return false;
        } else {
            department.setDepartment_name(department_name.getText().toString());
        }

        if (view_name.getText().toString().equals("")) {
            ToastUtil.show(DepartmentChangeActivity.this, "请输入页面名称");
            return false;
        } else {
            department.setView_name(view_name.getText().toString());
        }

        department.setAdd(addText);
        department.setDel(delText);
        department.setUpd(updText);
        department.setSel(selText);


        department.setCompany(userInfo.getCompany());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private class addSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            if (position == 0) {
                addText = "是";
            } else {
                addText = "否";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class delSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            if (position == 0) {
                delText = "是";
            } else {
                delText = "否";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class updSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            if (position == 0) {
                updText = "是";
            } else {
                updText = "否";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class selSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            if (position == 0) {
                selText = "是";
            } else {
                selText = "否";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
