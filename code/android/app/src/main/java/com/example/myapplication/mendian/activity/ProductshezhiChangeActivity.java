package com.example.myapplication.mendian.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.text.ParseException;
import java.util.List;

public class ProductshezhiChangeActivity extends AppCompatActivity {
    private YhMendianUser yhMendianUser;
    private YhMendianUserService yhMendianUserService;

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
    private EditText tingyong;
    private EditText photo;

    List<YhMendianProductshezhi> List;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productshezhi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        yhMendianUserService = new YhMendianUserService();
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
        tingyong = findViewById(R.id.tingyong);
        photo = findViewById(R.id.photo);


        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhMendianProductshezhi = new YhMendianProductshezhi();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        }else if(id == R.id.update_btn) {
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
            tingyong.setText(yhMendianProductshezhi.getTingyong());
            photo.setText(yhMendianProductshezhi.getPhoto());

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
        if (tingyong.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入是否停用");
            return false;
        } else {
            yhMendianProductshezhi.setTingyong(tingyong.getText().toString());
        }
        if (photo.getText().toString().equals("")) {
            ToastUtil.show(ProductshezhiChangeActivity.this, "请输入图片");
            return false;
        } else {
            yhMendianProductshezhi.setPhoto(photo.getText().toString());
        }


        yhMendianProductshezhi.setCompany(yhMendianProductshezhi.getCompany());

        return true;
    }


    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
