package com.example.myapplication.scheduling.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.entity.SystemBanner;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.PushNews;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.scheduling.service.PushNewsService;
import com.example.myapplication.service.SystemService;
import com.example.myapplication.utils.MarqueeTextView;
import com.example.myapplication.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SchedulingActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private DepartmentService departmentService;
    private List<Department> list;
    private boolean pd;

    private SystemService systemService;
    private PushNewsService pushNewsService;

    private Banner banner;
    private List<SystemBanner> list1;
    private List<SystemBanner> list2;
    private List<Integer> banner_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling_main);


        pushNewsService = new PushNewsService(this);
        loadPushNewsData();




        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        systemService = new SystemService();
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

        init();
        systeminit();

        LinearLayout module = findViewById(R.id.module);
        module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("模块单位") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, ModuleActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout time = findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("工作时间及休息日") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, TimeConfigActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout bom = findViewById(R.id.bom);
        bom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("BOM") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, BomActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("订单") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, OrderActivity.class);
                    startActivity(intent);
                }
            }
        });

//        LinearLayout orderCheck = findViewById(R.id.work_check);
//        orderCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pd = false;
//                for (Department department : list) {
//                    if (department.getView_name().equals("排产核对") && department.getSel().equals("是")) {
//                        myApplication.setPcDepartment(department);
//                        pd = true;
//                    }
//                }
//                if (!pd) {
//                    ToastUtil.show(SchedulingActivity.this, "无权限！");
//                } else {
//                    Intent intent = new Intent(SchedulingActivity.this, OrderCheckActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

        LinearLayout department = findViewById(R.id.department);
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("部门") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, DepartmentActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout renyuan = findViewById(R.id.paiban_renyuan);
        renyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("人员信息") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, PaibanRenyuanActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout paiban = findViewById(R.id.paiban);
        paiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("排班") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, PaibanInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout paiban_detail = findViewById(R.id.paiban_detail);
        paiban_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("排班明细") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, PaibanDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout user = findViewById(R.id.user_info);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("账号管理") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        });

//        LinearLayout work = findViewById(R.id.work);
//        work.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pd = false;
//                for (Department department : list) {
//                    if (department.getView_name().equals("排产") && department.getSel().equals("是")) {
//                        myApplication.setPcDepartment(department);
//                        pd = true;
//                    }
//                }
//                if (!pd) {
//                    ToastUtil.show(SchedulingActivity.this, "无权限！");
//                } else {
//                    Intent intent = new Intent(SchedulingActivity.this, WorkActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

