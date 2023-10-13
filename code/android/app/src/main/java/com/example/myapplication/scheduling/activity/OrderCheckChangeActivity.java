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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.OrderCheck;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.OrderCheckService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.Calendar;

public class OrderCheckChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private OrderCheck orderCheck;
    private OrderCheckService orderCheckService;

    private EditText order_number;
    private EditText moudle;
    private EditText riqi;
    private EditText num;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_check_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        orderCheckService = new OrderCheckService();

        order_number = findViewById(R.id.order_number);
        moudle = findViewById(R.id.moudle);
        riqi = findViewById(R.id.riqi);
        num = findViewById(R.id.num);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            orderCheck = new OrderCheck();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            orderCheck = (OrderCheck) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            order_number.setText(orderCheck.getOrder_number());
            moudle.setText(orderCheck.getMoudle());
            riqi.setText(orderCheck.getRiqi());
            String num_text = orderCheck.getNum() + "";
            num.setText(num_text);
        }

        showDateOnClick(riqi);

    }

    public void clearClick(View v) {
        order_number.setText("");
        moudle.setText("");
        riqi.setText("");
        num.setText("");
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
                    ToastUtil.show(OrderCheckChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(OrderCheckChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = orderCheckService.insert(orderCheck);
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
                    ToastUtil.show(OrderCheckChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(OrderCheckChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = orderCheckService.update(orderCheck);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (order_number.getText().toString().equals("")) {
            ToastUtil.show(OrderCheckChangeActivity.this, "请输入订单号");
            return false;
        } else {
            orderCheck.setOrder_number(order_number.getText().toString());
        }

        if (moudle.getText().toString().equals("")) {
            ToastUtil.show(OrderCheckChangeActivity.this, "请输入模块");
            return false;
        } else {
            orderCheck.setMoudle(moudle.getText().toString());
        }

        if (riqi.getText().toString().equals("")) {
            ToastUtil.show(OrderCheckChangeActivity.this, "请输入日期");
            return false;
        } else {
            orderCheck.setRiqi(riqi.getText().toString());
        }

        if (num.getText().toString().equals("")) {
            ToastUtil.show(OrderCheckChangeActivity.this, "请输入数量");
            return false;
        } else {
            orderCheck.setNum(Double.parseDouble(num.getText().toString()));
        }

        orderCheck.setCompany(userInfo.getCompany());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(OrderCheckChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

}
