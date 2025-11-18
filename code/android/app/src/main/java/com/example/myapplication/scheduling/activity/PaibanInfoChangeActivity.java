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
import com.example.myapplication.scheduling.entity.PaibanInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.scheduling.service.PaibanInfoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaibanInfoChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private PaibanInfo paibanInfo;
    private PaibanInfoService paibanInfoService;
    private DepartmentService departmentService;

    private EditText riqi;
    private EditText plan_name;
    private EditText renshu;
    private Spinner department_name;

    private String department_text;
    private List<String> departmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paiban_info_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        paibanInfoService = new PaibanInfoService();
        departmentService = new DepartmentService();

        riqi = findViewById(R.id.riqi);
        plan_name = findViewById(R.id.plan_name);
        renshu = findViewById(R.id.renshu);
        department_name = findViewById(R.id.department_name);

        initList();

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            paibanInfo = (PaibanInfo) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            riqi.setText(paibanInfo.getRiqi());
            plan_name.setText(paibanInfo.getPlan_name());
            renshu.setText(paibanInfo.getRenshu());
            department_name.setSelection(getDepartmentPosition(paibanInfo.getDepartment_name()));
        }

        department_name.setOnItemSelectedListener(new departmentSelectedListener());

        showDateOnClick(riqi);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void initList() {
//
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if (msg.obj != null) {
//                    department_name.setAdapter((SpinnerAdapter) msg.obj);
//                }
//
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                SpinnerAdapter adapter = null;
//                try {
//                    departmentService = new DepartmentService();
//                    departmentList = new ArrayList<>();
//                    departmentList = departmentService.getDepartment(userInfo.getCompany());
//                    adapter = new ArrayAdapter<String>(PaibanInfoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, departmentList);
////                    if (departmentList.size() > 0) {
//                        msg.obj = adapter;
////                    } else {
////                        msg.obj = null;
////                    }
//                    listLoadHandler.sendMessage(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    department_name.setAdapter((SpinnerAdapter) msg.obj);

                    // 在数据加载完成后设置选中项
                    Intent intent = getIntent();
                    int id = intent.getIntExtra("type", 0);
                    if (id == R.id.update_btn && paibanInfo != null) {
                        // 延迟设置，确保Spinner已经渲染完成
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setDepartmentSelection(paibanInfo.getDepartment_name());
                            }
                        }, 100);
                    }
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
                    adapter = new ArrayAdapter<String>(PaibanInfoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, departmentList);
                    msg.obj = adapter;
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 新增方法：设置部门选中项
    private void setDepartmentSelection(String departmentNameValue) {
        if (departmentList != null && departmentNameValue != null) {
            for (int i = 0; i < departmentList.size(); i++) {
                if (departmentNameValue.equals(departmentList.get(i))) {
                    department_name.setSelection(i);
                    department_text = departmentList.get(i); // 同时更新当前选中的文本
                    break;
                }
            }
        }
    }

    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(PaibanInfoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(PaibanInfoChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = paibanInfoService.update(paibanInfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        paibanInfo.setRiqi(riqi.getText().toString());
        paibanInfo.setPlan_name(plan_name.getText().toString());
        paibanInfo.setDepartment_name(department_text);
        paibanInfo.setRenshu(renshu.getText().toString());
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(PaibanInfoChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

}
