package com.example.myapplication.mendian.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import java.util.Locale;

public class OrderPanelActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;
    private ImageView imageView;
    private YhMendianUser yhMendianUser;
    private YhMendianOrdersService yhMendianOrdersService;
    private EditText ddh;
    private TextView textView;
    private String ddhText;
    private Button clear_button;
    private Button sel_button;
    private Button ins_button;
    private YhMendianProductshezhiService yhMendianProductshezhiService;
    private LinearLayout yourLinearLayout;
    private GestureDetector gestureDetector;
    // 在类的成员变量区域声明
    private Handler scrollHandler = new Handler();
    private Runnable hideRunnable;
    private Runnable showRunnable;
    private boolean isLayoutVisible = true;
    private long lastActionTime = 0;
    private static final long ACTION_COOLDOWN = 800; // 防抖时间800ms
    private static final long SCROLL_DELAY = 300; // 延迟300ms执行
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
    private int currentScrollDirection = 0;
    private long lastScrollTime = 0;
    private static final long SCROLL_INTERVAL = 500;
    private static final int MIN_SCROLL_DISTANCE = 1; // 最小滑动距离
    private float totalScrollDistance = 0; // 累计滑动距离
    int heightInDp = 80;
    int heightInDpshow = 120;
    private int heightInPixels;
    private int heightInPixelsshow;
//    private Banner banner;
//    private List<Integer> banner_data;
private Button btnQuanbu, btnZhekou, btnRexiao, btnXinpin;
    private List<Button> allButtons = new ArrayList<>();

    private void initGestureDetection() {
        yourLinearLayout = findViewById(R.id.ddbh);
        // 初始化Runnable
        hideRunnable = new Runnable() {
            @Override
            public void run() {
                if (isLayoutVisible) {
                    Log.d("GestureDebug", "准备隐藏布局");
                    smoothHideLayout();
                    isLayoutVisible = false;
                    lastActionTime = System.currentTimeMillis();
                    Log.d("GestureDebug", "隐藏完成, isLayoutVisible=" + isLayoutVisible);
                }else {
                    Log.d("GestureDebug", "布局已经隐藏，跳过");
                }
            }
        };
        showRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isLayoutVisible) {
                    Log.d("GestureDebug", "准备显示布局");
                    smoothShowLayout();
                    isLayoutVisible = true;
                    lastActionTime = System.currentTimeMillis();
                    Log.d("GestureDebug", "显示完成, isLayoutVisible=" + isLayoutVisible);
                }else {
                    Log.d("GestureDebug", "布局已经显示，跳过");
                }
            }
        };

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (Math.abs(distanceY) > 3) {
                    Log.d("GestureDebug", "滑动, distanceY: " + distanceY);
                    long currentTime = System.currentTimeMillis();

                    // 累计滑动距离
                    totalScrollDistance += Math.abs(distanceY);

                    // 防抖检查和频率控制
                    if (currentTime - lastActionTime < ACTION_COOLDOWN ||
                            currentTime - lastScrollTime < SCROLL_INTERVAL) {
                        return false;
                    }

                    int newDirection = (distanceY > 0) ? 1 : 2;

                    // 如果方向改变，重置状态和累计距离
                    if (newDirection != currentScrollDirection) {
                        currentScrollDirection = newDirection;
                        totalScrollDistance = 0;
                        scrollHandler.removeCallbacks(hideRunnable);
                        scrollHandler.removeCallbacks(showRunnable);
                        return false;
                    }

                    // 只有当滑动距离达到阈值时才触发
                    if (totalScrollDistance >= MIN_SCROLL_DISTANCE) {
                        if (currentScrollDirection == 1) { // 上滑
                            Log.d("GestureDebug", "达到滑动阈值，隐藏");
                            scrollHandler.removeCallbacks(hideRunnable);
                            scrollHandler.removeCallbacks(showRunnable);
                            scrollHandler.postDelayed(hideRunnable, SCROLL_DELAY);
                            lastScrollTime = currentTime;
                            totalScrollDistance = 0; // 重置累计距离
                        } else if (currentScrollDirection == 2) { // 下滑
                            Log.d("GestureDebug", "达到滑动阈值，显示");
                            scrollHandler.removeCallbacks(hideRunnable);
                            scrollHandler.removeCallbacks(showRunnable);
                            scrollHandler.postDelayed(showRunnable, SCROLL_DELAY);
                            lastScrollTime = currentTime;
                            totalScrollDistance = 0; // 重置累计距离
                        }
                    }
                }
                return false;
            }
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 快速滑动时立即执行，不延迟
                if (Math.abs(velocityY) > 1500) {
                    scrollHandler.removeCallbacks(hideRunnable);
                    scrollHandler.removeCallbacks(showRunnable);

                    if (velocityY > 0) {
                        smoothShowLayout();
                        isLayoutVisible = true;
                    } else {

                        smoothHideLayout();
                        isLayoutVisible = false;
                    }
                    lastActionTime = System.currentTimeMillis();
                }
                return false;
            }

        });



        // 为右侧PinnedHeaderListView设置触摸监听
        PinnedHeaderListView pinnedListView = findViewById(R.id.pinnedListView);
        pinnedListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);

