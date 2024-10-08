package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.jxc.service.YhJinXiaoCunZhengLiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BiJiActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunZhengLiService yhJinXiaoCunZhengLiService;
    private EditText cpname_text;
    private ListView listView;
    private ListView listView_block;
    private Button sel_button;
    private HorizontalScrollView biji_list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    List<YhJinXiaoCunZhengLi> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biji);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        cpname_text = findViewById(R.id.cpname_text);

        listView = findViewById(R.id.biji_list);
        listView_block = findViewById(R.id.biji_list_block);
        biji_list_table = findViewById(R.id.biji_list_table);

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
        sel_button.setEnabled(false);
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
                    yhJinXiaoCunZhengLiService = new YhJinXiaoCunZhengLiService();
                    list = yhJinXiaoCunZhengLiService.getList(yhJinXiaoCunUser.getGongsi(), cpname_text.getText().toString());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("spDm", list.get(i).getSpDm());
                        item.put("name", list.get(i).getName());
                        item.put("leiBie", list.get(i).getLeiBie());
                        item.put("danWei", list.get(i).getDanWei());
                        item.put("beizhu", list.get(i).getBeizhu());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(BiJiActivity.this, data, R.layout.biji_row, new String[]{"spDm", "name", "leiBie", "danWei", "beizhu"}, new int[]{R.id.spDm, R.id.name, R.id.leiBie, R.id.danWei, R.id.beizhu}) {
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

                adapter_block = new SimpleAdapter(BiJiActivity.this, data, R.layout.biji_row_block, new String[]{"spDm", "name", "leiBie", "danWei", "beizhu"}, new int[]{R.id.spDm, R.id.name, R.id.leiBie, R.id.danWei, R.id.beizhu}) {
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

    public void onInsertClick(View v) {
        Intent intent = new Intent(BiJiActivity.this, BiJiChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    @SuppressLint("WrongConstant")
    public void switchClick(View v) {
        if(listView_block.getVisibility() == 0){
            listView_block.setVisibility(8);
            biji_list_table.setVisibility(0);
        }else if(listView_block.getVisibility() == 8){
            listView_block.setVisibility(0);
            biji_list_table.setVisibility(8);
        }

    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(BiJiActivity.this, BiJiChangeActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(BiJiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(BiJiActivity.this, "删除成功");
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
                                msg.obj = yhJinXiaoCunZhengLiService.delete(list.get(position).getId());
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

                        xiangQingYe.setA_title("商品代码:");
                        xiangQingYe.setB_title("商品名称:");
                        xiangQingYe.setC_title("商品类别:");
                        xiangQingYe.setD_title("商品单位:");
                        xiangQingYe.setE_title("备注:");

                        xiangQingYe.setA(list.get(position).getSpDm());
                        xiangQingYe.setB(list.get(position).getName());
                        xiangQingYe.setC(list.get(position).getLeiBie());
                        xiangQingYe.setD(list.get(position).getDanWei());
                        xiangQingYe.setE(list.get(position).getBeizhu());

                        Intent intent = new Intent(BiJiActivity.this, XiangQingYeActivity.class);
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
