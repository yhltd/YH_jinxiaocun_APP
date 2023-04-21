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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceKeMuZongZhangService;
import com.example.myapplication.mendian.entity.YhMendianReportForm;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.math.BigDecimal;
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

        for(int i=0;i<list.size();i++){
            nianchuList.add(new BarEntry((float) (i+1),toFloat(list.get(i).boyCnt)));
            nianmoList.add(new BarEntry((float) (i+1),toFloat(list.get(i).girlCnt)));
        }


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

        for(int i=0;i<kemu_list.size();i++){
            nianchuKeMuList.add(new BarEntry((float) (i+1),toFloat(kemu_list.get(i).boyCnt)));
            nianmoKeMuList.add(new BarEntry((float) (i+1),toFloat(kemu_list.get(i).girlCnt)));
        }


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

    private void setLiRunHeJi(){

        barChart5=findViewById(R.id.bc_5);
        lirun_list = new ArrayList<>();

        lirun_list.add(new DictModel("收入",list5.get(0).getLoad(),list5.get(0).getBorrowed()));
        lirun_list.add(new DictModel("支出",list5.get(1).getLoad(),list5.get(1).getBorrowed()));

        nianchuLiRunList = new ArrayList<>();
        nianmoLiRunList = new ArrayList<>();

        for(int i=0;i<lirun_list.size();i++){
            nianchuLiRunList.add(new BarEntry((float) (i+1),toFloat(lirun_list.get(i).boyCnt)));
            nianmoLiRunList.add(new BarEntry((float) (i+1),toFloat(lirun_list.get(i).girlCnt)));
        }


        BarDataSet barDataSet=new BarDataSet(nianchuLiRunList,"月");
        barDataSet.setColor(Color.parseColor("#0066CC"));    //为第一组柱子设置颜色
        BarDataSet barDataSet2=new BarDataSet(nianmoLiRunList,"年");
        barDataSet2.setColor(Color.parseColor("#66CCFF"));   //为第二组柱子设置颜色
        BarData barData=new BarData(barDataSet);   //加上第一组
        barData.addDataSet(barDataSet2);    //加上第二组   （多组也可以用同样的方法）

        barDataSet.setHighLightAlpha(0);
        barDataSet2.setHighLightAlpha(0);

        barChart5.setData(
                barData);

        barData.setBarWidth(0.2f);//柱子的宽度
        //重点！   三个参数   分别代表   X轴起点     组与组之间的间隔      组内柱子的间隔
        barData.groupBars(1f,0.4f,0.1f);

        barChart5.getXAxis().setCenterAxisLabels(true);   //设置柱子（柱子组）居中对齐X轴上的点
        barChart5.setScaleEnabled(false);//禁止缩放
        barChart5.getXAxis().setAxisMaximum(nianchuLiRunList.size()+1);   //X轴最大数值
        barChart5.getXAxis().setAxisMinimum(1);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        barChart5.getXAxis().setLabelCount(nianmoLiRunList.size()+1,false);
        barChart5.getDescription().setEnabled(false);    //右下角一串英文字母不显示
        barChart5.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);   //X轴的位置设置为下  默认为上
        barChart5.getAxisRight().setEnabled(false);     //右侧Y轴不显示   默认为显示
        XAxis xl = barChart5.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);

        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if((int)value<=lirun_list.size() && (int)value-1 > 0){
                    return lirun_list.get((int) value-1).title;
                }else {
                    return lirun_list.get(0).title;
                }
            }
        });

        Legend l = barChart5.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        barChart5.invalidate();
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
