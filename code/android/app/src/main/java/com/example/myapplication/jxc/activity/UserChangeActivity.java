package com.example.myapplication.jxc.activity;

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
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.jxc.service.YhJinXiaoCunUserService;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunUser user;
    private YhJinXiaoCunUserService yhJinXiaoCunUserService;

    private EditText name;
    private EditText password;
    private Spinner adminis;
    private Spinner btype;

    private String pd;

    private String[] adminis_array;
    private String[] btype_array;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        user = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunUserService = new YhJinXiaoCunUserService();

        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        adminis = findViewById(R.id.adminis);
        btype = findViewById(R.id.btype);

        adminis_array = new String[]{"管理员", "普通用户"};
        btype_array = new String[]{"正常", "禁用"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, adminis_array);
        adminis.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, btype_array);
        btype.setAdapter(adapter2);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhJinXiaoCunUser = new YhJinXiaoCunUser();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            pd = "增";
        } else if (id == R.id.update_btn) {
            yhJinXiaoCunUser = (YhJinXiaoCunUser) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            name.setText(yhJinXiaoCunUser.getName());
            password.setText(yhJinXiaoCunUser.getPassword());
            adminis.setSelection(getAdminisPosition(yhJinXiaoCunUser.getAdminis()));
            btype.setSelection(getBtypePosition(yhJinXiaoCunUser.getBtype()));

            pd = "改";

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
                    ToastUtil.show(UserChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(UserChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                List<YhJinXiaoCunUser> count = yhJinXiaoCunUserService.getListById(yhJinXiaoCunUser.get_id());
                if (count.size() != 0) {
                    yhJinXiaoCunUser.set_id(yhJinXiaoCunUser.get_id() + "0");
                }
                msg.obj = yhJinXiaoCunUserService.insert(yhJinXiaoCunUser);
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
                    ToastUtil.show(UserChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(UserChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunUserService.update(yhJinXiaoCunUser);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (name.getText().toString().equals("")) {
            ToastUtil.show(UserChangeActivity.this, "请输入用户名");
            return false;
        } else {
            yhJinXiaoCunUser.setName(name.getText().toString());
        }

        if (password.getText().toString().equals("")) {
            ToastUtil.show(UserChangeActivity.this, "请输入密码");
            return false;
        } else {
            yhJinXiaoCunUser.setPassword(password.getText().toString());
        }

        if (adminis.getSelectedItem().toString().equals("管理员")) {
            yhJinXiaoCunUser.setAdminis("true");
        } else {
            yhJinXiaoCunUser.setAdminis("false");
        }

        yhJinXiaoCunUser.setBtype(btype.getSelectedItem().toString());

        if (pd.equals("增")) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat spd = new SimpleDateFormat("yyyyMMddhhmmss");
            Date date = new Date();
            yhJinXiaoCunUser.set_id(spd.format(date));
        }

        yhJinXiaoCunUser.setGongsi(user.getGongsi());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


    private int getAdminisPosition(String finishType) {
        for (int i = 0; i < adminis_array.length; i++) {
            if (finishType.equals(adminis_array[i])) {
                return i;
            }
        }
        return 0;
    }

    private int getBtypePosition(String finishType) {
        for (int i = 0; i < btype_array.length; i++) {
            if (finishType.equals(btype_array[i])) {
                return i;
            }
        }
        return 0;
    }
}
