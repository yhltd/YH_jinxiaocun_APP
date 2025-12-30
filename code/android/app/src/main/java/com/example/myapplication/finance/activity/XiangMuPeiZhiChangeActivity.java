package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceXiangMu;
import com.example.myapplication.finance.service.YhFinanceXiangMuService;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;

public class XiangMuPeiZhiChangeActivity extends AppCompatActivity {
    private YhFinanceUser yhFinanceUser;
    private YhFinanceXiangMu yhFinanceXiangMu;
    private YhFinanceXiangMuService yhFinanceXiangMuService;

    private Spinner ysyf;
    private EditText xiangmumingcheng;
    private EditText ysyfkemu;
    private EditText jine;

    // 应收应付选项
    private String[] ysyfOptions = {"应收", "应付"};

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_xiangmupeizhi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceXiangMuService = new YhFinanceXiangMuService();

        ysyf = findViewById(R.id.ysyf);
        xiangmumingcheng = findViewById(R.id.xiangmumingcheng);
        ysyfkemu = findViewById(R.id.ysyfkemu);
        jine = findViewById(R.id.jine);

        // 初始化应收应付下拉框
        initYsyfSpinner();

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhFinanceXiangMu = (YhFinanceXiangMu) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            loadFormData();
        } else {
            yhFinanceXiangMu = new YhFinanceXiangMu();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }
    }

    // 初始化应收应付下拉框
    private void initYsyfSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ysyfOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ysyf.setAdapter(adapter);

        // 设置监听器
        ysyf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = ysyfOptions[position];
                if (yhFinanceXiangMu != null) {
                    yhFinanceXiangMu.setYsyf(selected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 默认设置为应收
                if (yhFinanceXiangMu != null) {
                    yhFinanceXiangMu.setYsyf("应收");
                }
            }
        });
    }

    // 加载表单数据（更新时使用）
    private void loadFormData() {
        if (yhFinanceXiangMu == null) return;

        // 设置项目名称
        if (yhFinanceXiangMu.getxiangmumingcheng() != null) {
            xiangmumingcheng.setText(yhFinanceXiangMu.getxiangmumingcheng());
        }

        // 设置应收应付
        if (yhFinanceXiangMu.getYsyf() != null) {
            String currentYsyf = yhFinanceXiangMu.getYsyf();
            for (int i = 0; i < ysyfOptions.length; i++) {
                if (ysyfOptions[i].equals(currentYsyf)) {
                    ysyf.setSelection(i);
                    break;
                }
            }
        }

        // 设置科目
        if (yhFinanceXiangMu.getYsyfkemu() != null) {
            ysyfkemu.setText(yhFinanceXiangMu.getYsyfkemu());
        }

        // 设置金额
        if (yhFinanceXiangMu.getJine() != null) {
            jine.setText(yhFinanceXiangMu.getJine());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearClick(View v) {
        ysyf.setSelection(0); // 重置为"应收"
        xiangmumingcheng.setText("");
        ysyfkemu.setText("");
        jine.setText("");
    }

    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(XiangMuPeiZhiChangeActivity.this, "更新成功");
                    back();
                } else {
                    ToastUtil.show(XiangMuPeiZhiChangeActivity.this, "更新失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                // 确保公司信息已设置
                if (yhFinanceUser != null && yhFinanceUser.getCompany() != null) {
                    yhFinanceXiangMu.setCompany(yhFinanceUser.getCompany());
                }
                msg.obj = yhFinanceXiangMuService.update(yhFinanceXiangMu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void insertClick(View v) {
        if (!checkForm()) return;

        // 设置公司名称
        if (yhFinanceUser != null && yhFinanceUser.getCompany() != null) {
            yhFinanceXiangMu.setCompany(yhFinanceUser.getCompany());
        }

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(XiangMuPeiZhiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(XiangMuPeiZhiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceXiangMuService.insert(yhFinanceXiangMu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        // 检查应收应付
        String selectedYsyf = ysyf.getSelectedItem().toString();
        if (selectedYsyf == null || selectedYsyf.isEmpty()) {
            ToastUtil.show(this, "请选择应收/应付");
            return false;
        }
        yhFinanceXiangMu.setYsyf(selectedYsyf);

        // 检查项目名称
        String xiangmu = xiangmumingcheng.getText().toString().trim();
        if (xiangmu.isEmpty()) {
            ToastUtil.show(this, "请输入项目名称");
            return false;
        }
        yhFinanceXiangMu.setXiangmumingcheng(xiangmu);

        // 检查科目
        String kemu = ysyfkemu.getText().toString().trim();
        if (kemu.isEmpty()) {
            ToastUtil.show(this, "请输入类别");
            return false;
        }
        yhFinanceXiangMu.setYsyfkemu(kemu);

        // 检查金额
        String money = jine.getText().toString().trim();
        if (money.isEmpty()) {
            ToastUtil.show(this, "请输入金额");
            return false;
        }

        if (!money.matches("[0-9,]*")) {
            ToastUtil.show(this, "金额只能包含数字和英文逗号");
            return false;
        }
        yhFinanceXiangMu.setJine(money);

        return true;
    }

    private boolean validateMoney(String money) {
        // 使用正则表达式：只允许数字和逗号
        return money.matches("[0-9,]*");
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}