package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.ModuleType;
import com.example.myapplication.scheduling.entity.Summary;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.ModuleTypeService;
import com.example.myapplication.scheduling.service.SummaryService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private ModuleTypeService moduleTypeService;
    private SummaryService summaryService;

    private EditText order_id_text;
    private Spinner type_spinner;

    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private Button sel_button;

    List<ModuleType> typeList;
    List<String> tList;
    private int typeId;

    private SpinnerAdapter typeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        order_id_text = findViewById(R.id.order_id_text);
        type_spinner = findViewById(R.id.type_spinner);
        sel_button = findViewById(R.id.sel_button);

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        initList1();
        initList2();

        sel_button.setOnClickListener(selClick());
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

    private void initList1() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                type_spinner.setAdapter(typeAdapter);
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    moduleTypeService = new ModuleTypeService();
                    typeList = moduleTypeService.getList(userInfo.getCompany());
                    tList = new ArrayList<>();
                    tList.add("全部");
                    for (ModuleType moduleType : typeList) {
                        tList.add(moduleType.getName());
                    }
                    typeAdapter = new ArrayAdapter<String>(SummaryActivity.this, android.R.layout.simple_spinner_dropdown_item, tList);

                    summaryService = new SummaryService();
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    try {
                        typeId = 0;
                        List<Summary> list = summaryService.getList(userInfo.getCompany(), order_id_text.getText().toString(), typeId);
                        if (list == null) return;

                        for (int i = 0; i < list.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("type", list.get(i).getType());
                            item.put("name", list.get(i).getName());
                            item.put("num", list.get(i).getNum());
                            item.put("parentName", list.get(i).getParent_name());
                            item.put("workNum", list.get(i).getWork_num());
                            data.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    adapter = new SimpleAdapter(SummaryActivity.this, data, R.layout.summary_row, new String[]{"type", "name", "num", "parentName", "workNum"}, new int[]{R.id.type, R.id.name, R.id.num, R.id.parentName, R.id.workNum}) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                            LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                            linearLayout.setTag(position);
                            return view;
                        }
                    };

                    adapter_block = new SimpleAdapter(SummaryActivity.this, data, R.layout.summary_row_block, new String[]{"type", "name", "num", "parentName", "workNum"}, new int[]{R.id.type, R.id.name, R.id.num, R.id.parentName, R.id.workNum}) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                            LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                            linearLayout.setTag(position);
                            return view;
                        }
                    };

                    msg.obj = adapter;
                    
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initList2() {
        sel_button.setEnabled(false);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                sel_button.setEnabled(true);
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                summaryService = new SummaryService();
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    typeId = 0;
                    if (type_spinner.getSelectedItem().toString().equals("全部")) {
                        typeId = 0;
                    } else {
                        for (ModuleType moduleType : typeList) {
                            if (moduleType.getName().equals(type_spinner.getSelectedItem().toString())) {
                                typeId = moduleType.getId();
                            }
                        }
                    }
                    List<Summary> list = summaryService.getList(userInfo.getCompany(), order_id_text.getText().toString(), typeId);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("type", list.get(i).getType());
                        item.put("name", list.get(i).getName());
                        item.put("num", list.get(i).getNum());
                        item.put("parentName", list.get(i).getParent_name());
                        item.put("workNum", list.get(i).getWork_num());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(SummaryActivity.this, data, R.layout.summary_row, new String[]{"type", "name", "num", "parentName", "workNum"}, new int[]{R.id.type, R.id.name, R.id.num, R.id.parentName, R.id.workNum}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(SummaryActivity.this, data, R.layout.summary_row_block, new String[]{"type", "name", "num", "parentName", "workNum"}, new int[]{R.id.type, R.id.name, R.id.num, R.id.parentName, R.id.workNum}) {
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
                handler.sendMessage(msg);
            }
        }).start();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList2();
            }
        };
    }

}



