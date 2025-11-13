package com.example.myapplication.mendian.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianOrders;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianOrdersService;
import com.example.myapplication.mendian.service.YhMendianProductshezhiService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductshezhiActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianProductshezhi yhMendianProductshezhi;
    private YhMendianProductshezhiService yhMendianProductshezhiService;
    private EditText product_name;
    private EditText type;
    private String product_nameText;
    private String typeText;
    private ListView productshezhi_list;
    private Button sel_button;

    List<YhMendianProductshezhi> list;

    private boolean isExpanded = true;
    private LinearLayout seleOut;
    private Button toggleButton;
    private int originalHeight;
    private boolean isAnimating = false; // 防止重复点击

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productshezhi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhMendianProductshezhiService = new YhMendianProductshezhiService();

        //初始化控件
        product_name = findViewById(R.id.product_name);
        type = findViewById(R.id.type);
        productshezhi_list = findViewById(R.id.productshezhi_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        initList();

        seleOut = findViewById(R.id.sele_out);
        toggleButton = findViewById(R.id.toggle_button);

        // 获取原始高度
        seleOut.post(new Runnable() {
            @Override
            public void run() {
                originalHeight = seleOut.getHeight();
                updateButtonText();
            }
        });
        // 或者通过代码设置点击监听
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimating) {
                    toggleContainer();
                }
            }
        });
    }

    private void toggleContainer() {
        if (isAnimating) return;

        isAnimating = true;
        int startHeight = seleOut.getHeight();
        int endHeight = isExpanded ? 0 : originalHeight;

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.setDuration(300);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = seleOut.getLayoutParams();
                layoutParams.height = val;
                seleOut.setLayoutParams(layoutParams);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                isExpanded = !isExpanded;
                updateButtonText();

                // 动画结束后，如果完全收起，可以设置visibility为GONE（可选）
                if (seleOut.getHeight() == 0) {
                    // seleOut.setVisibility(View.GONE);
                } else {
                    // seleOut.setVisibility(View.VISIBLE);
                }
            }
        });

        animator.start();

        // 立即更新按钮文本，提供即时反馈
        updateButtonText();
    }

    private void updateButtonText() {
        if (toggleButton != null) {
            toggleButton.setText(isExpanded ? "收起" : "展开");
        }
    }


    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
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



