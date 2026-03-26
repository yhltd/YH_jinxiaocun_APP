package com.example.myapplication.mendian.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

public class ProductshezhiChangeActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private final static int REQUEST_CODE_FILE = 1001;
    private final static int REQUEST_CODE_GALLERY = 1002;
    private final static int REQUEST_CODE_CAMERA = 1003;

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

    List<YhMendianProductshezhi> List;

    String[] tingyong_selectArray;
    String[] beizhu1_selectArray;

    // 图片相关
    private ImageView photoPreview;      // 对应 photo
    private ImageView photo1Preview;     // 对应 photo1
    private ImageView photo2Preview;     // 对应 photo2

    private Button selectPhoto, selectPhoto1, selectPhoto2;
    private Button removePhoto, removePhoto1, removePhoto2;

    // 文件上传相关
    private File currentImageFile;
    private String currentFieldName = "";  // 当前正在操作的图片字段
    private String photoUrl = "";          // 对应 photo 字段
    private String photo1Url = "";         // 对应 photo1 字段
    private String photo2Url = "";         // 对应 photo2 字段

    private AlertDialog uploadProgressDialog;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productshezhi_change);

        initControls();
        initSpinners();
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
            setControlData();
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

        if (beizhu1.getSelectedItemPosition() == 0) {
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

        // 设置图片URL字段
        yhMendianProductshezhi.setPhoto(photoUrl);      // 对应 photo 字段
        yhMendianProductshezhi.setPhoto1(photo1Url);    // 对应 photo1 字段
        yhMendianProductshezhi.setPhoto2(photo2Url);    // 对应 photo2 字段

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
        return 0;
    }

    private void initViews() {
        // 初始化图片预览视图
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
        selectPhoto.setOnClickListener(v -> {
            currentFieldName = "photo";
            showImageSourceDialog();
        });
        removePhoto.setOnClickListener(v -> removeSelectedImage("photo"));

        // photo1 字段图片选择
        selectPhoto1.setOnClickListener(v -> {
            currentFieldName = "photo1";
            showImageSourceDialog();
        });
        removePhoto1.setOnClickListener(v -> removeSelectedImage("photo1"));

        // photo2 字段图片选择
        selectPhoto2.setOnClickListener(v -> {
            currentFieldName = "photo2";
            showImageSourceDialog();
        });
        removePhoto2.setOnClickListener(v -> removeSelectedImage("photo2"));
    }

    /**
     * 显示图片来源选择对话框
     */
    private void showImageSourceDialog() {
        String[] options = {"从相册选择", "拍照"};

        new AlertDialog.Builder(this)
                .setTitle("选择图片")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGallery();
                                break;
                            case 1:
                                openCamera();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 打开相册
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = new File(getExternalCacheDir(), "temp_photo_" + System.currentTimeMillis() + ".jpg");
        currentImageFile = photoFile;
        Uri photoUri = Uri.fromFile(photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void removeSelectedImage(String fieldName) {
        // 先删除服务器上的文件
        String oldImageUrl = getImageUrlByField(fieldName);
        if (oldImageUrl != null && !oldImageUrl.isEmpty() && oldImageUrl.startsWith("http")) {
            deleteImageFromServer(oldImageUrl, fieldName);
        } else {
            // 没有旧图片，直接清空界面
            clearImageUI(fieldName);
        }
    }

    /**
     * 根据字段名获取图片URL
     */
    private String getImageUrlByField(String fieldName) {
        switch (fieldName) {
            case "photo": return photoUrl;
            case "photo1": return photo1Url;
            case "photo2": return photo2Url;
            default: return null;
        }
    }

    /**
     * 根据字段名设置图片URL
     */
    private void setImageUrlByField(String fieldName, String url) {
        switch (fieldName) {
            case "photo": photoUrl = url; break;
            case "photo1": photo1Url = url; break;
            case "photo2": photo2Url = url; break;
        }
    }

    /**
     * 根据字段名获取ImageView
     */
    private ImageView getImageViewByField(String fieldName) {
        switch (fieldName) {
            case "photo": return photoPreview;
            case "photo1": return photo1Preview;
            case "photo2": return photo2Preview;
            default: return null;
        }
    }

    /**
     * 根据字段名获取移除按钮
     */
    private Button getRemoveButtonByField(String fieldName) {
        switch (fieldName) {
            case "photo": return removePhoto;
            case "photo1": return removePhoto1;
            case "photo2": return removePhoto2;
            default: return null;
        }
    }

    /**
     * 清空图片UI
     */
    private void clearImageUI(String fieldName) {
        ImageView imageView = getImageViewByField(fieldName);
        Button removeButton = getRemoveButtonByField(fieldName);

        if (imageView != null) {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        if (removeButton != null) {
            removeButton.setVisibility(View.GONE);
        }
        setImageUrlByField(fieldName, "");
    }

    /**
     * 从服务器删除图片
     */
    private void deleteImageFromServer(String imageUrl, String fieldName) {
        String fileName = extractFileName(imageUrl);
        String companyName = yhMendianUser != null ? yhMendianUser.getCompany() : "";
        if (companyName == null || companyName.isEmpty()) {
            ToastUtil.show(this, "公司名称不存在");
            return;
        }

        ToastUtil.show(this, "正在删除图片...");

        yhMendianProductshezhiService.deleteFileFromServer(fileName, companyName,
                new YhMendianProductshezhiService.DeleteCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clearImageUI(fieldName);

                                if (yhMendianProductshezhi != null && yhMendianProductshezhi.getId() > 0) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            boolean dbSuccess = yhMendianProductshezhiService.clearImageRecord(
                                                    yhMendianProductshezhi.getId(), fieldName);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (dbSuccess) {
                                                        ToastUtil.show(ProductshezhiChangeActivity.this, "图片已删除");
                                                    } else {
                                                        ToastUtil.show(ProductshezhiChangeActivity.this, "数据库更新失败");
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                } else {
                                    ToastUtil.show(ProductshezhiChangeActivity.this, "图片已删除");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(ProductshezhiChangeActivity.this, "删除图片失败: " + error);
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    // 显示图片预览
                    ImageView imageView = getImageViewByField(currentFieldName);
                    if (imageView != null) {
                        imageView.setImageURI(uri);
                    }
                    // 获取文件路径并上传
                    String filePath = getPathFromUri(uri);
                    if (filePath != null) {
                        uploadImageFile(new File(filePath), currentFieldName);
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                if (currentImageFile != null && currentImageFile.exists()) {
                    // 显示图片预览
                    ImageView imageView = getImageViewByField(currentFieldName);
                    if (imageView != null) {
                        imageView.setImageURI(Uri.fromFile(currentImageFile));
                    }
                    // 上传图片
                    uploadImageFile(currentImageFile, currentFieldName);
                }
            }
        }
    }

    /**
     * 从URI获取文件路径
     */
    private String getPathFromUri(Uri uri) {
        String filePath = null;

        try {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                ContentResolver resolver = getContentResolver();

                String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
                Cursor cursor = resolver.query(uri, projection, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    filePath = cursor.getString(columnIndex);

                    if (filePath == null) {
                        int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                        String fileName = cursor.getString(nameIndex);

                        File tempFile = new File(getCacheDir(), fileName);
                        copyFileFromUri(uri, tempFile);
                        filePath = tempFile.getAbsolutePath();
                    }

                    cursor.close();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                filePath = uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    /**
     * 从URI复制文件
     */
    private void copyFileFromUri(Uri uri, File destFile) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片文件（带空间检查）
     */
    private void uploadImageFile(final File file, final String fieldName) {
        if (file == null || !file.exists()) {
            ToastUtil.show(this, "文件不存在");
            return;
        }

        showUploadProgressDialog();

        String fileName = file.getName();
        String companyName = yhMendianUser != null ? yhMendianUser.getCompany() : "";
        if (companyName == null || companyName.isEmpty()) {
            ToastUtil.show(this, "公司名称不存在");
            hideUploadProgressDialog();
            return;
        }

        String recordId;
        if (yhMendianProductshezhi != null && yhMendianProductshezhi.getId() > 0) {
            recordId = String.valueOf(yhMendianProductshezhi.getId());
        } else {
            recordId = "temp_" + System.currentTimeMillis();
        }

        String recordName = product_name.getText().toString() + "_" + product_bianhao.getText().toString();

        // 如果有旧图片URL，先删除服务器上的旧文件
        String oldImageUrl = getImageUrlByField(fieldName);
        if (oldImageUrl != null && !oldImageUrl.isEmpty() && oldImageUrl.startsWith("http")) {
            String oldFileName = extractFileName(oldImageUrl);

            yhMendianProductshezhiService.deleteFileFromServer(oldFileName, companyName,
                    new YhMendianProductshezhiService.DeleteCallback() {
                        @Override
                        public void onSuccess() {
                            doUploadWithCheck(file, fileName, companyName, recordId, recordName, fieldName);
                        }

                        @Override
                        public void onFailure(String error) {
                            Log.e("FileUpload", "删除旧文件失败: " + error);
                            doUploadWithCheck(file, fileName, companyName, recordId, recordName, fieldName);
                        }
                    });
        } else {
            doUploadWithCheck(file, fileName, companyName, recordId, recordName, fieldName);
        }
    }

    /**
     * 执行带空间检查的上传
     */
    private void doUploadWithCheck(File file, String fileName, String companyName,
                                   String recordId, String recordName, String fieldName) {
        yhMendianProductshezhiService.uploadFileWithCheck(file, fileName, companyName,
                recordId, recordName, "",
                new YhMendianProductshezhiService.UploadCallback() {
                    @Override
                    public void onSuccess(String fileUrl) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideUploadProgressDialog();

                                setImageUrlByField(fieldName, fileUrl);

                                Button removeButton = getRemoveButtonByField(fieldName);
                                if (removeButton != null) {
                                    removeButton.setVisibility(View.VISIBLE);
                                }

                                // 如果是在编辑模式，同时更新数据库
                                if (yhMendianProductshezhi != null && yhMendianProductshezhi.getId() > 0) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            boolean dbSuccess = yhMendianProductshezhiService.updateImageRecord(
                                                    yhMendianProductshezhi.getId(), fieldName, fileUrl);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (dbSuccess) {
                                                        ToastUtil.show(ProductshezhiChangeActivity.this, "图片上传成功");
                                                    } else {
                                                        ToastUtil.show(ProductshezhiChangeActivity.this, "数据库更新失败");
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                } else {
                                    ToastUtil.show(ProductshezhiChangeActivity.this, "图片上传成功");
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideUploadProgressDialog();
                                ToastUtil.show(ProductshezhiChangeActivity.this, "图片上传失败: " + error);
                            }
                        });
                    }

                    @Override
                    public void onWarning(String message, double usagePercent, double estimatedUsagePercent) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(ProductshezhiChangeActivity.this, message);
                            }
                        });
                    }
                });
    }

    /**
     * 从URL中提取文件名
     */
    private String extractFileName(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "";
        }
        int lastSlash = fileUrl.lastIndexOf('/');
        if (lastSlash != -1 && lastSlash < fileUrl.length() - 1) {
            return fileUrl.substring(lastSlash + 1);
        }
        return fileUrl;
    }

    /**
     * 显示上传进度对话框
     */
    private void showUploadProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传中...");
        builder.setMessage("请稍候");
        builder.setCancelable(false);

        View progressView = getLayoutInflater().inflate(R.layout.dialog_progress, null);
        builder.setView(progressView);

        uploadProgressDialog = builder.create();
        uploadProgressDialog.show();
    }

    /**
     * 隐藏上传进度对话框
     */
    private void hideUploadProgressDialog() {
        if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
            uploadProgressDialog.dismiss();
            uploadProgressDialog = null;
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
        beizhu1 = findViewById(R.id.beizhu1);

        if (beizhu1 == null) {
            Log.e("ProductshezhiChange", "beizhu1 Spinner not found in layout");
            Toast.makeText(this, "销售类型控件未找到，请检查布局文件", Toast.LENGTH_LONG).show();
        } else {
            Log.d("ProductshezhiChange", "beizhu1 Spinner initialized successfully");
        }
    }

    private void initSpinners() {
        try {
            tingyong_selectArray = getResources().getStringArray(R.array.tingyong_list);
            beizhu1_selectArray = getResources().getStringArray(R.array.beizhu1_list);

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

            // 设置下拉选择
            if (tingyong != null && yhMendianProductshezhi.getTingyong() != null) {
                int tingyongPosition = getSpinnerPosition(tingyong_selectArray, yhMendianProductshezhi.getTingyong());
                tingyong.setSelection(tingyongPosition);
            }

            if (beizhu1 != null && yhMendianProductshezhi.getBeizhu1() != null) {
                int beizhu1Position = getSpinnerPosition(beizhu1_selectArray, yhMendianProductshezhi.getBeizhu1());
                beizhu1.setSelection(beizhu1Position);
                Log.d("ProductshezhiChange", "beizhu1 selection set to position: " + beizhu1Position);
            }

            // 加载现有图片数据（URL格式）
            loadExistingImages();
        } catch (Exception e) {
            Log.e("ProductshezhiChange", "Error setting control data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 加载现有图片（支持URL和Base64兼容）
     */
    private void loadExistingImages() {
        // 加载 photo
        loadImage(yhMendianProductshezhi.getPhoto(), photoPreview, removePhoto, "photo");
        // 加载 photo1
        loadImage(yhMendianProductshezhi.getPhoto1(), photo1Preview, removePhoto1, "photo1");
        // 加载 photo2
        loadImage(yhMendianProductshezhi.getPhoto2(), photo2Preview, removePhoto2, "photo2");
    }

    /**
     * 加载图片（兼容URL和Base64）
     */
    private void loadImage(String imageData, ImageView imageView, Button removeButton, String fieldName) {
        if (imageData != null && !imageData.isEmpty() && !imageData.equals("null")) {
            if (imageData.startsWith("http")) {
                // 新格式：URL
                Glide.with(this)
                        .load(imageData)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(imageView);
                setImageUrlByField(fieldName, imageData);
            } else {
                // 旧格式：Base64（兼容处理）
                try {
                    Bitmap bitmap = base64ToBitmap(imageData);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            removeButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Base64转Bitmap（用于兼容旧数据）
     */
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
}