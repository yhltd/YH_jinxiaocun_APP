package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunKeHuService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KuCunDiaoboChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private YhJinXiaoCunKeHuService yhJinXiaoCunKeHuService;

    private String churuku;
    private TextView kehu_type;

    private EditText orderid;
    private Spinner kehu;
    private EditText cangku2;
    private List<YhJinXiaoCunJiChuZiLiao> mingxiList;
    private List<YhJinXiaoCunMingXi> list;

    private String[] kehu_array;

    public KuCunDiaoboChangeActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kucundiaobo_change);

//        kehu_type = findViewById(R.id.kehu_type);

        Intent intent = getIntent();
        churuku = intent.getStringExtra("churuku");

        mingxiList = (List<YhJinXiaoCunJiChuZiLiao>) getIntent().getSerializableExtra("jczlList");

//        if ("".equals(churuku)) {
//            kehu_type.setText("供应商:");
//        } else {
//            kehu_type.setText("客户:");
//        }

        // 打印接收到的cangku数据
        Log.e("CangkuDebug", "=== RukuChangeActivity接收到的数据 ===");
        if (mingxiList != null) {
            Log.e("CangkuDebug", "接收到的记录数量: " + mingxiList.size());
            for (int i = 0; i < mingxiList.size(); i++) {
                YhJinXiaoCunJiChuZiLiao item = mingxiList.get(i);
                Log.e("CangkuDebug", "接收的第" + (i+1) + "条 - cangku: " + item.getcangku() +
                        ", 商品: " + item.getName() +
                        ", 数量: " + item.getNum());
            }
        } else {
            Log.e("CangkuDebug", "接收到的jczlList为null");
        }


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(churuku);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
        yhJinXiaoCunKeHuService = new YhJinXiaoCunKeHuService();


        orderid = findViewById(R.id.orderid);
//        kehu = findViewById(R.id.kehu);
        cangku2 = findViewById(R.id.cangku2);
//        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                kehu.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {

                        List<YhJinXiaoCunKeHu> kehuList = yhJinXiaoCunKeHuService.getListByGys(yhJinXiaoCunUser.getGongsi(), "");
                        kehu_array = new String[kehuList.size()];
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array[i] = kehuList.get(i).getBeizhu();

                    }
                    adapter = new ArrayAdapter<String>(KuCunDiaoboChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void insertClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KuCunDiaoboChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KuCunDiaoboChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunMingXiService.diaobo(list);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        list = new ArrayList<>();

        if (orderid.getText().toString().equals("")) {
            ToastUtil.show(KuCunDiaoboChangeActivity.this, "请输入订单号");
            return false;
        }

        if (cangku2.getText().toString().equals("")) {
            ToastUtil.show(KuCunDiaoboChangeActivity.this, "请输入目标仓库");
            return false;
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();


        for (int i = 0; i < mingxiList.size(); i++) {
            YhJinXiaoCunMingXi yhJinXiaoCunMingXi = new YhJinXiaoCunMingXi();
            yhJinXiaoCunMingXi.setCplb(mingxiList.get(i).getLeiBie());
            yhJinXiaoCunMingXi.setCpname(mingxiList.get(i).getName());
            yhJinXiaoCunMingXi.setCpsj(mingxiList.get(i).getJine());
            yhJinXiaoCunMingXi.setCpsl(mingxiList.get(i).getNum());
            yhJinXiaoCunMingXi.setMxtype(churuku);
            yhJinXiaoCunMingXi.setOrderid(orderid.getText().toString());
            yhJinXiaoCunMingXi.setShijian(spd.format(date));
            yhJinXiaoCunMingXi.setSpDm(mingxiList.get(i).getSpDm());
//            yhJinXiaoCunMingXi.setShou_h(kehu.getSelectedItem().toString());
            yhJinXiaoCunMingXi.setcangku2(cangku2.getText().toString());
            yhJinXiaoCunMingXi.setZhName(yhJinXiaoCunUser.getName());
            yhJinXiaoCunMingXi.setGsName(yhJinXiaoCunUser.getGongsi());
            yhJinXiaoCunMingXi.setcangku(mingxiList.get(i).getcangku());
            Log.e("FormDebug", "设置后的cangku: " + mingxiList.get(i).getcangku());
            list.add(yhJinXiaoCunMingXi);
        }

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
