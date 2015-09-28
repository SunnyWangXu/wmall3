package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.R;

/**
 * Created by Administrator on 2015/9/24 0024.
 */
public class DiscountPagerAdapter extends PagerAdapter {
    private Context context;
    private int[] arrID = {R.mipmap.ic_1, R.mipmap.ic_2, R.mipmap.ic_3, R.mipmap.ic_4};

    public DiscountPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {

        return arrID.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object); // 将view 类型 的object熊容器中移除,根据key
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_vp_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_dis);
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        ImageView image = (ImageView) view.findViewById(R.id.vp_item_img);
        image.setImageResource(arrID[position % 4]);
        container.addView(view);
        return view;
    }
}
