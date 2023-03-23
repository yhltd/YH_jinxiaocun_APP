package com.example.myapplication.jiaowu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jiaowu.entity.KeShiDetail;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.KeShiDetailService;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.jiaowu.service.StudentService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.List;

public class KeShiTongJiChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private KeShiDetail keShiDetail;
    private KeShiDetailService keShiDetailService;
    private SheZhiService sheZhiService;

    private EditText riqi;
    private EditText student_name;
    private Spinner course;
    private EditText keshi;
    private Spinner teacher_name;
    private EditText jine;

    String[] course_array;
    String[] teacher_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_keshitongji_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        keShiDetailService = new KeShiDetailService();
        sheZhiService = new SheZhiService();

        riqi = findViewById(R.id.riqi);
        student_name = findViewById(R.id.student_name);
        course = findViewById(R.id.course);
        keshi = findViewById(R.id.keshi);
        teacher_name = findViewById(R.id.teacher_name);
        jine = findViewById(R.id.jine);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            keShiDetail = (KeShiDetail) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            riqi.setText(keShiDetail.getRiqi().toString());
            student_name.setText(keShiDetail.getStudent_name().toString());
            keshi.setText(keShiDetail.getKeshi().toString());
            jine.setText(keShiDetail.getJine().toString());

        }else{
            keShiDetail = new KeShiDetail();
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
                SpinnerAdapter adapter_course = new ArrayAdapter<String>(KeShiTongJiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
                course.setAdapter(StringUtils.cast(adapter_course));
                course.setSelection(getCoursePosition(keShiDetail.getCourse()));

                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(KeShiTongJiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                teacher_name.setAdapter(StringUtils.cast(adapter_teacher));
                teacher_name.setSelection(getTeacherPosition(keShiDetail.getTeacher_name()));
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
                        course_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            course_array[i] = courseList.get(i).getCourse();
                        }
                        teacher_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            teacher_array[i] = courseList.get(i).getTeacher();
                        }
                    }
                    adapter = new ArrayAdapter<String>(KeShiTongJiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
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
                SpinnerAdapter adapter_course = new ArrayAdapter<String>(KeShiTongJiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
                course.setAdapter(StringUtils.cast(adapter_course));

                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(KeShiTongJiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                teacher_name.setAdapter(StringUtils.cast(adapter_teacher));
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
                        course_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            course_array[i] = courseList.get(i).getCourse();
                        }
                        teacher_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            teacher_array[i] = courseList.get(i).getTeacher();
                        }
                    }
                    adapter = new ArrayAdapter<String>(KeShiTongJiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
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
                    ToastUtil.show(KeShiTongJiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KeShiTongJiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = keShiDetailService.update(keShiDetail);
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
                    ToastUtil.show(KeShiTongJiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KeShiTongJiChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = keShiDetailService.insert(keShiDetail);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() throws ParseException {

        if (student_name.getText().toString().equals("")) {
            ToastUtil.show(KeShiTongJiChangeActivity.this, "请输入学生姓名");
            return false;
        } else {
            keShiDetail.setStudent_name(student_name.getText().toString());
        }

        if (course.getSelectedItem().toString().equals("")) {
            ToastUtil.show(KeShiTongJiChangeActivity.this, "请输入课时");
            return false;
        } else {
            keShiDetail.setCourse(course.getSelectedItem().toString());
        }

        if (teacher_name.getSelectedItem().toString().equals("")) {
            ToastUtil.show(KeShiTongJiChangeActivity.this, "请选择教师");
            return false;
        } else {
            keShiDetail.setTeacher_name(teacher_name.getSelectedItem().toString());
        }


        keShiDetail.setRiqi(riqi.getText().toString());
        keShiDetail.setKeshi(keshi.getText().toString());
        keShiDetail.setJine(jine.getText().toString());
        keShiDetail.setCompany(teacher.getCompany());
        return true;
    }


    private int getCoursePosition(String param) {
        if (course_array != null) {
            for (int i = 0; i < course_array.length; i++) {
                if (param.equals(course_array[i])) {
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


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
