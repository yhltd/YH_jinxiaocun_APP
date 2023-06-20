package com.example.myapplication.scheduling.activity;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.EChartOptionUtil;
import com.example.myapplication.EChartView;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.activity.VoucherSummaryChangeActivity;
import com.example.myapplication.finance.activity.YingFuBaoBiaoActivity;
import com.example.myapplication.finance.service.YhFinanceKeMuZongZhangService;
import com.example.myapplication.jiaowu.activity.JiaoFeiJiLuActivity;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.TimeConfig;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.service.TimeConfigService;
import com.example.myapplication.scheduling.service.WorkDetailService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkChartActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private WorkDetail workDetail;
    private WorkDetailService workDetailService;
    private TimeConfigService timeConfigService;

    private EditText start_date;
    private EditText stop_date;
    private Spinner order_number;
    private Spinner model;
    private Button sel_button;
    private EChartView barChart;

    private String type;
    private String start_dateText;
    private String stop_dateText;
    private String order_numberText;
    private String modelText;

    private List<WorkDetail> list;
    private List<ModuleInfo> moduleList;
    private List<TimeConfig> timeList;


    List<String> orderList;
    String[] modelList;

    private Object[] data1;
    private Object[] data2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_chart);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        workDetailService = new WorkDetailService();
        timeConfigService = new TimeConfigService();

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        barChart = findViewById(R.id.barChart);
        showDateOnClick(start_date);
        showDateOnClick(stop_date);

        order_number = findViewById(R.id.order_number);
        model = findViewById(R.id.model);
        sel_button = findViewById(R.id.sel_button);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        list = (List<WorkDetail>) getIntent().getSerializableExtra("list");
        moduleList = (List<ModuleInfo>) getIntent().getSerializableExtra("moduleList");

        Set hashSet = new HashSet<String>();
        for(int i=0; i<list.size(); i++){
            hashSet.add(list.get(i).getOrder_number());
        }

        orderList = new ArrayList<>(hashSet);

        SpinnerAdapter adapter = new ArrayAdapter<String>(WorkChartActivity.this, android.R.layout.simple_spinner_dropdown_item, orderList);
        order_number.setAdapter(adapter);

        order_number.setOnItemSelectedListener(new typeSelectSelectedListener());

        sel_button.setOnClickListener(selClick());
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList(type);
            }
        };
    }

    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            order_numberText = order_number.getItemAtPosition(position).toString();
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    SpinnerAdapter adapter_name = new ArrayAdapter<String>(WorkChartActivity.this, android.R.layout.simple_spinner_dropdown_item, modelList);
                    model.setAdapter(StringUtils.cast(adapter_name));
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Set hashSet = new HashSet<>();
                    for(int i=0; i<list.size(); i++){
                        if(list.get(i).getOrder_number().equals(order_numberText)){
                            hashSet.add(list.get(i).getModule());
                        }
                    }

                    if (hashSet.size() > 0) {
                        modelList = new String[hashSet.size()];
                        hashSet.toArray(modelList);
                    }

                    Message msg = new Message();
                    msg.obj = "";
                    listLoadHandler.sendMessage(msg);

                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList(String type) {
        sel_button.setEnabled(false);
        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();

        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100-12-31";
        }

        order_numberText = order_number.getSelectedItem().toString();
        modelText = model.getSelectedItem().toString();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                barChart.refreshEchartsWithOption(EChartOptionUtil.getBarChartOptions(data1, data2));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                DecimalFormat baoliu = new DecimalFormat("#.00");
                @SuppressLint("SimpleDateFormat")
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                List<HashMap<String, Object>> data = new ArrayList<>();
                timeList = new ArrayList<>();
                timeList = timeConfigService.getList(userInfo.getCompany());

                try {
                    List<Pc> pcList = new ArrayList<>();
                    if (type.equals("排产1")) {
                        //模块最后的开始时间
                        List<Paichan_modoule_time> pmtList = new ArrayList<>();
                        int num = 0;
                        //模块变量
                        int mokuai = 0;
                        int bianliang = 0;

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

                        for(int i=0; i<list.size(); i++){
                            if(list.get(i).getOrder_number().equals(order_numberText) && list.get(i).getModule().equals(modelText)){
                                workDetail = list.get(i);
                                break;
                            }
                        }
                        List<Object> riqiList = new ArrayList<>();
                        List<Object> numList = new ArrayList<>();

                        Date date1 = null;
                        Date date2 = null;
                        //实现将字符串转成⽇期类型
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        date1 = dateFormat.parse(start_dateText);
                        date2 = dateFormat.parse(stop_dateText);

                        for (Pc pc : pcList) {
                            if (pc.id == workDetail.getId() && pc.modoule_id == workDetail.getModule_id()) {
                                Date this_date = null;
                                this_date = dateFormat.parse(pc.riqi);
                                if(this_date.compareTo(date1) != -1 && this_date.compareTo(date2) != 1){
                                    riqiList.add(pc.riqi);
                                    numList.add(pc.num);
                                }
                            }
                        }
                        data1 = (Object[]) riqiList.stream().toArray();
                        data2 = (Object[]) numList.stream().toArray();
                    } else if (type.equals("排产2")) {

                        //模块最后的开始时间
                        List<Paichan_modoule_time> pmtList = new ArrayList<>();

                        for (WorkDetail workDetail : list) {
                            //订单开始日期
                            String nowDate = df.format(df.parse(workDetail.getWork_start_date()));
                            //总生产数量
                            double lastNum = workDetail.getWork_num();

                            do {
                                int zuihou = 0;
                                boolean pd = false;
                                String ks = nowDate;
                                double time1 = 0;
                                for (int i = 0; i < pmtList.size(); i++) {
                                    if (workDetail.getModule_id() == pmtList.get(i).modoule_id) {
                                        pd = true;
                                        zuihou = i;
                                        if (ks.compareTo(pmtList.get(i).riqi) == 0) {
                                            ks = pmtList.get(i).riqi;
                                            time1 = pmtList.get(i).time;
                                        } else {
                                            time1 = WorkHour(df.parse(ks));
                                        }
                                    }
                                }
                                double hour1 = WorkHour(df.parse(ks));
                                double shengchan = 0;
                                hour1 -= time1;
                                if (workDetail.getNum() != 0) {
                                    shengchan = lastNum / workDetail.getNum();
                                }
                                double workNum = 0;
                                if (shengchan >= hour1) {
                                    workNum = hour1 * workDetail.getNum();
                                    lastNum -= workNum;
                                    time1 = hour1;
                                    Pc pc = new Pc();
                                    pc.id = workDetail.getId();
                                    pc.num = workNum + "";
                                    pc.riqi = ks;
                                    pc.modoule_id = workDetail.getModule_id();
                                    pcList.add(pc);
                                } else {
                                    workNum = shengchan * workDetail.getNum();
                                    lastNum = 0;
                                    time1 = shengchan;
                                    Pc pc = new Pc();
                                    pc.id = workDetail.getId();
                                    pc.num = workNum + "";
                                    pc.riqi = ks;
                                    pc.modoule_id = workDetail.getModule_id();
                                    pcList.add(pc);
                                }

                                if (workNum == 0) {
                                    if (pd) {
                                        pmtList.get(zuihou).riqi = ks;
                                        pmtList.get(zuihou).time = time1;
                                    } else {
                                        Paichan_modoule_time pmt = new Paichan_modoule_time();
                                        pmt.modoule_id = workDetail.getModule_id();
                                        pmt.riqi = ks;
                                        pmt.time = time1;
                                        pmtList.add(pmt);
                                    }
                                }

                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(df.parse(nowDate));
                                calendar.add(calendar.DATE, +1);
                                nowDate = df.format(calendar.getTime());


                            } while (lastNum > 0);
                        }

                        for(int i=0; i<list.size(); i++){
                            if(list.get(i).getOrder_number().equals(order_numberText) && list.get(i).getModule().equals(modelText)){
                                workDetail = list.get(i);
                                break;
                            }
                        }

                        List<Object> riqiList = new ArrayList<>();
                        List<Object> numList = new ArrayList<>();

                        Date date1 = null;
                        Date date2 = null;
                        //实现将字符串转成⽇期类型
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        date1 = dateFormat.parse(start_dateText);
                        date2 = dateFormat.parse(stop_dateText);

                        for (Pc pc : pcList) {
                            if (pc.id == workDetail.getId() && pc.modoule_id == workDetail.getModule_id()) {
                                Date this_date = null;
                                this_date = dateFormat.parse(pc.riqi);
                                if(this_date.compareTo(date1) != -1 && this_date.compareTo(date2) != 1){
                                    riqiList.add(pc.riqi);
                                    numList.add(pc.num);
                                }
                            }
                        }
                        data1 = (Object[]) riqiList.stream().toArray();
                        data2 = (Object[]) numList.stream().toArray();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(WorkChartActivity.this, data, R.layout.work_detail_row, new String[]{"riqi", "num"}, new int[]{R.id.riqi, R.id.num}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(WorkChartActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}

