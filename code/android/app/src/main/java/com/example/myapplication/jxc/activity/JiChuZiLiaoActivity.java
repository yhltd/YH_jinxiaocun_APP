package com.example.myapplication.jxc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.jxc.service.YhJinXiaoCunUserService;
import com.example.myapplication.utils.GsonUtil;
import com.example.myapplication.utils.InputUtil;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JiChuZiLiaoActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;
    private EditText cpname_text;
    private ListView listView;
    private Button sel_button;

    List<YhJinXiaoCunJiChuZiLiao> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jichuziliao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        cpname_text = findViewById(R.id.cpname_text);
        listView = findViewById(R.id.jichuziliao_list);
        sel_button = findViewById(R.id.sel_button);

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        initList();
        sel_button.setOnClickListener(selClick());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
                    list = yhJinXiaoCunJiChuZiLiaoService.getList(yhJinXiaoCunUser.getGongsi(), cpname_text.getText().toString());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("spDm", list.get(i).getSpDm());
                        item.put("name", list.get(i).getName());
                        item.put("leiBie", list.get(i).getLeiBie());
                        item.put("danWei", list.get(i).getDanWei());
                        item.put("kehu", list.get(i).getShouHuo());
                        item.put("gongyingshang", list.get(i).getGongHuo());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(JiChuZiLiaoActivity.this, data, R.layout.jichuziliao_row, new String[]{"spDm", "name", "leiBie", "danWei", "kehu", "gongyingshang"}, new int[]{R.id.spDm, R.id.name, R.id.leiBie, R.id.danWei, R.id.kehu, R.id.gongyingshang}) {
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
                        try {
                            yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
                            list = yhJinXiaoCunJiChuZiLiaoService.getList(yhJinXiaoCunUser.getGongsi(), cpname_text.getText().toString());
                            if (list == null) return;

                            for (int i = 0; i < list.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("spDm", list.get(i).getSpDm());
                                item.put("name", list.get(i).getName());
                                item.put("leiBie", list.get(i).getLeiBie());
                                item.put("danWei", list.get(i).getDanWei());
                                item.put("kehu", list.get(i).getShouHuo());
                                item.put("gongyingshang", list.get(i).getGongHuo());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        SimpleAdapter adapter = new SimpleAdapter(JiChuZiLiaoActivity.this, data, R.layout.jichuziliao_row, new String[]{"spDm", "name", "leiBie", "danWei", "kehu", "gongyingshang"}, new int[]{R.id.spDm, R.id.name, R.id.leiBie, R.id.danWei, R.id.kehu, R.id.gongyingshang}) {
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
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(JiChuZiLiaoActivity.this, JiChuZiLiaoChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(JiChuZiLiaoActivity.this, JiChuZiLiaoChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JiChuZiLiaoActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(JiChuZiLiaoActivity.this, "????????????");
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
                                msg.obj = yhJinXiaoCunJiChuZiLiaoService.delete(list.get(position).getId());
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
