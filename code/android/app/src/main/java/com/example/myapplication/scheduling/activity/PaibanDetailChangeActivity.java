package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.PaibanDetail;
import com.example.myapplication.scheduling.entity.PaibanRenyuan;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.scheduling.service.PaibanDetailService;
import com.example.myapplication.scheduling.service.PaibanRenyuanService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaibanDetailChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private PaibanDetail paibanDetail;
    private PaibanDetailService paibanDetailService;
    private DepartmentService departmentService;

    private EditText staffName;
    private EditText phoneNumber;
    private EditText idNumber;
    private Spinner departmentName;
    private EditText banci;
    private EditText riqi;

    private String department_text;

    private List<String> departmentList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paiban_detail_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        paibanDetailService = new PaibanDetailService();
        departmentService = new DepartmentService();

        staffName = findViewById(R.id.staffName);
        phoneNumber = findViewById(R.id.phoneNumber);
        idNumber = findViewById(R.id.idNumber);
        departmentName = findViewById(R.id.departmentName);
        banci = findViewById(R.id.banci);
        riqi = findViewById(R.id.riqi);

        initList();

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            paibanDetail = (PaibanDetail) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            staffName.setText(paibanDetail.getStaff_name());
            phoneNumber.setText(paibanDetail.getPhone_number());
            idNumber.setText(paibanDetail.getId_number());
            departmentName.setSelection(getDepartmentPosition(paibanDetail.getDepartment_name()));
            banci.setText(paibanDetail.getBanci());
            riqi.setText(paibanDetail.getC());
        }

        departmentName.setOnItemSelectedListener(new departmentSelectedListener());

        showDateOnClick(riqi);
    }

    public void clearClick(View v) {
        staffName.setText("");
        phoneNumber.setText("");
        idNumber.setText("");
        banci.setText("");
        riqi.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    departmentName.setAdapter((SpinnerAdapter) msg.obj);
                }

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
                    adapter = new ArrayAdapter<String>(PaibanDetailChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, departmentList);
//                    if (departmentList.size() > 0) {
                        msg.obj = adapter;
//                    } else {
//                        msg.obj = null;
//                    }
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(PaibanDetailChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(PaibanDetailChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = paibanDetailService.update(paibanDetail);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (staffName.getText().toString().equals("")) {
            ToastUtil.show(PaibanDetailChangeActivity.this, "请输入姓名");
            return false;
        } else {
            paibanDetail.setStaff_name(staffName.getText().toString());
        }
        paibanDetail.setPhone_number(phoneNumber.getText().toString());
        paibanDetail.setId_number(idNumber.getText().toString());
        paibanDetail.setDepartment_name(department_text);
        paibanDetail.setBanci(banci.getText().toString());
        paibanDetail.setC(riqi.getText().toString());
        paibanDetail.setCompany(userInfo.getCompany());
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

    @SuppressLint("ClickableViewAccessibility")
    protected void showDateOnClick(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(editText);
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg(editText);
                }

            }
        });
    }

    protected void showDatePickDlg(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(PaibanDetailChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}
