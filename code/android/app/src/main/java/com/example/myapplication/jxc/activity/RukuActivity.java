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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
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


public class RukuActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 100;
    //商品二维码
    private static final int REQUEST_CODE_SCAN = 101;
    //订单二维码
    private static final int ORDER_CODE_SCAN = 102;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;

    private List<YhJinXiaoCunJiChuZiLiao> list;
    private List<YhJinXiaoCunMingXi> order_list;

    private String churuku;
    private ListView listView;
    private Button sel_button;
    private Button product_qr;
    private EditText search_text;

    HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();
    HashMap<Integer, String> num_map = new HashMap<Integer, String>();
    HashMap<Integer, String> jine_map = new HashMap<Integer, String>();

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
    // 用来存储 list_item_layout.xml中下义的View控件.
    // 并把这个ViewHolder对象放到Item的Tag中,
    // 这样做极大了方便我们之后对Item中的控件的修改操作
    // 并大大的减少了 findViewById的次数,提高效率
    class ViewHolder {
        public TextView name;
        public TextView spDm;
        public TextView leiBie;
        public TextView danWei;
        public EditText num;
        public EditText jine;
        public CheckBox cb;
    }

    class MyAdapter extends BaseAdapter {

        Context context;
        private LayoutInflater inflater = null;

        public MyAdapter(Context context) {
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
            // 声明一个 ViewHolder对象
            ViewHolder holder = null;
            View view;
            // convertView .就是上一次使用的Item的View对象, 参数View convertView 而这个convertView其实就是最关键的部分了 原理上讲
            // 当ListView滑动的过程中 会有item被滑出屏幕 而不再被使用 这时候Android会回收这个条目的view ,当item1被移除屏幕的时候 我们会重新new一个View给新显示的item_new
            // 而如果使用了这个convertView 我们其实可以复用它 这样就省去了new View的大量开销
            // 如果没有可用的 convertView 那么就要创建它
            if (convertView == null) {
                //view = LayoutInflater.from(context).inflate(R.layout.ruku_row,null);
                // 创建一个 ViewHolder对象,来保存这个 View中的了控件,这样,我们就不需要每都用 使用findViewById了.
                holder = new ViewHolder();
                // 使用 LayoutInflater 创建View
                view = inflater.inflate(R.layout.ruku_row, null);
                // 将,View 的了控件保存到 holder 中.
                holder.name = view.findViewById(R.id.name);
                holder.spDm = view.findViewById(R.id.spDm);
                holder.leiBie = view.findViewById(R.id.leiBie);
                holder.danWei = view.findViewById(R.id.danWei);
                holder.num = view.findViewById(R.id.num);
                holder.jine = view.findViewById(R.id.jine);
                holder.cb=view.findViewById(R.id.cb);
                // 将Hodler 存放在 convertView 的 Tag 中.
                view.setTag(holder);
            } else {
                view = convertView;
                // 如果有可用的 convertView.那么就得到存在它Tag中的 Holder对象
                holder = (ViewHolder) view.getTag();
            }

            // 对Holder对象中的控制设置属性或绑定事件
            holder.name.setText(list.get(position).getName());
            holder.spDm.setText(list.get(position).getSpDm());
            holder.leiBie.setText(list.get(position).getLeiBie());
            holder.danWei.setText(list.get(position).getDanWei());

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

            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).setCheck(b);
                }
            });
            holder.cb.setChecked(list.get(position).isCheck());

            // 将这个 view 返回
            return view;
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
            MyApplication myApplication = (MyApplication) getApplication();
            listView.setAdapter(new MyAdapter(myApplication.getApplicationContext()));
            listView.setSelection(row);
            Toast.makeText(this, "商品识别完成", Toast.LENGTH_SHORT).show();
        }else if (requestCode == ORDER_CODE_SCAN && data != null) {
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
            MyApplication myApplication = (MyApplication) getApplication();
            listView.setAdapter(new MyAdapter(myApplication.getApplicationContext()));
            Toast.makeText(this, "订单识别完成", Toast.LENGTH_SHORT).show();
        }
    }

    private void initList() {
        sel_button.setEnabled(false);
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MyApplication myApplication = (MyApplication) getApplication();
                listView.setAdapter(new MyAdapter(myApplication.getApplicationContext()));
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
                if(list.get(i).getNum() == null || list.get(i).getJine() == null){
                    ToastUtil.show(RukuActivity.this, "请将第 " + (i*1+1) + " 行的商品数量和单价补全！");
                    return;
                } else if(list.get(i).getNum().equals("") || list.get(i).getJine().equals("")){
                    ToastUtil.show(RukuActivity.this, "请将第 " + (i*1+1) + " 行的商品数量和单价补全！");
                    return;
                } else if(Float.parseFloat(list.get(i).getNum()) <= 0){
                    ToastUtil.show(RukuActivity.this, "第 " + (i*1+1) + " 行的商品数量需要大于0！");
                    return;
                }
                jczlList.add(list.get(i));
            }
        }

        if (jczlList.size() == 0) {
            ToastUtil.show(RukuActivity.this, "请先选择数据！");
            return;
        }

        Intent intent = new Intent(RukuActivity.this, RukuChangeActivity.class);
        intent.putExtra("jczlList", (Serializable) jczlList);
        intent.putExtra("churuku", churuku);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }


}
