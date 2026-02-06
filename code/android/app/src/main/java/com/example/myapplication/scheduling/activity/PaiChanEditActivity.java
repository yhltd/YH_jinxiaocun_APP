package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.OrderInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.MyApplication;
import com.example.myapplication.scheduling.entity.WorkModule;
import com.example.myapplication.scheduling.service.OrderInfoService;
import com.example.myapplication.scheduling.service.WorkDetailService;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaiChanEditActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private WorkDetailService workDetailService;
    private OrderInfoService orderInfoService;

    private Spinner orderSpinner;
    private TextView availableNum;
    private EditText productionNum;
    private Spinner typeSpinner;
    private EditText startDate;
    private EditText endDate;
    private Button saveBtn;
    private Button cancelBtn;

    private List<OrderInfo> orderList;
    private List<String> orderIdList;
    private List<String> typeList;
    private ArrayAdapter<String> orderAdapter;
    private ArrayAdapter<String> typeAdapter;

    private Spinner isInsertSpinner;
    private List<String> isInsertList;
    private ArrayAdapter<String> isInsertAdapter;

    private WorkDetail workDetail;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paichan_edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("排产编辑");
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        workDetailService = new WorkDetailService();
        orderInfoService = new OrderInfoService();

        initViews();
        initListeners();
        initData();
        loadSpinnerData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        orderSpinner = findViewById(R.id.order_spinner);
        availableNum = findViewById(R.id.available_num);
        productionNum = findViewById(R.id.production_num);
        typeSpinner = findViewById(R.id.type_spinner);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        saveBtn = findViewById(R.id.save_btn);
        cancelBtn = findViewById(R.id.cancel_btn);

        // 初始化类型列表
        typeList = new ArrayList<>();
        typeList.add("正常");
        typeList.add("优先");
        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        typeSpinner.setAdapter(typeAdapter);

        // 初始化插单列表
        isInsertSpinner = findViewById(R.id.is_insert_spinner);
        isInsertList = new ArrayList<>();
        isInsertList.add("否");
        isInsertList.add("是");
        isInsertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, isInsertList);
        isInsertSpinner.setAdapter(isInsertAdapter);
    }

    private void initListeners() {
        saveBtn.setOnClickListener(v -> {
            if (validateForm()) {
                saveWorkDetail();
            }
        });

        cancelBtn.setOnClickListener(v -> finish());

        startDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDateTimePicker(startDate);
                    return true;
                }
                return false;
            }
        });

        endDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDateTimePicker(endDate);
                    return true;
                }
                return false;
            }
        });

        orderSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (orderList != null && position < orderList.size()) {
                    OrderInfo order = orderList.get(position);
                    workDetail.setOrder_id(order.getId());
                    workDetail.setOrder_number(order.getOrder_id());
                    availableNum.setText(String.valueOf(order.getSet_num()));
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        typeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                workDetail.setType(typeList.get(position));
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        isInsertSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                workDetail.setIs_insert(position); // 0表示否，1表示是
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if ("edit".equals(action)) {
            // 编辑模式
            workDetail = (WorkDetail) intent.getSerializableExtra("workDetail");
            if (workDetail != null) {
                fillFormData();
            }
        } else {
            // 新增模式
            workDetail = new WorkDetail();
            workDetail.setCompany(userInfo.getCompany());
        }
    }

    private void fillFormData() {
        // 设置生产数量
        productionNum.setText(String.valueOf(workDetail.getWork_num()));

        // 设置开始日期
        if (workDetail.getWork_start_date() != null && !workDetail.getWork_start_date().isEmpty()) {
            startDate.setText(formatDateForDisplay(workDetail.getWork_start_date()));
        }

        // 设置结束日期
        if (workDetail.getJiezhishijian() != null && !workDetail.getJiezhishijian().isEmpty()) {
            endDate.setText(formatDateForDisplay(workDetail.getJiezhishijian()));
        }

        // 设置类型
        if (workDetail.getType() != null) {
            for (int i = 0; i < typeList.size(); i++) {
                if (typeList.get(i).equals(workDetail.getType())) {
                    typeSpinner.setSelection(i);
                    break;
                }
            }
        }

        // 设置是否插单
        if (workDetail.getIs_insert() == 0) {
            isInsertSpinner.setSelection(0); // 否
        } else if (workDetail.getIs_insert() == 1) {
            isInsertSpinner.setSelection(1); // 是
        }

        // 设置类型显示文本
        if (workDetail.getType() != null) {
            // 将数据库的英文值转换为中文显示
            String displayType = workDetail.getType();
            if ("urgent".equals(displayType)) {
                displayType = "优先";
            } else if ("normal".equals(displayType)) {
                displayType = "正常";
            }

            for (int i = 0; i < typeList.size(); i++) {
                if (typeList.get(i).equals(displayType)) {
                    typeSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private String formatDateForDisplay(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        try {
            // 从数据库格式中提取日期和时间部分，格式化为 yyyy-MM-dd HH:mm
            if (dateString.contains(" ")) {
                String[] parts = dateString.split(" ");
                if (parts.length >= 2) {
                    // 获取日期部分
                    String datePart = parts[0];

                    // 获取时间部分，处理可能包含的秒和毫秒
                    String timePart = parts[1];
                    if (timePart.contains(":")) {
                        // 只保留小时和分钟部分
                        String[] timeParts = timePart.split(":");
                        if (timeParts.length >= 2) {
                            return datePart + " " + timeParts[0] + ":" + timeParts[1];
                        }
                    }
                    return datePart + " " + timePart;
                }
            }
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return dateString;
        }
    }

    private void loadSpinnerData() {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                orderSpinner.setAdapter(orderAdapter);

                // 如果是编辑模式，设置选中的订单号
                if ("edit".equals(action) && workDetail != null && orderIdList != null) {
                    String orderNumber = workDetail.getOrder_number();
                    for (int i = 0; i < orderIdList.size(); i++) {
                        if (orderIdList.get(i).equals(orderNumber)) {
                            orderSpinner.setSelection(i);
                            break;
                        }
                    }
                }

                return true;
            }
        });

        new Thread(() -> {
            try {
                orderList = orderInfoService.getOrderId();
                orderIdList = new ArrayList<>();
                for (OrderInfo order : orderList) {
                    orderIdList.add(order.getOrder_id());
                }
                orderAdapter = new ArrayAdapter<>(PaiChanEditActivity.this,
                        android.R.layout.simple_spinner_item, orderIdList);

                handler.sendMessage(new Message());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean validateForm() {
        if (orderSpinner.getSelectedItem() == null) {
            ToastUtil.show(this, "请选择订单号");
            return false;
        }

        String prodNumStr = productionNum.getText().toString().trim();
        if (prodNumStr.isEmpty()) {
            ToastUtil.show(this, "请输入生产数量");
            return false;
        }

        try {
            int prodNum = Integer.parseInt(prodNumStr);
            String availableStr = availableNum.getText().toString();

            if (!availableStr.isEmpty()) {
                int available = Integer.parseInt(availableStr);
                if (prodNum > available) {
                    ToastUtil.show(this, "生产数量不能大于可生产数量");
                    return false;
                }
            }

            if (prodNum <= 0) {
                ToastUtil.show(this, "生产数量必须大于0");
                return false;
            }

            workDetail.setWork_num(prodNum);
        } catch (NumberFormatException e) {
            ToastUtil.show(this, "生产数量格式错误");
            return false;
        }

        if (typeSpinner.getSelectedItem() == null) {
            ToastUtil.show(this, "请选择类型");
            return false;
        }

        if (isInsertSpinner.getSelectedItem() == null) {
            ToastUtil.show(this, "请选择是否插单");
            return false;
        }

        String typeValue = typeSpinner.getSelectedItem().toString();
        if (typeValue.equals("优先")) {
            workDetail.setType("urgent");
        } else if (typeValue.equals("正常")) {
            workDetail.setType("normal");
        } else {
            workDetail.setType(typeValue);
        }


        String startDateStr = startDate.getText().toString().trim();
        if (startDateStr.isEmpty()) {
            ToastUtil.show(this, "请选择开始生产日期");
            return false;
        }
// 确保时间格式正确，包含分钟部分
        if (!startDateStr.contains(":")) {
            startDateStr += " 00:00";
        }
// 补充秒和毫秒
        workDetail.setWork_start_date(startDateStr + ":00.0000000");

        String endDateStr = endDate.getText().toString().trim();
        if (endDateStr.isEmpty()) {
            ToastUtil.show(this, "请选择预计结束时间");
            return false;
        }
// 确保时间格式正确，包含分钟部分
        if (!endDateStr.contains(":")) {
            endDateStr += " 00:00";
        }
        workDetail.setJiezhishijian(endDateStr + ":00.0000000");

        // 设置默认的模块ID（从原始代码中看，需要模块ID）
        workDetail.setModule_id(1); // 这里需要根据实际情况设置

        return true;
    }

    private void saveWorkDetail() {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean result = (boolean) msg.obj;
                if (result) {
                    ToastUtil.show(PaiChanEditActivity.this, "保存成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtil.show(PaiChanEditActivity.this, "保存失败");
                }
                return true;
            }
        });

        new Thread(() -> {
            Message msg = new Message();
            boolean result = false;

            try {
                if ("add".equals(action)) {
                    // 新增模式
                    // 1. 先插入work_detail表
                    if (workDetail.getRow_num() == 0) {
                        // 获取最大行号，需要先查询
                        List<WorkDetail> lastList = workDetailService.getLastList(userInfo.getCompany());
                        int nextRowNum = 1;
                        if (lastList != null && !lastList.isEmpty()) {
                            nextRowNum = lastList.get(0).getRow_num() + 1;
                        }
                        workDetail.setRow_num(nextRowNum);
                    }

                    // 插入work_detail表
                    result = workDetailService.insert(workDetail);

                    // 2. 如果插入成功，获取插入的work_id并插入work_module表
                    if (result && workDetail.getModule_id() > 0) {
                        // 获取刚插入的work_id
                        List<WorkDetail> newList = workDetailService.getLastList(userInfo.getCompany());
                        if (newList != null && !newList.isEmpty()) {
                            int newWorkId = newList.get(0).getId();

                            // 插入work_module表
                            WorkModule workModule = new WorkModule();
                            workModule.setWork_id(newWorkId);
                            workModule.setModule_id(workDetail.getModule_id());
                            workDetailService.insertModule(workModule);
                        }
                    }
                } else {
                    // 编辑模式
                    // 更新work_detail表的所有字段
                    result = workDetailService.update(workDetail);

                }
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }

            msg.obj = result;
            handler.sendMessage(msg);
        }).start();
    }

    private void showDateTimePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        final Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        // 选择时间
                        TimePickerDialog timePickerDialog = new TimePickerDialog(PaiChanEditActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        selectedDate.set(Calendar.MINUTE, minute);

                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        editText.setText(sdf.format(selectedDate.getTime()));
                                    }
                                }, hour, minute, true);
                        timePickerDialog.show();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}