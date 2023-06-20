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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.EChartOptionUtil;
import com.example.myapplication.EChartView;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.activity.RenYuanChartActivity;
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.Copy1Service;
import com.example.myapplication.fenquan.service.DepartmentService;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.fenquan.service.WorkbenchService;
import com.example.myapplication.scheduling.entity.LineChart;
import com.example.myapplication.scheduling.entity.OrderInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.OrderInfoService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderChartActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private OrderInfoService orderInfoService;

    private EditText start_date;
    private EChartView lineChart;

    private String start_dateText;

    private Button sel_button;

    private Object[] data1;
    private Object[] data2;
    List<LineChart> list;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_chart);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        start_date = findViewById(R.id.start_date);
        sel_button = findViewById(R.id.sel_button);
        lineChart = findViewById(R.id.lineChart);
        showDateOnClick(start_date);

        sel_button.setOnClickListener(selClick());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    private void initList() {
        sel_button.setEnabled(false);
        start_dateText = start_date.getText().toString();

        if(start_dateText.equals("")){
            start_dateText = "2023";
        }
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                lineChart.refreshEchartsWithOption(EChartOptionUtil.getLineChartOptions(data1, data2,"数量"));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    orderInfoService = new OrderInfoService();
                    list = orderInfoService.getLineChart(start_dateText,userInfo.getCompany());
                    data1 = new Object[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
                    if(list == null){
                        data2 = new Object[]{0,0,0,0,0,0,0,0,0,0,0,0};
                    }else{
                        data2 = new Object[]{list.get(0).getMonth1(),list.get(0).getMonth2(),list.get(0).getMonth3(),list.get(0).getMonth4(),list.get(0).getMonth5(),list.get(0).getMonth6(),list.get(0).getMonth7(),list.get(0).getMonth8(),list.get(0).getMonth9(),list.get(0).getMonth10(),list.get(0).getMonth11(),list.get(0).getMonth12()};
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.obj = "";
                listLoadHandler.sendMessage(msg);
            }
        }).start();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(OrderChartActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year +"");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}
