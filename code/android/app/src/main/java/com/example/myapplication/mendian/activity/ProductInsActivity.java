package com.example.myapplication.mendian.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianOrderDetail;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianProductshezhiService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProductInsActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;
    private YhMendianProductshezhi yhMendianProductshezhi;
    private YhMendianProductshezhiService yhMendianProductshezhiService;
    private YhMendianOrderDetail yhMendianOrderDetail;

    private TextView name;
//    private TextView type;
    private TextView danwei;
    private EditText num;
//    private Spinner guige;
//    private Spinner kouwei;
    private TextView price;
    private TextView shuliangTextView;
    private ImageView jianhaoImageView, jiahaoImageView;
    private int currentQuantity = 1; // 默认数量
    private String cplx_text;
    private String cpmc_text;
    private String dw_text;
    private String dj_text;
    private String guige_text;
    private String kouwei_text;
    private TextView jiageTextView;
    private String price_text;
    private LinearLayout guigeContainer;
    private String guigeText;
    private TextView jiarukouwei;
    private List<String> guigeList;
    private int selectedPosition = -1;
    private LinearLayout kouweiContainer;
    private String kouweiText;
    private List<String> kouweiList;
    private int selectedKouweiPosition = -1;
    private Banner banner;
    private List<Integer> banner_data;
    String[] guige_array;
    String[] kouwei_array;
    String[] price_array;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_ins);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianProductshezhi = new YhMendianProductshezhi();
        yhMendianProductshezhiService = new YhMendianProductshezhiService();
        yhMendianOrderDetail = new YhMendianOrderDetail();
        name = findViewById(R.id.name);

//        type = findViewById(R.id.type);
//        num = findViewById(R.id.num);

//        guige = findViewById(R.id.guige);
//        kouwei = findViewById(R.id.kouwei);
        price = findViewById(R.id.price);
        danwei = findViewById(R.id.danwei);
        shuliangTextView = findViewById(R.id.shuliang);
        jiarukouwei = findViewById(R.id.jiaru_kouwei);
        jianhaoImageView = findViewById(R.id.jianhao);
        jiahaoImageView = findViewById(R.id.jiahao);
        // 设置初始数量
        shuliangTextView.setText(String.valueOf(currentQuantity));
        jiageTextView = findViewById(R.id.jiage);
        TextView xiangqingText = findViewById(R.id.xiangqing_text);
        TextView beizhu1 = findViewById(R.id.beizhu1);

        cplx_text = getIntent().getStringExtra("cplx");
        cpmc_text = getIntent().getStringExtra("cpmc");
        dw_text = getIntent().getStringExtra("dw");
        dj_text = getIntent().getStringExtra("dj");
        guige_text = getIntent().getStringExtra("guige");
        kouwei_text = getIntent().getStringExtra("kouwei");
        guigeContainer = findViewById(R.id.guige_container);
        guigeText = getIntent().getStringExtra("guige");
        guige_array = guige_text.split(",");
        kouwei_array = kouwei_text.split(",");
        price_array = dj_text.split(",");

        Log.e("ProductInfo", "页面加载 - 产品名称: " + cpmc_text);
        Toast.makeText(this, "产品名称: " + cpmc_text, Toast.LENGTH_SHORT).show();

        getProduct(cpmc_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, guige_array);
//        guige.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kouwei_array);
//        kouwei.setAdapter(adapter);
        name.setText(cpmc_text);
//        type.setText(cplx_text);
        danwei.setText(dw_text);
