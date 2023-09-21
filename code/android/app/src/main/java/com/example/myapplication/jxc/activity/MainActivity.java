package com.example.myapplication.jxc.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<YhJinXiaoCunMingXi> mingxi_list;
    private Bitmap print_bitmap;
    private String title1;
    private String title2;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication myApplication = (MyApplication) getApplication();
        mingxi_list = myApplication.getMingxiList();
        Intent intent = getIntent();
        title1 = intent.getStringExtra("title1");
        title2 = intent.getStringExtra("title2");
        type = intent.getStringExtra("type");
        onOrderScanClick();
    }

    private void onOrderScanClick() {

        //这边直接用canvas画，然后保存
        if(type.equals("qrcode")){
            print_bitmap = Bitmap.createBitmap(600,10+mingxi_list.size()*500, Bitmap.Config.ARGB_8888);
        }else if(type.equals("barcode")){
            print_bitmap = Bitmap.createBitmap(600,10+mingxi_list.size()*300, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(print_bitmap);
        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#000000"));//画笔颜色为深粉色
        paint.setTextSize(35);//字体大小为40
        paint.setAntiAlias(true);//去除齿轮
        paint.setStyle(Paint.Style.FILL);//设置画笔样式为实心
        String fontPath = "yuan.ttf";//导入字体（路径assets目录下yuan.tff;如果没有assets目录需要右键main，new--folder--Assets folder创建assets目录）
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        paint.setTypeface(tf);
        if(type.equals("qrcode")){
            for(int i=0; i<mingxi_list.size(); i++){
                QRcode qrcode = new QRcode();
                Bitmap bitmap= qrcode.qrcode(mingxi_list.get(i).getSpDm(),type);
                Matrix matrix = new Matrix();
                matrix.postTranslate(100f, 100f);
                canvas.drawBitmap(bitmap, 100,10+ i * 500,null);
                canvas.drawText(title1 + mingxi_list.get(i).getSpDm(), 50, 440 + i * 500, paint);
                canvas.drawText(title2 + mingxi_list.get(i).getCpname(), 50, 490 + i * 500, paint);
            }
        }else if(type.equals("barcode")){
            for(int i=0; i<mingxi_list.size(); i++){
                QRcode qrcode = new QRcode();
                Bitmap bitmap= qrcode.qrcode(mingxi_list.get(i).getSpDm(),type);
                Matrix matrix = new Matrix();
                matrix.postTranslate(100f, 100f);
                canvas.drawBitmap(bitmap, 100,10+ i * 300,null);
                canvas.drawText(title1 + mingxi_list.get(i).getSpDm(), 50, 240 + i * 300, paint);
                canvas.drawText(title2 + mingxi_list.get(i).getCpname(), 50, 290 + i * 300, paint);
            }
        }


        canvas.save(); //保存
        canvas.restore(); // 存储

        ImageView qr_code = findViewById(R.id.qr_code);
        qr_code.setImageBitmap(print_bitmap);

    }

    public void picSave(View view) {

        //保存绘制的内容
        File imgFile = new File(Environment.getExternalStorageDirectory(),
                "IMG-" + System.currentTimeMillis() + ".png");//创建一个文件
        try {
            OutputStream os = new FileOutputStream(imgFile);//创建输出流
            print_bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "图片已保存，请到 " + imgFile.getPath() + " 路径下查看", Toast.LENGTH_SHORT).show();

    }


}