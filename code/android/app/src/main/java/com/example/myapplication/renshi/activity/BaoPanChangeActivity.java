package com.example.myapplication.renshi.activity;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiBaoPanShenPi;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiBaoPanShenPiService;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.utils.ToastUtil;

import java.util.Calendar;

public class BaoPanChangeActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiBaoPanShenPi yhRenShiBaoPanShenPi;
    private YhRenShiBaoPanShenPiService yhRenShiBaoPanShenPiService;

    private EditText shifa_gongzi;
    private EditText geren_zhichu;
    private EditText qiye_zhichu;
    private EditText yuangong_renshu;
    private EditText quanqin_tianshu;
    private EditText shenpiren;
    private EditText riqi;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baopan_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        yhRenShiBaoPanShenPiService = new YhRenShiBaoPanShenPiService();

        shifa_gongzi = findViewById(R.id.shifa_gongzi);
        geren_zhichu = findViewById(R.id.geren_zhichu);
        qiye_zhichu = findViewById(R.id.qiye_zhichu);
        yuangong_renshu = findViewById(R.id.yuangong_renshu);
        quanqin_tianshu = findViewById(R.id.quanqin_tianshu);
        shenpiren = findViewById(R.id.shenpiren);
        riqi = findViewById(R.id.riqi);
        showDateOnClick(riqi);
        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhRenShiBaoPanShenPi = new YhRenShiBaoPanShenPi();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            shifa_gongzi.setText(intent.getStringExtra("shifa_gongzi"));
            geren_zhichu.setText(intent.getStringExtra("geren_zhichu"));
            qiye_zhichu.setText(intent.getStringExtra("qiye_zhichu"));
            yuangong_renshu.setText(intent.getStringExtra("yuangong_renshu"));
            quanqin_tianshu.setText(intent.getStringExtra("quanqin_tianshu"));
            shenpiren.setText(yhRenShiUser.getB());

            Calendar now = Calendar.getInstance();

            riqi.setText(now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.getActualMaximum(Calendar.DAY_OF_MONTH));

        } else if (id == R.id.update_btn) {
            yhRenShiBaoPanShenPi = (YhRenShiBaoPanShenPi) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            shifa_gongzi.setText(yhRenShiBaoPanShenPi.getShifa_gongzi());
            geren_zhichu.setText(yhRenShiBaoPanShenPi.getGeren_zhichu());
            qiye_zhichu.setText(yhRenShiBaoPanShenPi.getQiye_zhichu());
            yuangong_renshu.setText(yhRenShiBaoPanShenPi.getYuangong_renshu());
            quanqin_tianshu.setText(yhRenShiBaoPanShenPi.getQuanqin_tianshu());
            shenpiren.setText(yhRenShiBaoPanShenPi.getShenpiren());
            riqi.setText(yhRenShiBaoPanShenPi.getRiqi());

        }
    }

    public void clearClick(View v) {
        shifa_gongzi.setText("");
        geren_zhichu.setText("");
        qiye_zhichu.setText("");
        yuangong_renshu.setText("");
        quanqin_tianshu.setText("");
        shenpiren.setText("");
        riqi.setText("");
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
                    ToastUtil.show(BaoPanChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BaoPanChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiBaoPanShenPiService.insert(yhRenShiBaoPanShenPi);
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
                    ToastUtil.show(BaoPanChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(BaoPanChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiBaoPanShenPiService.update(yhRenShiBaoPanShenPi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }



    @SuppressLint("ClickableViewAccessibility")
    protected void showDateOnClick(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(editText);
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg(editText);
                }

            }
        });
    }

    protected void showDatePickDlg(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(BaoPanChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private boolean checkForm() {
        
        yhRenShiBaoPanShenPi.setShifa_gongzi(shifa_gongzi.getText().toString());
        yhRenShiBaoPanShenPi.setGeren_zhichu(geren_zhichu.getText().toString());
        yhRenShiBaoPanShenPi.setQiye_zhichu(qiye_zhichu.getText().toString());
        yhRenShiBaoPanShenPi.setYuangong_renshu(yuangong_renshu.getText().toString());
        yhRenShiBaoPanShenPi.setQuanqin_tianshu(quanqin_tianshu.getText().toString());
        yhRenShiBaoPanShenPi.setShenpiren(shenpiren.getText().toString());
        yhRenShiBaoPanShenPi.setRiqi(riqi.getText().toString());
        yhRenShiBaoPanShenPi.setGongsi(yhRenShiUser.getL().replace("_hr",""));
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
