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
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.entity.TeacherInfo;
import com.example.myapplication.jiaowu.service.StudentService;
import com.example.myapplication.jiaowu.service.TeacherInfoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class JiaoShiXinXiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private Teacher teacher;
    private TeacherInfoService teacherInfoService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private EditText teacher_name;
    private String teacher_nameText;
    private Button sel_button;
    List<TeacherInfo> list;
    private Quanxian quanxian;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_jiaoshixinxi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.jiaoshixinxi_list);

        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);

        teacher_name = findViewById(R.id.teacher_name);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

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
        sel_button.setEnabled(false);
        teacher_nameText = teacher_name.getText().toString();
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
                    teacherInfoService = new TeacherInfoService();
                    list = teacherInfoService.getList(teacher.getCompany(),teacher_nameText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("t_name", list.get(i).getT_name());
                        item.put("sex", list.get(i).getSex());
                        item.put("id_code", list.get(i).getId_code());
                        item.put("minzu", list.get(i).getMinzu());
                        item.put("birthday", list.get(i).getBirthday());
                        item.put("post", list.get(i).getPost());
                        item.put("education", list.get(i).getEducation());
                        item.put("phone", list.get(i).getPhone());
                        item.put("rz_riqi", list.get(i).getRz_riqi());
                        item.put("state", list.get(i).getState());
                        item.put("shebao", list.get(i).getShebao());
                        item.put("address", list.get(i).getAddress());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(JiaoShiXinXiActivity.this, data, R.layout.jiaowu_jiaoshixinxi_row, new String[]{"t_name","sex","id_code","minzu","birthday","post","education","phone","rz_riqi","state","shebao","address"}, new int[]{R.id.t_name, R.id.sex, R.id.id_code, R.id.minzu, R.id.birthday, R.id.post, R.id.education, R.id.phone, R.id.rz_riqi, R.id.state, R.id.shebao, R.id.address}) {
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

                adapter_block = new SimpleAdapter(JiaoShiXinXiActivity.this, data, R.layout.jiaowu_jiaoshixinxi_row_block, new String[]{"t_name","sex","id_code","minzu","birthday","post","education","phone","rz_riqi","state","shebao","address"}, new int[]{R.id.t_name, R.id.sex, R.id.id_code, R.id.minzu, R.id.birthday, R.id.post, R.id.education, R.id.phone, R.id.rz_riqi, R.id.state, R.id.shebao, R.id.address}) {
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
            ToastUtil.show(JiaoShiXinXiActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(JiaoShiXinXiActivity.this, JiaoShiXinXiChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!quanxian.getUpd().equals("√")){
                    ToastUtil.show(JiaoShiXinXiActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(JiaoShiXinXiActivity.this, JiaoShiXinXiChangeActivity.class);
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
                    ToastUtil.show(JiaoShiXinXiActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(JiaoShiXinXiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(JiaoShiXinXiActivity.this, "删除成功");
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
                                msg.obj = teacherInfoService.delete(list.get(position).getId());
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(JiaoShiXinXiActivity.this, new DatePickerDialog.OnDateSetListener() {
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
