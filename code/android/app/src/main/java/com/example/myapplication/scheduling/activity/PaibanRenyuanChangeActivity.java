package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.activity.ProductQueryActivity;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.PaibanRenyuan;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.scheduling.service.PaibanRenyuanService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class PaibanRenyuanChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private PaibanRenyuan paibanRenyuan;
    private PaibanRenyuanService paibanRenyuanService;
    private DepartmentService departmentService;

    private EditText staffName;
    private EditText phoneNumber;
    private EditText idNumber;
    private Spinner departmentName;
    private EditText banci;

    private String department_text;

    private List<String> departmentList;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paiban_renyuan_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        paibanRenyuanService = new PaibanRenyuanService();
        departmentService = new DepartmentService();

        staffName = findViewById(R.id.staffName);
        phoneNumber = findViewById(R.id.phoneNumber);
        idNumber = findViewById(R.id.idNumber);
        departmentName = findViewById(R.id.departmentName);
        banci = findViewById(R.id.banci);

        initList();

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            paibanRenyuan = new PaibanRenyuan();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            paibanRenyuan = (PaibanRenyuan) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            staffName.setText(paibanRenyuan.getStaff_name());
            phoneNumber.setText(paibanRenyuan.getPhone_number());
            idNumber.setText(paibanRenyuan.getId_number());
            departmentName.setSelection(getDepartmentPosition(paibanRenyuan.getDepartment_name()));
            banci.setText(paibanRenyuan.getBanci());
        }

        departmentName.setOnItemSelectedListener(new departmentSelectedListener());
        
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
        staffName.setText("");
        phoneNumber.setText("");
        idNumber.setText("");
        banci.setText("");
    }

    private void initList() {
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    departmentName.setAdapter((SpinnerAdapter) msg.obj);
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                SpinnerAdapter adapter = null;
                try {
                    departmentService = new DepartmentService();
                    departmentList = new ArrayList<>();
                    departmentList = departmentService.getDepartment(userInfo.getCompany());
                    adapter = new ArrayAdapter<String>(PaibanRenyuanChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, departmentList);
                    if (departmentList.size() > 0) {
                        msg.obj = adapter;
                    } else {
                        msg.obj = null;
                    }
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void insertClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(PaibanRenyuanChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(PaibanRenyuanChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = paibanRenyuanService.insert(paibanRenyuan);
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
                    ToastUtil.show(PaibanRenyuanChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(PaibanRenyuanChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = paibanRenyuanService.update(paibanRenyuan);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (staffName.getText().toString().equals("")) {
            ToastUtil.show(PaibanRenyuanChangeActivity.this, "请输入姓名");
            return false;
        } else {
            paibanRenyuan.setStaff_name(staffName.getText().toString());
        }
        paibanRenyuan.setPhone_number(phoneNumber.getText().toString());
        paibanRenyuan.setId_number(idNumber.getText().toString());
        paibanRenyuan.setDepartment_name(department_text);
        paibanRenyuan.setBanci(banci.getText().toString());
        paibanRenyuan.setCompany(userInfo.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private class departmentSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            department_text = departmentList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private int getDepartmentPosition(String param) {
        if (departmentList != null) {
            for (int i = 0; i < departmentList.size(); i++) {
                if (param.equals(departmentList.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

}
