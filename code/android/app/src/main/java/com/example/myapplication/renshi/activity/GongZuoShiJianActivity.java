package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.entity.GongZuoShiJian;
import com.example.myapplication.renshi.service.GongZuoShiJianService;
import com.example.myapplication.utils.ToastUtil;
import java.text.SimpleDateFormat;
import java.util.*;

public class GongZuoShiJianActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private GongZuoShiJianService gongZuoShiJianService;

    private GridLayout calendarGrid;
    private TextView tvCurrentMonth;
    private ListView listSchedule;
    private Button btnPrevMonth, btnNextMonth, btnNewSchedule, btnRangeSelect;

    private int currentYear, currentMonth;
    private List<GongZuoShiJian> scheduleList;
    private List<String> selectedDates = new ArrayList<>();
    private SimpleAdapter scheduleAdapter;

    // 时间相关
    private String workStartTime = "08:00";
    private String workEndTime = "17:00";
    private String breakStartTime = "12:00";
    private String breakEndTime = "13:00";
    private String department = "";

    private AlertDialog dateRangeDialog;
    private AlertDialog editWorkDaysDialog;
    private List<String> editWorkDaysSelectedDates = new ArrayList<>();
    private List<String> editWorkDaysOriginalDates = new ArrayList<>();
    private GongZuoShiJian currentEditSchedule;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                updateScheduleList();
            } else {
                ToastUtil.show(GongZuoShiJianActivity.this, "数据加载失败");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongzuoshijian);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("工作安排日历");
        }

        initViews();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        gongZuoShiJianService = new GongZuoShiJianService();

        // 初始化当前年月
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;

        updateMonthDisplay();
        generateCalendar();
        loadSchedules();
    }

    private void initViews() {
        calendarGrid = findViewById(R.id.calendar_grid);
        tvCurrentMonth = findViewById(R.id.tv_current_month);
        listSchedule = findViewById(R.id.list_schedule);
        btnPrevMonth = findViewById(R.id.btn_prev_month);
        btnNextMonth = findViewById(R.id.btn_next_month);
        btnNewSchedule = findViewById(R.id.btn_new_schedule);
        btnRangeSelect = findViewById(R.id.btn_range_select);

        btnPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevMonth();
            }
        });

        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth();
            }
        });

        btnNewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDates.isEmpty()) {
                    ToastUtil.show(GongZuoShiJianActivity.this, "请先选择日期");
                    return;
                }
                showTimeSettingDialog();
            }
        });

        btnRangeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateRangeDialog();
            }
        });

    }

    private void updateMonthDisplay() {
        tvCurrentMonth.setText(currentYear + "年" + currentMonth + "月");
    }

    private void prevMonth() {
        currentMonth--;
        if (currentMonth < 1) {
            currentMonth = 12;
            currentYear--;
        }
        updateMonthDisplay();
        generateCalendar();
    }

    private void nextMonth() {
        currentMonth++;
        if (currentMonth > 12) {
            currentMonth = 1;
            currentYear++;
        }
        updateMonthDisplay();
        generateCalendar();
    }

    @SuppressLint("SetTextI18n")
    private void generateCalendar() {
        calendarGrid.removeAllViews();

        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth - 1, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 调整周一开始
        int offset = (firstDayOfWeek == Calendar.SUNDAY) ? 6 : firstDayOfWeek - 2;

        // 添加上个月的最后几天
        Calendar prevMonth = Calendar.getInstance();
        prevMonth.set(currentYear, currentMonth - 2, 1);
        int prevDaysInMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = offset; i >= 0; i--) {
            View dayView = getLayoutInflater().inflate(R.layout.item_calendar_day, null);
            TextView tvDay = dayView.findViewById(R.id.tv_day);
            View vScheduleDot = dayView.findViewById(R.id.v_schedule_dot);

            int day = prevDaysInMonth - i;
            tvDay.setText(String.valueOf(day));
            tvDay.setTextColor(getResources().getColor(R.color.blue));

            // 设置点击事件
            final String dateStr = formatDate(currentYear, currentMonth - 1, day);
            dayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDateSelection(dateStr, dayView);
                }
            });

            calendarGrid.addView(dayView);
        }

        // 添加当前月
        for (int day = 1; day <= daysInMonth; day++) {
            View dayView = getLayoutInflater().inflate(R.layout.item_calendar_day, null);
            TextView tvDay = dayView.findViewById(R.id.tv_day);
            View vScheduleDot = dayView.findViewById(R.id.v_schedule_dot);

            tvDay.setText(String.valueOf(day));

            // 检查是否为今天
            Calendar today = Calendar.getInstance();
            if (currentYear == today.get(Calendar.YEAR) &&
                    currentMonth == today.get(Calendar.MONTH) + 1 &&
                    day == today.get(Calendar.DAY_OF_MONTH)) {
                dayView.setBackgroundResource(R.drawable.today_bg);
            }

            // 检查是否有工作安排
            final String dateStr = formatDate(currentYear, currentMonth, day);
            boolean hasSchedule = checkHasSchedule(dateStr);
            vScheduleDot.setVisibility(hasSchedule ? View.VISIBLE : View.GONE);

            // 检查是否已选中
            if (selectedDates.contains(dateStr)) {
                dayView.setBackgroundResource(R.drawable.selected_day_bg);
            }

            // 设置点击事件
            dayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDateSelection(dateStr, dayView);
                }
            });

            calendarGrid.addView(dayView);
        }

        // 计算剩余格子
        int totalCells = 42;
        int remainingCells = totalCells - (offset + 1 + daysInMonth);

        // 添加下个月的前几天
        for (int day = 1; day <= remainingCells; day++) {
            View dayView = getLayoutInflater().inflate(R.layout.item_calendar_day, null);
            TextView tvDay = dayView.findViewById(R.id.tv_day);
            View vScheduleDot = dayView.findViewById(R.id.v_schedule_dot);

            tvDay.setText(String.valueOf(day));
            tvDay.setTextColor(getResources().getColor(R.color.blue));

            // 设置点击事件
            final String dateStr = formatDate(currentYear, currentMonth + 1, day);
            dayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDateSelection(dateStr, dayView);
                }
            });

            calendarGrid.addView(dayView);
        }
    }

    private String formatDate(int year, int month, int day) {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    private void toggleDateSelection(String dateStr, View dayView) {
        if (selectedDates.contains(dateStr)) {
            selectedDates.remove(dateStr);
            dayView.setBackgroundResource(0);
        } else {
            selectedDates.add(dateStr);
            dayView.setBackgroundResource(R.drawable.selected_day_bg);
        }
    }

    private boolean checkHasSchedule(String dateStr) {
        if (scheduleList == null) return false;

        for (GongZuoShiJian schedule : scheduleList) {
            String workDaysJson = schedule.getWorkDays();
            if (workDaysJson != null && workDaysJson.contains(dateStr)) {
                return true;
            }
        }
        return false;
    }

    private void loadSchedules() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL();
                    System.out.println("开始加载数据，公司：" + company);

                    scheduleList = gongZuoShiJianService.getList(company);
                    System.out.println("数据加载完成，数量：" + (scheduleList != null ? scheduleList.size() : 0));

                    if (scheduleList != null && !scheduleList.isEmpty()) {
                        // 注意：现在workDays是String类型，存储JSON格式
                        // 如果需要使用List，需要在这里解析
                        for (GongZuoShiJian schedule : scheduleList) {
                            String workDaysJson = schedule.getWorkDays();
                            if (workDaysJson != null && !workDaysJson.isEmpty()) {
                                System.out.println("work_days JSON内容：" + workDaysJson);
                                // 这里可以解析JSON字符串到List，如果需要的话
                            }
                        }
                    }

                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("数据加载异常：" + e.getMessage());
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private int getWorkDayCount(String workDaysJson) {
        if (workDaysJson == null || workDaysJson.isEmpty()) {
            return 0;
        }

        try {
            // 移除JSON格式字符
            String cleanJson = workDaysJson
                    .replace("[", "")
                    .replace("]", "")
                    .replace("\"", "")
                    .trim();

            if (cleanJson.isEmpty()) {
                return 0;
            }

            // 分割逗号获取日期数量
            String[] dates = cleanJson.split(",");
            return dates.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void updateScheduleList() {
        if (scheduleList == null || scheduleList.isEmpty()) {
            List<HashMap<String, Object>> emptyList = new ArrayList<>();
            HashMap<String, Object> item = new HashMap<>();
            item.put("empty", "暂无工作安排");
            emptyList.add(item);

            scheduleAdapter = new SimpleAdapter(this, emptyList,
                    R.layout.empty_item, new String[]{"empty"}, new int[]{R.id.tv_empty});

            listSchedule.setAdapter(scheduleAdapter);
            return;
        }

        List<HashMap<String, Object>> data = new ArrayList<>();
        for (GongZuoShiJian schedule : scheduleList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", schedule.getId());
            map.put("title", schedule.getScheduleTitle());
            map.put("work_time", schedule.getGongZuoShiJianKs() + " - " + schedule.getGongZuoShiJianJs());
            map.put("work_days", "📅 点击编辑日期 (" + getWorkDayCount(schedule.getWorkDays()) + "天)");
            map.put("break_time", "午休：" + schedule.getWuXiuShiJianKs() + " - " + schedule.getWuXiuShiJianJs());
            map.put("repeat_type", getRepeatLabel(schedule.getRepeatType()));
            data.add(map);
        }

        scheduleAdapter = new SimpleAdapter(this, data, R.layout.item_schedule,
                new String[]{"title", "work_time", "work_days", "break_time", "repeat_type"},
                new int[]{R.id.tv_schedule_title, R.id.tv_work_time, R.id.tv_work_days,
                        R.id.tv_break_time, R.id.tv_repeat_type}) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                final GongZuoShiJian schedule = scheduleList.get(position);

                // 工作时间点击编辑
                TextView tvWorkTime = view.findViewById(R.id.tv_work_time);
                tvWorkTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editWorkTime(schedule);
                    }
                });

                // 工作日点击编辑
                TextView tvWorkDays = view.findViewById(R.id.tv_work_days);
                tvWorkDays.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editWorkDays(schedule);
                    }
                });

                // 午休时间点击编辑
                TextView tvBreakTime = view.findViewById(R.id.tv_break_time);
                tvBreakTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editBreakTime(schedule);
                    }
                });

                // 删除按钮
                Button btnDelete = view.findViewById(R.id.btn_delete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteSchedule(schedule);
                    }
                });

                return view;
            }
        };

        listSchedule.setAdapter(scheduleAdapter);
    }

    private String getRepeatLabel(String repeatType) {
        switch (repeatType) {
            case "daily": return "每天";
            case "weekly": return "每周";
            case "monthly": return "每月";
            case "weekdays": return "工作日";
            case "custom": return "自定义";
            default: return "不重复";
        }
    }

    private void showTimeSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_time_setting, null);
        builder.setView(dialogView);

        final EditText etDepartment = dialogView.findViewById(R.id.et_department);
        final TextView tvWorkStartTime = dialogView.findViewById(R.id.tv_work_start_time);
        final TextView tvWorkEndTime = dialogView.findViewById(R.id.tv_work_end_time);
        final TextView tvBreakStartTime = dialogView.findViewById(R.id.tv_break_start_time);
        final TextView tvBreakEndTime = dialogView.findViewById(R.id.tv_break_end_time);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        etDepartment.setText(department);
        tvWorkStartTime.setText(workStartTime);
        tvWorkEndTime.setText(workEndTime);
        tvBreakStartTime.setText(breakStartTime);
        tvBreakEndTime.setText(breakEndTime);

        // 设置时间选择
        tvWorkStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("work_start", tvWorkStartTime);
            }
        });

        tvWorkEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("work_end", tvWorkEndTime);
            }
        });

        tvBreakStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("break_start", tvBreakStartTime);
            }
        });

        tvBreakEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("break_end", tvBreakEndTime);
            }
        });

        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                department = etDepartment.getText().toString();
                workStartTime = tvWorkStartTime.getText().toString();
                workEndTime = tvWorkEndTime.getText().toString();
                breakStartTime = tvBreakStartTime.getText().toString();
                breakEndTime = tvBreakEndTime.getText().toString();

                saveSchedule();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showTimePicker(final String field, final TextView textView) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                textView.setText(time);

                switch (field) {
                    case "work_start":
                        workStartTime = time;
                        break;
                    case "work_end":
                        workEndTime = time;
                        break;
                    case "break_start":
                        breakStartTime = time;
                        break;
                    case "break_end":
                        breakEndTime = time;
                        break;
                }
            }
        }, 8, 0, true);

        timePickerDialog.show();
    }

    private void saveSchedule() {
        if (selectedDates.isEmpty()) {
            ToastUtil.show(this, "请选择日期");
            return;
        }

        if (department.isEmpty()) {
            ToastUtil.show(this, "请输入部门");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GongZuoShiJian schedule = new GongZuoShiJian();
                    schedule.setGongsi(yhRenShiUser.getL().replace("_hr", ""));
                    schedule.setScheduleTitle(department);
                    schedule.setGongZuoShiJianKs(workStartTime);
                    schedule.setGongZuoShiJianJs(workEndTime);
                    schedule.setWuXiuShiJianKs(breakStartTime);
                    schedule.setWuXiuShiJianJs(breakEndTime);

                    // 将selectedDates列表转换为JSON字符串
                    String workDaysJson = convertListToJson(selectedDates);
                    schedule.setWorkDays(workDaysJson); // 直接设置JSON字符串

                    schedule.setRepeatType("none");
                    schedule.setScheduleStatus("active");

                    // 获取年月
                    if (!selectedDates.isEmpty()) {
                        String firstDate = selectedDates.get(0);
                        String[] parts = firstDate.split("-");
                        schedule.setYearMonth(parts[0] + "-" + parts[1]);
                        schedule.setRiqi(firstDate);
                    }

                    boolean success = gongZuoShiJianService.insert(schedule);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                ToastUtil.show(GongZuoShiJianActivity.this, "保存成功");
                                selectedDates.clear();
                                generateCalendar();
                                loadSchedules();
                            } else {
                                ToastUtil.show(GongZuoShiJianActivity.this, "保存失败");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(GongZuoShiJianActivity.this, "保存异常");
                        }
                    });
                }
            }
        }).start();
    }

    // 添加辅助方法：将List转换为JSON字符串
    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append("\"").append(list.get(i)).append("\"");
        }
        json.append("]");
        return json.toString();
    }

    private void editWorkTime(final GongZuoShiJian schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑工作时间");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_time, null);
        final TextView tvStartTime = dialogView.findViewById(R.id.tv_start_time);
        final TextView tvEndTime = dialogView.findViewById(R.id.tv_end_time);

        tvStartTime.setText(schedule.getGongZuoShiJianKs());
        tvEndTime.setText(schedule.getGongZuoShiJianJs());

        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerForEdit("start", tvStartTime, schedule);
            }
        });

        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerForEdit("end", tvEndTime, schedule);
            }
        });

        builder.setView(dialogView);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateWorkTime(schedule, tvStartTime.getText().toString(), tvEndTime.getText().toString());
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showTimePickerForEdit(final String type, final TextView textView, final GongZuoShiJian schedule) {
        String currentTime = textView.getText().toString();
        String[] parts = currentTime.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                textView.setText(time);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void updateWorkTime(final GongZuoShiJian schedule, final String startTime, final String endTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = gongZuoShiJianService.updateWorkTime(schedule.getId(), startTime, endTime);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            // 更新本地数据
                            schedule.setGongZuoShiJianKs(startTime);
                            schedule.setGongZuoShiJianJs(endTime);
                            updateScheduleList();
                            ToastUtil.show(GongZuoShiJianActivity.this, "修改成功");
                        } else {
                            ToastUtil.show(GongZuoShiJianActivity.this, "修改失败");
                        }
                    }
                });
            }
        }).start();
    }

    private void editWorkDays(final GongZuoShiJian schedule) {
        currentEditSchedule = schedule;

        // 解析JSON字符串到列表
        editWorkDaysSelectedDates.clear();
        editWorkDaysOriginalDates.clear();

        String workDaysJson = schedule.getWorkDays();
        if (workDaysJson != null && !workDaysJson.isEmpty()) {
            try {
                String cleanJson = workDaysJson
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .trim();

                if (!cleanJson.isEmpty()) {
                    String[] dates = cleanJson.split(",");
                    for (String date : dates) {
                        String trimmedDate = date.trim();
                        if (!trimmedDate.isEmpty()) {
                            editWorkDaysSelectedDates.add(trimmedDate);
                            editWorkDaysOriginalDates.add(trimmedDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 显示编辑对话框
        showEditWorkDaysDialog();
    }

    // 显示编辑工作日对话框
    private void showEditWorkDaysDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑工作日 - " + currentEditSchedule.getScheduleTitle());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_work_days, null);
        builder.setView(dialogView);

        // 初始化视图
        final GridLayout calendarGrid = dialogView.findViewById(R.id.edit_calendar_grid);
        final TextView tvEditMonth = dialogView.findViewById(R.id.tv_edit_month);
        final Button btnPrevMonth = dialogView.findViewById(R.id.btn_edit_prev_month);
        final Button btnNextMonth = dialogView.findViewById(R.id.btn_edit_next_month);
        final Button btnSelectAll = dialogView.findViewById(R.id.btn_select_all);
        final Button btnClearAll = dialogView.findViewById(R.id.btn_clear_all);
        final TextView tvSelectedCount = dialogView.findViewById(R.id.tv_selected_count);
        final Button btnSave = dialogView.findViewById(R.id.btn_save_edit);
        final Button btnCancel = dialogView.findViewById(R.id.btn_cancel_edit);

        // 初始化日历
        final int[] editCurrentYear = {Calendar.getInstance().get(Calendar.YEAR)};
        final int[] editCurrentMonth = {Calendar.getInstance().get(Calendar.MONTH) + 1};

        // 生成日历
        generateEditCalendar(calendarGrid, tvEditMonth, editCurrentYear[0], editCurrentMonth[0], tvSelectedCount);

        // 上个月
        btnPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCurrentMonth[0]--;
                if (editCurrentMonth[0] < 1) {
                    editCurrentMonth[0] = 12;
                    editCurrentYear[0]--;
                }
                generateEditCalendar(calendarGrid, tvEditMonth, editCurrentYear[0], editCurrentMonth[0], tvSelectedCount);
            }
        });

        // 下个月
        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCurrentMonth[0]++;
                if (editCurrentMonth[0] > 12) {
                    editCurrentMonth[0] = 1;
                    editCurrentYear[0]++;
                }
                generateEditCalendar(calendarGrid, tvEditMonth, editCurrentYear[0], editCurrentMonth[0], tvSelectedCount);
            }
        });

        // 全选本月
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllInCurrentMonth(editCurrentYear[0], editCurrentMonth[0]);
                generateEditCalendar(calendarGrid, tvEditMonth, editCurrentYear[0], editCurrentMonth[0], tvSelectedCount);
            }
        });

        // 清空所有
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWorkDaysSelectedDates.clear();
                generateEditCalendar(calendarGrid, tvEditMonth, editCurrentYear[0], editCurrentMonth[0], tvSelectedCount);
            }
        });

        // 保存
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkDaysEdit();
            }
        });

        // 取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWorkDaysDialog.dismiss();
            }
        });

        editWorkDaysDialog = builder.create();
        editWorkDaysDialog.show();
    }

    // 生成编辑日历
    private void generateEditCalendar(GridLayout calendarGrid, TextView tvMonth,
                                      int year, int month, TextView tvSelectedCount) {
        calendarGrid.removeAllViews();
        tvMonth.setText(year + "年" + month + "月");

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 调整周一开始
        int offset = (firstDayOfWeek == Calendar.SUNDAY) ? 6 : firstDayOfWeek - 2;

        // 添加上个月的最后几天
        Calendar prevMonth = Calendar.getInstance();
        prevMonth.set(year, month - 2, 1);
        int prevDaysInMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = offset; i >= 0; i--) {
            View dayView = getLayoutInflater().inflate(R.layout.item_calendar_day_edit, null);
            TextView tvDay = dayView.findViewById(R.id.tv_day_edit);

            int day = prevDaysInMonth - i;
            tvDay.setText(String.valueOf(day));
            tvDay.setTextColor(getResources().getColor(R.color.blue));
            dayView.setEnabled(false);

            calendarGrid.addView(dayView);
        }

        // 添加当前月
        Calendar today = Calendar.getInstance();
        for (int day = 1; day <= daysInMonth; day++) {
            View dayView = getLayoutInflater().inflate(R.layout.item_calendar_day_edit, null);
            TextView tvDay = dayView.findViewById(R.id.tv_day_edit);

            tvDay.setText(String.valueOf(day));

            // 检查是否为今天
            if (year == today.get(Calendar.YEAR) &&
                    month == today.get(Calendar.MONTH) + 1 &&
                    day == today.get(Calendar.DAY_OF_MONTH)) {
                dayView.setBackgroundResource(R.drawable.today_bg);
            }

            // 检查是否已选中
            final String dateStr = formatDate(year, month, day);
            if (editWorkDaysSelectedDates.contains(dateStr)) {
                dayView.setBackgroundResource(R.drawable.selected_day_bg);
            }

            // 设置点击事件
            dayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editWorkDaysSelectedDates.contains(dateStr)) {
                        editWorkDaysSelectedDates.remove(dateStr);
                        v.setBackgroundResource(0);
                    } else {
                        editWorkDaysSelectedDates.add(dateStr);
                        v.setBackgroundResource(R.drawable.selected_day_bg);
                    }
                    tvSelectedCount.setText("已选择 " + editWorkDaysSelectedDates.size() + " 天");
                }
            });

            calendarGrid.addView(dayView);
        }

        // 更新选中计数
        tvSelectedCount.setText("已选择 " + editWorkDaysSelectedDates.size() + " 天");
    }

    // 全选当前月
    private void selectAllInCurrentMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= daysInMonth; day++) {
            String dateStr = formatDate(year, month, day);
            if (!editWorkDaysSelectedDates.contains(dateStr)) {
                editWorkDaysSelectedDates.add(dateStr);
            }
        }
    }

    // 保存工作日编辑
    private void saveWorkDaysEdit() {
        // 检查是否有修改
        boolean hasChanged = false;

        // 先比较大小
        if (editWorkDaysSelectedDates.size() != editWorkDaysOriginalDates.size()) {
            hasChanged = true;
        } else {
            // 再比较内容
            List<String> sortedSelected = new ArrayList<>(editWorkDaysSelectedDates);
            List<String> sortedOriginal = new ArrayList<>(editWorkDaysOriginalDates);
            Collections.sort(sortedSelected);
            Collections.sort(sortedOriginal);

            for (int i = 0; i < sortedSelected.size(); i++) {
                if (!sortedSelected.get(i).equals(sortedOriginal.get(i))) {
                    hasChanged = true;
                    break;
                }
            }
        }

        if (!hasChanged) {
            ToastUtil.show(this, "没有修改");
            editWorkDaysDialog.dismiss();
            return;
        }

        // 转换为JSON字符串
        String workDaysJson = convertListToJson(editWorkDaysSelectedDates);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = gongZuoShiJianService.updateWorkDays(currentEditSchedule.getId(), workDaysJson);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            ToastUtil.show(GongZuoShiJianActivity.this, "保存成功");
                            // 更新本地数据
                            currentEditSchedule.setWorkDays(workDaysJson);
                            // 刷新列表
                            updateScheduleList();
                            // 重新生成日历
                            generateCalendar();
                        } else {
                            ToastUtil.show(GongZuoShiJianActivity.this, "保存失败");
                        }
                        editWorkDaysDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    // 更新日历选择状态
    private void updateCalendarSelection() {
        // 重新生成日历以更新选择状态
        generateCalendar();
    }

    // 辅助方法：格式化日期
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    private void editBreakTime(final GongZuoShiJian schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑午休时间");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_time, null);
        final TextView tvStartTime = dialogView.findViewById(R.id.tv_start_time);
        final TextView tvEndTime = dialogView.findViewById(R.id.tv_end_time);

        tvStartTime.setText(schedule.getWuXiuShiJianKs());
        tvEndTime.setText(schedule.getWuXiuShiJianJs());

        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerForEdit("break_start", tvStartTime, schedule);
            }
        });

        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerForEdit("break_end", tvEndTime, schedule);
            }
        });

        builder.setView(dialogView);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateBreakTime(schedule, tvStartTime.getText().toString(), tvEndTime.getText().toString());
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void updateBreakTime(final GongZuoShiJian schedule, final String startTime, final String endTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                schedule.setWuXiuShiJianKs(startTime);
                schedule.setWuXiuShiJianJs(endTime);

                boolean success = gongZuoShiJianService.update(schedule);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            ToastUtil.show(GongZuoShiJianActivity.this, "修改成功");
                            loadSchedules();
                        } else {
                            ToastUtil.show(GongZuoShiJianActivity.this, "修改失败");
                        }
                    }
                });
            }
        }).start();
    }

    private void deleteSchedule(final GongZuoShiJian schedule) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这个工作安排吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean success = gongZuoShiJianService.delete(schedule.getId());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (success) {
                                            ToastUtil.show(GongZuoShiJianActivity.this, "删除成功");
                                            loadSchedules();
                                            generateCalendar();
                                        } else {
                                            ToastUtil.show(GongZuoShiJianActivity.this, "删除失败");
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showDateRangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("日期范围选择");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_date_range, null);
        builder.setView(dialogView);

        // 初始化视图
        final TextView tvStartDate = dialogView.findViewById(R.id.tv_start_date);
        final TextView tvEndDate = dialogView.findViewById(R.id.tv_end_date);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group);
        final TextView tvDateCount = dialogView.findViewById(R.id.tv_date_count);
        final ListView listDates = dialogView.findViewById(R.id.list_dates);
        final Button btnApply = dialogView.findViewById(R.id.btn_apply);
        final Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        // 设置默认日期
        Calendar calendar = Calendar.getInstance();
        tvStartDate.setText(formatDate(calendar.getTime()));
        calendar.add(Calendar.MONTH, 1);
        tvEndDate.setText(formatDate(calendar.getTime()));

        // 日期选择
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvStartDate, true);
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvEndDate, false);
            }
        });

        // 单选按钮变化
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                calculateDateRange(tvStartDate.getText().toString(),
                        tvEndDate.getText().toString(), checkedId, tvDateCount, listDates);
            }
        });

        // 计算初始日期范围
        calculateDateRange(tvStartDate.getText().toString(),
                tvEndDate.getText().toString(),
                radioGroup.getCheckedRadioButtonId(),
                tvDateCount, listDates);

        // 应用按钮
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedDatesFromList = getSelectedDatesFromList(listDates);
                if (selectedDatesFromList.isEmpty()) {
                    ToastUtil.show(GongZuoShiJianActivity.this, "请先选择有效日期");
                    return;
                }

                // 清空原来的选择，并添加新的选择
                selectedDates.clear();
                selectedDates.addAll(selectedDatesFromList);

                // 更新日历显示
                generateCalendar(); // 直接调用generateCalendar()来刷新日历

                dateRangeDialog.dismiss();

                // 显示时间设置面板
                if (!selectedDates.isEmpty()) {
                    showTimeSettingDialog();
                }

                ToastUtil.show(GongZuoShiJianActivity.this,
                        "已选择" + selectedDates.size() + "天");
            }
        });

        // 取消按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateRangeDialog.dismiss();
            }
        });

        dateRangeDialog = builder.create();
        dateRangeDialog.show();
    }

    // 日期选择器
    private void showDatePicker(final TextView textView, final boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        try {
            String currentDate = textView.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(currentDate);
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        textView.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    // 计算日期范围
    private void calculateDateRange(String startDateStr, String endDateStr,
                                    int checkedId, TextView tvDateCount, ListView listDates) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            if (startDate.after(endDate)) {
                ToastUtil.show(this, "开始日期不能晚于结束日期");
                return;
            }

            List<String> dateList = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            // 获取筛选选项
            String filterOption = "all";
            switch (checkedId) {
                case R.id.rb_all:
                    filterOption = "all";
                    break;
                case R.id.rb_exclude_sat:
                    filterOption = "excludeSat";
                    break;
                case R.id.rb_exclude_sun:
                    filterOption = "excludeSun";
                    break;
                case R.id.rb_weekends:
                    filterOption = "weekends";
                    break;
                case R.id.rb_weekdays:
                    filterOption = "weekdays";
                    break;
            }

            while (!calendar.getTime().after(endDate)) {
                String currentDate = sdf.format(calendar.getTime());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                boolean shouldInclude = true;

                switch (filterOption) {
                    case "excludeSat":
                        shouldInclude = dayOfWeek != Calendar.SATURDAY;
                        break;
                    case "excludeSun":
                        shouldInclude = dayOfWeek != Calendar.SUNDAY;
                        break;
                    case "weekends":
                        shouldInclude = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
                        break;
                    case "weekdays":
                        shouldInclude = dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY;
                        break;
                    case "all":
                    default:
                        shouldInclude = true;
                        break;
                }

                if (shouldInclude) {
                    dateList.add(currentDate);
                }

                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // 更新显示
            tvDateCount.setText("共 " + dateList.size() + " 天");

            // 设置列表适配器
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_multiple_choice, dateList);
            listDates.setAdapter(adapter);
            listDates.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            // 默认全选
            for (int i = 0; i < dateList.size(); i++) {
                listDates.setItemChecked(i, true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(this, "日期格式错误");
        }
    }

    // 从列表获取选中的日期
    private List<String> getSelectedDatesFromList(ListView listView) {
        List<String> selectedDates = new ArrayList<>();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();

        for (int i = 0; i < listView.getCount(); i++) {
            if (listView.isItemChecked(i)) {
                selectedDates.add(adapter.getItem(i));
            }
        }
        return selectedDates;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}