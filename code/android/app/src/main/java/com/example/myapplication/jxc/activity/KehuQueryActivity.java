package com.example.myapplication.jxc.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KehuQueryActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private Spinner kehu_spinner;
    private ListView listView;

    private String kehuText;
    private List<YhJinXiaoCunMingXi> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kehu_query);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        kehu_spinner = findViewById(R.id.kehu_spinner);
        listView = findViewById(R.id.kehu_list);

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        initList();
        kehu_spinner.setOnItemSelectedListener(new kehuItemSelectedListener());

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
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    kehu_spinner.setAdapter((SpinnerAdapter) msg.obj);
                    LoadingDialog.getInstance(getApplicationContext()).dismiss();
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
                    adapter = new ArrayAdapter<String>(KehuQueryActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
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
                        list = yhJinXiaoCunMingXiService.getKeHuQuery(yhJinXiaoCunUser.getGongsi(), kehuText);
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

                    SimpleAdapter adapter = new SimpleAdapter(KehuQueryActivity.this, data, R.layout.kehu_query_row, new String[]{"kehu", "spDm", "cpname", "cplb", "ruku_num", "ruku_price"}, new int[]{R.id.kehu, R.id.spDm, R.id.cpname, R.id.cplb, R.id.ruku_num, R.id.ruku_price}) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                            LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                            linearLayout.setOnClickListener(updateClick());
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


    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KehuQueryActivity.this);
                int position = Integer.parseInt(view.getTag().toString());

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("客户/供应商:");
                        xiangQingYe.setB_title("商品代码:");
                        xiangQingYe.setC_title("商品名称:");
                        xiangQingYe.setD_title("商品类别:");
                        xiangQingYe.setE_title("入库/出库数量:");
                        xiangQingYe.setF_title("入库/出库金额:");

                        xiangQingYe.setA(list.get(position).getShou_h());
                        xiangQingYe.setB(list.get(position).getSpDm());
                        xiangQingYe.setC(list.get(position).getCpname());
                        xiangQingYe.setD(list.get(position).getCplb());
                        xiangQingYe.setE(list.get(position).getRukuNum());
                        xiangQingYe.setF(list.get(position).getRukuPrice());

                        Intent intent = new Intent(KehuQueryActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("确定查看明细？");
                builder.setTitle("提示");
                builder.show();
            }
        };
    }


}