//        guige.setOnItemSelectedListener(new typeSelectSelectedListener());
        shuliangTextView.setOnClickListener(v -> {
            showNumberInputDialog();
        });

        kouweiContainer = findViewById(R.id.kouwei_container);

        // 获取口味数据，这里可以根据您的数据来源调整
        kouweiText = getIntent().getStringExtra("kouwei");
        // 如果没有从intent获取到，可以设置默认口味
        if (kouweiText == null) {
            kouweiText = "常温"; // 默认口味
        }


        // 设置加号点击事件
        jiahaoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuantity++;
                shuliangTextView.setText(String.valueOf(currentQuantity));
                updateDisplay();
            }
        });

        // 设置减号点击事件
        jianhaoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuantity > 1) { // 防止数量小于1
                    currentQuantity--;
                    shuliangTextView.setText(String.valueOf(currentQuantity));
                    updateDisplay();
                }
            }
        });
        initGuigeButtons();
        initKouweiButtons();
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

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.mendian_diandan_shangpin);
        banner_data.add(R.drawable.lunbo2);
        banner_data.add(R.drawable.lunbo3);
    }

    private void initGuigeButtons() {
        // 解析规格字符串，假设规格是用逗号分隔的
        if (guigeText != null && !guigeText.isEmpty()) {
            guigeList = Arrays.asList(guigeText.split(","));
        } else {
            guigeList = new ArrayList<>();
            // 或者设置默认规格
            guigeList.add("默认规格");
        }

        // 清空容器
        guigeContainer.removeAllViews();

        // 动态创建按钮
        for (int i = 0; i < guigeList.size(); i++) {
            String guige = guigeList.get(i);

            Button button = new Button(this);

            // 设置布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    dpToPx(30)
            );
            params.setMargins(dpToPx(15), 0, 0, 0);
            button.setLayoutParams(params);

            // 设置按钮文本
            button.setText(guige);
            button.setTag(i); // 保存位置信息

            // 设置按钮样式
            setButtonStyle(button, false);

            // 添加点击事件
            final int position = i;
            button.setOnClickListener(v -> {
                selectGuige(position);
            });

            guigeContainer.addView(button);
        }
        // 关键：默认选择第一个规格
        if (!guigeList.isEmpty()) {
            selectGuige(0); // 选择第一个
        }
    }

    private void selectGuige(int position) {
        selectedPosition = position;

        // 更新所有按钮样式
        for (int i = 0; i < guigeContainer.getChildCount(); i++) {
            View child = guigeContainer.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                setButtonStyle(button, i == position);
            }
        }

        // 获取选中的规格
        String selectedGuige = guigeList.get(position);
        Toast.makeText(this, "已选择: " + selectedGuige, Toast.LENGTH_SHORT).show();

        // 这里可以处理选中的规格
        handleSelectedGuige(selectedGuige);
    }

    private void setButtonStyle(Button button, boolean isSelected) {
        if (isSelected) {
            // 选中状态的样式
            button.setBackgroundResource(R.drawable.guige_button_selected);
            button.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            // 未选中状态的样式
            button.setBackgroundResource(R.drawable.mendian_diandan_chanpin_guige);

            button.setTextColor(Color.parseColor("#854EDD"));

        }

        // 公共样式
        button.setPadding(dpToPx(10), 0, dpToPx(10), 0);
        button.setTextSize(12);
        button.setMinWidth(dpToPx(70));
        button.setMinHeight(dpToPx(30));
        button.setGravity(Gravity.CENTER);
        button.setAllCaps(false);
    }

    private void initKouweiButtons() {
        // 解析口味字符串，假设口味是用逗号分隔的
        if (kouweiText != null && !kouweiText.isEmpty()) {
            String[] kouweiArray = kouweiText.split(",");
            kouweiList = Arrays.asList(kouweiArray);
        } else {
            kouweiList = new ArrayList<>();
            kouweiList.add("原味"); // 默认口味
        }

        // 清空容器
        kouweiContainer.removeAllViews();

        // 动态创建口味按钮
        for (int i = 0; i < kouweiList.size(); i++) {
            String kouwei = kouweiList.get(i);

            Button button = new Button(this);

            // 设置布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    dpToPx(30) // 高度30dp
            );
            // 设置边距：左15dp, 上0dp, 右8dp, 下8dp
            params.setMargins(dpToPx(15), 0, dpToPx(8), dpToPx(8));
            button.setLayoutParams(params);

            // 设置按钮文本
            button.setText(kouwei);
            button.setTag(i); // 保存位置信息

            // 设置按钮样式
            setButtonStyle(button, false);

            // 添加点击事件
            final int position = i;
            button.setOnClickListener(v -> {
                selectKouwei(position);
            });

            kouweiContainer.addView(button);
        }

        // 默认选择第一个口味
        if (!kouweiList.isEmpty()) {
            selectKouwei(0);
        }
    }

    private void selectKouwei(int position) {
        selectedKouweiPosition = position;

        // 更新所有口味按钮样式
        for (int i = 0; i < kouweiContainer.getChildCount(); i++) {
            View child = kouweiContainer.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                setButtonStyle(button, i == position);
            }
        }

        // 获取选中的口味
        String selectedKouwei = kouweiList.get(position);
        Toast.makeText(this, "已选择: " + selectedKouwei, Toast.LENGTH_SHORT).show();
        jiarukouwei.setText(selectedKouwei);
        // 处理选中的口味
        handleSelectedKouwei(selectedKouwei);
    }



    private void handleSelectedKouwei(String selectedKouwei) {
        // 处理选中的口味
        Log.d("Kouwei", "选中的口味: " + selectedKouwei);

        // 这里可以根据选中的口味更新其他UI或数据
        // 例如：更新价格、库存等
    }

    // 获取当前选中的口味
    public String getSelectedKouwei() {
        if (selectedKouweiPosition != -1) {
            return kouweiList.get(selectedKouweiPosition);
        }
        return null;
    }





    // dp转px的工具方法
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void handleSelectedGuige(String selectedGuige) {
        // 处理选中的规格
        // 例如：更新数据、计算价格等
        if (selectedPosition >= 0 && selectedPosition < price_array.length) {
            price.setText(price_array[selectedPosition]);
        } else {
            price.setText("0"); // 索引越界时显示0
        }
        updateDisplay();
    }

    // 获取当前选中的规格
    public String getSelectedGuige() {
        if (selectedPosition != -1) {
            return guigeList.get(selectedPosition);
        }
        return null;
    }

    private void showNumberInputDialog() {
        // 创建自定义布局
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(shuliangTextView.getText().toString());
        input.setSelection(input.getText().length());
        input.setTextSize(18f);

        layout.addView(input);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("输入数量")
                .setView(layout)
                .setPositiveButton("确定", (dialogInterface, which) -> {
                    String numberStr = input.getText().toString().trim();
                    if (!numberStr.isEmpty()) {
                        int number = Integer.parseInt(numberStr);
                        if (number >= 0) {
                            shuliangTextView.setText(String.valueOf(number));
                            currentQuantity=number;
                            updateDisplay();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();

        dialog.show();

    }

    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int this_row = selectedPosition;
            price.setText(price_array[this_row]);
            price_text = price_array[this_row];
            updateDisplay();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

//    private void updateDisplay() {
//        // 更新数量显示
//        shuliangTextView.setText(String.valueOf(currentQuantity));
//
//        // 更新价格显示：
//        float zhje = Float.parseFloat(price.getText().toString()) * Float.parseFloat( shuliangTextView.getText().toString());
//        String totalPrice = Float.toString(zhje);
//        jiageTextView.setText(String.valueOf(totalPrice));
//    }
private void updateDisplay() {
    // 更新数量显示
    shuliangTextView.setText(String.valueOf(currentQuantity));

    // 使用BigDecimal进行精确计算
    try {
        BigDecimal priceValue = new BigDecimal(price.getText().toString());
        BigDecimal quantity = new BigDecimal(currentQuantity);
        BigDecimal total = priceValue.multiply(quantity);

        // 设置小数位数（比如保留2位小数）
        jiageTextView.setText(total.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
    } catch (Exception e) {
        // 如果转换失败，使用原来的方法
        float zhje = Float.parseFloat(price.getText().toString()) * currentQuantity;
        jiageTextView.setText(String.format("%.2f", zhje));
    }
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        ToastUtil.show(ProductInsActivity.this, "已加入购物车");
        MyApplication application = (MyApplication) getApplicationContext();
        application.setYhMendianOrderDetail(yhMendianOrderDetail);
        back();
    }


    private boolean checkForm() throws ParseException {

        if ( shuliangTextView.getText().toString().equals("")) {
            ToastUtil.show(ProductInsActivity.this, "请输入数量");
            return false;
        } else {
            yhMendianOrderDetail.setGs( shuliangTextView.getText().toString());
        }

//        yhMendianOrderDetail.setCplx(type.getText().toString());
        yhMendianOrderDetail.setCpmc(name.getText().toString());
        yhMendianOrderDetail.setDw(danwei.getText().toString());
        yhMendianOrderDetail.setDj(price.getText().toString());
        yhMendianOrderDetail.setDzbl("1");
        yhMendianOrderDetail.setZhdj(price.getText().toString());
        float zhje = Float.parseFloat(price.getText().toString()) * Float.parseFloat( shuliangTextView.getText().toString());
        yhMendianOrderDetail.setZhje(Float.toString(zhje));
        yhMendianOrderDetail.setCompany(yhMendianUser.getCompany());
        return true;
    }


    private int getGuiGePosition(String param) {
        if (guige_array != null) {
            for (int i = 0; i < guige_array.length; i++) {
                if (param.equals(guige_array[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private void getProduct(String name) {
        // 这里实现您的业务逻辑
        Log.d("ProductInfo", "正在获取产品: " + name);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<YhMendianProductshezhi> productList = yhMendianProductshezhiService.getProduct(name);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (productList != null && !productList.isEmpty()) {
                                yhMendianProductshezhi = productList.get(0);
                                Log.d("ProductInfo", "成功获取产品信息: " + yhMendianProductshezhi.toString());

                                updateProductDetail(yhMendianProductshezhi);
                                updateBannerImages(yhMendianProductshezhi);
                            } else {
                                Log.d("ProductInfo", "未找到产品: " + name);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ProductInfo", "获取产品失败: " + e.getMessage());
                }
            }
        }).start();
    }

    private void updateProductDetail(YhMendianProductshezhi product) {
        try {
            TextView xiangqingText = findViewById(R.id.xiangqing_text);
            TextView beizhu1 = findViewById(R.id.beizhu1);

            String detailText = "";
            String beizhuText = "";

             if (product.getXiangqing() != null && !product.getXiangqing().isEmpty()) {
                 detailText = product.getXiangqing();
             }

            if (product.getBeizhu1() != null && !product.getBeizhu1().isEmpty()) {
                beizhuText = product.getBeizhu1();
            }

            // 设置文本
            xiangqingText.setText(detailText);
            beizhu1.setText(beizhuText);

            Log.d("ProductInfo", "已更新产品详情: " + detailText);

        } catch (Exception e) {
            Log.e("ProductInfo", "更新产品详情失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateBannerImages(YhMendianProductshezhi product) {
        try {
            List<Bitmap> imageList = new ArrayList<>(); // 改为Bitmap列表

            // 检查 photo, photo1, photo2 字段
            String photo = getFieldValue(product, "photo");
            String photo1 = getFieldValue(product, "photo1");
            String photo2 = getFieldValue(product, "photo2");

            Log.d("BannerImages", "photo: " + (photo != null ? "有值" : "空"));
            Log.d("BannerImages", "photo1: " + (photo1 != null ? "有值" : "空"));
            Log.d("BannerImages", "photo2: " + (photo2 != null ? "有值" : "空"));

            // 如果有三张图片
            if (isValidBase64(photo) && isValidBase64(photo1) && isValidBase64(photo2)) {
                Log.d("BannerImages", "三张图片都有值，使用三张轮播");
                Bitmap bitmap1 = base64ToBitmap(photo);
                Bitmap bitmap2 = base64ToBitmap(photo1);
                Bitmap bitmap3 = base64ToBitmap(photo2);

                if (bitmap1 != null) imageList.add(bitmap1);
                if (bitmap2 != null) imageList.add(bitmap2);
                if (bitmap3 != null) imageList.add(bitmap3);
            }
            // 如果有两张图片的情况
            else if (isValidBase64(photo) && isValidBase64(photo1)) {
                Log.d("BannerImages", "photo和photo1有值，使用两张轮播");
                Bitmap bitmap1 = base64ToBitmap(photo);
                Bitmap bitmap2 = base64ToBitmap(photo1);

                if (bitmap1 != null) imageList.add(bitmap1);
                if (bitmap2 != null) imageList.add(bitmap2);
            }
            else if (isValidBase64(photo) && isValidBase64(photo2)) {
                Log.d("BannerImages", "photo和photo2有值，使用两张轮播");
                Bitmap bitmap1 = base64ToBitmap(photo);
                Bitmap bitmap2 = base64ToBitmap(photo2);

                if (bitmap1 != null) imageList.add(bitmap1);
                if (bitmap2 != null) imageList.add(bitmap2);
            }
            else if (isValidBase64(photo1) && isValidBase64(photo2)) {
                Log.d("BannerImages", "photo1和photo2有值，使用两张轮播");
                Bitmap bitmap1 = base64ToBitmap(photo1);
                Bitmap bitmap2 = base64ToBitmap(photo2);

                if (bitmap1 != null) imageList.add(bitmap1);
                if (bitmap2 != null) imageList.add(bitmap2);
            }
            // 如果只有photo字段有值
            else if (isValidBase64(photo)) {
                Log.d("BannerImages", "只有photo有值，使用单张图片");
                Bitmap bitmap = base64ToBitmap(photo);
                if (bitmap != null) imageList.add(bitmap);
            }
            else if (isValidBase64(photo1)) {
                Log.d("BannerImages", "只有photo1有值，使用单张图片");
                Bitmap bitmap = base64ToBitmap(photo1);
                if (bitmap != null) imageList.add(bitmap);
            }
            else if (isValidBase64(photo2)) {
                Log.d("BannerImages", "只有photo2有值，使用单张图片");
                Bitmap bitmap = base64ToBitmap(photo2);
                if (bitmap != null) imageList.add(bitmap);
            }
            // 其他情况使用默认图片
            else {
                Log.d("BannerImages", "使用默认轮播图片");
                // 使用默认图片资源
                imageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.mendian_diandan_shangpin));
                imageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.lunbo2));
                imageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.lunbo3));
            }

            // 更新轮播图数据
            updateBannerWithBitmaps(imageList);

            Log.d("BannerImages", "轮播图更新完成，图片数量: " + imageList.size());

        } catch (Exception e) {
            Log.e("BannerImages", "更新轮播图失败: " + e.getMessage());
            e.printStackTrace();
            // 出错时使用默认图片
            useDefaultBannerImages();
        }
    }

    private void updateBannerWithBitmaps(List<Bitmap> bitmaps) {
        if (bitmaps == null || bitmaps.isEmpty()) {
            useDefaultBannerImages();
            return;
        }

        // 设置自定义的Bitmap适配器
        banner.setAdapter(new BannerImageAdapter<Bitmap>(bitmaps) {
            @Override
            public void onBindView(BannerImageHolder holder, Bitmap data, int position, int size) {
                holder.imageView.setImageBitmap(data);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

        // 根据图片数量设置轮播行为
        if (bitmaps.size() == 1) {
            banner.isAutoLoop(false); // 单张图片不自动轮播
        } else {
            banner.isAutoLoop(true); // 多张图片自动轮播
        }

        banner.start();
    }

    /**
     * 使用默认图片
     */
    private void useDefaultBannerImages() {
        List<Integer> defaultImages = new ArrayList<>();
        defaultImages.add(R.drawable.mendian_diandan_shangpin);
        defaultImages.add(R.drawable.lunbo2);
        defaultImages.add(R.drawable.lunbo3);

        banner.setAdapter(new BannerImageAdapter<Integer>(defaultImages) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });
        banner.isAutoLoop(true);
        banner.start();
    }


    /**
     * 通过反射获取字段值
     */
    private String getFieldValue(YhMendianProductshezhi product, String fieldName) {
        try {
            java.lang.reflect.Field field = product.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(product);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            Log.d("FieldValue", "获取字段 " + fieldName + " 失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * Base64字符串转换为Bitmap（如果需要实际转换）
     */
    private Bitmap base64ToBitmap(String base64Str) {
        try {
            if (base64Str == null || base64Str.isEmpty()) {
                return null;
            }

            // 清理Base64字符串（移除可能的数据URI前缀）
            String cleanBase64 = base64Str;
            if (base64Str.contains(",")) {
                cleanBase64 = base64Str.substring(base64Str.indexOf(",") + 1);
            }

            // 解码Base64
            byte[] decodedBytes = android.util.Base64.decode(cleanBase64, android.util.Base64.DEFAULT);

            // 转换为Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap != null) {
                Log.d("Base64ToBitmap", "Base64转换成功，图片尺寸: " + bitmap.getWidth() + "x" + bitmap.getHeight());
            } else {
                Log.e("Base64ToBitmap", "Base64转换失败，无法解析为图片");
            }

            return bitmap;

        } catch (Exception e) {
            Log.e("Base64ToBitmap", "Base64转换失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查是否是有效的base64字符串
     */
    private boolean isValidBase64(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }

        // 移除可能的数据URI前缀
        String cleanStr = str;
        if (str.contains(",")) {
            cleanStr = str.substring(str.indexOf(",") + 1);
        }

        // 检查长度和基本特征
        return cleanStr.length() > 100 &&
                (cleanStr.contains("/") || cleanStr.contains("+") || cleanStr.contains("="));
    }



}
