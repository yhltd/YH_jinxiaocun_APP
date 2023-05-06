package com.example.myapplication.utils;
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
 
/**
 * Created by shen on 2015/8/19.
 */
@SuppressLint("AppCompatCustomView")
public class MarqueeTextView extends TextView {
 
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
 
    @Override
    public boolean isFocused(){
        return true;
    }
 
}