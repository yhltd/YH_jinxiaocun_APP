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
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceKeMuZongZhangService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeMuZongZhangActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhFinanceUser yhFinanceUser;

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

        //???????????????
        code_text = findViewById(R.id.code_text);
        listView = findViewById(R.id.kemuzongzhang_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());

        sel_button.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();

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
            //????????????????????????
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
                    if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(1,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(2,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(3,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(4,yhFinanceUser.getCompany(),"");
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(KeMuZongZhangActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(KeMuZongZhangActivity.this, "????????????");
                            initList();
                        }
                        return true;
                    }
                });

                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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

                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("??????????????????");
                builder.setTitle("??????");
                builder.show();
                return true;
            }
        };
    }


    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        Intent intent = new Intent(KeMuZongZhangActivity.this, KeMuZongZhangChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
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
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(1,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(2,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(3,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list = yhFinanceKeMuZongZhangService.getList(4,yhFinanceUser.getCompany(),code_text.getText().toString());
                            if (list == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
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
