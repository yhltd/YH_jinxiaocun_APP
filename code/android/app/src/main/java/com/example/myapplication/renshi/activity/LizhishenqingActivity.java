package com.example.myapplication.renshi.activity;

import android.content.Intent; // 添加这行
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiLizhiShenQing;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiLizhiShenQingService;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;

public class LizhishenqingActivity extends AppCompatActivity {

    private Spinner spDepartment;
    private EditText etXingming;
    private EditText etShenqingyuanyin;
    private TextView tvTijiaoriqi;
    private Button btnSubmit;
    private Button btnReset;
    private Button btnHistory; // 查看历史记录按钮

    private YhRenShiUser yhRenShiUser;
    private YhRenShiLizhiShenQingService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lizhishenqing);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("离职申请"); // 添加标题
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        service = new YhRenShiLizhiShenQingService();

        initView();
        loadDepartments();
        setupListeners();
        setCurrentDate();
    }

    private void loadDepartments() {
        // 显示加载提示
        Toast.makeText(this, "正在加载部门数据...", Toast.LENGTH_SHORT).show();

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                List<String> departments = (List<String>) msg.obj;

                if (departments == null || departments.isEmpty()) {
                    // 如果获取失败，使用默认部门列表
                    departments = new ArrayList<>();
                    departments.add("测试部");
                    departments.add("工程部");
                    departments.add("开发部");
                    departments.add("人事部");
                    departments.add("行政部");
                }

                // 在列表开头添加提示项
                departments.add(0, "请选择部门");

                // 创建适配器
                ArrayAdapter<String> adapter = new ArrayAdapter<>(LizhishenqingActivity.this,
                        android.R.layout.simple_spinner_item, departments);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // 设置适配器
                spDepartment.setAdapter(adapter);

                // 设置默认选中项
                spDepartment.setSelection(0);

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取公司名，并去掉_hr后缀
                    String companyName = yhRenShiUser.getL();
                    if (companyName != null && companyName.endsWith("_hr")) {
                        companyName = companyName.replace("_hr", "");
                    }

                    // 调用Service获取部门数据
                    List<String> departments = service.getDepartments(companyName);

                    Message msg = new Message();
                    msg.obj = departments;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    // 发生异常时发送空列表
                    Message msg = new Message();
                    msg.obj = new ArrayList<String>();
                    handler.sendMessage(msg);
                }
            }
        }).start();
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
        spDepartment = findViewById(R.id.sp_department);     // 部门下拉框
        etXingming = findViewById(R.id.et_name);             // 姓名
        etShenqingyuanyin = findViewById(R.id.et_reason);    // 离职原因
        tvTijiaoriqi = findViewById(R.id.tv_submit_date);    // 提交日期（只读）
        btnSubmit = findViewById(R.id.btn_submit);           // 提交按钮
        btnReset = findViewById(R.id.btn_reset);             // 重置按钮
        btnHistory = findViewById(R.id.btn_history);         // 查看历史记录按钮
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitResignation();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到历史记录页面
                Intent intent = new Intent(LizhishenqingActivity.this, LizhiHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        tvTijiaoriqi.setText(currentDate);
    }

    private void submitResignation() {
        // 获取选中的部门
        String selectedDept = (String) spDepartment.getSelectedItem();
        String bumen = selectedDept != null ? selectedDept.trim() : "";
        String xingming = etXingming.getText().toString().trim();
        String shenqingyuanyin = etShenqingyuanyin.getText().toString().trim();
        String tijiaoriqi = tvTijiaoriqi.getText().toString();

        // 表单验证 - 检查是否选择了有效部门
        if (bumen.isEmpty() || bumen.equals("请选择部门")) {
            Toast.makeText(this, "请选择部门", Toast.LENGTH_SHORT).show();
            spDepartment.requestFocus();
            return;
        }

        if (xingming.isEmpty()) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            etXingming.requestFocus();
            return;
        }

        if (shenqingyuanyin.isEmpty()) {
            Toast.makeText(this, "请输入离职原因", Toast.LENGTH_SHORT).show();
            etShenqingyuanyin.requestFocus();
            return;
        }

        // 显示加载
        btnSubmit.setEnabled(false);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                btnSubmit.setEnabled(true);
                if ((boolean) msg.obj) {
                    ToastUtil.show(LizhishenqingActivity.this, "离职申请提交成功，等待审批");
                    resetForm();
                } else {
                    ToastUtil.show(LizhishenqingActivity.this, "提交失败，请重试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建实体对象
                YhRenShiLizhiShenQing shenQing = new YhRenShiLizhiShenQing();
                shenQing.setBumen(bumen);
                shenQing.setXingming(xingming);
                shenQing.setTijiaoriqi(tijiaoriqi);
                shenQing.setShenqingyuanyin(shenqingyuanyin);
                shenQing.setShenpijieguo("待审批");
                shenQing.setShenpiyuanyin("");

                // 获取公司名，并去掉_hr后缀
                String companyName = yhRenShiUser.getL();
                if (companyName != null && companyName.endsWith("_hr")) {
                    companyName = companyName.replace("_hr", "");
                }
                shenQing.setGongsi(companyName);

                // 保存到数据库
                boolean result = service.insert(shenQing);

                Message msg = new Message();
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void resetForm() {
        if (spDepartment.getAdapter() != null && spDepartment.getAdapter().getCount() > 0) {
            spDepartment.setSelection(0);
        }
        etXingming.setText("");
        etShenqingyuanyin.setText("");
        setCurrentDate();
        Toast.makeText(this, "表单已重置", Toast.LENGTH_SHORT).show();
    }
}