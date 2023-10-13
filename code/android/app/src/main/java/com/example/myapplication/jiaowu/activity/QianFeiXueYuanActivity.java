package com.example.myapplication.jiaowu.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.StudentService;
import com.example.myapplication.utils.ExcelUtil;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class QianFeiXueYuanActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private Teacher teacher;
    private StudentService studentService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private EditText student_name;
    private String student_nameText;
    private Button sel_button;
    private Button export_button;
    List<Student> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_qianfeixueyuan);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.qianfeixueyuan_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        student_name = findViewById(R.id.student_name);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        export_button = findViewById(R.id.export_button);
        export_button.setOnClickListener(exportClick());

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initList() {
        sel_button.setEnabled(false);
        student_nameText = student_name.getText().toString();
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
                    studentService = new StudentService();
                    list = studentService.getListQianFei(teacher.getCompany(),student_nameText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("realName", list.get(i).getRealname());
                        item.put("nocost", list.get(i).getNocost());
                        item.put("rgdate", list.get(i).getRgdate());
                        item.put("course", list.get(i).getCourse());
                        item.put("teacher", list.get(i).getTeacher());
                        item.put("classnum", list.get(i).getClassnum());
                        item.put("phone", list.get(i).getPhone());
                        item.put("nohour", list.get(i).getNohour());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(QianFeiXueYuanActivity.this, data, R.layout.jiaowu_qianfeixueyuan_row, new String[]{"realName","nocost","rgdate","course","teacher","classnum","phone","nohour"}, new int[]{R.id.realName, R.id.nocost, R.id.rgdate, R.id.course, R.id.teacher, R.id.classnum, R.id.phone, R.id.nohour}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(QianFeiXueYuanActivity.this, data, R.layout.jiaowu_qianfeixueyuan_row_block, new String[]{"realName","nocost","rgdate","course","teacher","classnum","phone","nohour"}, new int[]{R.id.realName, R.id.nocost, R.id.rgdate, R.id.course, R.id.teacher, R.id.classnum, R.id.phone, R.id.nohour}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
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

    public View.OnClickListener exportClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] title = {"学生姓名", "欠费金额", "报名日期", "培训课程", "责任教师", "班级", "电话", "剩余课时"};
                String fileName = "欠费学员" + System.currentTimeMillis() + ".xls";
                ExcelUtil.initExcel(fileName, "欠费学员", title);
                ExcelUtil.jiaowu_qianfeixueyuanToExcel(list, fileName, MyApplication.getContext());
                String filePath = null;
                try {
                    filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统/" + fileName;
                    Uri uri = Uri.parse("file://" + filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(QianFeiXueYuanActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
