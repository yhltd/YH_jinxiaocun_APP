package com.example.myapplication.renshi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.activity.DepartmentChangeActivity;
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceDepartmentService;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiPeiZhiBiaoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeiZhiBiaoActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiPeiZhiBiaoService yhRenShiPeiZhiBiaoService;
    private ListView listView;

    List<YhRenShiPeiZhiBiao> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peizhibiao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.peizhibiao_list);

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

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
                    yhRenShiPeiZhiBiaoService = new YhRenShiPeiZhiBiaoService();
                    list = yhRenShiPeiZhiBiaoService.getList(yhRenShiUser.getL());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("kaoqin", list.get(i).getKaoqin());
                        item.put("kaoqin_peizhi", list.get(i).getKaoqin_peizhi());
                        item.put("bumen", list.get(i).getBumen());
                        item.put("zhiwu", list.get(i).getZhiwu());
                        item.put("chidao_koukuan", list.get(i).getChidao_koukuan());
                        item.put("geren_yiliao", list.get(i).getGeren_yiliao());
                        item.put("qiye_yiliao", list.get(i).getQiye_yiliao());
                        item.put("geren_shengyu", list.get(i).getGeren_shengyu());
                        item.put("qiye_shengyu", list.get(i).getQiye_shengyu());
                        item.put("geren_gongjijin", list.get(i).getGeren_gongjijin());
                        item.put("qiye_gongjijin", list.get(i).getQiye_gongjijin());
                        item.put("yiliao_jishu", list.get(i).getYiliao_jishu());
                        item.put("geren_nianjin", list.get(i).getGeren_nianjin());
                        item.put("qiye_nianjin", list.get(i).getQiye_nianjin());
                        item.put("zhinajin", list.get(i).getZhinajin());
                        item.put("nianjin_jishu", list.get(i).getNianjin_jishu());
                        item.put("lixi", list.get(i).getLixi());
                        item.put("geren_yanglao", list.get(i).getGeren_yanglao());
                        item.put("qiye_yanglao", list.get(i).getQiye_yanglao());
                        item.put("gangwei", list.get(i).getGangwei());
                        item.put("gangwei_gongzi", list.get(i).getGangwei_gongzi());
                        item.put("qiye_shiye", list.get(i).getQiye_shiye());
                        item.put("gongzi", list.get(i).getGongzi());
                        item.put("shuilv", list.get(i).getShuilv());
                        item.put("kuadu_gongzi", list.get(i).getKuadu_gongzi());
                        item.put("qiye_gongshang", list.get(i).getQiye_gongshang());
                        item.put("jintie", list.get(i).getJintie());
                        item.put("nianjin1", list.get(i).getNianjin1());
                        item.put("jiabanfei", list.get(i).getJiabanfei());
                        item.put("yansuangongshi", list.get(i).getYansuangongshi());
                        item.put("queqin_koukuan", list.get(i).getQueqin_koukuan());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(PeiZhiBiaoActivity.this, data, R.layout.peizhibiao_row, new String[]{"kaoqin","kaoqin_peizhi","bumen","zhiwu","chidao_koukuan","geren_yiliao","qiye_yiliao","geren_shengyu","qiye_shengyu","geren_gongjijin","qiye_gongjijin","yiliao_jishu","geren_nianjin","qiye_nianjin","zhinajin","nianjin_jishu","lixi","geren_yanglao","qiye_yanglao","gangwei","gangwei_gongzi","qiye_shiye","gongzi","shuilv","kuadu_gongzi","qiye_gongshang","jintie","nianjin1","jiabanfei","yansuangongshi","queqin_koukuan"}, new int[]{R.id.kaoqin, R.id.kaoqin_peizhi, R.id.bumen, R.id.zhiwu, R.id.chidao_koukuan, R.id.geren_yiliao, R.id.qiye_yiliao, R.id.geren_shengyu, R.id.qiye_shengyu, R.id.geren_gongjijin, R.id.qiye_gongjijin, R.id.yiliao_jishu, R.id.geren_nianjin, R.id.qiye_nianjin, R.id.zhinajin, R.id.nianjin_jishu, R.id.lixi, R.id.geren_yanglao, R.id.qiye_yanglao, R.id.gangwei, R.id.gangwei_gongzi, R.id.qiye_shiye, R.id.gongzi, R.id.shuilv, R.id.kuadu_gongzi, R.id.qiye_gongshang, R.id.jintie, R.id.nianjin1, R.id.jiabanfei, R.id.yansuangongshi, R.id.queqin_koukuan}) {
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


    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(PeiZhiBiaoActivity.this, PeiZhiBiaoChangeActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PeiZhiBiaoActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(PeiZhiBiaoActivity.this, "删除成功");
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
                                msg.obj = yhRenShiPeiZhiBiaoService.delete(list.get(position).getId());
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

    public void onInsertClick(View v) {
        Intent intent = new Intent(PeiZhiBiaoActivity.this, PeiZhiBiaoChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
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