//                // 触摸结束时取消延迟任务
//                if (event.getAction() == MotionEvent.ACTION_UP ||
//                        event.getAction() == MotionEvent.ACTION_CANCEL) {
//                    scrollHandler.removeCallbacks(hideRunnable);
//                    scrollHandler.removeCallbacks(showRunnable);
//                }

                return false;
            }
        });
    }
    // 平滑显示动画
    private void smoothShowLayout() {
        FrameLayout.LayoutParams imageParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        imageParams.height =heightInPixelsshow;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (yourLinearLayout.getVisibility() != View.VISIBLE) {
                    yourLinearLayout.setVisibility(View.VISIBLE);
                    // 从当前位置动画到0
                    yourLinearLayout.setTranslationY(-yourLinearLayout.getHeight());
                    yourLinearLayout.animate()
                            .translationY(0)
                            .setDuration(400) // 更长的动画时间
                            .setInterpolator(new OvershootInterpolator(0.8f)) // 弹性效果

                            .start();
                }
            }
        });
    }

    // 平滑隐藏动画
    private void smoothHideLayout() {
        Log.d("GestureDebug", "smoothHideLayout被调用");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("GestureDebug", "UI线程执行隐藏, 当前visibility=" + yourLinearLayout.getVisibility());
                if (yourLinearLayout.getVisibility() == View.VISIBLE) {
                    Log.d("GestureDebug", "开始隐藏动画, 布局高度=" + yourLinearLayout.getHeight());
                    yourLinearLayout.animate()
                            .translationY(-yourLinearLayout.getHeight())
                            .setDuration(400) // 更长的动画时间
                            .setInterpolator(new AccelerateInterpolator(1.2f)) // 加速效果
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    yourLinearLayout.setVisibility(View.GONE);
                                    Log.d("GestureDebug", "隐藏动画完成, 设置visibility=GONE");

                                }
                            })
                            .start();
                    FrameLayout.LayoutParams imageParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                    imageParams.height =heightInPixels;
                } else {
                    Log.d("GestureDebug", "布局已经不可见，跳过隐藏");
                }
                }

        });
    }

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

        updateWelcomeText();

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
        // 获取布局引用

        imageView = findViewById(R.id.diandantupian);

        // 在 onCreate 中初始化，此时 Context 已就绪
        heightInPixels = (int) (heightInDp * getResources().getDisplayMetrics().density);
        heightInPixelsshow = (int) (heightInDpshow * getResources().getDisplayMetrics().density);
        orderRefresh();
        initViews();
        setupClickListeners();
//        initData();
//        banner = findViewById(R.id.main_banner);
//
//        banner.setAdapter(new BannerImageAdapter<Integer>(banner_data) {
//
//            @Override
//            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
//                holder.imageView.setImageResource(data);
//            }
//        });
//
//        // 开启循环轮播
//        banner.isAutoLoop(true);
//        banner.setIndicator(new CircleIndicator(this));
//        banner.setScrollBarFadeDuration(1000);
//        // 设置指示器颜色(TODO 即选中时那个小点的颜色)
//        banner.setIndicatorSelectedColor(Color.GREEN);
//        // 开始轮播
//        banner.start();



        // 初始化滑动检测
        initGestureDetection();
    }

