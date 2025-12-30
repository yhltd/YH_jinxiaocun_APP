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
import android.view.Menu;
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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunCangKu;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunCangKuService;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;
import com.journeyapps.barcodescanner.CaptureManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaiGouActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 100;
    //商品二维码
    private static final int REQUEST_CODE_SCAN = 101;
    //订单二维码
    private static final int ORDER_CODE_SCAN = 102;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private YhJinXiaoCunCangKuService yhJinXiaoCunCangKuService; // 新增仓库服务

    private List<YhJinXiaoCunJiChuZiLiao> list;
    private List<YhJinXiaoCunMingXi> order_list;
    private List<YhJinXiaoCunCangKu> cangkuDataList; // 从数据库获取的仓库列表
    private List<String> cangkuDisplayList; // 用于显示的仓库名称列表

    private String churuku;
    private ListView listView;
    private Button sel_button;
    private EditText search_text;

    private boolean isFirstLoad = true; // 用于标记第一次加载

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caigou);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        listView = findViewById(R.id.product_list);
        listView.setCacheColorHint(Color.TRANSPARENT);
        sel_button = findViewById(R.id.sel_button);
        search_text = findViewById(R.id.product_search);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // 初始化仓库服务
        yhJinXiaoCunCangKuService = new YhJinXiaoCunCangKuService();

        // 先加载仓库数据，然后加载商品数据
        initCangkuData();

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

    // 从数据库获取仓库数据
    private void initCangkuData() {
        Handler cangkuHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (cangkuDataList != null && !cangkuDataList.isEmpty()) {
                    // 将仓库数据转换为显示列表
                    cangkuDisplayList = new ArrayList<>();
                    cangkuDisplayList.add("请选择仓库"); // 添加默认选项

                    for (YhJinXiaoCunCangKu cangku : cangkuDataList) {
                        if (cangku.getcangku() != null && !cangku.getcangku().isEmpty()) {
                            cangkuDisplayList.add(cangku.getcangku());
                        }
                    }

                    Log.d("CaiGouActivity", "成功加载仓库数据: " + cangkuDisplayList.size() + " 个仓库");

                    // 仓库数据加载完成后，再加载商品数据
                    initList();
                } else {
                    Log.e("CaiGouActivity", "未获取到仓库数据");
                    // 即使没有仓库数据，也加载商品数据
                    initList();
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 从yh_jinxiaocun_cangku表获取仓库数据
                    cangkuDataList = yhJinXiaoCunCangKuService.getList(yhJinXiaoCunUser.getGongsi());
                    Message msg = new Message();
                    msg.obj = cangkuDataList;
                    cangkuHandler.sendMessage(msg);
                } catch (Exception e) {
                    Log.e("CaiGouActivity", "获取仓库数据异常: " + e.getMessage());
                    e.printStackTrace();
                    cangkuHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    // ViewHolder类
    class ViewHolder {
        public TextView name;
        public TextView spDm;
        public TextView leiBie;
        public TextView danWei;
        public EditText num;
        public EditText jine;
        public Spinner cangkuSpinner; // 改为Spinner
        public CheckBox cb;
        public ArrayAdapter<String> cangkuAdapter;
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
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
            final ViewHolder holder;
            View view;

            if (convertView == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.caigou_row, null);

                holder.name = view.findViewById(R.id.name);
                holder.spDm = view.findViewById(R.id.spDm);
                holder.leiBie = view.findViewById(R.id.leiBie);
                holder.danWei = view.findViewById(R.id.danWei);
                holder.num = view.findViewById(R.id.num);
                holder.jine = view.findViewById(R.id.jine);
                holder.cangkuSpinner = view.findViewById(R.id.cangku);
                holder.cb = view.findViewById(R.id.cb);

                // 设置仓库下拉框适配器
                if (cangkuDisplayList != null && !cangkuDisplayList.isEmpty()) {
                    holder.cangkuAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, cangkuDisplayList);
                    holder.cangkuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.cangkuSpinner.setAdapter(holder.cangkuAdapter);
                }

                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            // 设置数据
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

            // 设置仓库下拉框 - 重要：移除旧的监听器
            holder.cangkuSpinner.setOnItemSelectedListener(null);
            holder.cangkuSpinner.setTag(position);

            String currentCangku = list.get(position).getcangku();
            if (currentCangku != null && !currentCangku.isEmpty() && holder.cangkuAdapter != null) {
                int spinnerPosition = holder.cangkuAdapter.getPosition(currentCangku);
                if (spinnerPosition >= 0) {
                    holder.cangkuSpinner.setSelection(spinnerPosition);
                } else {
                    holder.cangkuSpinner.setSelection(0);
                }
            } else {
                holder.cangkuSpinner.setSelection(0);
            }

            // 方法1：使用final变量解决内部类访问问题
            final int finalPosition = position;
            final ViewHolder finalHolder = holder;

            // 设置新的监听器
            holder.cangkuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    int a = (int) finalHolder.cangkuSpinner.getTag();
                    if (pos > 0) {
                        String selectedCangku = (String) parent.getItemAtPosition(pos);
                        list.get(a).setcangku(selectedCangku);
                    } else {
                        list.get(a).setcangku("");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // 设置复选框 - 同样解决内部类访问问题
            holder.cb.setOnCheckedChangeListener(null);
            holder.cb.setChecked(list.get(position).isCheck());

            // 方法2：使用数组包装position
            final int[] positionRef = {position};
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(positionRef[0]).setCheck(b);
                }
            });

            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 如果从CaiGouChangeActivity返回并且操作成功，清空页面数据
        if (requestCode == REQUEST_CODE_CHANG && resultCode == Activity.RESULT_OK) {
            // 清空所有输入内容和勾选状态
            clearAllInputs();
            ToastUtil.show(this, "操作成功，已清空数据");
        }

        if (requestCode == REQUEST_CODE_SCAN && data != null) {
            String result = data.getStringExtra("qr_code");
            int row = 0;
            for(int i=0; i<list.size(); i++){
                if(result.equals(list.get(i).getSpDm())){
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
            refreshListView();
            listView.setSelection(row);
            Toast.makeText(this, "商品识别完成", Toast.LENGTH_SHORT).show();
        } else if (requestCode == ORDER_CODE_SCAN && data != null) {
            String result = data.getStringExtra("qr_code");
            for(int i=0; i<order_list.size(); i++){
                if(result.equals(order_list.get(i).getOrderid())){
                    String this_sp = order_list.get(i).getSpDm();
                    int order_num = 0;
                    if(!order_list.get(i).getCpsl().equals("")){
                        order_num = Integer.parseInt(order_list.get(i).getCpsl());
                    }
                    for(int j=0; j<list.size(); j++){
                        if(list.get(j).getSpDm().equals(this_sp)){
                            int this_num = 0;
                            if(list.get(j).getNum() != null){
                                if(!list.get(j).getNum().equals("")){
                                    this_num = Integer.parseInt(list.get(j).getNum());
                                }
                            }
                            list.get(j).setNum(String.valueOf(this_num + order_num));
                            break;
                        }
                    }
                }
            }
            refreshListView();
            Toast.makeText(this, "订单识别完成", Toast.LENGTH_SHORT).show();
        }
    }

    // 清空所有输入内容和勾选状态
    private void clearAllInputs() {
        if (list != null) {
            for (YhJinXiaoCunJiChuZiLiao item : list) {
                item.setNum("");
                item.setJine("");
                item.setcangku("");
                item.setCheck(false);
            }
            refreshListView();
        }
        // 清空搜索框
        search_text.setText("");
    }

    // 刷新ListView
    private void refreshListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listView.getAdapter() != null) {
                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                }
            }
        });
    }

    private void initList() {
        sel_button.setEnabled(false);
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(new CaiGouActivity.MyAdapter(CaiGouActivity.this));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
                yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
                list = yhJinXiaoCunJiChuZiLiaoService.getList(yhJinXiaoCunUser.getGongsi(), search_text.getText().toString());
                order_list = yhJinXiaoCunMingXiService.getList(yhJinXiaoCunUser.getGongsi());

                // 初始化每个项目的状态
                if (list != null) {
                    for (YhJinXiaoCunJiChuZiLiao item : list) {
                        item.setCheck(false);
                        // 如果之前没有设置过仓库，设置为空
                        if (item.getcangku() == null) {
                            item.setcangku("");
                        }
                    }
                }

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
        if (list == null || list.isEmpty()) {
            ToastUtil.show(CaiGouActivity.this, "暂无商品数据");
            return;
        }

        List<YhJinXiaoCunJiChuZiLiao> jczlList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck()) {
                String currentCangku = list.get(i).getcangku();
                Log.e("CangkuDebug", "第" + (i+1) + "行 - cangku: " + currentCangku +
                        ", 商品: " + list.get(i).getName() +
                        ", 数量: " + list.get(i).getNum() +
                        ", 金额: " + list.get(i).getJine());

                // 验证输入
                if (list.get(i).getNum() == null || list.get(i).getJine() == null ||
                        list.get(i).getcangku() == null || list.get(i).getcangku().isEmpty()) {
                    ToastUtil.show(CaiGouActivity.this, "请将第 " + (i+1) + " 行的商品数量和单价以及仓库补全！");
                    return;
                } else if (list.get(i).getNum().isEmpty() || list.get(i).getJine().isEmpty() ||
                        list.get(i).getcangku().isEmpty()) {
                    ToastUtil.show(CaiGouActivity.this, "请将第 " + (i+1) + " 行的商品数量和单价以及仓库补全！");
                    return;
                } else if (list.get(i).getcangku().equals("请选择仓库")) {
                    ToastUtil.show(CaiGouActivity.this, "请为第 " + (i+1) + " 行选择一个仓库！");
                    return;
                } else {
                    try {
                        float numValue = Float.parseFloat(list.get(i).getNum());
                        if (numValue <= 0) {
                            ToastUtil.show(CaiGouActivity.this, "第 " + (i+1) + " 行的商品数量需要大于0！");
                            return;
                        }

                        float jineValue = Float.parseFloat(list.get(i).getJine());
                        if (jineValue <= 0) {
                            ToastUtil.show(CaiGouActivity.this, "第 " + (i+1) + " 行的商品单价需要大于0！");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        ToastUtil.show(CaiGouActivity.this, "第 " + (i+1) + " 行的商品数量或单价格式不正确！");
                        return;
                    }
                }
                jczlList.add(list.get(i));
            }
        }

        if (jczlList.size() == 0) {
            ToastUtil.show(CaiGouActivity.this, "请先选择数据！");
            return;
        }

        // 打印最终传递的数据
        Log.e("CangkuDebug", "=== 最终传递的数据 ===");
        Log.e("CangkuDebug", "传递的记录数量: " + jczlList.size());
        for (int i = 0; i < jczlList.size(); i++) {
            YhJinXiaoCunJiChuZiLiao item = jczlList.get(i);
            Log.e("CangkuDebug", "传递的第" + (i+1) + "条 - cangku: " + item.getcangku() +
                    ", 商品: " + item.getName() +
                    ", 数量: " + item.getNum() +
                    ", 金额: " + item.getJine());
        }

        Intent intent = new Intent(CaiGouActivity.this, CaiGouChangeActivity.class);
        intent.putExtra("jczlList", (Serializable) jczlList);
        intent.putExtra("churuku", churuku);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }
}