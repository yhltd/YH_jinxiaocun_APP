package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.service.ModuleInfoService;
import com.example.myapplication.scheduling.service.WorkDetailService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private Department department;
    private WorkDetailService workDetailService;
    private ModuleInfoService moduleInfoService;

    private EditText order_id_text;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

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
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
        pc_button = findViewById(R.id.pc_button);

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        department = myApplication.getPcDepartment();

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

                adapter = new SimpleAdapter(WorkActivity.this, data, R.layout.work_row, new String[]{"row_num", "orderId", "module", "num", "work_num", "type", "work_start_date"}, new int[]{R.id.row_num, R.id.orderId, R.id.module, R.id.num, R.id.work_num, R.id.type, R.id.work_start_date}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
                        linearLayout.setOnClickListener(pcDetail());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(WorkActivity.this, data, R.layout.work_row_block, new String[]{"row_num", "orderId", "module", "num", "work_num", "type", "work_start_date"}, new int[]{R.id.row_num, R.id.orderId, R.id.module, R.id.num, R.id.work_num, R.id.type, R.id.work_start_date}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
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

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!department.getDel().equals("是")){
                    ToastUtil.show(WorkActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(WorkActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(WorkActivity.this, "删除成功");
                            initList();
                        }
                        return true;
                    }
                });

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("编号:");
                        xiangQingYe.setB_title("订单号:");
                        xiangQingYe.setC_title("所属模块:");
                        xiangQingYe.setD_title("模块效率/时:");
                        xiangQingYe.setE_title("生产数量:");
                        xiangQingYe.setF_title("类型:");
                        xiangQingYe.setG_title("开始生产日期:");

                        xiangQingYe.setA(String.valueOf(list.get(position).getRow_num()));
                        xiangQingYe.setB(list.get(position).getOrder_number());
                        xiangQingYe.setC(list.get(position).getModule());
                        xiangQingYe.setD(String.valueOf(list.get(position).getNum()));
                        xiangQingYe.setE(String.valueOf(list.get(position).getWork_num()));
                        xiangQingYe.setF(list.get(position).getType());
                        xiangQingYe.setG(list.get(position).getWork_start_date());

                        Intent intent = new Intent(WorkActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.obj = workDetailService.delete(list.get(position).getOrder_number(), userInfo.getCompany());
                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("确定要删除此订单吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
            }
        };
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

    public void insertClick(View v) {
        if(!department.getAdd().equals("是")){
            ToastUtil.show(WorkActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(WorkActivity.this, WorkAddActivity.class);
        intent.putExtra("type", pc_button.getText().toString());
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("moduleList", (Serializable) moduleList);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public void insertPlClick(View v) {
        if(!department.getAdd().equals("是")){
            ToastUtil.show(WorkActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(WorkActivity.this, WorkPlAddActivity.class);
        intent.putExtra("type", pc_button.getText().toString());
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public void planClick(View v) {
        Intent intent = new Intent(WorkActivity.this, WorkPlanActivity.class);
        intent.putExtra("type", pc_button.getText().toString());
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("moduleList", (Serializable) moduleList);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public void tongjiClick(View v) {
        Intent intent = new Intent(WorkActivity.this, WorkTongjiActivity.class);
        intent.putExtra("type", pc_button.getText().toString());
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("moduleList", (Serializable) moduleList);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public void gotoChart(View v) {
        Intent intent = new Intent(WorkActivity.this, WorkChartActivity.class);
        intent.putExtra("type", pc_button.getText().toString());
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("moduleList", (Serializable) moduleList);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }
}
