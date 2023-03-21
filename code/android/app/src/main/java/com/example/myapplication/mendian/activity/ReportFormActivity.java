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
import com.example.myapplication.mendian.entity.YhMendianReportForm;
import com.example.myapplication.mendian.entity.YhMendianRijiao;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianReportFormService;
import com.example.myapplication.mendian.service.YhMendianRijiaoService;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
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

    List<YhMendianReportForm> list;

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

        yhMendianReportFormService = new YhMendianReportFormService();

        //初始化控件
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
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
        start_dateText = start_date.getText().toString();
        end_dateText = end_date.getText().toString();

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                reportform_list.setAdapter(StringUtils.cast(msg.obj));
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
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("hyzs", list.get(i).getHyzs());
                    item.put("xdrs", list.get(i).getXdrs());
                    item.put("ddzs", list.get(i).getDdzs());
                    item.put("xfje", list.get(i).getXfje());
                    item.put("ssje", list.get(i).getSsje());
                    item.put("yhje", list.get(i).getYhje());

                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(ReportFormActivity.this, data, R.layout.reportform_row, new String[]{"hyzs","xdrs","ddzs","xfje","ssje","yhje"}, new int[]{R.id.hyzs,R.id.xdrs,R.id.ddzs,R.id.xfje,R.id.ssje,R.id.yhje}) {
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
