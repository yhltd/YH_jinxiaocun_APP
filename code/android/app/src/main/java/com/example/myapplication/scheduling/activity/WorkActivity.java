package com.example.myapplication.scheduling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.service.ModuleInfoService;
import com.example.myapplication.scheduling.service.WorkDetailService;
import com.example.myapplication.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private WorkDetailService workDetailService;
    private ModuleInfoService moduleInfoService;

    private EditText order_id_text;
    private ListView listView;
    private Button sel_button;
    private Button pc_button;

    private List<WorkDetail> list;
    private List<ModuleInfo> moduleList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        order_id_text = findViewById(R.id.order_id_text);
        listView = findViewById(R.id.work_list);
        sel_button = findViewById(R.id.sel_button);
        pc_button = findViewById(R.id.pc_button);

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        initList();
        sel_button.setOnClickListener(selClick());
        pc_button.setOnClickListener(changePc());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
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
                    workDetailService = new WorkDetailService();
                    moduleInfoService = new ModuleInfoService();
                    list = workDetailService.getList(userInfo.getCompany(), order_id_text.getText().toString());
                    if (list == null) return;
                    moduleList = moduleInfoService.getList(userInfo.getCompany(), "全部");

                    for (int i = 0; i < list.size(); i++) {
                        if (moduleList != null) {
                            for (ModuleInfo moduleInfo : moduleList) {
                                if (list.get(i).getModule_id() == moduleInfo.getId()) {
                                    list.get(i).setModule(moduleInfo.getName());
                                    list.get(i).setNum(moduleInfo.getNum());
                                }
                            }
                        }
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("row_num", list.get(i).getRow_num());
                        item.put("orderId", list.get(i).getOrder_number());
                        item.put("module", list.get(i).getModule());
                        item.put("num", list.get(i).getNum());
                        item.put("work_num", list.get(i).getWork_num());
                        item.put("type", list.get(i).getType());
                        item.put("work_start_date", list.get(i).getWork_start_date());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(WorkActivity.this, data, R.layout.work_row, new String[]{"row_num", "orderId", "module", "num", "work_num", "type", "work_start_date"}, new int[]{R.id.row_num, R.id.orderId, R.id.module, R.id.num, R.id.work_num, R.id.type, R.id.work_start_date}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        //linearLayout.setOnLongClickListener(onItemLongClick());
                        linearLayout.setOnClickListener(pcDetail());
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public View.OnClickListener pcDetail() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(WorkActivity.this, WorkDetailActivity.class);
                intent.putExtra("type", pc_button.getText().toString());
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("moduleList", (Serializable) moduleList);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public View.OnClickListener changePc() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pc_button.getText().toString().equals("排产1")) {
                    pc_button.setText("排产2");
                } else {
                    pc_button.setText("排产1");
                }
            }
        };
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
