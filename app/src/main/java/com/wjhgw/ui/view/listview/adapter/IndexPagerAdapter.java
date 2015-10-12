package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 首页ViewPager 适配器
 */
public class IndexPagerAdapter extends PagerAdapter {
    private List<ImageView> imageViews;
    private Context context;
    private int imageSize;

    public IndexPagerAdapter(Context context, List<ImageView> imageViews) {
        this.context = context;
        this.imageViews = imageViews;
        imageSize = imageViews.size();
    }

    @Override
    public int getCount() {

//        return imageViews.size();
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(imageViews.get(position % (imageSize)));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        ImageView image = imageViews.get(position % (imageSize));
        ((ViewPager) container).addView(image);
        return image;
    }
}
