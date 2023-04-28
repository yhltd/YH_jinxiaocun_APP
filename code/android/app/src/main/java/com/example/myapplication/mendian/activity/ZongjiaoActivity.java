package com.example.myapplication.mendian.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.entity.YhMendianYuejiao;
import com.example.myapplication.mendian.entity.YhMendianZongjiao;
import com.example.myapplication.mendian.service.YhMendianYuejiaoService;
import com.example.myapplication.mendian.service.YhMendianZongjiaoService;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ZongjiaoActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianZongjiaoService yhMendianZongjiaoService;
    private EditText start_date;
    private EditText end_date;
    private String start_dateText;
    private String end_dateText;
    private ListView Zongjiao_list;
    private Button sel_button;

    List<YhMendianZongjiao> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zongjiao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhMendianZongjiaoService = new YhMendianZongjiaoService();

        //初始化控件
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        Zongjiao_list = findViewById(R.id.Zongjiao_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        showDateOnClick(start_date);
        showDateOnClick(end_date);
        initList();
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
        start_dateText = start_date.getText().toString();
        end_dateText = end_date.getText().toString();

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Zongjiao_list.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhMendianZongjiaoService = new YhMendianZongjiaoService();
                    list = yhMendianZongjiaoService.getList(start_dateText,end_dateText,yhMendianUser.getCompany());
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("riqi", list.get(i).getRiqi());
                    item.put("jiaoyie", list.get(i).getJiaoyie());
                    item.put("yishuae", list.get(i).getYishuae());
                    item.put("shouxufei", list.get(i).getShouxufei());
                    item.put("profit", list.get(i).getProfit());
                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(ZongjiaoActivity.this, data, R.layout.zongjiao_row, new String[]{"recipient","cardholder","drawee"}, new int[]{R.id.recipient,R.id.cardholder,R.id.drawee}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
//                        linearLayout.setOnLongClickListener(onItemLongClick());
//                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };
                Message msg = new Message();
                msg.obj = adapter;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ZongjiaoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String this_month = "";
                if (monthOfYear + 1 < 10){
                    this_month = "0" + String.format("%s",monthOfYear + 1);
                }else{
                    this_month = String.format("%s",monthOfYear + 1);
                }

                String this_day = "";
                if (dayOfMonth + 1 < 10){
                    this_day = "0" + String.format("%s",dayOfMonth);
                }else{
                    this_day = String.format("%s",dayOfMonth);
                }
                editText.setText(year + "-" + this_month + "-" + this_day);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                initList();
            }
        }
    }
}
