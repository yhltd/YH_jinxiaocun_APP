package com.example.myapplication.scheduling.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.TimeConfig;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.TimeConfigService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class TimeConfigChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private TimeConfig timeconfig;
    private TimeConfigService timeConfigService;

    private EditText week;
    private Spinner morning_start;
    private Spinner morning_end;
    private Spinner noon_start;
    private Spinner noon_end;
    private Spinner night_start;
    private Spinner night_end;

    private List<String> zaoList;
    private List<String> zhongList;
    private List<String> wanList;

    private String morning_start_text;
    private String morning_end_text;
    private String noon_start_text;
    private String noon_end_text;
    private String night_start_text;
    private String night_end_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_config_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        timeConfigService = new TimeConfigService();

        week = findViewById(R.id.week);
        morning_start = findViewById(R.id.morning_start);
        morning_end = findViewById(R.id.morning_end);
        noon_start = findViewById(R.id.noon_start);
        noon_end = findViewById(R.id.noon_end);
        night_start = findViewById(R.id.night_start);
        night_end = findViewById(R.id.night_end);

        zaoList=new ArrayList<>();
        zhongList=new ArrayList<>();
        wanList=new ArrayList<>();

        zaoList.add("06:00");
        zaoList.add("06:15");
        zaoList.add("06:30");
        zaoList.add("06:45");
        zaoList.add("07:00");
        zaoList.add("07:15");
        zaoList.add("07:30");
        zaoList.add("07:45");
        zaoList.add("08:00");
        zaoList.add("08:15");
        zaoList.add("08:30");
        zaoList.add("08:45");
        zaoList.add("09:00");
        zaoList.add("09:15");
        zaoList.add("09:30");
        zaoList.add("09:45");
        zaoList.add("10:00");

        zhongList.add("10:00");
        zhongList.add("10:15");
        zhongList.add("10:30");
        zhongList.add("10:45");
        zhongList.add("11:00");
        zhongList.add("11:15");
        zhongList.add("11:30");
        zhongList.add("11:45");
        zhongList.add("12:00");

        wanList.add("12:00");
        wanList.add("12:15");
        wanList.add("12:30");
        wanList.add("12:45");
        wanList.add("13:00");
        wanList.add("13:15");
        wanList.add("13:30");
        wanList.add("13:45");
        wanList.add("14:00");
        wanList.add("14:15");
        wanList.add("14:30");
        wanList.add("14:45");
        wanList.add("15:00");
        wanList.add("15:15");
        wanList.add("15:30");
        wanList.add("15:45");
        wanList.add("16:00");
        wanList.add("16:15");
        wanList.add("16:30");
        wanList.add("16:45");
        wanList.add("17:00");
        wanList.add("17:15");
        wanList.add("17:30");
        wanList.add("17:45");
        wanList.add("18:00");
        wanList.add("18:15");
        wanList.add("18:30");
        wanList.add("18:45");
        wanList.add("19:00");
        wanList.add("19:15");
        wanList.add("19:30");
        wanList.add("19:45");
        wanList.add("20:00");
        wanList.add("20:15");
        wanList.add("20:30");
        wanList.add("20:45");
        wanList.add("21:00");
        wanList.add("21:15");
        wanList.add("21:30");
        wanList.add("21:45");
        wanList.add("22:00");
        wanList.add("22:15");
        wanList.add("22:30");
        wanList.add("22:45");
        wanList.add("23:00");

        SpinnerAdapter zaoAdapter = null;
        SpinnerAdapter zhongAdapter = null;
        SpinnerAdapter wanAdapter = null;
        zaoAdapter = new ArrayAdapter<String>(TimeConfigChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, zaoList);
        zhongAdapter = new ArrayAdapter<String>(TimeConfigChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, zhongList);
        wanAdapter = new ArrayAdapter<String>(TimeConfigChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, wanList);

        morning_start.setAdapter(zaoAdapter);
        morning_end.setAdapter(zaoAdapter);
        noon_start.setAdapter(zhongAdapter);
        noon_end.setAdapter(zhongAdapter);
        night_start.setAdapter(wanAdapter);
        night_end.setAdapter(wanAdapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            timeconfig = (TimeConfig) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            if (timeconfig.getWeek() == 1) {
                week.setText("星期一");
            } else if (timeconfig.getWeek() == 2) {
                week.setText("星期二");
            } else if (timeconfig.getWeek() == 3) {
                week.setText("星期三");
            } else if (timeconfig.getWeek() == 4) {
                week.setText("星期四");
            } else if (timeconfig.getWeek() == 5) {
                week.setText("星期五");
            } else if (timeconfig.getWeek() == 6) {
                week.setText("星期六");
            } else if (timeconfig.getWeek() == 7) {
                week.setText("星期天");
            }

            morning_start.setSelection(getZaoPosition(timeconfig.getMorning_start()));
            morning_end.setSelection(getZaoPosition(timeconfig.getMorning_end()));
            noon_start.setSelection(getZhongPosition(timeconfig.getNoon_start()));
            noon_end.setSelection(getZhongPosition(timeconfig.getNight_end()));
            night_start.setSelection(getWanPosition(timeconfig.getNight_start()));
            night_end.setSelection(getWanPosition(timeconfig.getNight_end()));

        }
        week.setKeyListener(null);

        morning_start.setOnItemSelectedListener(new morningSSelectedListener());
        morning_end.setOnItemSelectedListener(new morningESelectedListener());
        noon_start.setOnItemSelectedListener(new noonSSelectedListener());
        noon_end.setOnItemSelectedListener(new noonESelectedListener());
        night_start.setOnItemSelectedListener(new nightSSelectedListener());
        night_end.setOnItemSelectedListener(new nightESelectedListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(TimeConfigChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(TimeConfigChangeActivity.this, "保存失败，请稍后再试");
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = timeConfigService.update(timeconfig);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        timeconfig.setMorning_start(morning_start_text);
        timeconfig.setMorning_end(morning_end_text);
        timeconfig.setNoon_start(noon_start_text);
        timeconfig.setNoon_end(noon_end_text);
        timeconfig.setNight_start(night_start_text);
        timeconfig.setNight_end(night_end_text);
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


    private int getZaoPosition(String param) {
        if (zaoList != null) {
            for (int i = 0; i < zaoList.size(); i++) {
                if (param.equals(zaoList.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getZhongPosition(String param) {
        if (zhongList != null) {
            for (int i = 0; i < zhongList.size(); i++) {
                if (param.equals(zhongList.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getWanPosition(String param) {
        if (wanList != null) {
            for (int i = 0; i < wanList.size(); i++) {
                if (param.equals(wanList.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private class morningSSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            morning_start_text = zaoList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class morningESelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            morning_end_text = zaoList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class noonSSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            noon_start_text = zhongList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class noonESelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            noon_end_text = zhongList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class nightSSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            night_start_text = wanList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class nightESelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            night_end_text = wanList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
