package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.GongZuoTongJiService;
import com.example.myapplication.renshi.service.GongZuoTongJiService.DynamicData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.*;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.*;

public class GongZuoTongJiActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private GongZuoTongJiService tongJiService;

    // UI组件
    private NestedScrollView scrollView;
    private Spinner spinnerChartType, spinnerGroupField;
    private Button btnLoadData, btnGenerateChart, btnShowData, btnHideData;
    private LinearLayout chartContainer, configPanel, fieldSelectionContainer;
    private LinearLayout dataTableScroll;
    private TableLayout dataTableContainer;
    private TextView tvDataCount, tvStatisticsInfo, tvChartTitle;
    private ProgressBar progressBar;

    // 图表组件
    private BarChart barChart;
    private LineChart lineChart;

    // 数据
    private List<String> allFields = new ArrayList<>();
    private List<DynamicData> allData = new ArrayList<>();
    private List<String> numericFields = new ArrayList<>();
    private List<CheckBox> fieldCheckboxes = new ArrayList<>();

    private String selectedChartType = "柱状图";
    private String selectedGroupField = "";
    private List<String> selectedFields = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressBar.setVisibility(View.GONE);
            if (msg.what == 0) {
                updateFieldSelection();
                tvDataCount.setText("共加载 " + allData.size() + " 条数据");
                Toast.makeText(GongZuoTongJiActivity.this, "数据加载成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(GongZuoTongJiActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                // 图表生成成功
                updateStatisticsInfo();
            } else if (msg.what == 3) {
                // 图表生成失败
                String message = msg.getData().getString("message");
                Toast.makeText(GongZuoTongJiActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongzuotongji);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("动态数据统计图表");
        }

        initViews();
        initData();
        loadChartData();
    }

    private void initViews() {
        scrollView = findViewById(R.id.scroll_view);

        spinnerChartType = findViewById(R.id.spinner_chart_type);
        spinnerGroupField = findViewById(R.id.spinner_group_field);

        btnLoadData = findViewById(R.id.btn_load_data);
        btnGenerateChart = findViewById(R.id.btn_generate_chart);
        btnShowData = findViewById(R.id.btn_show_data);
        btnHideData = findViewById(R.id.btn_hide_data);

        chartContainer = findViewById(R.id.chart_container);
        configPanel = findViewById(R.id.config_panel);
        fieldSelectionContainer = findViewById(R.id.field_selection_container);
        dataTableScroll = findViewById(R.id.data_table_scroll);
        dataTableContainer = findViewById(R.id.data_table_container);

        tvDataCount = findViewById(R.id.tv_data_count);
        tvStatisticsInfo = findViewById(R.id.tv_statistics_info);
        tvChartTitle = findViewById(R.id.tv_chart_title);
        progressBar = findViewById(R.id.progress_bar);

        // 初始化图表
        barChart = findViewById(R.id.bar_chart);
        lineChart = findViewById(R.id.line_chart);

        setupCharts();

        // 设置按钮点击事件
        btnLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadChartData();
            }
        });

        btnGenerateChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateChart();
            }
        });


        btnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataTable();
            }
        });

        btnHideData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDataTable();
            }
        });

        // Spinner监听
        spinnerChartType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedChartType = spinnerChartType.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerGroupField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroupField = spinnerGroupField.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initData() {
        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        tongJiService = new GongZuoTongJiService();

        // 初始化图表类型选项
        String[] chartTypes = tongJiService.getChartTypeOptions();
        ArrayAdapter<String> chartTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, chartTypes);
        chartTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChartType.setAdapter(chartTypeAdapter);

        // 重置状态
        selectedChartType = "柱状图";
        spinnerChartType.setSelection(0);

        // 初始隐藏数据表格
        if (dataTableScroll != null) {
            dataTableScroll.setVisibility(View.GONE);
        }
        chartContainer.setVisibility(View.GONE);
    }
    private void loadChartData() {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL();
                    Map<String, Object> result = tongJiService.loadChartData(company);

                    if (Boolean.TRUE.equals(result.get("success"))) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> dataMap = (Map<String, Object>) result.get("data");

                        if (dataMap != null) {
                            @SuppressWarnings("unchecked")
                            List<String> fields = (List<String>) dataMap.get("fields");
                            @SuppressWarnings("unchecked")
                            List<DynamicData> data = (List<DynamicData>) dataMap.get("data");

                            if (fields != null && data != null) {
                                allFields = fields;
                                allData = data;
                                numericFields = tongJiService.getNumericFields(fields, data);
                            }
                        }

                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void updateFieldSelection() {
        fieldSelectionContainer.removeAllViews();
        fieldCheckboxes.clear();

        // 清空之前的选中字段，避免残留
        selectedFields.clear();

        if (numericFields.isEmpty()) {
            TextView noDataText = new TextView(this);
            noDataText.setText("没有找到数值字段可用于统计");
            noDataText.setTextColor(Color.RED);
            noDataText.setPadding(16, 16, 16, 16);
            fieldSelectionContainer.addView(noDataText);
            return;
        }

        // 创建字段选择标题
        TextView titleText = new TextView(this);
        titleText.setText("选择统计字段（数值字段）：");
        titleText.setTextSize(16);
        titleText.setTextColor(Color.BLACK);
        titleText.setTypeface(titleText.getTypeface(), android.graphics.Typeface.BOLD);
        titleText.setPadding(0, 16, 0, 8);
        fieldSelectionContainer.addView(titleText);

        // 创建全选/取消全选按钮
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        Button btnSelectAll = new Button(this);
        btnSelectAll.setText("全选");
        btnSelectAll.setBackgroundResource(R.drawable.newbtn_style);
        btnSelectAll.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.setMargins(4, 4, 4, 4);
        btnSelectAll.setLayoutParams(params);
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllFields(true);
            }
        });

        Button btnClearAll = new Button(this);
        btnClearAll.setText("清空");
        btnClearAll.setBackgroundResource(R.drawable.newbtn_style);
        btnClearAll.setTextColor(Color.WHITE);
        btnClearAll.setLayoutParams(params);
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllFields(false);
            }
        });

        buttonLayout.addView(btnSelectAll);
        buttonLayout.addView(btnClearAll);
        fieldSelectionContainer.addView(buttonLayout);

        // 创建字段复选框
        for (String field : numericFields) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(field);
            checkBox.setTextSize(14);
            checkBox.setPadding(16, 8, 16, 8);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String fieldName = buttonView.getText().toString();
                    if (isChecked && !selectedFields.contains(fieldName)) {
                        selectedFields.add(fieldName);
                    } else if (!isChecked) {
                        selectedFields.remove(fieldName);
                    }
                }
            });

            fieldCheckboxes.add(checkBox);
            fieldSelectionContainer.addView(checkBox);
        }

        // 更新分组字段下拉列表（使用所有字段，不排除分组字段）
        ArrayAdapter<String> groupFieldAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allFields);
        groupFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupField.setAdapter(groupFieldAdapter);

        // 重置分组字段选择
        selectedGroupField = "";
        spinnerGroupField.setSelection(0);

        // 默认选择第一个数值字段
        if (!numericFields.isEmpty()) {
            String firstField = numericFields.get(0);
            for (CheckBox cb : fieldCheckboxes) {
                if (cb.getText().toString().equals(firstField)) {
                    cb.setChecked(true);
                    selectedFields.add(firstField);
                    break;
                }
            }
        }
    }

    private void selectAllFields(boolean select) {
        selectedFields.clear();
        for (CheckBox checkBox : fieldCheckboxes) {
            checkBox.setChecked(select);
            if (select) {
                selectedFields.add(checkBox.getText().toString());
            }
        }
    }

    private void setupCharts() {
        // 设置柱状图
        setupBarChart();

        // 设置折线图
        setupLineChart();

        // 默认显示柱状图
        showChart("柱状图");
    }

    private void setupBarChart() {
        barChart.getDescription().setEnabled(true);
        barChart.getDescription().setText("统计图表");
        barChart.getDescription().setTextSize(12f);
        barChart.getDescription().setPosition(100f, 20f);

        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setPinchZoom(true);
        barChart.setDrawBorders(false);
        barChart.setBorderWidth(0.5f);
        barChart.setBorderColor(Color.LTGRAY);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setAutoScaleMinMaxEnabled(true); // 启用自动缩放

        // 重要：启用水平滚动和缩放
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setScaleXEnabled(true);
        barChart.setScaleYEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true); // 启用粒度

        // 重要：不在这里设置LabelCount，在数据更新时动态设置
        // xAxis.setLabelCount(5); // 注释掉这行

        xAxis.setLabelRotationAngle(-45);
        xAxis.setAvoidFirstLastClipping(true); // 避免首尾标签被裁剪
        xAxis.setCenterAxisLabels(true); // 标签居中显示

        // 设置X轴最小最大值，但在更新数据时会重新设置
        // 这里只设置一个初始范围
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(4.5f); // 初始显示5个标签

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#EEEEEE"));
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return new DecimalFormat("#.##").format(value);
            }
        });

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);

        barChart.setExtraBottomOffset(20f);

        // 重要：设置可见范围
        barChart.setVisibleXRangeMaximum(10f); // 默认最多显示10个标签
        barChart.setVisibleXRangeMinimum(1f);
    }

    private void setupLineChart() {
        lineChart.getDescription().setEnabled(true);
        lineChart.getDescription().setText("统计图表");
        lineChart.getDescription().setTextSize(12f);
        lineChart.getDescription().setPosition(100f, 20f);

        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawBorders(false);
        lineChart.setBorderWidth(0.5f);
        lineChart.setBorderColor(Color.LTGRAY);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);
        xAxis.setLabelRotationAngle(-45);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#EEEEEE"));
        leftAxis.setGranularity(1f);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return new DecimalFormat("#.##").format(value);
            }
        });

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        lineChart.setExtraBottomOffset(20f);
    }

    private void showChart(String chartType) {
        if (chartType.equals("柱状图")) {
            barChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);

            // 重要：不再在这里调整宽度，在updateBarChart中处理
            // 如果需要水平滚动，确保布局参数正确
            ViewGroup.LayoutParams params = barChart.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT; // 使用WRAP_CONTENT
            barChart.setLayoutParams(params);

        } else if (chartType.equals("折线图")) {
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams params = lineChart.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lineChart.setLayoutParams(params);
        }
    }

    private int calculateChartWidthFromData(int labelCount, int datasetCount) {
        if (labelCount <= 0) {
            return 600; // 默认宽度
        }

        // 基础宽度
        int baseWidth = 600;

        // 每个标签需要的宽度（考虑标签文字长度和间距）
        int perLabelWidth = 80;

        // 如果有多个数据集，需要更多宽度用于分组显示
        if (datasetCount > 1) {
            perLabelWidth += datasetCount * 20;
        }

        // 计算总宽度：基础宽度 + 标签数量 * 每个标签宽度
        int totalWidth = baseWidth + (labelCount * perLabelWidth);

        // 确保最小宽度
        return Math.max(totalWidth, 600);
    }

    private void generateChart() {
        if (selectedFields.isEmpty()) {
            Toast.makeText(this, "请至少选择一个统计字段", Toast.LENGTH_SHORT).show();
            return;
        }

        if (allData.isEmpty()) {
            Toast.makeText(this, "没有可用的数据", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> chartResult = tongJiService.generateChartData(
                        selectedFields, selectedGroupField, selectedChartType, allData);

                Message msg = new Message();
                Bundle bundle = new Bundle();

                if (Boolean.TRUE.equals(chartResult.get("success"))) {
                    msg.what = 2;

                    // 在主线程更新图表
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showChart(selectedChartType);
                            updateChart(chartResult);
                        }
                    });
                } else {
                    msg.what = 3;
                    bundle.putString("message", (String) chartResult.get("message"));
                }

                msg.setData(bundle);
                handler.sendMessage(msg);
                progressBar.setVisibility(View.GONE);
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void updateChart(Map<String, Object> chartResult) {
        @SuppressWarnings("unchecked")
        List<String> labels = (List<String>) chartResult.get("labels");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> datasets = (List<Map<String, Object>>) chartResult.get("datasets");
        String chartType = (String) chartResult.get("chartType");

        if (labels == null || datasets == null) {
            return;
        }

        // 设置图表标题
        String title = "统计图表";
        if (chartResult.containsKey("groupField")) {
            title += " - 按" + chartResult.get("groupField") + "分组";
        }
        tvChartTitle.setText(title);

        if (chartType.equals("柱状图")) {
            updateBarChart(labels, datasets);
        } else if (chartType.equals("折线图")) {
            updateLineChart(labels, datasets);
        }

        chartContainer.setVisibility(View.VISIBLE);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, chartContainer.getTop());
            }
        });
    }

