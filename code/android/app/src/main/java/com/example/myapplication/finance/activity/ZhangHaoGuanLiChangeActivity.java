package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceFaPiao;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceFaPiaoService;
import com.example.myapplication.finance.service.YhFinanceInvoicePeizhiService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceQuanXianService;
import com.example.myapplication.finance.service.YhFinanceUserService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class ZhangHaoGuanLiChangeActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceUser yhFinanceUserNow;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceUserService yhFinanceUserService;
    private YhFinanceQuanXianService yhFinanceQuanXianService;

    private EditText name;
    private EditText pwd;
    private EditText doo;
    private Button upd_quanxian;

    List<YhFinanceUser> getList;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhanghaoguanli_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceUserService = new YhFinanceUserService();
        yhFinanceQuanXianService = new YhFinanceQuanXianService();
        yhFinanceQuanXian = new YhFinanceQuanXian();
        yhFinanceQuanXian.setKmzzAdd("是");
        yhFinanceQuanXian.setKmzzDelete("是");
        yhFinanceQuanXian.setKmzzSelect("是");
        yhFinanceQuanXian.setKmzzUpdate("是");
        yhFinanceQuanXian.setKzxmAdd("是");
        yhFinanceQuanXian.setKzxmDelete("是");
        yhFinanceQuanXian.setKzxmSelect("是");
        yhFinanceQuanXian.setKzxmUpdate("是");
        yhFinanceQuanXian.setBmszAdd("是");
        yhFinanceQuanXian.setBmszDelete("是");
        yhFinanceQuanXian.setBmszSelect("是");
        yhFinanceQuanXian.setBmszUpdate("是");
        yhFinanceQuanXian.setPzhzAdd("是");
        yhFinanceQuanXian.setPzhzDelete("是");
        yhFinanceQuanXian.setPzhzSelect("是");
        yhFinanceQuanXian.setPzhzUpdate("是");
        yhFinanceQuanXian.setZnkbSelect("是");
        yhFinanceQuanXian.setXjllSelect("是");
        yhFinanceQuanXian.setZcfzSelect("是");
        yhFinanceQuanXian.setLysySelect("是");
        yhFinanceQuanXian.setJjtzAdd("是");
        yhFinanceQuanXian.setJjtzDelete("是");
        yhFinanceQuanXian.setJjtzSelect("是");
        yhFinanceQuanXian.setJjtzUpdate("是");
        yhFinanceQuanXian.setJjzzUpdate("是");
        yhFinanceQuanXian.setJjzzAdd("是");
        yhFinanceQuanXian.setJjzzDelete("是");
        yhFinanceQuanXian.setJjzzSelect("是");
        yhFinanceQuanXian.setZhglUpdate("是");
        yhFinanceQuanXian.setZhglSelect("是");
        yhFinanceQuanXian.setZhglDelete("是");
        yhFinanceQuanXian.setZhglAdd("是");

        name = findViewById(R.id.name);
        pwd = findViewById(R.id.pwd);
        doo = findViewById(R.id.doo);

        upd_quanxian = findViewById(R.id.quanxianClick);
        upd_quanxian.setOnClickListener(quanxianClick());

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhFinanceUserNow = (YhFinanceUser) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            name.setText(yhFinanceUserNow.getName());
            pwd.setText(yhFinanceUserNow.getPwd());
            doo.setText(yhFinanceUserNow.getDoo());
            Button quanxian = findViewById(R.id.quanxianClick);
            quanxian.setVisibility(View.VISIBLE);
        }else{
            yhFinanceUserNow = new YhFinanceUser();
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

    public View.OnClickListener quanxianClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ZhangHaoGuanLiChangeActivity.this, QuanXianChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(yhFinanceUserNow);
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "保存失败，请稍后再试");
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceUserService.update(yhFinanceUserNow);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "保存失败，请稍后再试");
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                yhFinanceQuanXianService.insert(yhFinanceQuanXian);
                msg.obj = yhFinanceUserService.insert(yhFinanceUserNow);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkForm() throws ParseException {

        if (name.getText().toString().equals("")) {
            ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "请输入账号");
            return false;
        } else {
            yhFinanceUserNow.setName(name.getText().toString());
        }

        if (pwd.getText().toString().equals("")) {
            ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "请输入密码");
            return false;
        } else {
            yhFinanceUserNow.setPwd(pwd.getText().toString());
        }

        if (doo.getText().toString().equals("")) {
            ToastUtil.show(ZhangHaoGuanLiChangeActivity.this, "请输入操作密码");
            return false;
        } else {
            yhFinanceUserNow.setDoo(doo.getText().toString());
        }

        yhFinanceUserNow.setCompany(yhFinanceUser.getCompany());
        LocalDate now = LocalDate.now();
        System.out.println(now);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyymmddhhmmss");
        String bianhao = now.format(dateTimeFormatter);
        yhFinanceUserNow.setBianhao(bianhao);
        yhFinanceQuanXian.setBianhao(bianhao);
        return true;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
