package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.TimeConfig;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.PaibanDetailService;
import com.example.myapplication.scheduling.service.TimeConfigService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeConfigActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private Department department;
    private TimeConfigService timeConfigService;

    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private List<TimeConfig> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_config);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        department = myApplication.getPcDepartment();
        listView = findViewById(R.id.time_list);

        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);

        initList();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    timeConfigService = new TimeConfigService();
                    list = timeConfigService.getList(userInfo.getCompany());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        if (list.get(i).getWeek()==1){
                            item.put("week", "星期一");
                        }else if(list.get(i).getWeek()==2){
                            item.put("week", "星期二");
                        }else if(list.get(i).getWeek()==3){
                            item.put("week", "星期三");
                        }else if(list.get(i).getWeek()==4){
                            item.put("week", "星期四");
                        }else if(list.get(i).getWeek()==5){
                            item.put("week", "星期五");
                        }else if(list.get(i).getWeek()==6){
                            item.put("week", "星期六");
                        }else if(list.get(i).getWeek()==7){
                            item.put("week", "星期天");
                        }
                        item.put("morning_start", list.get(i).getMorning_start());
                        item.put("morning_end", list.get(i).getMorning_end());
                        item.put("noon_start", list.get(i).getNoon_start());
                        item.put("noon_end", list.get(i).getNoon_end());
                        item.put("night_start", list.get(i).getNight_start());
                        item.put("night_end", list.get(i).getNight_end());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(TimeConfigActivity.this, data, R.layout.time_config_row, new String[]{"week", "morning_start", "morning_end", "noon_start", "noon_end","night_start","night_end"}, new int[]{R.id.week, R.id.morning_start, R.id.morning_end, R.id.noon_start, R.id.noon_end, R.id.night_start,R.id.night_end}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(TimeConfigActivity.this, data, R.layout.time_config_row_block, new String[]{"week", "morning_start", "morning_end", "noon_start", "noon_end","night_start","night_end"}, new int[]{R.id.week, R.id.morning_start, R.id.morning_end, R.id.noon_start, R.id.noon_end, R.id.night_start,R.id.night_end}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
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

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!department.getUpd().equals("是")){
                    ToastUtil.show(TimeConfigActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(TimeConfigActivity.this, TimeConfigChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void holidayClick(View v) {
        Intent intent = new Intent(TimeConfigActivity.this, HolidayActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                initList();
            }
        }
    }

}
