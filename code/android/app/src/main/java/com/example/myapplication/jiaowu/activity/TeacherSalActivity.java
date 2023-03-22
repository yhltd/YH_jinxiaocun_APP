package com.example.myapplication.jiaowu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.entity.TeacherInfo;
import com.example.myapplication.jiaowu.entity.TeacherSal;
import com.example.myapplication.jiaowu.service.AccountManagementService;
import com.example.myapplication.jiaowu.service.TeacherSalService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TeacherSalActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    private Teacher teacher;
    private TeacherSalService teacherSalService;
    private ListView listView;
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

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();

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

    private void initList() {
        start_dateText = start_date.getText().toString();
        end_dateText = end_date.getText().toString();

        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }

        if(end_dateText.equals("")){
            end_dateText = "2100-12-31";
        }



        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
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
                        item.put("id", list.get(i).getId());
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

                SimpleAdapter adapter = new SimpleAdapter(TeacherSalActivity.this, data, R.layout.jiaowu_teachersal_row, new String[]{"teacher_name", "course", "keshi", "jie", "gongzihesuan"}, new int[]{R.id.teacher_name, R.id.course, R.id.keshi, R.id.jine, R.id.gongzihesuan}) {
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
}
