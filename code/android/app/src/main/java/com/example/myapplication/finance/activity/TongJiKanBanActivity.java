package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;

import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;

import com.example.myapplication.finance.service.YhFinanceJiJianTaiZhangService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TongJiKanBanActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceJiJianTaiZhangService yhFinanceJiJianTaiZhangService;

    private BarChart barChart1;

    // UI组件
    private TextView jiejin_sum;    // 现在用于显示总利润
    private TextView daijin_sum;    // 现在用于显示总成本
    private TextView jiefang_sum;   // 现在用于显示总收入
    private TextView daifang_sum;   // 现在用于显示总支出

    private List<BarEntry> profitList = new ArrayList<>(); // 利润柱状图数据
    private List<BarEntry> costList = new ArrayList<>();   // 成本柱状图数据
    private List<AccountingModel> accountingList = new ArrayList<>(); // X轴数据

    private float totalProfit = 0f;
    private float totalCost = 0f;
    private float totalReceipts = 0f;  // 总收入
    private float totalPayment = 0f;   // 总支出
    private Button toggleChartButton; // 切换按钮
    private boolean isBarChart = true; // 标记当前显示的是柱状图还是折线图
    List<YhFinanceJiJianTaiZhang> simpleDataList;
    private LineChart lineChart1; // 新增折线图对象
    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        super.onCreate(savedInstanceState);
        // 去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tongjikanban);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 初始化UI组件
        jiejin_sum = findViewById(R.id.jiejin_sum);
        daijin_sum = findViewById(R.id.daijin_sum);
        jiefang_sum = findViewById(R.id.jiefang_sum);
        daifang_sum = findViewById(R.id.daifang_sum);
        barChart1 = findViewById(R.id.bc_1);
        lineChart1 = findViewById(R.id.lc_1);
        // 设置UI文本
        jiejin_sum.setText("总利润：计算中...");
        daijin_sum.setText("总成本：计算中...");
        jiefang_sum.setText("总收入：计算中...");
        daifang_sum.setText("总支出：计算中...");
        toggleChartButton = findViewById(R.id.toggle_chart_button);
        toggleChartButton.setOnClickListener(v -> toggleChart());

        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // 切换图表显示
    private void toggleChart() {
        if (isBarChart) {
            // 显示折线图，隐藏柱状图
            barChart1.setVisibility(View.GONE);
            lineChart1.setVisibility(View.VISIBLE);
            toggleChartButton.setText("切换为柱状图");

            // 设置折线图数据
            setLineChartData();
        } else {
            // 显示柱状图，隐藏折线图
            barChart1.setVisibility(View.VISIBLE);
            lineChart1.setVisibility(View.GONE);
            toggleChartButton.setText("切换为折线图");

            // 重新设置柱状图数据（确保数据最新）
            setChartData();
        }
        isBarChart = !isBarChart;
    }

    public static float toFloat(BigDecimal b) {
        return b != null ? b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() : 0f;
    }

    /**
     * 设置柱状图数据
     */
    /**
     * 设置柱状图数据
     */
    private void setChartData() {
        barChart1 = findViewById(R.id.bc_1);

        // 先清空图表
        barChart1.clear();
        barChart1.fitScreen();

        // 清空数据
        profitList.clear();
        costList.clear();

        totalProfit = 0f;
        totalCost = 0f;
        totalReceipts = 0f;
        totalPayment = 0f;

        // 创建利润和成本数据 - 使用简单的X值
        for (int i = 0; i < accountingList.size(); i++) {
            AccountingModel model = accountingList.get(i);

            // 利润数据（receipts - payment）
            float profitValue = toFloat(model.profit);
            // 利润柱子在每个组的左侧，所以是 i * 2
            profitList.add(new BarEntry(i * 2f, profitValue));

            // 成本数据（payment） - 成本通常不会为负数
            float costValue = toFloat(model.cost);
            // 成本柱子在每个组的右侧，所以是 i * 2 + 1
            costList.add(new BarEntry(i * 2f + 1f, costValue));

            totalProfit += profitValue;
            totalCost += costValue;
            totalReceipts += toFloat(model.totalReceipts);
            totalPayment += toFloat(model.totalPayment);
        }

        // 更新UI显示
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        jiejin_sum.setText("总利润：" + nf.format(totalProfit));
        daijin_sum.setText("总成本：" + nf.format(totalCost));
        jiefang_sum.setText("总收入：" + nf.format(totalReceipts));
        daifang_sum.setText("总支出：" + nf.format(totalPayment));

        // 如果没有数据，显示空图表
        if (accountingList.isEmpty()) {
            barChart1.setNoDataText("暂无数据");
            barChart1.setNoDataTextColor(Color.BLACK);
            barChart1.invalidate();
            return;
        }

        // 创建数据集
        BarDataSet profitDataSet = new BarDataSet(profitList, "利润");

        // 为利润柱子设置颜色：正数为绿色，负数为红色
        // 使用自定义渲染器来设置不同颜色
        profitDataSet.setColor(Color.parseColor("#4CAF50")); // 默认绿色

        // 使用自定义颜色数组，但这样图例可能会显示多个颜色
        // 更好的方法是使用两个数据集：盈利和亏损
        List<BarEntry> positiveProfitList = new ArrayList<>();
        List<BarEntry> negativeProfitList = new ArrayList<>();

        for (int i = 0; i < profitList.size(); i++) {
            BarEntry entry = profitList.get(i);
            if (entry.getY() >= 0) {
                positiveProfitList.add(entry);
            } else {
                negativeProfitList.add(new BarEntry(entry.getX(), entry.getY()));
            }
        }

        // 创建两个利润数据集：盈利和亏损
        BarDataSet positiveProfitDataSet = null;
        BarDataSet negativeProfitDataSet = null;

        if (!positiveProfitList.isEmpty()) {
            positiveProfitDataSet = new BarDataSet(positiveProfitList, "盈利");
            positiveProfitDataSet.setColor(Color.parseColor("#4CAF50")); // 绿色
            positiveProfitDataSet.setValueTextColor(Color.BLACK);
            positiveProfitDataSet.setValueTextSize(10f);
            positiveProfitDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.format("%.2f", value);
                }
            });
        }

        if (!negativeProfitList.isEmpty()) {
            negativeProfitDataSet = new BarDataSet(negativeProfitList, "亏损");
            negativeProfitDataSet.setColor(Color.parseColor("#F44336")); // 红色
            negativeProfitDataSet.setValueTextColor(Color.BLACK);
            negativeProfitDataSet.setValueTextSize(10f);
            negativeProfitDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.format("%.2f", value);
                }
            });
        }

        BarDataSet costDataSet = new BarDataSet(costList, "成本");
        costDataSet.setColor(Color.parseColor("#FF9800")); // 橙色表示成本
        costDataSet.setHighLightColor(Color.parseColor("#F57C00"));
        costDataSet.setValueTextColor(Color.BLACK);
        costDataSet.setValueTextSize(10f);

        // 创建柱状图数据 - 根据是否有正负利润来组合数据集
        BarData barData;
        if (positiveProfitDataSet != null && negativeProfitDataSet != null) {
            barData = new BarData(positiveProfitDataSet, negativeProfitDataSet, costDataSet);
        } else if (positiveProfitDataSet != null) {
            barData = new BarData(positiveProfitDataSet, costDataSet);
        } else if (negativeProfitDataSet != null) {
            barData = new BarData(negativeProfitDataSet, costDataSet);
        } else {
            // 如果没有利润数据，只显示成本
            barData = new BarData(costDataSet);
        }

        // 设置柱状图宽度
        barData.setBarWidth(0.8f); // 设置合适的柱子宽度

        // 设置数据
        barChart1.setData(barData);

        // 配置X轴 - 这是关键部分
        XAxis xAxis = barChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true); // 让标签居中显示

        // 设置X轴范围
        // 每个科目对应两个柱子（利润和成本），所以范围是 0 到 accountingList.size() * 2
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(accountingList.size() * 2 - 0.5f);

        // 设置X轴标签
        xAxis.setLabelCount(accountingList.size());

        // 设置X轴标签格式 - 简化版本
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // value是X轴位置，每个科目占据两个位置（0和1对应第一个科目，2和3对应第二个科目...）
                int index = (int) (value / 2);
                if (index >= 0 && index < accountingList.size()) {
                    // 缩短科目名称，防止X轴标签过长
                    String name = accountingList.get(index).accountingName;
                    if (name.length() > 4) {
                        return name.substring(0, 4) + "..";
                    }
                    return name;
                }
                return "";
            }
        });

        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10f);
