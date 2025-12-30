package com.example.myapplication.jxc.activity;

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
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.jxc.entity.YhJinXiaoCunCangKu;

import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunCangKuService;

import com.example.myapplication.jxc.service.YhJinXiaoCunUserService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CangKuActivity extends AppCompatActivity {

        private final static int REQUEST_CODE_CHANG = 1;

        private YhJinXiaoCunUser yhJinXiaoCunUser;
        private YhJinXiaoCunCangKuService yhJinXiaoCunCangKuService;

        private ListView listView;

        private ListView listView_block;
        private HorizontalScrollView list_table;
        private SimpleAdapter adapter;
        private SimpleAdapter adapter_block;


        List<YhJinXiaoCunCangKu> list;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.cangku);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            listView = findViewById(R.id.qichu_list);
            listView_block = findViewById(R.id.list_block);
            list_table = findViewById(R.id.list_table);

            MyApplication myApplication = (MyApplication) getApplication();
            yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

            initList();
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
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    listView.setAdapter(StringUtils.cast(adapter));
                    listView_block.setAdapter(StringUtils.cast(adapter_block));
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    try {
                        yhJinXiaoCunCangKuService = new YhJinXiaoCunCangKuService();
                        list = yhJinXiaoCunCangKuService.getList(yhJinXiaoCunUser.getGongsi());
                        if (list == null) return;

                        for (int i = 0; i < list.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("cangku", list.get(i).getcangku());
                            data.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    adapter = new SimpleAdapter(com.example.myapplication.jxc.activity.CangKuActivity.this, data, R.layout.cangku_row, new String[]{"cangku"}, new int[]{R.id.cangku}) {
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

                    adapter_block = new SimpleAdapter(com.example.myapplication.jxc.activity.CangKuActivity.this, data, R.layout.cangku_row_block, new String[]{"cangku"}, new int[]{R.id.cangku}) {
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
            Intent intent = new Intent(com.example.myapplication.jxc.activity.CangKuActivity.this, CangKuChangeActivity.class);
            intent.putExtra("type", R.id.insert_btn);
            startActivityForResult(intent, REQUEST_CODE_CHANG);
        }

        public View.OnClickListener updateClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = Integer.parseInt(view.getTag().toString());
                    Intent intent = new Intent(com.example.myapplication.jxc.activity.CangKuActivity.this, CangKuChangeActivity.class);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(com.example.myapplication.jxc.activity.CangKuActivity.this);
                    int position = Integer.parseInt(view.getTag().toString());
                    Handler deleteHandler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(@NonNull Message msg) {
                            if ((boolean) msg.obj) {
                                ToastUtil.show(com.example.myapplication.jxc.activity.CangKuActivity.this, "删除成功");
                                initList();
                            }
                            return true;
                        }
                    });

                    builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            XiangQingYe xiangQingYe = new XiangQingYe();

                            xiangQingYe.setA_title("仓库:");


                            xiangQingYe.setA(list.get(position).getcangku());
                            Intent intent = new Intent(com.example.myapplication.jxc.activity.CangKuActivity.this, XiangQingYeActivity.class);
                            MyApplication myApplication = (MyApplication) getApplication();
                            myApplication.setObj(xiangQingYe);
                            startActivityForResult(intent, REQUEST_CODE_CHANG);
                        }
                    });

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = new Message();
                                    msg.obj = yhJinXiaoCunCangKuService.delete(list.get(position).get_id());
                                    deleteHandler.sendMessage(msg);
                                }
                            }).start();
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
