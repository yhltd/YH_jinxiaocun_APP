package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceShuiLv;
import com.example.myapplication.finance.entity.YhFinanceWaiBi;
import com.example.myapplication.finance.service.YhFinanceInvoicePeizhiService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceSimpleAccountingService;
import com.example.myapplication.finance.service.YhFinanceShuiLvService;
import com.example.myapplication.finance.service.YhFinanceWaiBiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

public class JiJianPeiZhiChangeActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceJiJianPeiZhi yhFinanceJiJianPeiZhi;
    private YhFinanceShuiLv yhFinanceShuiLv;
    private YhFinanceWaiBi yhFinanceWaiBi;
    private YhFinanceSimpleAccountingService yhFinanceSimpleAccountingService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;
    private YhFinanceInvoicePeizhiService yhFinanceInvoicePeizhiService;
    private YhFinanceShuiLvService yhFinanceShuiLvService;
    private YhFinanceWaiBiService yhFinanceWaiBiService;

    private EditText shouru;
    private EditText linjiezhiEditText; // 税率临界值
    private EditText huilvEditText;     // 外币汇率
    private TextView textView1;
    private String serviceType;
    private LinearLayout linjiezhiLayout; // 税率临界值布局
    private LinearLayout huilvLayout;     // 外币汇率布局

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jijianpeizhi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceSimpleAccountingService = new YhFinanceSimpleAccountingService();
        yhFinanceKehuPeizhiService = new YhFinanceKehuPeizhiService();
        yhFinanceInvoicePeizhiService = new YhFinanceInvoicePeizhiService();
        yhFinanceShuiLvService = new YhFinanceShuiLvService();
        yhFinanceWaiBiService = new YhFinanceWaiBiService();

        shouru = findViewById(R.id.shouru);
        textView1 = findViewById(R.id.jingyingshouru_text);

        // 初始化所有布局和控件
        linjiezhiLayout = findViewById(R.id.linjiezhi_layout);
        linjiezhiEditText = findViewById(R.id.linjiezhi);
        huilvLayout = findViewById(R.id.huilv_layout);
        huilvEditText = findViewById(R.id.huilv);

        Intent intent = getIntent();
        serviceType = intent.getStringExtra("service");
        textView1.setText(serviceType);

        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            if (serviceType != null && serviceType.equals("税率")) {
                yhFinanceShuiLv = new YhFinanceShuiLv();
                setupShuiLvInput();
            } else if (serviceType != null && serviceType.equals("外币")) {
                yhFinanceWaiBi = new YhFinanceWaiBi();
                setupWaiBiInput();
            } else {
                yhFinanceJiJianPeiZhi = new YhFinanceJiJianPeiZhi();
                setupNormalInput();
            }
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            if (serviceType != null && serviceType.equals("税率")) {
                yhFinanceShuiLv = (YhFinanceShuiLv) myApplication.getObj();
                setupShuiLvInput();
                loadShuiLvData();
            } else if (serviceType != null && serviceType.equals("外币")) {
                yhFinanceWaiBi = (YhFinanceWaiBi) myApplication.getObj();
                setupWaiBiInput();
                loadWaiBiData();
            } else {
                yhFinanceJiJianPeiZhi = (YhFinanceJiJianPeiZhi) myApplication.getObj();
                setupNormalInput();
                loadNormalData();
            }
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }
    }

    // 设置税率输入框
    private void setupShuiLvInput() {
        shouru.setHint("请输入税率");

        // 显示税率临界值布局，隐藏外币汇率布局
        if (linjiezhiLayout != null) {
            linjiezhiLayout.setVisibility(View.VISIBLE);
        }
        if (huilvLayout != null) {
            huilvLayout.setVisibility(View.GONE);
        }

        // 设置输入类型
        if (linjiezhiEditText != null) {
            linjiezhiEditText.setHint("请输入临界值");
            linjiezhiEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
    }

    // 设置外币输入框
    private void setupWaiBiInput() {
        shouru.setHint("请输入币种");

        // 显示外币汇率布局，隐藏税率临界值布局
        if (huilvLayout != null) {
            huilvLayout.setVisibility(View.VISIBLE);
        }
        if (linjiezhiLayout != null) {
            linjiezhiLayout.setVisibility(View.GONE);
        }

        // 设置输入类型
        if (huilvEditText != null) {
            huilvEditText.setHint("请输入汇率");
            huilvEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
    }

    // 设置普通输入框
    private void setupNormalInput() {
        shouru.setHint("请输入" + serviceType);

        // 隐藏所有附加布局
        if (linjiezhiLayout != null) {
            linjiezhiLayout.setVisibility(View.GONE);
        }
        if (huilvLayout != null) {
            huilvLayout.setVisibility(View.GONE);
        }
    }

    // 加载税率数据
    private void loadShuiLvData() {
        if (yhFinanceShuiLv != null) {
            shouru.setText(yhFinanceShuiLv.getShuilv());

            if (linjiezhiEditText != null && yhFinanceShuiLv.getLinjiezhi() != null) {
                linjiezhiEditText.setText(yhFinanceShuiLv.getLinjiezhi());
            }
        }
    }

    // 加载外币数据
    private void loadWaiBiData() {
        if (yhFinanceWaiBi != null) {
            shouru.setText(yhFinanceWaiBi.getBizhong());

            if (huilvEditText != null && yhFinanceWaiBi.getHuilv() != null) {
                huilvEditText.setText(yhFinanceWaiBi.getHuilv());
            }
        }
    }

    // 加载普通数据
    private void loadNormalData() {
        if (yhFinanceJiJianPeiZhi != null) {
            shouru.setText(yhFinanceJiJianPeiZhi.getPeizhi());
        }
    }

    public void clearClick(View v) {
        shouru.setText("");

        if (linjiezhiEditText != null) {
            linjiezhiEditText.setText("");
        }

        if (huilvEditText != null) {
            huilvEditText.setText("");
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

    private boolean checkForm() {
        if (serviceType != null && serviceType.equals("税率")) {
            // 税率验证逻辑
            if (shouru.getText().toString().trim().isEmpty()) {
                ToastUtil.show(JiJianPeiZhiChangeActivity.this, "请输入税率名称");
                return false;
            }

            if (linjiezhiEditText == null || linjiezhiEditText.getText().toString().trim().isEmpty()) {
                ToastUtil.show(JiJianPeiZhiChangeActivity.this, "请输入临界值");
                return false;
            }

            String linjiezhiStr = linjiezhiEditText.getText().toString().trim();
            try {
                Double.parseDouble(linjiezhiStr);

                yhFinanceShuiLv.setShuilv(shouru.getText().toString().trim());
                yhFinanceShuiLv.setLinjiezhi(linjiezhiStr);
                yhFinanceShuiLv.setCompany(yhFinanceUser.getCompany());

            } catch (Exception e) {
                ToastUtil.show(JiJianPeiZhiChangeActivity.this, "临界值格式不正确");
                return false;
            }

        } else if (serviceType != null && serviceType.equals("外币")) {
            // 外币验证逻辑
            if (shouru.getText().toString().trim().isEmpty()) {
                ToastUtil.show(JiJianPeiZhiChangeActivity.this, "请输入币种");
                return false;
            }

            if (huilvEditText == null || huilvEditText.getText().toString().trim().isEmpty()) {
                ToastUtil.show(JiJianPeiZhiChangeActivity.this, "请输入汇率");
                return false;
            }

            String huilvStr = huilvEditText.getText().toString().trim();
            try {
                Double.parseDouble(huilvStr);

                yhFinanceWaiBi.setBizhong(shouru.getText().toString().trim());
                yhFinanceWaiBi.setHuilv(huilvStr);
                yhFinanceWaiBi.setCompany(yhFinanceUser.getCompany());

            } catch (Exception e) {
                ToastUtil.show(JiJianPeiZhiChangeActivity.this, "汇率格式不正确");
                return false;
            }

        } else {
            // 普通配置验证逻辑
            if (shouru.getText().toString().trim().isEmpty()) {
                ToastUtil.show(JiJianPeiZhiChangeActivity.this, "未输入内容");
                return false;
            }

            yhFinanceJiJianPeiZhi.setPeizhi(shouru.getText().toString().trim());
            yhFinanceJiJianPeiZhi.setCompany(yhFinanceUser.getCompany());
        }

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    public void insertClick(View v) {
        if (!checkForm()) return;



        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                LoadingDialog.getInstance(JiJianPeiZhiChangeActivity.this).dismiss();
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiJianPeiZhiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiJianPeiZhiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                boolean result = false;
                try {
                    if (serviceType.equals("税率")) {
                        result = yhFinanceShuiLvService.insert(yhFinanceShuiLv);
                    } else if (serviceType.equals("科目")) {
                        result = yhFinanceSimpleAccountingService.insert(yhFinanceJiJianPeiZhi);
                    } else if (serviceType.equals("客户/供应商/往来单位")) {
                        result = yhFinanceKehuPeizhiService.insert(yhFinanceJiJianPeiZhi);
                    } else if (serviceType.equals("发票种类")) {
                        result = yhFinanceInvoicePeizhiService.insert(yhFinanceJiJianPeiZhi);
                    } else if (serviceType.equals("外币")) {
                        result = yhFinanceWaiBiService.insert(yhFinanceWaiBi);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msg.obj = result;
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;



        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                LoadingDialog.getInstance(JiJianPeiZhiChangeActivity.this).dismiss();
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiJianPeiZhiChangeActivity.this, "更新成功");
                    back();
                } else {
                    ToastUtil.show(JiJianPeiZhiChangeActivity.this, "更新失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                boolean result = false;
                try {
                    if (serviceType.equals("税率")) {
                        result = yhFinanceShuiLvService.update(yhFinanceShuiLv);
                    } else if (serviceType.equals("科目")) {
                        result = yhFinanceSimpleAccountingService.update(yhFinanceJiJianPeiZhi);
                    } else if (serviceType.equals("客户/供应商/往来单位")) {
                        result = yhFinanceKehuPeizhiService.update(yhFinanceJiJianPeiZhi);
                    } else if (serviceType.equals("发票种类")) {
                        result = yhFinanceInvoicePeizhiService.update(yhFinanceJiJianPeiZhi);
                    } else if (serviceType.equals("外币")) {
                        result = yhFinanceWaiBiService.update(yhFinanceWaiBi);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msg.obj = result;
                saveHandler.sendMessage(msg);
            }
        }).start();
    }
}