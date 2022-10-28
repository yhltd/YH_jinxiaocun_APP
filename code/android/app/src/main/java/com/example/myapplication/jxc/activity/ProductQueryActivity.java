package com.example.myapplication.jxc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductQueryActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;

    private Spinner product_spinner;
    private ListView listView;

    private String productText;
    private List<YhJinXiaoCunJiChuZiLiao> productList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_query);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        product_spinner = findViewById(R.id.product_spinner);
        listView = findViewById(R.id.product_list);

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        initList();
        product_spinner.setOnItemSelectedListener(new productItemSelectedListener());
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
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    product_spinner.setAdapter((SpinnerAdapter) msg.obj);
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                SpinnerAdapter adapter = null;
                try {
                    yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
                    List<String> list = yhJinXiaoCunMingXiService.getProduct(yhJinXiaoCunUser.getGongsi());
                    adapter = new ArrayAdapter<String>(ProductQueryActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                    if (list.size() > 0) {
                        msg.obj = adapter;
                    } else {
                        msg.obj = null;
                    }
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class productItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            productText = product_spinner.getItemAtPosition(position).toString();
            Handler productHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    listView.setAdapter(StringUtils.cast(msg.obj));
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    try {
                        List<YhJinXiaoCunMingXi> list = yhJinXiaoCunMingXiService.getProductQuery(yhJinXiaoCunUser.getGongsi(), productText);
                        if (list == null) return;

                        for (int i = 0; i < list.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("spDm", list.get(i).getSpDm());
                            item.put("cpname", list.get(i).getCpname());
                            item.put("cplb", list.get(i).getCplb());
                            item.put("ruku_num", list.get(i).getRukuNum());
                            item.put("ruku_price", list.get(i).getRukuPrice());
                            item.put("chuku_num", list.get(i).getChukuNum());
                            item.put("chuku_price", list.get(i).getChukuPrice());
                            data.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SimpleAdapter adapter = new SimpleAdapter(ProductQueryActivity.this, data, R.layout.product_query_row, new String[]{"spDm", "cpname", "cplb", "ruku_num", "ruku_price", "chuku_num", "chuku_price"}, new int[]{R.id.spDm, R.id.cpname, R.id.cplb, R.id.ruku_num, R.id.ruku_price, R.id.chuku_num, R.id.chuku_price}) {
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
                    productHandler.sendMessage(msg);

                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

}
