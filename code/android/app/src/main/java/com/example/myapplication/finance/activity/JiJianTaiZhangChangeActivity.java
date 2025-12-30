package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceShuiLv;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceWaiBi;
import com.example.myapplication.finance.entity.YhFinanceXiangMu;
import com.example.myapplication.finance.service.YhFinanceJiJianTaiZhangService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceShuiLvService;
import com.example.myapplication.finance.service.YhFinanceSimpleAccountingService;
import com.example.myapplication.finance.service.YhFinanceWaiBiService;
import com.example.myapplication.finance.service.YhFinanceXiangMuService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class JiJianTaiZhangChangeActivity extends AppCompatActivity implements OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private YhFinanceUser yhFinanceUser;
    private YhFinanceJiJianTaiZhang yhFinanceJiJianTaiZhang;
    private YhFinanceJiJianTaiZhangService yhFinanceJiJianTaiZhangService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;
    private YhFinanceSimpleAccountingService yhFinanceSimpleAccountingService;
    private YhFinanceWaiBiService yhFinanceWaiBiService;
    private YhFinanceXiangMuService yhFinanceXiangMuService;
    private YhFinanceShuiLvService yhFinanceShuiLvService;
    private TextView insert_date;
    private Spinner accounting;
    private EditText project;
    private Spinner kehu;
    private EditText receivable;
    private EditText receipts;
    private EditText cope;
    private EditText payment;
    private EditText nashuijine;
    private EditText yijiaoshuijine;
    private EditText zhaiyao;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TimePickerDialog pickerdialog;
    private String date_name;
    private Spinner bizhong;  // 添加币种Spinner变量
    private List<YhFinanceWaiBi> waiBiList; // 币种列表（包含汇率）
    private BigDecimal currentHuilv = BigDecimal.ONE; // 当前汇率，默认1
    private boolean isConverting = false; // 防止递归调用
    String[] bizhong_array;  // 币种数据数组
    String[] project_array;
    String[] kehu_array;
    // 添加延迟处理变量
    private Handler inputHandler = new Handler();
    private Runnable inputRunnable;
    private static final int INPUT_DELAY = 500; // 500毫秒延迟

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
        yhFinanceWaiBiService = new YhFinanceWaiBiService();  // 假设有这个Service
        yhFinanceXiangMuService = new YhFinanceXiangMuService();
        yhFinanceShuiLvService = new YhFinanceShuiLvService();
        insert_date = findViewById(R.id.insert_date);
        accounting = findViewById(R.id.accounting);
        project = findViewById(R.id.project);
        kehu = findViewById(R.id.kehu);
        bizhong = findViewById(R.id.bizhong);
        receivable = findViewById(R.id.receivable);
        receipts = findViewById(R.id.receipts);
        cope = findViewById(R.id.cope);
        payment = findViewById(R.id.payment);
        nashuijine = findViewById(R.id.nashuijine);
        yijiaoshuijine = findViewById(R.id.yijiaoshuijine);
        zhaiyao = findViewById(R.id.zhaiyao);

        insert_date.setOnClickListener(this);

        // 设置币种选择监听
        bizhong.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhFinanceJiJianTaiZhang = (YhFinanceJiJianTaiZhang) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(yhFinanceJiJianTaiZhang.getInsert_date().getTime());
            String java_date = sdf.format(date);
            insert_date.setText(java_date);
            project.setText(yhFinanceJiJianTaiZhang.getProject());

            receivable.setText(yhFinanceJiJianTaiZhang.getReceivable().toString());
            receipts.setText(yhFinanceJiJianTaiZhang.getReceipts().toString());
            cope.setText(yhFinanceJiJianTaiZhang.getCope().toString());
            payment.setText(yhFinanceJiJianTaiZhang.getPayment().toString());
            // 更简洁的写法
            nashuijine.setText(yhFinanceJiJianTaiZhang.getNashuijine() != null ?
                    yhFinanceJiJianTaiZhang.getNashuijine().toString() : "");

            yijiaoshuijine.setText(yhFinanceJiJianTaiZhang.getYijiaoshuijine() != null ?
                    yhFinanceJiJianTaiZhang.getYijiaoshuijine().toString() : "");
            zhaiyao.setText(yhFinanceJiJianTaiZhang.getZhaiyao());
        }else{
            yhFinanceJiJianTaiZhang = new YhFinanceJiJianTaiZhang();
            init2();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }

        // 为输入框添加文本变化监听，用于输入时自动乘以汇率
        setupAmountListeners();
    }

    // 为金额输入框设置文本变化监听（添加延迟）
    private void setupAmountListeners() {
        // 为每个输入框创建独立的TextWatcher
        setupSingleAmountListener(receivable);
        setupSingleAmountListener(receipts);
        setupSingleAmountListener(cope);
        setupSingleAmountListener(payment);
        setupSingleAmountListener(nashuijine);
        setupSingleAmountListener(yijiaoshuijine);
    }

    // 为单个输入框设置监听
    private void setupSingleAmountListener(final EditText editText) {
        TextWatcher amountWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 取消之前的延迟任务
                if (inputRunnable != null) {
                    inputHandler.removeCallbacks(inputRunnable);
                }

                // 如果正在转换或者选择了"请选择币种"，则不处理
                if (isConverting || "请选择币种".equals(bizhong.getSelectedItem().toString())) {
                    return;
                }

                // 如果输入为空或者汇率是1，不处理
                if (s.length() == 0 || currentHuilv.compareTo(BigDecimal.ONE) == 0) {
                    return;
                }

                // 设置新的延迟任务
                inputRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 处理当前输入框的转换
                        processAmountConversion(editText, s.toString());
                    }
                };

                // 延迟执行
                inputHandler.postDelayed(inputRunnable, INPUT_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editText.addTextChangedListener(amountWatcher);
    }


    // 处理金额转换
    private void processAmountConversion(EditText editText, String inputStr) {
        if (inputStr.isEmpty() || isConverting) return;

        try {
            BigDecimal inputValue = new BigDecimal(inputStr);

            // 检查是否为纳税金额输入框
            boolean isTaxField = editText.getId() == R.id.nashuijine ||
                    editText.getId() == R.id.yijiaoshuijine;

            // 对于纳税金额输入框，仍然需要乘以汇率
            if (isTaxField) {
                // 计算乘以汇率后的值
                BigDecimal convertedValue = inputValue.multiply(currentHuilv)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);

                // 更新输入框
                isConverting = true;
                editText.setText(convertedValue.toString());
                editText.setSelection(editText.getText().length());
                isConverting = false;
            } else {
                // 其他字段正常处理
                BigDecimal convertedValue = inputValue.multiply(currentHuilv)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);

                isConverting = true;
                editText.setText(convertedValue.toString());
                editText.setSelection(editText.getText().length());
                isConverting = false;
            }
        } catch (NumberFormatException e) {
            // 输入不是有效数字，不处理
            e.printStackTrace();
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
        insert_date.setText("请选择");
        project.setText("");
        receivable.setText("");
        receipts.setText("");
        cope.setText("");
        payment.setText("");
        nashuijine.setText("");
        yijiaoshuijine.setText("");
        zhaiyao.setText("");
        if (bizhong.getAdapter() != null && bizhong.getAdapter().getCount() > 0) {
            bizhong.setSelection(0); // 选择"请选择币种"
        }
        // 重置汇率
        currentHuilv = BigDecimal.ONE;
    }


    public void init1() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // 科目适配器
                SpinnerAdapter adapter_project = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, project_array);
                accounting.setAdapter(StringUtils.cast(adapter_project));
                accounting.setSelection(getAccountingPosition(yhFinanceJiJianTaiZhang.getAccounting()));

                // 客户适配器
                SpinnerAdapter adapter_kehu = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, kehu_array);
                kehu.setAdapter(StringUtils.cast(adapter_kehu));
                kehu.setSelection(getKehuPosition(yhFinanceJiJianTaiZhang.getKehu()));

                // 币种适配器
                if (bizhong_array != null && bizhong_array.length > 0) {
                    // 有数据：创建包含"请选择币种"的数组
                    String[] bizhongWithDefault = new String[bizhong_array.length + 1];
                    bizhongWithDefault[0] = "请选择币种";
                    System.arraycopy(bizhong_array, 0, bizhongWithDefault, 1, bizhong_array.length);

                    SpinnerAdapter adapter_bizhong = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, bizhongWithDefault);
                    bizhong.setAdapter(StringUtils.cast(adapter_bizhong));

                    // 如果有保存的币种，设置选中
                    if (yhFinanceJiJianTaiZhang.getBizhong() != null &&
                            !yhFinanceJiJianTaiZhang.getBizhong().isEmpty()) {
                        int position = getBizhongPosition(yhFinanceJiJianTaiZhang.getBizhong());
                        if (position >= 0) {
                            bizhong.setSelection(position);
                        }
                    }
                } else {
                    // 没有数据：只有"请选择币种"
                    String[] defaultBizhong = {"请选择币种"};
                    SpinnerAdapter adapter_bizhong = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, defaultBizhong);
                    bizhong.setAdapter(StringUtils.cast(adapter_bizhong));
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 加载科目数据
                    List<YhFinanceJiJianPeiZhi> accountingList = yhFinanceSimpleAccountingService.getList(yhFinanceUser.getCompany());
                    if (accountingList.size() > 0) {
                        project_array = new String[accountingList.size()];
                        for (int i = 0; i < accountingList.size(); i++) {
                            project_array[i] = accountingList.get(i).getPeizhi();
                        }
                    } else {
                        project_array = new String[0];
                    }

                    // 加载客户数据
                    List<YhFinanceJiJianPeiZhi> kehuList = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        kehu_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array[i] = kehuList.get(i).getPeizhi();
                        }
                    } else {
                        kehu_array = new String[0];
                    }

                    // 加载币种数据
                    waiBiList = yhFinanceWaiBiService.getList(yhFinanceUser.getCompany());
                    if (waiBiList != null && waiBiList.size() > 0) {
                        bizhong_array = new String[waiBiList.size()];
                        for (int i = 0; i < waiBiList.size(); i++) {
                            bizhong_array[i] = waiBiList.get(i).getBizhong();
                        }
                    } else {
                        // 没有数据，设为空数组
                        bizhong_array = new String[0];
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // 异常时设为空数组
                    bizhong_array = new String[0];
                }
                listLoadHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    public void init2() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // 科目适配器
                SpinnerAdapter adapter_project = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, project_array);
                accounting.setAdapter(StringUtils.cast(adapter_project));

                // 客户适配器
                SpinnerAdapter adapter_kehu = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, kehu_array);
                kehu.setAdapter(StringUtils.cast(adapter_kehu));

                // 币种适配器 - 包含"请选择币种"选项
                if (bizhong_array != null && bizhong_array.length > 0) {
                    // 创建包含"请选择币种"的数组
                    String[] bizhongWithDefault = new String[bizhong_array.length + 1];
                    bizhongWithDefault[0] = "请选择币种";
                    System.arraycopy(bizhong_array, 0, bizhongWithDefault, 1, bizhong_array.length);

                    SpinnerAdapter adapter_bizhong = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, bizhongWithDefault);
                    bizhong.setAdapter(StringUtils.cast(adapter_bizhong));
                    bizhong.setSelection(0); // 默认为"请选择币种"
                } else {
                    // 如果没有数据，只有"请选择币种"选项
                    String[] defaultBizhong = {"请选择币种"};
                    SpinnerAdapter adapter_bizhong = new ArrayAdapter<String>(JiJianTaiZhangChangeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, defaultBizhong);
                    bizhong.setAdapter(StringUtils.cast(adapter_bizhong));
                    bizhong.setSelection(0);
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 加载科目数据
                    List<YhFinanceJiJianPeiZhi> accountingList = yhFinanceSimpleAccountingService.getList(yhFinanceUser.getCompany());
                    if (accountingList.size() > 0) {
                        project_array = new String[accountingList.size()];
                        for (int i = 0; i < accountingList.size(); i++) {
                            project_array[i] = accountingList.get(i).getPeizhi();
                        }
                    } else {
                        project_array = new String[0];
                    }

                    // 加载客户数据
                    List<YhFinanceJiJianPeiZhi> kehuList = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        kehu_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array[i] = kehuList.get(i).getPeizhi();
                        }
                    } else {
                        kehu_array = new String[0];
                    }

                    // 加载币种数据
                    waiBiList = yhFinanceWaiBiService.getList(yhFinanceUser.getCompany());
                    if (waiBiList != null && waiBiList.size() > 0) {
                        bizhong_array = new String[waiBiList.size()];
                        for (int i = 0; i < waiBiList.size(); i++) {
                            bizhong_array[i] = waiBiList.get(i).getBizhong();
                        }
                    } else {
                        // 如果没有数据，设为空数组
                        bizhong_array = new String[0];
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // 异常时设置默认值
                    bizhong_array = new String[0];
                }
                listLoadHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    // 币种选择监听器实现
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.bizhong) {
            String selectedBizhong = (String) parent.getSelectedItem();

            // 如果选择的是"请选择币种"，设置汇率为1
            if ("请选择币种".equals(selectedBizhong)) {
                currentHuilv = BigDecimal.ONE;
                return;
            }

            // 从币种列表中查找对应的汇率
            BigDecimal newHuilv = BigDecimal.ONE;
            if (waiBiList != null) {
                for (YhFinanceWaiBi waiBi : waiBiList) {
                    if (selectedBizhong.equals(waiBi.getBizhong())) {
                        try {
                            newHuilv = new BigDecimal(waiBi.getHuilv());
                        } catch (Exception e) {
                            newHuilv = BigDecimal.ONE;
                        }
                        break;
                    }
                }
            }

            // 如果汇率有变化，将现有金额乘以新汇率
            if (!newHuilv.equals(currentHuilv) && newHuilv.compareTo(BigDecimal.ONE) != 0) {
                isConverting = true;
                try {
                    // 转换应收
                    convertAmount(receivable, newHuilv);

                    // 转换实收
                    convertAmount(receipts, newHuilv);

                    // 转换应付
                    convertAmount(cope, newHuilv);

                    // 转换实付
                    convertAmount(payment, newHuilv);

                    // 转换纳税金额
                    convertAmount(nashuijine, newHuilv);

                    // 转换已交税金额
                    convertAmount(yijiaoshuijine, newHuilv);
                } finally {
                    isConverting = false;
                }
            }

            // 更新当前汇率
            currentHuilv = newHuilv;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // 不处理
    }

    // 转换单个金额字段
    private void convertAmount(EditText editText, BigDecimal newHuilv) {
        String text = editText.getText().toString().trim();
        if (!text.isEmpty()) {
            try {
                BigDecimal amount = new BigDecimal(text);

                // 检查是否为纳税金额字段
                boolean isTaxField = editText.getId() == R.id.nashuijine ||
                        editText.getId() == R.id.yijiaoshuijine;

                BigDecimal convertedAmount;

                if (isTaxField) {
                    // 对于纳税金额：将当前显示金额转换为原始金额，再乘以新汇率
                    if (currentHuilv.compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal originalAmount = amount.divide(currentHuilv, 10, BigDecimal.ROUND_HALF_UP);
                        convertedAmount = originalAmount.multiply(newHuilv)
                                .setScale(2, BigDecimal.ROUND_HALF_UP);
                    } else {
                        convertedAmount = amount.multiply(newHuilv)
                                .setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                } else {
                    // 对于其他金额字段：直接乘以新汇率
                    convertedAmount = amount.multiply(newHuilv)
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                isConverting = true;
                editText.setText(convertedAmount.toString());
                isConverting = false;
            } catch (NumberFormatException e) {
                // 如果不是有效数字，不处理
                e.printStackTrace();
            }
        }
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


    public void yingshoujisuan(View view) {
        String projectName = project.getText().toString().trim();

        if (projectName.isEmpty()) {
            ToastUtil.show(this, "请输入项目名称");
            return;
        }

        ToastUtil.show(this, "正在计算应收...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 直接查询应收数据
                    List<YhFinanceXiangMu> yingshouList = yhFinanceXiangMuService.getYingshouList(
                            yhFinanceUser.getCompany(),
                            projectName
                    );

                    BigDecimal totalAmount = BigDecimal.ZERO;

                    if (yingshouList != null && !yingshouList.isEmpty()) {
                        for (YhFinanceXiangMu item : yingshouList) {
                            String jineStr = item.getJine();
                            if (jineStr != null && !jineStr.trim().isEmpty()) {
                                try {
                                    // 处理逗号分隔的数字
                                    String[] numbers = jineStr.split(",");
                                    for (String numStr : numbers) {
                                        if (!numStr.trim().isEmpty()) {
                                            BigDecimal amount = new BigDecimal(numStr.trim());
                                            totalAmount = totalAmount.add(amount);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("处理金额失败：" + jineStr);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    final BigDecimal finalTotal = totalAmount;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalTotal.compareTo(BigDecimal.ZERO) > 0) {
                                // 计算显示金额（原始金额 × 当前汇率）
                                BigDecimal displayAmount = finalTotal;
                                if (currentHuilv.compareTo(BigDecimal.ONE) != 0 &&
                                        !"请选择币种".equals(bizhong.getSelectedItem().toString())) {
                                    displayAmount = finalTotal.multiply(currentHuilv)
                                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                                }

                                // 只更新应收输入框，加上isConverting保护
                                isConverting = true;
                                receivable.setText(displayAmount.toString());
                                isConverting = false;

                                ToastUtil.show(JiJianTaiZhangChangeActivity.this,
                                        "计算完成，应收金额：" + displayAmount.toString());
                            } else {
                                isConverting = true;
                                receivable.setText("");
                                isConverting = false;
                                ToastUtil.show(JiJianTaiZhangChangeActivity.this,
                                        "未找到符合条件的应收数据");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(JiJianTaiZhangChangeActivity.this,
                                    "计算失败：" + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }


    public void yingfujisuan(View view) {
        String projectName = project.getText().toString().trim();

        if (projectName.isEmpty()) {
            ToastUtil.show(this, "请输入项目名称");
            return;
        }

        ToastUtil.show(this, "正在计算应付...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 直接查询应付数据
                    List<YhFinanceXiangMu> yingfuList = yhFinanceXiangMuService.getYingfuList(
                            yhFinanceUser.getCompany(),
                            projectName
                    );

                    BigDecimal totalAmount = BigDecimal.ZERO;

                    if (yingfuList != null && !yingfuList.isEmpty()) {
                        for (YhFinanceXiangMu item : yingfuList) {
                            String jineStr = item.getJine();
                            if (jineStr != null && !jineStr.trim().isEmpty()) {
                                try {
                                    // 处理逗号分隔的数字
                                    String[] numbers = jineStr.split(",");
                                    for (String numStr : numbers) {
                                        if (!numStr.trim().isEmpty()) {
                                            BigDecimal amount = new BigDecimal(numStr.trim());
                                            totalAmount = totalAmount.add(amount);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("处理金额失败：" + jineStr);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    final BigDecimal finalTotal = totalAmount;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalTotal.compareTo(BigDecimal.ZERO) > 0) {
                                // 计算显示金额（原始金额 × 当前汇率）
                                BigDecimal displayAmount = finalTotal;
                                if (currentHuilv.compareTo(BigDecimal.ONE) != 0 &&
                                        !"请选择币种".equals(bizhong.getSelectedItem().toString())) {
                                    displayAmount = finalTotal.multiply(currentHuilv)
                                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                                }

                                // 只更新应付输入框，加上isConverting保护
                                isConverting = true;
                                cope.setText(displayAmount.toString());
                                isConverting = false;

                                ToastUtil.show(JiJianTaiZhangChangeActivity.this,
                                        "计算完成，应付金额：" + displayAmount.toString());
                            } else {
                                isConverting = true;
                                cope.setText("");
                                isConverting = false;
                                ToastUtil.show(JiJianTaiZhangChangeActivity.this,
                                        "未找到符合条件的应付数据");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(JiJianTaiZhangChangeActivity.this,
                                    "计算失败：" + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        // 在调用 insert 前设置公司名称
        String companyName = getCompanyNameFromCache();
        if (!companyName.isEmpty()) {
            yhFinanceJiJianTaiZhang.setCompany(companyName);
        }

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

    // 获取公司名称的方法
    private String getCompanyNameFromCache() {
        try {
            SharedPreferences sharedPref = getSharedPreferences("my_cache", Context.MODE_PRIVATE);
            return sharedPref.getString("companyName", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private boolean checkForm() throws ParseException {
        if (insert_date.getText().toString().equals("请选择")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入日期");
            return false;
        } else {
            Timestamp startTime = CovertStrTODate(insert_date.getText().toString());
            yhFinanceJiJianTaiZhang.setInsert_date(CovertStrTODate(insert_date.getText().toString()));
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

        if (nashuijine.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入纳税金额");
            return false;
        } else {
            BigDecimal B5 = new BigDecimal(nashuijine.getText().toString());
            yhFinanceJiJianTaiZhang.setNashuijine(B5);
        }

        if (yijiaoshuijine.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入已交税金额");
            return false;
        } else {
            BigDecimal B6 = new BigDecimal(yijiaoshuijine.getText().toString());
            yhFinanceJiJianTaiZhang.setYijiaoshuijine(B6);
        }

        if (zhaiyao.getText().toString().equals("")) {
            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "请输入摘要");
            return false;
        } else {
            yhFinanceJiJianTaiZhang.setZhaiyao(zhaiyao.getText().toString());
        }

        // 保存币种信息
        String selectedBizhong = bizhong.getSelectedItem().toString();
        if (!"请选择币种".equals(selectedBizhong)) {
            yhFinanceJiJianTaiZhang.setBizhong(selectedBizhong);
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

    private int getBizhongPosition(String param) {
        if (bizhong_array != null && param != null) {
            for (int i = 0; i < bizhong_array.length; i++) {
                if (param.equals(bizhong_array[i])) {
                    return i + 1; // +1是因为有"请选择币种"选项
                }
            }
        }
        return 0;  // 如果没有找到，返回第一个位置（请选择币种）
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
        pickerdialog.show(getSupportFragmentManager(),"abc");
    }

    public void thisOnClick(View v) {
        intiTimeDialog(Type.ALL);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if(date_name.equals("insert_date")){
            insert_date.setText(getDateToString(millseconds));
        }
    }

    //Android时间选择器，支持年月日时分，年月日，年月，月日时分，时分格式，可以设置最小时间（精确到分）
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert_date:
                date_name = "insert_date";
                intiTimeDialog(Type.ALL);
                break;
        }
    }

    public static Timestamp CovertStrTODate(String str) {
        Timestamp ts =null;
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

    // 纳税计算方法
    public void nashuijisuan(View view) {
        // 创建选项对话框
        showCalculationOptionDialog();
    }

    // 显示计算选项对话框
    private void showCalculationOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择计算基础");

        String[] options = {"应收", "实收", "利润（实收-实付）"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 应收
                        calculateTaxBasedOnReceivable();
                        break;
                    case 1: // 实收
                        calculateTaxBasedOnReceipts();
                        break;
                    case 2: // 利润
                        calculateTaxBasedOnProfit();
                        break;
                }
            }
        });

        builder.show();
    }

    // 基于应收计算纳税
    private void calculateTaxBasedOnReceivable() {
        String receivableStr = receivable.getText().toString().trim();
        if (receivableStr.isEmpty()) {
            ToastUtil.show(this, "请输入应收金额");
            return;
        }

        try {
            BigDecimal receivableAmount = new BigDecimal(receivableStr);
            calculateAndDisplayTax(receivableAmount, "应收");
        } catch (Exception e) {
            ToastUtil.show(this, "应收金额格式错误");
            e.printStackTrace();
        }
    }

    // 基于实收计算纳税
    private void calculateTaxBasedOnReceipts() {
        String receiptsStr = receipts.getText().toString().trim();
        if (receiptsStr.isEmpty()) {
            ToastUtil.show(this, "请输入实收金额");
            return;
        }

        try {
            BigDecimal receiptsAmount = new BigDecimal(receiptsStr);
            calculateAndDisplayTax(receiptsAmount, "实收");
        } catch (Exception e) {
            ToastUtil.show(this, "实收金额格式错误");
            e.printStackTrace();
        }
    }

    // 基于利润计算纳税（利润 = 实收 - 实付）
    private void calculateTaxBasedOnProfit() {
        String receiptsStr = receipts.getText().toString().trim();
        String paymentStr = payment.getText().toString().trim();

        if (receiptsStr.isEmpty() || paymentStr.isEmpty()) {
            ToastUtil.show(this, "请输入实收和实付金额");
            return;
        }

        try {
            BigDecimal receiptsAmount = new BigDecimal(receiptsStr);
            BigDecimal paymentAmount = new BigDecimal(paymentStr);
            BigDecimal profit = receiptsAmount.subtract(paymentAmount);

            if (profit.compareTo(BigDecimal.ZERO) < 0) {
                ToastUtil.show(this, "利润为负，无需纳税");
                nashuijine.setText("0");
                return;
            }

            calculateAndDisplayTax(profit, "利润");
        } catch (Exception e) {
            ToastUtil.show(this, "金额格式错误");
            e.printStackTrace();
        }
    }

    private void calculateAndDisplayTax(BigDecimal baseAmount, String baseType) {
        if (baseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            ToastUtil.show(this, baseType + "金额必须大于0");
            isConverting = true;
            nashuijine.setText("0");
            isConverting = false;
            return;
        }

        ToastUtil.show(this, "正在计算纳税金额...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取税率配置
                    List<YhFinanceShuiLv> shuilvList = yhFinanceShuiLvService.getList(yhFinanceUser.getCompany());

                    if (shuilvList == null || shuilvList.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(JiJianTaiZhangChangeActivity.this, "未配置税率信息");
                                isConverting = true;
                                nashuijine.setText("0");
                                isConverting = false;
                            }
                        });
                        return;
                    }

                    // 排序税率配置
                    Collections.sort(shuilvList, new Comparator<YhFinanceShuiLv>() {
                        @Override
                        public int compare(YhFinanceShuiLv o1, YhFinanceShuiLv o2) {
                            try {
                                BigDecimal limit1 = new BigDecimal(o1.getLinjiezhi());
                                BigDecimal limit2 = new BigDecimal(o2.getLinjiezhi());
                                return limit1.compareTo(limit2);
                            } catch (Exception e) {
                                return 0;
                            }
                        }
                    });

                    // 关键修改：直接使用输入的金额进行计算，不考虑汇率
                    BigDecimal taxAmount = calculateStepTax(baseAmount, shuilvList);

                    final BigDecimal finalTaxAmount = taxAmount;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 关键修改：纳税计算结果显示原始值，不乘以汇率
                            // 如果计算得到500就直接显示500
                            BigDecimal displayAmount = finalTaxAmount;

                            // 加上isConverting保护
                            isConverting = true;
                            nashuijine.setText(displayAmount.toString());
                            isConverting = false;

                            ToastUtil.show(JiJianTaiZhangChangeActivity.this,
                                    "纳税计算完成：" + displayAmount.toString() +
                                            "（基于" + baseType + "：" + baseAmount + "）");
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(JiJianTaiZhangChangeActivity.this, "计算失败：" + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    // 修正后的计算方法
    private BigDecimal calculateStepTax(BigDecimal taxableAmount, List<YhFinanceShuiLv> taxRates) {
        if (taxableAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // 排序税率配置
        Collections.sort(taxRates, new Comparator<YhFinanceShuiLv>() {
            @Override
            public int compare(YhFinanceShuiLv o1, YhFinanceShuiLv o2) {
                try {
                    BigDecimal limit1 = new BigDecimal(o1.getLinjiezhi());
                    BigDecimal limit2 = new BigDecimal(o2.getLinjiezhi());
                    return limit1.compareTo(limit2);
                } catch (Exception e) {
                    return 0;
                }
            }
        });

        BigDecimal tax = BigDecimal.ZERO;

        try {
            // 根据您的描述，税率配置应该是：
            // 第一条：临界值500，税率2%  -> 500-2000部分税率
            // 第二条：临界值2000，税率5% -> 2000以上部分税率

            if (taxRates.size() >= 2) {
                YhFinanceShuiLv rate1 = taxRates.get(0); // 500, 2%
                YhFinanceShuiLv rate2 = taxRates.get(1); // 2000, 5%

                BigDecimal limit1 = new BigDecimal(rate1.getLinjiezhi()); // 500
                BigDecimal rate2Percent = new BigDecimal(rate2.getShuilv()); // 5
                BigDecimal limit2 = new BigDecimal(rate2.getLinjiezhi()); // 2000

                // 税率
                BigDecimal rate1Decimal = new BigDecimal(rate1.getShuilv()).divide(new BigDecimal("100")); // 2% -> 0.02
                BigDecimal rate2Decimal = rate2Percent.divide(new BigDecimal("100")); // 5% -> 0.05

                if (taxableAmount.compareTo(limit1) <= 0) {
                    // 小于等于500，不纳税
                    tax = BigDecimal.ZERO;
                } else if (taxableAmount.compareTo(limit2) <= 0) {
                    // 500 < 金额 ≤ 2000
                    // 只对超过500的部分按2%计税
                    BigDecimal exceedAmount = taxableAmount.subtract(limit1);
                    tax = exceedAmount.multiply(rate1Decimal);
                } else {
                    // 金额 > 2000
                    // 500-2000部分：1500 × 2% = 30
                    // 2000以上部分：(金额-2000) × 5%
                    BigDecimal amount1 = limit2.subtract(limit1); // 1500
                    BigDecimal tax1 = amount1.multiply(rate1Decimal); // 30

                    BigDecimal exceedAmount2 = taxableAmount.subtract(limit2);
                    BigDecimal tax2 = exceedAmount2.multiply(rate2Decimal);

                    tax = tax1.add(tax2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tax.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}