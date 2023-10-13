package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceQuanXianService;
import com.example.myapplication.finance.service.YhFinanceUserService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuanXianChangeActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceUser yhFinanceUserNow;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceUserService yhFinanceUserService;
    private YhFinanceQuanXianService yhFinanceQuanXianService;

    private Spinner kmzzAdd;
    private Spinner kmzzDelete;
    private Spinner kmzzUpdate;
    private Spinner kmzzSelect;

    private Spinner kzxmAdd;
    private Spinner kzxmDelete;
    private Spinner kzxmUpdate;
    private Spinner kzxmSelect;

    private Spinner bmszAdd;
    private Spinner bmszDelete;
    private Spinner bmszUpdate;
    private Spinner bmszSelect;

    private Spinner zhglAdd;
    private Spinner zhglDelete;
    private Spinner zhglUpdate;
    private Spinner zhglSelect;

    private Spinner pzhzAdd;
    private Spinner pzhzDelete;
    private Spinner pzhzUpdate;
    private Spinner pzhzSelect;

    private Spinner znkbSelect;
    private Spinner xjllSelect;
    private Spinner zcfzSelect;
    private Spinner lysySelect;

    private Spinner jjtzAdd;
    private Spinner jjtzDelete;
    private Spinner jjtzUpdate;
    private Spinner jjtzSelect;

    private Spinner jjzzAdd;
    private Spinner jjzzDelete;
    private Spinner jjzzUpdate;
    private Spinner jjzzSelect;

    private String[] type_selectArray;
    List<YhFinanceQuanXian> list;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanxian_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceUserService = new YhFinanceUserService();
        yhFinanceQuanXianService = new YhFinanceQuanXianService();
        yhFinanceQuanXian = new YhFinanceQuanXian();

        kmzzAdd = findViewById(R.id.kmzzAdd);
        kmzzDelete = findViewById(R.id.kmzzDelete);
        kmzzUpdate = findViewById(R.id.kmzzUpdate);
        kmzzSelect = findViewById(R.id.kmzzSelect);

        kzxmAdd = findViewById(R.id.kzxmAdd);
        kzxmDelete = findViewById(R.id.kzxmDelete);
        kzxmUpdate = findViewById(R.id.kzxmUpdate);
        kzxmSelect = findViewById(R.id.kzxmSelect);

        bmszAdd = findViewById(R.id.bmszAdd);
        bmszDelete = findViewById(R.id.bmszDelete);
        bmszUpdate = findViewById(R.id.bmszUpdate);
        bmszSelect = findViewById(R.id.bmszSelect);

        zhglAdd = findViewById(R.id.zhglAdd);
        zhglDelete = findViewById(R.id.zhglDelete);
        zhglUpdate = findViewById(R.id.zhglUpdate);
        zhglSelect = findViewById(R.id.zhglSelect);

        pzhzAdd = findViewById(R.id.pzhzAdd);
        pzhzDelete = findViewById(R.id.pzhzDelete);
        pzhzUpdate = findViewById(R.id.pzhzUpdate);
        pzhzSelect = findViewById(R.id.pzhzSelect);

        znkbSelect = findViewById(R.id.znkbSelect);
        xjllSelect = findViewById(R.id.xjllSelect);
        zcfzSelect = findViewById(R.id.zcfzSelect);
        lysySelect = findViewById(R.id.lysySelect);

        jjtzAdd = findViewById(R.id.jjtzAdd);
        jjtzDelete = findViewById(R.id.jjtzDelete);
        jjtzUpdate = findViewById(R.id.jjtzUpdate);
        jjtzSelect = findViewById(R.id.jjtzSelect);

        jjzzAdd = findViewById(R.id.jjzzAdd);
        jjzzDelete = findViewById(R.id.jjzzDelete);
        jjzzUpdate = findViewById(R.id.jjzzUpdate);
        jjzzSelect = findViewById(R.id.jjzzSelect);

        type_selectArray = getResources().getStringArray(R.array.yes_no_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type_selectArray);
        kmzzAdd.setAdapter(adapter);
        kmzzDelete.setAdapter(adapter);
        kmzzUpdate.setAdapter(adapter);
        kmzzSelect.setAdapter(adapter);

        kzxmAdd.setAdapter(adapter);
        kzxmDelete.setAdapter(adapter);
        kzxmUpdate.setAdapter(adapter);
        kzxmSelect.setAdapter(adapter);

        bmszAdd.setAdapter(adapter);
        bmszDelete.setAdapter(adapter);
        bmszUpdate.setAdapter(adapter);
        bmszSelect.setAdapter(adapter);

        zhglAdd.setAdapter(adapter);
        zhglDelete.setAdapter(adapter);
        zhglUpdate.setAdapter(adapter);
        zhglSelect.setAdapter(adapter);

        pzhzAdd.setAdapter(adapter);
        pzhzDelete.setAdapter(adapter);
        pzhzUpdate.setAdapter(adapter);
        pzhzSelect.setAdapter(adapter);

        znkbSelect.setAdapter(adapter);
        xjllSelect.setAdapter(adapter);
        zcfzSelect.setAdapter(adapter);
        lysySelect.setAdapter(adapter);

        jjtzAdd.setAdapter(adapter);
        jjtzDelete.setAdapter(adapter);
        jjtzUpdate.setAdapter(adapter);
        jjtzSelect.setAdapter(adapter);

        jjzzAdd.setAdapter(adapter);
        jjzzDelete.setAdapter(adapter);
        jjzzUpdate.setAdapter(adapter);
        jjzzSelect.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.update_btn) {
            yhFinanceUserNow = (YhFinanceUser) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            initList();
        }
    }


    private void initList() {

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                kmzzAdd.setSelection(getType(yhFinanceQuanXian.getKmzzAdd()));
                kmzzDelete.setSelection(getType(yhFinanceQuanXian.getKmzzDelete()));
                kmzzUpdate.setSelection(getType(yhFinanceQuanXian.getKmzzUpdate()));
                kmzzSelect.setSelection(getType(yhFinanceQuanXian.getKmzzSelect()));

                kzxmAdd.setSelection(getType(yhFinanceQuanXian.getKzxmAdd()));
                kzxmDelete.setSelection(getType(yhFinanceQuanXian.getKzxmDelete()));
                kzxmUpdate.setSelection(getType(yhFinanceQuanXian.getKzxmUpdate()));
                kzxmSelect.setSelection(getType(yhFinanceQuanXian.getKzxmSelect()));

                bmszAdd.setSelection(getType(yhFinanceQuanXian.getBmszAdd()));
                bmszDelete.setSelection(getType(yhFinanceQuanXian.getBmszDelete()));
                bmszUpdate.setSelection(getType(yhFinanceQuanXian.getBmszUpdate()));
                bmszSelect.setSelection(getType(yhFinanceQuanXian.getBmszSelect()));

                zhglAdd.setSelection(getType(yhFinanceQuanXian.getZhglAdd()));
                zhglDelete.setSelection(getType(yhFinanceQuanXian.getZhglDelete()));
                zhglUpdate.setSelection(getType(yhFinanceQuanXian.getZhglUpdate()));
                zhglSelect.setSelection(getType(yhFinanceQuanXian.getZhglSelect()));

                pzhzAdd.setSelection(getType(yhFinanceQuanXian.getPzhzAdd()));
                pzhzDelete.setSelection(getType(yhFinanceQuanXian.getPzhzDelete()));
                pzhzUpdate.setSelection(getType(yhFinanceQuanXian.getPzhzUpdate()));
                pzhzSelect.setSelection(getType(yhFinanceQuanXian.getPzhzSelect()));

                znkbSelect.setSelection(getType(yhFinanceQuanXian.getZnkbSelect()));
                xjllSelect.setSelection(getType(yhFinanceQuanXian.getXjllSelect()));
                zcfzSelect.setSelection(getType(yhFinanceQuanXian.getZcfzSelect()));
                lysySelect.setSelection(getType(yhFinanceQuanXian.getLysySelect()));

                jjtzAdd.setSelection(getType(yhFinanceQuanXian.getJjtzAdd()));
                jjtzDelete.setSelection(getType(yhFinanceQuanXian.getJjtzDelete()));
                jjtzUpdate.setSelection(getType(yhFinanceQuanXian.getJjtzUpdate()));
                jjtzSelect.setSelection(getType(yhFinanceQuanXian.getJjtzSelect()));

                jjzzAdd.setSelection(getType(yhFinanceQuanXian.getJjzzAdd()));
                jjzzDelete.setSelection(getType(yhFinanceQuanXian.getJjzzDelete()));
                jjzzUpdate.setSelection(getType(yhFinanceQuanXian.getJjzzUpdate()));
                jjzzSelect.setSelection(getType(yhFinanceQuanXian.getJjzzSelect()));

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhFinanceQuanXianService = new YhFinanceQuanXianService();
                    list = yhFinanceQuanXianService.getList(yhFinanceUserNow.getBianhao());
                    if (list == null) return;
                    yhFinanceQuanXian = list.get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.obj = "";
                listLoadHandler.sendMessage(msg);

            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(QuanXianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(QuanXianChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhFinanceUserService.update(yhFinanceUserNow);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkForm() throws ParseException {

        yhFinanceQuanXian.setKmzzAdd(kmzzAdd.getSelectedItem().toString());
        yhFinanceQuanXian.setKmzzDelete(kmzzDelete.getSelectedItem().toString());
        yhFinanceQuanXian.setKmzzUpdate(kmzzUpdate.getSelectedItem().toString());
        yhFinanceQuanXian.setKmzzSelect(kmzzSelect.getSelectedItem().toString());

        yhFinanceQuanXian.setKzxmAdd(kzxmAdd.getSelectedItem().toString());
        yhFinanceQuanXian.setKzxmDelete(kzxmDelete.getSelectedItem().toString());
        yhFinanceQuanXian.setKzxmUpdate(kzxmUpdate.getSelectedItem().toString());
        yhFinanceQuanXian.setKzxmSelect(kzxmSelect.getSelectedItem().toString());

        yhFinanceQuanXian.setBmszAdd(bmszAdd.getSelectedItem().toString());
        yhFinanceQuanXian.setBmszDelete(bmszDelete.getSelectedItem().toString());
        yhFinanceQuanXian.setBmszUpdate(bmszUpdate.getSelectedItem().toString());
        yhFinanceQuanXian.setBmszSelect(bmszSelect.getSelectedItem().toString());

        yhFinanceQuanXian.setZhglAdd(zhglAdd.getSelectedItem().toString());
        yhFinanceQuanXian.setZhglDelete(zhglDelete.getSelectedItem().toString());
        yhFinanceQuanXian.setZhglUpdate(zhglUpdate.getSelectedItem().toString());
        yhFinanceQuanXian.setZhglSelect(zhglSelect.getSelectedItem().toString());

        yhFinanceQuanXian.setPzhzAdd(pzhzAdd.getSelectedItem().toString());
        yhFinanceQuanXian.setPzhzDelete(pzhzDelete.getSelectedItem().toString());
        yhFinanceQuanXian.setPzhzUpdate(pzhzUpdate.getSelectedItem().toString());
        yhFinanceQuanXian.setPzhzSelect(pzhzSelect.getSelectedItem().toString());

        yhFinanceQuanXian.setZnkbSelect(znkbSelect.getSelectedItem().toString());
        yhFinanceQuanXian.setXjllSelect(xjllSelect.getSelectedItem().toString());
        yhFinanceQuanXian.setZcfzSelect(zcfzSelect.getSelectedItem().toString());
        yhFinanceQuanXian.setLysySelect(lysySelect.getSelectedItem().toString());

        yhFinanceQuanXian.setJjtzAdd(jjtzAdd.getSelectedItem().toString());
        yhFinanceQuanXian.setJjtzDelete(jjtzDelete.getSelectedItem().toString());
        yhFinanceQuanXian.setJjtzUpdate(jjtzUpdate.getSelectedItem().toString());
        yhFinanceQuanXian.setJjtzSelect(jjtzSelect.getSelectedItem().toString());

        yhFinanceQuanXian.setJjzzAdd(jjzzAdd.getSelectedItem().toString());
        yhFinanceQuanXian.setJjzzDelete(jjzzDelete.getSelectedItem().toString());
        yhFinanceQuanXian.setJjzzUpdate(jjzzUpdate.getSelectedItem().toString());
        yhFinanceQuanXian.setJjzzSelect(jjzzSelect.getSelectedItem().toString());

        return true;
    }

    private int getType(String param) {
        if (type_selectArray != null) {
            for (int i = 0; i < type_selectArray.length; i++) {
                if (param.equals(type_selectArray[i])) {
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


}
