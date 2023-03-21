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
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianOrders;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianMemberinfoService;
import com.example.myapplication.mendian.service.YhMendianOrdersService;
import com.example.myapplication.mendian.service.YhMendianUserService;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.List;

public class OrdersChangeActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;
    private YhMendianUserService yhMendianUserService;

    private YhMendianOrders yhMendianOrders;
    private YhMendianOrdersService yhMendianOrdersService;

    private EditText riqi;
    private EditText ddh;
    private EditText hyzh;
    private EditText hyxm;
    private EditText yhfa;
    private EditText xfje;
    private EditText ssje;
    private EditText yhje;
    private EditText syy;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianUserService = new YhMendianUserService();
        yhMendianOrders = new YhMendianOrders();
        yhMendianOrdersService = new YhMendianOrdersService();

        riqi = findViewById(R.id.riqi);
        ddh = findViewById(R.id.ddh);
        hyzh = findViewById(R.id.hyzh);
        hyxm = findViewById(R.id.hyxm);
        yhfa = findViewById(R.id.yhfa);
        syy = findViewById(R.id.syy);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhMendianOrders = new YhMendianOrders();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }else if(id == R.id.update_btn) {
            yhMendianOrders = (YhMendianOrders) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            riqi.setText(yhMendianOrders.getRiqi());
            ddh.setText(yhMendianOrders.getDdh());
            hyzh.setText(yhMendianOrders.getHyzh());
            hyxm.setText(yhMendianOrders.getHyxm());
            yhfa.setText(yhMendianOrders.getYhfa());
            syy.setText(yhMendianOrders.getSyy());
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
                    ToastUtil.show(OrdersChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(OrdersChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianOrdersService.updateByoOrders(yhMendianOrders);
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
                    ToastUtil.show(OrdersChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(OrdersChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianOrdersService.insertByOrders(yhMendianOrders);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {

        if (riqi.getText().toString().equals("")) {
            ToastUtil.show(OrdersChangeActivity.this, "请输入日期");
            return false;
        } else {
            yhMendianOrders.setRiqi(riqi.getText().toString());
        }

        if (ddh.getText().toString().equals("")) {
            ToastUtil.show(OrdersChangeActivity.this, "请输入订单号");
            return false;
        } else {
            yhMendianOrders.setDdh(ddh.getText().toString());
        }
        if (hyzh.getText().toString().equals("")) {
            ToastUtil.show(OrdersChangeActivity.this, "请输入会员账号");
            return false;
        } else {
            yhMendianOrders.setHyzh(hyzh.getText().toString());
        }
        if (hyxm.getText().toString().equals("")) {
            ToastUtil.show(OrdersChangeActivity.this, "请输入会员姓名");
            return false;
        } else {
            yhMendianOrders.setHyxm(hyxm.getText().toString());
        }
        if (yhfa.getText().toString().equals("")) {
            ToastUtil.show(OrdersChangeActivity.this, "请输入优惠方案");
            return false;
        } else {
            yhMendianOrders.setYhfa(yhfa.getText().toString());
        }
        if (syy.getText().toString().equals("")) {
            ToastUtil.show(OrdersChangeActivity.this, "请输入电话");
            return false;
        } else {
            yhMendianOrders.setSyy(syy.getText().toString());
        }


        yhMendianOrders.setCompany(yhMendianOrders.getCompany());

        return true;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
