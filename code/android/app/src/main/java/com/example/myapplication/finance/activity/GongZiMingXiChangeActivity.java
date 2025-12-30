package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceGongZiMingXi;

import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceGongZiMingXiService;

import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GongZiMingXiChangeActivity extends AppCompatActivity implements OnDateSetListener, View.OnClickListener {
    private YhFinanceUser yhFinanceUser;
    private YhFinanceGongZiMingXi yhFinanceGongZiMingXi;
    private YhFinanceGongZiMingXiService yhFinanceGongZiMingXiService;

    private TextView riqi;
    private EditText renming;
    private EditText yinhangzhanghu;
    private EditText koukuan;
    private EditText gongzi;
    private EditText yifu;
    private EditText beizhu;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TimePickerDialog pickerdialog;
    private String date_name;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_gongzimingxi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceGongZiMingXiService = new YhFinanceGongZiMingXiService();


        riqi = findViewById(R.id.riqi);
        renming = findViewById(R.id.renming);
        yinhangzhanghu = findViewById(R.id.yinhangzhanghu);
        koukuan = findViewById(R.id.koukuan);
        yifu = findViewById(R.id.yifu);
        beizhu = findViewById(R.id.beizhu);
        gongzi = findViewById(R.id.gongzi);

        riqi.setOnClickListener(this);


        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhFinanceGongZiMingXi = (YhFinanceGongZiMingXi) myApplication.getObj();

            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(yhFinanceGongZiMingXi.getRiqi().getTime());
            String java_date = sdf.format(date);
            riqi.setText(java_date);
            renming.setText(yhFinanceGongZiMingXi.getRenming());

            yinhangzhanghu.setText(yhFinanceGongZiMingXi.getYinhangzhanghu());
            koukuan.setText(yhFinanceGongZiMingXi.getKoukuan().toString());
            gongzi.setText(yhFinanceGongZiMingXi.getGongzi().toString());
            yifu.setText(yhFinanceGongZiMingXi.getYifu().toString());
            beizhu.setText(yhFinanceGongZiMingXi.getBeizhu());
        }else{
            yhFinanceGongZiMingXi = new YhFinanceGongZiMingXi();

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

    public void clearClick(View v) {
        riqi.setText("请选择");
        renming.setText("");
        yinhangzhanghu.setText("");
        koukuan.setText("");
        gongzi.setText("");
        yifu.setText("");
        beizhu.setText("");
    }





    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(GongZiMingXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZiMingXiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceGongZiMingXiService.update(yhFinanceGongZiMingXi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }




    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        // 在调用 insert 前设置公司名称
        String companyName = getCompanyNameFromCache();
        if (!companyName.isEmpty()) {
            yhFinanceGongZiMingXi.setCompany(companyName);
        }

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(GongZiMingXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZiMingXiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceGongZiMingXiService.insert(yhFinanceGongZiMingXi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    // 获取公司名称的方法
    private String getCompanyNameFromCache() {
        try {
            SharedPreferences sharedPref = getSharedPreferences("my_cache", Context.MODE_PRIVATE);
            return sharedPref.getString("companyName", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private boolean checkForm() throws ParseException {
        if (riqi.getText().toString().equals("请选择")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请输入日期");
            return false;
        } else {
            Timestamp startTime = CovertStrTODate(riqi.getText().toString());
            yhFinanceGongZiMingXi.setRiqi(CovertStrTODate(riqi.getText().toString()));
        }

        if (renming.getText().toString().equals("")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请输入姓名");
            return false;
        } else {
            yhFinanceGongZiMingXi.setRenming(renming.getText().toString());
        }

        if (yinhangzhanghu.getText().toString().equals("")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请输入银行账户");
            return false;
        } else {
            yhFinanceGongZiMingXi.setYinhangzhanghu(yinhangzhanghu.getText().toString());
        }

        if (koukuan.getText().toString().equals("")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请输入扣款");
            return false;
        } else {
            BigDecimal B1 = new BigDecimal(koukuan.getText().toString());
            yhFinanceGongZiMingXi.setKoukuan(B1);
        }

        if (gongzi.getText().toString().equals("")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请输入工资");
            return false;
        } else {
            BigDecimal B2 = new BigDecimal(gongzi.getText().toString());
            yhFinanceGongZiMingXi.setGongzi(B2);
        }

        if (yifu.getText().toString().equals("")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请输入已付");
            return false;
        } else {
            BigDecimal B3 = new BigDecimal(yifu.getText().toString());
            yhFinanceGongZiMingXi.setYifu(B3);
        }



        if (beizhu.getText().toString().equals("")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请输入备注");
            return false;
        } else {
            yhFinanceGongZiMingXi.setBeizhu(beizhu.getText().toString());
        }

        return true;
    }





    private void intiTimeDialog(Type claa) {
        pickerdialog = new TimePickerDialog.Builder()
                //设置类型
                .setType(claa)
                //设置选择时间监听回调
                .setCallBack(this)
                //设置标题
                .setTitleStringId("请选择时间")
                //设置时间
                //设置颜色
                .setThemeColor(getResources().getColor(R.color.button))
                //设置 字体大小
                .setWheelItemTextSize(15)
                //完毕
                .build();
        pickerdialog.show(getSupportFragmentManager(),"abc");
    }

    public void thisOnClick(View v) {
        intiTimeDialog(Type.ALL);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
//        Toast.makeText(this, "你选择的时间:"+getDateToString(millseconds), Toast.LENGTH_SHORT).show();
        if(date_name.equals("riqi")){
            riqi.setText(getDateToString(millseconds));
        }
    }

    //Android时间选择器，支持年月日时分，年月日，年月，月日时分，时分格式，可以设置最小时间（精确到分）
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.riqi:
                date_name = "riqi";
                intiTimeDialog(Type.ALL);
                break;
        }
    }

    public static Timestamp CovertStrTODate(String str) {
        Timestamp ts =null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setLenient(false);
        try {
            ts = new Timestamp(format.parse(str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return ts;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
