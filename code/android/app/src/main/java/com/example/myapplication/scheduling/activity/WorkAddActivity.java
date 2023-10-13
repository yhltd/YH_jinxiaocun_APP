package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.OrderInfo;
import com.example.myapplication.scheduling.entity.TimeConfig;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.entity.WorkModule;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.scheduling.service.ModuleInfoService;
import com.example.myapplication.scheduling.service.OrderInfoService;
import com.example.myapplication.scheduling.service.TimeConfigService;
import com.example.myapplication.scheduling.service.WorkDetailService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class WorkAddActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private WorkDetail workDetail;
    private WorkModule workModule;
    private WorkDetailService workDetailService;
    private OrderInfoService orderInfoService;
    private ModuleInfoService moduleInfoService;

    private Spinner is_insert;
    private Spinner row_num;
    private Spinner order_id;
    private Spinner module;
    private TextView shengchan_num;
    private EditText num;
    private EditText riqi;
    private EditText js_riqi;
    private TextView row_num_textview;

    private List<String> insertList;
    private List<OrderInfo> orderInfoList;
    private List<ModuleInfo> moduleInfoList;
    private List<WorkDetail> workDetailList;

    private List<WorkDetail> list;
    private List<ModuleInfo> moduleList;
    private List<TimeConfig> timeList;

    private TimeConfigService timeConfigService;

    private SpinnerAdapter orderAdapter;
    private SpinnerAdapter moduleAdapter;
    private SpinnerAdapter rowNumAdapter;

    private int order_id_;
    private int module_id;

    private String type;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_add);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        workDetailService = new WorkDetailService();
        orderInfoService = new OrderInfoService();
        moduleInfoService = new ModuleInfoService();
        timeConfigService = new TimeConfigService();

        is_insert = findViewById(R.id.is_insert);
        row_num = findViewById(R.id.row_num);
        order_id = findViewById(R.id.order_id);
        module = findViewById(R.id.module);
        shengchan_num = findViewById(R.id.shengchan_num);
        num = findViewById(R.id.num);
        riqi = findViewById(R.id.riqi);
        js_riqi = findViewById(R.id.js_riqi);

        row_num_textview = findViewById(R.id.row_num_textview);

        insertList = new ArrayList<>();
        insertList.add("否");
        insertList.add("是");

        SpinnerAdapter adapter = new ArrayAdapter<String>(WorkAddActivity.this, android.R.layout.simple_spinner_dropdown_item, insertList);
        is_insert.setAdapter(adapter);

        workDetail = new WorkDetail();
        workModule = new WorkModule();

        init();

        is_insert.setOnItemSelectedListener(new isInsertSelectedListener());
        order_id.setOnItemSelectedListener(new orderSelectedListener());
        module.setOnItemSelectedListener(new moduleSelectedListener());

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        list = (List<WorkDetail>) getIntent().getSerializableExtra("list");
        moduleList = (List<ModuleInfo>) getIntent().getSerializableExtra("moduleList");

        showDateOnClick(riqi);
        showDateOnClick(js_riqi);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class Paichan_modoule_time {
        public int modoule_id;
        public String riqi;
        public double time;
        public double num;
    }

    public class Pc {
        public int id;
        public String riqi;
        public String num;
        public int modoule_id;
    }

    private void init() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                order_id.setAdapter(orderAdapter);
                module.setAdapter(moduleAdapter);
                row_num.setAdapter(rowNumAdapter);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    List<String> oList = new ArrayList<>();
                    List<String> mList = new ArrayList<>();
                    List<Integer> iList = new ArrayList<>();
                    orderInfoList = orderInfoService.getOrderId();
                    if (orderInfoList != null) {
                        for (OrderInfo orderInfo : orderInfoList) {
                            oList.add(orderInfo.getOrder_id());
                        }
                    }
                    orderAdapter = new ArrayAdapter<String>(WorkAddActivity.this, android.R.layout.simple_spinner_dropdown_item, oList);

                    moduleInfoList = moduleInfoService.getList(userInfo.getCompany(), "全部");
                    if (moduleInfoList != null) {
                        for (ModuleInfo moduleInfo : moduleInfoList) {
                            mList.add(moduleInfo.getName());
                        }
                    }
                    moduleAdapter = new ArrayAdapter<String>(WorkAddActivity.this, android.R.layout.simple_spinner_dropdown_item, mList);

                    workDetailList = workDetailService.getList(userInfo.getCompany(), "");
                    if (workDetailList != null) {
                        for (WorkDetail workDetail : workDetailList) {
                            iList.add(workDetail.getRow_num());
                        }
                    }
                    rowNumAdapter = new ArrayAdapter<Integer>(WorkAddActivity.this, android.R.layout.simple_spinner_dropdown_item, iList);
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(WorkAddActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(WorkAddActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = workDetailService.insert(workDetail);

                if (is_insert.getSelectedItem().toString().equals("否")) {
                    List<WorkDetail> newList = workDetailService.getLastList(userInfo.getCompany());
                    if (newList != null) {
                        newList.get(0).setRow_num(newList.get(0).getId() + 1);
                        workDetailService.update(newList.get(0));
                        workModule.setWork_id(newList.get(0).getId());
                        workDetailService.insertModule(workModule);
                    }
                }
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void channengClick(View v) throws ParseException {
        if (!checkForm()) return;
        if(js_riqi.getText().toString().equals("")){
            ToastUtil.show(WorkAddActivity.this, "请选择预计结束时间");
            return;
        }
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (Boolean.valueOf(msg.obj.toString())) {
                    ToastUtil.show(WorkAddActivity.this, "产能足够！");
                } else {
                    ToastUtil.show(WorkAddActivity.this, "产能不足！");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                DecimalFormat baoliu = new DecimalFormat("#.00");
                @SuppressLint("SimpleDateFormat")
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                timeList = new ArrayList<>();
                timeList = timeConfigService.getList(userInfo.getCompany());
                boolean panduan = true;
                try {
                    List<Pc> pcList = new ArrayList<>();
                    //模块最后的开始时间
                    List<Paichan_modoule_time> pmtList = new ArrayList<>();
                    int num = 0;
                    //模块变量
                    int mokuai = 0;
                    int bianliang = 0;
                    list.add(workDetail);
                    for (WorkDetail workDetail : list) {
                        //订单开始日期
                        String nowDate = df.format(df.parse(workDetail.getWork_start_date()));
                        //总生产数量
                        double lastNum = workDetail.getWork_num();
                        //所需生产小时
                        double xiaoshi = 0;
                        //订单所有模块效率合计
                        double xiaolv = 0;

                        for (int i = 0; i < list.size(); i++) {
                            if (workDetail.getOrder_number().equals(list.get(i).getOrder_number()) && workDetail.getWork_num() == list.get(i).getWork_num()) {
                                xiaolv += list.get(i).getNum();
                            }
                        }

                        if (xiaolv != 0) {
                            xiaoshi = lastNum / xiaolv;
                        }

                        boolean pd = false;
                        double shichang = xiaoshi;
                        double shijianbianliang = 0;
                        String ks = nowDate;

                        for (int i = 0; i < pmtList.size(); i++) {
                            if (workDetail.getModule_id() == pmtList.get(i).modoule_id) {
                                if (ks.compareTo(pmtList.get(i).riqi) <= 0) {
                                    ks = pmtList.get(i).riqi;
                                }

                                int dowhile = 0;
                                do {
                                    double hour1 = WorkHour(df.parse(ks));
                                    if (dowhile == 0) {
                                        hour1 = pmtList.get(i).time;
                                        dowhile += 1;
                                    }
                                    if (shichang >= hour1) {
                                        Pc pc = new Pc();
                                        pc.id = workDetail.getId();
                                        pc.num = baoliu.format(hour1 * workDetail.getNum());
                                        pc.riqi = ks;
                                        pc.modoule_id = workDetail.getModule_id();
                                        pcList.add(pc);
                                        shichang -= hour1;
                                    } else {
                                        Pc pc = new Pc();
                                        pc.id = workDetail.getId();
                                        pc.num = baoliu.format(shichang * workDetail.getNum());
                                        pc.riqi = ks;
                                        pc.modoule_id = workDetail.getModule_id();
                                        pcList.add(pc);
                                        shijianbianliang = hour1 - shichang;
                                        shichang = 0;
                                    }
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.setTime(df.parse(ks));
                                    calendar.add(calendar.DATE, 1);
                                    ks = df.format(calendar.getTime());
                                } while (shichang > 0);
                                bianliang += 1;
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(df.parse(ks));
                                calendar.add(calendar.DATE, -1);
                                ks = df.format(calendar.getTime());

                                pmtList.get(i).riqi = ks;
                                pmtList.get(i).time = shijianbianliang;
                                pd = true;
                            }
                        }
                        if (!pd) {
                            do {
                                double hour1 = WorkHour(df.parse(ks));
                                if (shichang >= hour1) {
                                    Pc pc = new Pc();
                                    pc.id = workDetail.getId();
                                    pc.num = baoliu.format(hour1 * workDetail.getNum());
                                    pc.riqi = ks;
                                    pc.modoule_id = workDetail.getModule_id();
                                    pcList.add(pc);
                                    shichang -= hour1;
                                } else {
                                    Pc pc = new Pc();
                                    pc.id = workDetail.getId();
                                    pc.num = baoliu.format(shichang * workDetail.getNum());
                                    pc.riqi = ks;
                                    pc.modoule_id = workDetail.getModule_id();
                                    pcList.add(pc);
                                    shijianbianliang = hour1 - shichang;
                                    shichang = 0;
                                }
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(df.parse(ks));
                                calendar.add(calendar.DATE, 1);
                                ks = df.format(calendar.getTime());
                            } while (shichang > 0);
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(df.parse(ks));
                            calendar.add(calendar.DATE, -1);
                            ks = df.format(calendar.getTime());
                            bianliang += 1;
                            Paichan_modoule_time pmt = new Paichan_modoule_time();
                            pmt.modoule_id = workDetail.getModule_id();
                            pmt.riqi = ks;
                            pmt.time = shijianbianliang;
                            pmtList.add(pmt);
                        }
                    }

                    if (pcList.get(pcList.size() - 1).riqi.compareTo(df.format(df.parse(js_riqi.getText().toString()))) < 0) {
                        ToastUtil.show(WorkAddActivity.this, "产能足够！");
                        panduan = true;
                    } else {
                        ToastUtil.show(WorkAddActivity.this, "产能不足！");
                        panduan = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.obj = panduan;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }


    private class isInsertSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            if (insertList.get(position).equals("是")) {
                row_num_textview.setVisibility(View.VISIBLE);
                row_num.setVisibility(View.VISIBLE);
            } else {
                row_num_textview.setVisibility(View.GONE);
                row_num.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class orderSelectedListener implements AdapterView.OnItemSelectedListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            if (orderInfoList != null) {
                order_id_ = orderInfoList.get(position).getId();
                shengchan_num.setText(orderInfoList.get(position).getSet_num() + "");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class moduleSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            if (moduleInfoList != null) {
                module_id = moduleInfoList.get(position).getId();
                workDetail.setNum(moduleInfoList.get(position).getNum());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private boolean checkForm() throws ParseException {
        if (module.getSelectedItem().toString().equals("")) {
            ToastUtil.show(WorkAddActivity.this, "请选择使用模块");
            return false;
        } else {
            workDetail.setModule_id(module_id);
            workModule.setModule_id(module_id);
        }

        if (order_id.getSelectedItem().toString().equals("")) {
            ToastUtil.show(WorkAddActivity.this, "请选择订单号");
            return false;
        } else {
            workDetail.setOrder_id(order_id_);
            workDetail.setOrder_number(order_id.getSelectedItem().toString());
        }


        if (num.getText().toString().equals("")) {
            ToastUtil.show(WorkAddActivity.this, "请输入生产数量");
            return false;
        } else {
            if (Double.parseDouble(num.getText().toString()) > Double.parseDouble(shengchan_num.getText().toString())) {
                ToastUtil.show(WorkAddActivity.this, "生产数量不能大于可生产数量");
                return false;
            } else {
                workDetail.setWork_num(Integer.parseInt(num.getText().toString()));
            }
        }


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
        if (riqi.getText().toString().equals("")) {
            ToastUtil.show(WorkAddActivity.this, "请输入开始生产时间");
            return false;
        } else {
            workDetail.setWork_start_date(spd.format(spd.parse(riqi.getText().toString())) + " 00:00:00.0000000");
        }

        workDetail.setCompany(userInfo.getCompany());

        if (is_insert.getSelectedItem().equals("是")) {
            workDetail.setIs_insert(1);
        } else {
            workDetail.setIs_insert(0);
        }

        workDetail.setType(type);

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(WorkAddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private double WorkHour(Date riqi) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("HH:mm");
        long hours = 0;
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        int[] weekDaysCode = {7, 1, 2, 3, 4, 5, 6};

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(riqi);

        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        for (int i = 0; i < timeList.size(); i++) {
            if (timeList.get(i).getWeek() == weekDaysCode[intWeek]) {
                long diff = df.parse(timeList.get(i).getMorning_end()).getTime() - df.parse(timeList.get(i).getMorning_start()).getTime() + df.parse(timeList.get(i).getNoon_end()).getTime() - df.parse(timeList.get(i).getNoon_start()).getTime() + df.parse(timeList.get(i).getNight_end()).getTime() - df.parse(timeList.get(i).getNight_start()).getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            }
        }

        return (double) hours;

    }

}
