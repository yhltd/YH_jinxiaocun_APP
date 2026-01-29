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
import android.widget.AdapterView;
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
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
    private List<SheZhi> shezhiList;
    private HashMap<String, List<String>> courseTeacherMap = new HashMap<>();
//    private HashMap<String, List<String>> teacherCourseMap = new HashMap<>();
    private ArrayAdapter<String> courseAdapter;
    private ArrayAdapter<String> teacherAdapter;
    private List<String> courseNames = new ArrayList<>();
    private List<String> teacherNames = new ArrayList<>();
    String[] sex_array;
//    String[] course_array;
//    String[] teacher_array;
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
        showDateOnClick(rgdate);
        course = findViewById(R.id.course);
        teacher_sel = findViewById(R.id.teacher);
        classnum = findViewById(R.id.classnum);
        phone = findViewById(R.id.phone);
        fee = findViewById(R.id.fee);
        allhour = findViewById(R.id.allhour);
        type = findViewById(R.id.type);
        initSpinnerListeners();
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

            realName.setText(student.getRealname().toString());
            rgdate.setText(student.getRgdate().toString());
            classnum.setText(student.getClassnum().toString());
            phone.setText(student.getPhone().toString());
            fee.setText(String.valueOf(student.getFee()));
            allhour.setText(String.valueOf(student.getAllhour()));
        }else{
            student = new Student();
            init2();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }
    }

    //---0129
    private void initSpinnerListeners() {
        // 只保留课程选择监听
        course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem() != null) {
                    String selectedCourse = parent.getSelectedItem().toString();
                    updateTeacherSpinnerByCourse(selectedCourse);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 移除教师选择监听，不联动课程
        teacher_sel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 不做任何联动
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    public void init1() {
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                SpinnerAdapter adapter_course = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
//                course.setAdapter(StringUtils.cast(adapter_course));
//                course.setSelection(getCoursePosition(student.getCourse()));
//
//                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
//                teacher_sel.setAdapter(StringUtils.cast(adapter_teacher));
//                teacher_sel.setSelection(getTeacherPosition(student.getTeacher()));
//
//                SpinnerAdapter adapter_type = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, type_array);
//                type.setAdapter(StringUtils.cast(adapter_type));
//                type.setSelection(getTypePosition(student.getType()));
//                return true;
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SpinnerAdapter adapter = null;
//                try {
//                    List<SheZhi> courseList = sheZhiService.getList(teacher.getCompany());
//                    if (courseList.size() > 0) {
//                        course_array = new String[courseList.size()];
//                        for (int i = 0; i < courseList.size(); i++) {
//                            course_array[i] = courseList.get(i).getCourse();
//                        }
//                        teacher_array = new String[courseList.size()];
//                        for (int i = 0; i < courseList.size(); i++) {
//                            teacher_array[i] = courseList.get(i).getTeacher();
//                        }
//                        type_array = new String[courseList.size()];
//                        for (int i = 0; i < courseList.size(); i++) {
//                            type_array[i] = courseList.get(i).getType();
//                        }
//                    }
//                    adapter = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.obj = adapter;
//                listLoadHandler.sendMessage(msg);
//            }
//        }).start();
//    }
public void init1() {
    Handler listLoadHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // 初始化课程下拉框
            courseAdapter = new ArrayAdapter<>(XueShengXinXiChangeActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, courseNames);
            course.setAdapter(courseAdapter);
            course.setSelection(getCoursePosition(student.getCourse()));

            // 根据初始课程更新教师下拉框
            updateTeacherSpinnerByCourse(student.getCourse());

            // 初始化类型下拉框
            SpinnerAdapter adapter_type = new ArrayAdapter<>(XueShengXinXiChangeActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, type_array);
            type.setAdapter(StringUtils.cast(adapter_type));
            type.setSelection(getTypePosition(student.getType()));

            // 设置教师选择
            if (student.getTeacher() != null) {
                int teacherPosition = getTeacherPositionInAdapter(student.getTeacher());
                teacher_sel.setSelection(teacherPosition >= 0 ? teacherPosition : 0);
            }
            return true;
        }
    });

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                shezhiList = sheZhiService.getList(teacher.getCompany());
                if (shezhiList.size() > 0) {
                    // 构建映射关系
                    buildCourseTeacherMaps();

                    // 初始化类型数组
                    type_array = new String[shezhiList.size()];
                    for (int i = 0; i < shezhiList.size(); i++) {
                        type_array[i] = shezhiList.get(i).getType();
                    }
                }
                Message msg = new Message();
                listLoadHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }).start();
}
//    public void init2() {
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                SpinnerAdapter adapter_course = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
//                course.setAdapter(StringUtils.cast(adapter_course));
//
//                SpinnerAdapter adapter_teacher = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, teacher_array);
//                teacher_sel.setAdapter(StringUtils.cast(adapter_teacher));
//
//                SpinnerAdapter adapter_type = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, type_array);
//                type.setAdapter(StringUtils.cast(adapter_type));
//                return true;
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SpinnerAdapter adapter = null;
//                try {
//                    List<SheZhi> courseList = sheZhiService.getList(teacher.getCompany());
//                    if (courseList.size() > 0) {
//                        course_array = new String[courseList.size()];
//                        for (int i = 0; i < courseList.size(); i++) {
//                            course_array[i] = courseList.get(i).getCourse();
//                        }
//                        teacher_array = new String[courseList.size()];
//                        for (int i = 0; i < courseList.size(); i++) {
//                            teacher_array[i] = courseList.get(i).getTeacher();
//                        }
//                        type_array = new String[courseList.size()];
//                        for (int i = 0; i < courseList.size(); i++) {
//                            type_array[i] = courseList.get(i).getType();
//                        }
//                    }
//                    adapter = new ArrayAdapter<String>(XueShengXinXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, course_array);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.obj = adapter;
//                listLoadHandler.sendMessage(msg);
//            }
//        }).start();
//    }
public void init2() {
    Handler listLoadHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // 初始化课程下拉框
            courseAdapter = new ArrayAdapter<>(XueShengXinXiChangeActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, courseNames);
            course.setAdapter(courseAdapter);

            // 初始化教师下拉框（先显示所有教师）
            teacherAdapter = new ArrayAdapter<>(XueShengXinXiChangeActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, teacherNames);
            teacher_sel.setAdapter(teacherAdapter);

            // 初始化类型下拉框
            SpinnerAdapter adapter_type = new ArrayAdapter<>(XueShengXinXiChangeActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, type_array);
            type.setAdapter(StringUtils.cast(adapter_type));
            return true;
        }
    });

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                shezhiList = sheZhiService.getList(teacher.getCompany());
                if (shezhiList.size() > 0) {
                    // 构建映射关系
                    buildCourseTeacherMaps();

                    // 初始化类型数组
                    type_array = new String[shezhiList.size()];
                    for (int i = 0; i < shezhiList.size(); i++) {
                        type_array[i] = shezhiList.get(i).getType();
                    }
                }
                Message msg = new Message();
                listLoadHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }).start();
}
//-------0129
// 构建映射关系
private void buildCourseTeacherMaps() {
    courseTeacherMap.clear();
//    teacherCourseMap.clear();
    courseNames.clear();
    teacherNames.clear();

    for (SheZhi shezhi : shezhiList) {
        String courseName = shezhi.getCourse();
        String teacherName = shezhi.getTeacher();

        // 添加唯一课程名
        if (!courseNames.contains(courseName)) {
            courseNames.add(courseName);
        }

        // 添加唯一教师名
        if (!teacherNames.contains(teacherName)) {
            teacherNames.add(teacherName);
        }

        // 构建课程->教师映射
        if (!courseTeacherMap.containsKey(courseName)) {
            courseTeacherMap.put(courseName, new ArrayList<>());
        }
        List<String> teachersForCourse = courseTeacherMap.get(courseName);
        if (!teachersForCourse.contains(teacherName)) {
            teachersForCourse.add(teacherName);
        }
//
//        // 构建教师->课程映射
//        if (!teacherCourseMap.containsKey(teacherName)) {
//            teacherCourseMap.put(teacherName, new ArrayList<>());
//        }
//        List<String> coursesForTeacher = teacherCourseMap.get(teacherName);
//        if (!coursesForTeacher.contains(courseName)) {
//            coursesForTeacher.add(courseName);
//        }
    }
}

    // 根据课程更新教师下拉框
