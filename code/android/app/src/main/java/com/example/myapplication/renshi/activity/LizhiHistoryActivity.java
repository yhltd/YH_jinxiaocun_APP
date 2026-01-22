package com.example.myapplication.renshi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiLizhiShenQing;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiLizhiShenQingService;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class LizhiHistoryActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiLizhiShenQingService service;

    private EditText etName;
    private EditText etDepartment;
    private Button btnSearch;

    private TextView tvHistoryTitle;
    private LinearLayout containerRecords;
    private TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lizhi_history);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 获取用户信息
        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        // 初始化Service
        service = new YhRenShiLizhiShenQingService();

        initView();
        setupListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etDepartment = findViewById(R.id.et_department);
        btnSearch = findViewById(R.id.btn_search);

        tvHistoryTitle = findViewById(R.id.tv_history_title);
        containerRecords = findViewById(R.id.container_records);
        tvNoData = findViewById(R.id.tv_no_data);

        // 初始状态：显示提示信息，隐藏标题
        tvHistoryTitle.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
    }

    private void setupListeners() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWithConditions();
            }
        });
    }

    // 搜索条件查询
    private void searchWithConditions() {
        String nameText = etName.getText().toString().trim();
        String departmentText = etDepartment.getText().toString().trim();

        // 组合查询：必须同时输入姓名和部门
        if (nameText.isEmpty() && departmentText.isEmpty()) {
            ToastUtil.show(this, "请输入姓名和部门进行搜索");
            return;
        }

        // 必须同时输入两个条件
        if (nameText.isEmpty()) {
            ToastUtil.show(this, "请输入姓名进行搜索");
            return;
        }

        if (departmentText.isEmpty()) {
            ToastUtil.show(this, "请输入部门进行搜索");
            return;
        }

        // 执行组合查询
        searchHistory(nameText, departmentText);
    }

    // 搜索历史记录（现在只接收2个参数）
//    private void searchHistory(String name, String department) {
//        showLoading();
//
//        Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                List<YhRenShiLizhiShenQing> historyList = (List<YhRenShiLizhiShenQing>) msg.obj;
//                displayHistory(historyList, department); // 传入部门参数用于前端过滤
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 获取公司名，并去掉_hr后缀
//                String companyName = yhRenShiUser.getL();
//                if (companyName != null && companyName.endsWith("_hr")) {
//                    companyName = companyName.replace("_hr", "");
//                }
//
//                // 调用Service查询数据
//                List<YhRenShiLizhiShenQing> historyList;
//
//                if (name.isEmpty()) {
//                    // 如果姓名为空，查询所有记录（前端再过滤部门）
//                    historyList = service.getList(
//                            companyName,  // 公司（已去掉_hr后缀）
//                            "",           // 姓名（空表示查询所有）
//                            "1900-01-01", // 默认开始日期
//                            "2100-12-31"  // 默认结束日期
//                    );
//                } else {
//                    // 按姓名查询
//                    historyList = service.getList(
//                            companyName,  // 公司（已去掉_hr后缀）
//                            name,         // 姓名
//                            "1900-01-01", // 默认开始日期
//                            "2100-12-31"  // 默认结束日期
//                    );
//                }
//
//                Message msg = new Message();
//                msg.obj = historyList;
//                handler.sendMessage(msg);
//            }
//        }).start();
//    }
    private void searchHistory(String name, String department) {
        showLoading();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取公司名，并去掉_hr后缀
                String companyName = yhRenShiUser.getL();
                if (companyName != null && companyName.endsWith("_hr")) {
                    companyName = companyName.replace("_hr", "");
                }

                // 调用Service查询数据
                List<YhRenShiLizhiShenQing> historyList = service.getList(
                        companyName,  // 公司（已去掉_hr后缀）
                        name,         // 姓名
                        "1900-01-01", // 默认开始日期
                        "2100-12-31"  // 默认结束日期
                );

                // 在前端进行部门过滤
                List<YhRenShiLizhiShenQing> filteredList = new ArrayList<>();
                for (YhRenShiLizhiShenQing record : historyList) {
                    if (record.getBumen() != null &&
                            record.getBumen().toLowerCase().contains(department.toLowerCase())) {
                        filteredList.add(record);
                    }
                }

                // 使用 runOnUiThread 更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayHistory(filteredList);  // 传递已经过滤后的列表
                    }
                });
            }
        }).start();
    }
    // 显示加载状态
    private void showLoading() {
        containerRecords.removeAllViews();
        tvNoData.setText("搜索中...");
        tvNoData.setVisibility(View.VISIBLE);
        tvHistoryTitle.setVisibility(View.GONE);
        btnSearch.setEnabled(false);
    }

    // 显示历史记录
