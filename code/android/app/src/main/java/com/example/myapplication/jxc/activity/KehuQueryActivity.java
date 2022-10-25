package com.example.myapplication.jxc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KehuQueryActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private Spinner kehu_spinner;
    private ListView listView;

    private String kehuText;


    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kehu_query);

        //初始化控件
        kehu_spinner = findViewById(R.id.kehu_spinner);
        listView = findViewById(R.id.kehu_list);

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        initList();
        kehu_spinner.setOnItemSelectedListener(new kehuItemSelectedListener());

    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    kehu_spinner.setAdapter((SpinnerAdapter) msg.obj);
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
                    List<String> list = yhJinXiaoCunMingXiService.getKehu(yhJinXiaoCunUser.getGongsi());
                    adapter = new ArrayAdapter<String>(KehuQueryActivity.this, android.R.layout.simple_spinner_item, list);
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

    private class kehuItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            kehuText = kehu_spinner.getItemAtPosition(position).toString();
            Handler kehuHandler = new Handler(new Handler.Callback() {
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
                        List<YhJinXiaoCunMingXi> list = yhJinXiaoCunMingXiService.getKeHuQuery(yhJinXiaoCunUser.getGongsi(), kehuText);
                        if (list == null) return;

                        for (int i = 0; i < list.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("kehu", list.get(i).getShou_h());
                            item.put("spDm", list.get(i).getSpDm());
                            item.put("cpname", list.get(i).getCpname());
                            item.put("cplb", list.get(i).getCplb());
                            item.put("ruku_num", list.get(i).getRukuNum());
                            item.put("ruku_price", list.get(i).getRukuPrice());
                            data.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SimpleAdapter adapter = new SimpleAdapter(KehuQueryActivity.this, data, R.layout.kehu_query_row, new String[]{"kehu", "spDm", "cpname", "cplb", "ruku_price", "ruku_num", "ruku_price"}, new int[]{R.id.kehu, R.id.spDm, R.id.cpname, R.id.cplb, R.id.ruku_num, R.id.ruku_price}) {
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
                    kehuHandler.sendMessage(msg);

                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


}
