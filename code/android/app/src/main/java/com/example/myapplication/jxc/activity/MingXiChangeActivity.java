package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunKeHuService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MingXiChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunMingXi yhJinXiaoCunMingXi;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;
    private YhJinXiaoCunKeHuService yhJinXiaoCunKeHuService;

    private EditText orderid;
    private Spinner spDm;
    private EditText cpname;
    private EditText cplb;
    private EditText cpsj;
    private EditText cpsl;
    private Spinner mxtype;
    private Spinner shou_h;

    List<YhJinXiaoCunJiChuZiLiao> getList;


    String[] spDm_array;
    String[] mxtype_array = new String[]{"??????", "??????"};
    String[] shou_h_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mingxi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
        yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
        yhJinXiaoCunKeHuService = new YhJinXiaoCunKeHuService();

        orderid = findViewById(R.id.orderid);
        spDm = findViewById(R.id.spDm);
        cpname = findViewById(R.id.cpname);
        cplb = findViewById(R.id.cplb);
        cpsj = findViewById(R.id.cpsj);
        cpsl = findViewById(R.id.cpsl);
        mxtype = findViewById(R.id.mxtype);
        shou_h = findViewById(R.id.shou_h);

        SpinnerAdapter adapter = new ArrayAdapter<String>(MingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, mxtype_array);

        if (mxtype != null) {
            mxtype.setAdapter(adapter);
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhJinXiaoCunMingXi = (YhJinXiaoCunMingXi) myApplication.getObj();
            init1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            orderid.setText(yhJinXiaoCunMingXi.getOrderid());

            cpname.setText(yhJinXiaoCunMingXi.getCpname());
            cplb.setText(yhJinXiaoCunMingXi.getCplb());
            cpsj.setText(yhJinXiaoCunMingXi.getCpsj());
            cpsl.setText(yhJinXiaoCunMingXi.getCpsl());

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
                    //????????????????????????
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
                    //????????????????????????
                    String text = mxtype.getItemAtPosition(position).toString();
                    try {
                        List<YhJinXiaoCunKeHu> shouhList=new ArrayList<>();
                        if (text.equals("??????")){
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
                        adapter = new ArrayAdapter<String>(MingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, shou_h_array);
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

    public void init1() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                spDm.setAdapter(StringUtils.cast(msg.obj));
                spDm.setSelection(getSpDmPosition(yhJinXiaoCunMingXi.getSpDm()));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<String> spDmList = yhJinXiaoCunJiChuZiLiaoService.getCpid(yhJinXiaoCunUser.getGongsi());
                    if (spDmList != null) {
                        spDm_array = new String[spDmList.size()];
                        for (int i = 0; i < spDmList.size(); i++) {
                            spDm_array[i] = spDmList.get(i);
                        }
                    }
                    adapter = new ArrayAdapter<String>(MingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, spDm_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

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
                    ToastUtil.show(MingXiChangeActivity.this, "????????????");
                    back();
                } else {
                    ToastUtil.show(MingXiChangeActivity.this, "??????????????????????????????");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunMingXiService.update(yhJinXiaoCunMingXi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (orderid.getText().toString().equals("")) {
            ToastUtil.show(MingXiChangeActivity.this, "??????????????????");
            return false;
        } else {
            yhJinXiaoCunMingXi.setOrderid(orderid.getText().toString());
        }

        if (spDm.getSelectedItem().toString().equals("")) {
            ToastUtil.show(MingXiChangeActivity.this, "?????????????????????");
            return false;
        } else {
            yhJinXiaoCunMingXi.setSpDm(spDm.getSelectedItem().toString());
        }

        if (cpname.getText().toString().equals("")) {
            ToastUtil.show(MingXiChangeActivity.this, "?????????????????????");
            return false;
        } else {
            yhJinXiaoCunMingXi.setCpname(cpname.getText().toString());
        }

        if (cplb.getText().toString().equals("")) {
            ToastUtil.show(MingXiChangeActivity.this, "?????????????????????");
            return false;
        } else {
            yhJinXiaoCunMingXi.setCplb(cplb.getText().toString());
        }

        if (cpsj.getText().toString().equals("")) {
            ToastUtil.show(MingXiChangeActivity.this, "???????????????");
            return false;
        } else {
            yhJinXiaoCunMingXi.setCpsj(cpsj.getText().toString());
        }

        if (cpsl.getText().toString().equals("")) {
            ToastUtil.show(MingXiChangeActivity.this, "???????????????");
            return false;
        } else {
            yhJinXiaoCunMingXi.setCpsl(cpsl.getText().toString());
        }

        if (mxtype.getSelectedItem().toString().equals("")) {
            ToastUtil.show(MingXiChangeActivity.this, "?????????????????????");
            return false;
        } else {
            yhJinXiaoCunMingXi.setMxtype(mxtype.getSelectedItem().toString());
        }

        if (shou_h.getSelectedItem().toString().equals("")) {
            ToastUtil.show(MingXiChangeActivity.this, "????????????/?????????");
            return false;
        } else {
            yhJinXiaoCunMingXi.setShou_h(shou_h.getSelectedItem().toString());
        }

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
//                    //????????????????????????
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
