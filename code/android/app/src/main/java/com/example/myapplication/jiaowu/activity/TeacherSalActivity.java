package com.example.myapplication.jiaowu.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.activity.GongZuoTaiActivity;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.entity.TeacherInfo;
import com.example.myapplication.jiaowu.entity.TeacherSal;
import com.example.myapplication.jiaowu.service.AccountManagementService;
import com.example.myapplication.jiaowu.service.TeacherSalService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TeacherSalActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    private Teacher teacher;
    private TeacherSalService teacherSalService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private EditText start_date;
    private EditText end_date;
    private EditText teacher_name;
    private String start_dateText;
    private String end_dateText;
    private String teacher_nameText;
    private Button sel_button;

    List<TeacherSal> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_teachersal);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.teachersal_list);

        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);

        start_date = findViewById(R.id.startdate);
        end_date = findViewById(R.id.enddate);
        showDateOnClick(start_date);
        showDateOnClick(end_date);
        teacher_name = findViewById(R.id.teacher_name);
        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        initList();
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

    @SuppressLint("WrongConstant")
    public void switchClick(View v) {
        if(listView_block.getVisibility() == 0){
            listView_block.setVisibility(8);
            list_table.setVisibility(0);
        }else if(listView_block.getVisibility() == 8){
            listView_block.setVisibility(0);
            list_table.setVisibility(8);
        }

    }

    private void initList() {

        start_dateText = start_date.getText().toString();
        end_dateText = end_date.getText().toString();
        teacher_nameText = teacher_name.getText().toString();

        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }

        if(end_dateText.equals("")){
            end_dateText = "2100-12-31";
        }

        if(start_dateText.compareTo(end_dateText) > 0){
            ToastUtil.show(TeacherSalActivity.this, "开始日期不能晚于结束日期");
            return;
        }
        sel_button.setEnabled(false);
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    teacherSalService = new TeacherSalService();
                    list = teacherSalService.getList(start_dateText,end_dateText,teacher_nameText, teacher.getCompany());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("teacher_name", list.get(i).getTeacher_name());
                        item.put("course", list.get(i).getCourse());
                        item.put("keshi", list.get(i).getKeshi());
                        item.put("jine", list.get(i).getJine());
                        item.put("gongzihesuan", list.get(i).getGongzihesuan());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(TeacherSalActivity.this, data, R.layout.jiaowu_teachersal_row, new String[]{"teacher_name", "course", "keshi", "jine", "gongzihesuan"}, new int[]{R.id.teacher_name, R.id.course, R.id.keshi, R.id.jine, R.id.gongzihesuan}) {
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

                adapter_block = new SimpleAdapter(TeacherSalActivity.this, data, R.layout.jiaowu_teachersal_row_block, new String[]{"teacher_name", "course", "keshi", "jine", "gongzihesuan"}, new int[]{R.id.teacher_name, R.id.course, R.id.keshi, R.id.jine, R.id.gongzihesuan}) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                initList();
            }
        }
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(TeacherSalActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String mon = "";
                String day = "";
                if(monthOfYear + 1 < 10){
                    mon = "0" + (monthOfYear + 1);
                }else{
                    mon = "" + (monthOfYear + 1);
                }
                if(dayOfMonth < 10){
                    day = "0" + dayOfMonth;
                }else{
                    day = "" + dayOfMonth;
                }
                editText.setText(year + "-" + mon + "-" + day);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