//    private void initViews() {
//        btnQuanbu = findViewById(R.id.quanbu);
//        btnZhekou = findViewById(R.id.zhekou);
//        btnRexiao = findViewById(R.id.rexiao);
//        btnXinpin = findViewById(R.id.xinpin);
//
//        // 将所有按钮添加到列表
//        allButtons.add(btnQuanbu);
//        allButtons.add(btnZhekou);
//        allButtons.add(btnRexiao);
//        allButtons.add(btnXinpin);
//
//        // 设置默认选中"全部"按钮
//        setSelectedButton(btnQuanbu);
//    }

    private void initViews() {
        btnQuanbu = findViewById(R.id.quanbu);
        btnZhekou = findViewById(R.id.zhekou);
        btnRexiao = findViewById(R.id.rexiao);
        btnXinpin = findViewById(R.id.xinpin);

        // 将所有按钮添加到列表
        allButtons.add(btnQuanbu);
        allButtons.add(btnZhekou);
        allButtons.add(btnRexiao);
        allButtons.add(btnXinpin);

        // 设置默认选中"全部"按钮
        setSelectedButton(btnQuanbu);

        // 设置点击监听器
        setupClickListeners();
    }

//    private void setupClickListeners() {
//        // 方式一：使用实现接口的方式
//        btnQuanbu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSelectedButton(btnQuanbu);
//            }
//        });
//
//        btnZhekou.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSelectedButton(btnZhekou);
//            }
//        });
//
//        btnRexiao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSelectedButton(btnRexiao);
//            }
//        });
//
//        btnXinpin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSelectedButton(btnXinpin);
//            }
//        });
//    }
private void setupClickListeners() {
    btnQuanbu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelectedButton(btnQuanbu);
            filterProducts("全部"); // 显示所有商品
        }
    });

    btnZhekou.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelectedButton(btnZhekou);
            filterProducts("折扣"); // 筛选折扣商品
        }
    });

    btnRexiao.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelectedButton(btnRexiao);
            filterProducts("热销"); // 筛选热销商品
        }
    });

    btnXinpin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelectedButton(btnXinpin);
            filterProducts("新品"); // 筛选新品商品
        }
    });
}
    private void filterProducts(String filterType) {
        List<YhMendianProductshezhi> filteredList = new ArrayList<>();

        if ("全部".equals(filterType)) {
            // 显示所有商品
            filteredList = product_list;
        } else {
            // 根据beizhu1字段筛选
            for (YhMendianProductshezhi product : product_list) {
                if (filterType.equals(product.getBeizhu1())) {
                    filteredList.add(product);
                }
            }
        }

        // 更新右侧列表数据
        updateRightListData(filteredList);

        // 记录日志便于调试
        Log.d("FilterDebug", "筛选类型: " + filterType + ", 结果数量: " + filteredList.size());
    }
    private void updateRightListData(List<YhMendianProductshezhi> filteredList) {
        try {
            // 重新组织右侧数据
            rightStr = new YhMendianProductshezhi[type_list.size()][];

            for(int i = 0; i < type_list.size(); i++){
                String this_type = type_list.get(i).getType();
                int this_num = 0;

                // 计算该分类下的商品数量
                for(YhMendianProductshezhi product : filteredList){
                    if(product.getType().equals(this_type)){
                        this_num++;
                    }
                }

                if(this_num > 0){
                    rightStr[i] = new YhMendianProductshezhi[this_num];
                    this_num = 0;
                    for(YhMendianProductshezhi product : filteredList){
                        if(product.getType().equals(this_type)){
                            rightStr[i][this_num] = product;
                            this_num++;
                        }
                    }
                } else {
                    rightStr[i] = new YhMendianProductshezhi[0];
                }
            }

            // 刷新适配器
            if (pinnedListView.getAdapter() != null) {
                MainSectionedAdapter adapter = (MainSectionedAdapter) pinnedListView.getAdapter();
                adapter.updateData(rightStr);
                adapter.notifyDataSetChanged();

                // 重置左侧分类选中状态
                resetLeftListSelection();
            }
        } catch (Exception e) {
            Log.e("FilterError", "更新列表数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void resetLeftListSelection() {
        // 重置左侧分类选中状态，默认选中第一个
        if (flagArray != null && flagArray.length > 0) {
            for (int i = 0; i < flagArray.length; i++) {
                flagArray[i] = (i == 0); // 只有第一个选中
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

            // 滚动到顶部
            pinnedListView.setSelection(0);
        }
    }




    private void setSelectedButton(Button selectedButton) {
        // 重置所有按钮状态
        for (Button button : allButtons) {
            if (button == selectedButton) {
                // 设置选中状态
                button.setBackgroundResource(R.drawable.mendian_dingbu_xuanzhongbtn);
                button.setTextColor(Color.parseColor("#E6650F")); // 直接使用橙色
            } else {
                // 设置默认状态
                button.setBackgroundResource(R.drawable.mendian_dingbu_btn);
                button.setTextColor(ContextCompat.getColor(this, R.color.black)); // 默认文字颜色
            }
        }
    }

    private void handleButtonClick(int buttonId) {
        switch (buttonId) {
            case R.id.quanbu:
                setSelectedButton(btnQuanbu);
                filterProducts("全部");
                break;
            case R.id.zhekou:
                setSelectedButton(btnZhekou);
                filterProducts("折扣");
                break;
            case R.id.rexiao:
                setSelectedButton(btnRexiao);
                filterProducts("热销");
                break;
            case R.id.xinpin:
                setSelectedButton(btnXinpin);
                filterProducts("新品");
                break;
        }
    }


    private void huiseButton(View ins_button) {

            ins_button.setEnabled(false);
            ins_button.setBackgroundResource(R.drawable.mendian_jiezhang_huise);

    }


    private void updateButtonState(View ins_button) {
        if (list.size() == 0) {
            ins_button.setEnabled(false);
            ins_button.setBackgroundResource(R.drawable.mendian_jiezhang_huise);
        } else {
            ins_button.setEnabled(true);
            ins_button.setBackgroundResource(R.drawable.mendian_btn_jiezhang);
        }
    }

    private void instantiation() {
        left_listview = findViewById(R.id.left_listview);
        pinnedListView = findViewById(R.id.pinnedListView);

        // 去掉分割线
        pinnedListView.setDivider(null);
        pinnedListView.setDividerHeight(0);

        final MainSectionedAdapter sectionedAdapter = new MainSectionedAdapter(this, leftStr, rightStr);
        // 确保显示轮播图
        sectionedAdapter.setShowBanner(true);

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
                huiseButton(ins_button);
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

//修改ddh自增逻辑
    public void orderRefresh() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    // 1. 生成今天日期的8位字符串
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    String datePart = sdf.format(Calendar.getInstance().getTime()); // 示例："20231215"

                    // 2. 生成订单号
                    if (max_order != null && !max_order.isEmpty() && max_order.get(0).getDdh() != null) {
                        // 有历史订单，获取最大订单号
                        String maxDdh = max_order.get(0).getDdh().trim();
                        Log.d("OrderRefresh", "数据库最大订单号: " + maxDdh + ", 今天日期: " + datePart);

                        // 检查是否是今天的订单
                        if (maxDdh.startsWith(datePart) && maxDdh.length() >= 12) {
                            try {
                                // 提取序列号（后4位）
                                String sequenceStr = maxDdh.substring(8, 12); // 取第9-12位
                                Log.d("OrderRefresh", "提取的序列号: " + sequenceStr);

                                int sequence = Integer.parseInt(sequenceStr); // 转为数字
                                sequence++; // 自增

                                String newSequence = String.format("%04d", sequence); // 格式化为4位
                                String newDdh = datePart + newSequence; // 拼接新订单号

                                Log.d("OrderRefresh", "自增后订单号: " + newDdh);
                                ddh.setText(newDdh);
                            } catch (NumberFormatException e) {
                                // 序列号不是数字，从0001开始
                                Log.e("OrderRefresh", "序列号格式错误: " + e.getMessage());
                                ddh.setText(datePart + "0001");
                            } catch (StringIndexOutOfBoundsException e) {
                                // 字符串长度不够，从0001开始
                                Log.e("OrderRefresh", "订单号长度错误: " + e.getMessage());
                                ddh.setText(datePart + "0001");
                            }
                        } else {
                            // 不是今天的订单，从0001开始
                            Log.d("OrderRefresh", "不是今天订单或格式不符，重新开始");
                            ddh.setText(datePart + "0001");
                        }
                    } else {
                        // 没有历史订单，从0001开始
                        Log.d("OrderRefresh", "没有历史订单，生成第一个");
                        ddh.setText(datePart + "0001");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("OrderRefresh", "生成订单号异常: " + e.getMessage());
                    // 出错时生成默认订单号
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                        ddh.setText(sdf.format(Calendar.getInstance().getTime()) + "0001");
                    } catch (Exception ex) {
                        // 再次出错，设置一个默认值
                        ddh.setText("000000000001");
                    }
                }

                // 3. 初始化左侧分类列表
                if (type_list != null) {
                    leftStr = new String[type_list.size()];
                    for (int i = 0; i < type_list.size(); i++) {
                        leftStr[i] = type_list.get(i).getType();
                    }
                } else {
                    leftStr = new String[0];
                }

                // 4. 初始化右侧商品列表
                if (type_list != null && product_list != null) {
                    rightStr = new YhMendianProductshezhi[type_list.size()][];

                    for(int i = 0; i < type_list.size(); i++){
                        String this_type = leftStr[i];
                        int this_num = 0;

                        // 计算该分类下的商品数量
                        for(int j = 0; j < product_list.size(); j++){
                            if(product_list.get(j).getType().equals(this_type)){
                                this_num = this_num + 1;
                            }
                        }

                        if(this_num > 0){
                            rightStr[i] = new YhMendianProductshezhi[this_num];
                        } else {
                            rightStr[i] = new YhMendianProductshezhi[0];
                        }

                        // 填充该分类下的商品
                        this_num = 0;
                        for(int j = 0; j < product_list.size(); j++){
                            if(product_list.get(j).getType().equals(this_type)){
                                rightStr[i][this_num] = product_list.get(j);
                                this_num = this_num + 1;
                            }
                        }
                    }

                    Log.d("OrderRefresh", "商品分类初始化完成，共" + type_list.size() + "个分类");
                } else {
                    rightStr = new YhMendianProductshezhi[0][];
                    Log.w("OrderRefresh", "商品数据为空，初始化空列表");
                }

                // 5. 实例化列表视图
                instantiation();

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    // 1. 获取今天日期的8位字符串
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    String this_ddh = sdf.format(Calendar.getInstance().getTime());

                    Log.d("OrderRefresh", "线程开始，查询日期: " + this_ddh + ", 公司: " + yhMendianUser.getCompany());

                    // 2. 查询数据库中今天的最大订单号
                    max_order = yhMendianOrdersService.getListDDH(this_ddh, yhMendianUser.getCompany());
                    if (max_order != null) {
                        Log.d("OrderRefresh", "查询到订单数据条数: " + max_order.size());
                        if (!max_order.isEmpty() && max_order.get(0).getDdh() != null) {
                            Log.d("OrderRefresh", "最大订单号: " + max_order.get(0).getDdh());
                        }
                    }

                    // 3. 获取商品分类列表
                    type_list = yhMendianProductshezhiService.getTypeList(yhMendianUser.getCompany());
                    Log.d("OrderRefresh", "商品分类数量: " + (type_list != null ? type_list.size() : 0));

                    // 4. 获取商品列表
                    product_list = yhMendianProductshezhiService.getList("", "", yhMendianUser.getCompany());
                    Log.d("OrderRefresh", "商品总数: " + (product_list != null ? product_list.size() : 0));

                    // 调试：打印商品数据，检查beizhu1字段
                    if (product_list != null) {
                        for (YhMendianProductshezhi product : product_list) {
                            Log.d("ProductDebug", "商品: " + product.getProduct_name() +
                                    ", 分类: " + product.getType() +
                                    ", 标签: " + product.getBeizhu1());
                        }
                    }

                    // 5. 发送消息到主线程
                    Message msg = new Message();
                    msg.obj = max_order;
                    listLoadHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("OrderRefresh", "线程执行异常: " + e.getMessage());

                    // 即使出错也要发送消息，避免界面卡住
                    Message msg = new Message();
                    msg.obj = new ArrayList<YhMendianOrders>(); // 发送空列表
                    listLoadHandler.sendMessage(msg);
                }
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

        }
        textView.setText(list.size() + "");
        updateButtonState(ins_button);
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
//    private void initData(){
//        banner_data = new ArrayList<>();
//        banner_data.add(R.drawable.orderpanel_baokuan);
//        banner_data.add(R.drawable.lunbo1);
//        banner_data.add(R.drawable.lunbo2);
//    }


    private void updateWelcomeText() {
        try {

            android.content.SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);

            // 获取公司名称
            String companyName = sharedPref.getString("companyName", "云合未来");
            Log.d("WelcomeText", "获取到的公司名: " + companyName);

            // 截取前四位
            String displayName;
            if (companyName != null && !companyName.isEmpty()) {
                if (companyName.length() > 4) {
                    displayName = companyName.substring(0, 4);
                } else {
                    displayName = companyName;
                }
            } else {
                displayName = "云合未来"; // 默认值
            }

            // 更新TextView
            TextView huanyingTextView = findViewById(R.id.huanying);
            if (huanyingTextView != null) {
                huanyingTextView.setText(displayName);
                Log.d("WelcomeText", "更新欢迎文本: " + displayName);
            } else {
                Log.e("WelcomeText", "未找到huanying TextView");
            }

        } catch (Exception e) {
            Log.e("WelcomeText", "更新欢迎文本失败: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
