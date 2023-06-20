package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.renshi.service.YhRenShiUserService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class YuanGongDangAnActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiUserService yhRenShiUserService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private EditText yuangong_name;
    private String yuangong_nameText;
    private String dateText;
    private Button sel_button;
    private Button sel_button2;
    List<YhRenShiUser> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuangongdangan);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.yuangongdangan_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        yuangong_name = findViewById(R.id.yuangong_name);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        sel_button2 = findViewById(R.id.sel_button2);
        sel_button2.setOnClickListener(selClick2());
        sel_button2.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        initList();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public View.OnClickListener selClick2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayList();
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

    @SuppressLint("WrongConstant")
    public void switchClick(View v) {
        if(listView_block.getVisibility() == 0){
            listView_block.setVisibility(8);
            list_table.setVisibility(0);
        }else if(listView_block.getVisibility() == 8){
            listView_block.setVisibility(0);
            list_table.setVisibility(8);
        }

    }

    private void initList() {
        sel_button.setEnabled(false);
        yuangong_nameText = yuangong_name.getText().toString();

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhRenShiUserService = new YhRenShiUserService();
                    list = yhRenShiUserService.getList(yhRenShiUser.getL(),yuangong_nameText,"");
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("B", list.get(i).getB());
                        item.put("C", list.get(i).getC());
                        item.put("D", list.get(i).getD());
                        item.put("E", list.get(i).getE());
                        item.put("F", list.get(i).getF());
                        item.put("G", list.get(i).getG());
                        item.put("H", list.get(i).getH());
                        item.put("K", list.get(i).getK());
                        item.put("M", list.get(i).getM());
                        item.put("N", list.get(i).getN());
                        item.put("O", list.get(i).getO());
                        item.put("P", list.get(i).getP());
                        item.put("Q", list.get(i).getQ());
                        item.put("R", list.get(i).getR());
                        item.put("S", list.get(i).getS());
                        item.put("AC", list.get(i).getAc());
                        item.put("AD", list.get(i).getAd());
                        item.put("T", list.get(i).getT());
                        item.put("U", list.get(i).getU());
                        item.put("V", list.get(i).getV());
                        item.put("W", list.get(i).getW());
                        item.put("X", list.get(i).getX());
                        item.put("Y", list.get(i).getY());
                        item.put("Z", list.get(i).getZ());
                        item.put("AA", list.get(i).getAa());
                        item.put("AB", list.get(i).getAb());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(YuanGongDangAnActivity.this, data, R.layout.yuangongdangan_row, new String[]{"B","C","D","E","F","G","H","K","M","N","O","P","Q","R","S","AC","AD","T","U","V","W","X","Y","Z","AA","AB"}, new int[]{R.id.B, R.id.C, R.id.D,R.id.E,R.id.F,R.id.G,R.id.H,R.id.K,R.id.M, R.id.N,R.id.O,R.id.P,R.id.Q,R.id.R,R.id.S,R.id.AC,R.id.AD,R.id.T,R.id.U,R.id.V,R.id.W,R.id.X,R.id.Y,R.id.Z,R.id.AA,R.id.AB}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(YuanGongDangAnActivity.this, data, R.layout.yuangongdangan_row_block, new String[]{"B","C","D","E","F","G","H","K","M","N","O","P","Q","R","S","AC","AD","T","U","V","W","X","Y","Z","AA","AB"}, new int[]{R.id.B, R.id.C, R.id.D,R.id.E,R.id.F,R.id.G,R.id.H,R.id.K,R.id.M, R.id.N,R.id.O,R.id.P,R.id.Q,R.id.R,R.id.S,R.id.AC,R.id.AD,R.id.T,R.id.U,R.id.V,R.id.W,R.id.X,R.id.Y,R.id.Z,R.id.AA,R.id.AB}) {
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


    private void birthdayList() {
        Calendar now = Calendar.getInstance();
        dateText = now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DAY_OF_MONTH);

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
                    yhRenShiUserService = new YhRenShiUserService();
                    list = yhRenShiUserService.birthdayList(yhRenShiUser.getL(),dateText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("B", list.get(i).getB());
                        item.put("C", list.get(i).getC());
                        item.put("D", list.get(i).getD());
                        item.put("E", list.get(i).getE());
                        item.put("F", list.get(i).getF());
                        item.put("G", list.get(i).getG());
                        item.put("H", list.get(i).getH());
                        item.put("K", list.get(i).getK());
                        item.put("M", list.get(i).getM());
                        item.put("N", list.get(i).getN());
                        item.put("O", list.get(i).getO());
                        item.put("P", list.get(i).getP());
                        item.put("Q", list.get(i).getQ());
                        item.put("R", list.get(i).getR());
                        item.put("S", list.get(i).getS());
                        item.put("AC", list.get(i).getAc());
                        item.put("AD", list.get(i).getAd());
                        item.put("T", list.get(i).getT());
                        item.put("U", list.get(i).getU());
                        item.put("V", list.get(i).getV());
                        item.put("W", list.get(i).getW());
                        item.put("X", list.get(i).getX());
                        item.put("Y", list.get(i).getY());
                        item.put("Z", list.get(i).getZ());
                        item.put("AA", list.get(i).getAa());
                        item.put("AB", list.get(i).getAb());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(YuanGongDangAnActivity.this, data, R.layout.yuangongdangan_row, new String[]{"B","C","D","E","F","G","H","K","M","N","O","P","Q","R","S","AC","AD","T","U","V","W","X","Y","Z","AA","AB"}, new int[]{R.id.B, R.id.C, R.id.D,R.id.E,R.id.F,R.id.G,R.id.H,R.id.K,R.id.M, R.id.N,R.id.O,R.id.P,R.id.Q,R.id.R,R.id.S,R.id.AC,R.id.AD,R.id.T,R.id.U,R.id.V,R.id.W,R.id.X,R.id.Y,R.id.Z,R.id.AA,R.id.AB}) {
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
