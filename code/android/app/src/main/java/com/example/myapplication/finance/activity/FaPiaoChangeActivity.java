package com.example.myapplication.finance.activity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceFaPiao;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceFaPiaoService;
import com.example.myapplication.finance.service.YhFinanceInvoicePeizhiService;
import com.example.myapplication.finance.service.YhFinanceJiJianTaiZhangService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceSimpleAccountingService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FaPiaoChangeActivity extends AppCompatActivity {
    private YhFinanceUser yhFinanceUser;
    private YhFinanceFaPiao yhFinanceFaPiao;
    private YhFinanceFaPiaoService yhFinanceFaPiaoService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;
    private YhFinanceInvoicePeizhiService yhFinanceInvoicePeizhiService;

    private Spinner type;
    private EditText riqi;
    private EditText zhaiyao;
    private Spinner unit;
    private Spinner invoice_type;
    private EditText invoice_no;
    private EditText jine;
    private EditText remarks;

    List<YhFinanceJiJianPeiZhi> getList;


    String[] invoice_type_array;
    String[] kehu_array;
    String[] class_selectArray;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fapiao_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceFaPiaoService = new YhFinanceFaPiaoService();
        yhFinanceKehuPeizhiService = new YhFinanceKehuPeizhiService();
        yhFinanceInvoicePeizhiService = new YhFinanceInvoicePeizhiService();

        type = findViewById(R.id.type);
        riqi = findViewById(R.id.riqi);
        zhaiyao = findViewById(R.id.zhaiyao);
        unit = findViewById(R.id.unit);
        invoice_type = findViewById(R.id.invoice_type);
        invoice_no = findViewById(R.id.invoice_no);
        jine = findViewById(R.id.jine);
        remarks = findViewById(R.id.remarks);

        showDateOnClick(riqi);

        class_selectArray = getResources().getStringArray(R.array.fapiao_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, class_selectArray);
        type.setAdapter(adapter);


        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhFinanceFaPiao = (YhFinanceFaPiao) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            riqi.setText(yhFinanceFaPiao.getRiqi().toString());
            zhaiyao.setText(yhFinanceFaPiao.getZhaiyao());
            invoice_no.setText(yhFinanceFaPiao.getInvoice_no());
            type.setSelection(getType(yhFinanceFaPiao.getType()));
            jine.setText(yhFinanceFaPiao.getJine().toString());
            remarks.setText(yhFinanceFaPiao.getRemarks().toString());

        }else{
            yhFinanceFaPiao = new YhFinanceFaPiao();
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

    public void clearClick(View v) {
        riqi.setText("");
        zhaiyao.setText("");
        invoice_no.setText("");
        jine.setText("");
        remarks.setText("");
    }


    public void init1() {
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_project = new ArrayAdapter<String>(FaPiaoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, invoice_type_array);
                invoice_type.setAdapter(StringUtils.cast(adapter_project));
                invoice_type.setSelection(getAccountingPosition(yhFinanceFaPiao.getInvoice_type()));
                SpinnerAdapter adapter_kehu = new ArrayAdapter<String>(FaPiaoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);
                unit.setAdapter(StringUtils.cast(adapter_kehu));
                unit.setSelection(getKehuPosition(yhFinanceFaPiao.getUnit()));
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<YhFinanceJiJianPeiZhi> unitList = yhFinanceInvoicePeizhiService.getList(yhFinanceUser.getCompany());
                    if (unitList.size() > 0) {
                        invoice_type_array = new String[unitList.size()];
                        for (int i = 0; i < unitList.size(); i++) {
                            invoice_type_array[i] = unitList.get(i).getPeizhi();
                        }
                    }
                    List<YhFinanceJiJianPeiZhi> kehuList = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        kehu_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array[i] = kehuList.get(i).getPeizhi();
                        }
                    }
                    adapter = new ArrayAdapter<String>(FaPiaoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, invoice_type_array);
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
                SpinnerAdapter adapter_project = new ArrayAdapter<String>(FaPiaoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, invoice_type_array);
                invoice_type.setAdapter(StringUtils.cast(adapter_project));
                SpinnerAdapter adapter_kehu = new ArrayAdapter<String>(FaPiaoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);
                unit.setAdapter(StringUtils.cast(adapter_kehu));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<YhFinanceJiJianPeiZhi> unitList = yhFinanceInvoicePeizhiService.getList(yhFinanceUser.getCompany());
                    if (unitList.size() > 0) {
                        invoice_type_array = new String[unitList.size()];
                        for (int i = 0; i < unitList.size(); i++) {
                            invoice_type_array[i] = unitList.get(i).getPeizhi();
                        }
                    }
                    List<YhFinanceJiJianPeiZhi> kehuList = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        kehu_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array[i] = kehuList.get(i).getPeizhi();
                        }
                    }
                    adapter = new ArrayAdapter<String>(FaPiaoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, invoice_type_array);
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
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(FaPiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(FaPiaoChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = yhFinanceFaPiaoService.update(yhFinanceFaPiao);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(FaPiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(FaPiaoChangeActivity.this, "保存失败，请稍后再试");
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
                msg.obj = yhFinanceFaPiaoService.insert(yhFinanceFaPiao);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(FaPiaoChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String this_month = "";
                if (monthOfYear + 1 < 10){
                    this_month = "0" + String.format("%s",monthOfYear + 1);
                }else{
                    this_month = String.format("%s",monthOfYear + 1);
                }

                String this_day = "";
                if (dayOfMonth + 1 < 10){
                    this_day = "0" + String.format("%s",dayOfMonth);
                }else{
                    this_day = String.format("%s",dayOfMonth);
                }
                editText.setText(year + "-" + this_month + "-" + this_day);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private boolean checkForm() throws ParseException {

        if (type.getSelectedItem().toString().equals("")) {
            ToastUtil.show(FaPiaoChangeActivity.this, "请选择类型");
            return false;
        } else {
            yhFinanceFaPiao.setType(type.getSelectedItem().toString());
        }

        if (riqi.getText().toString().equals("")) {
            ToastUtil.show(FaPiaoChangeActivity.this, "请输入日期");
            return false;
        } else {
            yhFinanceFaPiao.setRiqi(riqi.getText().toString());
        }

        if (zhaiyao.getText().toString().equals("")) {
            ToastUtil.show(FaPiaoChangeActivity.this, "请输入摘要");
            return false;
        } else {
            yhFinanceFaPiao.setZhaiyao(zhaiyao.getText().toString());
        }

        if (unit.getSelectedItem().toString().equals("")) {
            ToastUtil.show(FaPiaoChangeActivity.this, "请选择往来单位");
            return false;
        } else {
            yhFinanceFaPiao.setUnit(unit.getSelectedItem().toString());
        }

        if (invoice_type.getSelectedItem().toString().equals("")) {
            ToastUtil.show(FaPiaoChangeActivity.this, "请选择发票种类");
            return false;
        } else {
            yhFinanceFaPiao.setInvoice_type(invoice_type.getSelectedItem().toString());
        }

        if (invoice_no.getText().toString().equals("")) {
            ToastUtil.show(FaPiaoChangeActivity.this, "请输入发票号码");
            return false;
        } else {
            yhFinanceFaPiao.setInvoice_no(invoice_no.getText().toString());
        }

        if (jine.getText().toString().equals("")) {
            ToastUtil.show(FaPiaoChangeActivity.this, "请输入金额");
            return false;
        } else {
            yhFinanceFaPiao.setJine(jine.getText().toString());
        }

        yhFinanceFaPiao.setRemarks(remarks.getText().toString());
        yhFinanceFaPiao.setCompany(yhFinanceUser.getCompany());

        return true;
    }


    private int getAccountingPosition(String param) {
        if (invoice_type_array != null) {
            for (int i = 0; i < invoice_type_array.length; i++) {
                if (param.equals(invoice_type_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getType(String param) {
        if (class_selectArray != null) {
            for (int i = 0; i < class_selectArray.length; i++) {
                if (param.equals(class_selectArray[i])) {
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
