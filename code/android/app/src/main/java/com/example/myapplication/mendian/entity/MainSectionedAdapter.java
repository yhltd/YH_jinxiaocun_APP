package com.example.myapplication.mendian.entity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.mendian.activity.KehuinfoChangeActivity;
import com.example.myapplication.mendian.activity.ProductInsActivity;
import com.lxj.xpopup.core.BasePopupView;

/**
 * 基本功能：右侧Adapter
 */
public class MainSectionedAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private String[] leftStr;
    private YhMendianProductshezhi[][] rightStr;
    BasePopupView popupView;

    public MainSectionedAdapter(Context context, String[] leftStr, YhMendianProductshezhi[][] rightStr) {
        this.mContext = context;
        this.leftStr = leftStr;
        this.rightStr = rightStr;
    }

    @Override
    public Object getItem(int section, int position) {
        return rightStr[section][position];
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr.length;
    }

    @Override
    public int getCountForSection(int section) {
        return rightStr[section].length;
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        RelativeLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) inflator.inflate(R.layout.order_panel_right_list, null);
        } else {
            layout = (RelativeLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.name)).setText(rightStr[section][position].getProduct_name());
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String[] specifications_list = rightStr[section][position].getSpecifications().split(",");
                String[] practice_list = rightStr[section][position].getPractice().split(",");
                String[] price_list = rightStr[section][position].getPrice().split(",");
                System.out.println(specifications_list);
                System.out.println(practice_list);
                System.out.println(price_list);
                Toast.makeText(mContext, rightStr[section][position].getProduct_name(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ProductInsActivity.class);
                intent.putExtra("cplx", rightStr[section][position].getType());
                intent.putExtra("cpmc", rightStr[section][position].getProduct_name());
                intent.putExtra("dw", rightStr[section][position].getUnit());
                intent.putExtra("dj", rightStr[section][position].getPrice());
                intent.putExtra("guige", rightStr[section][position].getSpecifications());
                intent.putExtra("kouwei", rightStr[section][position].getPractice());
                mContext.startActivity(intent);
            }
        });
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
            // 设置悬浮栏背景颜色
            layout.setBackgroundResource(R.drawable.mendian_diandan_xuanfubg);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        // 设置文字颜色

        ((TextView) layout.findViewById(R.id.textItem)).setTextColor(Color.WHITE);
        return layout;
    }

}