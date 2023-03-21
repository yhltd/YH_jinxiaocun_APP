package com.example.myapplication.mendian.activity;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.activity.ZhangHaoGuanLiChangeActivity;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceUserService;
import com.example.myapplication.mendian.entity.YhMendianKeHu;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianKehuService;
import com.example.myapplication.mendian.service.YhMendianUserService;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.List;

public class KehuinfoChangeActivity extends AppCompatActivity {

    private YhMendianUser yhMendianUser;
    private YhMendianUserService yhMendianUserService;
    private YhMendianKeHu yhMendianKeHu;
    private YhMendianKehuService yhMendianKehuService;

    private EditText recipient;
    private EditText cardholder;
    private EditText drawee;
    private EditText issuing_bank;
    private EditText bill_day;
    private EditText repayment_date;
    private EditText total;
    private EditText repayable;
    private EditText balance;
    private EditText loan;
    private EditText service_charge;
    private EditText telephone;
    private EditText password;
    private EditText staff;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kehuinfo_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianUserService = new YhMendianUserService();
        yhMendianKehuService = new YhMendianKehuService();

        recipient = findViewById(R.id.recipient);
        cardholder = findViewById(R.id.cardholder);
        drawee = findViewById(R.id.drawee);
        issuing_bank = findViewById(R.id.issuing_bank);
        bill_day = findViewById(R.id.bill_day);
        repayment_date = findViewById(R.id.repayment_date);
        total = findViewById(R.id.total);
        repayable = findViewById(R.id.repayable);
        balance = findViewById(R.id.balance);
        loan = findViewById(R.id.loan);
        service_charge = findViewById(R.id.service_charge);
        telephone = findViewById(R.id.telephone);
        password = findViewById(R.id.password);
        staff = findViewById(R.id.staff);


        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhMendianKeHu = (YhMendianKeHu) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            recipient.setText(yhMendianKeHu.getRecipient());
            cardholder.setText(yhMendianKeHu.getCardholder());
            drawee.setText(yhMendianKeHu.getDrawee());
            issuing_bank.setText(yhMendianKeHu.getIssuing_bank());
            bill_day.setText(yhMendianKeHu.getBill_day());
            repayment_date.setText(yhMendianKeHu.getRepayment_date());
            total.setText(yhMendianKeHu.getTotal());
            repayable.setText(yhMendianKeHu.getRepayable());
            balance.setText(yhMendianKeHu.getBalance());
            loan.setText(yhMendianKeHu.getLoan());
            service_charge.setText(yhMendianKeHu.getService_charge());
            telephone.setText(yhMendianKeHu.getTelephone());
            password.setText(yhMendianKeHu.getPassword());
            staff.setText(yhMendianKeHu.getStaff());

        }else{
            yhMendianKeHu = new YhMendianKeHu();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
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

    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KehuinfoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KehuinfoChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianKehuService.updateByKehu(yhMendianKeHu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KehuinfoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KehuinfoChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianKehuService.insertByKehu(yhMendianKeHu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {

        if (recipient.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入收卡人");
            return false;
        } else {
            yhMendianKeHu.setRecipient(recipient.getText().toString());
        }

        if (cardholder.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入付款人");
            return false;
        } else {
            yhMendianKeHu.setCardholder(cardholder.getText().toString());
        }
        if (drawee.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入持卡人");
            return false;
        } else {
            yhMendianKeHu.setDrawee(drawee.getText().toString());
        }
        if (issuing_bank.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入发卡行");
            return false;
        } else {
            yhMendianKeHu.setIssuing_bank(issuing_bank.getText().toString());
        }
        if (bill_day.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入账单日");
            return false;
        } else {
            yhMendianKeHu.setBill_day(bill_day.getText().toString());
        }
        if (repayment_date.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入还款日");
            return false;
        } else {
            yhMendianKeHu.setRepayment_date(repayment_date.getText().toString());
        }
        if (total.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入总金额");
            return false;
        } else {
            yhMendianKeHu.setTotal(total.getText().toString());
        }
        if (repayable.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入应还款");
            return false;
        } else {
            yhMendianKeHu.setRepayable(repayable.getText().toString());
        }
        if (balance.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入余款额");
            return false;
        } else {
            yhMendianKeHu.setBalance(balance.getText().toString());
        }
        if (loan.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入借款额");
            return false;
        } else {
            yhMendianKeHu.setLoan(loan.getText().toString());
        }
        if (service_charge.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入手续费");
            return false;
        } else {
            yhMendianKeHu.setService_charge(service_charge.getText().toString());
        }
        if (telephone.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入电话号");
            return false;
        } else {
            yhMendianKeHu.setTelephone(telephone.getText().toString());
        }
        if (password.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入密码");
            return false;
        } else {
            yhMendianKeHu.setPassword(password.getText().toString());
        }
        if (staff.getText().toString().equals("")) {
            ToastUtil.show(KehuinfoChangeActivity.this, "请输入员工");
            return false;
        } else {
            yhMendianKeHu.setStaff(staff.getText().toString());
        }

        yhMendianKeHu.setGongsi(yhMendianKeHu.getGongsi());

        return true;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
