package com.example.myapplication.jxc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RukuActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 100;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;

    private List<YhJinXiaoCunJiChuZiLiao> list;


    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruku);

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        listView = findViewById(R.id.product_list);

        initList();


    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
                list = yhJinXiaoCunJiChuZiLiaoService.getList(yhJinXiaoCunUser.getGongsi());

                if (list == null) return;

                List<HashMap<String, Object>> data = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("name", list.get(i).getName());
                    item.put("spDm", list.get(i).getSpDm());
                    item.put("leiBie", list.get(i).getLeiBie());
                    item.put("danWei", list.get(i).getDanWei());
                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(RukuActivity.this, data, R.layout.ruku_row, new String[]{"name", "spDm", "leiBie", "danWei"}, new int[]{R.id.name, R.id.spDm, R.id.leiBie, R.id.danWei}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        //HorizontalScrollView horizontalScrollView = (HorizontalScrollView) view.getChildAt(0);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);

                        //linearLayout.setOnClickListener(onClick());
                        //linearLayout.setOnLongClickListener(onLongClickListener());
                        linearLayout.setTag(position);
                        Button btn = (Button) view.getChildAt(1);
                        //btn.setOnClickListener(onItemClick());
                        btn.setTag(position);
                        return view;
                    }
                };
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }



}
