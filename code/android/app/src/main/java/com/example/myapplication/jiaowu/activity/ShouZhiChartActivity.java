package com.example.myapplication.jiaowu.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.EChartOptionUtil;
import com.example.myapplication.EChartView;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.entity.Workbench;
import com.example.myapplication.fenquan.service.WorkbenchService;
import com.example.myapplication.jiaowu.entity.ShouZhiMingXi;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.ShouZhiMingXiService;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


@SuppressLint("SetJavaScriptEnabled")
public class ShouZhiChartActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private Teacher teacher;
    private ShouZhiMingXiService shouZhiMingXiService;
    private EditText start_date;
    private EditText stop_date;
    private String start_dateText;
    private String stop_dateText;
    private EChartView barChart;
    List<ShouZhiMingXi> list;
    private Object[] data1;
    private Object[] data2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shouzhi_chart);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        barChart = findViewById(R.id.barChart);

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        initList();
    }

    private void initList() {
        start_dateText = "";
        stop_dateText = "";
        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100-12-31";
        }
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Intent intent = getIntent();
                String[] data1 = intent.getStringArrayExtra("data1");
                Object[] data2 = new Object[] {intent.getIntExtra("shouru",0),intent.getIntExtra("zhichu",0),intent.getIntExtra("xuefei",0),intent.getIntExtra("jieyu",0)};
                barChart.refreshEchartsWithOption(EChartOptionUtil.getBarChartOptions(data1, data2));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    shouZhiMingXiService = new ShouZhiMingXiService();
                    list = shouZhiMingXiService.getList(start_dateText,stop_dateText,teacher.getCompany());
                    if (list == null) return;
                    int shouru = 0;
                    int zhichu = 0;
                    int xuefei = 0;
                    int jieyu = 0;
                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("rgdate", list.get(i).getRgdate());
                        item.put("money", list.get(i).getMoney());
                        item.put("msort", list.get(i).getMsort());
                        item.put("mremark", list.get(i).getMremark());
                        item.put("paid", list.get(i).getPaid());
                        item.put("psort", list.get(i).getPsort());
                        item.put("premark", list.get(i).getPremark());
                        item.put("handle", list.get(i).getHandle());
                        data.add(item);
                        shouru = shouru + list.get(i).getMoney();
                        zhichu = zhichu + list.get(i).getPaid();
                        if(list.get(i).getMsort().equals("学费")){
                            xuefei = xuefei + list.get(i).getMoney();
                        }
                    }
                    jieyu = shouru - zhichu;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.obj = "";
                listLoadHandler.sendMessage(msg);

            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ShouZhiChartActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}
