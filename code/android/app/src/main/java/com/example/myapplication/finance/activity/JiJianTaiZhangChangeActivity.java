package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceJiJianTaiZhangService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceSimpleAccountingService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JiJianTaiZhangChangeActivity extends AppCompatActivity {
    private YhFinanceUser yhFinanceUser;
    private YhFinanceJiJianTaiZhang yhFinanceJiJianTaiZhang;
    private YhFinanceJiJianTaiZhangService yhFinanceJiJianTaiZhangService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;
    private YhFinanceSimpleAccountingService yhFinanceSimpleAccountingService;

    private EditText insert_date;
    private Spinner accounting;
    private EditText project;
    private Spinner kehu;
    private EditText receivable;
    private EditText receipts;
    private EditText cope;
    private EditText payment;
    private EditText zhaiyao;


    String[] project_array;
    String[] kehu_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jijiantaizhang_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceJiJianTaiZhangService = new YhFinanceJiJianTaiZhangService();
        yhFinanceKehuPeizhiService = new YhFinanceKehuPeizhiService();
        yhFinanceSimpleAccountingService = new YhFinanceSimpleAccountingService();

        insert_date = findViewById(R.id.insert_date);
        accounting = findViewById(R.id.accounting);
        project = findViewById(R.id.project);
        kehu = findViewById(R.id.kehu);
        receivable = findViewById(R.id.receivable);
        receipts = findViewById(R.id.receipts);
        cope = findViewById(R.id.cope);
        payment = findViewById(R.id.payment);
        zhaiyao = findViewById(R.id.zhaiyao);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhFinanceJiJianTaiZhang = (YhFinanceJiJianTaiZhang) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            insert_date.setText(yhFinanceJiJianTaiZhang.getInsert_date().toString());
            project.setText(yhFinanceJiJianTaiZhang.getProject());

            receivable.setText(yhFinanceJiJianTaiZhang.getReceivable().toString());
            receipts.setText(yhFinanceJiJianTaiZhang.getReceipts().toString());
            cope.setText(yhFinanceJiJianTaiZhang.getCope().toString());
            payment.setText(yhFinanceJiJianTaiZhang.getPayment().toString());
            zhaiyao.setText(yhFinanceJiJianTaiZhang.getZhaiyao());
        }else{
            yhFinanceJiJianTaiZhang = new YhFinanceJiJianTaiZhang();
            init2();
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


    public void init1() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_project = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, project_array);
                accounting.setAdapter(StringUtils.cast(adapter_project));
                accounting.setSelection(getAccountingPosition(yhFinanceJiJianTaiZhang.getAccounting()));
                SpinnerAdapter adapter_kehu = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);
                kehu.setAdapter(StringUtils.cast(adapter_kehu));
                kehu.setSelection(getKehuPosition(yhFinanceJiJianTaiZhang.getKehu()));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<YhFinanceJiJianPeiZhi> accountingList = yhFinanceSimpleAccountingService.getList(yhFinanceUser.getCompany());
                    if (accountingList.size() > 0) {
                        project_array = new String[accountingList.size()];
                        for (int i = 0; i < accountingList.size(); i++) {
                            project_array[i] = accountingList.get(i).getPeizhi();
                        }
                    }
                    List<YhFinanceJiJianPeiZhi> kehuList = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        kehu_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array[i] = kehuList.get(i).getPeizhi();
                        }
                    }
                    adapter = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, project_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void init2() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_project = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, project_array);
                accounting.setAdapter(StringUtils.cast(adapter_project));
                SpinnerAdapter adapter_kehu = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);
                kehu.setAdapter(StringUtils.cast(adapter_kehu));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<YhFinanceJiJianPeiZhi> accountingList = yhFinanceSimpleAccountingService.getList(yhFinanceUser.getCompany());
                    if (accountingList.size() > 0) {
                        project_array = new String[accountingList.size()];
                        for (int i = 0; i < accountingList.size(); i++) {
                            project_array[i] = accountingList.get(i).getPeizhi();
                        }
                    }
                    List<YhFinanceJiJianPeiZhi> kehuList = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        kehu_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array[i] = kehuList.get(i).getPeizhi();
                        }
                    }
                    adapter = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, project_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiJianTaiZhangChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiJianTaiZhangChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceJiJianTaiZhangService.update(yhFinanceJiJianTaiZhang);
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
                    ToastUtil.show(JiJianTaiZhangChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiJianTaiZhangChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceJiJianTaiZhangService.insert(yhFinanceJiJianTaiZhang);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() throws ParseException {
        if (insert_date.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入日期");
            return false;
        } else {
            DateFormat spd = new SimpleDateFormat("yyyyMMddHHmmss");
            Timestamp startTime = new Timestamp(spd.parse(insert_date.getText().toString()).getTime());
            yhFinanceJiJianTaiZhang.setInsert_date(startTime);
        }

        if (accounting.getSelectedItem().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请选择科目名称");
            return false;
        } else {
            yhFinanceJiJianTaiZhang.setAccounting(accounting.getSelectedItem().toString());
        }

        if (project.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入项目名称");
            return false;
        } else {
            yhFinanceJiJianTaiZhang.setProject(project.getText().toString());
        }

        if (kehu.getSelectedItem().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请选择客户/供应商");
            return false;
        } else {
            yhFinanceJiJianTaiZhang.setKehu(kehu.getSelectedItem().toString());
        }

        if (receivable.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入应收");
            return false;
        } else {
            BigDecimal B1 = new BigDecimal(receivable.getText().toString());
            yhFinanceJiJianTaiZhang.setReceivable(B1);
        }

        if (receipts.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入实收");
            return false;
        } else {
            BigDecimal B2 = new BigDecimal(receipts.getText().toString());
            yhFinanceJiJianTaiZhang.setReceipts(B2);
        }

        if (cope.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入应付");
            return false;
        } else {
            BigDecimal B3 = new BigDecimal(cope.getText().toString());
            yhFinanceJiJianTaiZhang.setCope(B3);
        }

        if (payment.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入实付");
            return false;
        } else {
            BigDecimal B4 = new BigDecimal(payment.getText().toString());
            yhFinanceJiJianTaiZhang.setPayment(B4);
        }

        if (zhaiyao.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入摘要");
            return false;
        } else {
            yhFinanceJiJianTaiZhang.setZhaiyao(zhaiyao.getText().toString());
        }

        return true;
    }


    private int getAccountingPosition(String param) {
        if (project_array != null) {
            for (int i = 0; i < project_array.length; i++) {
                if (param.equals(project_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getKehuPosition(String param) {
        if (kehu_array != null) {
            for (int i = 0; i < kehu_array.length; i++) {
                if (param.equals(kehu_array[i])) {
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