//        xAxis.setLabelRotationAngle(-45); // 旋转-45度，防止重叠
        xAxis.setAvoidFirstLastClipping(true); // 避免第一个和最后一个标签被裁剪

        // 配置Y轴 - 支持负数显示
        YAxis leftAxis = barChart1.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#444444"));
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.BLACK);

        // 计算Y轴的最小值和最大值
        float yMin = 0f;
        float yMax = 0f;

        // 找到所有数据中的最小值和最大值
        for (BarEntry entry : profitList) {
            yMin = Math.min(yMin, entry.getY());
            yMax = Math.max(yMax, entry.getY());
        }

        for (BarEntry entry : costList) {
            yMin = Math.min(yMin, entry.getY());
            yMax = Math.max(yMax, entry.getY());
        }

        // 设置合适的Y轴范围
        if (yMin >= 0) {
            // 所有值都是非负数
            leftAxis.setAxisMinimum(0f);
            leftAxis.setAxisMaximum(yMax * 1.2f); // 留出20%空间
        } else {
            // 有负数，需要显示负数区域
            // 给一些余量，让图表更好看
            float padding = Math.max(Math.abs(yMin), Math.abs(yMax)) * 0.1f;
            leftAxis.setAxisMinimum(yMin - padding);
            leftAxis.setAxisMaximum(yMax + padding);
        }

        // 设置Y轴标签格式，支持负数
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        YAxis rightAxis = barChart1.getAxisRight();
        rightAxis.setEnabled(false); // 禁用右侧Y轴

        // 配置图例 - 水平排列在右上角
        Legend legend = barChart1.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(10f);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        legend.setXEntrySpace(8f);
        legend.setYEntrySpace(5f);
        legend.setWordWrapEnabled(true); // 允许换行

        // 设置图例位置偏移
        legend.setXOffset(-5f);  // 向左偏移
        legend.setYOffset(10f);  // 向下偏移

        // 其他配置
        barChart1.getDescription().setEnabled(false);
        barChart1.setDrawGridBackground(false);
        barChart1.setDrawBorders(false);
        barChart1.setFitBars(true); // 使柱子适应屏幕

        // 触摸交互配置 - 避免空指针
        barChart1.setTouchEnabled(false);
        barChart1.setDragEnabled(false);
        barChart1.setScaleEnabled(false);
        barChart1.setPinchZoom(false);
        barChart1.setDoubleTapToZoomEnabled(false);
        barChart1.setHighlightPerTapEnabled(false);  // 禁用点击高亮
        barChart1.setHighlightPerDragEnabled(false); // 禁用拖拽高亮

        // 设置背景和网格线颜色
        barChart1.setGridBackgroundColor(Color.TRANSPARENT);
        barChart1.setDrawGridBackground(false);

        // 设置动画
        barChart1.animateY(1500);
