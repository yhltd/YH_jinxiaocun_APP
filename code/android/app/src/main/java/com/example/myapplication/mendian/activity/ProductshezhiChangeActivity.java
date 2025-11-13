package com.example.myapplication.mendian.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianOrders;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianOrdersService;
import com.example.myapplication.mendian.service.YhMendianProductshezhiService;
import com.example.myapplication.mendian.service.YhMendianUserService;
import com.example.myapplication.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.List;

public class ProductshezhiChangeActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;

    private YhMendianProductshezhi yhMendianProductshezhi;
    private YhMendianProductshezhiService yhMendianProductshezhiService;

    private EditText product_bianhao;
    private EditText type;
    private EditText product_name;
    private EditText unit;
    private EditText price;
    private EditText chengben;
    private EditText specifications;
    private EditText practice;
    private EditText xiangqing;
    private Spinner beizhu1;
    private Spinner tingyong;
    private EditText photo;

    List<YhMendianProductshezhi> List;

    String[] tingyong_selectArray;
    String[] beizhu1_selectArray;

    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private static final int REQUEST_CODE_PICK_IMAGE1 = 1002;
    private static final int REQUEST_CODE_PICK_IMAGE2 = 1003;

    // 图片预览视图 - 对应数据库字段
    private ImageView photoPreview;      // 对应 photo
    private ImageView photo1Preview;     // 对应 photo1
    private ImageView photo2Preview;     // 对应 photo2

    // 选择按钮
    private Button selectPhoto, selectPhoto1, selectPhoto2;

    // 移除按钮
    private Button removePhoto, removePhoto1, removePhoto2;

    // 图片Bitmap和Base64字符串 - 对应数据库字段
    private Bitmap photoBitmap;          // 对应 photo
    private Bitmap photo1Bitmap;         // 对应 photo1
    private Bitmap photo2Bitmap;         // 对应 photo2

    private String photoBase64 = "";     // 对应 photo 字段
    private String photo1Base64 = "";    // 对应 photo1 字段
    private String photo2Base64 = "";    // 对应 photo2 字段

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productshezhi_change);

        initControls();  // 先调用这个

        // 然后初始化下拉选项
        initSpinners();  // 再调用这个

        initViews();
        setupImageSelection();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianProductshezhi = new YhMendianProductshezhi();
        yhMendianProductshezhiService = new YhMendianProductshezhiService();

        product_bianhao = findViewById(R.id.product_bianhao);
        type = findViewById(R.id.type);
        product_name = findViewById(R.id.product_name);
        unit = findViewById(R.id.unit);
        price = findViewById(R.id.price);
        chengben = findViewById(R.id.chengben);
        specifications = findViewById(R.id.specifications);
        practice = findViewById(R.id.practice);
        xiangqing = findViewById(R.id.xiangqing);
        beizhu1 = findViewById(R.id.beizhu1);
        tingyong = findViewById(R.id.tingyong);
        photo = findViewById(R.id.photo);

        tingyong_selectArray = getResources().getStringArray(R.array.tingyong_list);
        beizhu1_selectArray = getResources().getStringArray(R.array.beizhu1_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tingyong_selectArray);
        tingyong.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhMendianProductshezhi = new YhMendianProductshezhi();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhMendianProductshezhi = (YhMendianProductshezhi) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            product_bianhao.setText(yhMendianProductshezhi.getProduct_bianhao());
            type.setText(yhMendianProductshezhi.getType());
            product_name.setText(yhMendianProductshezhi.getProduct_name());
            unit.setText(yhMendianProductshezhi.getUnit());
            price.setText(yhMendianProductshezhi.getPrice());
            chengben.setText(yhMendianProductshezhi.getChengben());
            specifications.setText(yhMendianProductshezhi.getSpecifications());
            practice.setText(yhMendianProductshezhi.getPractice());
            xiangqing.setText(yhMendianProductshezhi.getXiangqing());
            beizhu1.setSelection(getSpinnerPosition(beizhu1_selectArray, yhMendianProductshezhi.getBeizhu1()));
            tingyong.setSelection(gettingyongPosition(yhMendianProductshezhi.getTingyong()));

            // 加载现有图片数据
            loadExistingData();
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

    public void updateClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ProductshezhiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ProductshezhiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianProductshezhiService.updateByProductshezhi(yhMendianProductshezhi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void insertClick(View v) throws ParseException {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ProductshezhiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ProductshezhiChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhMendianProductshezhiService.insertByProductshezhi(yhMendianProductshezhi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() throws ParseException {
        // 原有字段验证
        if (product_bianhao.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入商品编号");
            return false;
        } else {
            yhMendianProductshezhi.setProduct_bianhao(product_bianhao.getText().toString());
        }

        if (type.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入商品类别");
            return false;
        } else {
            yhMendianProductshezhi.setType(type.getText().toString());
        }
        if (product_name.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入商品名称");
            return false;
        } else {
            yhMendianProductshezhi.setProduct_name(product_name.getText().toString());
        }
        if (unit.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入单位");
            return false;
        } else {
            yhMendianProductshezhi.setUnit(unit.getText().toString());
        }
        if (price.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入单价");
            return false;
        } else {
            yhMendianProductshezhi.setPrice(price.getText().toString());
        }
        if (chengben.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入成本");
            return false;
        } else {
            yhMendianProductshezhi.setChengben(chengben.getText().toString());
        }
        if (specifications.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入规格");
            return false;
        } else {
            yhMendianProductshezhi.setSpecifications(specifications.getText().toString());
        }
        if (practice.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入保存方式");
            return false;
        } else {
            yhMendianProductshezhi.setPractice(practice.getText().toString());
        }

        // 新增详情字段验证
        if (xiangqing.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入商品详情");
            return false;
        } else {
            yhMendianProductshezhi.setXiangqing(xiangqing.getText().toString());
        }

        if (beizhu1.getSelectedItemPosition() == 0) {  // 第一个是"请选择"
            ToastUtil.show(ProductshezhiChangeActivity.this, "请选择商品类型");
            return false;
        } else {
            yhMendianProductshezhi.setBeizhu1(beizhu1.getSelectedItem().toString());
        }

        if (tingyong.getSelectedItem().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入是否停用");
            return false;
        } else {
            yhMendianProductshezhi.setTingyong(tingyong.getSelectedItem().toString());
        }

        // 设置图片字段 - 直接对应数据库字段名
        yhMendianProductshezhi.setPhoto(photoBase64);      // 对应 photo 字段
        yhMendianProductshezhi.setPhoto1(photo1Base64);    // 对应 photo1 字段
        yhMendianProductshezhi.setPhoto2(photo2Base64);    // 对应 photo2 字段

        yhMendianProductshezhi.setCompany(yhMendianUser.getCompany());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private int gettingyongPosition(String param) {
        if (tingyong_selectArray != null) {
            for (int i = 0; i < tingyong_selectArray.length; i++) {
                if (param.equals(tingyong_selectArray[i])) {
                    return i;
                }
            }
        }
        return 0;
    }
    private int getSpinnerPosition(String[] array, String value) {
        if (array != null && value != null) {
            for (int i = 0; i < array.length; i++) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }
        return 0;  // 默认选择第一个
    }

    private void initViews() {
        // 初始化图片预览视图 - 对应数据库字段
        photoPreview = findViewById(R.id.photo_preview);      // photo
        photo1Preview = findViewById(R.id.photo1_preview);    // photo1
        photo2Preview = findViewById(R.id.photo2_preview);    // photo2

        // 初始化选择按钮
        selectPhoto = findViewById(R.id.select_photo);
        selectPhoto1 = findViewById(R.id.select_photo1);
        selectPhoto2 = findViewById(R.id.select_photo2);

        // 初始化移除按钮
        removePhoto = findViewById(R.id.remove_photo);
        removePhoto1 = findViewById(R.id.remove_photo1);
        removePhoto2 = findViewById(R.id.remove_photo2);
    }

    private void setupImageSelection() {
        // photo 字段图片选择
        selectPhoto.setOnClickListener(v -> openImageChooser(REQUEST_CODE_PICK_IMAGE));
        removePhoto.setOnClickListener(v -> removeSelectedImage("photo"));

        // photo1 字段图片选择
        selectPhoto1.setOnClickListener(v -> openImageChooser(REQUEST_CODE_PICK_IMAGE1));
        removePhoto1.setOnClickListener(v -> removeSelectedImage("photo1"));

        // photo2 字段图片选择
        selectPhoto2.setOnClickListener(v -> openImageChooser(REQUEST_CODE_PICK_IMAGE2));
        removePhoto2.setOnClickListener(v -> removeSelectedImage("photo2"));
    }

    private void openImageChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), requestCode);
    }

    private void removeSelectedImage(String fieldName) {
        switch (fieldName) {
            case "photo":
                photoBitmap = null;
                photoBase64 = "";
                photoPreview.setImageResource(android.R.drawable.ic_menu_gallery);
                removePhoto.setVisibility(View.GONE);
                break;
            case "photo1":
                photo1Bitmap = null;
                photo1Base64 = "";
                photo1Preview.setImageResource(android.R.drawable.ic_menu_gallery);
                removePhoto1.setVisibility(View.GONE);
                break;
            case "photo2":
                photo2Bitmap = null;
                photo2Base64 = "";
                photo2Preview.setImageResource(android.R.drawable.ic_menu_gallery);
                removePhoto2.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                String base64Image = bitmapToBase64(bitmap);

                switch (requestCode) {
                    case REQUEST_CODE_PICK_IMAGE:
                        // 对应 photo 字段
                        photoBitmap = bitmap;
                        photoBase64 = base64Image;
                        photoPreview.setImageBitmap(bitmap);
                        removePhoto.setVisibility(View.VISIBLE);
                        break;
                    case REQUEST_CODE_PICK_IMAGE1:
                        // 对应 photo1 字段
                        photo1Bitmap = bitmap;
                        photo1Base64 = base64Image;
                        photo1Preview.setImageBitmap(bitmap);
                        removePhoto1.setVisibility(View.VISIBLE);
                        break;
                    case REQUEST_CODE_PICK_IMAGE2:
                        // 对应 photo2 字段
                        photo2Bitmap = bitmap;
                        photo2Base64 = base64Image;
                        photo2Preview.setImageBitmap(bitmap);
                        removePhoto2.setVisibility(View.VISIBLE);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "图片选择失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // 加载现有数据（编辑模式）
    private void loadExistingData() {
        // 从Intent或全局变量获取现有商品数据
        MyApplication myApplication = (MyApplication) getApplication();
        YhMendianProductshezhi product = (YhMendianProductshezhi) myApplication.getObj();

        if (product != null) {
            // 加载现有图片 - 对应数据库字段
            loadImageFromBase64(product.getPhoto(), photoPreview, removePhoto, "photo");
            loadImageFromBase64(product.getPhoto1(), photo1Preview, removePhoto1, "photo1");
            loadImageFromBase64(product.getPhoto2(), photo2Preview, removePhoto2, "photo2");
        }
    }

    private void loadImageFromBase64(String base64String, ImageView imageView, Button removeButton, String fieldName) {
        if (base64String != null && !base64String.isEmpty() && !base64String.equals("null")) {
            Bitmap bitmap = base64ToBitmap(base64String);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                removeButton.setVisibility(View.VISIBLE);

                // 保存Base64字符串到对应字段
                switch (fieldName) {
                    case "photo":
                        photoBase64 = base64String;
                        photoBitmap = bitmap;
                        break;
                    case "photo1":
                        photo1Base64 = base64String;
                        photo1Bitmap = bitmap;
                        break;
                    case "photo2":
                        photo2Base64 = base64String;
                        photo2Bitmap = bitmap;
                        break;
                }
            }
        }
    }

    private Bitmap base64ToBitmap(String base64String) {
        try {
            if (base64String.contains(",")) {
                base64String = base64String.substring(base64String.indexOf(",") + 1);
            }
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initControls() {
        product_bianhao = findViewById(R.id.product_bianhao);
        type = findViewById(R.id.type);
        product_name = findViewById(R.id.product_name);
        unit = findViewById(R.id.unit);
        price = findViewById(R.id.price);
        chengben = findViewById(R.id.chengben);
        specifications = findViewById(R.id.specifications);
        practice = findViewById(R.id.practice);
        xiangqing = findViewById(R.id.xiangqing);
        tingyong = findViewById(R.id.tingyong);
        beizhu1 = findViewById(R.id.beizhu1);  // 确保这行存在

        // 添加调试信息
        if (beizhu1 == null) {
            Log.e("ProductshezhiChange", "beizhu1 Spinner not found in layout");
            Toast.makeText(this, "销售类型控件未找到，请检查布局文件", Toast.LENGTH_LONG).show();
        } else {
            Log.d("ProductshezhiChange", "beizhu1 Spinner initialized successfully");
        }
    }

    private void initSpinners() {
        try {
            // 获取下拉选项数组
            tingyong_selectArray = getResources().getStringArray(R.array.tingyong_list);
            beizhu1_selectArray = getResources().getStringArray(R.array.beizhu1_list);

            // 设置适配器 - 添加null检查
            if (tingyong != null && tingyong_selectArray != null) {
                ArrayAdapter<String> tingyongAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, tingyong_selectArray);
                tingyong.setAdapter(tingyongAdapter);
            } else {
                Log.e("ProductshezhiChange", "tingyong spinner or array is null");
            }

            if (beizhu1 != null && beizhu1_selectArray != null) {
                ArrayAdapter<String> beizhu1Adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, beizhu1_selectArray);
                beizhu1.setAdapter(beizhu1Adapter);
                Log.d("ProductshezhiChange", "beizhu1 adapter set successfully");
            } else {
                Log.e("ProductshezhiChange", "beizhu1 spinner or array is null");
                if (beizhu1 == null) {
                    Toast.makeText(this, "销售类型控件初始化失败", Toast.LENGTH_LONG).show();
                }
                if (beizhu1_selectArray == null) {
                    Toast.makeText(this, "销售类型选项数据加载失败", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("ProductshezhiChange", "Error initializing spinners: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setControlData() {
        try {
            // 设置文本字段
            product_bianhao.setText(yhMendianProductshezhi.getProduct_bianhao());
            type.setText(yhMendianProductshezhi.getType());
            product_name.setText(yhMendianProductshezhi.getProduct_name());
            unit.setText(yhMendianProductshezhi.getUnit());
            price.setText(yhMendianProductshezhi.getPrice());
            chengben.setText(yhMendianProductshezhi.getChengben());
            specifications.setText(yhMendianProductshezhi.getSpecifications());
            practice.setText(yhMendianProductshezhi.getPractice());
            xiangqing.setText(yhMendianProductshezhi.getXiangqing());

            // 设置下拉选择 - 添加null检查
            if (tingyong != null && yhMendianProductshezhi.getTingyong() != null) {
                int tingyongPosition = getSpinnerPosition(tingyong_selectArray, yhMendianProductshezhi.getTingyong());
                tingyong.setSelection(tingyongPosition);
            }

            if (beizhu1 != null && yhMendianProductshezhi.getBeizhu1() != null) {
                int beizhu1Position = getSpinnerPosition(beizhu1_selectArray, yhMendianProductshezhi.getBeizhu1());
                beizhu1.setSelection(beizhu1Position);
                Log.d("ProductshezhiChange", "beizhu1 selection set to position: " + beizhu1Position);
            } else {
                Log.e("ProductshezhiChange", "Cannot set beizhu1 selection - spinner: " + beizhu1 + ", value: " + yhMendianProductshezhi.getBeizhu1());
            }

            // 加载现有图片数据
            loadExistingData();
        } catch (Exception e) {
            Log.e("ProductshezhiChange", "Error setting control data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


