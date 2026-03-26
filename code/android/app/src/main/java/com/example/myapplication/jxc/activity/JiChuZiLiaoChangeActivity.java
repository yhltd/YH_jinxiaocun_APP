//package com.example.myapplication.jxc.activity;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.util.Base64;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.myapplication.MyApplication;
//import com.example.myapplication.R;
//import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
//import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
//import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
//import com.example.myapplication.utils.LoadingDialog;
//import com.example.myapplication.utils.ToastUtil;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.WriterException;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Map;
//
//public class JiChuZiLiaoChangeActivity extends AppCompatActivity {
//    private YhJinXiaoCunUser yhJinXiaoCunUser;
//    private YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao;
//    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;
//
//    private EditText cpDm;
//    private EditText name;
//    private EditText leiBie;
//    private EditText danWei;
//    private EditText kehu;
//    private EditText gongyingshang;
//    private ImageView mark1;
//    private LinearLayout qr_code_line;
//    private ImageView qr_code;
//
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.jichuziliao_change);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//        MyApplication myApplication = (MyApplication) getApplication();
//        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
//        yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
//
//        mark1 = findViewById(R.id.mark1);
//        cpDm = findViewById(R.id.cpDm);
//        name = findViewById(R.id.name);
//        leiBie = findViewById(R.id.leiBie);
//        danWei = findViewById(R.id.danWei);
//        kehu = findViewById(R.id.kehu);
//        gongyingshang = findViewById(R.id.gongyingshang);
//        qr_code_line = findViewById(R.id.qr_code_line);
//        qr_code = findViewById(R.id.qr_code);
//
//        Intent intent = getIntent();
//        int id = intent.getIntExtra("type", 0);
//        if (id == R.id.insert_btn) {
//            yhJinXiaoCunJiChuZiLiao = new YhJinXiaoCunJiChuZiLiao();
//            Button btn = findViewById(id);
//            btn.setVisibility(View.VISIBLE);
//        } else if (id == R.id.update_btn) {
//            yhJinXiaoCunJiChuZiLiao = (YhJinXiaoCunJiChuZiLiao) myApplication.getObj();
//            Button btn = findViewById(id);
//            btn.setVisibility(View.VISIBLE);
//            qr_code_line.setVisibility(View.VISIBLE);
//            byte[] decodedString = Base64.decode(yhJinXiaoCunJiChuZiLiao.getMark1(), Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            mark1.setImageBitmap(decodedByte);
//            cpDm.setText(yhJinXiaoCunJiChuZiLiao.getSpDm());
//            name.setText(yhJinXiaoCunJiChuZiLiao.getName());
//            leiBie.setText(yhJinXiaoCunJiChuZiLiao.getLeiBie());
//            danWei.setText(yhJinXiaoCunJiChuZiLiao.getDanWei());
//            kehu.setText(yhJinXiaoCunJiChuZiLiao.getShouHuo());
//            gongyingshang.setText(yhJinXiaoCunJiChuZiLiao.getGongHuo());
//            QRcode qrcode = new QRcode();
//            Bitmap bitmap= qrcode.qrcode(yhJinXiaoCunJiChuZiLiao.getSpDm(),"qrcode");
//            qr_code.setImageBitmap(bitmap);
//        }
//
//        mark1.setOnClickListener(imageSel());
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            this.finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void clearClick(View v) {
//        byte[] decodedString = Base64.decode("", Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        mark1.setImageBitmap(decodedByte);
//        cpDm.setText("");
//        name.setText("");
//        leiBie.setText("");
//        danWei.setText("");
//        kehu.setText("");
//        gongyingshang.setText("");
//    }
//
//    //点击image控件选择图片
//    public View.OnClickListener imageSel() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK, null);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
//                startActivityForResult(intent, 0x1);
//            }
//        };
//    }
//
//
//    //获取选择图片的base64
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//// TODO Auto-generated method stub
//        if (requestCode == 0x1 && resultCode == RESULT_OK) {
//            if (data != null) {
//                mark1.setImageURI(data.getData());
//                String v_path  = getRealPathFromURI(this,data.getData());
//                File file = new File(v_path);
//                String base64=  fileToBase64(file);
//                System.out.println(base64);
//                yhJinXiaoCunJiChuZiLiao.setMark1(base64);
//                System.out.println(base64);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }
//
//
//
//
//    public void insertClick(View v) {
//        if (!checkForm()) return;
//
//        Handler saveHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                if ((boolean) msg.obj) {
//                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存成功");
//                    back();
//                } else {
//                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存失败，请稍后再试");
//                }
//
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.obj = yhJinXiaoCunJiChuZiLiaoService.insert(yhJinXiaoCunJiChuZiLiao);
//                saveHandler.sendMessage(msg);
//            }
//        }).start();
//    }
//
//    public void updateClick(View v) {
//        if (!checkForm()) return;
//
//        Handler saveHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                if ((boolean) msg.obj) {
//                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存成功");
//                    back();
//                } else {
//                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存失败，请稍后再试");
//                }
//
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.obj = yhJinXiaoCunJiChuZiLiaoService.update(yhJinXiaoCunJiChuZiLiao);
//                saveHandler.sendMessage(msg);
//            }
//        }).start();
//    }
//
//    private boolean checkForm() {
//        if (cpDm.getText().toString().equals("")) {
//            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输商品代码");
//            return false;
//        } else {
//            yhJinXiaoCunJiChuZiLiao.setSpDm(cpDm.getText().toString());
//        }
//
//        if (name.getText().toString().equals("")) {
//            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输商品名称");
//            return false;
//        } else {
//            yhJinXiaoCunJiChuZiLiao.setName(name.getText().toString());
//        }
//
//        if (leiBie.getText().toString().equals("")) {
//            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输入商品类别");
//            return false;
//        } else {
//            yhJinXiaoCunJiChuZiLiao.setLeiBie(leiBie.getText().toString());
//        }
//
//        if (danWei.getText().toString().equals("")) {
//            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输入商品单位");
//            return false;
//        } else {
//            yhJinXiaoCunJiChuZiLiao.setDanWei(danWei.getText().toString());
//        }
//
//        yhJinXiaoCunJiChuZiLiao.setShouHuo(kehu.getText().toString());
//        yhJinXiaoCunJiChuZiLiao.setGongHuo(gongyingshang.getText().toString());
//        yhJinXiaoCunJiChuZiLiao.setGsName(yhJinXiaoCunUser.getGongsi());
//
//        return true;
//    }
//
//
//    public static String getRealPathFromURI(Context mContext, Uri contentUri) {
//
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            ;
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }
//
//    public String fileToBase64(File file) {
//        String base64 = null;
//        InputStream in = null;
//        try {
//            in = new FileInputStream(file);
//            byte[] bytes = new byte[in.available()];
//            int length = in.read(bytes);
//            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return base64;
//    }
//
//    private String fileBase64String(String path) {
//        try {
//            FileInputStream fis = new FileInputStream(path);//转换成输入流
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int count = 0;
//            while ((count = fis.read(buffer)) >= 0) {
//                baos.write(buffer, 0, count);//读取输入流并写入输出字节流中
//            }
//            fis.close();//关闭文件输入流
//            String uploadBuffer = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));  //进行Base64编码
//            return uploadBuffer;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private void back() {
//        setResult(RESULT_OK, new Intent());
//        finish();
//    }
//}
package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

public class JiChuZiLiaoChangeActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_FILE = 1001;
    private final static int REQUEST_CODE_GALLERY = 1002;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;

    private EditText cpDm;
    private EditText name;
    private EditText leiBie;
    private EditText danWei;
    private EditText kehu;
    private EditText gongyingshang;
    private ImageView mark1;
    private LinearLayout qr_code_line;
    private ImageView qr_code;

    // 文件上传相关
    private File currentImageFile;
    private String currentImageUrl;
    private AlertDialog uploadProgressDialog;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jichuziliao_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();

        mark1 = findViewById(R.id.mark1);
        cpDm = findViewById(R.id.cpDm);
        name = findViewById(R.id.name);
        leiBie = findViewById(R.id.leiBie);
        danWei = findViewById(R.id.danWei);
        kehu = findViewById(R.id.kehu);
        gongyingshang = findViewById(R.id.gongyingshang);
        qr_code_line = findViewById(R.id.qr_code_line);
        qr_code = findViewById(R.id.qr_code);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhJinXiaoCunJiChuZiLiao = new YhJinXiaoCunJiChuZiLiao();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhJinXiaoCunJiChuZiLiao = (YhJinXiaoCunJiChuZiLiao) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            qr_code_line.setVisibility(View.VISIBLE);

            // 加载图片 - 兼容URL和Base64格式
            String mark1Value = yhJinXiaoCunJiChuZiLiao.getMark1();
            loadImageCompat(mark1Value, mark1);

            cpDm.setText(yhJinXiaoCunJiChuZiLiao.getSpDm());
            name.setText(yhJinXiaoCunJiChuZiLiao.getName());
            leiBie.setText(yhJinXiaoCunJiChuZiLiao.getLeiBie());
            danWei.setText(yhJinXiaoCunJiChuZiLiao.getDanWei());
            kehu.setText(yhJinXiaoCunJiChuZiLiao.getShouHuo());
            gongyingshang.setText(yhJinXiaoCunJiChuZiLiao.getGongHuo());

            // 生成二维码
            Bitmap bitmap = generateQRCode(yhJinXiaoCunJiChuZiLiao.getSpDm());
            if (bitmap != null) {
                qr_code.setImageBitmap(bitmap);
            }
        }



        mark1.setOnClickListener(imageSel());
        // 添加长按删除图片功能
        mark1.setOnLongClickListener(imageLongClick());
    }

    /**
     * 兼容加载图片（支持URL和Base64）
     */
    private void loadImageCompat(String imageData, ImageView imageView) {
        if (imageData == null || imageData.isEmpty() || imageData.equals("null")) {
            // 没有图片，设置默认占位图
            imageView.setImageResource(R.drawable.ic_image_placeholder);
            return;
        }

        // 判断是否为URL格式
        if (imageData.startsWith("http://") || imageData.startsWith("https://")) {
            // URL格式 - 使用Glide加载网络图片
            Glide.with(this)
                    .load(imageData)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            // Base64格式 - 解码显示
            try {
                // 清理Base64字符串（移除可能的数据URI前缀）
                String base64Image = imageData;
                if (imageData.contains(",")) {
                    base64Image = imageData.substring(imageData.indexOf(",") + 1);
                }
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedByte != null) {
                    imageView.setImageBitmap(decodedByte);
                    Log.d("ImageLoad", "Base64解码成功");
                } else {
                    imageView.setImageResource(R.drawable.ic_image_error);
                    Log.e("ImageLoad", "Base64解码结果为null");
                }
            } catch (IllegalArgumentException e) {
                Log.e("ImageLoad", "Base64解码失败: " + e.getMessage());
                imageView.setImageResource(R.drawable.ic_image_error);
            } catch (Exception e) {
                Log.e("ImageLoad", "图片加载异常: " + e.getMessage());
                imageView.setImageResource(R.drawable.ic_image_error);
            }
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

    public void clearClick(View v) {
        // 清空表单
        cpDm.setText("");
        name.setText("");
        leiBie.setText("");
        danWei.setText("");
        kehu.setText("");
        gongyingshang.setText("");
        mark1.setImageResource(R.drawable.ic_image_placeholder);
        currentImageFile = null;
        currentImageUrl = null;
    }

    public void clearImageClick(View v) {
        // 如果已有图片，先删除服务器上的文件
        if (yhJinXiaoCunJiChuZiLiao != null && yhJinXiaoCunJiChuZiLiao.getId() > 0) {
            String oldImageUrl = yhJinXiaoCunJiChuZiLiao.getMark1();
            // 只有URL格式才需要从服务器删除，Base64格式不需要
            if (oldImageUrl != null && (oldImageUrl.startsWith("http://") || oldImageUrl.startsWith("https://"))) {
                String fileName = extractFileName(oldImageUrl);
                String companyName = yhJinXiaoCunUser != null ? yhJinXiaoCunUser.getGongsi() : "";

                if (companyName == null || companyName.isEmpty()) {
                    ToastUtil.show(this, "公司名称不存在");
                    return;
                }

                ToastUtil.show(this, "正在删除图片...");

                yhJinXiaoCunJiChuZiLiaoService.deleteFileFromServer(fileName, companyName,
                        new YhJinXiaoCunJiChuZiLiaoService.DeleteCallback() {
                            @Override
                            public void onSuccess() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean dbSuccess = yhJinXiaoCunJiChuZiLiaoService.clearFileRecord(
                                                yhJinXiaoCunJiChuZiLiao.getId());

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (dbSuccess) {
                                                    yhJinXiaoCunJiChuZiLiao.setMark1(null);
                                                    mark1.setImageResource(R.drawable.ic_image_placeholder);
                                                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "图片已清空");
                                                } else {
                                                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "数据库更新失败");
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            }

                            @Override
                            public void onFailure(String error) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.show(JiChuZiLiaoChangeActivity.this, "删除图片失败: " + error);
                                    }
                                });
                            }
                        });
                return;
            }
        }

        // 如果没有旧图片或不是URL，直接清空界面
        mark1.setImageResource(R.drawable.ic_image_placeholder);
        currentImageFile = null;
        currentImageUrl = null;
        if (yhJinXiaoCunJiChuZiLiao != null) {
            yhJinXiaoCunJiChuZiLiao.setMark1(null);
        }
        ToastUtil.show(this, "图片已清空");
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
     * 生成二维码
     */
    private Bitmap generateQRCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 200, 200, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 点击image控件选择图片
    public View.OnClickListener imageSel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageSourceDialog();
            }
        };
    }

    // 长按image控件删除图片
    public View.OnLongClickListener imageLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(JiChuZiLiaoChangeActivity.this)
                        .setTitle("删除图片")
                        .setMessage("确定要删除当前图片吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearImageClick(null);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        };
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
        // 创建临时文件保存照片
        File photoFile = new File(getExternalCacheDir(), "temp_photo_" + System.currentTimeMillis() + ".jpg");
        currentImageFile = photoFile;
        Uri photoUri = Uri.fromFile(photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    // 添加相机请求码
    private static final int REQUEST_CODE_CAMERA = 1003;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    // 显示图片预览
                    mark1.setImageURI(uri);
                    // 获取文件路径并上传
                    String filePath = getPathFromUri(uri);
                    if (filePath != null) {
                        uploadImageFile(new File(filePath));
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                if (currentImageFile != null && currentImageFile.exists()) {
                    // 显示图片预览
                    mark1.setImageURI(Uri.fromFile(currentImageFile));
                    // 上传图片
                    uploadImageFile(currentImageFile);
                }
            }
        }
    }

    /**
     * 上传图片文件
     */
    private void uploadImageFile(final File file) {
        if (file == null || !file.exists()) {
            ToastUtil.show(this, "文件不存在");
            return;
        }

        showUploadProgressDialog();

        String fileName = file.getName();
        String companyName = yhJinXiaoCunUser != null ? yhJinXiaoCunUser.getGongsi() : "";
        if (companyName == null || companyName.isEmpty()) {
            ToastUtil.show(this, "公司名称不存在");
            hideUploadProgressDialog();
            return;
        }

        String recordId = yhJinXiaoCunJiChuZiLiao.getId() > 0 ?
                String.valueOf(yhJinXiaoCunJiChuZiLiao.getId()) : "temp_" + System.currentTimeMillis();
        String recordName = cpDm.getText().toString() + "_" + name.getText().toString();

        // 如果有旧图片URL，先删除服务器上的旧文件（只有URL格式才需要删除）
        if (yhJinXiaoCunJiChuZiLiao != null && yhJinXiaoCunJiChuZiLiao.getMark1() != null &&
                (yhJinXiaoCunJiChuZiLiao.getMark1().startsWith("http://") ||
                        yhJinXiaoCunJiChuZiLiao.getMark1().startsWith("https://"))) {

            String oldImageUrl = yhJinXiaoCunJiChuZiLiao.getMark1();
            String oldFileName = extractFileName(oldImageUrl);

            yhJinXiaoCunJiChuZiLiaoService.deleteFileFromServer(oldFileName, companyName,
                    new YhJinXiaoCunJiChuZiLiaoService.DeleteCallback() {
                        @Override
                        public void onSuccess() {
                            doUploadWithCheck(file, fileName, companyName, recordId, recordName);
                        }

                        @Override
                        public void onFailure(String error) {
                            // 删除失败也继续上传
                            doUploadWithCheck(file, fileName, companyName, recordId, recordName);
                        }
                    });
        } else {
            doUploadWithCheck(file, fileName, companyName, recordId, recordName);
        }
    }

    /**
     * 执行带空间检查的上传
     */
    private void doUploadWithCheck(File file, String fileName, String companyName,
                                   String recordId, String recordName) {
        yhJinXiaoCunJiChuZiLiaoService.uploadFileWithCheck(file, fileName, companyName,
                recordId, recordName, "",
                new YhJinXiaoCunJiChuZiLiaoService.UploadCallback() {
                    @Override
                    public void onSuccess(String fileUrl) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideUploadProgressDialog();

                                currentImageUrl = fileUrl;

                                if (yhJinXiaoCunJiChuZiLiao.getId() > 0) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            yhJinXiaoCunJiChuZiLiao.setMark1(fileUrl);
                                            boolean dbSuccess = yhJinXiaoCunJiChuZiLiaoService.updateFileRecord(
                                                    yhJinXiaoCunJiChuZiLiao.getId(), fileUrl);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (dbSuccess) {
                                                        ToastUtil.show(JiChuZiLiaoChangeActivity.this, "图片上传成功");
                                                    } else {
                                                        ToastUtil.show(JiChuZiLiaoChangeActivity.this, "数据库更新失败");
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                } else {
                                    yhJinXiaoCunJiChuZiLiao.setMark1(fileUrl);
                                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "图片上传成功");
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
                                ToastUtil.show(JiChuZiLiaoChangeActivity.this, "图片上传失败: " + error);
                            }
                        });
                    }

                    @Override
                    public void onWarning(String message, double usagePercent, double estimatedUsagePercent) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(JiChuZiLiaoChangeActivity.this, message);
                            }
                        });
                    }
                });
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

    public void insertClick(View v) {
        if (!checkForm()) return;

        // 如果有新上传的图片但还没保存到对象，使用currentImageUrl
        if (currentImageUrl != null && yhJinXiaoCunJiChuZiLiao.getMark1() == null) {
            yhJinXiaoCunJiChuZiLiao.setMark1(currentImageUrl);
        }

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunJiChuZiLiaoService.insert(yhJinXiaoCunJiChuZiLiao);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;

        // 如果有新上传的图片但还没保存到对象，使用currentImageUrl
        if (currentImageUrl != null) {
            yhJinXiaoCunJiChuZiLiao.setMark1(currentImageUrl);
        }

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(JiChuZiLiaoChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunJiChuZiLiaoService.update(yhJinXiaoCunJiChuZiLiao);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (cpDm.getText().toString().equals("")) {
            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输商品代码");
            return false;
        } else {
            yhJinXiaoCunJiChuZiLiao.setSpDm(cpDm.getText().toString());
        }

        if (name.getText().toString().equals("")) {
            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输商品名称");
            return false;
        } else {
            yhJinXiaoCunJiChuZiLiao.setName(name.getText().toString());
        }

        if (leiBie.getText().toString().equals("")) {
            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输入商品类别");
            return false;
        } else {
            yhJinXiaoCunJiChuZiLiao.setLeiBie(leiBie.getText().toString());
        }

        if (danWei.getText().toString().equals("")) {
            ToastUtil.show(JiChuZiLiaoChangeActivity.this, "请输入商品单位");
            return false;
        } else {
            yhJinXiaoCunJiChuZiLiao.setDanWei(danWei.getText().toString());
        }

        yhJinXiaoCunJiChuZiLiao.setShouHuo(kehu.getText().toString());
        yhJinXiaoCunJiChuZiLiao.setGongHuo(gongyingshang.getText().toString());
        yhJinXiaoCunJiChuZiLiao.setGsName(yhJinXiaoCunUser.getGongsi());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}