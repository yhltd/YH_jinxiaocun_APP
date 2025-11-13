package com.example.myapplication.mendian.entity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.mendian.activity.KehuinfoChangeActivity;
import com.example.myapplication.mendian.activity.ProductInsActivity;
import com.lxj.xpopup.core.BasePopupView;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本功能：右侧Adapter
 */
public class MainSectionedAdapter extends SectionedBaseAdapter {
    // 新增的标签常量
    private static final String TAG_NORMAL = "normal";
    private static final String TAG_EMPTY = "empty";
    private static final String TAG_BANNER = "banner";
    private Context mContext;
    private String[] leftStr;
    private YhMendianProductshezhi[][] rightStr;
    BasePopupView popupView;

    // 轮播图相关
    private List<Integer> bannerData;
    private boolean showBanner = true;

    public MainSectionedAdapter(Context context, String[] leftStr, YhMendianProductshezhi[][] rightStr) {
        this.mContext = context;
        this.leftStr = leftStr;
        this.rightStr = rightStr;
        initBannerData(); // 初始化轮播图数据
    }

    // 初始化轮播图数据
    private void initBannerData() {
        bannerData = new ArrayList<>();
        bannerData.add(R.drawable.orderpanel_baokuan);
        bannerData.add(R.drawable.lunbo1);
        bannerData.add(R.drawable.lunbo2);
    }

    // 设置轮播图数据
    public void setBannerData(List<Integer> images) {
        if (images != null) {
            this.bannerData = images;
        }
        notifyDataSetChanged();
    }

    // 设置是否显示轮播图
    public void setShowBanner(boolean showBanner) {
        this.showBanner = showBanner;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int section, int position) {
        if (isBannerPosition(section, position)) {
            return null;
        }
        int actualPosition = getActualPosition(section, position);
        return rightStr[section][actualPosition];

    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr.length;
    }

    @Override
    public int getCountForSection(int section) {
        int originalCount = rightStr[section].length;

        int count = originalCount;

        // 如果是第一个section且需要显示轮播图，在顶部添加一个位置
        if (section == 0 && showBanner) {
            count += 1;
        }

        // 如果是最后一个section，在底部添加空布局
        if (section == getSectionCount() - 1) {
            count += 1;
        }

        return count;
    }

//    @Override
//    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
//        // 检查是否是轮播图位置
//        if (isBannerPosition(section, position)) {
//            return getBannerView(null, parent);  // 传入null，不复用
//        }
//
//        int actualPosition = getActualPosition(section, position);
//        int originalCount = rightStr[section].length;
//
//
//
//        if (section == getSectionCount() - 1 && actualPosition == rightStr[section].length) {
//            return createEmptyView(parent);
//        }
//        RelativeLayout layout = null;
//        if (convertView != null && convertView instanceof RelativeLayout) {
//            // 简单检查：如果是RelativeLayout就复用
//            View testView = convertView.findViewById(R.id.name);
//            if (testView != null) {
//                // 有这个TextView，说明是商品布局，可以复用
//                layout = (RelativeLayout) convertView;
//            } else {
//                // 没有这个TextView，说明是其他布局，重新创建
//                LayoutInflater inflator = LayoutInflater.from(parent.getContext());
//                layout = (RelativeLayout) inflator.inflate(R.layout.order_panel_right_list, parent, false);
//            }
//        } else {
//            LayoutInflater inflator = LayoutInflater.from(parent.getContext());
//            layout = (RelativeLayout) inflator.inflate(R.layout.order_panel_right_list, parent, false);
//        }
//        final YhMendianProductshezhi product = rightStr[section][actualPosition];
//        ((TextView) layout.findViewById(R.id.name)).setText(product.getProduct_name());
//        String priceStr = product.getPrice();
//        if (priceStr != null && !priceStr.isEmpty()) {
//            // 按逗号切割并取第一个值
//            String[] priceArray = priceStr.split(",");
//            String firstPrice = priceArray[0].trim(); // 去除前后空格
//            ((TextView) layout.findViewById(R.id.price)).setText(firstPrice);
//        } else {
//            ((TextView) layout.findViewById(R.id.price)).setText("0"); // 默认值
//        }
//        ((TextView) layout.findViewById(R.id.danwei)).setText(product.getUnit());
//        layout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                String[] specifications_list = product.getSpecifications().split(",");
//                String[] practice_list = product.getPractice().split(",");
//                String[] price_list = product.getPrice().split(",");
//                System.out.println(specifications_list);
//                System.out.println(practice_list);
//                System.out.println(price_list);
//                Toast.makeText(mContext, product.getProduct_name(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mContext, ProductInsActivity.class);
//                intent.putExtra("cplx", product.getType());
//                intent.putExtra("cpmc", product.getProduct_name());
//                intent.putExtra("dw", product.getUnit());
//                intent.putExtra("dj", product.getPrice());
//                intent.putExtra("guige", product.getSpecifications());
//                intent.putExtra("kouwei", product.getPractice());
//                mContext.startActivity(intent);
//            }
//        });
//        return layout;
//    }
    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        // 检查是否是轮播图位置
        if (isBannerPosition(section, position)) {
            return getBannerView(null, parent);  // 传入null，不复用
        }

        int actualPosition = getActualPosition(section, position);
        int originalCount = rightStr[section].length;

        if (section == getSectionCount() - 1 && actualPosition == rightStr[section].length) {
            return createEmptyView(parent);
        }

