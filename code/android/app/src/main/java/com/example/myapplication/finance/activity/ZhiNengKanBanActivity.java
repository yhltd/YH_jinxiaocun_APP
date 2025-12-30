package com.example.myapplication.finance.activity;

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
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceKeMuZongZhangService;
import com.example.myapplication.mendian.entity.YhMendianReportForm;
import com.example.myapplication.utils.LoadingDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ZhiNengKanBanActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceKeMuZongZhangService yhFinanceKeMuZongZhangService;
    private EditText start_date;
    private EditText end_date;
    private String start_dateText;
    private String end_dateText;
    private Button sel_button;
    private BarChart barChart1;
    private BarChart barChart2;
    private BarChart barChart3;
    private BarChart barChart4;
    private BarChart barChart5;
    private BarChart barChart6;
    private List<BarEntry> nianchuList = new ArrayList<>();
    private List<BarEntry> nianmoList = new ArrayList<>();
    private List<DictModel>  list = new ArrayList<>();

    private List<BarEntry> nianchuPingZhengList = new ArrayList<>();
    private List<BarEntry> nianmoPingZhengList = new ArrayList<>();
    private List<DictModel> pingzheng_list = new ArrayList<>();

    private List<BarEntry> nianchuKeMuList = new ArrayList<>();
    private List<BarEntry> nianmoKeMuList = new ArrayList<>();
    private List<DictModel> kemu_list = new ArrayList<>();

    private List<BarEntry> nianchuZiChanList = new ArrayList<>();
    private List<BarEntry> nianmoZiChanList = new ArrayList<>();
    private List<DictModel> zichan_list = new ArrayList<>();

    private List<BarEntry> nianchuLiRunList = new ArrayList<>();
    private List<BarEntry> nianmoLiRunList = new ArrayList<>();
    private List<DictModel> lirun_list = new ArrayList<>();

    private List<BarEntry> nianchuXianJinList = new ArrayList<>();
    private List<BarEntry> nianmoXianJinList = new ArrayList<>();
    private List<DictModel> xianjin_list = new ArrayList<>();

    private float jiejin_sum = 0f;
    private float daijin_sum = 0f;

    private float jiefang_sum = 0f;
    private float daifang_sum = 0f;
    private LineChart lineChart1; // 新增折线图对象
    private LineChart lineChart3; // 新增折线图对象
    private LineChart lineChart4; // 新增折线图对象
    private boolean isBarChart = true; // 标记当前显示的是柱状图还是折线图
    private boolean isBarChart3 = true; // 标记当前显示的是柱状图还是折线图
    private boolean isBarChart4 = true; // 标记当前显示的是柱状图还是折线图
    private Button toggleChartButton; // 切换按钮
    private Button toggleChartButton3; // 切换按钮
    private Button toggleChartButton4; // 切换按钮

    List<YhFinanceKeMuZongZhang> list1;
    List<YhFinanceKeMuZongZhang> list2;
    List<YhFinanceKeMuZongZhang> list3;
    List<YhFinanceKeMuZongZhang> list4_1;
    List<YhFinanceKeMuZongZhang> list4_2;
    List<YhFinanceKeMuZongZhang> list4_3;
    List<YhFinanceKeMuZongZhang> list5;
    List<YhFinanceKeMuZongZhang> list6_1;
    List<YhFinanceKeMuZongZhang> list6_2;
    List<YhFinanceKeMuZongZhang> list6_3;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        super.onCreate(savedInstanceState);
        // 去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.zhinengkanban);



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        barChart1 = findViewById(R.id.bc_1);
        barChart2 = findViewById(R.id.bc_2);
        // 初始化新增的折线图
        lineChart1 = findViewById(R.id.lc_1);
        lineChart3 = findViewById(R.id.lc_3);
        lineChart4 = findViewById(R.id.lc_4);

        // 初始化切换按钮
        toggleChartButton = findViewById(R.id.toggle_chart_button);
        toggleChartButton.setOnClickListener(v -> toggleChart());
        toggleChartButton3 = findViewById(R.id.toggle_chart_button3);
        toggleChartButton3.setOnClickListener(v -> toggleChart3());
        toggleChartButton4 = findViewById(R.id.toggle_chart_button4);
        toggleChartButton4.setOnClickListener(v -> toggleChart4());
        
        //初始化控件
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        showDateOnClick(start_date);
        showDateOnClick(end_date);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        initList();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
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


    public static float toFloat(BigDecimal b)
    {
        return b.setScale(2).floatValue();
    }

    // 切换图表显示
    private void toggleChart() {
        if (isBarChart) {
            // 显示折线图，隐藏柱状图
            barChart1.setVisibility(View.GONE);
            lineChart1.setVisibility(View.VISIBLE);
            toggleChartButton.setText("切换为柱状图");

            // 设置折线图数据
            setNianChuYuELineChart();
        } else {
            // 显示柱状图，隐藏折线图
            barChart1.setVisibility(View.VISIBLE);
            lineChart1.setVisibility(View.GONE);
            toggleChartButton.setText("切换为折线图");

            // 重新设置柱状图数据（确保数据最新）
            setNianChuYuE();
        }
        isBarChart = !isBarChart;
    }


    // 切换图表显示
    private void toggleChart3() {
        if (isBarChart3) {
            // 显示折线图，隐藏柱状图
            barChart3.setVisibility(View.GONE);
            lineChart3.setVisibility(View.VISIBLE);
            toggleChartButton3.setText("切换为柱状图");

            // 设置折线图数据
            setKeMuYuELineChart();
        } else {
            // 显示柱状图，隐藏折线图
            barChart3.setVisibility(View.VISIBLE);
            lineChart3.setVisibility(View.GONE);
            toggleChartButton3.setText("切换为折线图");

            // 重新设置柱状图数据（确保数据最新）
            setKeMuYuE();
        }
        isBarChart3 = !isBarChart3;
    }

    // 切换图表显示
    private void toggleChart4() {
        if (isBarChart4) {
            // 显示折线图，隐藏柱状图
            barChart4.setVisibility(View.GONE);
            lineChart4.setVisibility(View.VISIBLE);
            toggleChartButton3.setText("切换为柱状图");

            // 设置折线图数据
            setZiChanFuZhaiLineChart();
        } else {
            // 显示柱状图，隐藏折线图
            barChart4.setVisibility(View.VISIBLE);
            lineChart4.setVisibility(View.GONE);
            toggleChartButton4.setText("切换为折线图");

            // 重新设置柱状图数据（确保数据最新）
            setZiChanFuZhai();
        }
        isBarChart4 = !isBarChart4;
    }

    // 新增的折线图方法
    private void setNianChuYuELineChart() {
        // 使用与柱状图相同的数据
        list = new ArrayList<>();
        list.add(new DictModel("资产类", list1.get(0).getLoad(), list1.get(0).getBorrowed()));
        list.add(new DictModel("负债类", list1.get(1).getLoad(), list1.get(1).getBorrowed()));
        list.add(new DictModel("权益类", list1.get(2).getLoad(), list1.get(2).getBorrowed()));
        list.add(new DictModel("成本类", list1.get(3).getLoad(), list1.get(3).getBorrowed()));
        list.add(new DictModel("损益类", list1.get(4).getLoad(), list1.get(4).getBorrowed()));

        // 计算净额（借金 - 贷金）
        List<Entry> netEntries = new ArrayList<>();
        float totalNet = 0f;

        for (int i = 0; i < list.size(); i++) {
            float boyValue = toFloat(list.get(i).boyCnt);
            float girlValue = toFloat(list.get(i).girlCnt);
            float netValue = boyValue - girlValue;
            totalNet += netValue;
            netEntries.add(new Entry(i, netValue));
        }

        // 设置TextView显示净额合计
        TextView jiejin = findViewById(R.id.jiejin_sum);
        TextView daijin = findViewById(R.id.daijin_sum);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        jiejin.setText("借金合计：" + nf.format(jiejin_sum));
        daijin.setText("贷金合计：" + nf.format(daijin_sum));


        // 创建折线数据集
        LineDataSet lineDataSet = new LineDataSet(netEntries, "净额");
        lineDataSet.setColor(Color.parseColor("#FF9800")); // 橙色线条
        lineDataSet.setCircleColor(Color.parseColor("#FF5722")); // 红色圆点
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setDrawValues(true); // 显示数值
        lineDataSet.setValueTextSize(10f);

        // 创建折线数据对象
        LineData lineData = new LineData(lineDataSet);

        // 设置折线图
        lineChart1.setData(lineData);
        lineChart1.getDescription().setEnabled(false);
        lineChart1.setDrawGridBackground(false);
        lineChart1.setTouchEnabled(true);
        lineChart1.setDragEnabled(true);
        lineChart1.setScaleEnabled(true);
        lineChart1.setPinchZoom(true);

        // X轴设置
        XAxis xAxis = lineChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(-0.5f); // 添加这行
        xAxis.setAxisMaximum(list.size() - 0.5f); // 添加这行
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < list.size()) { // 添加边界检查
                    return list.get(index).title;
                }
                return "";
            }
        });

        // 增加左边距
        lineChart1.setExtraLeftOffset(15f);

        // 左侧Y轴设置
        lineChart1.getAxisLeft().setDrawGridLines(true);
        lineChart1.getAxisRight().setEnabled(false);

        // 图例设置
        Legend legend = lineChart1.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        // 刷新图表
        lineChart1.invalidate();
    }


    private void setNianChuYuE(){

        barChart1=findViewById(R.id.bc_1);
        list = new ArrayList<>();

        list.add(new DictModel("资产类",list1.get(0).getLoad(),list1.get(0).getBorrowed()));
        list.add(new DictModel("负债类",list1.get(1).getLoad(),list1.get(1).getBorrowed()));
        list.add(new DictModel("权益类",list1.get(2).getLoad(),list1.get(2).getBorrowed()));
        list.add(new DictModel("成本类",list1.get(3).getLoad(),list1.get(3).getBorrowed()));
        list.add(new DictModel("损益类",list1.get(4).getLoad(),list1.get(4).getBorrowed()));

        nianchuList = new ArrayList<>();
        nianmoList = new ArrayList<>();

        jiejin_sum = 0f;
        daijin_sum = 0f;

        for(int i=0;i<list.size();i++){
            jiejin_sum = jiejin_sum + toFloat(list.get(i).boyCnt);
            daijin_sum = daijin_sum + toFloat(list.get(i).girlCnt);
            nianchuList.add(new BarEntry((float) (i+1),toFloat(list.get(i).boyCnt)));
            nianmoList.add(new BarEntry((float) (i+1),toFloat(list.get(i).girlCnt)));
        }

        TextView jiejin = findViewById(R.id.jiejin_sum);
        TextView daijin = findViewById(R.id.daijin_sum);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        jiejin.setText("借金合计：" + nf.format(jiejin_sum));
        daijin.setText("贷金合计：" + nf.format(daijin_sum));

        BarDataSet barDataSet=new BarDataSet(nianchuList,"年初借金");
        barDataSet.setColor(Color.parseColor("#0066CC"));    //为第一组柱子设置颜色
        BarDataSet barDataSet2=new BarDataSet(nianmoList,"年初贷金");
        barDataSet2.setColor(Color.parseColor("#66CCFF"));   //为第二组柱子设置颜色
        BarData barData=new BarData(barDataSet);   //加上第一组
        barData.addDataSet(barDataSet2);    //加上第二组   （多组也可以用同样的方法）

        barDataSet.setHighLightAlpha(0);
        barDataSet2.setHighLightAlpha(0);

        barChart1.setData(
                barData);

        barData.setBarWidth(0.2f);//柱子的宽度
        //重点！   三个参数   分别代表   X轴起点     组与组之间的间隔      组内柱子的间隔
        barData.groupBars(1f,0.4f,0.1f);

        barChart1.getXAxis().setCenterAxisLabels(true);   //设置柱子（柱子组）居中对齐X轴上的点
        barChart1.setScaleEnabled(false);//禁止缩放
        barChart1.getXAxis().setAxisMaximum(nianchuList.size()+1);   //X轴最大数值
        barChart1.getXAxis().setAxisMinimum(1);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        barChart1.getXAxis().setLabelCount(nianmoList.size()+1,false);
        barChart1.getDescription().setEnabled(false);    //右下角一串英文字母不显示
        barChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);   //X轴的位置设置为下  默认为上
        barChart1.getAxisRight().setEnabled(false);     //右侧Y轴不显示   默认为显示
        XAxis xl = barChart1.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);

        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if((int)value<=list.size() && (int)value-1 > 0){
                    return list.get((int) value-1).title;
                }else {
                    return list.get(0).title;
                }
            }
        });

        Legend l = barChart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        barChart1.invalidate();
    }

    private void setPingZhengJinE(){

        barChart2=findViewById(R.id.bc_2);
        pingzheng_list = new ArrayList<>();
        BigDecimal daifang = new BigDecimal("0");
        BigDecimal jiefang = new BigDecimal("0");
        if(list2 != null){
            for(int i=0;i<list2.size();i++){
                if(list2.get(i).isDirection()){
                    daifang = list2.get(i).getLoad();
                }else{
                    jiefang = list2.get(i).getLoad();
                }
            }
        }
        pingzheng_list.add(new DictModel("金额",daifang,jiefang));

        nianchuPingZhengList = new ArrayList<>();
        nianmoPingZhengList = new ArrayList<>();

        for(int i=0;i<pingzheng_list.size();i++){
            nianchuPingZhengList.add(new BarEntry((float) (i+1),toFloat(pingzheng_list.get(i).boyCnt)));
            nianmoPingZhengList.add(new BarEntry((float) (i+1),toFloat(pingzheng_list.get(i).girlCnt)));
        }

        BarDataSet barDataSet=new BarDataSet(nianchuPingZhengList,"贷方金额");
        barDataSet.setColor(Color.parseColor("#0066CC"));    //为第一组柱子设置颜色
        BarDataSet barDataSet2=new BarDataSet(nianmoPingZhengList,"借方金额");
        barDataSet2.setColor(Color.parseColor("#66CCFF"));   //为第二组柱子设置颜色
        BarData barData=new BarData(barDataSet);   //加上第一组
        barData.addDataSet(barDataSet2);    //加上第二组   （多组也可以用同样的方法）

        barDataSet.setHighLightAlpha(0);
        barDataSet2.setHighLightAlpha(0);

        barChart2.setData(
                barData);

        barData.setBarWidth(0.2f);//柱子的宽度
        //重点！   三个参数   分别代表   X轴起点     组与组之间的间隔      组内柱子的间隔
        barData.groupBars(1f,0.4f,0.1f);

        barChart2.getXAxis().setCenterAxisLabels(true);   //设置柱子（柱子组）居中对齐X轴上的点
        barChart2.setScaleEnabled(false);//禁止缩放
        barChart2.getXAxis().setAxisMaximum(nianchuPingZhengList.size()+1);   //X轴最大数值
        barChart2.getXAxis().setAxisMinimum(1);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        barChart2.getXAxis().setLabelCount(nianmoPingZhengList.size()+1,false);
        barChart2.getDescription().setEnabled(false);    //右下角一串英文字母不显示
        barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);   //X轴的位置设置为下  默认为上
        barChart2.getAxisRight().setEnabled(false);     //右侧Y轴不显示   默认为显示
        XAxis xl = barChart2.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);

        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if((int)value<=pingzheng_list.size() && (int)value-1 > 0){
                    return pingzheng_list.get((int) value-1).title;
                }else {
                    return pingzheng_list.get(0).title;
                }
            }
        });

        Legend l = barChart2.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        barChart2.invalidate();
    }

    private void setKeMuYuE(){

        barChart3=findViewById(R.id.bc_3);
        kemu_list = new ArrayList<>();
        kemu_list.add(new DictModel("资产类",list3.get(0).getLoad(),list3.get(0).getBorrowed()));
        kemu_list.add(new DictModel("负债类",list3.get(1).getLoad(),list3.get(1).getBorrowed()));
        kemu_list.add(new DictModel("权益类",list3.get(2).getLoad(),list3.get(2).getBorrowed()));
        kemu_list.add(new DictModel("成本类",list3.get(3).getLoad(),list3.get(3).getBorrowed()));
        kemu_list.add(new DictModel("损益类",list3.get(4).getLoad(),list3.get(4).getBorrowed()));

        nianchuKeMuList = new ArrayList<>();
        nianmoKeMuList = new ArrayList<>();
        jiefang_sum = 0f;
        daifang_sum = 0f;
        for(int i=0;i<kemu_list.size();i++){
            jiefang_sum = jiefang_sum + toFloat(kemu_list.get(i).boyCnt);
            daifang_sum = daifang_sum + toFloat(kemu_list.get(i).girlCnt);
            nianchuKeMuList.add(new BarEntry((float) (i+1),toFloat(kemu_list.get(i).boyCnt)));
            nianmoKeMuList.add(new BarEntry((float) (i+1),toFloat(kemu_list.get(i).girlCnt)));
        }

        TextView jiejin = findViewById(R.id.jiefang_sum);
        TextView daijin = findViewById(R.id.daifang_sum);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        jiejin.setText("借方合计：" + nf.format(jiefang_sum));
        daijin.setText("贷方合计：" + nf.format(daifang_sum));


        BarDataSet barDataSet=new BarDataSet(nianchuKeMuList,"贷方");
        barDataSet.setColor(Color.parseColor("#0066CC"));    //为第一组柱子设置颜色
        BarDataSet barDataSet2=new BarDataSet(nianmoKeMuList,"借方");
        barDataSet2.setColor(Color.parseColor("#66CCFF"));   //为第二组柱子设置颜色
        BarData barData=new BarData(barDataSet);   //加上第一组
        barData.addDataSet(barDataSet2);    //加上第二组   （多组也可以用同样的方法）

        barDataSet.setHighLightAlpha(0);
        barDataSet2.setHighLightAlpha(0);

        barChart3.setData(
                barData);

        barData.setBarWidth(0.2f);//柱子的宽度
        //重点！   三个参数   分别代表   X轴起点     组与组之间的间隔      组内柱子的间隔
        barData.groupBars(1f,0.4f,0.1f);

        barChart3.getXAxis().setCenterAxisLabels(true);   //设置柱子（柱子组）居中对齐X轴上的点
        barChart3.setScaleEnabled(false);//禁止缩放
        barChart3.getXAxis().setAxisMaximum(nianchuKeMuList.size()+1);   //X轴最大数值
        barChart3.getXAxis().setAxisMinimum(1);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        barChart3.getXAxis().setLabelCount(nianmoKeMuList.size()+1,false);
        barChart3.getDescription().setEnabled(false);    //右下角一串英文字母不显示
        barChart3.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);   //X轴的位置设置为下  默认为上
        barChart3.getAxisRight().setEnabled(false);     //右侧Y轴不显示   默认为显示
        XAxis xl = barChart3.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);

        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if((int)value<=kemu_list.size() && (int)value-1 > 0){
                    return kemu_list.get((int) value-1).title;
                }else {
                    return kemu_list.get(0).title;
                }
            }
        });

        Legend l = barChart3.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        barChart3.invalidate();
    }


    private void setKeMuYuELineChart() {
        // 获取或创建折线图视图
        LineChart lineChart = findViewById(R.id.lc_3); // 确保布局中有这个ID

        // 使用与柱状图相同的数据
        List<DictModel> kemu_list = new ArrayList<>();
        kemu_list.add(new DictModel("资产类", list3.get(0).getLoad(), list3.get(0).getBorrowed()));
        kemu_list.add(new DictModel("负债类", list3.get(1).getLoad(), list3.get(1).getBorrowed()));
        kemu_list.add(new DictModel("权益类", list3.get(2).getLoad(), list3.get(2).getBorrowed()));
        kemu_list.add(new DictModel("成本类", list3.get(3).getLoad(), list3.get(3).getBorrowed()));
        kemu_list.add(new DictModel("损益类", list3.get(4).getLoad(), list3.get(4).getBorrowed()));

        // 计算净额（借方 - 贷方）和合计
        List<Entry> netEntries = new ArrayList<>();
        float totalNet = 0f;
        float jiefangSum = 0f;
        float daifangSum = 0f;

        for (int i = 0; i < kemu_list.size(); i++) {
            float jiefangValue = toFloat(kemu_list.get(i).boyCnt);
            float daifangValue = toFloat(kemu_list.get(i).girlCnt);
            float netValue = jiefangValue - daifangValue;
            totalNet += netValue;
            jiefangSum += jiefangValue;
            daifangSum += daifangValue;
            netEntries.add(new Entry(i, netValue));
        }



        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);


        // 创建折线数据集
        LineDataSet lineDataSet = new LineDataSet(netEntries, "净额(借方-贷方)");
        lineDataSet.setColor(Color.parseColor("#4CAF50")); // 绿色线条
        lineDataSet.setCircleColor(Color.parseColor("#2E7D32")); // 深绿色圆点
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setDrawValues(true); // 显示数值
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return nf.format(value);
            }
        });

        // 创建折线数据对象
        LineData lineData = new LineData(lineDataSet);

        // 设置折线图
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        // X轴设置
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(-0.5f); // 给第一条数据留出空间
        xAxis.setAxisMaximum(kemu_list.size() - 0.5f); // 确保最后一条数据显示完整
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < kemu_list.size()) {
                    return kemu_list.get(index).title;
                }
                return "";
            }
        });

        // Y轴设置
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return nf.format(value);
            }
        });

        lineChart.getAxisRight().setEnabled(false);

        // 图例设置
        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        // 增加边距防止数据贴边
        lineChart.setExtraLeftOffset(15f);
        lineChart.setExtraRightOffset(15f);
        lineChart.setExtraBottomOffset(10f);

        // 刷新图表
        lineChart.invalidate();
    }




    private void setZiChanFuZhai(){

        barChart4=findViewById(R.id.bc_4);
        zichan_list = new ArrayList<>();
        zichan_list.add(new DictModel("资产类",list4_1.get(0).getLoad(),list4_1.get(0).getBorrowed()));
        zichan_list.add(new DictModel("负债类",list4_2.get(0).getLoad(),list4_2.get(0).getBorrowed()));
        zichan_list.add(new DictModel("权益类",list4_3.get(0).getLoad(),list4_3.get(0).getBorrowed()));

        nianchuZiChanList = new ArrayList<>();
        nianmoZiChanList = new ArrayList<>();

        for(int i=0;i<zichan_list.size();i++){
            nianchuZiChanList.add(new BarEntry((float) (i+1),toFloat(zichan_list.get(i).boyCnt)));
            nianmoZiChanList.add(new BarEntry((float) (i+1),toFloat(zichan_list.get(i).girlCnt)));
        }


        BarDataSet barDataSet=new BarDataSet(nianchuZiChanList,"年初");
        barDataSet.setColor(Color.parseColor("#0066CC"));    //为第一组柱子设置颜色
        BarDataSet barDataSet2=new BarDataSet(nianmoZiChanList,"年末");
        barDataSet2.setColor(Color.parseColor("#66CCFF"));   //为第二组柱子设置颜色
        BarData barData=new BarData(barDataSet);   //加上第一组
        barData.addDataSet(barDataSet2);    //加上第二组   （多组也可以用同样的方法）

        barDataSet.setHighLightAlpha(0);
        barDataSet2.setHighLightAlpha(0);

        barChart4.setData(
                barData);

        barData.setBarWidth(0.2f);//柱子的宽度
        //重点！   三个参数   分别代表   X轴起点     组与组之间的间隔      组内柱子的间隔
        barData.groupBars(1f,0.4f,0.1f);

        barChart4.getXAxis().setCenterAxisLabels(true);   //设置柱子（柱子组）居中对齐X轴上的点
        barChart4.setScaleEnabled(false);//禁止缩放
        barChart4.getXAxis().setAxisMaximum(nianchuZiChanList.size()+1);   //X轴最大数值
        barChart4.getXAxis().setAxisMinimum(1);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        barChart4.getXAxis().setLabelCount(nianmoZiChanList.size()+1,false);
        barChart4.getDescription().setEnabled(false);    //右下角一串英文字母不显示
        barChart4.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);   //X轴的位置设置为下  默认为上
        barChart4.getAxisRight().setEnabled(false);     //右侧Y轴不显示   默认为显示
        XAxis xl = barChart4.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);

        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if((int)value<=zichan_list.size() && (int)value-1 > 0){
                    return zichan_list.get((int) value-1).title;
                }else {
                    return zichan_list.get(0).title;
                }
            }
        });

        Legend l = barChart4.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        barChart4.invalidate();
    }

    private void setZiChanFuZhaiLineChart() {
        // 获取折线图视图
        LineChart lineChart = findViewById(R.id.lc_4);

        // 使用与柱状图相同的数据源
        List<DictModel> zichanList = new ArrayList<>();
        zichanList.add(new DictModel("资产类", list4_1.get(0).getLoad(), list4_1.get(0).getBorrowed()));
        zichanList.add(new DictModel("负债类", list4_2.get(0).getLoad(), list4_2.get(0).getBorrowed()));
        zichanList.add(new DictModel("权益类", list4_3.get(0).getLoad(), list4_3.get(0).getBorrowed()));

        // 计算增长额（年末 - 年初）和合计
        List<Entry> growthEntries = new ArrayList<>();
        float totalGrowth = 0f;
        float nianchuSum = 0f;
        float nianmoSum = 0f;

        for (int i = 0; i < zichanList.size(); i++) {
            float nianchuValue = toFloat(zichanList.get(i).boyCnt);  // 年初值
            float nianmoValue = toFloat(zichanList.get(i).girlCnt);  // 年末值
            float growthValue = nianmoValue-nianchuValue;  // 增长额
            totalGrowth += growthValue;
            nianchuSum += nianchuValue;
            nianmoSum += nianmoValue;
            growthEntries.add(new Entry(i, growthValue));
        }



        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);


        // 创建折线数据集
        LineDataSet lineDataSet = new LineDataSet(growthEntries, "增长额(年末-年初)");
        lineDataSet.setColor(Color.parseColor("#9C27B0")); // 紫色线条
        lineDataSet.setCircleColor(Color.parseColor("#7B1FA2")); // 深紫色圆点
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER); // 水平平滑曲线

        // 设置增长额的正负颜色区分
        lineDataSet.setColor(Color.parseColor("#4CAF50")); // 默认绿色
        lineDataSet.setCircleColor(Color.parseColor("#2E7D32")); // 默认深绿色

        // 根据数值设置不同颜色（正数为增长，负数为减少）
        lineDataSet.setGradientColor(Color.parseColor("#4CAF50"), Color.parseColor("#F44336"));
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

        // 设置数值格式化
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return nf.format(value);
            }
        });

        // 创建折线数据对象
        LineData lineData = new LineData(lineDataSet);

        // 设置折线图
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setExtraBottomOffset(10f);

        // X轴设置
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(zichanList.size() - 0.5f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < zichanList.size()) {
                    return zichanList.get(index).title;
                }
                return "";
            }
        });

        // Y轴设置
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return nf.format(value);
            }
        });
        lineChart.getAxisRight().setEnabled(false);

        // 添加限制线（0值线）
        LimitLine zeroLine = new LimitLine(0f, "基准线");
        zeroLine.setLineColor(Color.RED);
        zeroLine.setLineWidth(1f);
        zeroLine.enableDashedLine(10f, 5f, 0f);
        zeroLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        zeroLine.setTextSize(10f);
        leftAxis.addLimitLine(zeroLine);

        // 图例设置
        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        // 增加边距
        lineChart.setExtraLeftOffset(15f);
        lineChart.setExtraRightOffset(15f);

        // 刷新图表
        lineChart.invalidate();
    }

    private void setLiRunHeJi() {
        try {
            barChart5 = findViewById(R.id.bc_5);

            // 清除旧数据
            barChart5.clear();

            // 1. 添加空值和大小检查
            if (list5 == null) {
                Log.e("ChartError", "list5 is null");
                barChart5.setNoDataText("数据加载中...");
                barChart5.invalidate();
                return;
            }

            if (list5.isEmpty()) {
                Log.e("ChartError", "list5 is empty, size: 0");
                barChart5.setNoDataText("暂无利润数据");
                barChart5.invalidate();
                return;
            }

            Log.d("ChartDebug", "list5 size: " + list5.size());

            lirun_list = new ArrayList<>();

            // 2. 处理数据，确保至少有2条数据
            if (list5.size() >= 2) {
                // 正常情况：有收入和支出两条数据
                lirun_list.add(new DictModel("收入",
                        list5.get(0).getLoad() != null ? list5.get(0).getLoad() : BigDecimal.ZERO,
                        list5.get(0).getBorrowed() != null ? list5.get(0).getBorrowed() : BigDecimal.ZERO));
                lirun_list.add(new DictModel("支出",
                        list5.get(1).getLoad() != null ? list5.get(1).getLoad() : BigDecimal.ZERO,
                        list5.get(1).getBorrowed() != null ? list5.get(1).getBorrowed() : BigDecimal.ZERO));
            } else if (list5.size() == 1) {
                // 只有一条数据的情况：假设为收入，支出设为0
                Log.w("ChartWarning", "Only one data in list5, using default for expense");
                lirun_list.add(new DictModel("收入",
                        list5.get(0).getLoad() != null ? list5.get(0).getLoad() : BigDecimal.ZERO,
                        list5.get(0).getBorrowed() != null ? list5.get(0).getBorrowed() : BigDecimal.ZERO));
                lirun_list.add(new DictModel("支出", BigDecimal.ZERO, BigDecimal.ZERO));
            }

            // 3. 验证数据是否有效
            for (int i = 0; i < lirun_list.size(); i++) {
                DictModel item = lirun_list.get(i);
                Log.d("ChartDebug", item.title + ": boyCnt=" + item.boyCnt + ", girlCnt=" + item.girlCnt);
            }

            nianchuLiRunList = new ArrayList<>();
            nianmoLiRunList = new ArrayList<>();

            // 4. 创建图表数据条目
            for (int i = 0; i < lirun_list.size(); i++) {
                try {
                    float boyValue = toFloat(lirun_list.get(i).boyCnt);
                    float girlValue = toFloat(lirun_list.get(i).girlCnt);

                    // 确保值不是NaN或无限大
                    if (Float.isNaN(boyValue) || Float.isInfinite(boyValue)) boyValue = 0f;
                    if (Float.isNaN(girlValue) || Float.isInfinite(girlValue)) girlValue = 0f;

                    nianchuLiRunList.add(new BarEntry((float) (i + 1), boyValue));
                    nianmoLiRunList.add(new BarEntry((float) (i + 1), girlValue));

                    Log.d("ChartDebug", "Entry " + i + ": boy=" + boyValue + ", girl=" + girlValue);
                } catch (Exception e) {
                    Log.e("ChartError", "Error creating BarEntry for index " + i, e);
                    nianchuLiRunList.add(new BarEntry((float) (i + 1), 0f));
                    nianmoLiRunList.add(new BarEntry((float) (i + 1), 0f));
                }
            }

            // 5. 检查图表数据是否有效
            if (nianchuLiRunList.isEmpty() || nianmoLiRunList.isEmpty()) {
                Log.e("ChartError", "Chart data lists are empty");
                barChart5.setNoDataText("数据异常");
                barChart5.invalidate();
                return;
            }

            BarDataSet barDataSet = new BarDataSet(nianchuLiRunList, "月");
            barDataSet.setColor(Color.parseColor("#0066CC"));
            BarDataSet barDataSet2 = new BarDataSet(nianmoLiRunList, "年");
            barDataSet2.setColor(Color.parseColor("#66CCFF"));

            BarData barData = new BarData(barDataSet);
            barData.addDataSet(barDataSet2);

            // 6. 防止高亮效果
            barDataSet.setHighLightAlpha(0);
            barDataSet2.setHighLightAlpha(0);

            // 7. 设置数据
            barChart5.setData(barData);

            // 8. 图表配置
            barData.setBarWidth(0.2f);
            barData.groupBars(1f, 0.4f, 0.1f);

            barChart5.getXAxis().setCenterAxisLabels(true);
            barChart5.setScaleEnabled(false);
            barChart5.getXAxis().setAxisMaximum(nianchuLiRunList.size() + 1);
            barChart5.getXAxis().setAxisMinimum(1);
            barChart5.getXAxis().setLabelCount(nianmoLiRunList.size() + 1, false);
            barChart5.getDescription().setEnabled(false);
            barChart5.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart5.getAxisRight().setEnabled(false);

            XAxis xl = barChart5.getXAxis();
            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
            xl.setDrawAxisLine(true);
            xl.setDrawGridLines(false);
            xl.setGranularity(1f);

            // 9. X轴标签格式化器
            xl.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    try {
                        int index = (int) value - 1;
                        if (index >= 0 && index < lirun_list.size()) {
                            return lirun_list.get(index).title;
                        } else if (lirun_list.size() > 0) {
                            return lirun_list.get(0).title;
                        } else {
                            return "数据";
                        }
                    } catch (Exception e) {
                        Log.e("ChartError", "Error in value formatter", e);
                        return "数据";
                    }
                }
            });

            // 10. 图例设置
            Legend l = barChart5.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

            // 11. 强制Y轴从0开始
            barChart5.getAxisLeft().setAxisMinimum(0f);

            // 12. 刷新图表
            barChart5.invalidate();

            Log.d("ChartDebug", "Chart 5 setup completed successfully");

        } catch (Exception e) {
            Log.e("ChartError", "Error in setLiRunHeJi", e);
            if (barChart5 != null) {
                barChart5.setNoDataText("图表加载失败: " + e.getMessage());
                barChart5.invalidate();
            }
        }
    }

    private void setXianJinLiuLiang(){

        barChart6=findViewById(R.id.bc_6);
        xianjin_list = new ArrayList<>();

        xianjin_list.add(new DictModel("投资结余",list6_1.get(0).getLoad(),list6_1.get(0).getBorrowed()));
        xianjin_list.add(new DictModel("筹资结余",list6_2.get(0).getLoad(),list6_2.get(0).getBorrowed()));
        xianjin_list.add(new DictModel("经营结余",list6_3.get(0).getLoad(),list6_3.get(0).getBorrowed()));

        nianchuXianJinList = new ArrayList<>();
        nianmoXianJinList = new ArrayList<>();

        for(int i=0;i<xianjin_list.size();i++){
            nianchuXianJinList.add(new BarEntry((float) (i+1),toFloat(xianjin_list.get(i).boyCnt)));
            nianmoXianJinList.add(new BarEntry((float) (i+1),toFloat(xianjin_list.get(i).girlCnt)));
        }


        BarDataSet barDataSet=new BarDataSet(nianchuXianJinList,"月");
        barDataSet.setColor(Color.parseColor("#0066CC"));    //为第一组柱子设置颜色
        BarDataSet barDataSet2=new BarDataSet(nianmoXianJinList,"年");
        barDataSet2.setColor(Color.parseColor("#66CCFF"));   //为第二组柱子设置颜色
        BarData barData=new BarData(barDataSet);   //加上第一组
        barData.addDataSet(barDataSet2);    //加上第二组   （多组也可以用同样的方法）

        barDataSet.setHighLightAlpha(0);
        barDataSet2.setHighLightAlpha(0);

        barChart6.setData(
                barData);

        barData.setBarWidth(0.2f);//柱子的宽度
        //重点！   三个参数   分别代表   X轴起点     组与组之间的间隔      组内柱子的间隔
        barData.groupBars(1f,0.4f,0.1f);

        barChart6.getXAxis().setCenterAxisLabels(true);   //设置柱子（柱子组）居中对齐X轴上的点
        barChart6.setScaleEnabled(false);//禁止缩放
        barChart6.getXAxis().setAxisMaximum(nianchuXianJinList.size()+1);   //X轴最大数值
        barChart6.getXAxis().setAxisMinimum(1);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        barChart6.getXAxis().setLabelCount(nianmoXianJinList.size()+1,false);
        barChart6.getDescription().setEnabled(false);    //右下角一串英文字母不显示
        barChart6.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);   //X轴的位置设置为下  默认为上
        barChart6.getAxisRight().setEnabled(false);     //右侧Y轴不显示   默认为显示
        XAxis xl = barChart6.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);

        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if((int)value<=xianjin_list.size() && (int)value-1 > 0){
                    return xianjin_list.get((int) value-1).title;
                }else {
                    return xianjin_list.get(0).title;
                }
            }
        });

        Legend l = barChart6.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        barChart6.invalidate();
    }

    private void initList() {
        sel_button.setEnabled(false);
        start_dateText = start_date.getText().toString();
        end_dateText = end_date.getText().toString();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));

        if(start_dateText.equals("")){
            start_dateText =  formatter.format(date) + "-01-01";
        }
        if(end_dateText.equals("")){
            end_dateText =  formatter.format(date) + "-12-31";
        }

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                setNianChuYuE();
                setPingZhengJinE();
                setKeMuYuE();
                setZiChanFuZhai();
                setLiRunHeJi();
                setXianJinLiuLiang();
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                    list1 = yhFinanceKeMuZongZhangService.getChartList1(yhFinanceUser.getCompany());
                    list2 = yhFinanceKeMuZongZhangService.getChartList2(yhFinanceUser.getCompany(),start_dateText,end_dateText);
                    list3 = yhFinanceKeMuZongZhangService.getChartList3(yhFinanceUser.getCompany());
                    list4_1 = yhFinanceKeMuZongZhangService.getChartList4_01(yhFinanceUser.getCompany(),start_dateText);
                    list4_2 = yhFinanceKeMuZongZhangService.getChartList4_02(yhFinanceUser.getCompany(),start_dateText);
                    list4_3 = yhFinanceKeMuZongZhangService.getChartList4_03(yhFinanceUser.getCompany(),start_dateText);
                    list5 = yhFinanceKeMuZongZhangService.getChartList5(yhFinanceUser.getCompany(),start_dateText);
                    list6_1 = yhFinanceKeMuZongZhangService.getChartList6_1(yhFinanceUser.getCompany(),start_dateText);
                    list6_2 = yhFinanceKeMuZongZhangService.getChartList6_2(yhFinanceUser.getCompany(),start_dateText);
                    list6_3 = yhFinanceKeMuZongZhangService.getChartList6_3(yhFinanceUser.getCompany(),start_dateText);

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ZhiNengKanBanActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    class DictModel{
        public String title;
        public BigDecimal boyCnt;
        public BigDecimal girlCnt;

        public DictModel(String title,BigDecimal boyCnt, BigDecimal girlCnt) {
            this.title = title;
            this.boyCnt = boyCnt;
            this.girlCnt = girlCnt;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                initList();
            }
        }
    }
}
