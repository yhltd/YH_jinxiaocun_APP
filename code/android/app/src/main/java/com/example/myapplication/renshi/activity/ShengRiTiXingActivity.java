package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.activity.GongZuoTaiActivity;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiShengRiTiXing;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.renshi.service.YhRenShiUserService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ShengRiTiXingActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiUserService yhRenShiUserService;
    private ListView listView;
    private EditText this_date;
    private String this_dateText;
    private int this_year;
    private int this_day;
    private Button sel_button;
    List<YhRenShiUser> list;
    List<YhRenShiShengRiTiXing> list1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shengritixing);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.shengritixing_list);
        this_date = findViewById(R.id.this_date);
        showDateOnClick(this_date);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

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
        sel_button.setEnabled(false);
        this_dateText = this_date.getText().toString();
        if(this_dateText.equals("")){
            Calendar now = Calendar.getInstance();
            String moth = (now.get(Calendar.MONTH) + 1) + "";
            this_day = now.getActualMaximum(Calendar.DAY_OF_MONTH);
            this_dateText = moth;
            this_year = now.get(Calendar.YEAR);
        }else{
            this_dateText = this_dateText + "-1";
            Calendar now = Calendar.getInstance();
            now.set(Calendar.YEAR, Integer.parseInt(this_dateText.split("-")[0]));
            now.set(Calendar.MONTH, Integer.parseInt(this_dateText.split("-")[1]) - 1);
            now.set(Calendar.DAY_OF_MONTH, 1);
            this_day = now.getActualMaximum(Calendar.DAY_OF_MONTH);
            this_year = now.get(Calendar.YEAR);
            this_dateText = this_dateText.split("-")[1];
        }
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhRenShiUserService = new YhRenShiUserService();
                    list = yhRenShiUserService.monthBirthdayList(yhRenShiUser.getL(),this_dateText);
                    list1 = new ArrayList<>();
                    if (list == null) return;

                    String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
                    YhRenShiShengRiTiXing this_month = new YhRenShiShengRiTiXing();
                    YhRenShiShengRiTiXing this_name = new YhRenShiShengRiTiXing();
                    for (int i = 1; i <= this_day; i++) {
                        Calendar now = Calendar.getInstance();
                        now.set(Calendar.YEAR, this_year);
                        now.set(Calendar.MONTH, Integer.parseInt(this_dateText) - 1);
                        now.set(Calendar.DAY_OF_MONTH, i);
                        int weekday = now.get(Calendar.DAY_OF_WEEK) - 1;// 1是星期日
                        String cWeekDay = weeks[weekday];
                        if(cWeekDay.equals("星期一")){
                            this_month.setZhou1(String.valueOf(i));
                            String nameStr = "";
                            for(int j=0; j<list.size(); j++){
                                if(!list.get(j).getQ().equals("") && list.get(j).getQ().split("/").length == 3 ){
                                    if(String.valueOf(i).equals(list.get(j).getQ().split("/")[2])){
                                        if(nameStr.equals("")){
                                            nameStr = list.get(j).getB();
                                        }else{
                                            nameStr += "," + list.get(j).getB();
                                        }
                                    }
                                }
                            }
                            this_name.setZhou1(nameStr);
                        }else if(cWeekDay.equals("星期二")){
                            this_month.setZhou2(String.valueOf(i));
                            String nameStr = "";
                            for(int j=0; j<list.size(); j++){
                                if(!list.get(j).getQ().equals("") && list.get(j).getQ().split("/").length == 3 ){
                                    if(String.valueOf(i).equals(list.get(j).getQ().split("/")[2])){
                                        if(nameStr.equals("")){
                                            nameStr = list.get(j).getB();
                                        }else{
                                            nameStr += "," + list.get(j).getB();
                                        }
                                    }
                                }
                            }
                            this_name.setZhou2(nameStr);
                        }else if(cWeekDay.equals("星期三")){
                            this_month.setZhou3(String.valueOf(i));
                            String nameStr = "";
                            for(int j=0; j<list.size(); j++){
                                if(!list.get(j).getQ().equals("") && list.get(j).getQ().split("/").length == 3 ){
                                    if(String.valueOf(i).equals(list.get(j).getQ().split("/")[2])){
                                        if(nameStr.equals("")){
                                            nameStr = list.get(j).getB();
                                        }else{
                                            nameStr += "," + list.get(j).getB();
                                        }
                                    }
                                }
                            }
                            this_name.setZhou3(nameStr);
                        }else if(cWeekDay.equals("星期四")){
                            this_month.setZhou4(String.valueOf(i));
                            String nameStr = "";
                            for(int j=0; j<list.size(); j++){
                                if(!list.get(j).getQ().equals("") && list.get(j).getQ().split("/").length == 3 ){
                                    if(String.valueOf(i).equals(list.get(j).getQ().split("/")[2])){
                                        if(nameStr.equals("")){
                                            nameStr = list.get(j).getB();
                                        }else{
                                            nameStr += "," + list.get(j).getB();
                                        }
                                    }
                                }
                            }
                            this_name.setZhou4(nameStr);
                        }else if(cWeekDay.equals("星期五")){
                            this_month.setZhou5(String.valueOf(i));
                            String nameStr = "";
                            for(int j=0; j<list.size(); j++){
                                if(!list.get(j).getQ().equals("") && list.get(j).getQ().split("/").length == 3 ){
                                    if(String.valueOf(i).equals(list.get(j).getQ().split("/")[2])){
                                        if(nameStr.equals("")){
                                            nameStr = list.get(j).getB();
                                        }else{
                                            nameStr += "," + list.get(j).getB();
                                        }
                                    }
                                }
                            }
                            this_name.setZhou5(nameStr);
                        }else if(cWeekDay.equals("星期六")){
                            this_month.setZhou6(String.valueOf(i));
                            String nameStr = "";
                            for(int j=0; j<list.size(); j++){
                                if(!list.get(j).getQ().equals("") && list.get(j).getQ().split("/").length == 3 ){
                                    if(String.valueOf(i).equals(list.get(j).getQ().split("/")[2])){
                                        if(nameStr.equals("")){
                                            nameStr = list.get(j).getB();
                                        }else{
                                            nameStr += "," + list.get(j).getB();
                                        }
                                    }
                                }
                            }
                            this_name.setZhou6(nameStr);
                        }else if(cWeekDay.equals("星期日")){
                            this_month.setZhou7(String.valueOf(i));
                            String nameStr = "";
                            for(int j=0; j<list.size(); j++){
                                if(!list.get(j).getQ().equals("") && list.get(j).getQ().split("/").length == 3 ){
                                    if(String.valueOf(i).equals(list.get(j).getQ().split("/")[2])){
                                        if(nameStr.equals("")){
                                            nameStr = list.get(j).getB();
                                        }else{
                                            nameStr += "," + list.get(j).getB();
                                        }
                                    }
                                }
                            }
                            this_name.setZhou7(nameStr);
                            if(this_month.getZhou1() == null){
                                this_month.setZhou1("");
                            }
                            if(this_month.getZhou2() == null){
                                this_month.setZhou2("");
                            }
                            if(this_month.getZhou3() == null){
                                this_month.setZhou3("");
                            }
                            if(this_month.getZhou4() == null){
                                this_month.setZhou4("");
                            }
                            if(this_month.getZhou5() == null){
                                this_month.setZhou5("");
                            }
                            if(this_month.getZhou6() == null){
                                this_month.setZhou6("");
                            }
                            list1.add(this_month);
                            list1.add(this_name);
                            this_month = new YhRenShiShengRiTiXing();
                            this_name = new YhRenShiShengRiTiXing();
                        }
                    }
                    if(this_month.getZhou1() == null){
                        this_month.setZhou1("");
                    }
                    if(this_month.getZhou2() == null){
                        this_month.setZhou2("");
                    }
                    if(this_month.getZhou3() == null){
                        this_month.setZhou3("");
                    }
                    if(this_month.getZhou4() == null){
                        this_month.setZhou4("");
                    }
                    if(this_month.getZhou5() == null){
                        this_month.setZhou5("");
                    }
                    if(this_month.getZhou6() == null){
                        this_month.setZhou6("");
                    }
                    if(this_month.getZhou7() == null){
                        this_month.setZhou7("");
                    }
                    list1.add(this_month);
                    list1.add(this_name);
                    this_month = new YhRenShiShengRiTiXing();
                    this_name = new YhRenShiShengRiTiXing();

                    for (int i = 0; i < list1.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("zhou1", list1.get(i).getZhou1());
                        item.put("zhou2", list1.get(i).getZhou2());
                        item.put("zhou3", list1.get(i).getZhou3());
                        item.put("zhou4", list1.get(i).getZhou4());
                        item.put("zhou5", list1.get(i).getZhou5());
                        item.put("zhou6", list1.get(i).getZhou6());
                        item.put("zhou7", list1.get(i).getZhou7());
                        data.add(item);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(ShengRiTiXingActivity.this, data, R.layout.shengritixing_row, new String[]{"zhou1","zhou2","zhou3","zhou4","zhou5","zhou6","zhou7"}, new int[]{R.id.zhou1, R.id.zhou2, R.id.zhou3, R.id.zhou4, R.id.zhou5, R.id.zhou6, R.id.zhou7}) {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ShengRiTiXingActivity.this, new DatePickerDialog.OnDateSetListener() {
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