//    private void updateBarChart(List<String> labels, List<Map<String, Object>> datasets) {
//        try {
//            if (labels == null || labels.isEmpty() || datasets == null || datasets.isEmpty()) {
//                Toast.makeText(this, "图表数据为空", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // 清除之前的图表数据
//            barChart.clear();
//
//            List<IBarDataSet> dataSets = new ArrayList<>();
//
//            for (int i = 0; i < datasets.size(); i++) {
//                Map<String, Object> dataset = datasets.get(i);
//
//                // 安全检查
//                if (dataset == null) continue;
//
//                @SuppressWarnings("unchecked")
//                List<Double> dataValues = (List<Double>) dataset.get("data");
//                String label = (String) dataset.get("label");
//                Object colorIndexObj = dataset.get("colorIndex");
//                int colorIndex = colorIndexObj != null ? ((Number) colorIndexObj).intValue() : i;
//
//                if (dataValues == null || label == null) {
//                    continue;
//                }
//
//                List<BarEntry> entries = new ArrayList<>();
//                for (int j = 0; j < dataValues.size(); j++) {
//                    Double value = dataValues.get(j);
//                    if (value != null) {
//                        // 关键：X轴位置使用j，确保与折线图一致
//                        entries.add(new BarEntry(j, value.floatValue()));
//                    } else {
//                        entries.add(new BarEntry(j, 0f));
//                    }
//                }
//
//                if (entries.isEmpty()) {
//                    continue;
//                }
//
//                BarDataSet dataSet = new BarDataSet(entries, label);
//                dataSet.setColor(getChartColor(colorIndex));
//                dataSet.setValueTextSize(10f);
//                dataSet.setValueFormatter(new ValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value) {
//                        if (value == (int) value) {
//                            return String.valueOf((int) value);
//                        } else {
//                            return new DecimalFormat("#.##").format(value);
//                        }
//                    }
//                });
//                dataSet.setValueTextColor(Color.BLACK);
//                dataSet.setDrawValues(true); // 确保显示数值
//
//                dataSets.add(dataSet);
//            }
//
//            if (dataSets.isEmpty()) {
//                Toast.makeText(this, "没有有效的数据集", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            BarData barData = new BarData(dataSets);
//            int dataSetCount = dataSets.size();
//
//            // 设置柱子宽度 - 根据数据集数量调整
//            if (dataSetCount > 1) {
//                // 多个数据集，使用较窄的柱子
//                barData.setBarWidth(0.3f);
//            } else {
//                // 单个数据集，可以使用较宽的柱子
//                barData.setBarWidth(0.6f);
//            }
//
//            // 设置X轴 - 关键：使用与折线图完全相同的设置
//            XAxis xAxis = barChart.getXAxis();
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//            // 关键：使用与折线图完全相同的IndexAxisValueFormatter
//            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//
//            xAxis.setGranularity(1f);
//            xAxis.setGranularityEnabled(true);
//
//            // 关键：设置X轴范围，与折线图保持一致
//            // 范围设置为从-0.5到labels.size()-0.5，确保标签在数据点中间
//            xAxis.setAxisMinimum(-0.5f);
//            xAxis.setAxisMaximum(labels.size() - 0.5f);
//
//            // 启用标签居中显示
//            xAxis.setCenterAxisLabels(true);
//
//            // 设置标签数量
//            xAxis.setLabelCount(Math.min(labels.size(), 10));
//
//            xAxis.setDrawGridLines(false);
//            xAxis.setDrawAxisLine(true);
//            xAxis.setAxisLineWidth(1f);
//            xAxis.setAxisLineColor(Color.GRAY);
//            xAxis.setAvoidFirstLastClipping(true); // 避免首尾标签被裁剪
//
//            // 处理标签旋转，避免重叠
//            if (labels.size() > 5) {
//                xAxis.setLabelRotationAngle(-45);
//                // 当标签倾斜时，需要调整偏移量
//                barChart.setExtraBottomOffset(30f);
//            } else {
//                xAxis.setLabelRotationAngle(0);
//                barChart.setExtraBottomOffset(20f);
//            }
//
//            // 如果是多数据集，设置分组显示
//            if (dataSetCount > 1) {
//                // 设置分组间距
//                float groupSpace = 0.08f;
//                float barSpace = 0.03f;
//
//                // 使用groupBars方法分组显示
//                // 关键：起始位置设置为-0.5f，确保与X轴范围对齐
//                barData.groupBars(-0.5f, groupSpace, barSpace);
//
//                // 计算分组宽度用于调整显示
//                float barWidth = 0.3f;
//                float groupWidth = barWidth * dataSetCount + barSpace * (dataSetCount - 1);
//
//                // 调整X轴显示，确保标签在分组中间
//                xAxis.setAxisMinimum(-0.5f);
//                xAxis.setAxisMaximum(labels.size() - 0.5f);
//                xAxis.setCenterAxisLabels(true);
//            }
//
//            // 配置Y轴左侧 - 保持与原来一致
//            YAxis leftAxis = barChart.getAxisLeft();
//            leftAxis.setValueFormatter(new ValueFormatter() {
//                @Override
//                public String getFormattedValue(float value) {
//                    if (value == (int) value) {
//                        return String.valueOf((int) value);
//                    } else {
//                        return new DecimalFormat("#.##").format(value);
//                    }
//                }
//            });
//
//            // 自动计算Y轴范围
//            float minValue = Float.MAX_VALUE;
//            float maxValue = Float.MIN_VALUE;
//
//            for (IBarDataSet dataSet : dataSets) {
//                int entryCount = dataSet.getEntryCount();
//                for (int i = 0; i < entryCount; i++) {
//                    BarEntry entry = (BarEntry) dataSet.getEntryForIndex(i);
//                    if (entry != null) {
//                        float yValue = entry.getY();
//                        if (yValue < minValue) minValue = yValue;
//                        if (yValue > maxValue) maxValue = yValue;
//                    }
//                }
//            }
//
//            // 设置合适的Y轴范围
//            if (minValue == Float.MAX_VALUE) minValue = 0f;
//            if (maxValue == Float.MIN_VALUE) maxValue = 10f;
//
//            // 确保有足够的显示空间
//            float yMargin = (maxValue - minValue) * 0.1f;
//            if (yMargin < 0.1f) yMargin = 0.1f;
//
//            leftAxis.setAxisMinimum(Math.max(0, minValue - yMargin));
//            leftAxis.setAxisMaximum(maxValue + yMargin);
//
//            leftAxis.setGranularity(1f);
//            leftAxis.setGranularityEnabled(true);
//            leftAxis.setDrawGridLines(true);
//            leftAxis.setGridColor(Color.parseColor("#EEEEEE"));
//            leftAxis.setGridLineWidth(0.5f);
//            leftAxis.setAxisLineColor(Color.GRAY);
//            leftAxis.setAxisLineWidth(1f);
//            leftAxis.setLabelCount(6, true); // 自动计算标签数量
//
//            // 配置Y轴右侧
//            YAxis rightAxis = barChart.getAxisRight();
//            rightAxis.setEnabled(false);
//
//            // 配置图例
//            Legend legend = barChart.getLegend();
//            legend.setForm(Legend.LegendForm.SQUARE);
//            legend.setFormSize(10f);
//            legend.setTextSize(12f);
//            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//            legend.setDrawInside(false);
//            legend.setXEntrySpace(10f);
//            legend.setYEntrySpace(5f);
//            legend.setYOffset(10f);
//            legend.setWordWrapEnabled(true); // 允许图例文字换行
//
//            // 配置图表描述
//            Description description = new Description();
//            description.setText("统计图表");
//            description.setTextSize(12f);
//            description.setTextColor(Color.GRAY);
//            description.setPosition(100f, 20f); // 设置位置
//            barChart.setDescription(description);
//
//            // 配置图表其他属性
//            barChart.setDrawGridBackground(false);
//            barChart.setDrawBarShadow(false);
//            barChart.setPinchZoom(true);
//            barChart.setScaleEnabled(true);
//            barChart.setDragEnabled(true);
//            barChart.setDoubleTapToZoomEnabled(true);
//            barChart.setHighlightPerTapEnabled(true);
//            barChart.setHighlightPerDragEnabled(true);
//
//            // 设置边界 - 根据标签数量调整
//            barChart.setExtraTopOffset(10f);
//            barChart.setExtraBottomOffset(labels.size() > 5 ? 30f : 20f);
//            barChart.setExtraLeftOffset(10f);
//            barChart.setExtraRightOffset(10f);
//
//            // 设置数据
//            barChart.setData(barData);
//
//            // 重要：刷新图表数据
//            barChart.notifyDataSetChanged();
//
//            // 计算并设置图表宽度
//            int chartWidth = calculateBarChartWidth(labels, dataSetCount);
//            ViewGroup.LayoutParams params = barChart.getLayoutParams();
//            params.width = chartWidth;
//            barChart.setLayoutParams(params);
//
//            // 刷新图表
//            barChart.invalidate();
//
//            // 添加动画
//            barChart.animateY(1000, Easing.EaseInOutQuad);
//
//            // 可选：添加点击监听
//            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//                @Override
//                public void onValueSelected(Entry e, Highlight h) {
//                    if (e != null) {
//                        int dataSetIndex = h.getDataSetIndex();
//                        int entryIndex = (int) e.getX();
//
//                        if (entryIndex >= 0 && entryIndex < labels.size()) {
//                            String label = labels.get(entryIndex);
//                            String value = new DecimalFormat("#.##").format(e.getY());
//                            String dataSetLabel = dataSets.get(dataSetIndex).getLabel();
//
//                            String message = dataSetLabel + ": " + label + " = " + value;
//                            Toast.makeText(GongZuoTongJiActivity.this, message, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onNothingSelected() {
//                    // 没有选中时的处理
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "生成图表失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void updateBarChart(List<String> labels, List<Map<String, Object>> datasets) {
        try {
            if (labels == null || labels.isEmpty() || datasets == null || datasets.isEmpty()) {
                Toast.makeText(this, "图表数据为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 清除之前的图表数据
            barChart.clear();

            List<IBarDataSet> dataSets = new ArrayList<>();

            for (int i = 0; i < datasets.size(); i++) {
                Map<String, Object> dataset = datasets.get(i);

                if (dataset == null) continue;

                @SuppressWarnings("unchecked")
                List<Double> dataValues = (List<Double>) dataset.get("data");
                String label = (String) dataset.get("label");
                Object colorIndexObj = dataset.get("colorIndex");
                int colorIndex = colorIndexObj != null ? ((Number) colorIndexObj).intValue() : i;

                if (dataValues == null || label == null) {
                    continue;
                }

                List<BarEntry> entries = new ArrayList<>();
                for (int j = 0; j < dataValues.size(); j++) {
                    Double value = dataValues.get(j);
                    if (value != null) {
                        // X轴位置使用j，对应标签索引
                        entries.add(new BarEntry(j, value.floatValue()));
                    } else {
                        entries.add(new BarEntry(j, 0f));
                    }
                }

                if (entries.isEmpty()) {
                    continue;
                }

                BarDataSet dataSet = new BarDataSet(entries, label);
                dataSet.setColor(getChartColor(colorIndex));
                dataSet.setValueTextSize(10f);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        if (value == (int) value) {
                            return String.valueOf((int) value);
                        } else {
                            return new DecimalFormat("#.##").format(value);
                        }
                    }
                });
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setDrawValues(true);

                dataSets.add(dataSet);
            }

            if (dataSets.isEmpty()) {
                Toast.makeText(this, "没有有效的数据集", Toast.LENGTH_SHORT).show();
                return;
            }

            BarData barData = new BarData(dataSets);
            int dataSetCount = dataSets.size();

            // 设置柱子宽度
            float barWidth;
            if (dataSetCount > 1) {
                // 多个数据集：窄柱子，留出分组空间
                barWidth = 0.3f;
            } else {
                // 单个数据集：宽柱子
                barWidth = 0.6f;
            }
            barData.setBarWidth(barWidth);

            // 配置X轴
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(true); // 重要：标签居中显示

            // 根据数据集数量设置不同的X轴范围
            if (dataSetCount > 1) {
                // 多数据集分组显示
                float groupSpace = 0.08f;
                float barSpace = 0.03f;

                // 重要：设置分组显示
                barData.groupBars(0, groupSpace, barSpace);

                // 计算每组的总宽度
                float groupWidth = barData.getBarWidth() * dataSetCount + barSpace * (dataSetCount - 1);

                // 设置X轴范围，确保标签在每组中间
                // 开始位置：-0.5 * (groupWidth + groupSpace)
                // 结束位置：labels.size() * (groupWidth + groupSpace) - groupSpace
                float startX = -0.5f * (groupWidth + groupSpace);
                float endX = labels.size() * (groupWidth + groupSpace) - groupSpace;

                xAxis.setAxisMinimum(startX);
                xAxis.setAxisMaximum(endX);

                // 标签数量等于分组数量
                xAxis.setLabelCount(labels.size());

                // 设置标签偏移，确保显示在每组中间
                xAxis.setCenterAxisLabels(true);

            } else {
                // 单数据集简单显示
                // X轴范围：从-0.5到labels.size()-0.5，确保柱子在标签位置
                xAxis.setAxisMinimum(-0.5f);
                xAxis.setAxisMaximum(labels.size() - 0.5f);
                xAxis.setLabelCount(Math.min(labels.size(), 10));
            }

            // 处理标签旋转
            if (labels.size() > 5) {
                xAxis.setLabelRotationAngle(-45);
                barChart.setExtraBottomOffset(30f);
            } else {
                xAxis.setLabelRotationAngle(0);
                barChart.setExtraBottomOffset(20f);
            }

            // 配置Y轴
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setAxisMinimum(0f);
            leftAxis.setGranularity(1f);
            leftAxis.setDrawGridLines(true);
            leftAxis.setGridColor(Color.parseColor("#EEEEEE"));

            // 自动计算Y轴最大值
            float maxValue = 0f;
            for (IBarDataSet dataSet : dataSets) {
                for (int i = 0; i < dataSet.getEntryCount(); i++) {
                    BarEntry entry = (BarEntry) dataSet.getEntryForIndex(i);
                    maxValue = Math.max(maxValue, entry.getY());
                }
            }
            // 增加10%的余量，并确保最小高度
            leftAxis.setAxisMaximum(Math.max(maxValue * 1.1f, 10f));

            // 禁用右侧Y轴
            barChart.getAxisRight().setEnabled(false);

            // 设置数据
            barChart.setData(barData);

            // 重要：通知图表数据已更新
            barChart.notifyDataSetChanged();

            // 重要：让柱子适应屏幕宽度
            barChart.invalidate(); // 关键方法：让柱子适应X轴

            // 重要：重新计算图表布局
            barChart.requestLayout();

            // 设置可见范围
            if (labels.size() > 5) {
                // 如果标签很多，设置合适的可见范围
                barChart.setVisibleXRangeMaximum(5f); // 默认显示5个分组
                barChart.setVisibleXRangeMinimum(1f);
            }

            // 移动到起始位置
            barChart.moveViewToX(0);

            // 计算需要的图表宽度
            int chartWidth = calculateBarChartWidth(labels, dataSetCount);
            ViewGroup.LayoutParams params = barChart.getLayoutParams();
            params.width = chartWidth;
            barChart.setLayoutParams(params);

            // 刷新图表
            barChart.invalidate();

            // 添加动画
            barChart.animateY(1000, Easing.EaseInOutQuad);

            // 添加点击监听
            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    if (e != null && h != null) {
                        int dataSetIndex = h.getDataSetIndex();
                        int entryIndex = (int) e.getX();

                        if (entryIndex >= 0 && entryIndex < labels.size()) {
                            String label = labels.get(entryIndex);
                            String value = new DecimalFormat("#.##").format(e.getY());
                            String dataSetLabel = dataSets.get(dataSetIndex).getLabel();

                            String message = dataSetLabel + ": " + label + " = " + value;
                            Toast.makeText(GongZuoTongJiActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onNothingSelected() {
                    // 没有选中时的处理
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "生成图表失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 手动调整柱状图显示（替代fitBars()方法）
     */
    private void adjustBarChartDisplay(List<String> labels, int dataSetCount) {
        if (labels == null || labels.isEmpty()) {
            return;
        }

        XAxis xAxis = barChart.getXAxis();

        // 设置X轴范围，确保标签和柱子对齐
        if (dataSetCount > 1) {
            // 多个数据集的情况
            // 计算每个标签的位置
            // 每组柱子的中心位置应该是标签的位置
            xAxis.setAxisMinimum(-0.5f);
            xAxis.setAxisMaximum(labels.size() - 0.5f);
            xAxis.setCenterAxisLabels(true);

            // 设置标签显示在每组柱子的中心
            xAxis.setLabelCount(labels.size());
            xAxis.setGranularity(1f);

            // 调整图表视图，确保所有柱子都可见
            float barWidth = 0.3f; // 与设置的一致
            float groupSpace = 0.08f;
            float barSpace = 0.03f;

            // 计算需要的总宽度
            float groupWidth = barWidth * dataSetCount + barSpace * (dataSetCount - 1);
            float totalWidth = labels.size() * (groupWidth + groupSpace);

            // 根据标签数量调整图表显示
            if (labels.size() > 5) {
                barChart.setVisibleXRangeMaximum(5); // 最多同时显示5个标签
            }

        } else {
            // 单个数据集的情况
            // 每个标签对应一个柱子
            xAxis.setAxisMinimum(-0.5f);
            xAxis.setAxisMaximum(labels.size() - 0.5f);
            xAxis.setCenterAxisLabels(true);
            xAxis.setLabelCount(labels.size());
            xAxis.setGranularity(1f);

            // 调整显示范围
            if (labels.size() > 10) {
                barChart.setVisibleXRangeMaximum(10); // 最多同时显示10个柱子
            }
        }

        // 刷新图表显示
        barChart.notifyDataSetChanged();

        // 设置图表可以滚动查看所有数据
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

        // 如果是水平滚动，设置合适的滚动范围
        if (labels.size() > 5) {
            // 计算图表需要的总宽度
            int chartWidth = calculateBarChartWidth(labels, dataSetCount);
            ViewGroup.LayoutParams params = barChart.getLayoutParams();
            params.width = chartWidth;
            barChart.setLayoutParams(params);
        }
    }

    /**
     * 计算柱状图需要的宽度
     */
    private int calculateBarChartWidth(List<String> labels, int dataSetCount) {
        if (labels == null || labels.isEmpty()) {
            return 800; // 默认宽度
        }

        // 基础宽度
        int baseWidth = 400;

        // 每个分组需要的宽度
        int perGroupWidth;
        if (dataSetCount > 1) {
            // 多数据集分组：每个分组需要更多宽度
            perGroupWidth = 120 + (dataSetCount * 20);
        } else {
            // 单数据集：宽度较小
            perGroupWidth = 80;
        }

        // 考虑标签长度
        int maxLabelLength = 0;
        for (String label : labels) {
            maxLabelLength = Math.max(maxLabelLength, label.length());
        }

        if (maxLabelLength > 10) {
            perGroupWidth += (maxLabelLength - 10) * 10;
        }

        // 计算总宽度
        int totalWidth = baseWidth + (labels.size() * perGroupWidth);

        // 设置合理的宽度范围：最小800，最大2000
        return Math.max(800, Math.min(totalWidth, 2000));
    }

    // 添加新的辅助方法
    private int calculateChartWidth(List<String> labels, List<Map<String, Object>> datasets) {
        // 根据标签数量和数据集数量计算需要的宽度
        int baseWidth = 400; // 基础宽度
        int labelCount = labels != null ? labels.size() : 0;
        int datasetCount = datasets != null ? datasets.size() : 0;

        // 如果有分组，需要更多宽度
        if (datasetCount > 1) {
            return baseWidth + (labelCount * 100) + (datasetCount * 30);
        } else {
            return baseWidth + (labelCount * 60);
        }
    }

    private void updateLineChart(List<String> labels, List<Map<String, Object>> datasets) {
        List<ILineDataSet> dataSets = new ArrayList<>();

        for (int i = 0; i < datasets.size(); i++) {
            Map<String, Object> dataset = datasets.get(i);
            @SuppressWarnings("unchecked")
            List<Double> dataValues = (List<Double>) dataset.get("data");
            String label = (String) dataset.get("label");
            int colorIndex = (int) dataset.get("colorIndex");

            List<Entry> entries = new ArrayList<>();
            for (int j = 0; j < dataValues.size(); j++) {
                entries.add(new Entry(j, dataValues.get(j).floatValue()));
            }

            LineDataSet dataSet = new LineDataSet(entries, label);
            dataSet.setColor(getChartColor(colorIndex));
            dataSet.setCircleColor(getChartColor(colorIndex));
            dataSet.setLineWidth(2f);
            dataSet.setCircleRadius(3f);
            dataSet.setDrawCircleHole(false);
            dataSet.setValueTextSize(10f);
            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return new DecimalFormat("#.##").format(value);
                }
            });

            dataSets.add(dataSet);
        }

        LineData lineData = new LineData(dataSets);

        // 设置X轴标签
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.getXAxis().setLabelCount(Math.min(labels.size(), 10));

        lineChart.setData(lineData);
        lineChart.invalidate();

        // 重要：调整折线图宽度
        int chartWidth = calculateChartWidth(labels, datasets);
        ViewGroup.LayoutParams params = lineChart.getLayoutParams();
        params.width = chartWidth;
        lineChart.setLayoutParams(params);

        lineChart.animateY(1000);
    }

    private int getChartColor(int index) {
        int[] colors = {
                Color.parseColor("#1890FF"), // 蓝色
                Color.parseColor("#52C41A"), // 绿色
                Color.parseColor("#FAAD14"), // 橙色
                Color.parseColor("#F5222D"), // 红色
                Color.parseColor("#722ED1"), // 紫色
                Color.parseColor("#13C2C2"), // 青色
                Color.parseColor("#EB2F96"), // 粉色
                Color.parseColor("#8AC6D1"), // 浅蓝
                Color.parseColor("#C3E6CB"), // 浅绿
                Color.parseColor("#FFE8A1")  // 浅黄
        };
        return colors[index % colors.length];
    }

    private void updateStatisticsInfo() {
        String info = tongJiService.getStatisticsInfo(selectedFields, allData);
        tvStatisticsInfo.setText(info);
    }

    private void exportChartImage() {
        // TODO: 实现图表导出功能
        Toast.makeText(this, "导出功能开发中", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void showDataTable() {
        if (selectedFields.isEmpty() || allData.isEmpty()) {
            Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Map<String, String>> tableData = tongJiService.getDataTable(
                selectedFields, selectedGroupField, allData);

        // 清空表格
        dataTableContainer.removeAllViews();

        if (tableData.isEmpty()) {
            Toast.makeText(this, "没有表格数据", Toast.LENGTH_SHORT).show();
            return;
        }
        // 使用LinkedHashSet来去重并保持顺序
        Set<String> headerSet = new LinkedHashSet<>();

        // 确定表头字段
        if (selectedGroupField != null && !selectedGroupField.isEmpty()) {
            headerSet.add(selectedGroupField);
        } else {
            headerSet.add("序号");
        }

        // 添加选中的字段，但排除分组字段（如果已包含）
        for (String field : selectedFields) {
            if (!field.equals(selectedGroupField)) {
                headerSet.add(field);
            }
        }

        // 转换为列表
        List<String> headerFields = new ArrayList<>(headerSet);

        // 添加表头
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.parseColor("#1890FF"));

        for (String header : headerFields) {
            TextView tv = new TextView(this);
            tv.setText(header);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(12, 8, 12, 8);
            tv.setTypeface(tv.getTypeface(), android.graphics.Typeface.BOLD);
            tv.setTextSize(12);
            tv.setSingleLine(true);
            headerRow.addView(tv);
        }

        dataTableContainer.addView(headerRow);

        // 添加数据行
        int rowCount = 0;
        for (Map<String, String> rowData : tableData) {
            rowCount++;

            TableRow dataRow = new TableRow(this);
            // 交替行颜色
            if (rowCount % 2 == 0) {
                dataRow.setBackgroundColor(Color.WHITE);
            } else {
                dataRow.setBackgroundColor(Color.parseColor("#F9F9F9"));
            }

            for (String field : headerFields) {
                TextView tv = new TextView(this);
                String value = rowData.get(field);
                tv.setText(value != null ? value : "");
                tv.setTextColor(Color.BLACK);
                tv.setPadding(12, 8, 12, 8);
                tv.setTextSize(12);
                tv.setSingleLine(true);
                dataRow.addView(tv);
            }

            dataTableContainer.addView(dataRow);
        }

        // 添加行数统计
        TextView countText = new TextView(this);
        countText.setText("共 " + rowCount + " 行数据");
        countText.setTextColor(Color.parseColor("#666666"));
        countText.setTextSize(12);
        countText.setPadding(12, 8, 12, 8);
        countText.setGravity(Gravity.CENTER);

        TableRow countRow = new TableRow(this);
        countRow.addView(countText);
        dataTableContainer.addView(countRow);

        dataTableScroll.setVisibility(View.VISIBLE);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, dataTableScroll.getTop());
            }
        });
    }

    // 修改hideDataTable方法
    private void hideDataTable() {
        if (dataTableScroll != null) {
            dataTableScroll.setVisibility(View.GONE);
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
}