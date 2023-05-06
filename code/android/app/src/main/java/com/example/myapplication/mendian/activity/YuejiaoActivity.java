package com.example.myapplication.mendian.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianRijiao;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.entity.YhMendianYuejiao;
import com.example.myapplication.mendian.service.YhMendianRijiaoService;
import com.example.myapplication.mendian.service.YhMendianYuejiaoService;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class YuejiaoActivity extends AppCompatActivity {

private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianYuejiaoService yhMendianYuejiaoService;
    private EditText shouka;
    private EditText fukuan;
    private EditText chika;
    private EditText start_date;
    private EditText end_date;
    private String shoukaText;
    private String fukuanText;
    private String chikaText;
    private String start_dateText;
    private String end_dateText;
    private ListView Yuejiao_list;
    private Button sel_button;

    List<YhMendianYuejiao> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuejiao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhMendianYuejiaoService = new YhMendianYuejiaoService();

        //初始化控件
        shouka = findViewById(R.id.shouka);
        fukuan = findViewById(R.id.fukuan);
        chika = findViewById(R.id.chika);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        showDateOnClick(start_date);
        showDateOnClick(end_date);
        Yuejiao_list = findViewById(R.id.Yuejiao_list);
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
        shoukaText = shouka.getText().toString();
        fukuanText = fukuan.getText().toString();
        chikaText = chika.getText().toString();
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
                Yuejiao_list.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhMendianYuejiaoService = new YhMendianYuejiaoService();
                    list = yhMendianYuejiaoService.getList(shoukaText,fukuanText,chikaText,start_dateText,end_dateText,yhMendianUser.getCompany());
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("recipient", list.get(i).getRecipient());
                    item.put("cardholder", list.get(i).getCardholder());
                    item.put("drawee", list.get(i).getDrawee());
                    item.put("issuing_bank", list.get(i).getIssuing_bank());
                    item.put("bill_day", list.get(i).getBill_day());
                    item.put("repayment_date", list.get(i).getRepayment_date());
                    item.put("total", list.get(i).getTotal());
                    item.put("repayable", list.get(i).getRepayable());
                    item.put("balance", list.get(i).getBalance());
                    item.put("loan", list.get(i).getLoan());
                    item.put("repayment", list.get(i).getRepayment());
                    item.put("swipe", list.get(i).getSwipe());
                    item.put("balance_of_credit_card", list.get(i).getBalance_of_credit_card());
                    item.put("the_total_fee", list.get(i).getThe_total_fee());
                    item.put("collected_amount", list.get(i).getCollected_amount());
                    item.put("profit", list.get(i).getProfitfrom());

                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(YuejiaoActivity.this, data, R.layout.yuejiao_row, new String[]{"recipient","cardholder","drawee","issuing_bank","bill_day","repayment_date","total","repayable","balance","loan","repayment","swipe","balance_of_credit_card","the_total_fee","collected_amount","profit"}, new int[]{R.id.recipient,R.id.cardholder,R.id.drawee,R.id.issuing_bank,R.id.bill_day,R.id.repayment_date,R.id.total,R.id.repayable,R.id.balance,R.id.loan,R.id.repayment,R.id.swipe,R.id.balance_of_credit_card,R.id.the_total_fee,R.id.collected_amount,R.id.profit}) {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(YuejiaoActivity.this, new DatePickerDialog.OnDateSetListener() {
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