//    private void updateTeacherSpinnerByCourse(String selectedCourse) {
//        List<String> filteredTeachers;
//        if (selectedCourse == null || selectedCourse.isEmpty() || !courseTeacherMap.containsKey(selectedCourse)) {
//            filteredTeachers = teacherNames; // 显示所有教师
//        } else {
//            filteredTeachers = courseTeacherMap.get(selectedCourse);
//        }
//
//        teacherAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, filteredTeachers);
//        teacher_sel.setAdapter(teacherAdapter);
//    }
    //--------0129
    private void updateTeacherSpinnerByCourse(String selectedCourse) {
        if (teacher_sel == null) return;

        List<String> filteredTeachers;
        if (selectedCourse == null || selectedCourse.isEmpty() || !courseTeacherMap.containsKey(selectedCourse)) {
            filteredTeachers = teacherNames; // 显示所有教师
        } else {
            filteredTeachers = courseTeacherMap.get(selectedCourse);
        }

        teacherAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, filteredTeachers);
        teacher_sel.setAdapter(teacherAdapter);
    }
    // 根据教师更新课程下拉框
//    private void updateCourseSpinnerByTeacher(String selectedTeacher) {
//        List<String> filteredCourses;
//        if (selectedTeacher == null || selectedTeacher.isEmpty() || !teacherCourseMap.containsKey(selectedTeacher)) {
//            filteredCourses = courseNames; // 显示所有课程
//        } else {
//            filteredCourses = teacherCourseMap.get(selectedTeacher);
//        }
//
//        courseAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, filteredCourses);
//        course.setAdapter(courseAdapter);
//    }
//---------0129
//    private void updateCourseSpinnerByTeacher(String selectedTeacher) {
//        if (course == null) return;
//
//        List<String> filteredCourses;
//        if (selectedTeacher == null || selectedTeacher.isEmpty() || !teacherCourseMap.containsKey(selectedTeacher)) {
//            filteredCourses = courseNames; // 显示所有课程
//        } else {
//            filteredCourses = teacherCourseMap.get(selectedTeacher);
//        }
//
//        courseAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, filteredCourses);
//        course.setAdapter(courseAdapter);
//    }
    // 在适配器中查找教师位置
    private int getTeacherPositionInAdapter(String teacherName) {
        if (teacherAdapter == null) return -1;
        for (int i = 0; i < teacherAdapter.getCount(); i++) {
            if (teacherAdapter.getItem(i).equals(teacherName)) {
                return i;
            }
        }
        return -1;
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
            student.setRealname(realName.getText().toString());
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

        student.setSex(sex.getSelectedItem().toString());
        student.setRgdate(rgdate.getText().toString());
        student.setClassnum(classnum.getText().toString());
        student.setPhone(phone.getText().toString());
        if(fee.getText().toString().equals("")){
            student.setFee(Integer.parseInt("0"));
        }else{
            student.setFee(Integer.parseInt(fee.getText().toString()));
        }
        if(allhour.getText().toString().equals("")){
            student.setAllhour(Integer.parseInt("0"));
        }else{
            student.setAllhour(Integer.parseInt(allhour.getText().toString()));
        }
        student.setType(type.getSelectedItem().toString());
        student.setCompany(teacher.getCompany());
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(XueShengXinXiChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


//    private int getCoursePosition(String param) {
//        if (course_array != null) {
//            for (int i = 0; i < course_array.length; i++) {
//                if (param.equals(course_array[i])) {
//                    return i;
//                }
//            }
//        }
//        return 0;
//    }
private int getCoursePosition(String param) {
    for (int i = 0; i < courseNames.size(); i++) {
        if (param.equals(courseNames.get(i))) {
            return i;
        }
    }
    return 0;
}
//    private int getTeacherPosition(String param) {
//        if (teacher_array != null) {
//            for (int i = 0; i < teacher_array.length; i++) {
//                if (param.equals(teacher_array[i])) {
//                    return i;
//                }
//            }
//        }
//        return 0;
//    }

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
