package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class JiChuZiLiaoChangeActivity extends AppCompatActivity {
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
            byte[] decodedString = Base64.decode(yhJinXiaoCunJiChuZiLiao.getMark1(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mark1.setImageBitmap(decodedByte);
            cpDm.setText(yhJinXiaoCunJiChuZiLiao.getSpDm());
            name.setText(yhJinXiaoCunJiChuZiLiao.getName());
            leiBie.setText(yhJinXiaoCunJiChuZiLiao.getLeiBie());
            danWei.setText(yhJinXiaoCunJiChuZiLiao.getDanWei());
            kehu.setText(yhJinXiaoCunJiChuZiLiao.getShouHuo());
            gongyingshang.setText(yhJinXiaoCunJiChuZiLiao.getGongHuo());

        }

        mark1.setOnClickListener(imageSel());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //点击image控件选择图片
    public View.OnClickListener imageSel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, 0x1);
            }
        };
    }


    //获取选择图片的base64
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            if (data != null) {
                mark1.setImageURI(data.getData());
                String v_path  = getRealPathFromURI(this,data.getData());
                File file = new File(v_path);
                String base64=  fileToBase64(file);
                System.out.println(base64);
                yhJinXiaoCunJiChuZiLiao.setMark1(base64);
                System.out.println(base64);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }




    public void insertClick(View v) {
        if (!checkForm()) return;
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


    public static String getRealPathFromURI(Context mContext, Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }

    private String fileBase64String(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);//转换成输入流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);//读取输入流并写入输出字节流中
            }
            fis.close();//关闭文件输入流
            String uploadBuffer = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));  //进行Base64编码
            return uploadBuffer;
        } catch (Exception e) {
            return null;
        }
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
