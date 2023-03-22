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
import com.example.myapplication.finance.activity.BaoBiaoActivity;
import com.example.myapplication.finance.activity.JiJianTaiZhangChangeActivity;
import com.example.myapplication.finance.activity.ZhangHaoGuanLiChangeActivity;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.jiaowu.entity.Kaoqin;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.entity.TeacherCurriculum;
import com.example.myapplication.jiaowu.service.KaoqinService;
import com.example.myapplication.jiaowu.service.TeacherCurriculumService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class TeacherCurriculumChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private TeacherCurriculum teacherCurriculum;
    private TeacherCurriculumService teacherCurriculumService;

    private EditText teacher1;
    private Spinner course;
    private EditText riqi;
    private Spinner xingqi;
    private String teacher1Text;
    private String courseText;
    private String riqiText;
    private String xingqiText;

    String[] course_array;
    String[] xingqi_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_teachercurriculum_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        teacherCurriculumService = new TeacherCurriculumService();

        teacher1 = findViewById(R.id.teacher);
        course = findViewById(R.id.course);
        riqi = findViewById(R.id.riqi);
        xingqi = findViewById(R.id.xingqi);

        riqi = findViewById(R.id.riqi);
        showDateOnClick(riqi);

        course_array = getResources().getStringArray(R.array.fapiao_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, course_array);
        course.setAdapter(adapter);
        xingqi_array = getResources().getStringArray(R.array.xingqi_array);
        ArrayAdapter<String> adapter_xingqi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, xingqi_array);
        xingqi.setAdapter(adapter_xingqi);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            teacher = new Teacher();
            init2();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            teacher = (Teacher) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            init1();
        }


    }

    public void init1() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_course = new ArrayAdapter<String>(TeacherCurriculumChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
                course.setAdapter(StringUtils.cast(adapter_course));
                course.setSelection(getCoursePosition(teacherCurriculum.getCourse()));
                SpinnerAdapter adapter_xingqi = new ArrayAdapter<String>(TeacherCurriculumChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, xingqi_array);
                xingqi.setAdapter(StringUtils.cast(adapter_xingqi));
                xingqi.setSelection(getXingqiPosition(teacherCurriculum.getXingqi()));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<TeacherCurriculum> CourseList = teacherCurriculumService.getList(teacher1Text,courseText,teacher.getCompany());
                    if (CourseList.size() > 0) {
                        course_array = new String[CourseList.size()];
                        for (int i = 0; i < CourseList.size(); i++) {
                            course_array[i] = CourseList.get(i).getCourse();
                        }
                    }
                    List<TeacherCurriculum> XingList = teacherCurriculumService.getList(teacher1Text,courseText,teacher.getCompany());
                    if (XingList.size() > 0) {
                        xingqi_array = new String[XingList.size()];
                        for (int i = 0; i < XingList.size(); i++) {
                            xingqi_array[i] = XingList.get(i).getXingqi();
                        }
                    }
                    adapter = new ArrayAdapter<String>(TeacherCurriculumChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
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
                SpinnerAdapter adapter_course = new ArrayAdapter<String>(TeacherCurriculumChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
                course.setAdapter(StringUtils.cast(adapter_course));
                SpinnerAdapter adapter_xingqi = new ArrayAdapter<String>(TeacherCurriculumChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, xingqi_array);
                xingqi.setAdapter(StringUtils.cast(adapter_xingqi));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<TeacherCurriculum> CourseList = teacherCurriculumService.getList(teacher1Text,courseText,teacherCurriculum.getCompany());
                    if (CourseList.size() > 0) {
                        course_array = new String[CourseList.size()];
                        for (int i = 0; i < CourseList.size(); i++) {
                            course_array[i] = CourseList.get(i).getCourse();
                        }
                    }
                    List<TeacherCurriculum> XingList = teacherCurriculumService.getList(teacher1Text,courseText,teacherCurriculum.getCompany());
                    if (XingList.size() > 0) {
                        xingqi_array = new String[XingList.size()];
                        for (int i = 0; i < XingList.size(); i++) {
                            xingqi_array[i] = XingList.get(i).getXingqi();
                        }
                    }
                    adapter = new ArrayAdapter<String>(TeacherCurriculumChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
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

    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(TeacherCurriculumChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(TeacherCurriculumChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = teacherCurriculumService.insert(teacherCurriculum);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(TeacherCurriculumChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(TeacherCurriculumChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = teacherCurriculumService.update(teacherCurriculum);
                saveHandler.sendMessage(msg);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(TeacherCurriculumChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private boolean checkForm() throws ParseException {

        if (teacher1.getText().toString().equals("")) {
            ToastUtil.show(TeacherCurriculumChangeActivity.this, "请输入教师");
            return false;
        } else {
            teacherCurriculum.setTeacher(teacher1.getText().toString());
        }

        if (riqi.getText().toString().equals("")) {
            ToastUtil.show(TeacherCurriculumChangeActivity.this, "请输入日期");
            return false;
        } else {
            teacherCurriculum.setRiqi(riqi.getText().toString());
        }


        teacher.setCompany(teacher.getCompany());

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

    private int getXingqiPosition(String param) {
        if (xingqi_array != null) {
            for (int i = 0; i < xingqi_array.length; i++) {
                if (param.equals(xingqi_array[i])) {
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