// 增加这行代码 - 给底部增加额外空间，确保X轴标签完全显示
        barChart1.setExtraBottomOffset(20f); // 增加40像素的底部空间
        // 刷新图表
        barChart1.invalidate();
        barChart1.notifyDataSetChanged();
    }

    private void setLineChartData() {
        // 初始化折线图控件（假设你已经在布局中添加了LineChart，id为line_chart）
        LineChart lineChart = findViewById(R.id.lc_1);

        // 清空并重置图表
        lineChart.clear();
        lineChart.fitScreen();

        // 准备利润率数据
        List<Entry> profitRateList = new ArrayList<>();

        // 计算每个条目的成本利润率（利润/成本）
        for (int i = 0; i < accountingList.size(); i++) {
            AccountingModel model = accountingList.get(i);
            float profitValue = toFloat(model.profit);
            float costValue = toFloat(model.cost);

            // 计算利润率（避免除以零）
            float profitRate = 0f;
            if (costValue != 0) {
                profitRate = profitValue / costValue * 100; // 转换为百分比
            }

            // X轴位置与柱状图对应（居中显示）
            profitRateList.add(new Entry(i * 2f + 0.5f, profitRate));
        }

        // 创建数据集
        LineDataSet profitRateDataSet = new LineDataSet(profitRateList, "成本利润率(%)");
        profitRateDataSet.setColor(Color.parseColor("#2196F3")); // 蓝色线条
        profitRateDataSet.setCircleColor(Color.parseColor("#2196F3")); // 蓝色圆点
        profitRateDataSet.setLineWidth(2f);
        profitRateDataSet.setCircleRadius(4f);
        profitRateDataSet.setDrawCircleHole(false);
        profitRateDataSet.setValueTextSize(10f);
        profitRateDataSet.setValueTextColor(Color.BLACK);

        // 设置值格式化为百分比
        profitRateDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f%%", value);
            }
        });

        // 创建折线图数据
        LineData lineData = new LineData(profitRateDataSet);

        // 设置数据
        lineChart.setData(lineData);

        // 配置X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(accountingList.size() * 2 - 0.5f);
        xAxis.setLabelCount(accountingList.size());

        // 使用与柱状图相同的X轴标签
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) (value / 2);
                if (index >= 0 && index < accountingList.size()) {
                    String name = accountingList.get(index).accountingName;
                    if (name.length() > 4) {
                        return name.substring(0, 4) + "..";
                    }
                    return name;
                }
                return "";
            }
        });

        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10f);
        xAxis.setAvoidFirstLastClipping(true);

        // 配置Y轴（利润率百分比）
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#444444"));
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f%%", value); // 显示为百分比整数
            }
        });

        // 自动计算Y轴范围（基于利润率数据）
        float minRate = Float.MAX_VALUE;
        float maxRate = Float.MIN_VALUE;

        for (Entry entry : profitRateList) {
            minRate = Math.min(minRate, entry.getY());
            maxRate = Math.max(maxRate, entry.getY());
        }

        // 设置合理的Y轴范围（留出一些空间）
        float padding = Math.max(Math.abs(minRate), Math.abs(maxRate)) * 0.2f;
        leftAxis.setAxisMinimum(minRate - padding);
        leftAxis.setAxisMaximum(maxRate + padding);

        // 禁用右侧Y轴
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        // 配置图例
        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(10f);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);

        // 其他配置
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.setTouchEnabled(false); // 根据需求设置是否可交互
        lineChart.setExtraBottomOffset(20f); // 与柱状图一致的底部空间

        // 设置动画
        lineChart.animateY(1500);

        // 刷新图表
        lineChart.invalidate();
    }

    /**
     * 根据accounting字段汇总数据
     */
    private void aggregateDataByAccounting(List<YhFinanceJiJianTaiZhang> dataList) {
        // 使用Map来汇总数据
        Map<String, AccountingModel> accountingMap = new HashMap<>();

        for (YhFinanceJiJianTaiZhang data : dataList) {
            String accounting = data.getAccounting();

            // 确保科目不为空
            if (accounting == null || accounting.trim().isEmpty()) {
                continue;
            }

            // 获取或创建科目模型
            AccountingModel model = accountingMap.get(accounting);
            if (model == null) {
                model = new AccountingModel(accounting);
                accountingMap.put(accounting, model);
            }

            // 汇总数据
            BigDecimal receipts = data.getReceipts() != null ? data.getReceipts() : BigDecimal.ZERO;
            BigDecimal payment = data.getPayment() != null ? data.getPayment() : BigDecimal.ZERO;

            model.totalReceipts = model.totalReceipts.add(receipts);
            model.totalPayment = model.totalPayment.add(payment);
        }

        // 转换为列表并计算利润
        accountingList.clear();
        for (AccountingModel model : accountingMap.values()) {
            // 利润 = receipts - payment
            model.profit = model.totalReceipts.subtract(model.totalPayment);
            // 成本 = payment
            model.cost = model.totalPayment;
            accountingList.add(model);
        }

        // 按利润降序排序（只显示前10个科目）
        Collections.sort(accountingList, new Comparator<AccountingModel>() {
            @Override
            public int compare(AccountingModel o1, AccountingModel o2) {
                return o2.profit.compareTo(o1.profit); // 降序排序
            }
        });

        // 如果科目太多，只取前10个
        if (accountingList.size() > 10) {
            accountingList = accountingList.subList(0, 10);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // 处理数据并设置图表
                try {
                    aggregateDataByAccounting(simpleDataList);
                    setChartData();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 显示错误信息
                    jiejin_sum.setText("总利润：计算错误");
                    daijin_sum.setText("总成本：计算错误");
                    jiefang_sum.setText("总收入：计算错误");
                    daifang_sum.setText("总支出：计算错误");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    yhFinanceJiJianTaiZhangService = new YhFinanceJiJianTaiZhangService();
                    // 获取所有simpleData数据
                    simpleDataList = yhFinanceJiJianTaiZhangService.getList1(yhFinanceUser.getCompany());


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.obj = "";
                listLoadHandler.sendMessage(msg);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(TongJiKanBanActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String this_month = "";
                if (monthOfYear + 1 < 10) {
                    this_month = "0" + String.format("%s", monthOfYear + 1);
                } else {
                    this_month = String.format("%s", monthOfYear + 1);
                }

                String this_day = "";
                if (dayOfMonth + 1 < 10) {
                    this_day = "0" + String.format("%s", dayOfMonth);
                } else {
                    this_day = String.format("%s", dayOfMonth);
                }
                editText.setText(year + "-" + this_month + "-" + this_day);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * 科目数据模型
     */
    class AccountingModel {
        public String accountingName;
        public BigDecimal totalReceipts = BigDecimal.ZERO;
        public BigDecimal totalPayment = BigDecimal.ZERO;
        public BigDecimal profit = BigDecimal.ZERO;
        public BigDecimal cost = BigDecimal.ZERO;

        public AccountingModel(String accountingName) {
            this.accountingName = accountingName;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                initData();
            }
        }
    }
}