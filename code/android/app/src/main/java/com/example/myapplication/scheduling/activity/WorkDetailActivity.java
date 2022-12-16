package com.example.myapplication.scheduling.activity;

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
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.service.ModuleInfoService;
import com.example.myapplication.scheduling.service.WorkDetailService;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkDetailActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private WorkDetail workDetail;
    private WorkDetailService workDetailService;
    private ListView listView;

    private List<WorkDetail> list;
    private List<ModuleInfo> moduleList;

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

    public class paichan_modoule_time {
        public int modoule_id;
        public String riqi;
        public double time;
        public double num;


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
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    if (type.equals("排产1")) {
                        //模块最后的开始时间
                        List<paichan_modoule_time> pmtList = new ArrayList<>();
                        int num = 0;
                        //模块变量
                        int mokuai = 0;

                        for (WorkDetail workDetail : list) {
                            //订单开始日期
                            String nowDate = workDetail.getWork_start_date();
                            //总生产数量
                            double lastNum = workDetail.getWork_num();
                            //所需生产小时
                            double xiaoshi = 0;
                            //订单所有模块效率合计
                            double xiaolv = 0;

                            for (int i = 0; i < list.size(); i++) {
                                if (workDetail.getOrder_number().equals(list.get(i).getOrder_number())) {
                                    xiaolv += list.get(i).getNum();
                                }
                            }
                            if(xiaolv!=0){
                                xiaoshi = lastNum / xiaolv;
                            }
                        }

                    } else if (type.equals("排产2")) {

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
}
