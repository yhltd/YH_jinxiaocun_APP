package com.example.myapplication.jiaowu.activity;

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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.entity.TeacherInfo;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.jiaowu.service.StudentService;
import com.example.myapplication.jiaowu.service.TeacherInfoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class JiaoShiXinXiChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private TeacherInfo teacherInfo;
    private TeacherInfoService teacherInfoService;
    private SheZhiService sheZhiService;

    private Spinner t_name;
    private Spinner sex;
    private EditText id_code;
    private EditText minzu;
    private EditText birthday;
    private EditText post;
    private EditText education;
    private EditText phone;
    private EditText rz_riqi;
    private Spinner state;
    private EditText shebao;
    private EditText address;

    String[] sex_array;
    String[] teacher_array;
    String[] state_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_jiaoshixinxi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        teacherInfoService = new TeacherInfoService();
        sheZhiService = new SheZhiService();

        t_name = findViewById(R.id.t_name);
        sex = findViewById(R.id.sex);
        id_code = findViewById(R.id.id_code);
        minzu = findViewById(R.id.minzu);
        birthday = findViewById(R.id.birthday);
        showDateOnClick(birthday);
        post = findViewById(R.id.post);
        education = findViewById(R.id.education);
        phone = findViewById(R.id.phone);
        rz_riqi = findViewById(R.id.rz_riqi);
        showDateOnClick(rz_riqi);
        state = findViewById(R.id.state);
        shebao = findViewById(R.id.shebao);
        address = findViewById(R.id.address);

        sex_array = getResources().getStringArray(R.array.sex_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sex_array);
        sex.setAdapter(adapter);

        state_array = getResources().getStringArray(R.array.state_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, state_array);
        state.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            teacherInfo = (TeacherInfo) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            id_code.setText(teacherInfo.getId_code().toString());
            minzu.setText(teacherInfo.getMinzu());
            birthday.setText(teacherInfo.getBirthday());
            post.setText(teacherInfo.getPost().toString());
            education.setText(teacherInfo.getEducation().toString());
            phone.setText(teacherInfo.getPhone().toString());
            rz_riqi.setText(teacherInfo.getRz_riqi().toString());
            shebao.setText(teacherInfo.getShebao().toString());
            address.setText(teacherInfo.getAddress().toString());
        }else{
            teacherInfo = new TeacherInfo();
            init2();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
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


    public void init1() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                sex.setSelection(getSexPosition(teacherInfo.getSex()));

                state.setSelection(getStatePosition(teacherInfo.getState()));

                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(JiaoShiXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                t_name.setAdapter(StringUtils.cast(adapter_teacher));
                t_name.setSelection(getTeacherPosition(teacherInfo.getT_name()));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<SheZhi> courseList = sheZhiService.getList(teacher.getCompany());
                    if (courseList.size() > 0) {
                        teacher_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            teacher_array[i] = courseList.get(i).getTeacher();
                        }
                    }
                    adapter = new ArrayAdapter<String>(JiaoShiXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void init2() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(JiaoShiXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                t_name.setAdapter(StringUtils.cast(adapter_teacher));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<SheZhi> courseList = sheZhiService.getList(teacher.getCompany());
                    if (courseList.size() > 0) {
                        teacher_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            teacher_array[i] = courseList.get(i).getTeacher();
                        }
                    }
                    adapter = new ArrayAdapter<String>(JiaoShiXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiaoShiXinXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiaoShiXinXiChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = teacherInfoService.update(teacherInfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiaoShiXinXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiaoShiXinXiChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = teacherInfoService.insert(teacherInfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() throws ParseException {

        if (t_name.getSelectedItem().toString().equals("")) {
            ToastUtil.show(JiaoShiXinXiChangeActivity.this, "请选择教师");
            return false;
        } else {
            teacherInfo.setT_name(t_name.getSelectedItem().toString());
        }

        teacherInfo.setSex(sex.getSelectedItem().toString());
        teacherInfo.setId_code(id_code.getText().toString());
        teacherInfo.setMinzu(minzu.getText().toString());
        teacherInfo.setBirthday(birthday.getText().toString());
        teacherInfo.setPost(post.getText().toString());
        teacherInfo.setEducation(education.getText().toString());
        teacherInfo.setPhone(phone.getText().toString());
        teacherInfo.setRz_riqi(rz_riqi.getText().toString());
        teacherInfo.setState(state.getSelectedItem().toString());
        teacherInfo.setShebao(shebao.getText().toString());
        teacherInfo.setAddress(address.getText().toString());
        teacherInfo.setCompany(teacher.getCompany());
        return true;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(JiaoShiXinXiChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private int getSexPosition(String param) {
        if (sex_array != null) {
            for (int i = 0; i < sex_array.length; i++) {
                if (param.equals(sex_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getTeacherPosition(String param) {
        if (teacher_array != null) {
            for (int i = 0; i < teacher_array.length; i++) {
                if (param.equals(teacher_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getStatePosition(String param) {
        if (state_array != null) {
            for (int i = 0; i < state_array.length; i++) {
                if (param.equals(state_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