//    private void initList() {
//        product_nameText = product_name.getText().toString();
//        typeText = type.getText().toString();
//
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                productshezhi_list.setAdapter(StringUtils.cast(msg.obj));
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<HashMap<String, Object>> data = new ArrayList<>();
//                try {
//                    yhMendianProductshezhiService = new YhMendianProductshezhiService();
//                    list = yhMendianProductshezhiService.getList(product_nameText,typeText,yhMendianUser.getCompany());
//                    if (list == null) return;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                for (int i = 0; i < list.size(); i++) {
//                    HashMap<String, Object> item = new HashMap<>();
//                    item.put("product_bianhao", list.get(i).getProduct_bianhao());
//                    item.put("type", list.get(i).getType());
//                    item.put("product_name", list.get(i).getProduct_name());
//                    item.put("unit", list.get(i).getUnit());
//                    item.put("price", list.get(i).getPrice());
//                    item.put("chengben", list.get(i).getChengben());
//                    item.put("xiangqing",list.get(i).getXiangqing());
//                    item.put("photo",list.get(i).getPhoto());
//                    item.put("photo1",list.get(i).getPhoto1());
//                    item.put("photo2",list.get(i).getPhoto2());
//                    item.put("tingyong", list.get(i).getTingyong());
//
//                    data.add(item);
//                }
//
//                SimpleAdapter adapter = new SimpleAdapter(ProductshezhiActivity.this, data, R.layout.productshezhi_row,  new String[]{
//                        "product_bianhao", "type", "product_name", "unit", "price", "chengben", "tingyong",
//                        "xiangqing", "photo", "photo1", "photo2"
//                },
//                        new int[]{
//                                R.id.product_bianhao, R.id.type, R.id.product_name, R.id.unit, R.id.price, R.id.chengben, R.id.tingyong,
//                                R.id.xiangqing, R.id.photo, R.id.photo1, R.id.photo2
//                        }) {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
//                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
//                        linearLayout.setOnLongClickListener(onItemLongClick());
//                        linearLayout.setOnClickListener(updateClick());
//                        linearLayout.setTag(position);
//                        return view;
//                    }
//                };
//                Message msg = new Message();
//                msg.obj = adapter;
//                listLoadHandler.sendMessage(msg);
//
//            }
//        }).start();
//    }

    // 添加 static 关键字
    public static class ProductshezhiAdapter extends BaseAdapter {
        private Context context;
        private List<HashMap<String, Object>> data;
        private View.OnLongClickListener onItemLongClick;
        private View.OnClickListener updateClick;

        public ProductshezhiAdapter(Context context, List<HashMap<String, Object>> data,
                                    View.OnLongClickListener onItemLongClick,
                                    View.OnClickListener updateClick) {
            this.context = context;
            this.data = data;
            this.onItemLongClick = onItemLongClick;
            this.updateClick = updateClick;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.productshezhi_row, parent, false);
                holder = new ViewHolder();

                // 初始化文本视图
                holder.productBianhao = convertView.findViewById(R.id.product_bianhao);
                holder.type = convertView.findViewById(R.id.type);
                holder.productName = convertView.findViewById(R.id.product_name);
                holder.unit = convertView.findViewById(R.id.unit);
                holder.price = convertView.findViewById(R.id.price);
                holder.chengben = convertView.findViewById(R.id.chengben);
                holder.tingyong = convertView.findViewById(R.id.tingyong);
                holder.xiangqing = convertView.findViewById(R.id.xiangqing);
                holder.beizhu1 = convertView.findViewById(R.id.beizhu1);

                // 初始化图片视图
                holder.photo = convertView.findViewById(R.id.photo);
                holder.photo1 = convertView.findViewById(R.id.photo1);
                holder.photo2 = convertView.findViewById(R.id.photo2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, Object> item = data.get(position);

            // 设置文本内容 - 确保所有字段都有值，空值用空字符串占位
            holder.productBianhao.setText(getSafeString(item.get("product_bianhao")));
            holder.type.setText(getSafeString(item.get("type")));
            holder.productName.setText(getSafeString(item.get("product_name")));
            holder.unit.setText(getSafeString(item.get("unit")));
            holder.price.setText(getSafeString(item.get("price")));
            holder.chengben.setText(getSafeString(item.get("chengben")));
            holder.tingyong.setText(getSafeString(item.get("tingyong")));
            holder.xiangqing.setText(getSafeString(item.get("xiangqing")));
            holder.beizhu1.setText(getSafeString(item.get("beizhu1")));

            // 加载 Base64 图片 - 确保图片视图始终可见
            loadBase64Image(getSafeString(item.get("photo")), holder.photo);
            loadBase64Image(getSafeString(item.get("photo1")), holder.photo1);
            loadBase64Image(getSafeString(item.get("photo2")), holder.photo2);

            // 将 position 存储在 ViewHolder 中
            holder.position = position;

            // 设置点击事件到整个 convertView
            convertView.setOnLongClickListener(onItemLongClick);
            convertView.setOnClickListener(updateClick);

            return convertView;
        }

        // 添加安全字符串获取方法
        private String getSafeString(Object value) {
            if (value == null || "null".equals(value)) {
                return "";
            }
            return String.valueOf(value);
        }

        // 修改图片加载方法，确保图片视图始终占用空间
        private void loadBase64Image(String base64String, ImageView imageView) {
            if (base64String != null && !base64String.isEmpty() && !base64String.equals("null")) {
                try {
                    // 去除 Base64 字符串可能包含的数据URI前缀
                    String base64Image = base64String;
                    if (base64String.contains(",")) {
                        base64Image = base64String.substring(base64String.indexOf(",") + 1);
                    }

                    // 解码 Base64 字符串
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    if (decodedByte != null) {
                        imageView.setImageBitmap(decodedByte);
                        imageView.setVisibility(View.VISIBLE);
                    } else {
                        // 设置透明占位图或保持可见但无图片
                        imageView.setImageResource(android.R.color.transparent);
                        imageView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 出错时也保持可见
                    imageView.setImageResource(android.R.color.transparent);
                    imageView.setVisibility(View.VISIBLE);
                }
            } else {
                // 没有图片时设置透明占位图，保持布局一致性
                imageView.setImageResource(android.R.color.transparent);
                imageView.setVisibility(View.VISIBLE);
            }
        }

        // 修改 ViewHolder 类
        static class ViewHolder {
            TextView productBianhao, type, productName, unit, price, chengben, tingyong, xiangqing,beizhu1;
            ImageView photo, photo1, photo2;
            int position; // 添加 position 字段
        }
    }
    // 然后在 initList() 方法中使用自定义 Adapter
    private void initList() {
        product_nameText = product_name.getText().toString();
        typeText = type.getText().toString();

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                productshezhi_list.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhMendianProductshezhiService = new YhMendianProductshezhiService();
                    list = yhMendianProductshezhiService.getList(product_nameText, typeText, yhMendianUser.getCompany());
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("product_bianhao", list.get(i).getProduct_bianhao() != null ? list.get(i).getProduct_bianhao() : "");
                    item.put("type", list.get(i).getType() != null ? list.get(i).getType() : "");
                    item.put("product_name", list.get(i).getProduct_name() != null ? list.get(i).getProduct_name() : "");
                    item.put("unit", list.get(i).getUnit() != null ? list.get(i).getUnit() : "");
                    item.put("price", list.get(i).getPrice() != null ? list.get(i).getPrice() : "");
                    item.put("chengben", list.get(i).getChengben() != null ? list.get(i).getChengben() : "");
                    item.put("xiangqing", list.get(i).getXiangqing() != null ? list.get(i).getXiangqing() : "");
                    item.put("beizhu1", list.get(i).getBeizhu1() != null ? list.get(i).getBeizhu1() : "");
                    item.put("photo", list.get(i).getPhoto() != null ? list.get(i).getPhoto() : "");
                    item.put("photo1", list.get(i).getPhoto1() != null ? list.get(i).getPhoto1() : "");
                    item.put("photo2", list.get(i).getPhoto2() != null ? list.get(i).getPhoto2() : "");
                    item.put("tingyong", list.get(i).getTingyong() != null ? list.get(i).getTingyong() : "");

                    data.add(item);
                }

                // 使用自定义的 ProductshezhiAdapter 替换 SimpleAdapter
                ProductshezhiAdapter adapter = new ProductshezhiAdapter(
                        ProductshezhiActivity.this,
                        data,
                        onItemLongClick(),
                        updateClick()
                );

                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

//    public View.OnLongClickListener onItemLongClick() {
//        return new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ProductshezhiActivity.this);
//                int position = Integer.parseInt(view.getTag().toString());
//                Handler deleteHandler = new Handler(new Handler.Callback() {
//                    @Override
//                    public boolean handleMessage(@NonNull Message msg) {
//                        if ((boolean) msg.obj) {
//                            ToastUtil.show(ProductshezhiActivity.this, "删除成功");
//                            initList();
//                        }
//                        return true;
//                    }
//                });
//
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Message msg = new Message();
//                                msg.obj = yhMendianProductshezhiService.deleteByProductshezhi(list.get(position).getId());
//                                deleteHandler.sendMessage(msg);
//                            }
//                        }).start();
//                    }
//                });
//
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.setMessage("确定删除吗？");
//                builder.setTitle("提示");
//                builder.show();
//                return true;
//            }
//        };
//    }
//
//    public View.OnClickListener updateClick() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = Integer.parseInt(view.getTag().toString());
//                Intent intent = new Intent(ProductshezhiActivity.this, ProductshezhiChangeActivity.class);
//                intent.putExtra("type", R.id.update_btn);
//                MyApplication myApplication = (MyApplication) getApplication();
//                myApplication.setObj(list.get(position));
//                startActivityForResult(intent, REQUEST_CODE_CHANG);
//            }
//        };
//    }

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // 从 ViewHolder 中获取 position
                ProductshezhiAdapter.ViewHolder holder = (ProductshezhiAdapter.ViewHolder) view.getTag();
                if (holder == null) {
                    return true;
                }

                int position = holder.position;
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductshezhiActivity.this);

                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(ProductshezhiActivity.this, "删除成功");
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
                                msg.obj = yhMendianProductshezhiService.deleteByProductshezhi(list.get(position).getId());
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

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 从 ViewHolder 中获取 position
                ProductshezhiAdapter.ViewHolder holder = (ProductshezhiAdapter.ViewHolder) view.getTag();
                if (holder == null) {
                    return;
                }

                int position = holder.position;
                Intent intent = new Intent(ProductshezhiActivity.this, ProductshezhiChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(ProductshezhiActivity.this, ProductshezhiChangeActivity.class);
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
