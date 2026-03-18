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
                    // 查询数据库中的时间配置
                    List<TimeConfig> dbList = timeConfigService.getList(userInfo.getCompany());

                    // 创建7条默认数据（星期一到星期日）
                    List<TimeConfig> fullList = new ArrayList<>();

                    // 先将数据库中的数据按week映射
                    HashMap<Integer, TimeConfig> dbMap = new HashMap<>();
                    if (dbList != null) {
                        for (TimeConfig config : dbList) {
                            dbMap.put(config.getWeek(), config);
                        }
                    }

                    // 生成完整的7条数据
                    for (int week = 1; week <= 7; week++) {
                        TimeConfig config;
                        if (dbMap.containsKey(week)) {
                            // 数据库中有数据，使用数据库中的数据
                            config = dbMap.get(week);
                        } else {
                            // 数据库中没有数据，创建空的时间配置
                            config = new TimeConfig();
                            config.setWeek(week);
                            config.setCompany(userInfo.getCompany());
                            config.setMorning_start("08:00");
                            config.setMorning_end("12:00");
                            config.setNoon_start("12:00");
                            config.setNoon_end("14:00");
                            config.setNight_start("14:00");
                            config.setNight_end("18:00");
                            // 设置默认值，可以根据实际需求调整
                        }
                        fullList.add(config);
                    }

                    // 使用fullList进行界面显示
                    for (int i = 0; i < fullList.size(); i++) {
                        TimeConfig config = fullList.get(i);
                        HashMap<String, Object> item = new HashMap<>();

                        // 设置星期显示
                        switch (config.getWeek()) {
                            case 1:
                                item.put("week", "星期一");
                                break;
                            case 2:
                                item.put("week", "星期二");
                                break;
                            case 3:
                                item.put("week", "星期三");
                                break;
                            case 4:
                                item.put("week", "星期四");
                                break;
                            case 5:
                                item.put("week", "星期五");
                                break;
                            case 6:
                                item.put("week", "星期六");
                                break;
                            case 7:
                                item.put("week", "星期天");
                                break;
                        }

                        // 设置时间值
                        item.put("morning_start", config.getMorning_start() != null ? config.getMorning_start() : "");
                        item.put("morning_end", config.getMorning_end() != null ? config.getMorning_end() : "");
                        item.put("noon_start", config.getNoon_start() != null ? config.getNoon_start() : "");
                        item.put("noon_end", config.getNoon_end() != null ? config.getNoon_end() : "");
                        item.put("night_start", config.getNight_start() != null ? config.getNight_start() : "");
                        item.put("night_end", config.getNight_end() != null ? config.getNight_end() : "");

                        data.add(item);
                    }

                    // 保存完整的列表供编辑时使用
                    list = fullList;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(TimeConfigActivity.this, data, R.layout.time_config_row,
                        new String[]{"week", "morning_start", "morning_end", "noon_start", "noon_end","night_start","night_end"},
                        new int[]{R.id.week, R.id.morning_start, R.id.morning_end, R.id.noon_start, R.id.noon_end, R.id.night_start,R.id.night_end}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(TimeConfigActivity.this, data, R.layout.time_config_row_block,
                        new String[]{"week", "morning_start", "morning_end", "noon_start", "noon_end","night_start","night_end"},
                        new int[]{R.id.week, R.id.morning_start, R.id.morning_end, R.id.noon_start, R.id.noon_end, R.id.night_start,R.id.night_end}) {
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

                // 传递选中的TimeConfig对象
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));

                // 添加一个标记，用于判断是更新还是新增
                TimeConfig selectedConfig = list.get(position);
                if (selectedConfig.getId() != 0) {  // int类型不能为null，默认值为0
                    intent.putExtra("action", "update");  // 已存在的数据，执行更新
                } else {
                    intent.putExtra("action", "insert");  // 补全的数据，执行新增
                }

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
