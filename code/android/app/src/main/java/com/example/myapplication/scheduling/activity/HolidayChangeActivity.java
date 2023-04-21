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
import com.example.myapplication.scheduling.entity.Holiday;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.HolidayService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.Calendar;

public class HolidayChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private Holiday holiday;

    private HolidayService holidayService;

    private EditText riqi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holiday_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        holidayService = new HolidayService();

        riqi = findViewById(R.id.riqi);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            holiday = new Holiday();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            holiday = (Holiday) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            riqi.setText(holiday.getDay_or_reset());
        }

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

    public void insertClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(HolidayChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(HolidayChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = holidayService.insert(holiday);
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
                    ToastUtil.show(HolidayChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(HolidayChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = holidayService.update(holiday);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (riqi.getText().toString().equals("")) {
            ToastUtil.show(HolidayChangeActivity.this, "请输入休息日");
            return false;
        } else {
            holiday.setDay_or_reset(riqi.getText().toString());
        }

        holiday.setCompany(userInfo.getCompany());
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(HolidayChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
