package com.example.myapplication.jxc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.entity.YhJinXiaoCunCangKu;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.jxc.service.YhJinXiaoCunCangKuService;
import com.example.myapplication.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RukuActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 100;
    //商品二维码
    private static final int REQUEST_CODE_SCAN = 101;
    //订单二维码
    private static final int ORDER_CODE_SCAN = 102;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private YhJinXiaoCunCangKuService yhJinXiaoCunCangKuService;

    private List<YhJinXiaoCunJiChuZiLiao> list;
    private List<YhJinXiaoCunMingXi> order_list;
    private List<YhJinXiaoCunCangKu> cangkuList; // 新增：仓库列表

    private String churuku;
    private ListView listView;
    private Button sel_button;
    private Button product_qr;
    private EditText search_text;

    private MyAdapter adapter; // 添加适配器引用

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruku);

        Intent intent = getIntent();
        churuku = intent.getStringExtra("churuku");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(churuku);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        listView = findViewById(R.id.product_list);
        listView.setCacheColorHint(Color.TRANSPARENT);
        sel_button = findViewById(R.id.sel_button);
        product_qr = findViewById(R.id.product_qr);
        search_text = findViewById(R.id.product_search);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

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

    // 创建一个 ViewHolder 类
    class ViewHolder {
        public TextView name;
        public TextView spDm;
        public TextView leiBie;
        public TextView danWei;
        public EditText num;
        public EditText jine;
        public Spinner cangku; // 改为Spinner
        public CheckBox cb;
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;
        private List<String> cangkuNames; // 仓库名称列表

        public MyAdapter(Context context) {
            this.context = context; // 保存context
            inflater = LayoutInflater.from(context);
            // 初始化仓库名称列表
            cangkuNames = new ArrayList<>();
            if (cangkuList != null) {
                for (YhJinXiaoCunCangKu cangku : cangkuList) {
                    cangkuNames.add(cangku.getcangku());
                }
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View view;

            if (convertView == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.ruku_row, null);

                // 将View的控件保存到holder中
                holder.name = view.findViewById(R.id.name);
                holder.spDm = view.findViewById(R.id.spDm);
                holder.leiBie = view.findViewById(R.id.leiBie);
                holder.danWei = view.findViewById(R.id.danWei);
                holder.num = view.findViewById(R.id.num);
                holder.jine = view.findViewById(R.id.jine);
                holder.cangku = view.findViewById(R.id.cangku);
                holder.cb = view.findViewById(R.id.cb);

                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            // 设置基本数据
            holder.name.setText(list.get(position).getName());
            holder.spDm.setText(list.get(position).getSpDm());
            holder.leiBie.setText(list.get(position).getLeiBie());
            holder.danWei.setText(list.get(position).getDanWei());

            // 设置数量输入框
            holder.num.setTag(position);
            holder.num.clearFocus();
            holder.num.setText(list.get(position).getNum());

            final EditText num = holder.num;
            holder.num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int a = (int) num.getTag();
                    list.get(a).setNum(s.toString());
                }
            });

            // 设置金额输入框
            holder.jine.setTag(position);
            holder.jine.clearFocus();
            holder.jine.setText(list.get(position).getJine());

            final EditText jine = holder.jine;
            holder.jine.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int a = (int) jine.getTag();
                    list.get(a).setJine(s.toString());
                }
            });

            // 设置仓库下拉框
            holder.cangku.setTag(position);

            // 创建适配器 - 使用传入的context
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context, // 使用正确的context
                    android.R.layout.simple_spinner_item,
                    cangkuNames
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.cangku.setAdapter(adapter);

            // 设置默认选中项
            String currentCangku = list.get(position).getcangku();
            if (currentCangku != null && !currentCangku.isEmpty()) {
                int selectionPosition = cangkuNames.indexOf(currentCangku);
                if (selectionPosition >= 0) {
                    holder.cangku.setSelection(selectionPosition);
                } else {
                    holder.cangku.setSelection(0);
                }
            } else {
                holder.cangku.setSelection(0);
            }

            // 设置选择监听器
            final Spinner cangkuSpinner = holder.cangku;
            holder.cangku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    int itemPosition = (int) cangkuSpinner.getTag();
                    if (pos >= 0 && pos < cangkuNames.size()) {
                        String selectedCangku = cangkuNames.get(pos);
                        list.get(itemPosition).setcangku(selectedCangku);
                        Log.d("CangkuSelection", "第" + (itemPosition + 1) + "行选择了仓库: " + selectedCangku);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // 什么都不选时的处理
                }
            });

            // 设置复选框
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).setCheck(b);
                }
            });
            holder.cb.setChecked(list.get(position).isCheck());

            return view;
        }

        // 清空所有输入内容和复选框选择
        public void clearAllInputs() {
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    YhJinXiaoCunJiChuZiLiao item = list.get(i);
                    item.setNum("");        // 清空数量
                    item.setJine("");       // 清空金额
                    item.setcangku("");     // 清空仓库
                    item.setCheck(false);   // 取消勾选
                }
                notifyDataSetChanged();     // 通知数据更新
                Log.d("ClearInputs", "已清空所有输入内容和勾选");
            }
        }
    }

    public void onScanClick(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    public void onOrderScanClick(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, ORDER_CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHANG) {
            // 处理从RukuChangeActivity返回的结果
            if (resultCode == RESULT_OK) {
                // 添加成功后清空输入内容
                clearAllInputsAndSelections();
                Toast.makeText(this, "入库单已成功提交！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_SCAN && data != null) {
            String result = data.getStringExtra("qr_code");
            int row = 0;
            for(int i = 0; i < list.size(); i++) {
                if(result.equals(list.get(i).getSpDm())) {
                    int this_num = 0;
                    if(list.get(i).getNum() != null) {
                        if (!list.get(i).getNum().equals("")) {
                            this_num = Integer.parseInt(list.get(i).getNum());
                        }
                    }
                    list.get(i).setNum(String.valueOf(this_num + 1));
                    row = i;
                    break;
                }
            }
            // 使用 Activity 的 context
            adapter = new MyAdapter(RukuActivity.this);
            listView.setAdapter(adapter);
            listView.setSelection(row);
            Toast.makeText(this, "商品识别完成", Toast.LENGTH_SHORT).show();
        } else if (requestCode == ORDER_CODE_SCAN && data != null) {
            String result = data.getStringExtra("qr_code");
            for(int i = 0; i < order_list.size(); i++) {
                if(result.equals(order_list.get(i).getOrderid())) {
                    String this_sp = order_list.get(i).getSpDm();
                    int order_num = 0;
                    if(!order_list.get(i).getCpsl().equals("")) {
                        order_num = Integer.parseInt(order_list.get(i).getCpsl());
                    }
                    for(int j = 0; j < list.size(); j++) {
                        if(list.get(j).getSpDm().equals(this_sp)) {
                            int this_num = 0;
                            if(list.get(j).getNum() != null) {
                                if(!list.get(j).getNum().equals("")) {
                                    this_num = Integer.parseInt(list.get(j).getNum());
                                }
                            }
                            list.get(j).setNum(String.valueOf(this_num + order_num));
                            break;
                        }
                    }
                }
            }
            // 使用 Activity 的 context
            adapter = new MyAdapter(RukuActivity.this);
            listView.setAdapter(adapter);
            Toast.makeText(this, "订单识别完成", Toast.LENGTH_SHORT).show();
        }
    }

    private void initList() {
        sel_button.setEnabled(false);
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // 使用 RukuActivity.this 作为context
                adapter = new MyAdapter(RukuActivity.this);
                listView.setAdapter(adapter);
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
                yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
                yhJinXiaoCunCangKuService = new YhJinXiaoCunCangKuService();

                // 获取商品列表
                list = yhJinXiaoCunJiChuZiLiaoService.getList(yhJinXiaoCunUser.getGongsi(), search_text.getText().toString());

                // 获取订单列表
                order_list = yhJinXiaoCunMingXiService.getList(yhJinXiaoCunUser.getGongsi());

                // 获取仓库列表
                cangkuList = yhJinXiaoCunCangKuService.getList(yhJinXiaoCunUser.getGongsi());
                Log.d("CangkuDebug", "获取到仓库数量: " + (cangkuList != null ? cangkuList.size() : 0));

                Message msg = new Message();
                msg.obj = list;
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
        List<YhJinXiaoCunJiChuZiLiao> jczlList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck()) {
                String currentCangku = list.get(i).getcangku();
                Log.e("CangkuDebug", "第" + (i+1) + "行 - cangku: " + currentCangku +
                        ", 商品: " + list.get(i).getName() +
                        ", 数量: " + list.get(i).getNum() +
                        ", 金额: " + list.get(i).getJine());

                if(list.get(i).getNum() == null || list.get(i).getJine() == null || list.get(i).getcangku() == null) {
                    ToastUtil.show(RukuActivity.this, "请将第 " + (i+1) + " 行的商品数量、单价和仓库补全！");
                    return;
                } else if(list.get(i).getNum().equals("") || list.get(i).getJine().equals("") || list.get(i).getcangku().equals("")) {
                    ToastUtil.show(RukuActivity.this, "请将第 " + (i+1) + " 行的商品数量、单价和仓库补全！");
                    return;
                } else if(Float.parseFloat(list.get(i).getNum()) <= 0) {
                    ToastUtil.show(RukuActivity.this, "第 " + (i+1) + " 行的商品数量需要大于0！");
                    return;
                }
                jczlList.add(list.get(i));
            }
        }

        if (jczlList.size() == 0) {
            ToastUtil.show(RukuActivity.this, "请先选择数据！");
            return;
        }

        // 打印最终传递的cangku数据
        Log.e("CangkuDebug", "=== 最终传递的数据 ===");
        Log.e("CangkuDebug", "传递的记录数量: " + jczlList.size());
        for (int i = 0; i < jczlList.size(); i++) {
            YhJinXiaoCunJiChuZiLiao item = jczlList.get(i);
            Log.e("CangkuDebug", "传递的第" + (i+1) + "条 - cangku: " + item.getcangku() +
                    ", 商品: " + item.getName() +
                    ", 数量: " + item.getNum());
        }

        Intent intent = new Intent(RukuActivity.this, RukuChangeActivity.class);
        intent.putExtra("jczlList", (Serializable) jczlList);
        intent.putExtra("churuku", churuku);
        // 使用startActivityForResult以便接收返回结果
        startActivityForResult(intent, REQUEST_CODE_CHANG);

        // 立即清空当前页面输入内容
        clearAllInputsAndSelections();
    }

    // 清空所有输入内容和勾选
    private void clearAllInputsAndSelections() {
        if (adapter != null) {
            adapter.clearAllInputs();
        }
        // 清空搜索框
        if (search_text != null) {
            search_text.setText("");
        }
        ToastUtil.show(this, "已清空所有输入内容和勾选", Toast.LENGTH_SHORT);
    }

    // 添加一个清空按钮的点击事件
    public void onClearClick(View v) {
        clearAllInputsAndSelections();
    }

    // 如果需要，可以添加一个重置按钮到布局中
    // <Button
    //     android:id="@+id/btn_clear"
    //     android:layout_width="wrap_content"
    //     android:layout_height="wrap_content"
    //     android:text="清空"
    //     android:onClick="onClearClick" />
}