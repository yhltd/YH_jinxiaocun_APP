package com.example.myapplication.fenquan.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.EChartOptionUtil;
import com.example.myapplication.EChartView;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.entity.Workbench;
import com.example.myapplication.fenquan.service.WorkbenchService;
import com.example.myapplication.finance.activity.VoucherSummaryChangeActivity;
import com.example.myapplication.renshi.activity.BuMenHuiZongActivity;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@SuppressLint("SetJavaScriptEnabled")
public class GongSiChartActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private Renyuan renyuan;
    private WorkbenchService workbenchService;

    private Handler mHandler =  new Handler();
    private Runnable mRunnable = new Runnable() {
        public void run() {
            //为了方便 查看，我们用Log打印出来
            initList();
        }
    };

    private EditText start_date;
    private EditText stop_date;
    private Spinner class_select;
    private Button sel_button;
    private EChartView barChart;
    private Button clear_button;
    private String start_dateText;
    private String stop_dateText;
    private String class_selectText;

    private Object[] data1;
    private Object[] data2;

    private List<Workbench> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongsi_chart);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        workbenchService = new WorkbenchService();

        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
        class_select = findViewById(R.id.class_select);
        barChart = findViewById(R.id.barChart);
        sel_button = findViewById(R.id.sel_button);
        clear_button = findViewById(R.id.clear_button);
        clear_button.setOnClickListener(clearClick());
        String[] class_selectArray = getResources().getStringArray(R.array.column_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, class_selectArray);
        class_select.setAdapter(adapter);

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

        sel_button.setOnClickListener(selClick());
        sel_button.callOnClick();
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
        stop_dateText = stop_date.getText().toString();
        class_selectText = class_select.getSelectedItem().toString();

        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100-12-31";
        }

        if(start_dateText.compareTo(stop_dateText) > 0){
            ToastUtil.show(GongSiChartActivity.this, "开始日期不能晚于结束日期");
            return;
        }
        sel_button.setEnabled(false);
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                barChart.refreshEchartsWithOption(EChartOptionUtil.getBarChartOptions(data1, data2));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    workbenchService = new WorkbenchService();
                    list = workbenchService.getChart(renyuan.getB(), start_dateText ,stop_dateText);
                    if(list == null){
                        if(class_selectText.equals("A列-Z列")){
                            data1 = new Object[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                            data2 = new Object[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
                        }else if(class_selectText.equals("AA列-AZ列")){
                            data1 = new Object[]{"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
                            data2 = new Object[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
                        }else if(class_selectText.equals("BA列-BZ列")){
                            data1 = new Object[]{"BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ"};
                            data2 = new Object[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
                        }else if(class_selectText.equals("CA列-CV列")){
                            data1 = new Object[]{"CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV"};
                            data2 = new Object[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
                        }else if(class_selectText.equals("A列-CV列")){
                            data1 = new Object[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ","BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ","CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV"};
                            data2 = new Object[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
                        }
                    }else{
                        if(class_selectText.equals("A列-Z列")){
                            data1 = new Object[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                            data2 = new Object[]{list.get(0).getA(),list.get(0).getB(),list.get(0).getC(),list.get(0).getD(),list.get(0).getE(),list.get(0).getF(),list.get(0).getG(),list.get(0).getH(),list.get(0).getI(),list.get(0).getJ(),list.get(0).getK(),list.get(0).getL(),list.get(0).getM(),list.get(0).getN(),list.get(0).getO(),list.get(0).getP(),list.get(0).getQ(),list.get(0).getR(),list.get(0).getS(),list.get(0).getT(),list.get(0).getU(),list.get(0).getV(),list.get(0).getW(),list.get(0).getX(),list.get(0).getY(),list.get(0).getZ()};
                        }else if(class_selectText.equals("AA列-AZ列")){
                            data1 = new Object[]{"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
                            data2 = new Object[]{list.get(0).getAa(),list.get(0).getAb(),list.get(0).getAc(),list.get(0).getAd(),list.get(0).getAe(),list.get(0).getAf(),list.get(0).getAg(),list.get(0).getAh(),list.get(0).getAi(),list.get(0).getAj(),list.get(0).getAk(),list.get(0).getAl(),list.get(0).getAm(),list.get(0).getAn(),list.get(0).getAo(),list.get(0).getAp(),list.get(0).getAq(),list.get(0).getAr(),list.get(0).getAss(),list.get(0).getAt(),list.get(0).getAu(),list.get(0).getAv(),list.get(0).getAw(),list.get(0).getAx(),list.get(0).getAy(),list.get(0).getAz()};
                        }else if(class_selectText.equals("BA列-BZ列")){
                            data1 = new Object[]{"BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ"};
                            data2 = new Object[]{list.get(0).getBa(),list.get(0).getBb(),list.get(0).getBc(),list.get(0).getBd(),list.get(0).getBe(),list.get(0).getBf(),list.get(0).getBg(),list.get(0).getBh(),list.get(0).getBi(),list.get(0).getBj(),list.get(0).getBk(),list.get(0).getBl(),list.get(0).getBm(),list.get(0).getBn(),list.get(0).getBo(),list.get(0).getBp(),list.get(0).getBq(),list.get(0).getBr(),list.get(0).getBs(),list.get(0).getBt(),list.get(0).getBu(),list.get(0).getBv(),list.get(0).getBw(),list.get(0).getBx(),list.get(0).getByy(),list.get(0).getBz()};
                        }else if(class_selectText.equals("CA列-CV列")){
                            data1 = new Object[]{"CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV"};
                            data2 = new Object[]{list.get(0).getCa(),list.get(0).getCb(),list.get(0).getCc(),list.get(0).getCd(),list.get(0).getCe(),list.get(0).getCf(),list.get(0).getCg(),list.get(0).getCh(),list.get(0).getCi(),list.get(0).getCj(),list.get(0).getCk(),list.get(0).getCl(),list.get(0).getCm(),list.get(0).getCn(),list.get(0).getCo(),list.get(0).getCp(),list.get(0).getCq(),list.get(0).getCr(),list.get(0).getCs(),list.get(0).getCt(),list.get(0).getCu(),list.get(0).getCv()};
                        }else if(class_selectText.equals("A列-CV列")){
                            data1 = new Object[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ","BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ","CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV"};
                            data2 = new Object[]{list.get(0).getA(),list.get(0).getB(),list.get(0).getC(),list.get(0).getD(),list.get(0).getE(),list.get(0).getF(),list.get(0).getG(),list.get(0).getH(),list.get(0).getI(),list.get(0).getJ(),list.get(0).getK(),list.get(0).getL(),list.get(0).getM(),list.get(0).getN(),list.get(0).getO(),list.get(0).getP(),list.get(0).getQ(),list.get(0).getR(),list.get(0).getS(),list.get(0).getT(),list.get(0).getU(),list.get(0).getV(),list.get(0).getW(),list.get(0).getX(),list.get(0).getY(),list.get(0).getZ(),list.get(0).getAa(),list.get(0).getAb(),list.get(0).getAc(),list.get(0).getAd(),list.get(0).getAe(),list.get(0).getAf(),list.get(0).getAg(),list.get(0).getAh(),list.get(0).getAi(),list.get(0).getAj(),list.get(0).getAk(),list.get(0).getAl(),list.get(0).getAm(),list.get(0).getAn(),list.get(0).getAo(),list.get(0).getAp(),list.get(0).getAq(),list.get(0).getAr(),list.get(0).getAss(),list.get(0).getAt(),list.get(0).getAu(),list.get(0).getAv(),list.get(0).getAw(),list.get(0).getAx(),list.get(0).getAy(),list.get(0).getAz(),list.get(0).getBa(),list.get(0).getBb(),list.get(0).getBc(),list.get(0).getBd(),list.get(0).getBe(),list.get(0).getBf(),list.get(0).getBg(),list.get(0).getBh(),list.get(0).getBi(),list.get(0).getBj(),list.get(0).getBk(),list.get(0).getBl(),list.get(0).getBm(),list.get(0).getBn(),list.get(0).getBo(),list.get(0).getBp(),list.get(0).getBq(),list.get(0).getBr(),list.get(0).getBs(),list.get(0).getBt(),list.get(0).getBu(),list.get(0).getBv(),list.get(0).getBw(),list.get(0).getBx(),list.get(0).getByy(),list.get(0).getBz(),list.get(0).getCa(),list.get(0).getCb(),list.get(0).getCc(),list.get(0).getCd(),list.get(0).getCe(),list.get(0).getCf(),list.get(0).getCg(),list.get(0).getCh(),list.get(0).getCi(),list.get(0).getCj(),list.get(0).getCk(),list.get(0).getCl(),list.get(0).getCm(),list.get(0).getCn(),list.get(0).getCo(),list.get(0).getCp(),list.get(0).getCq(),list.get(0).getCr(),list.get(0).getCs(),list.get(0).getCt(),list.get(0).getCu(),list.get(0).getCv()};
                        }
                    }

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(GongSiChartActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String mon = "";
                String day = "";
                if(monthOfYear + 1 < 10){
                    mon = "0" + (monthOfYear + 1);
                }else{
                    mon = "" + (monthOfYear + 1);
                }
                if(dayOfMonth < 10){
                    day = "0" + dayOfMonth;
                }else{
                    day = "" + dayOfMonth;
                }
                editText.setText(year + "-" + mon + "-" + day);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.post(mRunnable);
            }
        };
    }

    public View.OnClickListener clearClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空搜索框的值
                start_date.setText("");
                stop_date.setText("");
            }
        };
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


    @Override
    protected void onDestroy() {
        //将线程销毁掉
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }


}
