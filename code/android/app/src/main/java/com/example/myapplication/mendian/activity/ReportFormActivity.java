package com.example.myapplication.mendian.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianReportForm;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianReportFormService;
import com.example.myapplication.utils.StringUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReportFormActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianReportFormService yhMendianReportFormService;
    private EditText start_date;
    private EditText end_date;
    private String start_dateText;
    private String end_dateText;
    private ListView reportform_list;
    private Button sel_button;
    private Float xiaofei_num;
    private Float shishou_num;
    private Float youhui_num;
    private BarChart mBarChart;
    List<YhMendianReportForm> list;
    List<YhMendianReportForm> list2;

//    private boolean isExpanded = true;
//    private LinearLayout seleOut;
//    private Button toggleButton;
//    private int originalHeight;
//    private boolean isAnimating = false; // 防止重复点击

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportform);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        reportform_list = findViewById(R.id.reportform_list);
        mBarChart = findViewById(R.id.bc_1);
        yhMendianReportFormService = new YhMendianReportFormService();

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

    private void initList() {
        start_dateText = start_date.getText().toString();
        end_dateText = end_date.getText().toString();

        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }
        if(end_dateText.equals("")){
            end_dateText = "2100-12-31";
        }

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                reportform_list.setAdapter(StringUtils.cast(msg.obj));
                initBarChart();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhMendianReportFormService = new YhMendianReportFormService();
                    list = yhMendianReportFormService.getList(start_dateText,end_dateText,yhMendianUser.getCompany());
                    list2 = yhMendianReportFormService.getList2(start_dateText,end_dateText,yhMendianUser.getCompany());
                    if (list == null && list2 == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                HashMap<String, Object> item = new HashMap<>();
                if(list != null){
                    item.put("xfje", list.get(0).getXfje());
                    item.put("ssje", list.get(0).getSsje());
                    item.put("yhje", list.get(0).getYhje());
                    xiaofei_num = Float.parseFloat(list.get(0).getXfje());
                    shishou_num = Float.parseFloat(list.get(0).getSsje());
                    youhui_num = Float.parseFloat(list.get(0).getYhje());
                }else{
                    item.put("xfje", 0);
                    item.put("ssje", 0);
                    item.put("yhje", 0);
                    xiaofei_num = 0f;
                    shishou_num = 0f;
                    youhui_num = 0f;
                }

                if(list2 != null){
                    item.put("hyzs", list2.get(0).getHuiyuan_sum());
                    item.put("xdrs", list2.get(1).getHuiyuan_sum());
                    item.put("ddzs", list2.get(2).getHuiyuan_sum());
                }else{
                    item.put("hyzs", 0);
                    item.put("xdrs", 0);
                    item.put("ddzs", 0);
                }

                data.add(item);

                SimpleAdapter adapter = new SimpleAdapter(ReportFormActivity.this, data, R.layout.reportform_row, new String[]{"hyzs","xdrs","ddzs","xfje","ssje","yhje"}, new int[]{R.id.hyzs,R.id.xdrs,R.id.ddzs,R.id.xfje,R.id.ssje,R.id.yhje}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setTag(position);
                        return view;
                    }
                };
                Message msg = new Message();
                msg.obj = adapter;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ReportFormActivity.this, new DatePickerDialog.OnDateSetListener() {
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


    private void initBarChart() {
        //没有数据显示这里面的
        mBarChart.setNoDataText("没有数据哦");
        // 设置是否可以缩放
        mBarChart.setScaleEnabled(false);
        //设置双击不放大
        mBarChart.setDoubleTapToZoomEnabled(false);
        //设置控件之间的间距
        mBarChart.setExtraOffsets(20,20,20,20);
        //获取XAxis 获取XAxis  setDrawGridLines:设置绘图网格线
        mBarChart.getXAxis().setDrawGridLines(false);
        //获取描述,是否显示右下角描述
        mBarChart.getDescription().setEnabled(false);
        //获取图例,是否显示图例
        mBarChart.getLegend().setEnabled(false);
        // 设置执行的动画,XY轴
        mBarChart.animateXY(2500,2500);
        //设置倾斜角度  setLabelRotationAngle:设置旋转角度
//        mBarChart.getXAxis().setLabelRotationAngle(-30);
        //获取X轴
        XAxis xAxis = mBarChart.getXAxis();
        //设置X轴的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴min数值
        xAxis.setAxisMinimum(0f);
        //设置X轴Max数值
        xAxis.setAxisMaximum(3f);
        //设置可视范围,0-5,可以防止X轴数据过长导致遮挡其他X轴数据
        mBarChart.setVisibleXRange(0,5);
        //设置X轴最大范围
        xAxis.setLabelCount(3);
        //使得左边柱子完全显示
        xAxis.setAxisMinimum(0.5f);
        //不绘制格网线
//        xAxis.setDrawGridLines(false);
        //设置与顶部的距离
//        mBarChart.setExtraTopOffset(30);
        //设置最小间隔，防止当放大时，出现重复标签。
        xAxis.setGranularity(1);
        //设置x轴显示的值
        final String labelName[] = {"","消费金额", "实收金额", "优惠金额"};
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                //判断value的值是否小于数组长度
                if((int)value<labelName.length){
                    return labelName[(int) value];
                }else{
                    return "";
                }
            }
        });
        //设置X轴标签与Y轴的间距
        xAxis.setYOffset(0);
        //设置y轴,y轴有两条,分别是左边和右边,右边一边不常用可以隐藏
        //获取右边的y轴
        YAxis axisRight = mBarChart.getAxisRight();
        //将右边的y轴设置为不显示
        axisRight.setEnabled(false);
        //获取左边的y轴
        YAxis axisLeft = mBarChart.getAxisLeft();
//        //设置y轴最大值
//        axisLeft.setAxisMaximum(40f);
        //设置y轴最低值
        axisLeft.setAxisMinimum(0f);
        //设置y标签字体大小
        axisLeft.setTextSize(10f);
        //设置BarEntry集合,用来存放柱状图的数值
        List<BarEntry>barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,xiaofei_num));
        barEntries.add(new BarEntry(2,shishou_num));
        barEntries.add(new BarEntry(3,youhui_num));
        //将数据添加到BarDataSet中,
        BarDataSet barDataSet1 = new BarDataSet(barEntries,"");
        //柱状图数值颜色
        barDataSet1.setValueTextColor(Color.RED);
        //柱状图数值的大小(文字大小)
        barDataSet1.setValueTextSize(10f);
        //柱状图对应的颜色
        barDataSet1.setColor(Color.parseColor("#03A9F4"));
        BarData barData = new BarData(barDataSet1);
        //设置柱子的宽度
        barData.setBarWidth(0.3f);
        //将数据添加到组件中
        mBarChart.setData(barData);
        //刷新图表,
        mBarChart.invalidate();
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
