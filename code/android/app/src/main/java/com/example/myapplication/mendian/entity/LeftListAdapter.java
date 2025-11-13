package com.example.myapplication.mendian.entity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;


/**
 * 左侧Adapter
 */
public class LeftListAdapter extends BaseAdapter {
    private String[] leftStr;
    boolean[] flagArray;
    private Context context;

    public LeftListAdapter(Context context, String[] leftStr, boolean[] flagArray) {
        this.leftStr = leftStr;
        this.context = context;
        this.flagArray = flagArray;
    }

    @Override
    public int getCount() {
        return leftStr.length;
    }

    @Override
    public Object getItem(int arg0) {
        return leftStr[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        Holder holder = null;
        if (arg1 == null) {
            holder = new Holder();
            arg1 = LayoutInflater.from(context).inflate(R.layout.order_panel_left_list, null);
            holder.left_list_item = (TextView) arg1.findViewById(R.id.left_list_item);
            arg1.setTag(holder);
        } else {
            holder = (Holder) arg1.getTag();
        }
        holder.updataView(arg0);
        return arg1;
    }

    private class Holder {
        private TextView left_list_item;

        public void updataView(final int position) {
            left_list_item.setText(leftStr[position]);
            float density = left_list_item.getContext().getResources().getDisplayMetrics().density;

            if (flagArray[position]) {
                // 选中状态 - 设置背景颜色和文字颜色
                left_list_item.setBackgroundResource(R.drawable.mendian_diandian_left_xuanzhong);
                left_list_item.setTextScaleX(1.1f);
                // 添加选中时的高度变化
                ViewGroup.LayoutParams params = left_list_item.getLayoutParams();
                params.height = (int) (90 * density); // 60dp
                left_list_item.setLayoutParams(params);

                // 设置图标并调整大小
                Drawable drawable = ContextCompat.getDrawable(left_list_item.getContext(), R.drawable.mendian_diandan_lfimg);
                if (drawable != null) {
                    int iconSize = (int) (20 * density); // 设置图标大小为20dp
                    drawable.setBounds(0, 0, iconSize, iconSize);
                    left_list_item.setCompoundDrawables(null,  drawable,null, null);
                }

                left_list_item.setCompoundDrawablePadding((int) (8 * density));
                left_list_item.setTextColor(Color.parseColor("#FFFFFF")); // 选中文字颜色
            } else {
                // 未选中状态 - 设置背景颜色和文字颜色
                left_list_item.setBackgroundColor(Color.TRANSPARENT); // 默认背景
                left_list_item.setTextColor(Color.parseColor("#4C4C4C")); // 默认文字颜色
                // 清除图标
                left_list_item.setCompoundDrawables(null, null, null, null);

                // 恢复默认高度
                ViewGroup.LayoutParams params = left_list_item.getLayoutParams();
                params.height = (int) (60 * density);  // 默认高度，你可以根据需要调整
                left_list_item.setLayoutParams(params);
            }
        }

    }
}