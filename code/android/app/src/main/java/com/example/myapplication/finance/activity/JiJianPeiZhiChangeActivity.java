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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceInvoicePeizhiService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceSimpleAccountingService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

public class JiJianPeiZhiChangeActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceJiJianPeiZhi yhFinanceJiJianPeiZhi;
    private YhFinanceSimpleAccountingService yhFinanceSimpleAccountingService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;
    private YhFinanceInvoicePeizhiService yhFinanceInvoicePeizhiService;

    private EditText shouru;
    private TextView textView1;

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

        shouru = findViewById(R.id.shouru);
        textView1 = findViewById(R.id.jingyingshouru_text);

        Intent intent = getIntent();
        String service = intent.getStringExtra("service");
        textView1.setText(service);

        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhFinanceJiJianPeiZhi = new YhFinanceJiJianPeiZhi();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhFinanceJiJianPeiZhi = (YhFinanceJiJianPeiZhi) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            shouru.setText(yhFinanceJiJianPeiZhi.getPeizhi());
        }
    }

    public void clearClick(View v) {
        shouru.setText("");
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
        if (shouru.getText().toString().equals("")) {
            ToastUtil.show(JiJianPeiZhiChangeActivity.this, "未输入内容");
            return false;
        } else {
            yhFinanceJiJianPeiZhi.setPeizhi(shouru.getText().toString());
        }

        yhFinanceJiJianPeiZhi.setCompany(yhFinanceUser.getCompany());
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
                Intent intent = getIntent();
                String service = intent.getStringExtra("service");
                if(service.equals("科目")){
                    msg.obj = yhFinanceSimpleAccountingService.insert(yhFinanceJiJianPeiZhi);
                }else if(service.equals("客户/供应商/往来单位")){
                    msg.obj = yhFinanceKehuPeizhiService.insert(yhFinanceJiJianPeiZhi);
                }else if(service.equals("发票种类")){
                    msg.obj = yhFinanceInvoicePeizhiService.insert(yhFinanceJiJianPeiZhi);
                }

                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
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
                Intent intent = getIntent();
                String service = intent.getStringExtra("service");
                if(service.equals("科目")){
                    msg.obj = yhFinanceSimpleAccountingService.update(yhFinanceJiJianPeiZhi);
                }else if(service.equals("客户/供应商/往来单位")){
                    msg.obj = yhFinanceKehuPeizhiService.update(yhFinanceJiJianPeiZhi);
                }else if(service.equals("发票种类")){
                    msg.obj = yhFinanceInvoicePeizhiService.update(yhFinanceJiJianPeiZhi);
                }
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

}
