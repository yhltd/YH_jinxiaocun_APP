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
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceJiJianTaiZhangService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceSimpleAccountingService;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Student;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.jiaowu.service.StudentService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class XueShengXinXiChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private Student student;
    private StudentService studentService;
    private SheZhiService sheZhiService;

    private EditText realName;
    private Spinner sex;
    private EditText rgdate;
    private Spinner course;
    private Spinner teacher_sel;
    private EditText classnum;
    private EditText phone;
    private EditText fee;
    private EditText allhour;
    private Spinner type;

    String[] sex_array;
    String[] course_array;
    String[] teacher_array;
    String[] type_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_xueshengxinxi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        studentService = new StudentService();
        sheZhiService = new SheZhiService();

        realName = findViewById(R.id.realName);
        sex = findViewById(R.id.sex);
        rgdate = findViewById(R.id.rgdate);
        course = findViewById(R.id.course);
        teacher_sel = findViewById(R.id.teacher);
        classnum = findViewById(R.id.classnum);
        phone = findViewById(R.id.phone);
        fee = findViewById(R.id.fee);
        allhour = findViewById(R.id.allhour);
        type = findViewById(R.id.type);

        String[] sex_selectArray = getResources().getStringArray(R.array.sex_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sex_selectArray);
        sex.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            student = (Student) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            realName.setText(student.getRealName().toString());
            rgdate.setText(student.getRgdate().toString());
            classnum.setText(student.getClassnum().toString());
            phone.setText(student.getPhone().toString());
            fee.setText(student.getFee().toString());
            allhour.setText(student.getAllhour().toString());
        }else{
            student = new Student();
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
                SpinnerAdapter adapter_course = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
                course.setAdapter(StringUtils.cast(adapter_course));
                course.setSelection(getCoursePosition(student.getCourse()));

                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                teacher_sel.setAdapter(StringUtils.cast(adapter_teacher));
                teacher_sel.setSelection(getTeacherPosition(student.getTeacher()));

                SpinnerAdapter adapter_type = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, type_array);
                type.setAdapter(StringUtils.cast(adapter_type));
                type.setSelection(getTypePosition(student.getType()));
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
                        type_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            type_array[i] = courseList.get(i).getType();
                        }
                    }
                    adapter = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
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
                SpinnerAdapter adapter_course = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
                course.setAdapter(StringUtils.cast(adapter_course));

                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
                teacher_sel.setAdapter(StringUtils.cast(adapter_teacher));

                SpinnerAdapter adapter_type = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, type_array);
                type.setAdapter(StringUtils.cast(adapter_type));
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
                        type_array = new String[courseList.size()];
                        for (int i = 0; i < courseList.size(); i++) {
                            type_array[i] = courseList.get(i).getType();
                        }
                    }
                    adapter = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
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
                    ToastUtil.show(XueShengXinXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(XueShengXinXiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = studentService.update(student);
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
                    ToastUtil.show(XueShengXinXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(XueShengXinXiChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = studentService.insert(student);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {
        if (realName.getText().toString().equals("")) {
            ToastUtil.show(XueShengXinXiChangeActivity.this, "请输入学生姓名");
            return false;
        } else {
            student.setRealName(realName.getText().toString());
        }

        if (rgdate.getText().toString().equals("")) {
            ToastUtil.show(XueShengXinXiChangeActivity.this, "请输入日期");
            return false;
        } else {
            student.setRgdate(rgdate.getText().toString());
        }

        if (course.getSelectedItem().toString().equals("")) {
            ToastUtil.show(XueShengXinXiChangeActivity.this, "请选择课程");
            return false;
        } else {
            student.setCourse(course.getSelectedItem().toString());
        }

        if (teacher_sel.getSelectedItem().toString().equals("")) {
            ToastUtil.show(XueShengXinXiChangeActivity.this, "请选择责任教师");
            return false;
        } else {
            student.setTeacher(teacher_sel.getSelectedItem().toString());
        }

        realName = findViewById(R.id.realName);
        sex = findViewById(R.id.sex);
        rgdate = findViewById(R.id.rgdate);
        course = findViewById(R.id.course);
        teacher_sel = findViewById(R.id.teacher);
        classnum = findViewById(R.id.classnum);
        phone = findViewById(R.id.phone);
        fee = findViewById(R.id.fee);
        allhour = findViewById(R.id.allhour);
        type = findViewById(R.id.type);

        student.setSex(sex.getSelectedItem().toString());
        student.setRgdate(rgdate.getText().toString());
        student.setClassnum(classnum.getText().toString());
        student.setPhone(phone.getText().toString());
        student.setFee(fee.getText().toString());
        student.setAllhour(allhour.getText().toString());
        student.setType(type.getSelectedItem().toString());

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

    private int getTypePosition(String param) {
        if (type_array != null) {
            for (int i = 0; i < type_array.length; i++) {
                if (param.equals(type_array[i])) {
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
