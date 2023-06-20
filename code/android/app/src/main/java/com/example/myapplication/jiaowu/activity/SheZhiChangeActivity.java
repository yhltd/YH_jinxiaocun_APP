package com.example.myapplication.jiaowu.activity;

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
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

public class SheZhiChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private SheZhi sheZhi;
    private SheZhiService sheZhiService;

    private EditText course;
    private EditText teacher_t;
    private EditText type;
    private EditText paiment;
    private EditText msort;
    private EditText psort;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_shezhi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        sheZhiService = new SheZhiService();

        course = findViewById(R.id.course);
        teacher_t = findViewById(R.id.teacher);
        type = findViewById(R.id.type);
        paiment = findViewById(R.id.paiment);
        msort = findViewById(R.id.msort);
        psort = findViewById(R.id.psort);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            sheZhi = new SheZhi();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            sheZhi = (SheZhi) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            course.setText(sheZhi.getCourse());
            teacher_t.setText(sheZhi.getTeacher());
            type.setText(sheZhi.getType());
            paiment.setText(sheZhi.getPaiment());
            msort.setText(sheZhi.getMsort());
            psort.setText(sheZhi.getPsort());
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
                    ToastUtil.show(SheZhiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(SheZhiChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = sheZhiService.insert(sheZhi);
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
                    ToastUtil.show(SheZhiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(SheZhiChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = sheZhiService.update(sheZhi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {

        sheZhi.setCourse(course.getText().toString());
        sheZhi.setTeacher(teacher_t.getText().toString());
        sheZhi.setType(type.getText().toString());
        sheZhi.setPaiment(paiment.getText().toString());
        sheZhi.setMsort(msort.getText().toString());
        sheZhi.setPsort(psort.getText().toString());
        sheZhi.setCompany(teacher.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
