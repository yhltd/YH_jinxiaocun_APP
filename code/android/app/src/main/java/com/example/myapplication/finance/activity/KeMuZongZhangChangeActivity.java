package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
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
import com.example.myapplication.finance.entity.YhFinanceKeMuZongZhang;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceKeMuZongZhangService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeMuZongZhangChangeActivity extends AppCompatActivity {

    private YhFinanceUser yhFinanceUser;
    private YhFinanceKeMuZongZhang yhFinanceKeMuZongZhang;
    private YhFinanceKeMuZongZhangService yhFinanceKeMuZongZhangService;

    private Spinner class1;
    private Spinner class2;
    private Spinner class3;
    private Spinner direction;
    private EditText code;
    private EditText name;
    private EditText load;
    private EditText borrowed;
    private String type_selectText;
    private String type_selectText1 = "";
    private String type_selectText2 = "";
    private String type_selectText3 = "";
    private String code_text1;
    private String code_text2;
    private String code_text3;
    private boolean panduan = false;

    List<YhFinanceKeMuZongZhang> list1;

    List<YhFinanceKeMuZongZhang> list2;

    List<YhFinanceKeMuZongZhang> list3;

    String[] class2_array;

    String[] class3_array;

    String[] fangxiang_array = {"借","贷"};

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kemuzongzhang_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();

        class1 = findViewById(R.id.class1);
        class2 = findViewById(R.id.class2);
        class3 = findViewById(R.id.class3);
        direction = findViewById(R.id.direction);
        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        load = findViewById(R.id.load);
        borrowed = findViewById(R.id.borrowed);

        String[] type_selectArray = getResources().getStringArray(R.array.class_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type_selectArray);
        class1.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fangxiang_array);
        direction.setAdapter(adapter);

        class1.setOnItemSelectedListener(new typeSelectSelectedListener01());

        class2.setOnItemSelectedListener(new typeSelectSelectedListener02());

        class3.setOnItemSelectedListener(new typeSelectSelectedListener03());

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhFinanceKeMuZongZhang = new YhFinanceKeMuZongZhang();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhFinanceKeMuZongZhang = (YhFinanceKeMuZongZhang) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            name.setText(yhFinanceKeMuZongZhang.getName());
            load.setText(yhFinanceKeMuZongZhang.getLoad().toString());
            borrowed.setText(yhFinanceKeMuZongZhang.getBorrowed().toString());
            if(yhFinanceKeMuZongZhang.isDirection()){
                direction.setSelection(0);
            }else{
                direction.setSelection(1);
            }
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
        code.setText("");
        name.setText("");
        load.setText("");
        borrowed.setText("");
    }


    private class typeSelectSelectedListener01 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            type_selectText = class1.getItemAtPosition(position).toString();
            type_selectText1 = class1.getItemAtPosition(position).toString();
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    SpinnerAdapter adapter_class2 = new ArrayAdapter<String>(KeMuZongZhangChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, class2_array);
                    class2.setAdapter(StringUtils.cast(adapter_class2));
                    code.setText(code_text1);
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    float this_code = 0;
                    if (type_selectText.equals("资产类")) {
                        this_code = 1001f;
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list1 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"1",4);
                            if (list1 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("负债类")) {
                        this_code = 2001f;
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list1 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"2",4);
                            if (list1 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("权益类")) {
                        this_code = 3001f;
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list1 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"3",4);
                            if (list1 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("成本类")) {
                        this_code = 4001f;
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list1 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"4",4);
                            if (list1 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("损益类")) {
                        this_code = 5001f;
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            list1 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"5",4);
                            if (list1 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    if (list1.size() > 0) {
                        class2_array = new String[list1.size() + 1];
                        class2_array[0] = "";
                        for (int i = 0; i < list1.size(); i++) {
                            class2_array[i+1] = list1.get(i).getName();
                            if(Float.parseFloat(list1.get(i).getCode()) >= this_code){
                                this_code = Float.parseFloat(list1.get(i).getCode()) + 1;
                            }
                        }
                    }

                    code_text1 = Float.toString(this_code).replace(".0","");

                    Message msg = new Message();
                    msg.obj = "";
                    listLoadHandler.sendMessage(msg);

                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }


    private class typeSelectSelectedListener02 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            type_selectText = class2.getItemAtPosition(position).toString();
            type_selectText2 = class2.getItemAtPosition(position).toString();
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    SpinnerAdapter adapter_class3 = new ArrayAdapter<String>(KeMuZongZhangChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, class3_array);
                    class3.setAdapter(StringUtils.cast(adapter_class3));
                    if(!type_selectText.equals("")){
                        code.setText(code_text2);
                    }else{
                        code.setText(code_text1);
                    }
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    float this_code = 0;
                    if (type_selectText1.equals("资产类")) {
                        this_code = 1001f;
                        for(int i=0; i<list1.size(); i++){
                            if(list1.get(i).getName().equals(class2.getSelectedItem())){
                                this_code = Float.parseFloat(list1.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class2.getSelectedItem().equals("")){
                                list2 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"1",6,Float.toString(this_code).replace(".0",""));
                            }else{
                                list2 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"1",4);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list2 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText1.equals("负债类")) {
                        this_code = 2001f;
                        for(int i=0; i<list1.size(); i++){
                            if(list1.get(i).getName().equals(class2.getSelectedItem())){
                                this_code = Float.parseFloat(list1.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class2.getSelectedItem().equals("")){
                                list2 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"2",6,Float.toString(this_code).replace(".0",""));
                            }else{
                                list2 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"2",4);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list2 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText1.equals("权益类")) {
                        this_code = 3001f;
                        for(int i=0; i<list1.size(); i++){
                            if(list1.get(i).getName().equals(class2.getSelectedItem())){
                                this_code = Float.parseFloat(list1.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class2.getSelectedItem().equals("")){
                                list2 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"3",6,Float.toString(this_code).replace(".0",""));
                            }else{
                                list2 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"3",4);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list2 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText1.equals("成本类")) {
                        this_code = 4001f;
                        for(int i=0; i<list1.size(); i++){
                            if(list1.get(i).getName().equals(class2.getSelectedItem())){
                                this_code = Float.parseFloat(list1.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class2.getSelectedItem().equals("")){
                                list2 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"4",6,Float.toString(this_code).replace(".0",""));
                            }else{
                                list2 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"4",4);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list2 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText1.equals("损益类")) {
                        this_code = 5001f;
                        for(int i=0; i<list1.size(); i++){
                            if(list1.get(i).getName().equals(class2.getSelectedItem())){
                                this_code = Float.parseFloat(list1.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class2.getSelectedItem().equals("")){
                                list2 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"5",6,Float.toString(this_code).replace(".0",""));
                            }else{
                                list2 = yhFinanceKeMuZongZhangService.getCodeList(yhFinanceUser.getCompany(),"5",4);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list2 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    if (list2.size() > 0) {
                        class3_array = new String[list2.size() + 1];
                        class3_array[0] = "";
                        for (int i = 0; i < list2.size(); i++) {
                            class3_array[i+1] = list2.get(i).getName();
                            if(Float.parseFloat(list2.get(i).getCode()) >= this_code){
                                this_code = Float.parseFloat(list2.get(i).getCode()) + 1;
                            }
                        }
                    }

                    code_text2 = Float.toString(this_code).replace(".0","");

                    Message msg = new Message();
                    msg.obj = "";
                    listLoadHandler.sendMessage(msg);

                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class typeSelectSelectedListener03 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            type_selectText = class3.getItemAtPosition(position).toString();
            type_selectText3 = class3.getItemAtPosition(position).toString();
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    if(!type_selectText3.equals("") && !type_selectText2.equals("")){
                        code.setText(code_text3);
                    }else if(!type_selectText2.equals("")){
                        code.setText(code_text2);
                    }else{
                        code.setText(code_text1);
                    }
                    if(panduan == false){
                        Intent intent = getIntent();
                        int id = intent.getIntExtra("type", 0);
                        if (id == R.id.insert_btn) {
                            yhFinanceKeMuZongZhang = new YhFinanceKeMuZongZhang();
                            Button btn = findViewById(id);
                            btn.setVisibility(View.VISIBLE);
                        } else if (id == R.id.update_btn) {
                            MyApplication myApplication = (MyApplication) getApplication();
                            yhFinanceKeMuZongZhang = (YhFinanceKeMuZongZhang) myApplication.getObj();
                            Button btn = findViewById(id);
                            btn.setVisibility(View.VISIBLE);
                            code.setText(yhFinanceKeMuZongZhang.getCode());
                            name.setText(yhFinanceKeMuZongZhang.getName());
                            load.setText(yhFinanceKeMuZongZhang.getLoad().toString());
                            borrowed.setText(yhFinanceKeMuZongZhang.getBorrowed().toString());

                        }
                        panduan = true;
                    }
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    float this_code = 0;
                    if (type_selectText1.equals("资产类")) {
                        this_code = 1001f;
                        for(int i=0; i<list2.size(); i++){
                            if(list2.get(i).getName().equals(class3.getSelectedItem())){
                                this_code = Float.parseFloat(list2.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class3.getSelectedItem().equals("")){
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"1",8,Float.toString(this_code).replace(".0",""));
                            }else{
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"1",6,code_text2);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");

                            if (list3 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText1.equals("负债类")) {
                        this_code = 2001f;
                        for(int i=0; i<list2.size(); i++){
                            if(list2.get(i).getName().equals(class3.getSelectedItem())){
                                this_code = Float.parseFloat(list2.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class3.getSelectedItem().equals("")){
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"2",8,Float.toString(this_code).replace(".0",""));
                            }else{
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"2",6,code_text2);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list3 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText1.equals("权益类")) {
                        this_code = 3001f;
                        for(int i=0; i<list2.size(); i++){
                            if(list2.get(i).getName().equals(class3.getSelectedItem())){
                                this_code = Float.parseFloat(list2.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class3.getSelectedItem().equals("")){
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"3",8,Float.toString(this_code).replace(".0",""));
                            }else{
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"3",6,code_text2);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list3 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText1.equals("成本类")) {
                        this_code = 4001f;
                        for(int i=0; i<list2.size(); i++){
                            if(list2.get(i).getName().equals(class3.getSelectedItem())){
                                this_code = Float.parseFloat(list2.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class3.getSelectedItem().equals("")){
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"4",8,Float.toString(this_code).replace(".0",""));
                            }else{
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"4",6,code_text2);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list3 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText1.equals("损益类")) {
                        this_code = 5001f;
                        for(int i=0; i<list2.size(); i++){
                            if(list2.get(i).getName().equals(class3.getSelectedItem())){
                                this_code = Float.parseFloat(list2.get(i).getCode());
                                break;
                            }
                        }

                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceKeMuZongZhangService = new YhFinanceKeMuZongZhangService();
                            if(!class3.getSelectedItem().equals("")){
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"5",8,Float.toString(this_code).replace(".0",""));
                            }else{
                                list3 = yhFinanceKeMuZongZhangService.getCodeListByCode(yhFinanceUser.getCompany(),"5",6,code_text2);
                            }
                            this_code = Float.parseFloat(Float.toString(this_code).replace(".0","") + "01");
                            if (list3 == null) return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    if (list3.size() > 0) {
                        for (int i = 0; i < list3.size(); i++) {
                            if(Float.parseFloat(list3.get(i).getCode()) >= this_code){
                                this_code = Float.parseFloat(list3.get(i).getCode()) + 1;
                            }
                        }
                    }

                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    code_text3 = nf.format(this_code);

                    Message msg = new Message();
                    msg.obj = "";
                    listLoadHandler.sendMessage(msg);

                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KeMuZongZhangChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KeMuZongZhangChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceKeMuZongZhangService.insert(yhFinanceKeMuZongZhang);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KeMuZongZhangChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KeMuZongZhangChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceKeMuZongZhangService.update(yhFinanceKeMuZongZhang);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (code.getText().toString().equals("")) {
            ToastUtil.show(KeMuZongZhangChangeActivity.this, "请输科目代码");
            return false;
        } else {
            yhFinanceKeMuZongZhang.setCode(code.getText().toString());
        }

        if (name.getText().toString().equals("")) {
            ToastUtil.show(KeMuZongZhangChangeActivity.this, "请输科目名称");
            return false;
        } else {
            yhFinanceKeMuZongZhang.setName(name.getText().toString());
        }

        if (load.getText().toString().equals("")) {
            ToastUtil.show(KeMuZongZhangChangeActivity.this, "请输年初借金");
            return false;
        } else {
            BigDecimal bd = new BigDecimal(load.getText().toString());
            yhFinanceKeMuZongZhang.setLoad(bd);
        }

        if (borrowed.getText().toString().equals("")) {
            ToastUtil.show(KeMuZongZhangChangeActivity.this, "请输年初贷金");
            return false;
        } else {
            BigDecimal bd = new BigDecimal(borrowed.getText().toString());
            yhFinanceKeMuZongZhang.setBorrowed(bd);
        }

        if (direction.getSelectedItem().equals("借")) {
            yhFinanceKeMuZongZhang.setDirection(true);
        }else{
            yhFinanceKeMuZongZhang.setDirection(false);
        }

        yhFinanceKeMuZongZhang.setCompany(yhFinanceUser.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
