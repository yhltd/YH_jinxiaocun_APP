package com.example.myapplication.mendian.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.activity.VoucherSummaryActivity;
import com.example.myapplication.finance.service.YhFinanceVoucherSummaryService;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.ShouZhiMingXi;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.jiaowu.service.ShouZhiMingXiService;
import com.example.myapplication.mendian.entity.YhMendianOrderDetail;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductInsActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;
    private YhMendianProductshezhi yhMendianProductshezhi;
    private YhMendianOrderDetail yhMendianOrderDetail;

    private TextView name;
    private TextView type;
    private TextView danwei;
    private EditText num;
    private Spinner guige;
    private Spinner kouwei;
    private TextView price;

    private String cplx_text;
    private String cpmc_text;
    private String dw_text;
    private String dj_text;
    private String guige_text;
    private String kouwei_text;

    private String price_text;

    String[] guige_array;
    String[] kouwei_array;
    String[] price_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_ins);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianProductshezhi = new YhMendianProductshezhi();
        yhMendianOrderDetail = new YhMendianOrderDetail();
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        num = findViewById(R.id.num);
        guige = findViewById(R.id.guige);
        kouwei = findViewById(R.id.kouwei);
        price = findViewById(R.id.price);
        danwei = findViewById(R.id.danwei);

        cplx_text = getIntent().getStringExtra("cplx");
        cpmc_text = getIntent().getStringExtra("cpmc");
        dw_text = getIntent().getStringExtra("dw");
        dj_text = getIntent().getStringExtra("dj");
        guige_text = getIntent().getStringExtra("guige");
        kouwei_text = getIntent().getStringExtra("kouwei");

        guige_array = guige_text.split(",");
        kouwei_array = kouwei_text.split(",");
        price_array = dj_text.split(",");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, guige_array);
        guige.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kouwei_array);
        kouwei.setAdapter(adapter);
        name.setText(cpmc_text);
        type.setText(cplx_text);
        danwei.setText(dw_text);
        guige.setOnItemSelectedListener(new typeSelectSelectedListener());
    }

    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int this_row = getGuiGePosition(guige.getSelectedItem().toString());
            price.setText(price_array[this_row]);
            price_text = price_array[this_row];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        ToastUtil.show(ProductInsActivity.this, "已加入购物车");
        MyApplication application = (MyApplication) getApplicationContext();
        application.setYhMendianOrderDetail(yhMendianOrderDetail);
        back();
    }


    private boolean checkForm() throws ParseException {

        if (num.getText().toString().equals("")) {
            ToastUtil.show(ProductInsActivity.this, "请输入数量");
            return false;
        } else {
            yhMendianOrderDetail.setGs(num.getText().toString());
        }

        yhMendianOrderDetail.setCplx(type.getText().toString());
        yhMendianOrderDetail.setCpmc(name.getText().toString());
        yhMendianOrderDetail.setDw(danwei.getText().toString());
        yhMendianOrderDetail.setDj(price.getText().toString());
        yhMendianOrderDetail.setDzbl("1");
        yhMendianOrderDetail.setZhdj(price.getText().toString());
        float zhje = Float.parseFloat(price.getText().toString()) * Float.parseFloat(num.getText().toString());
        yhMendianOrderDetail.setZhje(Float.toString(zhje));
        yhMendianOrderDetail.setCompany(yhMendianUser.getCompany());
        return true;
    }


    private int getGuiGePosition(String param) {
        if (guige_array != null) {
            for (int i = 0; i < guige_array.length; i++) {
                if (param.equals(guige_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
