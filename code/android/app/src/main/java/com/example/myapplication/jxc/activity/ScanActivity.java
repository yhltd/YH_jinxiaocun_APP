package com.example.myapplication.jxc.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

public class ScanActivity extends AppCompatActivity {
    private CompoundBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        barcodeView = findViewById(R.id.barcodeView);
        barcodeView.decodeContinuous(callback);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            // 扫描结果回调方法
            String barcodeValue = result.getText();
            // 处理扫描结果，可以根据你的需求进行处理
            Intent intent=new Intent();
            intent.putExtra("qr_code",barcodeValue);//返回值
            setResult(101,intent);//有返回值的使用这个，没有要返回的值用setResult(0);
            finish();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            // 未处理结果回调方法
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}