//        LinearLayout summary = findViewById(R.id.summary);
//        summary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pd = false;
//                for (Department department : list) {
//                    if (department.getView_name().equals("汇总") && department.getSel().equals("是")) {
//                        myApplication.setPcDepartment(department);
//                        pd = true;
//                    }
//                }
//                if (!pd) {
//                    ToastUtil.show(SchedulingActivity.this, "无权限！");
//                } else {
//                    Intent intent = new Intent(SchedulingActivity.this, SummaryActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

        LinearLayout shengchanxian = findViewById(R.id.shengchanxian);
        shengchanxian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("生产线") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, ShengChanXianActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout paichan = findViewById(R.id.paichan);
        paichan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("排产") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, PaiChanListActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout grzx = findViewById(R.id.grzx);
        grzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, GrzxActivity.class);
                startActivity(intent);
            }
        });
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出到登录页面", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.paichan_banner_01);
        banner_data.add(R.drawable.paichan_banner_01);
        banner_data.add(R.drawable.paichan_banner_01);
    }

    private void systeminit() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MarqueeTextView marqueeTextView = findViewById(R.id.marquee);
                if(list1.size() > 0){
                    marqueeTextView.setText(list1.get(0).getText());
                }else if(list2.size() > 0){
                    marqueeTextView.setText(list2.get(0).getText());
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    systemService = new SystemService();
                    list1 = systemService.getList("排产",userInfo.getCompany());
                    list2 = systemService.getTongYongList("排产");
                    if (list1 == null && list2 == null) return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = null;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    private void init() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    departmentService = new DepartmentService();
                    list = departmentService.getList(userInfo.getCompany(), userInfo.getDepartment_name(), "");
                    if (list == null) return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = null;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    // 全局变量
    private Handler carouselHandler;
    private Runnable carouselRunnable;
    private int currentCarouselIndex = 0;
    private List<Bitmap> carouselBitmaps = new ArrayList<>();
    private boolean isContentVisible = true;
    private Dialog floatingDialog;
    private Handler marqueeHandler;
    private boolean isMarqueeRunning = false;
    private String currentMarqueeText = "";

    private void loadPushNewsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取数据
                    List<PushNews> result = pushNewsService.getList();

                    // 处理返回数据
                    if (result != null && !result.isEmpty()) {
                        PushNews news = result.get(0);

                        String beizhu1 = news.getBeizhu1();
                        if (beizhu1 != null && "隐藏广告".equals(beizhu1.trim())) {
                            System.out.println("DEBUG: beizhu1字段为'隐藏广告'，直接隐藏所有内容");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideAllContent();
                                }
                            });
                            return; // 🆕 直接返回，不再执行后续逻辑
                        }

                        // 处理textbox内容
                        final String[] textboxContentArr = new String[1];
                        textboxContentArr[0] = news.getTextbox();
                        if (textboxContentArr[0] == null || textboxContentArr[0].trim().isEmpty()) {
                            textboxContentArr[0] = "暂无公告信息";
                        }
                        System.out.println("DEBUG: 获取到textbox内容: " + textboxContentArr[0]);

                        // 🆕【解决位置3】先设置跑马灯内容，只设置一次
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupMarqueeText(textboxContentArr[0]);
                            }
                        });

                        // 收集tptop2-tptop6的图片数据用于轮播图
                        final List<String> tptopImages = new ArrayList<>();

                        if (news.getTptop2() != null && !news.getTptop2().trim().isEmpty()) {
                            tptopImages.add(news.getTptop2());
                            System.out.println("DEBUG: 获取到tptop2图片数据");
                        }
                        if (news.getTptop3() != null && !news.getTptop3().trim().isEmpty()) {
                            tptopImages.add(news.getTptop3());
                            System.out.println("DEBUG: 获取到tptop3图片数据");
                        }
                        if (news.getTptop4() != null && !news.getTptop4().trim().isEmpty()) {
                            tptopImages.add(news.getTptop4());
                            System.out.println("DEBUG: 获取到tptop4图片数据");
                        }
                        if (news.getTptop5() != null && !news.getTptop5().trim().isEmpty()) {
                            tptopImages.add(news.getTptop5());
                            System.out.println("DEBUG: 获取到tptop5图片数据");
                        }
                        if (news.getTptop6() != null && !news.getTptop6().trim().isEmpty()) {
                            tptopImages.add(news.getTptop6());
                            System.out.println("DEBUG: 获取到tptop6图片数据");
                        }

                        // 处理tptop1图片数据用于悬浮
                        final String tptop1Data = news.getTptop1();

                        System.out.println("DEBUG: 总共获取到 " + tptopImages.size() + " 个轮播图片");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 🆕【解决位置4】跑马灯已经在上面设置过了，这里不再重复设置
                                // 只处理轮播图和悬浮图片

                                // 显示轮播图到第一个内容区，传入news对象
                                if (!tptopImages.isEmpty()) {
                                    showCarouselContent(tptopImages, news);
                                }

                                if (tptop1Data != null && !tptop1Data.trim().isEmpty()) {
                                    showFloatingImage(tptop1Data, news);
                                }
                            }
                        });
                    } else {
                        System.out.println("DEBUG: 返回数据: 空数组");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupMarqueeText("暂无公告信息");
                                hideAllContent();
                            }
                        });
                    }

                } catch (Exception e) {
                    System.out.println("DEBUG: 操作失败 - " + e.getMessage());
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupMarqueeText("数据加载失败");
                        }
                    });
                }
            }
        }).start();
    }

    // 显示轮播图Dialog
    private void showCarouselContent(List<String> base64Images, PushNews news) {
        // 先解码所有图片
        carouselBitmaps.clear();
        for (String base64Data : base64Images) {
            try {
                Bitmap bitmap = decodeBase64ToBitmap(base64Data);
                if (bitmap != null) {
                    carouselBitmaps.add(bitmap);
                }
            } catch (Exception e) {
                System.out.println("DEBUG: 图片解码失败: " + e.getMessage());
            }
        }

        if (carouselBitmaps.isEmpty()) {
            System.out.println("DEBUG: 没有有效的图片数据");
            return;
        }

        // 停止之前的轮播
        stopCarousel();

        // 获取第一个内容区的组件（轮播图）
        RelativeLayout topContent1 = findViewById(R.id.top_content_1);
        ImageView carouselImage = findViewById(R.id.carousel_image);
        TextView indicatorText = findViewById(R.id.indicator_text);
        TextView closeButton = findViewById(R.id.carousel_close_button);

        // 根据topgao字段动态设置高度（单位：dp）
        String topgao = news.getTopgao();
        if (topgao != null && !topgao.trim().isEmpty()) {
            try {
                int heightDp = Integer.parseInt(topgao.trim());
                // 将dp转换为px
                int heightPx = dpToPx(heightDp);

                // 设置ImageView高度
                ViewGroup.LayoutParams imageParams = carouselImage.getLayoutParams();
                imageParams.height = heightPx;
                carouselImage.setLayoutParams(imageParams);

                System.out.println("DEBUG: 设置轮播图高度为: " + heightDp + "dp (" + heightPx + "px)");
            } catch (NumberFormatException e) {
                System.out.println("DEBUG: topgao格式错误，使用默认高度");
                // 使用默认高度150dp
                int defaultHeightPx = dpToPx(150);
                ViewGroup.LayoutParams imageParams = carouselImage.getLayoutParams();
                imageParams.height = defaultHeightPx;
                carouselImage.setLayoutParams(imageParams);
            }
        } else {
            // 如果没有topgao数据，使用默认高度150dp
            int defaultHeightPx = dpToPx(150);
            ViewGroup.LayoutParams imageParams = carouselImage.getLayoutParams();
            imageParams.height = defaultHeightPx;
            carouselImage.setLayoutParams(imageParams);
            System.out.println("DEBUG: topgao为空，使用默认高度150dp");
        }

        // 显示第一个内容区（轮播图）
        topContent1.setVisibility(View.VISIBLE);

        // 显示第一张图片
        currentCarouselIndex = 0;
        updateCarouselImage(carouselImage, indicatorText);

        // 设置关闭按钮 - 隐藏轮播图和跑马灯
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllContent();
            }
        });

        // 🆕【解决位置1】移除了在轮播图方法中重新调用跑马灯的逻辑
        // 跑马灯已经在 loadPushNewsData 中设置过了，这里不需要重复设置

        // 开始轮播
        startCarousel(carouselImage, indicatorText);

        System.out.println("DEBUG: 轮播图显示成功，共 " + carouselBitmaps.size() + " 张图片");
    }

    // dp转px的工具方法
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // 解码base64为Bitmap
    private Bitmap decodeBase64ToBitmap(String base64Data) {
        try {
            String cleanedData = base64Data.trim();
            String base64Image;

            // 检测是否包含data:image前缀
            if (cleanedData.contains("base64,")) {
                base64Image = cleanedData.split(",")[1];
            } else {
                base64Image = cleanedData;
            }

            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            System.out.println("DEBUG: 解码base64失败: " + e.getMessage());
            return null;
        }
    }

    // 更新轮播图显示
    private void updateCarouselImage(ImageView imageView, TextView indicatorText) {
        if (carouselBitmaps.isEmpty() || imageView == null || indicatorText == null) return;

        // 🆕 在图片切换前保存跑马灯焦点状态
        TextView marqueeView = findViewById(R.id.top_content_2);
        boolean hadFocus = marqueeView != null && marqueeView.isFocused();

        imageView.setImageBitmap(carouselBitmaps.get(currentCarouselIndex));
        indicatorText.setText((currentCarouselIndex + 1) + "/" + carouselBitmaps.size());

        // 🆕 恢复跑马灯焦点
        if (hadFocus && marqueeView != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (marqueeView.getVisibility() == View.VISIBLE) {
                        marqueeView.requestFocus();
                    }
                }
            }, 50);
        }
    }

    // 开始轮播
    private void startCarousel(final ImageView imageView, final TextView indicatorText) {
        carouselHandler = new Handler();

        carouselRunnable = new Runnable() {
            @Override
            public void run() {
                RelativeLayout carouselContainer = findViewById(R.id.top_content_1);

                if (carouselContainer != null &&
                        carouselContainer.getVisibility() == View.VISIBLE &&
                        !carouselBitmaps.isEmpty()) {

                    // 切换到下一张图片
                    currentCarouselIndex = (currentCarouselIndex + 1) % carouselBitmaps.size();
                    updateCarouselImage(imageView, indicatorText);

                    // 🆕 增加轮播间隔，减少对跑马灯的干扰
                    carouselHandler.postDelayed(this, 5000); // 从3秒增加到5秒
                }
            }
        };

        // 🆕 增加初始延迟
        carouselHandler.postDelayed(carouselRunnable, 5000);
    }

    // 停止轮播
    private void stopCarousel() {
        if (carouselHandler != null && carouselRunnable != null) {
            carouselHandler.removeCallbacks(carouselRunnable);
        }
    }

    // 隐藏所有内容（轮播图和跑马灯）
    private void hideAllContent() {
        isContentVisible = false;

        // 隐藏第一个内容区（轮播图）
        RelativeLayout topContent1 = findViewById(R.id.top_content_1);
        if (topContent1 != null) {
            topContent1.setVisibility(View.GONE);
        }

        // 隐藏第二个内容区（跑马灯）
        TextView topContent2 = findViewById(R.id.top_content_2);
        if (topContent2 != null) {
            topContent2.setVisibility(View.GONE);
        }

        // 停止轮播
        stopCarousel();

        System.out.println("DEBUG: 已隐藏轮播图和跑马灯");
    }

    // 设置跑马灯文本的方法
    private void setupMarqueeText(String text) {
        TextView topContent2 = findViewById(R.id.top_content_2);

        if (topContent2 != null) {
            // 根据显示状态设置可见性
            if (isContentVisible) {
                topContent2.setVisibility(View.VISIBLE);

                // 🆕 保存当前文本
                currentMarqueeText = text;

                // 🆕 设置文本
                topContent2.setText(text);

                // 🆕 停止之前的跑马灯
                stopMarquee();

                // 🆕 延迟启动稳定的跑马灯
                startStableMarquee(topContent2);

            } else {
                topContent2.setVisibility(View.GONE);
                stopMarquee();
            }
        }
    }

    private void startStableMarquee(final TextView marqueeTextView) {
        if (marqueeHandler == null) {
            marqueeHandler = new Handler();
        }

        // 清除之前的回调
        marqueeHandler.removeCallbacksAndMessages(null);

        // 第一步：初始重置
        marqueeHandler.post(new Runnable() {
            @Override
            public void run() {
                if (marqueeTextView != null && marqueeTextView.getVisibility() == View.VISIBLE) {
                    marqueeTextView.setSelected(false);
                }
            }
        });

        // 第二步：配置跑马灯属性
        marqueeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (marqueeTextView != null && marqueeTextView.getVisibility() == View.VISIBLE) {
                    // 配置跑马灯属性
                    marqueeTextView.setSingleLine(true);
                    marqueeTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    marqueeTextView.setMarqueeRepeatLimit(-1);
                    marqueeTextView.setFocusable(true);
                    marqueeTextView.setFocusableInTouchMode(true);
                    marqueeTextView.setHorizontallyScrolling(true);
                    marqueeTextView.setTextSize(16);
                    marqueeTextView.setTextColor(Color.BLACK);
                }
            }
        }, 50);

        // 第三步：启动跑马灯
        marqueeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (marqueeTextView != null && marqueeTextView.getVisibility() == View.VISIBLE) {
                    marqueeTextView.setSelected(true);
                    marqueeTextView.requestFocus();
                    isMarqueeRunning = true;
                    System.out.println("DEBUG: 跑马灯稳定启动: " + currentMarqueeText);
                }
            }
        }, 100);

        // 第四步：确认运行
        marqueeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (marqueeTextView != null && marqueeTextView.getVisibility() == View.VISIBLE && !marqueeTextView.isSelected()) {
                    marqueeTextView.setSelected(true);
                    System.out.println("DEBUG: 跑马灯确认运行");
                }
            }
        }, 300);
    }

    // 🆕 新增：停止跑马灯
    private void stopMarquee() {
        if (marqueeHandler != null) {
            marqueeHandler.removeCallbacksAndMessages(null);
        }

        TextView topContent2 = findViewById(R.id.top_content_2);
        if (topContent2 != null) {
            topContent2.setSelected(false);
        }

        isMarqueeRunning = false;
        System.out.println("DEBUG: 跑马灯已停止");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 🆕 恢复时重新启动跑马灯
        restartMarqueeIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 🆕 暂停时停止跑马灯
        stopMarquee();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 🆕 销毁时清理资源
        stopMarquee();
        stopCarousel();
        if (marqueeHandler != null) {
            marqueeHandler.removeCallbacksAndMessages(null);
        }
        if (carouselHandler != null) {
            carouselHandler.removeCallbacksAndMessages(null);
        }
    }

    // 🆕 新增：如果需要时重新启动跑马灯
    private void restartMarqueeIfNeeded() {
        if (isContentVisible && currentMarqueeText != null && !currentMarqueeText.isEmpty()) {
            TextView topContent2 = findViewById(R.id.top_content_2);
            if (topContent2 != null && topContent2.getVisibility() == View.VISIBLE) {
                startStableMarquee(topContent2);
            }
        }
    }

    // 显示悬浮Dialog
    private void showFloatingImage(String base64Data, PushNews news) {
        // 获取悬浮容器
        RelativeLayout floatingContainer = findViewById(R.id.floating_container);
        ImageView floatingImage = findViewById(R.id.floating_image);
        TextView floatingClose = findViewById(R.id.dialog_close_button);

        // 解码图片
        Bitmap bitmap = decodeBase64ToBitmap(base64Data);
        if (bitmap == null) {
            System.out.println("DEBUG: 悬浮图片解码失败");
            return;
        }

        // 设置图片
        floatingImage.setImageBitmap(bitmap);

        // 设置宽度
        String xuankuan = news.getXuankuan();
        int widthDp = 300;
        if (xuankuan != null && !xuankuan.trim().isEmpty()) {
            try {
                widthDp = Integer.parseInt(xuankuan.trim());
            } catch (NumberFormatException e) {
                // 使用默认宽度
            }
        }

        ViewGroup.LayoutParams params = floatingImage.getLayoutParams();
        params.width = dpToPx(widthDp);
        floatingImage.setLayoutParams(params);

        // 设置关闭按钮
        floatingClose.setOnClickListener(v -> {
            floatingContainer.setVisibility(View.GONE);
            stopFloatingAnimation();
        });

        // 显示容器并开始浮动
        floatingContainer.setVisibility(View.VISIBLE);
        startRandomFloating(floatingContainer, news);
    }

    // 自动浮动动画（使用固定位置数组方案）
    private void startRandomFloating(final View floatingView, final PushNews news) {
        final Handler handler = new Handler();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int widthDp = 300;
        String xuankuan = news.getXuankuan();
        if (xuankuan != null && !xuankuan.trim().isEmpty()) {
            try {
                widthDp = Integer.parseInt(xuankuan.trim());
            } catch (NumberFormatException e) {
                // 使用默认
            }
        }
        int widthPx = dpToPx(widthDp);
        int heightPx = floatingView.getHeight();

        int maxX = size.x - widthPx;
        int maxY = size.y - heightPx;
        int margin = 20;

        // 🆕 调整移动范围，整体上移（在上半屏和中部移动）
        int safeMinX = margin;
        int safeMinY = margin;
        int safeMaxX = maxX - margin;
        int safeMaxY = maxY * 2 / 3; // 🆕 限制在下三分之二区域（避免太靠底部）

        // 🆕 定义更丝滑的八字形路线点
        final List<float[]> routePoints = new ArrayList<>();
        int centerX = safeMinX + (safeMaxX - safeMinX) / 2;
        int centerY = safeMinY + (safeMaxY - safeMinY) / 3; // 🆕 中心点上移
        int radiusX = (safeMaxX - safeMinX) / 4; // 🆕 减小水平幅度
        int radiusY = (safeMaxY - safeMinY) / 6; // 🆕 减小垂直幅度

        // 🆕 增加路径点数量，使移动更丝滑
        for (int i = 0; i <= 32; i++) { // 🆕 从16个点增加到32个点
            double angle = Math.PI * i / 16; // 🆕 调整角度计算
            float x = centerX + (float)(radiusX * Math.sin(angle));
            float y = centerY + (float)(radiusY * Math.sin(2 * angle));
            routePoints.add(new float[]{x, y});
        }

        // 🆕 设置初始位置为路径中的随机点
        Random random = new Random();
        int startIndex = random.nextInt(routePoints.size());
        floatingView.setX(routePoints.get(startIndex)[0]);
        floatingView.setY(routePoints.get(startIndex)[1]);

        final int totalPoints = routePoints.size();

        final Runnable routeRunnable = new Runnable() {
            private int currentPointIndex = startIndex;

            @Override
            public void run() {
                if (floatingView.getVisibility() == View.VISIBLE) {
                    currentPointIndex = (currentPointIndex + 1) % totalPoints;
                    float[] targetPoint = routePoints.get(currentPointIndex);

                    // 🆕 使用更丝滑的动画参数
                    floatingView.animate()
                            .x(targetPoint[0])
                            .y(targetPoint[1])
                            .setDuration(1200) // 🆕 增加动画时长，更丝滑
                            .setInterpolator(new LinearInterpolator()) // 🆕 使用线性插值器，匀速移动
                            .start();

                    // 🆕 调整移动频率
                    handler.postDelayed(this, 1300); // 🆕 缩短间隔，连续移动
                }
            }
        };

        handler.postDelayed(routeRunnable, 800);
    }

    // 停止浮动动画
    private void stopFloatingAnimation() {
        View floatingView = findViewById(R.id.floating_container);
        if (floatingView != null) {
            // 🆕 停止所有动画
            floatingView.animate().cancel();
        }
        // 清除所有回调
        new Handler().removeCallbacksAndMessages(null);
    }

}
