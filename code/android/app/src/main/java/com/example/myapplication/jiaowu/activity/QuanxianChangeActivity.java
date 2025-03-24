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
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.AccountManagementService;
import com.example.myapplication.jiaowu.service.QuanxianService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

public class QuanxianChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private Quanxian quanxian;
    private QuanxianService quanxianService;

    private EditText Realname;
    private EditText view_name;
    private Spinner add;
    private Spinner del;
    private Spinner upd;
    private Spinner sel;

    String[] add_array;
    String[] del_array;
    String[] upd_array;
    String[] sel_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_account_quanxian_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        quanxianService = new QuanxianService();

        Realname = findViewById(R.id.Realname);
        view_name = findViewById(R.id.view_name);
        add = findViewById(R.id.add);
        del = findViewById(R.id.Del);
        upd = findViewById(R.id.Upd);
        sel = findViewById(R.id.Sel);

        String[] add_selectArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
        String[] Del_selectArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
        String[] Upd_selectArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
        String[] Sel_selectArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, add_selectArray);
        add.setAdapter(adapter);
        del.setAdapter(adapter);
        upd.setAdapter(adapter);
        sel.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            quanxian = new Quanxian();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            quanxian = (Quanxian) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            Realname.setText(quanxian.getRealname());
            view_name.setText(quanxian.getView_name());
//            add.setText(quanxian.getAdd());
//            del.setText(quanxian.getDel());
//            upd.setText(quanxian.getUpd());
//            sel.setText(quanxian.getSel());
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
                    ToastUtil.show(QuanxianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(QuanxianChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = quanxianService.insert(quanxian);
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
                    ToastUtil.show(QuanxianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(QuanxianChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = quanxianService.update(quanxian);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {

        quanxian.setRealname(Realname.getText().toString());
        quanxian.setView_name(view_name.getText().toString());
//        quanxian.setAdd(add.getText().toString());
//        quanxian.setDel(del.getText().toString());
//        quanxian.setUpd(upd.getText().toString());
//        quanxian.setSel(sel.getText().toString());
        quanxian.setAdd(add.getSelectedItem().toString());
        quanxian.setDel(del.getSelectedItem().toString());
        quanxian.setUpd(upd.getSelectedItem().toString());
        quanxian.setSel(sel.getSelectedItem().toString());
        quanxian.setCompany(teacher.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
