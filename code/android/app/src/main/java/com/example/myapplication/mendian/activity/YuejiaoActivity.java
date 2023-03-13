package com.example.myapplication.mendian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private void initList() {
        shoukaText = shouka.getText().toString();
        fukuanText = fukuan.getText().toString();
        chikaText = chika.getText().toString();
        start_dateText = start_date.getText().toString();
        end_dateText = end_date.getText().toString();

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
                    list = yhMendianYuejiaoService.getList(shoukaText,fukuanText,chikaText,yhMendianUser.getCompany());
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
                    item.put("profit", list.get(i).getProfit());

                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(YuejiaoActivity.this, data, R.layout.yuejiao_row, new String[]{"recipient","cardholder","drawee"}, new int[]{R.id.recipient,R.id.cardholder,R.id.drawee}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
//                        linearLayout.setOnLongClickListener(onItemLongClick());
//                        linearLayout.setOnClickListener(updateClick());
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
