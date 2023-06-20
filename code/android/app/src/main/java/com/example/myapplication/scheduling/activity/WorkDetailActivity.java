package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.scheduling.entity.BomInfo;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.TimeConfig;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.service.ModuleInfoService;
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
import java.util.List;

public class WorkDetailActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private WorkDetail workDetail;
    private WorkDetailService workDetailService;
    private TimeConfigService timeConfigService;
    private ListView listView;

    private List<WorkDetail> list;
    private List<ModuleInfo> moduleList;
    private List<TimeConfig> timeList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        workDetailService = new WorkDetailService();
        timeConfigService = new TimeConfigService();

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        listView = findViewById(R.id.work_detail_list);

        workDetail = (WorkDetail) myApplication.getObj();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        list = (List<WorkDetail>) getIntent().getSerializableExtra("list");
        moduleList = (List<ModuleInfo>) getIntent().getSerializableExtra("moduleList");
        initList(type);
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
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
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

                        for (Pc pc : pcList) {
                            if (pc.id == workDetail.getId() && pc.modoule_id == workDetail.getModule_id()) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("riqi", pc.riqi);
                                item.put("num", pc.num);
                                data.add(item);
                            }
                        }
                    } else if (type.equals("排产2")) {
//                        //模块最后的开始时间
//                        List<Paichan_modoule_time> pmtList = new ArrayList<>();
//                        int num = 0;
//                        //模块变量
//                        int mokuai = 0;
//                        int bianliang = 0;

//                        for (WorkDetail workDetail : list) {
//                            //订单开始日期
//                            String nowDate = df.format(df.parse(workDetail.getWork_start_date()));
//                            //总生产数量
//                            double lastNum = workDetail.getWork_num();
//                            //所需生产小时
//                            double xiaoshi = 0;
//                            //订单所有模块效率合计
//                            double xiaolv = 0;
//
//                            for (int i = 0; i < list.size(); i++) {
//                                if (workDetail.getOrder_number().equals(list.get(i).getOrder_number()) && workDetail.getWork_num() == list.get(i).getWork_num()) {
//                                    xiaolv += list.get(i).getNum();
//                                }
//                            }
//
//                            if (xiaolv != 0) {
//                                xiaoshi = lastNum / xiaolv;
//                            }
//
//                            boolean pd = false;
//                            double shichang = xiaoshi;
//                            double shijianbianliang = 0;
//                            String ks = nowDate;
//
//                            for (int i = 0; i < pmtList.size(); i++) {
//                                if (workDetail.getModule_id() == pmtList.get(i).modoule_id) {
//                                    if (ks.compareTo(pmtList.get(i).riqi) <= 0) {
//                                        ks = pmtList.get(i).riqi;
//                                    }
//
//                                    int dowhile = 0;
//                                    do {
//                                        double hour1 = WorkHour(df.parse(ks));
//                                        if (dowhile == 0) {
//                                            hour1 = pmtList.get(i).time;
//                                            dowhile += 1;
//                                        }
//                                        if (shichang >= hour1) {
//                                            Pc pc = new Pc();
//                                            pc.id = workDetail.getId();
//                                            pc.num = baoliu.format(hour1 * workDetail.getNum());
//                                            pc.riqi = ks;
//                                            pc.modoule_id = workDetail.getModule_id();
//                                            pcList.add(pc);
//                                            shichang -= hour1;
//                                        } else {
//                                            Pc pc = new Pc();
//                                            pc.id = workDetail.getId();
//                                            pc.num = baoliu.format(shichang * workDetail.getNum());
//                                            pc.riqi = ks;
//                                            pc.modoule_id = workDetail.getModule_id();
//                                            pcList.add(pc);
//                                            shijianbianliang = hour1 - shichang;
//                                            shichang = 0;
//                                        }
//                                        Calendar calendar = new GregorianCalendar();
//                                        calendar.setTime(df.parse(ks));
//                                        calendar.add(calendar.DATE, 1);
//                                        ks = df.format(calendar.getTime());
//                                    } while (shichang > 0);
//                                    bianliang += 1;
//                                    Calendar calendar = new GregorianCalendar();
//                                    calendar.setTime(df.parse(ks));
//                                    calendar.add(calendar.DATE, -1);
//                                    ks = df.format(calendar.getTime());
//
//                                    pmtList.get(i).riqi = ks;
//                                    pmtList.get(i).time = shijianbianliang;
//                                    pd = true;
//                                }
//                            }
//                            if (!pd) {
//                                do {
//                                    double hour1 = WorkHour(df.parse(ks));
//                                    if (shichang >= hour1) {
//                                        Pc pc = new Pc();
//                                        pc.id = workDetail.getId();
//                                        pc.num = baoliu.format(hour1 * workDetail.getNum());
//                                        pc.riqi = ks;
//                                        pc.modoule_id = workDetail.getModule_id();
//                                        pcList.add(pc);
//                                        shichang -= hour1;
//                                    } else {
//                                        Pc pc = new Pc();
//                                        pc.id = workDetail.getId();
//                                        pc.num = baoliu.format(shichang * workDetail.getNum());
//                                        pc.riqi = ks;
//                                        pc.modoule_id = workDetail.getModule_id();
//                                        pcList.add(pc);
//                                        shijianbianliang = hour1 - shichang;
//                                        shichang = 0;
//                                    }
//                                    Calendar calendar = new GregorianCalendar();
//                                    calendar.setTime(df.parse(ks));
//                                    calendar.add(calendar.DATE, 1);
//                                    ks = df.format(calendar.getTime());
//                                } while (shichang > 0);
//                                Calendar calendar = new GregorianCalendar();
//                                calendar.setTime(df.parse(ks));
//                                calendar.add(calendar.DATE, -1);
//                                ks = df.format(calendar.getTime());
//                                bianliang += 1;
//                                Paichan_modoule_time pmt = new Paichan_modoule_time();
//                                pmt.modoule_id = workDetail.getModule_id();
//                                pmt.riqi = ks;
//                                pmt.time = shijianbianliang;
//                                pmtList.add(pmt);
//                            }
//                        }

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
                        for (Pc pc : pcList) {
                            if (pc.id == workDetail.getId() && pc.modoule_id == workDetail.getModule_id()) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("riqi", pc.riqi);
                                item.put("num", pc.num);
                                data.add(item);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(WorkDetailActivity.this, data, R.layout.work_detail_row, new String[]{"riqi", "num"}, new int[]{R.id.riqi, R.id.num}) {
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


}

