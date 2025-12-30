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
import com.example.myapplication.jxc.entity.YhJinXiaoCunCaiGou;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunKeHu;

import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunCaiGouService;
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

public class CaiGouChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunCaiGouService yhJinXiaoCunCaiGOuService;
    private YhJinXiaoCunKeHuService yhJinXiaoCunKeHuService;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;


    private String churuku;
    private TextView kehu_type;

    private EditText orderid;
    private Spinner kehu;
    private Spinner isRukuSpinner;

    private List<YhJinXiaoCunJiChuZiLiao> mingxiList;
    private List<YhJinXiaoCunCaiGou> list;

    private String[] kehu_array;
    private String[] isRukuArray = {"是", "否"}; // 是否入库选项

    public CaiGouChangeActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caigou_change);

        kehu_type = findViewById(R.id.kehu_type);

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
        yhJinXiaoCunCaiGOuService = new YhJinXiaoCunCaiGouService();
        yhJinXiaoCunKeHuService = new YhJinXiaoCunKeHuService();
        yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService(); // 初始化mingxi服务

        orderid = findViewById(R.id.orderid);
        kehu = findViewById(R.id.kehu);
        isRukuSpinner = findViewById(R.id.is_ruku_spinner); // 初始化是否入库Spinner

        init();
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
        // 初始化是否入库Spinner
        ArrayAdapter<String> isRukuAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, isRukuArray);
        isRukuSpinner.setAdapter(isRukuAdapter);
        // 默认选择"是"
        isRukuSpinner.setSelection(0);

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
                    adapter = new ArrayAdapter<String>(CaiGouChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);

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
                    ToastUtil.show(CaiGouChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(CaiGouChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                boolean mainResult = yhJinXiaoCunCaiGOuService.caigou(list);

                // 检查是否入库选择
                String isRuku = isRukuSpinner.getSelectedItem().toString();
                boolean mingxiResult = true;

                if ("是".equals(isRuku)) {
                    // 如果选择"是"，向mingxi表插入数据
                    Log.d("InsertDebug", "选择入库，开始向mingxi表插入数据");
                    mingxiResult = insertToMingXiTable();
                } else {
                    Log.d("InsertDebug", "选择不入库，跳过mingxi表插入");
                }

                Message msg = new Message();
                msg.obj = mainResult && mingxiResult; // 两个操作都成功才算成功
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 向yh_jinxiaocun_mingxi表插入数据
     */
    private boolean insertToMingXiTable() {
        try {
            List<YhJinXiaoCunMingXi> mingxiList = new ArrayList<>();

            for (YhJinXiaoCunCaiGou caiGou : list) {
                // 创建mingxi实体对象
                YhJinXiaoCunMingXi mingxi = new YhJinXiaoCunMingXi();

                // 设置字段
                mingxi.setCplb(caiGou.getCplb());
                mingxi.setCpname(caiGou.getCpname());
                mingxi.setCpsj(caiGou.getCpsj());
                mingxi.setCpsl(caiGou.getCpsl());
                mingxi.setOrderid(caiGou.getOrderid());
                mingxi.setShijian(caiGou.getShijian());
                mingxi.setSpDm(caiGou.getSpDm());
                mingxi.setShou_h(caiGou.getShou_h());
                mingxi.setZhName(caiGou.getZhName());
                mingxi.setGsName(caiGou.getGsName());
                mingxi.setcangku(caiGou.getcangku());

                mingxiList.add(mingxi);

                Log.d("MingXiInsert",
                        "准备插入明细：订单=" + mingxi.getOrderid() +
                                ", 商品=" + mingxi.getCpname() +
                                ", 数量=" + mingxi.getCpsl());
            }

            // 调用Service批量插入数据
            boolean result = yhJinXiaoCunMingXiService.insert_caigou(mingxiList);

            Log.d("MingXiInsert", "批量插入结果: " + result + ", 记录数: " + mingxiList.size());
            return result;

        } catch (Exception e) {
            Log.e("MingXiInsert", "插入mingxi表异常：" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkForm() {
        list = new ArrayList<>();

        if (orderid.getText().toString().equals("")) {
            ToastUtil.show(CaiGouChangeActivity.this, "请输入订单号");
            return false;
        }
        // 获取是否入库的选择
        String isRuku = isRukuSpinner.getSelectedItem().toString();


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat spd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();


        for (int i = 0; i < mingxiList.size(); i++) {
            YhJinXiaoCunCaiGou yhJinXiaoCunCaiGou = new YhJinXiaoCunCaiGou();
            yhJinXiaoCunCaiGou.setCplb(mingxiList.get(i).getLeiBie());
            yhJinXiaoCunCaiGou.setCpname(mingxiList.get(i).getName());
            yhJinXiaoCunCaiGou.setCpsj(mingxiList.get(i).getJine());
            yhJinXiaoCunCaiGou.setCpsl(mingxiList.get(i).getNum());
            yhJinXiaoCunCaiGou.setMxtype(churuku);
            yhJinXiaoCunCaiGou.setOrderid(orderid.getText().toString());
            yhJinXiaoCunCaiGou.setShijian(spd.format(date));
            yhJinXiaoCunCaiGou.setSpDm(mingxiList.get(i).getSpDm());
            yhJinXiaoCunCaiGou.setShou_h(kehu.getSelectedItem().toString());

            yhJinXiaoCunCaiGou.setZhName(yhJinXiaoCunUser.getName());
            yhJinXiaoCunCaiGou.setGsName(yhJinXiaoCunUser.getGongsi());
            yhJinXiaoCunCaiGou.setcangku(mingxiList.get(i).getcangku());
            if ("是".equals(isRuku)) {
                yhJinXiaoCunCaiGou.setRuku("已入库");
            } else {
                yhJinXiaoCunCaiGou.setRuku("");  // 空字符串
            }
            Log.e("FormDebug", "设置后的cangku: " + mingxiList.get(i).getcangku());
            list.add(yhJinXiaoCunCaiGou);
        }

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
