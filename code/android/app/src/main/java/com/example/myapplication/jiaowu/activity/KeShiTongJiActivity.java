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
import com.example.myapplication.jiaowu.entity.KeShiDetail;
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.KeShiDetailService;
import com.example.myapplication.jiaowu.service.StudentService;
import com.example.myapplication.utils.ExcelUtil;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class KeShiTongJiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private Teacher teacher;
    private KeShiDetailService keShiDetailService;
    private ListView listView;
    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;
    private EditText start_date;
    private EditText stop_date;
    private EditText teacher_name;
    private EditText class_name;
    private String start_dateText;
    private String stop_dateText;
    private String teacher_nameText;
    private String class_nameText;
    private Button sel_button;
    private Button export_button;
    List<KeShiDetail> list;
    private Quanxian quanxian;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_keshitongji);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.keshitongji_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
        teacher_name = findViewById(R.id.teacher_name);
        class_name = findViewById(R.id.class_name);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        export_button = findViewById(R.id.export_button);
        export_button.setOnClickListener(exportClick());

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        quanxian = myApplication.getQuanxian();
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

        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();
        teacher_nameText = teacher_name.getText().toString();
        class_nameText = class_name.getText().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100-12-31";
        }

        if(start_dateText.compareTo(stop_dateText) > 0){
            ToastUtil.show(KeShiTongJiActivity.this, "开始日期不能晚于结束日期");
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
                    keShiDetailService = new KeShiDetailService();
                    list = keShiDetailService.getList(teacher.getCompany(),teacher_nameText,class_nameText,start_dateText,stop_dateText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("riqi", list.get(i).getRiqi());
                        item.put("student_name", list.get(i).getStudent_name());
                        item.put("course", list.get(i).getCourse());
                        item.put("keshi", list.get(i).getKeshi());
                        item.put("teacher_name", list.get(i).getTeacher_name());
                        item.put("jine", list.get(i).getJine());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(KeShiTongJiActivity.this, data, R.layout.jiaowu_keshitongji_row, new String[]{"riqi","student_name","course","keshi","teacher_name","jine"}, new int[]{R.id.riqi, R.id.student_name, R.id.course, R.id.keshi, R.id.teacher_name, R.id.jine}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(KeShiTongJiActivity.this, data, R.layout.jiaowu_keshitongji_row_block, new String[]{"riqi","student_name","course","keshi","teacher_name","jine"}, new int[]{R.id.riqi, R.id.student_name, R.id.course, R.id.keshi, R.id.teacher_name, R.id.jine}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
                        linearLayout.setOnClickListener(updateClick());
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

    public void onInsertClick(View v) {
        if(!quanxian.getAdd().equals("√")){
            ToastUtil.show(KeShiTongJiActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(KeShiTongJiActivity.this, KeShiTongJiChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener exportClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] title = {"日期", "学生姓名", "培训课程", "课时", "每节课时金额"};
                String fileName = "课时统计" + System.currentTimeMillis() + ".xls";
                ExcelUtil.initExcel(fileName, "课时统计", title);
                ExcelUtil.jiaowu_keshitongjiToExcel(list, fileName, MyApplication.getContext());
            }
        };
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!quanxian.getUpd().equals("√")){
                    ToastUtil.show(KeShiTongJiActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(KeShiTongJiActivity.this, KeShiTongJiChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!quanxian.getDel().equals("√")){
                    ToastUtil.show(KeShiTongJiActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(KeShiTongJiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(KeShiTongJiActivity.this, "删除成功");
                            initList();
                        }
                        return true;
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.obj = keShiDetailService.delete(list.get(position).getId());
                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(KeShiTongJiActivity.this, new DatePickerDialog.OnDateSetListener() {
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
