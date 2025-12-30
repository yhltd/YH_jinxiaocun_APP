package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.jxc.service.YhJinXiaoCunCaiGouService;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunKeHuService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class CaiGouTuiHuoChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunMingXi yhJinXiaoCunMingXi;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private YhJinXiaoCunCaiGouService yhJinXiaoCunCaiGouService;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;
    private YhJinXiaoCunKeHuService yhJinXiaoCunKeHuService;

    private EditText orderid;
    private Spinner spDm;
    private EditText cpname;
    private EditText cplb;
    private EditText cpsj;
    private EditText cpsl;
    private EditText cangku;
    private Spinner mxtype;
    private Spinner ruku;
    private Spinner shou_h;
    private LinearLayout qr_code_line;
    private ImageView qr_code;

    List<YhJinXiaoCunJiChuZiLiao> getList;
    List<YhJinXiaoCunCaiGou> list;


    String[] spDm_array;
    String[] mxtype_array = new String[]{"采购"};
    String[] ruku_array = new String[]{"是","否"};
    String[] shou_h_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caigoutuihuo_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
        yhJinXiaoCunCaiGouService = new YhJinXiaoCunCaiGouService();
        yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
        yhJinXiaoCunKeHuService = new YhJinXiaoCunKeHuService();

        orderid = findViewById(R.id.orderid);
        spDm = findViewById(R.id.spDm);
        ruku = findViewById(R.id.ruku);
        cpname = findViewById(R.id.cpname);
        cplb = findViewById(R.id.cplb);
        cpsj = findViewById(R.id.cpsj);
        cpsl = findViewById(R.id.cpsl);
        cangku = findViewById(R.id.cangku);
        mxtype = findViewById(R.id.mxtype);
        shou_h = findViewById(R.id.shou_h);
        qr_code_line = findViewById(R.id.qr_code_line);
        qr_code = findViewById(R.id.qr_code);

        SpinnerAdapter adapter = new ArrayAdapter<String>(CaiGouTuiHuoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, mxtype_array);

        if (mxtype != null) {
            mxtype.setAdapter(adapter);
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhJinXiaoCunMingXi = (YhJinXiaoCunMingXi) myApplication.getObj();
            init();
//            init2();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            qr_code_line.setVisibility(View.VISIBLE);
            orderid.setText(yhJinXiaoCunMingXi.getOrderid());
            cpname.setText(yhJinXiaoCunMingXi.getCpname());
            cplb.setText(yhJinXiaoCunMingXi.getCplb());
            cpsj.setText(yhJinXiaoCunMingXi.getCpsj());
            cpsl.setText(yhJinXiaoCunMingXi.getCpsl());
            cangku.setText(yhJinXiaoCunMingXi.getcangku());
            QRcode qrcode = new QRcode();
            Bitmap bitmap= qrcode.qrcode(yhJinXiaoCunMingXi.getOrderid(),"qrcode");
            qr_code.setImageBitmap(bitmap);

            spDm.setOnItemSelectedListener(new spDmItemSelectedListener());
            mxtype.setOnItemSelectedListener(new mxtypeItemSelectedListener());
            mxtype.setSelection(getMxtypePosition(yhJinXiaoCunMingXi.getMxtype()), true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearClick(View v) {
        orderid.setText("");
        cpname.setText("");
        cplb.setText("");
        cpsj.setText("");
        cpsl.setText("");
        cangku.setText("");
    }

    private class spDmItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Handler systemHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    if (getList != null && getList.size() != 0) {
                        cpname.setText(getList.get(0).getName());
                        cplb.setText(getList.get(0).getLeiBie());
                    }
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取选择的项的值
                    String text = spDm.getItemAtPosition(position).toString();
                    try {
                        getList = yhJinXiaoCunJiChuZiLiaoService.getListByCpid(yhJinXiaoCunUser.getGongsi(), text);
                        Message msg = new Message();
                        msg.obj = getList;
                        systemHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }



    private class mxtypeItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Handler systemHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    shou_h.setAdapter(StringUtils.cast(msg.obj));
                    shou_h.setSelection(getSpDmPosition(yhJinXiaoCunMingXi.getShou_h()));
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Adapter adapter=null;
                    //获取选择的项的值
                    String text = mxtype.getItemAtPosition(position).toString();
                    try {
                        List<YhJinXiaoCunKeHu> shouhList=new ArrayList<>();
                        if (text.equals("采购")){
                            shouhList = yhJinXiaoCunKeHuService.getListByGys(yhJinXiaoCunUser.getGongsi(), "");
                        }else{
                            shouhList = yhJinXiaoCunKeHuService.getListByKehu(yhJinXiaoCunUser.getGongsi(), "");
                        }
                        if(shouhList!=null){
                            shou_h_array=new String[shouhList.size()];
                            for (int i = 0; i < shouhList.size(); i++) {
                                shou_h_array[i] = shouhList.get(i).getBeizhu();
                            }
                        }
                        adapter = new ArrayAdapter<String>(CaiGouTuiHuoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, shou_h_array);
                        Message msg = new Message();
                        msg.obj = adapter;
                        systemHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public void init() {
        // 初始化是否入库Spinner
        ArrayAdapter<String> isRukuAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ruku_array);
        ruku.setAdapter(isRukuAdapter);
        // 默认选择"是"
        ruku.setSelection(1);
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    spDm.setAdapter(StringUtils.cast(msg.obj));
                    spDm.setSelection(getSpDmPosition(yhJinXiaoCunMingXi.getSpDm()));
                } else if (msg.what == 1) {
                    shou_h.setAdapter(StringUtils.cast(msg.obj));
                    shou_h.setSelection(getShouhPosition(yhJinXiaoCunMingXi.getShou_h()));
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter1 = null;
                SpinnerAdapter adapter2 = null;
                try {
                    // Initialize spDm
                    List<String> spDmList = yhJinXiaoCunJiChuZiLiaoService.getCpid(yhJinXiaoCunUser.getGongsi());
                    if (spDmList != null) {
                        spDm_array = new String[spDmList.size() + 1];
                        spDm_array[0] = "";
                        for (int i = 0; i < spDmList.size(); i++) {
                            spDm_array[i + 1] = spDmList.get(i);
                        }
                    }
                    adapter1 = new ArrayAdapter<String>(CaiGouTuiHuoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, spDm_array);

                    // Initialize shou_h
                    List<YhJinXiaoCunKeHu> shouhList = yhJinXiaoCunKeHuService.getListByGys(yhJinXiaoCunUser.getGongsi(), "");
                    List<YhJinXiaoCunKeHu> shouhList2 = yhJinXiaoCunKeHuService.getListByKehu(yhJinXiaoCunUser.getGongsi(), "");

                    List<YhJinXiaoCunKeHu> list1 = new ArrayList<>();
                    if (shouhList != null) {
                        for (int i = 0; i < shouhList.size(); i++) {
                            list1.add(shouhList.get(i));
                        }
                    }

                    if (shouhList2 != null) {
                        for (int i = 0; i < shouhList2.size(); i++) {
                            list1.add(shouhList2.get(i));
                        }
                    }

                    if (list1 != null) {
                        shou_h_array = new String[list1.size()];
                        for (int i = 0; i < list1.size(); i++) {
                            shou_h_array[i] = list1.get(i).getBeizhu();
                        }
                    }
                    adapter2 = new ArrayAdapter<String>(CaiGouTuiHuoChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, shou_h_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg1 = new Message();
                msg1.what = 0;
                msg1.obj = adapter1;
                listLoadHandler.sendMessage(msg1);

                Message msg2 = new Message();
                msg2.what = 1;
                msg2.obj = adapter2;
                listLoadHandler.sendMessage(msg2);
            }
        }).start();
    }


//    public void init1() {
//
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                spDm.setAdapter(StringUtils.cast(msg.obj));
//                spDm.setSelection(getSpDmPosition(yhJinXiaoCunMingXi.getSpDm()));
//
//                return true;
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SpinnerAdapter adapter = null;
//                try {
//                    List<String> spDmList = yhJinXiaoCunJiChuZiLiaoService.getCpid(yhJinXiaoCunUser.getGongsi());
//                    if (spDmList != null) {
//                        spDm_array = new String[spDmList.size() + 1];
//                        spDm_array[0] = "";
//                        for (int i = 0; i < spDmList.size(); i++) {
//                            spDm_array[i + 1] = spDmList.get(i);
//                        }
//                    }
//                    adapter = new ArrayAdapter<String>(MingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, spDm_array);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.obj = adapter;
//                listLoadHandler.sendMessage(msg);
//            }
//        }).start();
//    }
//
//    public void init2() {
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                shou_h.setAdapter(StringUtils.cast(msg.obj));
//                shou_h.setSelection(getShouhPosition(yhJinXiaoCunMingXi.getShou_h()));
//                return true;
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SpinnerAdapter adapter = null;
//                try {
//                    List<YhJinXiaoCunKeHu> shouhList = yhJinXiaoCunKeHuService.getListByGys(yhJinXiaoCunUser.getGongsi(), "");
//                    List<YhJinXiaoCunKeHu> shouhList2 = yhJinXiaoCunKeHuService.getListByKehu(yhJinXiaoCunUser.getGongsi(), "");
//
//                    List<YhJinXiaoCunKeHu> list1 = new ArrayList<>();
//                    if(shouhList!=null){
//                        for (int i = 0; i < shouhList.size(); i++) {
//                            list1.add(shouhList.get(i));
//                        }
//                    }
//
//                    if(shouhList2!=null){
//                        for (int i = 0; i < shouhList2.size(); i++) {
//                            list1.add(shouhList2.get(i));
//                        }
//                    }
//
//                    if (list1 != null) {
//                        shou_h_array = new String[list1.size()];
//                        for (int i = 0; i < list1.size(); i++) {
//                            shou_h_array[i] = list1.get(i).getBeizhu();
//                        }
//                    }
//                    adapter = new ArrayAdapter<String>(MingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, shou_h_array);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.obj = adapter;
//                listLoadHandler.sendMessage(msg);
//            }
//        }).start();
//    }

    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                boolean mainResult = yhJinXiaoCunMingXiService.updatecgwrk(yhJinXiaoCunMingXi);

                // 检查是否入库选择
                String isRuku = ruku.getSelectedItem().toString();
                boolean mingxiResult = true;
                boolean tuihuomingxiResult = true;
                if ("是".equals(isRuku)) {
                    // 如果选择"是"，向mingxi表插入数据
                    Log.d("InsertDebug", "选择入库，开始向mingxi表插入数据");
                    mingxiResult = insertCurrentToMingXiTable();
                    tuihuomingxiResult = insertCurrentToTuiHuoMingXiTable();
                } else {
                    Log.d("InsertDebug", "选择不入库，跳过mingxi表插入");
                }
                Message msg = new Message();
                msg.obj = mainResult && mingxiResult && tuihuomingxiResult; // 两个操作都成功才算成功
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    /**
     * 向yh_jinxiaocun_mingxi表插入数据
     */
    private boolean insertCurrentToMingXiTable() {
        try {
            Log.d("MingXiInsert", "开始创建明细对象...");

            // 创建mingxi实体对象
            YhJinXiaoCunMingXi mingxi = new YhJinXiaoCunMingXi();

            // 从当前编辑的数据设置字段
            mingxi.setOrderid(yhJinXiaoCunMingXi.getOrderid());
            mingxi.setSpDm(yhJinXiaoCunMingXi.getSpDm());
            mingxi.setCpname(yhJinXiaoCunMingXi.getCpname());
            mingxi.setCplb(yhJinXiaoCunMingXi.getCplb());
            mingxi.setCpsj(yhJinXiaoCunMingXi.getCpsj());
            mingxi.setCpsl(yhJinXiaoCunMingXi.getCpsl());
            mingxi.setcangku(yhJinXiaoCunMingXi.getcangku());
            mingxi.setShou_h(yhJinXiaoCunMingXi.getShou_h());

            // 根据insert_caigou方法，mxtype固定为"入库"
            // 注意：这里和之前的"采购"不同，根据insert_caigou方法中的硬编码"入库"
            mingxi.setMxtype("采购退货");

            // 设置公司信息和用户信息
            if (yhJinXiaoCunUser != null) {
                mingxi.setGsName(yhJinXiaoCunUser.getGongsi());
                mingxi.setZhName(yhJinXiaoCunUser.getName());
            } else {
                mingxi.setGsName("默认公司");
            }

            // 设置当前时间作为入库时间
            String currentTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            mingxi.setShijian(currentTime);

            // 注意：根据insert_caigou方法，SQL中硬编码mxtype为"入库"，所以这里不需要单独设置ruku字段
            // 如果需要保存入库状态到实体对象，可以保留

            Log.d("MingXiInsert",
                    "准备插入明细数据：" +
                            "\n订单号: " + mingxi.getOrderid() +
                            "\n商品代码: " + mingxi.getSpDm() +
                            "\n商品名称: " + mingxi.getCpname() +
                            "\n商品类别: " + mingxi.getCplb() +
                            "\n商品单价: " + mingxi.getCpsj() +
                            "\n商品数量: " + mingxi.getCpsl() +
                            "\n仓库: " + mingxi.getcangku() +
                            "\n收货方: " + mingxi.getShou_h() +
                            "\n明细类型: " + mingxi.getMxtype() +
                            "\n公司: " + mingxi.getGsName() +
                            "\n用户: " + mingxi.getZhName() +
                            "\n时间: " + mingxi.getShijian());

            // 创建列表，即使只有一条数据
            List<YhJinXiaoCunMingXi> mingxiList = new ArrayList<>();
            mingxiList.add(mingxi);

            // 调用Service的insert_caigou方法插入数据
            boolean result = yhJinXiaoCunMingXiService.insert_tuihuo(mingxiList);

            Log.d("MingXiInsert", "插入mingxi表结果: " + result);
            return result;

        } catch (Exception e) {
            Log.e("MingXiInsert", "插入mingxi表异常：" + e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }


    private boolean insertCurrentToTuiHuoMingXiTable() {
        try {
            Log.d("MingXiInsert", "开始创建明细对象...");

            // 创建mingxi实体对象
            YhJinXiaoCunMingXi mingxi = new YhJinXiaoCunMingXi();

            // 从当前编辑的数据设置字段
            mingxi.setOrderid(yhJinXiaoCunMingXi.getOrderid());
            mingxi.setSpDm(yhJinXiaoCunMingXi.getSpDm());
            mingxi.setCpname(yhJinXiaoCunMingXi.getCpname());
            mingxi.setCplb(yhJinXiaoCunMingXi.getCplb());
            mingxi.setCpsj(yhJinXiaoCunMingXi.getCpsj());
            mingxi.setCpsl(yhJinXiaoCunMingXi.getCpsl());
            mingxi.setcangku(yhJinXiaoCunMingXi.getcangku());
            mingxi.setShou_h(yhJinXiaoCunMingXi.getShou_h());

            // 根据insert_caigou方法，mxtype固定为"入库"
            // 注意：这里和之前的"采购"不同，根据insert_caigou方法中的硬编码"入库"
            mingxi.setMxtype("采购退货");

            // 设置公司信息和用户信息
            if (yhJinXiaoCunUser != null) {
                mingxi.setGsName(yhJinXiaoCunUser.getGongsi());
                mingxi.setZhName(yhJinXiaoCunUser.getName());
            } else {
                mingxi.setGsName("默认公司");
            }

            // 设置当前时间作为入库时间
            String currentTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            mingxi.setShijian(currentTime);

            // 注意：根据insert_caigou方法，SQL中硬编码mxtype为"入库"，所以这里不需要单独设置ruku字段
            // 如果需要保存入库状态到实体对象，可以保留

            Log.d("MingXiInsert",
                    "准备插入明细数据：" +
                            "\n订单号: " + mingxi.getOrderid() +
                            "\n商品代码: " + mingxi.getSpDm() +
                            "\n商品名称: " + mingxi.getCpname() +
                            "\n商品类别: " + mingxi.getCplb() +
                            "\n商品单价: " + mingxi.getCpsj() +
                            "\n商品数量: " + mingxi.getCpsl() +
                            "\n仓库: " + mingxi.getcangku() +
                            "\n收货方: " + mingxi.getShou_h() +
                            "\n明细类型: " + mingxi.getMxtype() +
                            "\n公司: " + mingxi.getGsName() +
                            "\n用户: " + mingxi.getZhName() +
                            "\n时间: " + mingxi.getShijian());

            // 创建列表，即使只有一条数据
            List<YhJinXiaoCunMingXi> mingxiList = new ArrayList<>();
            mingxiList.add(mingxi);

            // 调用Service的insert_caigou方法插入数据
            boolean result = yhJinXiaoCunCaiGouService.tuihuo(mingxiList);

            Log.d("MingXiInsert", "插入mingxi表结果: " + result);
            return result;

        } catch (Exception e) {
            Log.e("MingXiInsert", "插入mingxi表异常：" + e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }




    private boolean checkForm() {
        if (orderid.getText().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请输入订单号");
            return false;
        } else {
            yhJinXiaoCunMingXi.setOrderid(orderid.getText().toString());
        }

        if (spDm.getSelectedItem().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请选择商品代码");
            return false;
        } else {
            yhJinXiaoCunMingXi.setSpDm(spDm.getSelectedItem().toString());
        }

        if (cpname.getText().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请输入商品名称");
            return false;
        } else {
            yhJinXiaoCunMingXi.setCpname(cpname.getText().toString());
        }

        if (cplb.getText().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请输入产品类别");
            return false;
        } else {
            yhJinXiaoCunMingXi.setCplb(cplb.getText().toString());
        }

        if (cpsj.getText().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请输入价格");
            return false;
        } else {
            yhJinXiaoCunMingXi.setCpsj(cpsj.getText().toString());
        }

        if (cpsl.getText().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请输入数量");
            return false;
        } else {
            yhJinXiaoCunMingXi.setCpsl(cpsl.getText().toString());
        }

        if (cangku.getText().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请输入仓库");
            return false;
        } else {
            yhJinXiaoCunMingXi.setcangku(cangku.getText().toString());
        }

        if (mxtype.getSelectedItem().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请选择明细类型");
            return false;
        } else {
            yhJinXiaoCunMingXi.setMxtype(mxtype.getSelectedItem().toString());
        }

        if (shou_h.getSelectedItem().toString().equals("")) {
            ToastUtil.show(CaiGouTuiHuoChangeActivity.this, "请选择收/进货方");
            return false;
        } else {
            yhJinXiaoCunMingXi.setShou_h(shou_h.getSelectedItem().toString());
        }
        // 获取是否入库的选择
        String isRuku = ruku.getSelectedItem().toString();

        return true;
    }


    private int getSpDmPosition(String param) {
        if (spDm_array != null) {
            for (int i = 0; i < spDm_array.length; i++) {
                if (param.equals(spDm_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getMxtypePosition(String param) {
        if (mxtype_array != null) {
            for (int i = 0; i < mxtype_array.length; i++) {
                if (param.equals(mxtype_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getShouhPosition(String param) {
        if (shou_h_array != null) {
            for (int i = 0; i < shou_h_array.length; i++) {
                if (param.equals(shou_h_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

//    private class spdmItemSelectedListener implements AdapterView.OnItemSelectedListener {
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Handler systemHandler = new Handler(new Handler.Callback() {
//                @Override
//                public boolean handleMessage(@NonNull Message msg) {
//                    return true;
//                }
//            });
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //获取选择的项的值
//                    String text = spDm.getItemAtPosition(position).toString();
//                    try {
//                        yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
//                        List<YhJinXiaoCunJiChuZiLiao> getList = yhJinXiaoCunJiChuZiLiaoService.getListByCpid(yhJinXiaoCunUser.getGongsi(), text);
//                        if (getList.size() != 0) {
//                            cpname.setText(getList.get(0).getName());
//                            cplb.setText(getList.get(0).getLeiBie());
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    }

}