//    private void displayHistory(List<YhRenShiLizhiShenQing> historyList, String departmentFilter) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                btnSearch.setEnabled(true);
//
//                if (historyList == null || historyList.isEmpty()) {
//                    tvNoData.setText("没有找到符合条件的记录");
//                    tvNoData.setVisibility(View.VISIBLE);
//                    tvHistoryTitle.setVisibility(View.GONE);
//                    return;
//                }
//
//                // 清空容器
//                containerRecords.removeAllViews();
//
//                int count = 0;
//                // 显示所有记录（前端过滤部门）
//                for (YhRenShiLizhiShenQing record : historyList) {
//                    // 前端过滤部门（如果输入了部门）
//                    if (!departmentFilter.isEmpty()) {
//                        if (record.getBumen() == null ||
//                                !record.getBumen().toLowerCase().contains(departmentFilter.toLowerCase())) {
//                            continue; // 跳过不匹配的记录
//                        }
//                    }
//
//                    addHistoryItem(record);
//                    count++;
//                }
//
//                // 显示结果
//                if (count == 0) {
//                    tvNoData.setText("没有找到符合条件的记录");
//                    tvNoData.setVisibility(View.VISIBLE);
//                    tvHistoryTitle.setVisibility(View.GONE);
//                } else {
//                    tvNoData.setVisibility(View.GONE);
//                    tvHistoryTitle.setVisibility(View.VISIBLE);
//                    tvHistoryTitle.setText("搜索结果（共" + count + "条）");
//                }
//            }
//        });
//    }
    private void displayHistory(List<YhRenShiLizhiShenQing> historyList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnSearch.setEnabled(true);

                if (historyList == null || historyList.isEmpty()) {
                    tvNoData.setText("没有找到符合条件的记录");
                    tvNoData.setVisibility(View.VISIBLE);
                    tvHistoryTitle.setVisibility(View.GONE);
                    return;
                }

                // 清空容器
                containerRecords.removeAllViews();

                int count = 0;
                // 显示所有记录（已经在前端过滤过了）
                for (YhRenShiLizhiShenQing record : historyList) {
                    addHistoryItem(record);
                    count++;
                }

                // 显示结果
                if (count == 0) {
                    tvNoData.setText("没有找到符合条件的记录");
                    tvNoData.setVisibility(View.VISIBLE);
                    tvHistoryTitle.setVisibility(View.GONE);
                } else {
                    tvNoData.setVisibility(View.GONE);
                    tvHistoryTitle.setVisibility(View.VISIBLE);
                    tvHistoryTitle.setText("搜索结果（共" + count + "条）");
                }
            }
        });
    }
    // 添加一个历史记录项
    private void addHistoryItem(YhRenShiLizhiShenQing record) {
        // 创建历史记录项布局
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setBackgroundResource(R.drawable.border);
        itemLayout.setPadding(16, 12, 16, 12);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 8;
        itemLayout.setLayoutParams(params);

        // 第一行：部门和状态
        LinearLayout row1 = new LinearLayout(this);
        row1.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        row1.setOrientation(LinearLayout.HORIZONTAL);

        // 部门
        TextView tvBumen = new TextView(this);
        tvBumen.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        tvBumen.setText("部门：" + getSafeString(record.getBumen()));
        tvBumen.setTextSize(14);
        tvBumen.setTextColor(0xFF333333);
        row1.addView(tvBumen);

        // 状态
        TextView tvStatus = new TextView(this);
        String status = getSafeString(record.getShenpijieguo());
        tvStatus.setText("状态：" + status);
        tvStatus.setTextSize(14);

        // 根据状态设置颜色
        if ("待审批".equals(status)) {
            tvStatus.setTextColor(0xFFFF9800); // 橙色
        } else if ("通过".equals(status)) {
            tvStatus.setTextColor(0xFF4CAF50); // 绿色
        } else if ("拒绝".equals(status)) {
            tvStatus.setTextColor(0xFFF44336); // 红色
        } else {
            tvStatus.setTextColor(0xFF666666); // 灰色
        }
        row1.addView(tvStatus);

        itemLayout.addView(row1);

        // 第二行：姓名和日期
        LinearLayout row2 = new LinearLayout(this);
        row2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        row2.setOrientation(LinearLayout.HORIZONTAL);

        // 姓名
        TextView tvName = new TextView(this);
        tvName.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        tvName.setText("姓名：" + getSafeString(record.getXingming()));
        tvName.setTextSize(14);
        tvName.setTextColor(0xFF333333);
        row2.addView(tvName);

        // 日期
        TextView tvDate = new TextView(this);
        tvDate.setText("日期：" + getSafeString(record.getTijiaoriqi()));
        tvDate.setTextSize(14);
        tvDate.setTextColor(0xFF333333);
        row2.addView(tvDate);

        itemLayout.addView(row2);

        // 离职原因
        TextView tvReason = new TextView(this);
        String reason = record.getShenqingyuanyin();
        if (reason != null && reason.length() > 30) {
            reason = reason.substring(0, 30) + "...";
        }
        tvReason.setText("原因：" + (reason != null ? reason : ""));
        tvReason.setTextSize(14);
        tvReason.setTextColor(0xFF666666);
        itemLayout.addView(tvReason);

        // 将历史记录项添加到容器中
        containerRecords.addView(itemLayout);
    }

    // 安全获取字符串，防止null
    private String getSafeString(String str) {
        return str != null ? str : "";
    }
}