package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CaiGouTuiHuoTongJiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private EditText start_date;
    private EditText end_date;
    private String start_dateText;
    private String end_dateText;
    private Button sel_button;
    private LineChart lineChart;  // 改为LineChart
    private BarChart barChart;  // 改为BarChart
    private PieChart pieChart;
    private TextView jiejin_sum;   // 总cpsl
    private TextView daijin_sum;   // 总产品金额
    private TextView jiefang_sum;  // 平均数量
    private TextView daifang_sum;  // 涉及商品数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cgthtongji);  // 确保使用正确的布局文件

        myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        lineChart = findViewById(R.id.lc_1);  // 注意ID要对应
        barChart = findViewById(R.id.bc_2);  // 注意ID要对应
        pieChart = findViewById(R.id.pc_3);  // 注意ID要对应

        // 初始化控件
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        showDateOnClick(start_date);
        showDateOnClick(end_date);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        // 初始化控件后添加以下代码
        jiejin_sum = findViewById(R.id.jiejin_sum);
        daijin_sum = findViewById(R.id.daijin_sum);
        jiefang_sum = findViewById(R.id.jiefang_sum);
        daifang_sum = findViewById(R.id.daifang_sum);


        initLineChart();
        initBarChart();    // 初始化柱状图
        initPieChart();    // 初始化饼图
        loadSalesReturnData();
    }

    // 按商品类别汇总数据
    private Map<String, Float> aggregateDataByCategory(List<YhJinXiaoCunMingXi> dataList) {
        Map<String, Float> categoryMap = new HashMap<>();

        if (dataList != null && !dataList.isEmpty()) {
            for (YhJinXiaoCunMingXi item : dataList) {
                try {
                    String category = item.getCplb();
                    if (category == null || category.isEmpty()) {
                        category = "其他";
                    }

                    float quantity = 0f;
                    try {
                        quantity = Float.parseFloat(item.getCpsl());
                    } catch (NumberFormatException e) {
                        quantity = 0f;
                    }

                    if (categoryMap.containsKey(category)) {
                        categoryMap.put(category, categoryMap.get(category) + quantity);
                    } else {
                        categoryMap.put(category, quantity);
                    }

                } catch (Exception e) {
                    Log.e("CategoryAggregate", "处理类别数据异常: " + e.getMessage());
                }
            }
        }

        Log.d("CategoryAggregate", "类别统计完成，共 " + categoryMap.size() + " 种类别");
        for (Map.Entry<String, Float> entry : categoryMap.entrySet()) {
            Log.d("CategoryData", entry.getKey() + ": " + entry.getValue());
        }

        // 返回HashMap，饼图不需要特定顺序
        return categoryMap;
    }

    // 初始化柱状图配置
    private void initBarChart() {
        barChart.clear();

        // 基本设置
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setBackgroundColor(Color.WHITE);

        // 禁用描述
        barChart.getDescription().setEnabled(false);

        // 设置X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10f);
        xAxis.setLabelRotationAngle(-45);  // 旋转标签防止重叠

        // 设置Y轴
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(10f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // 设置图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);  // 柱状图不需要图例
    }


    // 初始化折线图配置
    private void initLineChart() {
        // 清除图表
        lineChart.clear();

        // 基本设置 - 禁用所有交互
        lineChart.setTouchEnabled(false);  // 禁用触摸
        lineChart.setDragEnabled(false);   // 禁用拖动
        lineChart.setScaleEnabled(false);  // 禁用缩放
        lineChart.setPinchZoom(false);     // 禁用捏合缩放
        lineChart.setDrawGridBackground(false);  // 不绘制网格背景
        lineChart.setBackgroundColor(Color.WHITE);  // 设置背景色

        // 禁用描述
        lineChart.getDescription().setEnabled(false);

        // 设置X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);  // 绘制网格线
        xAxis.setGranularity(1f);  // 设置最小间隔
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10f);
        xAxis.setAvoidFirstLastClipping(true);  // 避免标签被裁剪
        xAxis.setCenterAxisLabels(false);  // 不居中对齐标签

        // 设置Y轴
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);  // Y轴从0开始
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(10f);
        leftAxis.setGranularity(1f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);  // 禁用右侧Y轴

        // 设置图例
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        // 设置无数据时的提示
        lineChart.setNoDataText("暂无数据");
        lineChart.setNoDataTextColor(Color.GRAY);
        lineChart.setNoDataTextTypeface(android.graphics.Typeface.DEFAULT_BOLD);
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSalesReturnData();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 初始化饼图配置
    private void initPieChart() {
        pieChart.clear();

        // 基本设置
        pieChart.setTouchEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.setBackgroundColor(Color.WHITE);

        // 禁用描述
        pieChart.getDescription().setEnabled(false);

        // 设置中间圆孔
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setHoleRadius(50f);

        // 设置中间文字
        pieChart.setCenterText("退货分布");
        pieChart.setCenterTextSize(18f);
        pieChart.setCenterTextColor(Color.BLACK);

        // 设置图例
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setTextSize(12f);
    }


    // 按商品汇总数据
    private Map<String, Float> aggregateDataByProduct(List<YhJinXiaoCunMingXi> dataList) {
        Map<String, Float> productMap = new HashMap<>();

        if (dataList != null && !dataList.isEmpty()) {
            for (YhJinXiaoCunMingXi item : dataList) {
                try {
                    String productName = item.getCpname();
                    if (productName == null || productName.isEmpty()) {
                        continue;
                    }

                    float quantity = 0f;
                    try {
                        quantity = Float.parseFloat(item.getCpsl());
                    } catch (NumberFormatException e) {
                        quantity = 0f;
                    }

                    if (productMap.containsKey(productName)) {
                        productMap.put(productName, productMap.get(productName) + quantity);
                    } else {
                        productMap.put(productName, quantity);
                    }

                } catch (Exception e) {
                    Log.e("ProductAggregate", "处理商品数据异常: " + e.getMessage());
                }
            }
        }

        // 转换为List进行排序
        List<Map.Entry<String, Float>> list = new ArrayList<>(productMap.entrySet());

        // 使用Collections.sort排序（降序）
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> entry1, Map.Entry<String, Float> entry2) {
                // 降序排序
                return Float.compare(entry2.getValue(), entry1.getValue());
            }
        });

        // 创建排序后的Map（只取前10个）- 使用LinkedHashMap保持顺序
        Map<String, Float> sortedMap = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<String, Float> entry : list) {
            if (count++ < 10) {  // 只取前10个
                sortedMap.put(entry.getKey(), entry.getValue());
            } else {
                break;
            }
        }

        Log.d("ProductAggregate", "商品统计完成，显示前 " + sortedMap.size() + " 种商品");
        for (Map.Entry<String, Float> entry : sortedMap.entrySet()) {
            Log.d("TopProducts", entry.getKey() + ": " + entry.getValue());
        }

        return sortedMap;
    }

    private void loadSalesReturnData() {
        start_dateText = start_date.getText().toString();
        end_dateText = end_date.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());

        if (start_dateText.equals("")) {
            start_dateText = formatter.format(date) + "-01-01";
        }
        if (end_dateText.equals("")) {
            end_dateText = formatter.format(date) + "-12-31";
        }

        Log.d("DateRange", "查询日期范围: " + start_dateText + " 到 " + end_dateText);

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    try {
                        // 使用Bundle获取数据
                        Bundle bundle = msg.getData();

                        // 修复：将所有Map转换为HashMap后再传递
                        HashMap<String, Float> dailyData = (HashMap<String, Float>) bundle.getSerializable("dailyData");
                        HashMap<String, Float> productData = (HashMap<String, Float>) bundle.getSerializable("productData");
                        HashMap<String, Float> categoryData = (HashMap<String, Float>) bundle.getSerializable("categoryData");

                        Log.d("DataReceived", "收到数据 - daily: " + (dailyData != null ? dailyData.size() : 0) +
                                ", product: " + (productData != null ? productData.size() : 0) +
                                ", category: " + (categoryData != null ? categoryData.size() : 0));

                        // 设置折线图
                        setupLineChart(dailyData);
                        // 设置柱状图
                        setupBarChart(productData);
                        // 设置饼图
                        setupPieChart(categoryData);

                        // ==================== 新增统计数据显示部分 ====================
                        // 从Bundle中获取统计结果
                        float totalQuantity = bundle.getFloat("totalQuantity", 0f);
                        float totalAmount = bundle.getFloat("totalAmount", 0f);
                        float averageQuantity = bundle.getFloat("averageQuantity", 0f);
                        int productCount = bundle.getInt("productCount", 0);

                        // 更新显示到TextView
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 1. 总cpsl
                                jiejin_sum.setText(String.format("总数量: %.2f", totalQuantity));

                                // 2. 总产品金额
                                daijin_sum.setText(String.format("总金额: ¥%.2f", totalAmount));

                                // 3. 平均数量
                                jiefang_sum.setText(String.format("日均: %.2f", averageQuantity));

                                // 4. 涉及商品数
                                daifang_sum.setText(String.format("涉及商品: %d种", productCount));
                            }
                        });
                        // ==================== 新增统计数据显示部分结束 ====================

                    } catch (Exception e) {
                        Log.e("DataProcessing", "处理数据异常: " + e.getMessage());
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lineChart.clear();
                                barChart.clear();
                                pieChart.clear();
                                lineChart.invalidate();
                                barChart.invalidate();
                                pieChart.invalidate();

                                // ==================== 新增清空统计显示 ====================
                                jiejin_sum.setText("总数量: 0");
                                daijin_sum.setText("总金额: ¥0.00");
                                jiefang_sum.setText("日均: 0");
                                daifang_sum.setText("涉及商品: 0种");
                                // ==================== 新增清空统计显示结束 ====================
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lineChart.clear();
                            barChart.clear();
                            pieChart.clear();
                            lineChart.invalidate();
                            barChart.invalidate();
                            pieChart.invalidate();

                            // ==================== 新增清空统计显示 ====================
                            jiejin_sum.setText("总数量: 0");
                            daijin_sum.setText("总金额: ¥0.00");
                            jiefang_sum.setText("日均: 0");
                            daifang_sum.setText("涉及商品: 0种");
                            // ==================== 新增清空统计显示结束 ====================
                        }
                    });
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Database", "开始查询数据库...");
                    List<YhJinXiaoCunMingXi> salesReturnList = yhJinXiaoCunMingXiService.getQuerycgthtongji(
                            yhJinXiaoCunUser.getGongsi(),
                            start_dateText,
                            end_dateText
                    );

                    Log.d("Database", "查询完成，获取到 " + (salesReturnList != null ? salesReturnList.size() : 0) + " 条记录");

                    if (salesReturnList == null || salesReturnList.isEmpty()) {
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("dailyData", new HashMap<String, Float>());
                        bundle.putSerializable("productData", new HashMap<String, Float>());
                        bundle.putSerializable("categoryData", new HashMap<String, Float>());
                        msg.setData(bundle);
                        return;
                    }

                    // 按日期汇总数据（折线图）
                    Map<String, Float> dailyDataMap = aggregateDataByDate(salesReturnList);
                    // 按商品汇总数据（柱状图）
                    Map<String, Float> productDataMap = aggregateDataByProduct(salesReturnList);
                    // 按类别汇总数据（饼图）
                    Map<String, Float> categoryDataMap = aggregateDataByCategory(salesReturnList);

                    // 修复：将TreeMap转换为HashMap
                    HashMap<String, Float> dailyData = convertToHashMap(dailyDataMap);
                    HashMap<String, Float> productData = convertToHashMap(productDataMap);
                    HashMap<String, Float> categoryData = convertToHashMap(categoryDataMap);

                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dailyData", dailyData);
                    bundle.putSerializable("productData", productData);
                    bundle.putSerializable("categoryData", categoryData);

                    // ==================== 新增统计计算部分 ====================
                    // 计算四个统计指标
                    float totalQuantity = 0f;        // 总cpsl
                    float totalAmount = 0f;          // 总产品金额
                    Set<String> uniqueProducts = new HashSet<>();  // 涉及商品数

                    for (YhJinXiaoCunMingXi item : salesReturnList) {
                        try {
                            // 1. 统计总数量
                            float quantity = 0f;
                            try {
                                quantity = Float.parseFloat(item.getCpsl());
                            } catch (NumberFormatException e) {
                                quantity = 0f;
                            }
                            totalQuantity += quantity;

                            // 2. 统计总金额（数量 × 单价）
                            float price = 0f;
                            try {
                                price = Float.parseFloat(item.getCpsj());
                            } catch (NumberFormatException e) {
                                price = 0f;
                            }
                            totalAmount += (quantity * price);

                            // 3. 统计涉及商品数
                            String productName = item.getCpname();
                            if (productName != null && !productName.isEmpty()) {
                                uniqueProducts.add(productName);
                            }

                        } catch (Exception e) {
                            Log.e("CalculateStatistics", "计算统计指标异常: " + e.getMessage());
                        }
                    }

                    // 4. 计算平均数量（总数量 ÷ 查询时间段的总天数）
                    float averageQuantity = 0f;
                    try {
                        // 计算查询时间段的总天数
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = sdf.parse(start_dateText);
                        Date endDate = sdf.parse(end_dateText);

                        // 计算两个日期的天数差（包含起始和结束日期）
                        long timeDiff = endDate.getTime() - startDate.getTime();
                        long daysBetween = timeDiff / (1000 * 60 * 60 * 24) + 1; // +1 包含起始日

                        Log.d("DaysCalculation", "开始日期: " + start_dateText +
                                ", 结束日期: " + end_dateText +
                                ", 总天数: " + daysBetween);

                        // 确保天数大于0
                        if (daysBetween > 0) {
                            averageQuantity = totalQuantity / daysBetween;
                        } else {
                            averageQuantity = totalQuantity; // 如果只有一天
                        }

                    } catch (ParseException e) {
                        Log.e("DaysCalculation", "日期解析错误: " + e.getMessage());
                        // 如果日期解析失败，使用有记录的天数作为备用
                        averageQuantity = dailyDataMap.size() > 0 ? totalQuantity / dailyDataMap.size() : 0f;
                    }

                    // 将统计结果添加到Bundle中
                    bundle.putFloat("totalQuantity", totalQuantity);
                    bundle.putFloat("totalAmount", totalAmount);
                    bundle.putFloat("averageQuantity", averageQuantity);
                    bundle.putInt("productCount", uniqueProducts.size());

                    Log.d("Statistics", String.format("统计结果: 总数量=%.2f, 总金额=%.2f, 平均数量=%.2f, 涉及商品数=%d",
                            totalQuantity, totalAmount, averageQuantity, uniqueProducts.size()));
                    // ==================== 新增统计计算部分结束 ====================

                    msg.setData(bundle);
                    listLoadHandler.sendMessage(msg);

                } catch (Exception e) {
                    Log.e("Database", "查询异常: " + e.getMessage());
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 0;
                    listLoadHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    // 辅助方法：将Map转换为HashMap
    private HashMap<String, Float> convertToHashMap(Map<String, Float> map) {
        if (map == null) {
            return new HashMap<>();
        }

        HashMap<String, Float> hashMap = new HashMap<>();
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }


    // 设置饼图数据
    private void setupPieChart(Map<String, Float> categoryData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (categoryData == null || categoryData.isEmpty()) {
                    pieChart.clear();
                    pieChart.invalidate();
                    return;
                }

                List<PieEntry> entries = new ArrayList<>();
                for (Map.Entry<String, Float> entry : categoryData.entrySet()) {
                    entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                }

                PieDataSet dataSet = new PieDataSet(entries, "");

                // 设置颜色
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                dataSet.setValueTextColor(Color.WHITE);
                dataSet.setValueTextSize(12f);
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);

                PieData pieData = new PieData(dataSet);
                pieData.setValueFormatter(new PercentFormatter(pieChart));

                pieChart.clear();
                pieChart.setData(pieData);
                pieChart.setUsePercentValues(true);
                pieChart.invalidate();
                pieChart.animateY(1000);

                Log.d("PieChartStatus", "饼图绘制完成，共 " + entries.size() + " 个类别");
            }
        });
    }

    // 设置柱状图数据 - 修复标签对齐问题
    private void setupBarChart(Map<String, Float> productData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (productData == null || productData.isEmpty()) {
                    barChart.clear();
                    barChart.invalidate();
                    return;
                }

                // 重置图表
                barChart.clear();

                // 基础设置
                barChart.setTouchEnabled(false);
                barChart.setDragEnabled(false);
                barChart.setScaleEnabled(false);
                barChart.setPinchZoom(false);
                barChart.setDrawGridBackground(false);
                barChart.setBackgroundColor(Color.WHITE);
                barChart.getDescription().setEnabled(false);

                // 获取数据
                List<Map.Entry<String, Float>> topProducts = new ArrayList<>(productData.entrySet());
                if (topProducts.size() > 10) {
                    topProducts = topProducts.subList(0, 10);
                }

                List<BarEntry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();

                // 关键：使用从1开始的整数作为X值（1, 2, 3, ...）
                // 这样避免0位置的偏移问题
                for (int i = 0; i < topProducts.size(); i++) {
                    Map.Entry<String, Float> entry = topProducts.get(i);
                    entries.add(new BarEntry(i + 1, entry.getValue())); // 从1开始
                    labels.add(entry.getKey());
                }

                BarDataSet dataSet = new BarDataSet(entries, "商品退货数量");
                dataSet.setColor(Color.parseColor("#2196F3"));
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setValueTextSize(10f);
                dataSet.setDrawValues(true);

                BarData barData = new BarData(dataSet);
                barData.setBarWidth(0.8f);

                // 设置X轴
                XAxis xAxis = barChart.getXAxis();

                // 关键：使用自定义索引
                final Map<Float, String> positionToLabelMap = new HashMap<>();
                for (int i = 0; i < labels.size(); i++) {
                    positionToLabelMap.put((float)(i + 1), labels.get(i));
                }

                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        // 四舍五入到最接近的整数
                        int rounded = Math.round(value);
                        String label = positionToLabelMap.get((float)rounded);
                        if (label != null) {
                            if (label.length() > 6) {
                                return label.substring(0, 5) + "...";
                            }
                            return label;
                        }
                        return "";
                    }
                });

                // 关键：设置X轴范围
                // 因为X值从1开始，范围应该是0.5到n+0.5
                float minX = 0.5f;
                float maxX = topProducts.size() + 0.5f;

                xAxis.setAxisMinimum(minX);
                xAxis.setAxisMaximum(maxX);
                xAxis.setCenterAxisLabels(false);
                xAxis.setGranularity(1f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);

                // 设置标签
                xAxis.setLabelCount(labels.size());

                if (labels.size() > 5) {
                    xAxis.setLabelRotationAngle(-45);
                }

                // 设置Y轴
                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setAxisMinimum(0f);

                float maxValue = 0;
                for (BarEntry entry : entries) {
                    if (entry.getY() > maxValue) maxValue = entry.getY();
                }
                leftAxis.setAxisMaximum(maxValue * 1.2f);

                barChart.getAxisRight().setEnabled(false);
                barChart.getLegend().setEnabled(false);

                // 设置数据
                barChart.setData(barData);

                // 关键：禁用自动调整
                barChart.setAutoScaleMinMaxEnabled(false);

                // 刷新
                barChart.notifyDataSetChanged();
                barChart.invalidate();

                // 动画
                barChart.animateY(1000);

                Log.d("BarChartDebug", "X轴范围: " + minX + " 到 " + maxX);
                Log.d("BarChartDebug", "第一个柱子的X位置: " + entries.get(0).getX());
            }
        });
    }

    // 按日期汇总数据
    private Map<String, Float> aggregateDataByDate(List<YhJinXiaoCunMingXi> dataList) {
        // 修改：使用HashMap而不是TreeMap
        Map<String, Float> dailyMap = new HashMap<>();  // 改为HashMap

        // 定义多种可能的日期格式
        SimpleDateFormat[] possibleFormats = {
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
                new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        };

        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Log.d("AggregateData", "开始汇总数据，数据条数: " + (dataList != null ? dataList.size() : 0));

        if (dataList != null && !dataList.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                YhJinXiaoCunMingXi item = dataList.get(i);
                Log.d("DataItem", "第" + (i+1) + "条数据 - 时间: " + item.getShijian() +
                        ", 数量: " + item.getCpsl() + ", 商品: " + item.getCpname());

                try {
                    if (item.getShijian() == null || item.getShijian().isEmpty()) {
                        Log.w("DataItem", "时间字段为空，跳过此条记录");
                        continue;
                    }

                    Date date = null;
                    String dateKey = "";
                    boolean parsed = false;

                    for (SimpleDateFormat format : possibleFormats) {
                        try {
                            date = format.parse(item.getShijian());
                            dateKey = displayFormat.format(date);
                            Log.d("DateFormat", "使用格式 " + format.toPattern() + " 解析成功: " + item.getShijian() + " -> " + dateKey);
                            parsed = true;
                            break;
                        } catch (ParseException e) {
                            // 继续尝试下一个格式
                        }
                    }

                    if (!parsed) {
                        Log.e("DateFormat", "无法解析时间，所有格式都失败: " + item.getShijian());
                        continue;
                    }

                    float quantity = 0f;
                    try {
                        quantity = Float.parseFloat(item.getCpsl());
                    } catch (NumberFormatException e) {
                        Log.w("Quantity", "数量格式错误: " + item.getCpsl());
                        quantity = 0f;
                    }

                    if (dailyMap.containsKey(dateKey)) {
                        dailyMap.put(dateKey, dailyMap.get(dateKey) + quantity);
                    } else {
                        dailyMap.put(dateKey, quantity);
                    }

                    Log.d("DataSum", "日期 " + dateKey + " 累计数量: " + dailyMap.get(dateKey));

                } catch (Exception e) {
                    Log.e("ProcessItem", "处理数据异常: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            Log.w("AggregateData", "数据列表为空");
        }

        // 对日期进行排序（如果需要按日期顺序显示）
        Map<String, Float> sortedMap = new TreeMap<>(dailyMap);

        Log.d("AggregateData", "汇总完成，共 " + sortedMap.size() + " 天数据");
        for (Map.Entry<String, Float> entry : sortedMap.entrySet()) {
            Log.d("DailyData", entry.getKey() + ": " + entry.getValue());
        }

        // 返回排序后的TreeMap
        return sortedMap;
    }

    // 设置折线图数据
    private void setupLineChart(Map<String, Float> dailyData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dailyData == null || dailyData.isEmpty()) {
                    lineChart.clear();
                    lineChart.invalidate();
                    return;
                }

                // 如果传入的是HashMap，需要转换为TreeMap来排序日期
                Map<String, Float> sortedData;
                if (dailyData instanceof TreeMap) {
                    sortedData = dailyData;
                } else {
                    // 转换为TreeMap自动按日期排序
                    sortedData = new TreeMap<>(dailyData);
                }

                List<Entry> entries = new ArrayList<>();
                List<String> dates = new ArrayList<>(sortedData.keySet());

                Log.d("ChartData", "准备绘制图表，数据天数: " + dates.size());
                Log.d("ChartData", "日期列表: " + dates.toString());

                // 使用从0开始的整数作为X值
                for (int i = 0; i < dates.size(); i++) {
                    float value = dailyData.get(dates.get(i));
                    // 重要：X值应该是i（0, 1, 2, ...）
                    entries.add(new Entry(i, value));
                    Log.d("ChartEntry", "X轴索引[" + i + "]: 日期=" + dates.get(i) + ", 值=" + value);
                }

                // 创建数据集
                LineDataSet dataSet = new LineDataSet(entries, "销售退货数量");
                dataSet.setColor(Color.parseColor("#FF4081"));  // 粉色
                dataSet.setCircleColor(Color.parseColor("#FF4081"));
                dataSet.setLineWidth(2f);
                dataSet.setCircleRadius(4f);
                dataSet.setDrawCircleHole(true);
                dataSet.setCircleHoleColor(Color.WHITE);
                dataSet.setValueTextSize(10f);
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setDrawValues(true);

                // 设置填充
                dataSet.setDrawFilled(true);
                dataSet.setFillColor(Color.parseColor("#80FF4081"));  // 半透明粉色
                dataSet.setFillAlpha(100);

                // 设置高亮
                dataSet.setHighlightEnabled(false);  // 禁用高亮
                dataSet.setDrawHorizontalHighlightIndicator(false);

                // 创建数据
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(dataSet);
                LineData lineData = new LineData(dataSets);

                // 清除之前的图表数据
                lineChart.clear();

                // 设置X轴
                XAxis xAxis = lineChart.getXAxis();

                // 关键：设置自定义的ValueFormatter
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int index = (int) value;
                        if (index >= 0 && index < dates.size()) {
                            return dates.get(index);
                        }
                        return "";
                    }
                });

                // 重要：正确设置X轴范围
                // 对于n个数据点，X轴范围应该是0到n-1
                float dataCount = dates.size();
                xAxis.setAxisMinimum(0f);  // 从0开始
                xAxis.setAxisMaximum(Math.max(dataCount - 1, 0f));  // 到n-1结束

                // 如果只有一个数据点，设置一个合理的范围
                if (dataCount <= 1) {
                    xAxis.setAxisMaximum(1f);
                }

                xAxis.setLabelCount(Math.min(dates.size(), 10), true);  // 最多显示10个标签
                xAxis.setGranularity(1f);  // 设置粒度为1
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(true);
                xAxis.setDrawLabels(true);  // 确保标签显示
                xAxis.setAvoidFirstLastClipping(true);  // 避免第一个和最后一个标签被裁剪
                xAxis.setCenterAxisLabels(false);  // 不居中对齐标签

                // 设置Y轴
                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setAxisMinimum(0f);
                leftAxis.setGranularity(1f);
                leftAxis.setDrawGridLines(true);

                // 禁用右侧Y轴
                lineChart.getAxisRight().setEnabled(false);

                // 禁用所有交互
                lineChart.setTouchEnabled(false);
                lineChart.setDragEnabled(false);
                lineChart.setScaleEnabled(false);
                lineChart.setPinchZoom(false);

                // 禁用描述
                lineChart.getDescription().setEnabled(false);

                // 设置图表数据
                lineChart.setData(lineData);

                // 强制设置数据后刷新
                lineChart.notifyDataSetChanged();
                lineChart.fitScreen();

                // 设置图例
                Legend legend = lineChart.getLegend();
                legend.setTextSize(14f);
                legend.setTextColor(Color.BLACK);
                legend.setFormSize(12f);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

                // 设置动画
                lineChart.animateY(1000);

                // 刷新图表
                lineChart.invalidate();

                Log.d("ChartStatus", "图表绘制完成，共 " + dates.size() + " 个数据点");
                Log.d("ChartStatus", "X轴范围: " + xAxis.getAxisMinimum() + " 到 " + xAxis.getAxisMaximum());
            }
        });
    }

    // 显示统计信息
    private void showStatistics(Map<String, Float> dailyData) {
        float total = 0f;
        float max = 0f;
        float min = Float.MAX_VALUE;
        String maxDate = "";
        String minDate = "";

        for (Map.Entry<String, Float> entry : dailyData.entrySet()) {
            float value = entry.getValue();
            total += value;

            if (value > max) {
                max = value;
                maxDate = entry.getKey();
            }

            if (value < min) {
                min = value;
                minDate = entry.getKey();
            }
        }

        float average = total / dailyData.size();




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
        DatePickerDialog datePickerDialog = new DatePickerDialog(CaiGouTuiHuoTongJiActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String this_month = "";
                        if (monthOfYear + 1 < 10) {
                            this_month = "0" + (monthOfYear + 1);
                        } else {
                            this_month = String.valueOf(monthOfYear + 1);
                        }

                        String this_day = "";
                        if (dayOfMonth < 10) {
                            this_day = "0" + dayOfMonth;
                        } else {
                            this_day = String.valueOf(dayOfMonth);
                        }
                        editText.setText(year + "-" + this_month + "-" + this_day);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                loadSalesReturnData();
            }
        }
    }
}