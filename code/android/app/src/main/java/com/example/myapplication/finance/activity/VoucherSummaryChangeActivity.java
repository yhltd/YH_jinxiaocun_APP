package com.example.myapplication.finance.activity;

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
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceVoucherSummary;
import com.example.myapplication.finance.service.YhFinanceDepartmentService;
import com.example.myapplication.finance.service.YhFinanceKeMuZongZhangService;
import com.example.myapplication.finance.service.YhFinanceVoucherSummaryService;
import com.example.myapplication.finance.service.YhFinanceVoucherWordService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class VoucherSummaryChangeActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceVoucherSummary yhFinanceVoucherSummary;
    private YhFinanceVoucherSummaryService yhFinanceVoucherSummaryService;
    private YhFinanceVoucherWordService yhFinanceVoucherWordService;
    private YhFinanceDepartmentService yhFinanceDepartmentService;

    private Spinner word;
    private EditText no;
    private EditText voucherDate;
    private EditText zhaiyao;
    private EditText code;
    private EditText money;
    private Spinner department;
    private EditText expenditure;
    private EditText note;
    private EditText real;

    String[] word_array;
    String[] department_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_summary_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceVoucherSummaryService = new YhFinanceVoucherSummaryService();
        yhFinanceVoucherWordService = new YhFinanceVoucherWordService();
        yhFinanceDepartmentService = new YhFinanceDepartmentService();

        word = findViewById(R.id.word);
        no = findViewById(R.id.no);
        voucherDate = findViewById(R.id.voucherDate);
        zhaiyao = findViewById(R.id.zhaiyao);
        code = findViewById(R.id.code);
        money = findViewById(R.id.money);
        department = findViewById(R.id.department);
        expenditure = findViewById(R.id.expenditure);
        note = findViewById(R.id.note);
        real = findViewById(R.id.real);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhFinanceVoucherSummary = new YhFinanceVoucherSummary();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            init2();
        } else if (id == R.id.update_btn) {
            yhFinanceVoucherSummary = (YhFinanceVoucherSummary) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            init1();
            no.setText(yhFinanceVoucherSummary.getNo());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            voucherDate.setText(df.format(yhFinanceVoucherSummary.getInsert_date()));
            zhaiyao.setText(yhFinanceVoucherSummary.getZhaiyao().toString());
            code.setText(String.valueOf(yhFinanceVoucherSummary.getCode()));
            money.setText(yhFinanceVoucherSummary.getMoney().toString());
            expenditure.setText(yhFinanceVoucherSummary.getExpenditure().toString());
            note.setText(yhFinanceVoucherSummary.getNote().toString());
            real.setText(yhFinanceVoucherSummary.getReal().toString());
        }
    }

    public void init1() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_word = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, word_array);
                word.setAdapter(StringUtils.cast(adapter_word));
                word.setSelection(getWordPosition(yhFinanceVoucherSummary.getWord()));
                SpinnerAdapter adapter_department = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, department_array);
                department.setAdapter(StringUtils.cast(adapter_department));
                department.setSelection(getDepartmentPosition(yhFinanceVoucherSummary.getDepartment()));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<YhFinanceExpenditure> wordList = yhFinanceVoucherWordService.getList(yhFinanceUser.getCompany());
                    if (wordList.size() > 0) {
                        word_array = new String[wordList.size()];
                        for (int i = 0; i < wordList.size(); i++) {
                            word_array[i] = wordList.get(i).getShouru();
                        }
                    }
                    List<YhFinanceDepartment> kehuList = yhFinanceDepartmentService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        department_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            department_array[i] = kehuList.get(i).getDepartment();
                        }
                    }
                    adapter = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, word_array);
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
                SpinnerAdapter adapter_word = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, word_array);
                word.setAdapter(StringUtils.cast(adapter_word));
                SpinnerAdapter adapter_department = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, department_array);
                department.setAdapter(StringUtils.cast(adapter_department));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<YhFinanceExpenditure> wordList = yhFinanceVoucherWordService.getList(yhFinanceUser.getCompany());
                    if (wordList.size() > 0) {
                        word_array = new String[wordList.size()];
                        for (int i = 0; i < wordList.size(); i++) {
                            word_array[i] = wordList.get(i).getShouru();
                        }
                    }
                    List<YhFinanceDepartment> kehuList = yhFinanceDepartmentService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        department_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            department_array[i] = kehuList.get(i).getDepartment();
                        }
                    }
                    adapter = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, word_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
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
                    ToastUtil.show(VoucherSummaryChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(VoucherSummaryChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceVoucherSummaryService.update(yhFinanceVoucherSummary);
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
                    ToastUtil.show(VoucherSummaryChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(VoucherSummaryChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceVoucherSummaryService.insert(yhFinanceVoucherSummary);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() throws ParseException {
        if (word.getSelectedItem().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请选择凭证字");
            return false;
        } else {
            yhFinanceVoucherSummary.setWord(word.getSelectedItem().toString());
        }

        if (no.getText().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请输凭证号");
            return false;
        } else {
            yhFinanceVoucherSummary.setNo(no.getText().toString());
        }

        if (voucherDate.getText().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请输录入时间");
            return false;
        } else {
            DateFormat spd = new SimpleDateFormat("yyyyMMddHHmmss");
            Timestamp startTime = new Timestamp(spd.parse(voucherDate.getText().toString()).getTime());
            yhFinanceVoucherSummary.setInsert_date(startTime);
        }

        if (zhaiyao.getText().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请输摘要");
            return false;
        } else {
            yhFinanceVoucherSummary.setZhaiyao(zhaiyao.getText().toString());
        }

        if (code.getText().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请输科目代码");
            return false;
        } else {
            yhFinanceVoucherSummary.setCode(Integer.parseInt(code.getText().toString()));
        }

        if (money.getText().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请输借贷金额");
            return false;
        } else {
            BigDecimal B4 = new BigDecimal(money.getText().toString());
            yhFinanceVoucherSummary.setMoney(B4);
        }

        if (department.getSelectedItem().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请选择部门");
            return false;
        } else {
            yhFinanceVoucherSummary.setDepartment(department.getSelectedItem().toString());
        }

        if (expenditure.getText().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请输开支项目");
            return false;
        } else {
            yhFinanceVoucherSummary.setExpenditure(expenditure.getText().toString());
        }

        if (real.getText().toString().equals("")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请输实收/付");
            return false;
        } else {
            BigDecimal B4 = new BigDecimal(real.getText().toString());
            yhFinanceVoucherSummary.setMoney(B4);
        }

        yhFinanceVoucherSummary.setNote(note.getText().toString());
        yhFinanceVoucherSummary.setCompany(yhFinanceUser.getCompany());
        return true;
    }

    private int getWordPosition(String param) {
        if (word_array != null) {
            for (int i = 0; i < word_array.length; i++) {
                if (param.equals(word_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getDepartmentPosition(String param) {
        if (department_array != null) {
            for (int i = 0; i < department_array.length; i++) {
                if (param.equals(department_array[i])) {
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
