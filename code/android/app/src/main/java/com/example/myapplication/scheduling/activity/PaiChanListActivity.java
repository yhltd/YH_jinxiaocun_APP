package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.service.WorkDetailService;
import com.example.myapplication.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaiChanListActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private Department department;
    private WorkDetailService workDetailService;

    private EditText searchOrderId;
    private Button searchBtn;
    private Button addBtn;
    private ListView listView;

    private List<WorkDetail> workList;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paichan_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("排产管理");
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        department = myApplication.getPcDepartment();

        workDetailService = new WorkDetailService();

        initViews();
        initListeners();
        loadData("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        searchOrderId = findViewById(R.id.search_order_id);
        searchBtn = findViewById(R.id.search_btn);
        addBtn = findViewById(R.id.add_btn);
        listView = findViewById(R.id.paichan_listview);
    }

    private void initListeners() {
        searchBtn.setOnClickListener(v -> {
            String orderId = searchOrderId.getText().toString().trim();
            loadData(orderId);
        });


        addBtn.setOnClickListener(v -> {
            if(!department.getAdd().equals("是")){
                ToastUtil.show(PaiChanListActivity.this, "无权限！");
                return;
            }
            Intent intent = new Intent(PaiChanListActivity.this, PaiChanEditActivity.class);
            intent.putExtra("action", "add");
            startActivityForResult(intent, REQUEST_CODE_CHANG);
        });

        Button paichanBtn = findViewById(R.id.paichan_btn);
        paichanBtn.setOnClickListener(v -> {
            // 跳转到排产结果页面
            Intent intent = new Intent(PaiChanListActivity.this, PaiChanResultActivity.class);
            startActivity(intent);
        });
    }

    private void loadData(String orderId) {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(adapter);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    workList = workDetailService.getList(userInfo.getCompany(), orderId);
                    if (workList == null) return;

                    for (int i = 0; i < workList.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("row_num", i + 1);
                        item.put("order_id", workList.get(i).getOrder_number());
                        item.put("work_num", workList.get(i).getWork_num());

                        // 修改类型显示
                        String typeDisplay = workList.get(i).getType();
                        if ("urgent".equals(typeDisplay)) {
                            item.put("type", "优先");
                        } else if ("normal".equals(typeDisplay)) {
                            item.put("type", "正常");
                        } else {
                            item.put("type", typeDisplay);
                        }

                        // 添加是否插单显示
                        int isInsertValue = workList.get(i).getIs_insert();
                        item.put("is_insert", isInsertValue == 1 ? "是" : "否");

                        // 修改日期格式显示，包含时间
                        item.put("work_start_date", formatDateTime(workList.get(i).getWork_start_date()));
                        item.put("end_date", formatDateTime(workList.get(i).getJiezhishijian()));
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(PaiChanListActivity.this, data, R.layout.paichan_row,
                        new String[]{"row_num", "order_id", "work_num", "type", "is_insert", "work_start_date", "end_date"},
                        new int[]{R.id.row_num, R.id.order_id, R.id.work_num, R.id.type, R.id.is_insert, R.id.work_start_date, R.id.end_date}) {
                    @Override
                    public View getView(int position, View convertView, android.view.ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        view.setOnLongClickListener(onItemLongClick());
                        view.setTag(position);
                        return view;
                    }
                };

                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    private String formatDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return "";
        }
        try {
            // 显示完整日期时间，格式：yyyy-MM-dd HH:mm
            if (dateTimeString.contains(" ")) {
                String[] parts = dateTimeString.split(" ");
                if (parts.length >= 2) {
                    String timePart = parts[1].split("\\.")[0]; // 去掉毫秒
                    if (timePart.length() > 5) {
                        timePart = timePart.substring(0, 5); // 只保留小时和分钟
                    }
                    return parts[0] + " " + timePart;
                }
            }
            return dateTimeString;
        } catch (Exception e) {
            return dateTimeString;
        }
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        // 简单格式化日期，去掉时间部分
        try {
            if (dateString.contains(" ")) {
                return dateString.split(" ")[0];
            }
            return dateString;
        } catch (Exception e) {
            return dateString;
        }
    }

    private View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!department.getDel().equals("是")){
                    ToastUtil.show(PaiChanListActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(PaiChanListActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                WorkDetail workDetail = workList.get(position);

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDetail(workDetail);
                    }
                });

                builder.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editWorkDetail(workDetail);
                    }
                });

                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDeleteDialog(workDetail);
                    }
                });

                builder.setMessage("请选择操作");
                builder.setTitle("操作选项");
                builder.show();
                return true;
            }
        };
    }

    private void showDetail(WorkDetail workDetail) {
        XiangQingYe xiangQingYe = new XiangQingYe();

        xiangQingYe.setA_title("编号:");
        xiangQingYe.setB_title("订单号:");
        xiangQingYe.setC_title("生产数量:");
        xiangQingYe.setD_title("类型:");
        xiangQingYe.setE_title("是否插单:");
        xiangQingYe.setF_title("开始生产日期:");
        xiangQingYe.setG_title("预计结束时间:");

        int position = -1;
        for (int i = 0; i < workList.size(); i++) {
            if (workList.get(i).getId() == workDetail.getId()) {
                position = i;
                break;
            }
        }

        // 使用位置+1作为编号
        if (position >= 0) {
            xiangQingYe.setA(String.valueOf(position + 1));
        } else {
            xiangQingYe.setA(String.valueOf(workDetail.getRow_num()));
        }
        xiangQingYe.setB(workDetail.getOrder_number());
        xiangQingYe.setC(String.valueOf(workDetail.getWork_num()));

        // 修改类型显示
        if ("urgent".equals(workDetail.getType())) {
            xiangQingYe.setD("优先");
        } else if ("normal".equals(workDetail.getType())) {
            xiangQingYe.setD("正常");
        } else {
            xiangQingYe.setD(workDetail.getType());
        }

        // 添加是否插单显示
        xiangQingYe.setE(workDetail.getIs_insert() == 1 ? "是" : "否");

        // 修改日期显示，包含时间
        xiangQingYe.setF(formatDateTime(workDetail.getWork_start_date()));
        xiangQingYe.setG(formatDateTime(workDetail.getJiezhishijian()));

        Intent intent = new Intent(PaiChanListActivity.this, XiangQingYeActivity.class);
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setObj(xiangQingYe);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    private void editWorkDetail(WorkDetail workDetail) {
        Intent intent = new Intent(PaiChanListActivity.this, PaiChanEditActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("workDetail", (Serializable) workDetail);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    private void showDeleteDialog(WorkDetail workDetail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认删除");
        builder.setMessage("确定要删除订单号 " + workDetail.getOrder_number() + " 的排产信息吗？");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteWorkDetail(workDetail);
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void deleteWorkDetail(WorkDetail workDetail) {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean result = (boolean) msg.obj;
                if (result) {
                    ToastUtil.show(PaiChanListActivity.this, "删除成功");
                    loadData("");
                } else {
                    ToastUtil.show(PaiChanListActivity.this, "删除失败");
                }
                return true;
            }
        });

        new Thread(() -> {
            Message msg = new Message();
            msg.obj = workDetailService.delete(workDetail.getOrder_number(), userInfo.getCompany());
            handler.sendMessage(msg);
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                loadData("");
            }
        }
    }
}