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
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceDepartmentService;
import com.example.myapplication.finance.service.YhFinanceExpenditureService;
import com.example.myapplication.finance.service.YhFinanceIncomeService;
import com.example.myapplication.finance.service.YhFinanceInvestmentExpenditure;
import com.example.myapplication.finance.service.YhFinanceInvestmentIncome;
import com.example.myapplication.finance.service.YhFinanceManagementExpenditure;
import com.example.myapplication.finance.service.YhFinanceManagementIncome;
import com.example.myapplication.finance.service.YhFinanceVoucherWord;
import com.example.myapplication.utils.ToastUtil;

public class ExpenditureChangeActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceExpenditure yhFinanceExpenditure;
    private YhFinanceExpenditureService yhFinanceExpenditureService;
    private YhFinanceIncomeService yhFinanceIncomeService;
    private YhFinanceInvestmentExpenditure yhFinanceInvestmentExpenditure;
    private YhFinanceInvestmentIncome yhFinanceInvestmentIncome;
    private YhFinanceManagementExpenditure yhFinanceManagementExpenditure;
    private YhFinanceManagementIncome yhFinanceManagementIncome;
    private YhFinanceVoucherWord yhFinanceVoucherWord;

    private EditText shouru;
    private TextView textView1;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenditure_jingyingshouru_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceExpenditureService = new YhFinanceExpenditureService();
        yhFinanceIncomeService = new YhFinanceIncomeService();
        yhFinanceInvestmentExpenditure = new YhFinanceInvestmentExpenditure();
        yhFinanceInvestmentIncome = new YhFinanceInvestmentIncome();
        yhFinanceManagementExpenditure = new YhFinanceManagementExpenditure();
        yhFinanceManagementIncome = new YhFinanceManagementIncome();
        yhFinanceVoucherWord = new YhFinanceVoucherWord();

        shouru = findViewById(R.id.shouru);
        textView1 = findViewById(R.id.jingyingshouru_text);

        Intent intent = getIntent();
        String service = intent.getStringExtra("service");
        textView1.setText(service);

        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhFinanceExpenditure = new YhFinanceExpenditure();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhFinanceExpenditure = (YhFinanceExpenditure) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            shouru.setText(yhFinanceExpenditure.getShouru());
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


    public void insertClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ExpenditureChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ExpenditureChangeActivity.this, "保存失败，请稍后再试");
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
                if(service.equals("经营收入")){
                    msg.obj = yhFinanceExpenditureService.insert(yhFinanceExpenditure);
                }else if(service.equals("经营支出")){
                    msg.obj = yhFinanceIncomeService.insert(yhFinanceExpenditure);
                }else if(service.equals("筹资收入")){
                    msg.obj = yhFinanceInvestmentIncome.insert(yhFinanceExpenditure);
                }else if(service.equals("筹资支出")){
                    msg.obj = yhFinanceInvestmentExpenditure.insert(yhFinanceExpenditure);
                }else if(service.equals("投资收入")){
                    msg.obj = yhFinanceManagementExpenditure.insert(yhFinanceExpenditure);
                }else if(service.equals("投资支出")){
                    msg.obj = yhFinanceManagementIncome.insert(yhFinanceExpenditure);
                }else if(service.equals("凭证字")){
                    msg.obj = yhFinanceVoucherWord.insert(yhFinanceExpenditure);
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
                    ToastUtil.show(ExpenditureChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ExpenditureChangeActivity.this, "保存失败，请稍后再试");
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
                if(service.equals("经营收入")){
                    msg.obj = yhFinanceExpenditureService.update(yhFinanceExpenditure);
                }else if(service.equals("经营支出")){
                    msg.obj = yhFinanceIncomeService.update(yhFinanceExpenditure);
                }else if(service.equals("筹资收入")){
                    msg.obj = yhFinanceInvestmentIncome.update(yhFinanceExpenditure);
                }else if(service.equals("筹资支出")){
                    msg.obj = yhFinanceInvestmentExpenditure.update(yhFinanceExpenditure);
                }else if(service.equals("投资收入")){
                    msg.obj = yhFinanceManagementExpenditure.update(yhFinanceExpenditure);
                }else if(service.equals("投资支出")){
                    msg.obj = yhFinanceManagementIncome.update(yhFinanceExpenditure);
                }else if(service.equals("凭证字")){
                    msg.obj = yhFinanceVoucherWord.update(yhFinanceExpenditure);
                }
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() {
        if (shouru.getText().toString().equals("")) {
            ToastUtil.show(ExpenditureChangeActivity.this, "未输入内容");
            return false;
        } else {
            yhFinanceExpenditure.setShouru(shouru.getText().toString());
        }

        yhFinanceExpenditure.setCompany(yhFinanceUser.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
