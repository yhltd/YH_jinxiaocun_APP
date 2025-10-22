package com.example.myapplication.mendian.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.LeftListAdapter;
import com.example.myapplication.mendian.entity.MainSectionedAdapter;
import com.example.myapplication.mendian.entity.PinnedHeaderListView;
import com.example.myapplication.mendian.entity.YhMendianOrderDetail;
import com.example.myapplication.mendian.entity.YhMendianOrders;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianOrdersService;
import com.example.myapplication.mendian.service.YhMendianProductshezhiService;
import com.example.myapplication.utils.ToastUtil;
import com.lxj.xpopup.core.BasePopupView;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;
import java.text.SimpleDateFormat;
public class OrderPanelActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianOrdersService yhMendianOrdersService;
    private EditText ddh;
    private TextView textView;
    private String ddhText;
    private Button clear_button;
    private Button sel_button;
    private Button ins_button;
    private YhMendianProductshezhiService yhMendianProductshezhiService;


    private boolean isScroll = true;
    ListView left_listview;//左侧列表
    PinnedHeaderListView pinnedListView;//右侧列表
    private LeftListAdapter adapter;
    private String[] leftStr;
    List<YhMendianProductshezhi> type_list;
    List<YhMendianProductshezhi> product_list;
    private boolean[] flagArray = {true, false, false, false, false, false, false, false, false};
    private YhMendianProductshezhi[][] rightStr;

    List<YhMendianOrderDetail> list = new ArrayList<>();
    List<YhMendianOrders> max_order;

    private Banner banner;
    private List<Integer> banner_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_panel);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhMendianOrdersService = new YhMendianOrdersService();
        yhMendianProductshezhiService = new YhMendianProductshezhiService();

        //初始化控件

        textView = findViewById(R.id.textView);

        ddh = findViewById(R.id.ddh);
        clear_button = findViewById(R.id.clear_button);
        clear_button.setOnClickListener(delClick());
        clear_button.requestFocus();

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        ins_button = findViewById(R.id.ins_button);
        ins_button.setOnClickListener(insClick());
        ins_button.requestFocus();

        orderRefresh();

        initData();
        banner = findViewById(R.id.main_banner);

        banner.setAdapter(new BannerImageAdapter<Integer>(banner_data) {

            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
            }
        });

        // 开启循环轮播
        banner.isAutoLoop(true);
        banner.setIndicator(new CircleIndicator(this));
        banner.setScrollBarFadeDuration(1000);
        // 设置指示器颜色(TODO 即选中时那个小点的颜色)
        banner.setIndicatorSelectedColor(Color.GREEN);
        // 开始轮播
        banner.start();
    }

    private void instantiation() {
        left_listview = findViewById(R.id.left_listview);
        pinnedListView = findViewById(R.id.pinnedListView);
        final MainSectionedAdapter sectionedAdapter = new MainSectionedAdapter(this, leftStr, rightStr);
        pinnedListView.setAdapter(sectionedAdapter);
        adapter = new LeftListAdapter(this, leftStr, flagArray);
        left_listview.setAdapter(adapter);
        left_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                isScroll = false;

                for (int i = 0; i < leftStr.length; i++) {
                    if (i == position) {
                        flagArray[i] = true;
                    } else {
                        flagArray[i] = false;
                    }
                }
                adapter.notifyDataSetChanged();
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    rightSection += sectionedAdapter.getCountForSection(i) + 1;
                }
                pinnedListView.setSelection(rightSection);

            }

        });

        pinnedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (pinnedListView.getLastVisiblePosition() == (pinnedListView.getCount() - 1)) {
                            left_listview.setSelection(ListView.FOCUS_DOWN);
                        }

                        // 判断滚动到顶部
                        if (pinnedListView.getFirstVisiblePosition() == 0) {
                            left_listview.setSelection(0);
                        }

                        break;
                }
            }

            int y = 0;
            int x = 0;
            int z = 0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < rightStr.length; i++) {
                        if (i == sectionedAdapter.getSectionForPosition(pinnedListView.getFirstVisiblePosition())) {
                            flagArray[i] = true;
                            x = i;
                        } else {
                            flagArray[i] = false;
                        }
                    }
                    if (x != y) {
                        adapter.notifyDataSetChanged();
                        y = x;
                        if (y == left_listview.getLastVisiblePosition()) {
//                            z = z + 3;
                            left_listview.setSelection(z);
                        }
                        if (x == left_listview.getFirstVisiblePosition()) {
//                            z = z - 1;
                            left_listview.setSelection(z);
                        }
                        if (firstVisibleItem + visibleItemCount == totalItemCount - 1) {
                            left_listview.setSelection(ListView.FOCUS_DOWN);
                        }
                    }
                } else {
                    isScroll = true;
                }
            }
        });
    }

    public View.OnClickListener delClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setYhMendianOrderDetail(new YhMendianOrderDetail());
                myApplication.setOrderDetails(new ArrayList<>());
                textView.setText("0");
                ToastUtil.show(OrderPanelActivity.this, "已作废");
            }
        };
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderPanelActivity.this, GouWuCheActivity.class);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list);
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public View.OnClickListener insClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderPanelActivity.this, OrderInsActivity.class);
                MyApplication myApplication = (MyApplication) getApplication();
                if(list.size() == 0){
                    ToastUtil.show(OrderPanelActivity.this, "购物车为空");
                }else{
                    myApplication.setObj(list);
                    startActivityForResult(intent, REQUEST_CODE_CHANG);
                }
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


    public void orderRefresh() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean handleMessage(Message msg) {
                if(max_order.get(0).getDdh() != null){
                    BigDecimal danhao = new BigDecimal(max_order.get(0).getDdh());
                    ddh.setText(danhao.toString());
                }else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String this_ddh = sdf.format(Calendar.getInstance().getTime());
//                    String this_ddh = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    ddh.setText(this_ddh + "0001");
                }

                leftStr = new String[type_list.size()];
                for (int i = 0; i < type_list.size(); i++) {
                    leftStr[i] = type_list.get(i).getType();
                }

                rightStr = new YhMendianProductshezhi[type_list.size()][];
                for(int i=0; i<rightStr.length; i++){
                    String this_type = leftStr[i];
                    int this_num = 0;
                    for(int j=0; j<product_list.size(); j++){
                        if(product_list.get(j).getType().equals(this_type)){
                            this_num = this_num + 1;
                        }
                    }
                    if(this_num > 0){
                        rightStr[i] = new YhMendianProductshezhi[this_num];
                    }else{
                        rightStr[i] = new YhMendianProductshezhi[0];
                    }
                    this_num = 0;
                    for(int j=0; j<product_list.size(); j++){
                        if(product_list.get(j).getType().equals(this_type)){
                            rightStr[i][this_num] = product_list.get(j);
                            this_num = this_num + 1;

                        }
                    }
                    System.out.println(rightStr);
                }
                instantiation();
                return true;
            }
        });
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String this_ddh = sdf.format(Calendar.getInstance().getTime());
//                    String this_ddh = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    max_order = yhMendianOrdersService.getListDDH(this_ddh,yhMendianUser.getCompany());
                    type_list = yhMendianProductshezhiService.getTypeList(yhMendianUser.getCompany());
                    product_list = yhMendianProductshezhiService.getList("","",yhMendianUser.getCompany());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = max_order;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }



    @Override
    protected void onResume() {
        super.onResume();
        //重新获取数据的逻辑，此处根据自己的要求回去
        YhMendianOrderDetail new_detail = myApplication.getYhMendianOrderDetail();
        list = myApplication.getOrderDetails();
        if(new_detail.getCpmc() != null){
            list.add(new_detail);
            for(int i=0; i<list.size(); i++){
                list.get(i).setDdid(ddh.getText().toString());
            }
            myApplication.setYhMendianOrderDetail(new YhMendianOrderDetail());
            myApplication.setOrderDetails(list);
            textView.setText(list.size() + "");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setYhMendianOrderDetail(new YhMendianOrderDetail());
                myApplication.setOrderDetails(new ArrayList<>());
                back();
            }
        }
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.orderpanel_baokuan);
    }

}
