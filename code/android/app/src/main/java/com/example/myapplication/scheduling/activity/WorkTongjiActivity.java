package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.TimeConfig;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
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
import java.util.HashSet;
import java.util.List;

public class WorkTongjiActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private WorkDetail workDetail;
    private WorkDetailService workDetailService;
    private TimeConfigService timeConfigService;

    private EditText ks;
    private EditText js;
    private ListView listView;
    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;
    private Button sel_button;

    private List<WorkDetail> list;
    private List<ModuleInfo> moduleList;
    private List<TimeConfig> timeList;

    private String type;
    private String ks_riqi;
    private String js_riqi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_tongji);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        workDetailService = new WorkDetailService();
        timeConfigService = new TimeConfigService();

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        sel_button = findViewById(R.id.sel_button);
        listView = findViewById(R.id.list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        ks = findViewById(R.id.ks);
        js = findViewById(R.id.js);
       // workDetail = (WorkDetail) myApplication.getObj();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        list = (List<WorkDetail>) getIntent().getSerializableExtra("list");
        moduleList = (List<ModuleInfo>) getIntent().getSerializableExtra("moduleList");

        try {
            initList(type);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        showDateOnClick(ks);
        showDateOnClick(js);

        sel_button.setOnClickListener(selClick());
    }

    @SuppressLint("WrongConstant")
    public void switchClick(View v) {
        if(listView_block.getVisibility() == 0){
            listView_block.setVisibility(8);
            list_table.setVisibility(0);
        }else if(listView_block.getVisibility() == 8){
            listView_block.setVisibility(0);
            list_table.setVisibility(8);
        }

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
        public String xiaolv;
        public String modoule;
        public String order_number;
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

    private void initList(String type) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (ks.getText().toString().equals("")) {
            ks_riqi = "1900-01-01";
        } else {
            ks_riqi = df.format(df.parse(ks.getText().toString()));
        }
        if (js.getText().toString().equals("")) {
            js_riqi = "2200-01-01";
        } else {
            js_riqi = df.format(df.parse(js.getText().toString()));
        }
        sel_button.setEnabled(false);
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                DecimalFormat baoliu = new DecimalFormat("#.00");

                List<HashMap<String, Object>> data = new ArrayList<>();
                timeList = new ArrayList<>();
                timeList = timeConfigService.getList(userInfo.getCompany());

                try {
                    List<Pc> pcList = new ArrayList<>();
                    HashSet<String> set = new HashSet<>();
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
                                            set.add(workDetail.getOrder_number());
                                            Pc pc = new Pc();
                                            pc.riqi = ks;
                                            pc.order_number = workDetail.getOrder_number();
                                            pc.num = baoliu.format(hour1 * workDetail.getNum());
                                            pc.xiaolv = workDetail.getNum() + "";
                                            pc.modoule = workDetail.getModule();
                                            pcList.add(pc);
                                            shichang -= hour1;
                                        } else {
                                            set.add(workDetail.getOrder_number());
                                            Pc pc = new Pc();
                                            pc.riqi = ks;
                                            pc.order_number = workDetail.getOrder_number();
                                            pc.num = baoliu.format(shichang * workDetail.getNum());
                                            pc.xiaolv = workDetail.getNum() + "";
                                            pc.modoule = workDetail.getModule();
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
                                        set.add(workDetail.getOrder_number());
                                        Pc pc = new Pc();
                                        pc.riqi = ks;
                                        pc.order_number = workDetail.getOrder_number();
                                        pc.num = baoliu.format(hour1 * workDetail.getNum());
                                        pc.xiaolv = workDetail.getNum() + "";
                                        pc.modoule = workDetail.getModule();
                                        pcList.add(pc);
                                        shichang -= hour1;
                                    } else {
                                        set.add(workDetail.getOrder_number());
                                        Pc pc = new Pc();
                                        pc.riqi = ks;
                                        pc.order_number = workDetail.getOrder_number();
                                        pc.num = baoliu.format(shichang * workDetail.getNum());
                                        pc.xiaolv = workDetail.getNum() + "";
                                        pc.modoule = workDetail.getModule();
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

                        for (String str : set) {
                            double work_num = 0;
                            for (Pc pc : pcList) {
                                if (str.equals(pc.order_number) && ks_riqi.compareTo(pc.riqi) < 0 && js_riqi.compareTo(pc.riqi) > 0) {
                                    work_num+=Double.parseDouble(pc.num);
                                }
                            }
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("order_number", str);
                            item.put("num", baoliu.format(work_num));
                            data.add(item);
                        }
                    } else if (type.equals("排产2")) {
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
                                            set.add(workDetail.getOrder_number());
                                            Pc pc = new Pc();
                                            pc.riqi = ks;
                                            pc.order_number = workDetail.getOrder_number();
                                            pc.num = baoliu.format(hour1 * workDetail.getNum());
                                            pc.xiaolv = workDetail.getNum() + "";
                                            pc.modoule = workDetail.getModule();
                                            pcList.add(pc);
                                            shichang -= hour1;
                                        } else {
                                            set.add(workDetail.getOrder_number());
                                            Pc pc = new Pc();
                                            pc.riqi = ks;
                                            pc.order_number = workDetail.getOrder_number();
                                            pc.num = baoliu.format(shichang * workDetail.getNum());
                                            pc.xiaolv = workDetail.getNum() + "";
                                            pc.modoule = workDetail.getModule();
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
                                        set.add(workDetail.getOrder_number());
                                        Pc pc = new Pc();
                                        pc.riqi = ks;
                                        pc.order_number = workDetail.getOrder_number();
                                        pc.num = baoliu.format(hour1 * workDetail.getNum());
                                        pc.xiaolv = workDetail.getNum() + "";
                                        pc.modoule = workDetail.getModule();
                                        pcList.add(pc);
                                        shichang -= hour1;
                                    } else {
                                        set.add(workDetail.getOrder_number());
                                        Pc pc = new Pc();
                                        pc.riqi = ks;
                                        pc.order_number = workDetail.getOrder_number();
                                        pc.num = baoliu.format(shichang * workDetail.getNum());
                                        pc.xiaolv = workDetail.getNum() + "";
                                        pc.modoule = workDetail.getModule();
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

                        for (String str : set) {
                            double work_num = 0;
                            for (Pc pc : pcList) {
                                if (str.equals(pc.order_number) && ks_riqi.compareTo(pc.riqi) < 0 && js_riqi.compareTo(pc.riqi) > 0) {
                                    work_num+=Double.parseDouble(pc.num);
                                }
                            }
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("order_number", str);
                            item.put("num", baoliu.format(work_num));
                            data.add(item);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(WorkTongjiActivity.this, data, R.layout.work_tongji_row, new String[]{"order_number", "num"}, new int[]{R.id.order_number, R.id.num}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(WorkTongjiActivity.this, data, R.layout.work_tongji_row_block, new String[]{"order_number", "num"}, new int[]{R.id.order_number, R.id.num}) {
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    initList(type);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(WorkTongjiActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


}
