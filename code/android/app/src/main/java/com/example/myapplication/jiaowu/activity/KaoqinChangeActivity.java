package com.example.myapplication.jiaowu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Kaoqin;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.AccountManagementService;
import com.example.myapplication.jiaowu.service.KaoqinService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

public class KaoqinChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private Kaoqin kaoqin;
    private KaoqinService kaoqinService;

    private EditText s_name;
    private EditText nian;
    private Spinner yue;
    private Spinner ri1;
    private Spinner ri2;
    private Spinner ri3;
    private Spinner ri4;
    private Spinner ri5;
    private Spinner ri6;
    private Spinner ri7;
    private Spinner ri8;
    private Spinner ri9;
    private Spinner ri10;
    private Spinner ri11;
    private Spinner ri12;
    private Spinner ri13;
    private Spinner ri14;
    private Spinner ri15;
    private Spinner ri16;
    private Spinner ri17;
    private Spinner ri18;
    private Spinner ri19;
    private Spinner ri20;
    private Spinner ri21;
    private Spinner ri22;
    private Spinner ri23;
    private Spinner ri24;
    private Spinner ri25;
    private Spinner ri26;
    private Spinner ri27;
    private Spinner ri28;
    private Spinner ri29;
    private Spinner ri30;
    private Spinner ri31;

    String[] month_array;
    String[] ri_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_kaoqin_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        kaoqinService = new KaoqinService();

        s_name = findViewById(R.id.s_name);
        nian = findViewById(R.id.nian);
        yue = findViewById(R.id.yue);
        ri1 = findViewById(R.id.ri1);
        ri2 = findViewById(R.id.ri2);
        ri3 = findViewById(R.id.ri3);
        ri4 = findViewById(R.id.ri4);
        ri5 = findViewById(R.id.ri5);
        ri6 = findViewById(R.id.ri6);
        ri7 = findViewById(R.id.ri7);
        ri8 = findViewById(R.id.ri8);
        ri9 = findViewById(R.id.ri9);
        ri10 = findViewById(R.id.ri10);
        ri11 = findViewById(R.id.ri11);
        ri12 = findViewById(R.id.ri12);
        ri13 = findViewById(R.id.ri13);
        ri14 = findViewById(R.id.ri14);
        ri15 = findViewById(R.id.ri15);
        ri16 = findViewById(R.id.ri16);
        ri17 = findViewById(R.id.ri17);
        ri18 = findViewById(R.id.ri18);
        ri19 = findViewById(R.id.ri19);
        ri20 = findViewById(R.id.ri20);
        ri21 = findViewById(R.id.ri21);
        ri22 = findViewById(R.id.ri22);
        ri23 = findViewById(R.id.ri23);
        ri24 = findViewById(R.id.ri24);
        ri25 = findViewById(R.id.ri25);
        ri26 = findViewById(R.id.ri26);
        ri27 = findViewById(R.id.ri27);
        ri28 = findViewById(R.id.ri28);
        ri29 = findViewById(R.id.ri29);
        ri30 = findViewById(R.id.ri30);
        ri31 = findViewById(R.id.ri31);

        month_array = getResources().getStringArray(R.array.month_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, month_array);
        yue.setAdapter(adapter);

        ri_array = getResources().getStringArray(R.array.ri_array);
        ArrayAdapter<String> adapter_ri = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ri_array);
        ri1.setAdapter(adapter_ri);
        ri2.setAdapter(adapter_ri);
        ri3.setAdapter(adapter_ri);
        ri4.setAdapter(adapter_ri);
        ri5.setAdapter(adapter_ri);
        ri6.setAdapter(adapter_ri);
        ri7.setAdapter(adapter_ri);
        ri8.setAdapter(adapter_ri);
        ri9.setAdapter(adapter_ri);
        ri10.setAdapter(adapter_ri);
        ri11.setAdapter(adapter_ri);
        ri12.setAdapter(adapter_ri);
        ri13.setAdapter(adapter_ri);
        ri14.setAdapter(adapter_ri);
        ri15.setAdapter(adapter_ri);
        ri16.setAdapter(adapter_ri);
        ri17.setAdapter(adapter_ri);
        ri18.setAdapter(adapter_ri);
        ri19.setAdapter(adapter_ri);
        ri20.setAdapter(adapter_ri);
        ri21.setAdapter(adapter_ri);
        ri22.setAdapter(adapter_ri);
        ri23.setAdapter(adapter_ri);
        ri24.setAdapter(adapter_ri);
        ri25.setAdapter(adapter_ri);
        ri26.setAdapter(adapter_ri);
        ri27.setAdapter(adapter_ri);
        ri28.setAdapter(adapter_ri);
        ri29.setAdapter(adapter_ri);
        ri30.setAdapter(adapter_ri);
        ri31.setAdapter(adapter_ri);


        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            kaoqin = new Kaoqin();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            kaoqin = (Kaoqin) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            s_name.setText(kaoqin.getS_name());
            nian.setText(kaoqin.getNian());
            yue.setSelection(getYuePosition(kaoqin.getYue()));
            ri1.setSelection(getKaoQinPosition(kaoqin.getRi1()));
            ri2.setSelection(getKaoQinPosition(kaoqin.getRi2()));
            ri3.setSelection(getKaoQinPosition(kaoqin.getRi3()));
            ri4.setSelection(getKaoQinPosition(kaoqin.getRi4()));
            ri5.setSelection(getKaoQinPosition(kaoqin.getRi5()));
            ri6.setSelection(getKaoQinPosition(kaoqin.getRi6()));
            ri7.setSelection(getKaoQinPosition(kaoqin.getRi7()));
            ri8.setSelection(getKaoQinPosition(kaoqin.getRi8()));
            ri9.setSelection(getKaoQinPosition(kaoqin.getRi9()));
            ri10.setSelection(getKaoQinPosition(kaoqin.getRi10()));
            ri11.setSelection(getKaoQinPosition(kaoqin.getRi11()));
            ri12.setSelection(getKaoQinPosition(kaoqin.getRi12()));
            ri13.setSelection(getKaoQinPosition(kaoqin.getRi13()));
            ri14.setSelection(getKaoQinPosition(kaoqin.getRi14()));
            ri15.setSelection(getKaoQinPosition(kaoqin.getRi15()));
            ri16.setSelection(getKaoQinPosition(kaoqin.getRi16()));
            ri17.setSelection(getKaoQinPosition(kaoqin.getRi17()));
            ri18.setSelection(getKaoQinPosition(kaoqin.getRi18()));
            ri19.setSelection(getKaoQinPosition(kaoqin.getRi19()));
            ri20.setSelection(getKaoQinPosition(kaoqin.getRi20()));
            ri21.setSelection(getKaoQinPosition(kaoqin.getRi21()));
            ri22.setSelection(getKaoQinPosition(kaoqin.getRi22()));
            ri23.setSelection(getKaoQinPosition(kaoqin.getRi23()));
            ri24.setSelection(getKaoQinPosition(kaoqin.getRi24()));
            ri25.setSelection(getKaoQinPosition(kaoqin.getRi25()));
            ri26.setSelection(getKaoQinPosition(kaoqin.getRi26()));
            ri27.setSelection(getKaoQinPosition(kaoqin.getRi27()));
            ri28.setSelection(getKaoQinPosition(kaoqin.getRi28()));
            ri29.setSelection(getKaoQinPosition(kaoqin.getRi29()));
            ri30.setSelection(getKaoQinPosition(kaoqin.getRi30()));
            ri31.setSelection(getKaoQinPosition(kaoqin.getRi31()));
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
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KaoqinChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KaoqinChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = kaoqinService.insert(kaoqin);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KaoqinChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KaoqinChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = kaoqinService.update(kaoqin);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {

        if (s_name.getText().toString().equals("")) {
            ToastUtil.show(KaoqinChangeActivity.this, "请输入教师");
            return false;
        } else {
            kaoqin.setS_name(s_name.getText().toString());
        }

        if (nian.getText().toString().equals("")) {
            ToastUtil.show(KaoqinChangeActivity.this, "请输入年份");
            return false;
        } else {
            kaoqin.setNian(nian.getText().toString());
        }

        kaoqin.setYue(yue.getSelectedItem().toString());

        kaoqin.setRi1(ri1.getSelectedItem().toString());
        kaoqin.setRi2(ri2.getSelectedItem().toString());
        kaoqin.setRi3(ri3.getSelectedItem().toString());
        kaoqin.setRi4(ri4.getSelectedItem().toString());
        kaoqin.setRi5(ri5.getSelectedItem().toString());
        kaoqin.setRi6(ri6.getSelectedItem().toString());
        kaoqin.setRi7(ri7.getSelectedItem().toString());
        kaoqin.setRi8(ri8.getSelectedItem().toString());
        kaoqin.setRi9(ri9.getSelectedItem().toString());
        kaoqin.setRi10(ri10.getSelectedItem().toString());
        kaoqin.setRi11(ri11.getSelectedItem().toString());
        kaoqin.setRi12(ri12.getSelectedItem().toString());
        kaoqin.setRi13(ri13.getSelectedItem().toString());
        kaoqin.setRi14(ri14.getSelectedItem().toString());
        kaoqin.setRi15(ri15.getSelectedItem().toString());
        kaoqin.setRi16(ri16.getSelectedItem().toString());
        kaoqin.setRi17(ri17.getSelectedItem().toString());
        kaoqin.setRi18(ri18.getSelectedItem().toString());
        kaoqin.setRi19(ri19.getSelectedItem().toString());
        kaoqin.setRi20(ri20.getSelectedItem().toString());
        kaoqin.setRi21(ri21.getSelectedItem().toString());
        kaoqin.setRi22(ri22.getSelectedItem().toString());
        kaoqin.setRi23(ri23.getSelectedItem().toString());
        kaoqin.setRi24(ri24.getSelectedItem().toString());
        kaoqin.setRi25(ri25.getSelectedItem().toString());
        kaoqin.setRi26(ri26.getSelectedItem().toString());
        kaoqin.setRi27(ri27.getSelectedItem().toString());
        kaoqin.setRi28(ri28.getSelectedItem().toString());
        kaoqin.setRi29(ri29.getSelectedItem().toString());
        kaoqin.setRi30(ri30.getSelectedItem().toString());
        kaoqin.setRi31(ri31.getSelectedItem().toString());

        kaoqin.setCompany(teacher.getCompany());

        return true;
    }

    private int getYuePosition(String param) {
        if (month_array != null) {
            for (int i = 0; i < month_array.length; i++) {
                if (param.equals(month_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getKaoQinPosition(String param) {
        if (ri_array != null) {
            for (int i = 0; i < ri_array.length; i++) {
                if (param.equals(ri_array[i])) {
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
