package com.example.myapplication.renshi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SheBaoActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiGongZiMingXiService yhRenShiGongZiMingXiService;
    private ListView listView;
    private Spinner bumen_select;
    private String bumen_selectText;
    private Button sel_button;
    List<YhRenShiGongZiMingXi> list;

    List<String> bumen_array;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shebao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.shebao_list);
        bumen_select = findViewById(R.id.bumen_select);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        init_select();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init_select() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter = new ArrayAdapter<String>(SheBaoActivity.this, android.R.layout.simple_spinner_dropdown_item, bumen_array);
                bumen_select.setAdapter(StringUtils.cast(adapter));
                initList();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();
                    List<YhRenShiGongZiMingXi> bumenList = yhRenShiGongZiMingXiService.getBuMenList(yhRenShiUser.getL());
                    bumen_array = new ArrayList<>();
                    bumen_array.add("");
                    if (bumenList.size() > 0) {
                        for (int i = 0; i < bumenList.size(); i++) {
                            bumen_array.add(bumenList.get(i).getC());
                        }
                    }

                    adapter = new ArrayAdapter<String>(SheBaoActivity.this, android.R.layout.simple_spinner_dropdown_item, bumen_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }


    private void initList() {
        LoadingDialog.getInstance(this).show();
        bumen_selectText = bumen_select.getSelectedItem().toString();
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
                    yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();
                    list = yhRenShiGongZiMingXiService.shebaoQueryList(yhRenShiUser.getL(),bumen_selectText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("C", list.get(i).getC());
                        item.put("D", list.get(i).getD());
                        item.put("E", list.get(i).getE());
                        item.put("F", list.get(i).getF());
                        item.put("G", list.get(i).getG());
                        item.put("H", list.get(i).getH());
                        item.put("I", list.get(i).getI());
                        item.put("J", list.get(i).getJ());
                        item.put("K", list.get(i).getK());
                        item.put("L", list.get(i).getL());
                        item.put("M", list.get(i).getM());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(SheBaoActivity.this, data, R.layout.shebao_row, new String[]{"C","D","E","F","G","H","I","J","K","L","M"}, new int[]{R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M}) {
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
                listLoadHandler.sendMessage(msg);

            }
        }).start();
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(SheBaoActivity.this, SheBaoItemActivity.class);
        bumen_selectText = bumen_select.getSelectedItem().toString();
        if(bumen_selectText.equals("")){
            ToastUtil.show(SheBaoActivity.this, "未选择部门");
        }else{
            intent.putExtra("type", bumen_selectText);
            startActivityForResult(intent, REQUEST_CODE_CHANG);
        }
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
