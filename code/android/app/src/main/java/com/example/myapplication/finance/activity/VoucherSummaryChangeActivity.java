package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

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
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VoucherSummaryChangeActivity extends AppCompatActivity implements OnDateSetListener, View.OnClickListener {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceVoucherSummary yhFinanceVoucherSummary;
    private YhFinanceVoucherSummaryService yhFinanceVoucherSummaryService;
    private YhFinanceVoucherWordService yhFinanceVoucherWordService;
    private YhFinanceDepartmentService yhFinanceDepartmentService;
    private YhFinanceKeMuZongZhangService yhFinanceKeMuZongZhangService;


    private Spinner word;
    private EditText no;
    private TextView voucherDate;
    private EditText zhaiyao;
    private Spinner kemu_type;
    private Spinner kemu_lv;
    private Spinner kemu_name;
    private EditText code;
    private EditText money;
    private Spinner department;
    private EditText expenditure;
    private EditText note;
    private EditText real;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TimePickerDialog pickerdialog;
    private String date_name;
    // 添加这两个变量到你的类变量中
    private String typeText = "资产类"; // 设置默认值
    private String lvText = "一级";    // 设置默认值
    private String nameText;
    private boolean needAutoSelectKemu = false;
    private int autoSelectCode = 0;
    private String autoSelectName = "";
    List<YhFinanceKeMuZongZhang> list;

    String[] word_array;
    String[] department_array;
    String[] type_array = {"资产类", "负债类", "权益类", "成本类", "损益类"};
    String[] lv_array = {"一级", "二级", "三级"};
    String[] name_array;
    private boolean isLoadingKemu = false;

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
        yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();

        word = findViewById(R.id.word);
        no = findViewById(R.id.no);
        voucherDate = findViewById(R.id.voucherDate);
        zhaiyao = findViewById(R.id.zhaiyao);
        kemu_type = findViewById(R.id.kemu_type);
        kemu_lv = findViewById(R.id.kemu_lv);
        kemu_name = findViewById(R.id.kemu_name);
        code = findViewById(R.id.code);
        money = findViewById(R.id.money);
        department = findViewById(R.id.department);
        expenditure = findViewById(R.id.expenditure);
        note = findViewById(R.id.note);
        real = findViewById(R.id.real);

        // 分别设置不同的监听器
        kemu_type.setOnItemSelectedListener(new typeSelectSelectedListener());
        kemu_lv.setOnItemSelectedListener(new lvSelectSelectedListener());

        kemu_name.setOnItemSelectedListener(new nameSelectSelectedListener());

        voucherDate.setOnClickListener(this);

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
            if (yhFinanceVoucherSummary.getReal() != null) {
                real.setText(yhFinanceVoucherSummary.getReal().toString());
            } else {
                real.setText("0");
            }
            if (yhFinanceVoucherSummary.getName() != null && !yhFinanceVoucherSummary.getName().isEmpty()) {
                // 设置一个标志，表示需要自动选择科目
                needAutoSelectKemu = true;
                autoSelectCode = yhFinanceVoucherSummary.getCode();
                autoSelectName = yhFinanceVoucherSummary.getName();
            }
        }
    }

    // 为 kemu_type 创建独立的监听器
    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 只处理 type 的选择
            if (position >= 0 && position < type_array.length) {
                typeText = type_array[position];
            } else {
                typeText = "资产类"; // 默认值
            }

            // 获取当前 lv 的选中项
            int lvPosition = kemu_lv.getSelectedItemPosition();
            if (lvPosition >= 0 && lvPosition < lv_array.length) {
                lvText = lv_array[lvPosition];
            } else {
                lvText = "一级"; // 默认值
            }

            Log.d("TypeSelect", "选择类型: " + typeText + ", 级别: " + lvText);

            // 加载科目名称数据
            loadKemuNames(typeText, lvText);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    // 为 kemu_lv 创建独立的监听器
    private class lvSelectSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 只处理 lv 的选择
            if (position >= 0 && position < lv_array.length) {
                lvText = lv_array[position];
            } else {
                lvText = "一级"; // 默认值
            }

            // 获取当前 type 的选中项
            int typePosition = kemu_type.getSelectedItemPosition();
            if (typePosition >= 0 && typePosition < type_array.length) {
                typeText = type_array[typePosition];
            } else {
                typeText = "资产类"; // 默认值
            }

            Log.d("LvSelect", "选择类型: " + typeText + ", 级别: " + lvText);

            // 加载科目名称数据
            loadKemuNames(typeText, lvText);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


    private class nameSelectSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (position > 0 && name_array != null && position < name_array.length) {
                    nameText = kemu_name.getItemAtPosition(position).toString();

                    if (list != null && !list.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            if (nameText.equals(list.get(i).getName())) {
                                code.setText(list.get(i).getCode());
                                break;
                            }
                        }
                    }
                } else {
                    // 选择空项时清空代码
                    code.setText("");
                }
            } catch (Exception e) {
                Log.e("NameSelect", "选择科目异常: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    public void clearClick(View v) {
        no.setText("");
        voucherDate.setText("");
        zhaiyao.setText("");
        code.setText("");
        money.setText("");
        expenditure.setText("");
        note.setText("");
        real.setText("");
    }

    public void init1() {
//
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                SpinnerAdapter adapter_word = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, word_array);
//                word.setAdapter(StringUtils.cast(adapter_word));
//                word.setSelection(getWordPosition(yhFinanceVoucherSummary.getWord()));
//                SpinnerAdapter adapter_department = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, department_array);
//                department.setAdapter(StringUtils.cast(adapter_department));
//                department.setSelection(getDepartmentPosition(yhFinanceVoucherSummary.getDepartment()));
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, type_array);
//                kemu_type.setAdapter(adapter);
//                adapter = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, lv_array);
//                kemu_lv.setAdapter(adapter);
//                // ============ 添加这行代码 ============
//                // 初始化时加载默认科目
//                loadKemuNames("资产类", "一级");
//                // ============ 添加结束 ============
//
//
//                return true;
//            }
//        });
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_word = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, word_array);
                word.setAdapter(StringUtils.cast(adapter_word));
                word.setSelection(getWordPosition(yhFinanceVoucherSummary.getWord()));
                SpinnerAdapter adapter_department = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, department_array);
                department.setAdapter(StringUtils.cast(adapter_department));
                department.setSelection(getDepartmentPosition(yhFinanceVoucherSummary.getDepartment()));

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, type_array);
                kemu_type.setAdapter(adapter);
                adapter = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, lv_array);
                kemu_lv.setAdapter(adapter);

                // ============ 关键修改：根据凭证中的科目代码判断类型和级别 ============
                if (yhFinanceVoucherSummary != null && yhFinanceVoucherSummary.getCode() > 0) {
                    String codeStr = String.valueOf(yhFinanceVoucherSummary.getCode());

                    // 根据代码自动选择类型和级别
                    autoSelectTypeAndLevel(codeStr);
                } else {
                    // 默认加载资产类一级科目
                    loadKemuNames("资产类", "一级");
                }

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
    // 添加这个方法：根据代码自动选择类型和级别
    private void autoSelectTypeAndLevel(String codeStr) {
        if (codeStr == null || codeStr.isEmpty()) {
            loadKemuNames("资产类", "一级");
            return;
        }

        // 根据代码第一位判断类型
        int typeNum = getTypeFromCode(codeStr);
        String typeText = "";
        switch (typeNum) {
            case 1: typeText = "资产类"; break;
            case 2: typeText = "负债类"; break;
            case 3: typeText = "权益类"; break;
            case 4: typeText = "成本类"; break;
            case 5: typeText = "损益类"; break;
            default: typeText = "资产类";
        }

        // 根据代码长度判断级别
        String lvText = "";
        int len = codeStr.length();
        if (len == 4) lvText = "一级";
        else if (len == 6) lvText = "二级";
        else if (len == 8) lvText = "三级";
        else lvText = "一级"; // 默认

        Log.d("AutoSelect", "根据代码 " + codeStr + " 自动选择类型: " + typeText + ", 级别: " + lvText);

        // 设置类型选择器
        for (int i = 0; i < type_array.length; i++) {
            if (typeText.equals(type_array[i])) {
                kemu_type.setSelection(i);
                typeText = type_array[i]; // 更新当前类型
                break;
            }
        }

        // 设置级别选择器
        for (int i = 0; i < lv_array.length; i++) {
            if (lvText.equals(lv_array[i])) {
                kemu_lv.setSelection(i);
                lvText = lv_array[i]; // 更新当前级别
                break;
            }
        }

        // 加载对应类型和级别的科目
        loadKemuNames(typeText, lvText);
    }
    public void init2() {

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter_word = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, word_array);
                word.setAdapter(StringUtils.cast(adapter_word));
                SpinnerAdapter adapter_department = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, department_array);
                department.setAdapter(StringUtils.cast(adapter_department));

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, type_array);
                kemu_type.setAdapter(adapter);
                adapter = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, lv_array);
                kemu_lv.setAdapter(adapter);
                // ============ 添加这行代码 ============
                // 初始化时加载默认科目
                loadKemuNames("资产类", "一级");
                // ============ 添加结束 ============


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

        if (voucherDate.getText().toString().equals("请选择")) {
            ToastUtil.show(VoucherSummaryChangeActivity.this, "请输录入时间");
            return false;
        } else {
            yhFinanceVoucherSummary.setInsert_date(CovertStrTODate(voucherDate.getText().toString()));

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
        String selectedName = kemu_name.getSelectedItem().toString();
        if (!selectedName.equals("") && !selectedName.equals("请选择")) {
            yhFinanceVoucherSummary.setName(selectedName);
        } else {
            // 如果科目名称为空，可以尝试从列表中查找
            if (list != null && !list.isEmpty() && !code.getText().toString().equals("")) {
                for (int i = 0; i < list.size(); i++) {
                    if (code.getText().toString().equals(String.valueOf(list.get(i).getCode()))) {
                        yhFinanceVoucherSummary.setName(list.get(i).getName());
                        break;
                    }
                }
            }
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
            // yhFinanceVoucherSummary.setMoney(B4);
            yhFinanceVoucherSummary.setReal(B4);
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

    private void intiTimeDialog(Type claa) {
        pickerdialog = new TimePickerDialog.Builder()
                //设置类型
                .setType(claa)
                //设置选择时间监听回调
                .setCallBack(this)
                //设置标题
                .setTitleStringId("请选择时间")
                //设置时间
                //设置颜色
                .setThemeColor(getResources().getColor(R.color.button))
                //设置 字体大小
                .setWheelItemTextSize(15)
                //完毕
                .build();
        pickerdialog.show(getSupportFragmentManager(), "abc");
    }

    public void thisOnClick(View v) {
        intiTimeDialog(Type.ALL);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
//        Toast.makeText(this, "你选择的时间:"+getDateToString(millseconds), Toast.LENGTH_SHORT).show();
        if (date_name.equals("voucherDate")) {
            voucherDate.setText(getDateToString(millseconds));
        }
    }

    //Android时间选择器，支持年月日时分，年月日，年月，月日时分，时分格式，可以设置最小时间（精确到分）
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voucherDate:
                date_name = "voucherDate";
                intiTimeDialog(Type.ALL);
                break;
        }
    }

    public static Timestamp CovertStrTODate(String str) {
        Timestamp ts = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setLenient(false);
        try {
            ts = new Timestamp(format.parse(str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return ts;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


    // ============ 添加这些辅助方法 ============
// 辅助方法：根据类型文字获取数字
    private int getTypeNumber(String type) {
        if (type == null) return 1;
        switch (type) {
            case "资产类":
                return 1;
            case "负债类":
                return 2;
            case "权益类":
                return 3;
            case "成本类":
                return 4;
            case "损益类":
                return 5;
            default:
                return 1;
        }
    }

    // 辅助方法：根据级别获取代码长度
    private int getCodeLength(String lv) {
        if (lv == null) return 4;
        switch (lv) {
            case "一级":
                return 4;
            case "二级":
                return 6;
            case "三级":
                return 8;
            default:
                return 4;
        }
    }

    private final Object loadKemuLock = new Object();

    // 提取加载科目的方法
//    private void loadKemuNames(String type, String lv) {
//        synchronized (loadKemuLock) {
//            if (isLoadingKemu) {
//                Log.d("LoadKemuNames", "正在加载中，跳过重复调用");
//                return;
//            }
//
//            if (type == null || type.trim().isEmpty() || lv == null || lv.trim().isEmpty()) {
//                Log.w("LoadKemuNames", "类型或级别为空，跳过加载");
//                return;
//            }
//
//            isLoadingKemu = true;
//        if (type == null || type.trim().isEmpty() || lv == null || lv.trim().isEmpty()) {
//            Log.w("LoadKemuNames", "类型或级别为空，跳过加载");
//            return;
//        }
//
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                if (name_array != null && name_array.length > 0) {
//                    SpinnerAdapter adapter_name = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this,
//                            android.R.layout.simple_spinner_dropdown_item, name_array);
//                    kemu_name.setAdapter(StringUtils.cast(adapter_name));
//                } else {
//                    // 设置空适配器
//                    SpinnerAdapter adapter_name = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this,
//                            android.R.layout.simple_spinner_dropdown_item, new String[]{""});
//                    kemu_name.setAdapter(StringUtils.cast(adapter_name));
//                }
//                return true;
//            }
//        });
//
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                int typeNum = getTypeNumber(type);
////                int codeLength = getCodeLength(lv);
////
////                Log.d("LoadKemuNames", "开始加载科目，类型: " + type + "(" + typeNum + "), 级别: " + lv + "(" + codeLength + ")");
////
////                try {
////                    // ============ 修改这里：简化处理 ============
////                    // 创建新的服务实例
////                    YhFinanceKeMuZongZhangService service = new YhFinanceKeMuZongZhangService();
////
////                    try {
////                        list = service.getCodeList(yhFinanceUser.getCompany(),
////                                String.valueOf(typeNum), codeLength);
////                    } catch (Exception e) {
////                        Log.e("LoadKemuNames", "查询数据库异常: " + e.getMessage());
////                        e.printStackTrace();
////                        list = null; // 设置为null，避免空指针
////                    }
////
////                    if (list != null && !list.isEmpty()) {
////                        name_array = new String[list.size() + 1];
////                        name_array[0] = "";
////                        for (int i = 0; i < list.size(); i++) {
////                            name_array[i + 1] = list.get(i).getName();
////                        }
////                        Log.d("LoadKemuNames", "加载到 " + list.size() + " 个科目");
////                    } else {
////                        name_array = new String[]{""}; // 空数组
////                        Log.d("LoadKemuNames", "未查询到科目数据");
////                    }
////
////                    listLoadHandler.sendEmptyMessage(0);
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    Log.e("LoadKemuNames", "加载科目异常: " + e.getMessage());
////                    name_array = new String[]{""};
////                    listLoadHandler.sendEmptyMessage(0);
////                }
////            }
////        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    int typeNum = getTypeNumber(type);
//                    int codeLength = getCodeLength(lv);
//
//                    Log.d("LoadKemuNames", "开始加载科目，类型: " + type + "(" + typeNum + "), 级别: " + lv + "(" + codeLength + ")");
//
//                    yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
//                    list = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),
//                            String.valueOf(typeNum), codeLength);
//
//                    if (list != null && !list.isEmpty()) {
//                        name_array = new String[list.size() + 1];
//                        name_array[0] = "";
//                        for (int i = 0; i < list.size(); i++) {
//                            name_array[i + 1] = list.get(i).getName();
//                        }
//                        Log.d("LoadKemuNames", "加载到 " + list.size() + " 个科目");
//
//                        // ============ 新增：如果是编辑模式，自动选择对应的科目 ============
//                        if (yhFinanceVoucherSummary != null && yhFinanceVoucherSummary.getCode() > 0) {
//                            // 在UI线程中设置选择
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    selectKemuByCode(yhFinanceVoucherSummary.getCode());
//                                }
//                            });
//                        }
//                    } else {
//                        name_array = new String[]{""};
//                        Log.d("LoadKemuNames", "未查询到科目数据");
//                    }
//
//                    listLoadHandler.sendEmptyMessage(0);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("LoadKemuNames", "加载科目异常: " + e.getMessage());
//                    name_array = new String[]{""};
//                    listLoadHandler.sendEmptyMessage(0);
//                    isLoadingKemu = false;
//                }
//            }
//        }).start();
//    }
//
//}
    private void loadKemuNames(String type, String lv) {
        synchronized (loadKemuLock) {
            if (isLoadingKemu) {
                Log.d("LoadKemuNames", "正在加载中，跳过重复调用");
                return;
            }

            if (type == null || type.trim().isEmpty() || lv == null || lv.trim().isEmpty()) {
                Log.w("LoadKemuNames", "类型或级别为空，跳过加载");
                return;
            }

            isLoadingKemu = true;
        }

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (name_array != null && name_array.length > 0) {
                    SpinnerAdapter adapter_name = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, name_array);
                    kemu_name.setAdapter(StringUtils.cast(adapter_name));

                    // ============ 关键修改：科目列表加载完成后自动选择 ============
                    if (needAutoSelectKemu && autoSelectCode > 0) {
                        selectKemuByCode(autoSelectCode);
                        needAutoSelectKemu = false; // 重置标志
                    }
                } else {
                    SpinnerAdapter adapter_name = new ArrayAdapter<String>(VoucherSummaryChangeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, new String[]{""});
                    kemu_name.setAdapter(StringUtils.cast(adapter_name));
                }

                // 重置加载状态
                synchronized (loadKemuLock) {
                    isLoadingKemu = false;
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int typeNum = getTypeNumber(type);
                    int codeLength = getCodeLength(lv);

                    Log.d("LoadKemuNames", "开始加载科目，类型: " + type + "(" + typeNum + "), 级别: " + lv + "(" + codeLength + ")");

                    yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                    list = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),
                            String.valueOf(typeNum), codeLength);

                    if (list != null && !list.isEmpty()) {
                        name_array = new String[list.size() + 1];
                        name_array[0] = "";
                        for (int i = 0; i < list.size(); i++) {
                            name_array[i + 1] = list.get(i).getName();
                        }
                        Log.d("LoadKemuNames", "加载到 " + list.size() + " 个科目");
                    } else {
                        name_array = new String[]{""};
                        Log.d("LoadKemuNames", "未查询到科目数据");
                    }

                    listLoadHandler.sendEmptyMessage(0);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LoadKemuNames", "加载科目异常: " + e.getMessage());
                    name_array = new String[]{""};
                    listLoadHandler.sendEmptyMessage(0);

                    // 异常时重置状态
                    synchronized (loadKemuLock) {
                        isLoadingKemu = false;
                    }
                }
            }
        }).start();
    }
    // 根据代码自动选择对应的科目
    // 修改 selectKemuByCode 方法
