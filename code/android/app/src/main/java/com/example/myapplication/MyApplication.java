package com.example.myapplication;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.widget.EditText;

import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;

public class MyApplication extends Application {
    private YhJinXiaoCunUser yhJinXiaoCunUser;



    public YhJinXiaoCunUser getYhJinXiaoCunUser() {
        return yhJinXiaoCunUser;
    }

    public void setYhJinXiaoCunUser(YhJinXiaoCunUser yhJinXiaoCunUser) {
        this.yhJinXiaoCunUser = yhJinXiaoCunUser;
    }

    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public static void setEditTextReadOnly(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setCursorVisible(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }
    }
}
