package com.example.myapplication.jiaowu.activity;

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
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.ShouZhiMingXi;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.jiaowu.service.ShouZhiMingXiService;
import com.example.myapplication.jiaowu.service.StudentService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class ShouZhiMingXiChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private ShouZhiMingXi shouZhiMingXi;
    private ShouZhiMingXiService shouZhiMingXiService;
    private SheZhiService sheZhiService;

    private EditText rgdate;
    private EditText money;
    private Spinner msort;
    private EditText mremark;
    private EditText paid;
    private Spinner psort;
    private EditText premark;
    private Spinner handle;

    String[] msort_array;
    String[] psort_array;
    String[] teacher_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_shouzhimingxi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        shouZhiMingXiService = new ShouZhiMingXiService();
        sheZhiService = new SheZhiService();

        rgdate = findViewById(R.id.rgdate);
        showDateOnClick(rgdate);
        money = findViewById(R.id.money);
        msort = findViewById(R.id.msort);
        mremark = findViewById(R.id.mremark);
        paid = findViewById(R.id.paid);
        psort = findViewById(R.id.psort);
        premark = findViewById(R.id.premark);
        handle = findViewById(R.id.handle);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            shouZhiMingXi = (ShouZhiMingXi) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            rgdate.setText(shouZhiMingXi.getRgdate().toString());
            money.setText(String.valueOf(shouZhiMingXi.getMoney()));
            mremark.setText(shouZhiMingXi.getMremark().toString());
            paid.setText(String.valueOf(shouZhiMingXi.getPaid()));
            premark.setText(shouZhiMingXi.getPremark().toString());

        }else{
            shouZhiMingXi = new ShouZhiMingXi();
            init2();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
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


    public void init1() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_msort = new ArrayAdapter<String>(ShouZhiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, msort_array);
                msort.setAdapter(StringUtils.cast(adapter_msort));
                msort.setSelection(getMsortPosition(shouZhiMingXi.getMsort()));

                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(ShouZhiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                handle.setAdapter(StringUtils.cast(adapter_teacher));
                handle.setSelection(getTeacherPosition(shouZhiMingXi.getHandle()));

                SpinnerAdapter adapter_psort = new ArrayAdapter<String>(ShouZhiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, psort_array);
                psort.setAdapter(StringUtils.cast(adapter_psort));
                psort.setSelection(getPsortPosition(shouZhiMingXi.getPsort()));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<SheZhi> courseList = sheZhiService.getList(teacher.getCompany());
                    if (courseList.size() > 0) {
                        msort_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            msort_array[i] = courseList.get(i).getMsort();
                        }
                        teacher_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            teacher_array[i] = courseList.get(i).getTeacher();
                        }
                        psort_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            psort_array[i] = courseList.get(i).getPsort();
                        }
                    }
                    adapter = new ArrayAdapter<String>(ShouZhiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, msort_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void init2() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_msort = new ArrayAdapter<String>(ShouZhiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, msort_array);
                msort.setAdapter(StringUtils.cast(adapter_msort));

                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(ShouZhiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                handle.setAdapter(StringUtils.cast(adapter_teacher));

                SpinnerAdapter adapter_psort = new ArrayAdapter<String>(ShouZhiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, psort_array);
                psort.setAdapter(StringUtils.cast(adapter_psort));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<SheZhi> courseList = sheZhiService.getList(teacher.getCompany());
                    if (courseList.size() > 0) {
                        msort_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            msort_array[i] = courseList.get(i).getMsort();
                        }
                        teacher_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            teacher_array[i] = courseList.get(i).getTeacher();
                        }
                        psort_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            psort_array[i] = courseList.get(i).getPsort();
                        }
                    }
                    adapter = new ArrayAdapter<String>(ShouZhiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, msort_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ShouZhiMingXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ShouZhiMingXiChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = shouZhiMingXiService.update(shouZhiMingXi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ShouZhiMingXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ShouZhiMingXiChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = shouZhiMingXiService.insert(shouZhiMingXi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {

        if (rgdate.getText().toString().equals("")) {
            ToastUtil.show(ShouZhiMingXiChangeActivity.this, "请选择日期");
            return false;
        } else {
            shouZhiMingXi.setRgdate(rgdate.getText().toString());
        }

        if (handle.getSelectedItem().toString().equals("")) {
            ToastUtil.show(ShouZhiMingXiChangeActivity.this, "请选择经手人");
            return false;
        } else {
            shouZhiMingXi.setHandle(handle.getSelectedItem().toString());
        }

        if(money.getText().toString().equals("")){
            shouZhiMingXi.setMoney(Integer.parseInt("0"));
        }else{
            shouZhiMingXi.setMoney(Integer.parseInt(money.getText().toString()));
        }
        shouZhiMingXi.setMsort(msort.getSelectedItem().toString());
        shouZhiMingXi.setMremark(mremark.getText().toString());
        if(paid.getText().toString().equals("")){
            shouZhiMingXi.setPaid(Integer.parseInt("0"));
        }else{
            shouZhiMingXi.setPaid(Integer.parseInt(paid.getText().toString()));
        }
        shouZhiMingXi.setPsort(psort.getSelectedItem().toString());
        shouZhiMingXi.setPremark(premark.getText().toString());
        shouZhiMingXi.setCompany(teacher.getCompany());
        return true;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ShouZhiMingXiChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private int getMsortPosition(String param) {
        if (msort_array != null) {
            for (int i = 0; i < msort_array.length; i++) {
                if (param.equals(msort_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getTeacherPosition(String param) {
        if (teacher_array != null) {
            for (int i = 0; i < teacher_array.length; i++) {
                if (param.equals(teacher_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getPsortPosition(String param) {
        if (psort_array != null) {
            for (int i = 0; i < psort_array.length; i++) {
                if (param.equals(psort_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
