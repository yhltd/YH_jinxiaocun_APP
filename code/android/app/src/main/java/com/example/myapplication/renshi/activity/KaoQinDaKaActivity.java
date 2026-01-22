package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiGongZuoShiJian;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinDaKa;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinQingJia;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiKaoQinDaKaService;
import com.example.myapplication.renshi.service.YhRenShiKaoQinQingJiaService;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KaoQinDaKaActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiKaoQinDaKaService yhRenShiKaoQinDaKaService;
    private YhRenShiKaoQinQingJiaService yhRenShiKaoQinQingJiaService;
    private SharedPreferences sp;

    // 用户信息
    private EditText etDepartment;
    private EditText etName;
    private Button btnSaveInfo;

    // 时间显示
    private TextView tvDate;
    private TextView tvTime;

    // 打卡按钮
    private Button btnSignIn;
    private Button btnSignOut;
    private TextView tvSignStatus;

    // 工作安排
    private TextView tvSchedule;
    private TextView tvWorkTime;
    private TextView tvRestTime;

    // 请假申请
    private Button btnApplyLeave;
    private LinearLayout containerLeaveRecords;
    private TextView tvNoLeaveRecord;

    // 请假对话框相关
    private String selectedStartDate = "";
    private String selectedEndDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaoqindaka);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("考勤打卡");
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        yhRenShiKaoQinDaKaService = new YhRenShiKaoQinDaKaService();
        yhRenShiKaoQinQingJiaService = new YhRenShiKaoQinQingJiaService();
        sp = getSharedPreferences("user_info", MODE_PRIVATE);

        initView();
        setupListeners();
        startTimeUpdate();
        loadUserInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        // 用户信息
        etDepartment = findViewById(R.id.et_department);
        etName = findViewById(R.id.et_name);
        btnSaveInfo = findViewById(R.id.btn_save_info);

        // 时间显示
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);

        // 打卡按钮
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignOut = findViewById(R.id.btn_sign_out);
        tvSignStatus = findViewById(R.id.tv_sign_status);

        // 工作安排
        tvSchedule = findViewById(R.id.tv_schedule);
        tvWorkTime = findViewById(R.id.tv_work_time);
        tvRestTime = findViewById(R.id.tv_rest_time);

        // 请假申请
        btnApplyLeave = findViewById(R.id.btn_apply_leave);
        containerLeaveRecords = findViewById(R.id.container_leave_records);
        tvNoLeaveRecord = findViewById(R.id.tv_no_leave_record);
    }

    private void setupListeners() {
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        btnApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaveApplicationDialog();
            }
        });
    }

    // 更新时间显示
    private void startTimeUpdate() {
        final Handler timeHandler = new Handler();
        Runnable timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                timeHandler.postDelayed(this, 1000);
            }
        };
        timeHandler.post(timeRunnable);
    }

    private void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Date now = new Date();
        tvDate.setText(dateFormat.format(now));
        tvTime.setText(timeFormat.format(now));
    }

    // 加载用户信息
    private void loadUserInfo() {
//        String department = sp.getString("department", "");
//        String name = sp.getString("name", "");
//
//        if (!department.isEmpty() && !name.isEmpty()) {
//            etDepartment.setText(department);
//            etName.setText(name);
//
//            // 加载工作安排
//            loadWorkSchedule(department, name);
//
//            // 获取今日考勤状态
//            getTodayAttendanceStatus(name);
//
//            // 加载请假记录
//            loadLeaveRecords();
//        }
        String department = sp.getString("department", "");
        String name = sp.getString("name", "");

        if (!department.isEmpty() && !name.isEmpty()) {
            etDepartment.setText(department);
            etName.setText(name);

            // 初始状态：签到可用，签退不可用
//            btnSignIn.setEnabled(true);
//            btnSignOut.setEnabled(false);
//            updatePunchButtons("未打卡");
            // 加载工作安排
            loadWorkSchedule(department, name);

            // 获取今日考勤状态
            getTodayAttendanceStatus(name);

            // 加载请假记录
            loadLeaveRecords();
            System.out.println("DEBUG loadUserInfo: 开始更新请假状态");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String company = yhRenShiUser.getL();
                                if (company != null) {
                                    System.out.println("DEBUG: 从loadUserInfo调用updateLeaveStatusToAttendance");
                                    yhRenShiKaoQinDaKaService.updateLeaveStatusToAttendance(company, name);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }, 2000); // 延迟2秒执行
        } else {
            // 如果没有保存过信息，初始状态
            btnSignIn.setEnabled(true);
            btnSignOut.setEnabled(false);
        }
    }

    // 获取今日考勤状态
    private void getTodayAttendanceStatus(String name) {
//        Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                String status = (String) msg.obj;
//                if (status != null) {
//                    updateStatusDisplay(status);
//                    updatePunchButtons(status);
//                }
//                return true;
//            }
//        });
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String status = (String) msg.obj;

                if (status != null) {
                    // 新增：检查是否为"休"
                    if ("休".equals(status)) {
                        // 禁用签到按钮
                        btnSignIn.setEnabled(false);
                        // 提示今天是休息日
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(KaoQinDaKaActivity.this, "今天是休息日，不需要签到");
                            }
                        });
                        // 设置状态显示为休息
                        tvSignStatus.setText("休息");
                        tvSignStatus.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                    } else {
                        // 如果不是休息日，正常更新状态显示
                        updateStatusDisplay(status);
                    }
                    // 不再调用updatePunchButtons方法
                }
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取公司名 - 从yhRenShiUser对象的L字段获取
                    String company = yhRenShiUser.getL();
                    if (company == null || company.isEmpty()) {
                        Message msg = new Message();
                        msg.obj = "未打卡";
                        handler.sendMessage(msg);
                        return;
                    }

                    String status = yhRenShiKaoQinDaKaService.getTodayStatus(company, name);

                    Message msg = new Message();
                    msg.obj = status;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "未打卡";
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    // 保存用户信息
//    private void saveUserInfo() {
//        final String department = etDepartment.getText().toString().trim();
//        final String name = etName.getText().toString().trim();
//
//        if (department.isEmpty()) {
//            Toast.makeText(this, "请输入部门", Toast.LENGTH_SHORT).show();
//            etDepartment.requestFocus();
//            return;
//        }
//
//        if (name.isEmpty()) {
//            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
//            etName.requestFocus();
//            return;
//        }
//
//        btnSaveInfo.setEnabled(false);
//
//        Handler saveHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                btnSaveInfo.setEnabled(true);
//                int resultType = msg.what; // 1:成功, 2:部门不存在, 3:其他错误
//
//                switch (resultType) {
//                    case 1: // 成功
//                        // 保存到SharedPreferences
//                        SharedPreferences.Editor editor = sp.edit();
//                        editor.putString("department", department);
//                        editor.putString("name", name);
//                        editor.apply();
//
//                        ToastUtil.show(KaoQinDaKaActivity.this, "个人信息保存成功");
//
//                        // 保存后立即刷新UI
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // 加载工作安排
//                                loadWorkSchedule(department, name);
//
//                                // 获取今日考勤状态
//                                getTodayAttendanceStatus(name);
//
//                                // 加载请假记录
//                                loadLeaveRecords();
//                            }
//                        });
//                        break;
//
//                    case 2: // 部门不存在
//                        ToastUtil.show(KaoQinDaKaActivity.this, "部门不存在，请输入正确的部门名称");
//                        etDepartment.requestFocus();
//                        etDepartment.selectAll();
//                        break;
//
//                    case 3: // 其他错误
//                        ToastUtil.show(KaoQinDaKaActivity.this, "保存失败，请稍后再试");
//                        break;
//                }
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 获取公司名
//                    String company = yhRenShiUser.getL();
//                    if (company == null || company.isEmpty()) {
//                        Message msg = new Message();
//                        msg.what = 3; // 错误
//                        saveHandler.sendMessage(msg);
//                        return;
//                    }
//
//                    // 1. 先验证部门是否存在
//                    boolean departmentExists = yhRenShiKaoQinDaKaService.checkDepartmentExists(company, department);
//                    if (!departmentExists) {
//                        Message msg = new Message();
//                        msg.what = 2; // 部门不存在
//                        saveHandler.sendMessage(msg);
//                        return;
//                    }
//
//                    // 2. 获取当前年月
//                    Calendar cal = Calendar.getInstance();
//                    String year = String.valueOf(cal.get(Calendar.YEAR));
//                    String moth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
//
//                    // 3. 检查是否已有记录
//                    String this_date = year + moth;
//                    List<YhRenShiKaoQinDaKa> records = yhRenShiKaoQinDaKaService.nameMonthList(company, name, this_date);
//                    YhRenShiKaoQinDaKa record = (records != null && !records.isEmpty()) ? records.get(0) : null;
//
//                    boolean result = true;
//                    if (record == null) {
//                        // 创建新记录
//                        record = new YhRenShiKaoQinDaKa();
//                        record.setYear(year);
//                        record.setMonth(moth);
//                        record.setName(name);
//                        record.setAo(company.replace("_hr", ""));
//
//                        // 初始化统计字段
//                        record.setAj("0");  // 全勤天数
//                        record.setAk("0");  // 实际天数
//                        record.setAl("0");  // 请假小时
//                        record.setAm("0");  // 加班小时
//                        record.setAn("0");  // 异常天数
//
//                        result = yhRenShiKaoQinDaKaService.insert(record);
//                    }
//
//                    Message msg = new Message();
//                    msg.what = result ? 1 : 3; // 1:成功, 3:错误
//                    saveHandler.sendMessage(msg);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Message msg = new Message();
//                    msg.what = 3; // 错误
//                    saveHandler.sendMessage(msg);
//                }
//            }
//        }).start();
//    }
    private void saveUserInfo() {
        final String department = etDepartment.getText().toString().trim();
        final String name = etName.getText().toString().trim();

        if (department.isEmpty()) {
            Toast.makeText(this, "请输入部门", Toast.LENGTH_SHORT).show();
            etDepartment.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return;
        }

        btnSaveInfo.setEnabled(false);

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                btnSaveInfo.setEnabled(true);
                int resultType = msg.what; // 1:成功, 2:部门不存在, 3:其他错误
                int scheduledWorkDays = msg.arg1; // 获取工作天数

                switch (resultType) {
                    case 1: // 成功
                        // 保存到SharedPreferences
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("department", department);
                        editor.putString("name", name);
                        editor.apply();

                        ToastUtil.show(KaoQinDaKaActivity.this, "个人信息保存成功，本月工作天数为：" + scheduledWorkDays + "天");

                        // 保存后立即刷新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 加载工作安排
                                loadWorkSchedule(department, name);

                                // 获取今日考勤状态
                                getTodayAttendanceStatus(name);

                                // 加载请假记录
                                loadLeaveRecords();
                            }
                        });
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    String company = yhRenShiUser.getL();
//                                    if (company != null && !company.isEmpty()) {
//                                        System.out.println("DEBUG: 开始更新请假状态到考勤记录");
//                                        yhRenShiKaoQinDaKaService.updateLeaveStatusToAttendance(company, name);
//                                        System.out.println("DEBUG: 请假状态更新完成");
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    System.out.println("DEBUG: 更新请假状态失败: " + e.getMessage());
//                                }
//                            }
//                        }).start();
                        break;

                    case 2: // 部门不存在
                        ToastUtil.show(KaoQinDaKaActivity.this, "部门不存在，请输入正确的部门名称");
                        etDepartment.requestFocus();
                        etDepartment.selectAll();
                        break;

                    case 3: // 其他错误
                        ToastUtil.show(KaoQinDaKaActivity.this, "保存失败，请稍后再试");
                        break;
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取公司名
                    String company = yhRenShiUser.getL();
                    if (company == null || company.isEmpty()) {
                        Message msg = new Message();
                        msg.what = 3; // 错误
                        saveHandler.sendMessage(msg);
                        return;
                    }

                    // 1. 先验证部门是否存在
                    boolean departmentExists = yhRenShiKaoQinDaKaService.checkDepartmentExists(company, department);
                    if (!departmentExists) {
                        Message msg = new Message();
                        msg.what = 2; // 部门不存在
                        saveHandler.sendMessage(msg);
                        return;
                    }

                    // 2. 获取当前年月
                    Calendar cal = Calendar.getInstance();
                    String year = String.valueOf(cal.get(Calendar.YEAR));
                    String moth = String.format("%02d", cal.get(Calendar.MONTH) + 1);

                    // 3. 计算该部门在当前年月的工作天数
                    int scheduledWorkDays = yhRenShiKaoQinDaKaService.calculateScheduledWorkDays(
                            company, department, year, moth);

                    System.out.println("DEBUG: 计算得到" + year + "年" + moth + "月工作天数: " + scheduledWorkDays);

                    // 4. 检查是否已有记录
                    String this_date = year + moth;
                    List<YhRenShiKaoQinDaKa> records = yhRenShiKaoQinDaKaService.nameMonthList(company, name, this_date);
                    YhRenShiKaoQinDaKa record = (records != null && !records.isEmpty()) ? records.get(0) : null;

                    boolean result = true;
                    if (record == null) {
                        // 创建新记录
                        record = new YhRenShiKaoQinDaKa();
                        record.setYear(year);
                        record.setMonth(moth);
                        record.setName(name);
                        record.setAo(company.replace("_hr", ""));

                        // 设置工作天数统计 - AJ字段设置为计划工作天数
                        record.setAj(String.valueOf(scheduledWorkDays)); // AJ: 计划工作天数
                        record.setAk("0");  // AK: 实际天数（初始为0）
                        record.setAl("0");  // AL: 请假小时
                        record.setAm("0");  // AM: 加班小时
                        record.setAn("0");  // AN: 异常天数

                        result = yhRenShiKaoQinDaKaService.insert(record);
                    } else {
                        // 已有记录，更新AJ字段为计划工作天数
                        record.setAj(String.valueOf(scheduledWorkDays));
                        result = yhRenShiKaoQinDaKaService.update(record);
                    }

                    Message msg = new Message();
                    msg.what = result ? 1 : 3; // 1:成功, 3:错误
                    msg.arg1 = scheduledWorkDays; // 传递工作天数
                    saveHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3; // 错误
                    saveHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    //-----------------
    private void loadWorkSchedule(String department, String name) {
        // 在子线程中查询工作时间
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int resultType = msg.what; // 0:找到工作时间, 1:没有工作时间安排, 2:错误
                YhRenShiGongZuoShiJian workTime = (YhRenShiGongZuoShiJian) msg.obj;

                System.out.println("DEBUG loadWorkSchedule UI线程: resultType=" + resultType);

                if (resultType == 0 && workTime != null) {
                    // 成功找到工作时间安排
                    System.out.println("DEBUG 设置工作时间安排: ");
                    System.out.println("  schedule_title: " + workTime.getScheduleTitle());
                    System.out.println("  gongzuoshijianks: " + workTime.getGongzuoshijianks());
                    System.out.println("  gongzuoshijianjs: " + workTime.getGongzuoshijianjs());
                    System.out.println("  wuxiushijianks: " + workTime.getWuxiushijianks());
                    System.out.println("  wuxiushijianjs: " + workTime.getWuxiushijianjs());

                    // 设置排班标题
                    String scheduleTitle = workTime.getScheduleTitle();
                    if (scheduleTitle != null && !scheduleTitle.isEmpty()) {
                        tvSchedule.setText(scheduleTitle);
                    } else {
                        tvSchedule.setText(department);
                    }

                    // 设置工作时间
                    String workStart = workTime.getGongzuoshijianks();
                    String workEnd = workTime.getGongzuoshijianjs();
                    if (workStart != null && workEnd != null &&
                            !workStart.isEmpty() && !workEnd.isEmpty()) {
                        tvWorkTime.setText(workStart + " - " + workEnd);
                        System.out.println("DEBUG 设置工作时间: " + workStart + " - " + workEnd);
                    } else {
                        tvWorkTime.setText("09:00 - 18:00"); // 默认值
                        System.out.println("DEBUG 工作时间为空，使用默认值");
                    }

                    // 设置午休时间
                    String restStart = workTime.getWuxiushijianks();
                    String restEnd = workTime.getWuxiushijianjs();
                    if (restStart != null && restEnd != null &&
                            !restStart.isEmpty() && !restEnd.isEmpty()) {
                        tvRestTime.setText(restStart + " - " + restEnd);
                        System.out.println("DEBUG 设置午休时间: " + restStart + " - " + restEnd);
                    } else {
                        tvRestTime.setText("12:00 - 13:05"); // 默认值
                        System.out.println("DEBUG 午休时间为空，使用默认值");
                    }

                    Toast.makeText(KaoQinDaKaActivity.this,
                            "已加载 " + department + " 部门的工作时间安排",
                            Toast.LENGTH_SHORT).show();
                } else if (resultType == 1) {
                    // 部门存在但没有工作时间安排
                    System.out.println("DEBUG 部门存在但");
                    tvSchedule.setText(department);
                    tvWorkTime.setText("09:00 - 18:00");
                    tvRestTime.setText("12:00 - 13:05");

                    Toast.makeText(KaoQinDaKaActivity.this,
                            "部门 '" + department + "' 存在，但未设置工作时间安排，使用默认时间",
                            Toast.LENGTH_LONG).show();
                } else {
                    // 查询出错
                    System.out.println("DEBUG 查询工作时间失败");
                    tvSchedule.setText(department);
                    tvWorkTime.setText("09:00 - 18:00");
                    tvRestTime.setText("12:00 - 13:05");

                    Toast.makeText(KaoQinDaKaActivity.this,
                            "查询工作时间失败，使用默认时间",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取当前日期
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDate = dateFormat.format(new Date());

                    System.out.println("DEBUG loadWorkSchedule 子线程: 部门=" + department + ", 日期=" + currentDate);
                    String company = yhRenShiUser.getL();
                    if (company == null || company.isEmpty()) {
                        Message msg = new Message();
                        msg.obj = "无法获取公司信息";
                        return;
                    }
                    // 查询工作时间（不需要公司名）
                    YhRenShiGongZuoShiJian workTime = yhRenShiKaoQinDaKaService.getWorkSchedule(company, department, name, currentDate);

                    Message msg = new Message();
                    if (workTime != null) {
                        msg.what = 0; // 找到工作时间
                        msg.obj = workTime;
                    } else {
                        msg.what = 1; // 没有工作时间安排
                        msg.obj = null;
                    }
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("DEBUG loadWorkSchedule 异常: " + e.getMessage());
                    Message msg = new Message();
                    msg.what = 2; // 错误
                    msg.obj = null;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    private void signIn() {
//        final String department = etDepartment.getText().toString().trim();
//        final String name = etName.getText().toString().trim();
//
//        if (department.isEmpty() || name.isEmpty()) {
//            Toast.makeText(this, "请先保存个人信息", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        btnSignIn.setEnabled(false);
//
//        // 获取当前时间
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//        final String currentTime = timeFormat.format(new Date());
//
//        Handler signInHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                btnSignIn.setEnabled(true);
//                String result = (String) msg.obj;
//                if ("success".equals(result)) {
//                    String message = "签到成功：" + currentTime;
//
//                    // 获取更新后的状态
//                    getTodayAttendanceStatus(name);
//
//                    ToastUtil.show(KaoQinDaKaActivity.this, message);
//                } else {
//                    ToastUtil.show(KaoQinDaKaActivity.this, "签到失败：" + result);
//                }
//                return true;
//            }
//        });
        final String department = etDepartment.getText().toString().trim();
        final String name = etName.getText().toString().trim();

        if (department.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "请先保存个人信息", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentStatus = tvSignStatus.getText().toString();
        if ("休".equals(currentStatus) || "休息".equals(currentStatus)) {
            ToastUtil.show(KaoQinDaKaActivity.this, "今天是休息日，不需要签到");
            return;
        }


        btnSignIn.setEnabled(false);

        // 获取当前时间
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        final String currentTime = timeFormat.format(new Date());

        Handler signInHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String result = (String) msg.obj;
                if ("success".equals(result)) {
                    String message = "签到成功：" + currentTime;

                    // 关键修改：签到成功后立即设置按钮状态
                    btnSignIn.setEnabled(false);  // 签到按钮禁用
                    btnSignOut.setEnabled(true);   // 签退按钮启用

                    ToastUtil.show(KaoQinDaKaActivity.this, message);

                    // 延迟1秒后再获取服务器状态（避免冲突）
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getTodayAttendanceStatus(name);
                        }
                    }, 1000);

                } else {
                    // 签到失败时重新启用签到按钮
                    btnSignIn.setEnabled(true);
                    ToastUtil.show(KaoQinDaKaActivity.this, "签到失败：" + result);
                }
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取公司名
                    String company = yhRenShiUser.getL();
                    if (company == null || company.isEmpty()) {
                        Message msg = new Message();
                        msg.obj = "无法获取公司信息";
                        signInHandler.sendMessage(msg);
                        return;
                    }

                    // 传递部门参数
                    boolean success = yhRenShiKaoQinDaKaService.signIn(company, name, department, currentTime);

                    Message msg = new Message();
                    msg.obj = success ? "success" : "数据库更新失败";
                    signInHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "异常：" + e.getMessage();
                    signInHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    // 签退
    private void signOut() {
//        final String department = etDepartment.getText().toString().trim();
//        final String name = etName.getText().toString().trim();
//
//        if (department.isEmpty() || name.isEmpty()) {
//            Toast.makeText(this, "请先保存个人信息", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        btnSignOut.setEnabled(false);
//
//        // 获取当前时间
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//        final String currentTime = timeFormat.format(new Date());
//
//        Handler signOutHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                btnSignOut.setEnabled(true);
//                String result = (String) msg.obj;
//                if ("success".equals(result)) {
//                    String message = "签退成功：" + currentTime;
//
//                    // 获取更新后的状态
//                    getTodayAttendanceStatus(name);
//
//                    ToastUtil.show(KaoQinDaKaActivity.this, message);
//                } else {
//                    ToastUtil.show(KaoQinDaKaActivity.this, "签退失败：" + result);
//                }
//                return true;
//            }
//        });
        final String department = etDepartment.getText().toString().trim();
        final String name = etName.getText().toString().trim();

        if (department.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "请先保存个人信息", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSignOut.setEnabled(false);

        // 获取当前时间
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        final String currentTime = timeFormat.format(new Date());

        Handler signOutHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String result = (String) msg.obj;
                if ("success".equals(result)) {
                    String message = "签退成功：" + currentTime;

                    // 关键修改：签退成功后立即设置按钮状态
                    btnSignIn.setEnabled(false);   // 签到按钮禁用
                    btnSignOut.setEnabled(false);  // 签退按钮禁用

                    ToastUtil.show(KaoQinDaKaActivity.this, message);

                    // 延迟1秒后再获取服务器状态（避免冲突）
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getTodayAttendanceStatus(name);
                        }
                    }, 1000);

                } else {
                    // 签退失败时重新启用签退按钮
                    btnSignOut.setEnabled(true);
                    ToastUtil.show(KaoQinDaKaActivity.this, "签退失败：" + result);
                }
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取公司名
                    String company = yhRenShiUser.getL();
                    if (company == null || company.isEmpty()) {
                        Message msg = new Message();
                        msg.obj = "无法获取公司信息";
                        signOutHandler.sendMessage(msg);
                        return;
                    }

                    // 传递部门参数
                    boolean success = yhRenShiKaoQinDaKaService.signOut(company, name, department, currentTime);

                    Message msg = new Message();
                    msg.obj = success ? "success" : "数据库更新失败";
                    signOutHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "异常：" + e.getMessage();
                    signOutHandler.sendMessage(msg);
                }
            }
        }).start();
    }
    /**
     * 更新打卡按钮状态 - 简单实现
     */
    private void updatePunchButtonsSimple(String currentStatus) {
        if (currentStatus == null || currentStatus.isEmpty()) {
            currentStatus = "未打卡";
        }

        // 根据当前状态控制按钮可用性
        switch (currentStatus) {
            case "未打卡":
            case "旷勤":  // 旷勤时可以重新签到
                btnSignIn.setEnabled(true);
                btnSignOut.setEnabled(false);
                break;

            case "早签":
            case "迟到":
            case "出勤":  // 已签到，可以签退
                btnSignIn.setEnabled(false);
                btnSignOut.setEnabled(true);
                break;

            case "早退":  // 已完成签退
                btnSignIn.setEnabled(false);
                btnSignOut.setEnabled(false);
                break;

            default:
                btnSignIn.setEnabled(true);
                btnSignOut.setEnabled(false);
        }
    }

    /**
     * 在签到成功后调用，更新按钮状态
     */
    private void afterSignIn() {
        // 签到后按钮状态
        btnSignIn.setEnabled(false);
        btnSignOut.setEnabled(true);

        final String name = etName.getText().toString().trim();
        if (!name.isEmpty()) {
            // 获取更新后的状态
            getTodayAttendanceStatus(name);
        }
    }

    /**
     * 在签退成功后调用，更新按钮状态
     */
    private void afterSignOut() {
        // 签退后按钮状态
        btnSignIn.setEnabled(false);
        btnSignOut.setEnabled(false);

        final String name = etName.getText().toString().trim();
        if (!name.isEmpty()) {
            // 获取更新后的状态
            getTodayAttendanceStatus(name);
        }
    }
    // 更新状态显示
    private void updateStatusDisplay(String status) {
        tvSignStatus.setText(status);

        switch (status) {
            case "休":
                tvSignStatus.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                btnSignIn.setEnabled(false); // 禁用签到按钮
                break;
            case "出勤":
                tvSignStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "迟到":
            case "早退":
                tvSignStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "旷勤":
                tvSignStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                tvSignStatus.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    // 更新打卡按钮状态
    private void updatePunchButtons(String status) {
        if (status == null || status.isEmpty()) {
            status = "未打卡";
        }

        if ("休".equals(status)) {
            btnSignIn.setEnabled(false);
            btnSignOut.setEnabled(false);
            btnSignIn.setAlpha(0.5f); // 半透明
            btnSignOut.setAlpha(0.5f); // 半透明
            return;
        }

        // 根据当前状态控制按钮可用性和颜色
        switch (status) {
            case "未打卡":
                btnSignIn.setEnabled(true);
                btnSignOut.setEnabled(false);
                btnSignIn.setBackgroundColor(getResources().getColor(R.color.primary)); // 正常颜色
                btnSignOut.setAlpha(0.5f); // 半透明
                break;
            case "早签":
            case "迟到":
            case "旷勤":
                btnSignIn.setEnabled(false);
                btnSignOut.setEnabled(true);
                btnSignOut.setAlpha(0.5f); // 半透明
                btnSignOut.setBackgroundColor(getResources().getColor(R.color.primary)); // 正常颜色
                break;
            case "出勤":
            case "早退":
                btnSignIn.setEnabled(false);
                btnSignOut.setEnabled(false);
                btnSignOut.setAlpha(0.5f); // 半透明
                btnSignOut.setAlpha(0.5f); // 半透明
                break;
            default:
                btnSignIn.setEnabled(true);
                btnSignOut.setEnabled(false);
                btnSignIn.setBackgroundColor(getResources().getColor(R.color.primary));
                btnSignOut.setAlpha(0.5f); // 半透明
        }
    }

    // 显示请假申请对话框（修改后的方法）
    private void showLeaveApplicationDialog() {
        // 先重置选择的时间
        selectedStartDate = "";
        selectedEndDate = "";

        // 创建自定义对话框视图
        View dialogView = View.inflate(this, R.layout.dialog_apply_leave, null);

        final TextView tvStartDate = dialogView.findViewById(R.id.tv_start_date);
        final TextView tvEndDate = dialogView.findViewById(R.id.tv_end_date);
        final EditText etReason = dialogView.findViewById(R.id.et_reason);
        final TextView tvDays = dialogView.findViewById(R.id.tv_days);

        // 设置开始时间选择
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog("start", tvStartDate, tvEndDate, tvDays);
            }
        });

        // 设置结束时间选择
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog("end", tvEndDate, tvStartDate, tvDays);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("请假申请");

        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String startDate = tvStartDate.getText().toString();
                String endDate = tvEndDate.getText().toString();
                String reason = etReason.getText().toString().trim();

                if (startDate.isEmpty() || startDate.equals("请选择开始时间")) {
                    ToastUtil.show(KaoQinDaKaActivity.this, "请选择开始时间");
                    return;
                }

                if (endDate.isEmpty() || endDate.equals("请选择结束时间")) {
                    ToastUtil.show(KaoQinDaKaActivity.this, "请选择结束时间");
                    return;
                }

                if (reason.isEmpty()) {
                    ToastUtil.show(KaoQinDaKaActivity.this, "请输入请假原因");
                    return;
                }

                // 检查开始时间是否早于结束时间
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = sdf.parse(startDate);
                    Date end = sdf.parse(endDate);

                    if (end.before(start)) {
                        ToastUtil.show(KaoQinDaKaActivity.this, "结束时间不能早于开始时间");
                        return;
                    }

                    applyLeave(startDate, endDate, reason);

                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show(KaoQinDaKaActivity.this, "时间格式错误");
                }
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    // 日期选择器
    @SuppressLint("ClickableViewAccessibility")
    private void showDatePickerDialog(final String type, final TextView targetTextView,
                                      final TextView otherTextView, final TextView tvDays) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(KaoQinDaKaActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String mon = "";
                        String day = "";
                        if (monthOfYear + 1 < 10) {
                            mon = "0" + (monthOfYear + 1);
                        } else {
                            mon = "" + (monthOfYear + 1);
                        }
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = "" + dayOfMonth;
                        }

                        String dateStr = year + "-" + mon + "-" + day;
                        targetTextView.setText(dateStr);

                        // 保存选择的日期
                        if ("start".equals(type)) {
                            selectedStartDate = dateStr;
                        } else {
                            selectedEndDate = dateStr;
                        }

                        // 计算天数 - 添加空检查
                        if (otherTextView != null && tvDays != null) {
                            calculateDays(otherTextView, targetTextView, tvDays);
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // 计算请假天数
    private void calculateDays(TextView tvStart, TextView tvEnd, TextView tvDays) {
        try {
            String startStr = tvStart.getText().toString();
            String endStr = tvEnd.getText().toString();

            System.out.println("DEBUG calculateDays: startStr=" + startStr + ", endStr=" + endStr);

            // 检查是否是默认文本
            if (startStr.equals("请选择开始时间") || startStr.equals("请选择结束时间") ||
                    endStr.equals("请选择开始时间") || endStr.equals("请选择结束时间")) {
                System.out.println("DEBUG 有未选择的日期，不计算天数");
                tvDays.setText("0天");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startStr);
            Date endDate = sdf.parse(endStr);

            long diff = endDate.getTime() - startDate.getTime();
            long days = diff / (1000 * 60 * 60 * 24) + 1; // +1因为包含开始和结束日

            System.out.println("DEBUG 计算天数: " + days + "天");

            if (days < 0) {
                tvDays.setText("0天");
            } else {
                tvDays.setText(days + "天");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG 计算天数失败: " + e.getMessage());
            tvDays.setText("计算失败");
        }
    }
    // 申请请假（新增方法）
    private void applyLeave(final String startDate, final String endDate, final String reason) {
        final String department = etDepartment.getText().toString().trim();
        final String name = etName.getText().toString().trim();

        if (department.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "请先保存个人信息", Toast.LENGTH_SHORT).show();
            return;
        }

        Handler applyHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int resultType = msg.what; // 1:成功, 2:失败

                switch (resultType) {
                    case 1: // 成功
                        ToastUtil.show(KaoQinDaKaActivity.this, "请假申请提交成功");
                        // 刷新请假记录
                        loadLeaveRecords();
                        break;

                    case 2: // 失败
                        ToastUtil.show(KaoQinDaKaActivity.this, "提交失败，请稍后再试");
                        break;
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取公司名
                    String company = yhRenShiUser.getL();
                    if (company == null || company.isEmpty()) {
                        Message msg = new Message();
                        msg.what = 2; // 失败
                        applyHandler.sendMessage(msg);
                        return;
                    }

                    // 创建请假申请对象
                    YhRenShiKaoQinQingJia leave = new YhRenShiKaoQinQingJia();
                    leave.setBumen(department);
                    leave.setXingming(name);
                    leave.setQsqingjiashijian(startDate);
                    leave.setJzqingjiashijian(endDate);
                    leave.setQingjiayuanyin(reason);
                    leave.setGongsi(company.replace("_hr", ""));
                    // 状态默认为"待审批"

                    boolean result = yhRenShiKaoQinQingJiaService.insert(leave);

                    Message msg = new Message();
                    msg.what = result ? 1 : 2; // 1:成功, 2:失败
                    applyHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2; // 失败
                    applyHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    // 加载请假记录（修改后的方法）
    private void loadLeaveRecords() {
        final String department = etDepartment.getText().toString().trim();
        final String name = etName.getText().toString().trim();

        if (department.isEmpty() || name.isEmpty()) {
            tvNoLeaveRecord.setVisibility(View.VISIBLE);
            return;
        }

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                List<YhRenShiKaoQinQingJia> list = (List<YhRenShiKaoQinQingJia>) msg.obj;
                displayLeaveRecords(list);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL();
                    if (company == null || company.isEmpty()) {
                        Message msg = new Message();
                        msg.obj = null;
                        handler.sendMessage(msg);
                        return;
                    }

                    List<YhRenShiKaoQinQingJia> list = yhRenShiKaoQinQingJiaService.getList(company, name);

                    Message msg = new Message();
                    msg.obj = list;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = null;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    // 显示请假记录
    private void displayLeaveRecords(List<YhRenShiKaoQinQingJia> list) {
        containerLeaveRecords.removeAllViews();

        if (list == null || list.isEmpty()) {
            tvNoLeaveRecord.setVisibility(View.VISIBLE);
            return;
        }

        tvNoLeaveRecord.setVisibility(View.GONE);

        for (final YhRenShiKaoQinQingJia record : list) {
            // 创建请假记录行视图
            View recordView = View.inflate(this, R.layout.item_leave_record, null);

            // 设置数据
            TextView tvTime = recordView.findViewById(R.id.tv_time);
            TextView tvDays = recordView.findViewById(R.id.tv_days);
            TextView tvReason = recordView.findViewById(R.id.tv_reason);
            TextView tvStatus = recordView.findViewById(R.id.tv_status);

            // 设置请假时间
            String timeText = record.getQsqingjiashijian() + "至\n" + record.getJzqingjiashijian();
            tvTime.setText(timeText);

            // 计算请假天数
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = sdf.parse(record.getQsqingjiashijian());
                Date endDate = sdf.parse(record.getJzqingjiashijian());
                long diff = endDate.getTime() - startDate.getTime();
                long days = diff / (1000 * 60 * 60 * 24) + 1;
                tvDays.setText(days + "天");
            } catch (Exception e) {
                tvDays.setText("1天");
            }

            // 设置请假原因
            tvReason.setText(record.getQingjiayuanyin());

            // 设置状态和颜色 - 修复null问题
            String status = record.getZhuangtai();
            if (status == null || status.isEmpty()) {
                status = "待审批";  // 如果数据库中是null，设为"待审批"
            }
            tvStatus.setText(status);  // 只显示状态，不拼接null

            // 根据状态设置颜色
            switch (status) {
                case "待审批":
                    tvStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    break;
                case "通过":
                    tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    break;
                case "驳回":
                    tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    break;
                default:
                    tvStatus.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }

            // 添加到容器
            containerLeaveRecords.addView(recordView);
        }
    }
}