//    private void selectKemuByCode(int code) {
//        if (list == null || list.isEmpty()) {
//            return;
//        }
//
//        String codeStr = String.valueOf(code);
//        String codeName = null;
//        int codeType = 0;
//        String codeLv = "";
//
//        // 查找对应的科目信息
//        for (YhFinanceKeMuZongZhang item : list) {
//            if (codeStr.equals(item.getCode())) {
//                codeName = item.getName();
//
//                // ============ 根据代码第一位判断类型 ============
//                codeType = getTypeFromCode(codeStr);
//
//                // 根据代码长度判断等级
//                int len = codeStr.length();
//                if (len == 4) codeLv = "一级";
//                else if (len == 6) codeLv = "二级";
//                else if (len == 8) codeLv = "三级";
//                else {
//                    // 尝试从grade字段获取等级
//                    String grade = item.getGrade();
//                    if ("1".equals(grade)) codeLv = "一级";
//                    else if ("2".equals(grade)) codeLv = "二级";
//                    else if ("3".equals(grade)) codeLv = "三级";
//                }
//                break;
//            }
//        }
//
//        if (codeName != null) {
//            // 设置科目名称
//            for (int i = 0; i < name_array.length; i++) {
//                if (codeName.equals(name_array[i])) {
//                    kemu_name.setSelection(i);
//                    break;
//                }
//            }
//
//            // 设置科目类型
//            String typeText = "";
//            switch (codeType) {
//                case 1: typeText = "资产类"; break;
//                case 2: typeText = "负债类"; break;
//                case 3: typeText = "权益类"; break;
//                case 4: typeText = "成本类"; break;
//                case 5: typeText = "损益类"; break;
//            }
//            if (!typeText.isEmpty()) {
//                for (int i = 0; i < type_array.length; i++) {
//                    if (typeText.equals(type_array[i])) {
//                        kemu_type.setSelection(i);
//                        break;
//                    }
//                }
//            }
//
//            // 设置科目等级
//            if (!codeLv.isEmpty()) {
//                for (int i = 0; i < lv_array.length; i++) {
//                    if (codeLv.equals(lv_array[i])) {
//                        kemu_lv.setSelection(i);
//                        break;
//                    }
//                }
//            }
//        }
//    }
    private void selectKemuByCode(int code) {
        if (list == null || list.isEmpty()) {
            Log.d("SelectKemu", "科目列表为空，无法选择");
            return;
        }

        String codeStr = String.valueOf(code);
        Log.d("SelectKemu", "尝试选择科目，代码: " + codeStr);

        String codeName = null;
        int codeType = 0;
        String codeLv = "";

        // 查找对应的科目信息
        for (YhFinanceKeMuZongZhang item : list) {
            if (codeStr.equals(item.getCode())) {
                codeName = item.getName();
                Log.d("SelectKemu", "找到科目: " + codeName);

                // 根据代码第一位判断类型
                codeType = getTypeFromCode(codeStr);
                Log.d("SelectKemu", "推断类型代码: " + codeType);

                // 根据代码长度判断等级
                int len = codeStr.length();
                if (len == 4) codeLv = "一级";
                else if (len == 6) codeLv = "二级";
                else if (len == 8) codeLv = "三级";
                else {
                    // 尝试从grade字段获取等级
                    String grade = item.getGrade();
                    if ("1".equals(grade)) codeLv = "一级";
                    else if ("2".equals(grade)) codeLv = "二级";
                    else if ("3".equals(grade)) codeLv = "三级";
                    else codeLv = "一级"; // 默认
                }
                Log.d("SelectKemu", "推断等级: " + codeLv);
                break;
            }
        }

        if (codeName != null) {
            // 设置科目名称
            boolean nameFound = false;
            for (int i = 0; i < name_array.length; i++) {
                if (codeName.equals(name_array[i])) {
                    kemu_name.setSelection(i);
                    nameFound = true;
                    Log.d("SelectKemu", "设置科目名称成功，位置: " + i);
                    break;
                }
            }

            if (!nameFound) {
                Log.d("SelectKemu", "未在列表中找到科目名称: " + codeName);
            }

            // 设置科目类型
            String typeText = "";
            switch (codeType) {
                case 1: typeText = "资产类"; break;
                case 2: typeText = "负债类"; break;
                case 3: typeText = "权益类"; break;
                case 4: typeText = "成本类"; break;
                case 5: typeText = "损益类"; break;
            }

            if (!typeText.isEmpty()) {
                boolean typeFound = false;
                for (int i = 0; i < type_array.length; i++) {
                    if (typeText.equals(type_array[i])) {
                        kemu_type.setSelection(i);
                        typeFound = true;
                        Log.d("SelectKemu", "设置科目类型成功: " + typeText);
                        break;
                    }
                }

                if (!typeFound) {
                    Log.d("SelectKemu", "未找到科目类型: " + typeText);
                }
            }

            // 设置科目等级
            if (!codeLv.isEmpty()) {
                boolean lvFound = false;
                for (int i = 0; i < lv_array.length; i++) {
                    if (codeLv.equals(lv_array[i])) {
                        kemu_lv.setSelection(i);
                        lvFound = true;
                        Log.d("SelectKemu", "设置科目等级成功: " + codeLv);
                        break;
                    }
                }

                if (!lvFound) {
                    Log.d("SelectKemu", "未找到科目等级: " + codeLv);
                }
            }
        } else {
            Log.d("SelectKemu", "未找到代码为 " + codeStr + " 的科目");
        }
    }
    // 根据代码第一位判断类型
    private int getTypeFromCode(String code) {
        if (code == null || code.isEmpty()) return 1;

        try {
            char firstChar = code.charAt(0);
            switch (firstChar) {
                case '1': return 1; // 资产类
                case '2': return 2; // 负债类
                case '3': return 3; // 权益类
                case '4': return 4; // 成本类
                case '5': return 5; // 损益类
                default: return 1;  // 默认资产类
            }
        } catch (Exception e) {
            return 1;
        }
    }
}
