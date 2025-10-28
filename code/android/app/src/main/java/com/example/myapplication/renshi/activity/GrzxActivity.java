package com.example.myapplication.renshi.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.service.GeRenService;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.PushNewsService;
import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GrzxActivity extends AppCompatActivity {
    private YhRenShiUser yhRenShiUser;


    private Banner banner;
    private List<Integer> banner_data;
    private GeRenService geRenService;

    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_PERMISSIONS = 102;
    private ImageView companyLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renshi_grzx);

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        companyLogo = findViewById(R.id.company_logo);
        geRenService = new com.example.myapplication.renshi.service.GeRenService(this);

        setupAvatarClick();
        loadPushNewsData();

        // 设置欢迎文本
        setWelcomeText();

        String bumen = yhRenShiUser.getC();
        String username = yhRenShiUser.getI();
        String company = yhRenShiUser.getL();
        String Bianhao = yhRenShiUser.getD(); // 如果有角色字段
        String Yuangongname = yhRenShiUser.getB();
        // 在界面显示用户信息
        TextView tvUsername = findViewById(R.id.zhmc);
        TextView tvCompany = findViewById(R.id.ssgs);
        TextView tvYonghuming = findViewById(R.id.yonghuming);
        TextView tvBianhao = findViewById(R.id.ygsf);
        TextView tvYuangongname = findViewById(R.id.ygmc);

        tvUsername.setText(bumen);
        tvCompany.setText(company);
        tvBianhao.setText(Bianhao);
        tvYonghuming.setText(username);
        tvYuangongname.setText(Yuangongname);

        initData();
        banner = findViewById(R.id.main_banner);

        banner.setAdapter(new BannerImageAdapter<Integer>(banner_data) {

            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
            }
        });
        startRotatingMask();
        // 开启循环轮播
        banner.isAutoLoop(true);
        banner.setIndicator(new CircleIndicator(this));
        banner.setScrollBarFadeDuration(1000);
        // 设置指示器颜色(TODO 即选中时那个小点的颜色)
        banner.setIndicatorSelectedColor(Color.GREEN);
        // 开始轮播
        banner.start();

        // 绑定退出按钮 - 直接退出，不需要确认
        View exitButton = findViewById(R.id.tuichu);
        exitButton.setOnClickListener(v -> {
            exitToLogin();
        });



    }


    /**
     * 设置欢迎文本：欢迎使用 + 公司名前4位 + 排产系统
     */
    private void setWelcomeText() {
        TextView welcomeText = findViewById(R.id.welcome_text); // 需要给TextView添加id

        // 从缓存读取公司名称
        SharedPreferences sharedPref = getSharedPreferences("my_cache", MODE_PRIVATE);
        String companyName = sharedPref.getString("companyName", "");

        String welcomeMessage;
        if (companyName != null && !companyName.isEmpty()) {
            // 截取公司名前4位，如果公司名称长度不足4位，则使用全称
            String shortCompanyName = companyName.length() >= 4 ?
                    companyName.substring(0, 4) : companyName;
            welcomeMessage = "欢迎使用" + shortCompanyName + "人事系统";
        } else {
            // 如果缓存中没有公司名称，使用默认文本
            welcomeMessage = "欢迎使用云合未来人事系统";
        }

        welcomeText.setText(welcomeMessage);
    }


    private void exitToLogin() {
        // 跳转到登录页面
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void startRotatingMask() {
        View rotatingMask = findViewById(R.id.rotating_mask);

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(
                rotatingMask,
                "rotation",
                0f, 360f
        );

        rotationAnimator.setDuration(6000);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.start();
    }

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.renshi_banner_01);
        banner_data.add(R.drawable.renshi_banner_01);
        banner_data.add(R.drawable.renshi_banner_01);
    }



    private void loadPushNewsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取数据
                    List<com.example.myapplication.renshi.entity.GenRen> result = geRenService.getList();

                    // 处理返回数据
                    if (result != null && !result.isEmpty()) {
                        com.example.myapplication.renshi.entity.GenRen news = result.get(0);

                        // 处理tptop1图片数据
                        final String tptop1Data = news.getTouxiang();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 只处理tptop1图片，替换companylogo
                                if (tptop1Data != null && !tptop1Data.trim().isEmpty()) {
                                    replaceCompanyLogo(tptop1Data);
                                }
                            }
                        });
                    } else {
                        System.out.println("DEBUG: 返回数据: 空数组");
                    }

                } catch (Exception e) {
                    System.out.println("DEBUG: 操作失败 - " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 替换公司logo图片的方法
    private void replaceCompanyLogo(String base64ImageData) {
        try {
            // 清理base64数据
            String cleanedData = base64ImageData
                    .replace("\r", "")
                    .replace("\n", "")
                    .replace(" ", "")
                    .trim();

            // 解码base64图片数据
            byte[] decodedBytes = Base64.decode(cleanedData, Base64.DEFAULT);

            // 创建Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap != null) {
                // 找到ImageView并设置图片
                ImageView companyLogo = findViewById(R.id.company_logo); // 需要给ImageView设置id
                if (companyLogo != null) {
                    companyLogo.setImageBitmap(bitmap);
                    System.out.println("DEBUG: 公司logo图片替换成功");
                } else {
                    System.out.println("DEBUG: 未找到company_logo ImageView");
                }
            } else {
                System.out.println("DEBUG: base64图片数据解码失败");
            }

        } catch (Exception e) {
            System.out.println("DEBUG: 替换公司logo失败 - " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupAvatarClick() {
        companyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectDialog();
            }
        });

        // 添加长按提示
        companyLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(com.example.myapplication.renshi.activity.GrzxActivity.this, "点击头像可以更换", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void showImageSelectDialog() {
        String[] options = {"拍照", "从相册选择", "取消"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择头像");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 拍照
                        checkCameraPermission();
                        break;
                    case 1: // 从相册选择
                        checkStoragePermission();
                        break;
                    case 2: // 取消
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    // 检查存储权限
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSIONS);
        } else {
            openGallery();
        }
    }

    // 检查相机权限
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_CODE_PERMISSIONS);
        } else {
            openCamera();
        }
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 根据点击的类型重新执行操作
                // 这里需要记录用户之前点击的是哪个选项，简化处理：默认打开相册
                openGallery();
            } else {
                Toast.makeText(this, "需要权限才能选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    // 打开相机
    private File cameraFile;
    private void openCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 创建临时文件
            cameraFile = createImageFile();
            if (cameraFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        cameraFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "无法打开相机", Toast.LENGTH_SHORT).show();
        }
    }

    // 创建图片文件
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    // 处理Activity结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_IMAGE:
                    if (data != null && data.getData() != null) {
                        handleSelectedImage(data.getData());
                    }
                    break;
                case REQUEST_CODE_CAMERA:
                    if (cameraFile != null) {
                        handleSelectedImage(Uri.fromFile(cameraFile));
                    }
                    break;
            }
        }
    }

    // 处理选择的图片
    private void handleSelectedImage(Uri imageUri) {
        try {
            // 解码图片
            Bitmap bitmap = decodeUriToBitmap(imageUri);
            if (bitmap != null) {
                // 显示新头像
                companyLogo.setImageBitmap(bitmap);

                // 转换为base64并上传
                String base64Image = bitmapToBase64(bitmap);
                uploadNewAvatar(base64Image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "图片处理失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 将Uri转换为Bitmap
    private Bitmap decodeUriToBitmap(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 将Bitmap转换为Base64
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // 上传新头像
    private void uploadNewAvatar(String base64Image) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取当前用户信息
                    SharedPreferences sharedPref = getSharedPreferences("my_cache", Context.MODE_PRIVATE);
                    String companyName = sharedPref.getString("companyName", "云合未来");
                    String userAccount = sharedPref.getString("userAccount", "");
                    String userPassword = sharedPref.getString("userPassword", "");

                    // 构建更新SQL
                    String sql = "UPDATE gongzi_renyuan SET touxiang = ? WHERE L = ? AND J = ? AND I = ?";

                    // 执行更新
                    boolean success = updateAvatarInDatabase(sql, base64Image, companyName, userAccount, userPassword);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                Toast.makeText(com.example.myapplication.renshi.activity.GrzxActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(com.example.myapplication.renshi.activity.GrzxActivity.this, "头像更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(com.example.myapplication.renshi.activity.GrzxActivity.this, "上传失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    // 数据库更新方法
    private boolean updateAvatarInDatabase(String sql, String base64Image, String company, String account, String password) {
        renshiBaseDao baseDao = new renshiBaseDao();
        try {
            System.out.println("DEBUG: 开始更新头像到数据库");
            System.out.println("DEBUG: 更新参数 - company: " + company + ", account: " + account);
            System.out.println("DEBUG: Base64图片数据长度: " + (base64Image != null ? base64Image.length() : "null"));

            // 清理base64数据（移除可能的换行和空格）
            String cleanedBase64 = base64Image
                    .replace("\r", "")
                    .replace("\n", "")
                    .replace(" ", "")
                    .trim();

            // 执行更新操作
            boolean result = baseDao.execute(sql, cleanedBase64, company, account, password);

            System.out.println("DEBUG: 头像更新结果: " + result);
            return result;

        } catch (Exception e) {
            System.out.println("DEBUG: 头像更新异常 - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }






}