package com.example.myapplication.finance.activity;

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
import android.widget.Button;
import android.widget.EditText;
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
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceKeMuZongZhangService;
import com.example.myapplication.jxc.activity.BiJiActivity;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeMuZongZhangActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXian yhFinanceQuanXian;

    private YhFinanceKeMuZongZhangService yhFinanceKeMuZongZhangService;


    private EditText code_text;
    private ListView listView;
    private Spinner type_select;
    private String type_selectText;
    private Button sel_button;

    List<YhFinanceKeMuZongZhang> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kemuzongzhang);
        type_select = findViewById(R.id.type_select);

        String[] type_selectArray = getResources().getStringArray(R.array.class_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type_selectArray);
        type_select.setAdapter(adapter);

        type_select.setOnItemSelectedListener(new typeSelectSelectedListener());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        code_text = findViewById(R.id.code_text);
        listView = findViewById(R.id.kemuzongzhang_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());

        sel_button.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceQuanXian = myApplication.getYhFinanceQuanXian();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            type_selectText = type_select.getItemAtPosition(position).toString();
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    listView.setAdapter(StringUtils.cast(msg.obj));
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    if (type_selectText.equals("资产类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(1,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("负债类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(2,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("权益类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(3,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("成本类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(4,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("损益类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(5,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("code", list.get(i).getCode());
                        item.put("name", list.get(i).getName());
                        item.put("grade", list.get(i).getGrade());
                        if(list.get(i).getName1() == null){
                            list.get(i).setName1(list.get(i).getName2());
                        }
                        if(list.get(i).getName2() == null){
                            list.get(i).setName2(list.get(i).getName3());
                        }
                        if(!list.get(i).getName1().equals(list.get(i).getName2())){
                            list.get(i).setName1(list.get(i).getName1() + "-" + list.get(i).getName2());
                        }
                        if(!list.get(i).getName2().equals(list.get(i).getName3())){
                            list.get(i).setName1(list.get(i).getName1() + "-" + list.get(i).getName3());
                        }
                        item.put("name1", list.get(i).getName1());
                        item.put("direction_text", list.get(i).getDirection_text());
                        item.put("money", list.get(i).getMoney());
                        item.put("mingxi", list.get(i).getMingxi());
                        item.put("load", list.get(i).getLoad());
                        item.put("borrowed", list.get(i).getBorrowed());
                        data.add(item);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(KeMuZongZhangActivity.this, data, R.layout.kemuzongzhang_row, new String[]{"code","name","grade","name1","direction_text","money","mingxi","load","borrowed"}, new int[]{R.id.code,R.id.name,R.id.grade,R.id.name1,R.id.direction_text,R.id.money,R.id.mingxi,R.id.load,R.id.borrowed}) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                            LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                            linearLayout.setOnLongClickListener(onItemLongClick());
                            linearLayout.setOnClickListener(updateClick());
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
        public void onNothingSelected(AdapterView<?> parent) {}
    }


    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!yhFinanceQuanXian.getKmzzDelete().equals("是")){
                    ToastUtil.show(KeMuZongZhangActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(KeMuZongZhangActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(KeMuZongZhangActivity.this, "删除成功");
                            initList();
                        }
                        return true;
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.obj = yhFinanceKeMuZongZhangService.delete(list.get(position).getId());
                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("科目代码:");
                        xiangQingYe.setB_title("科目名称:");
                        xiangQingYe.setC_title("科目等级:");
                        xiangQingYe.setD_title("科目全称:");
                        xiangQingYe.setE_title("方向:");
                        xiangQingYe.setF_title("借贷合计:");
                        xiangQingYe.setG_title("明细:");
                        xiangQingYe.setH_title("年初借金:");
                        xiangQingYe.setI_title("年初贷金:");

                        xiangQingYe.setA(list.get(position).getCode());
                        xiangQingYe.setB(list.get(position).getName());
                        xiangQingYe.setC(list.get(position).getGrade());
                        xiangQingYe.setD(list.get(position).getName1());
                        xiangQingYe.setE(list.get(position).getDirection_text());
                        xiangQingYe.setF(list.get(position).getMoney());
                        xiangQingYe.setG(list.get(position).getMingxi());
                        xiangQingYe.setH(list.get(position).getLoad().toString());
                        xiangQingYe.setI(list.get(position).getBorrowed().toString());

                        Intent intent = new Intent(KeMuZongZhangActivity.this, XiangQingYeActivity.class);
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

                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
            }
        };
    }


    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!yhFinanceQuanXian.getKmzzUpdate().equals("是")){
                    ToastUtil.show(KeMuZongZhangActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(KeMuZongZhangActivity.this, KeMuZongZhangChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        if(!yhFinanceQuanXian.getKmzzAdd().equals("是")){
            ToastUtil.show(KeMuZongZhangActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(KeMuZongZhangActivity.this, KeMuZongZhangChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }


    private void initList() {
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    if (type_selectText.equals("资产类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(1,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("负债类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(2,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("权益类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(3,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("成本类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(4,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("损益类")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(5,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("code", list.get(i).getCode());
                        item.put("name", list.get(i).getName());
                        item.put("grade", list.get(i).getGrade());
                        if(list.get(i).getName1() == null){
                            list.get(i).setName1(list.get(i).getName2());
                        }
                        if(list.get(i).getName2() == null){
                            list.get(i).setName2(list.get(i).getName3());
                        }
                        if(!list.get(i).getName1().equals(list.get(i).getName2())){
                            list.get(i).setName1(list.get(i).getName1() + "-" + list.get(i).getName2());
                        }
                        if(!list.get(i).getName2().equals(list.get(i).getName3())){
                            list.get(i).setName1(list.get(i).getName1() + "-" + list.get(i).getName3());
                        }
                        item.put("name1", list.get(i).getName1());
                        item.put("direction_text", list.get(i).getDirection_text());
                        item.put("money", list.get(i).getMoney());
                        item.put("mingxi", list.get(i).getMingxi());
                        item.put("load", list.get(i).getLoad());
                        item.put("borrowed", list.get(i).getBorrowed());
                        data.add(item);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(KeMuZongZhangActivity.this, data, R.layout.kemuzongzhang_row, new String[]{"code","name","grade","name1","direction_text","money","mingxi","load","borrowed"}, new int[]{R.id.code,R.id.name,R.id.grade,R.id.name1,R.id.direction_text,R.id.money,R.id.mingxi,R.id.load,R.id.borrowed}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
                        linearLayout.setOnClickListener(updateClick());
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
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
