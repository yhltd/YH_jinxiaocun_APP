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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RukuActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 100;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;

    private List<YhJinXiaoCunJiChuZiLiao> list;

    private String churuku;

    private ListView listView;
    private Button sel_button;
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

    // ???????????? ViewHolder ???
    // ???????????? list_item_layout.xml????????????View??????.
    // ????????????ViewHolder????????????Item???Tag???,
    // ???????????????????????????????????????Item???????????????????????????
    // ????????????????????? findViewById?????????,????????????
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
            // ???????????? ViewHolder??????
            ViewHolder holder = null;
            View view;
            // convertView .????????????????????????Item???View??????, ??????View convertView ?????????convertView????????????????????????????????? ????????????
            // ???ListView?????????????????? ??????item??????????????? ?????????????????? ?????????Android????????????????????????view ,???item1???????????????????????? ???????????????new??????View???????????????item_new
            // ????????????????????????convertView ??????????????????????????? ??????????????????new View???????????????
            // ????????????????????? convertView ?????????????????????
            if (convertView == null) {
                //view = LayoutInflater.from(context).inflate(R.layout.ruku_row,null);
                // ???????????? ViewHolder??????,??????????????? View???????????????,??????,??????????????????????????? ??????findViewById???.
                holder = new ViewHolder();
                // ?????? LayoutInflater ??????View
                view = inflater.inflate(R.layout.ruku_row, null);
                // ???,View ????????????????????? holder ???.
                holder.name = view.findViewById(R.id.name);
                holder.spDm = view.findViewById(R.id.spDm);
                holder.leiBie = view.findViewById(R.id.leiBie);
                holder.danWei = view.findViewById(R.id.danWei);
                holder.num = view.findViewById(R.id.num);
                holder.jine = view.findViewById(R.id.jine);
                holder.cb=view.findViewById(R.id.cb);
                // ???Hodler ????????? convertView ??? Tag ???.
                view.setTag(holder);
            } else {
                view = convertView;
                // ?????????????????? convertView.????????????????????????Tag?????? Holder??????
                holder = (ViewHolder) view.getTag();
            }

            // ???Holder?????????????????????????????????????????????
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

            // ????????? view ??????
            return view;
        }
    }


    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MyApplication myApplication = (MyApplication) getApplication();
                listView.setAdapter(new MyAdapter(myApplication.getApplicationContext()));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
                list = yhJinXiaoCunJiChuZiLiaoService.getList(yhJinXiaoCunUser.getGongsi(), search_text.getText().toString());
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
                jczlList.add(list.get(i));
            }
        }

        if (jczlList.size() == 0) {
            ToastUtil.show(RukuActivity.this, "?????????????????????");
            return;
        }

        Intent intent = new Intent(RukuActivity.this, RukuChangeActivity.class);
        intent.putExtra("jczlList", (Serializable) jczlList);
        intent.putExtra("churuku", churuku);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }
}
