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
import com.example.myapplication.jiaowu.entity.Payment;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.PaymentService;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.jiaowu.service.StudentService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class JiaoFeiJiLuChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private Payment payment;
    private PaymentService paymentService;
    private SheZhiService sheZhiService;

    private EditText ksdate;
    private EditText realname;
    private EditText paid;
    private EditText money;
    private Spinner paiment;
    private EditText keeper;
    private EditText remark;

    String[] paiment_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_jiaofeijilu_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        paymentService = new PaymentService();
        sheZhiService = new SheZhiService();

        ksdate = findViewById(R.id.ksdate);
        showDateOnClick(ksdate);
        realname = findViewById(R.id.realname);
        paid = findViewById(R.id.paid);
        money = findViewById(R.id.money);
        paiment = findViewById(R.id.paiment);
        keeper = findViewById(R.id.keeper);
        remark = findViewById(R.id.remark);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            payment = (Payment) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            ksdate.setText(payment.getKsdate().toString());
            realname.setText(payment.getRealname().toString());
            paid.setText(String.valueOf(payment.getPaid()));
            money.setText(String.valueOf(payment.getMoney()));
            keeper.setText(payment.getKeeper().toString());
            remark.setText(payment.getRemark().toString());
        }else{
            payment = new Payment();
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
                SpinnerAdapter adapter_course = new ArrayAdapter<String>(JiaoFeiJiLuChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, paiment_array);
                paiment.setAdapter(StringUtils.cast(adapter_course));
                paiment.setSelection(getPaimentPosition(payment.getPaiment()));
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
                        paiment_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            paiment_array[i] = courseList.get(i).getPaiment();
                        }
                    }
                    adapter = new ArrayAdapter<String>(JiaoFeiJiLuChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, paiment_array);
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
                SpinnerAdapter adapter_course = new ArrayAdapter<String>(JiaoFeiJiLuChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, paiment_array);
                paiment.setAdapter(StringUtils.cast(adapter_course));
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
                        paiment_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            paiment_array[i] = courseList.get(i).getPaiment();
                        }
                    }
                    adapter = new ArrayAdapter<String>(JiaoFeiJiLuChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, paiment_array);
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

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiaoFeiJiLuChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiaoFeiJiLuChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = paymentService.update(payment);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiaoFeiJiLuChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiaoFeiJiLuChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = paymentService.insert(payment);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() throws ParseException {

        if (ksdate.getText().toString().equals("")) {
            ToastUtil.show(JiaoFeiJiLuChangeActivity.this, "请输入日期");
            return false;
        } else {
            payment.setKsdate(ksdate.getText().toString());
        }

        if (realname.getText().toString().equals("")) {
            ToastUtil.show(JiaoFeiJiLuChangeActivity.this, "请输入学生姓名");
            return false;
        } else {
            payment.setRealname(realname.getText().toString());
        }
        if(paid.getText().toString().equals("")){
            payment.setPaid(Integer.parseInt("0"));
        }else{
            payment.setPaid(Integer.parseInt(paid.getText().toString()));
        }
        if(money.getText().toString().equals("")){
            payment.setMoney(Integer.parseInt("0"));
        }else{
            payment.setMoney(Integer.parseInt(money.getText().toString()));
        }

        payment.setPaiment(paiment.getSelectedItem().toString());
        payment.setKeeper(keeper.getText().toString());
        payment.setRemark(remark.getText().toString());
        payment.setCompany(teacher.getCompany());

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(JiaoFeiJiLuChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private int getPaimentPosition(String param) {
        if (paiment_array != null) {
            for (int i = 0; i < paiment_array.length; i++) {
                if (param.equals(paiment_array[i])) {
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
