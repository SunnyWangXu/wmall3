package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wjhgw.APP;
import com.wjhgw.business.bean.Home_Pager_Data;

import java.util.List;

/**
 * 首页ViewPager 适配器
 */
public class HomePagerAdapter extends PagerAdapter {
    private List<Home_Pager_Data> data;
    private Context context;
    private int imageSize;

    public HomePagerAdapter(Context context, List<Home_Pager_Data> data) {
        this.context = context;
        this.data = data;
        imageSize = data.size();
    }

    @Override
    public int getCount() {

        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//         container.removeView(imageViews.get(position % (imageSize)));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView image = new ImageView(context);
        String imageUrl = data.get(position % imageSize).adv_pic;
        APP.getApp().getImageLoader().displayImage(imageUrl, image, APP.getApp().getImageOptions());
        container.addView(image);
        return image;
    }
}