        RelativeLayout layout = null;
        if (convertView != null && convertView instanceof RelativeLayout) {
            // 简单检查：如果是RelativeLayout就复用
            View testView = convertView.findViewById(R.id.name);
            if (testView != null) {
                // 有这个TextView，说明是商品布局，可以复用
                layout = (RelativeLayout) convertView;
            } else {
                // 没有这个TextView，说明是其他布局，重新创建
                LayoutInflater inflator = LayoutInflater.from(parent.getContext());
                layout = (RelativeLayout) inflator.inflate(R.layout.order_panel_right_list, parent, false);
            }
        } else {
            LayoutInflater inflator = LayoutInflater.from(parent.getContext());
            layout = (RelativeLayout) inflator.inflate(R.layout.order_panel_right_list, parent, false);
        }

        final YhMendianProductshezhi product = rightStr[section][actualPosition];
        ((TextView) layout.findViewById(R.id.name)).setText(product.getProduct_name());
        String priceStr = product.getPrice();
        if (priceStr != null && !priceStr.isEmpty()) {
            // 按逗号切割并取第一个值
            String[] priceArray = priceStr.split(",");
            String firstPrice = priceArray[0].trim(); // 去除前后空格
            ((TextView) layout.findViewById(R.id.price)).setText(firstPrice);
        } else {
            ((TextView) layout.findViewById(R.id.price)).setText("0"); // 默认值
        }
        ((TextView) layout.findViewById(R.id.danwei)).setText(product.getUnit());

        // 新增：加载商品图片到 imageItem
        ImageView imageItem = layout.findViewById(R.id.imageItem);
        loadProductImage(product.getPhoto(), imageItem);

        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String[] specifications_list = product.getSpecifications().split(",");
                String[] practice_list = product.getPractice().split(",");
                String[] price_list = product.getPrice().split(",");
                System.out.println(specifications_list);
                System.out.println(practice_list);
                System.out.println(price_list);
                Toast.makeText(mContext, product.getProduct_name(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ProductInsActivity.class);
                intent.putExtra("cplx", product.getType());
                intent.putExtra("cpmc", product.getProduct_name());
                intent.putExtra("dw", product.getUnit());
                intent.putExtra("dj", product.getPrice());
                intent.putExtra("guige", product.getSpecifications());
                intent.putExtra("kouwei", product.getPractice());
                mContext.startActivity(intent);
            }
        });
        return layout;
    }

    // 新增：图片加载方法
    private void loadProductImage(String base64String, ImageView imageView) {
        if (base64String != null && !base64String.isEmpty() && !base64String.equals("null")) {
            try {
                // 去除 Base64 字符串可能包含的数据URI前缀
                String base64Image = base64String;
                if (base64String.contains(",")) {
                    base64Image = base64String.substring(base64String.indexOf(",") + 1);
                }

                // 解码 Base64 字符串
                byte[] decodedString = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                if (decodedByte != null) {
                    imageView.setImageBitmap(decodedByte);
                } else {
                    // 如果解码失败，显示默认图片
                    imageView.setImageResource(R.drawable.mendian_diandan);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 发生异常时显示默认图片
                imageView.setImageResource(R.drawable.mendian_diandan);
            }
        } else {
            // 没有图片时显示默认图片
            imageView.setImageResource(R.drawable.mendian_diandan);
        }
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
            // 设置悬浮栏背景颜色
            layout.setBackgroundResource(R.drawable.mendian_diandan_xuanfubg);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        // 设置文字颜色

        ((TextView) layout.findViewById(R.id.textItem)).setTextColor(Color.BLACK);
        return layout;
    }




    // 创建空布局的方法
    private View createEmptyView(ViewGroup parent) {
        // 直接创建一个空的 View
        View emptyView = new View(parent.getContext());

        // 设置高度，可以根据需要调整
        ViewGroup.LayoutParams params = emptyView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (100 * parent.getContext().getResources().getDisplayMetrics().density) // 100dp高度
            );
        } else {
            params.height = (int) (100 * parent.getContext().getResources().getDisplayMetrics().density);
        }
        emptyView.setLayoutParams(params);

        return emptyView;
    }




    private void initBanner(Banner banner) {
        banner.setAdapter(new BannerImageAdapter<Integer>(bannerData) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
            }
        });

        // 开启循环轮播
        banner.isAutoLoop(true);
        banner.setIndicator(new CircleIndicator(mContext));
        banner.setScrollBarFadeDuration(1000);
        // 设置指示器颜色
        banner.setIndicatorSelectedColor(Color.GREEN);
        // 开始轮播
        banner.start();
    }

    private View getBannerView(View convertView, ViewGroup parent) {
        if (convertView != null && TAG_BANNER.equals(convertView.getTag())) {
            return convertView;
        }

        View bannerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mendian_lunbo, null);
        bannerView.setTag(TAG_BANNER);

        Banner banner = bannerView.findViewById(R.id.main_banner);
        initBanner(banner);

        return bannerView;
    }

    private boolean isBannerPosition(int section, int position) {
        return section == 0 && position == 0 && showBanner;
    }

    private int getActualPosition(int section, int position) {
        int actualPosition = position;
        if (section == 0 && showBanner) {
            actualPosition = position - 1; // 减去轮播图位置
        }
        return actualPosition;
    }

    public void updateData(YhMendianProductshezhi[][] newData) {
        this.rightStr = newData;
        notifyDataSetChanged();
    }

}



