package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wjhgw.R;

/**
 * 首页ViewPager 适配器
 */
public class IndexPagerAdapter extends PagerAdapter {
    private Context context;
    private int[] arrID = {R.mipmap.ic_1, R.mipmap.ic_2, R.mipmap.ic_3, R.mipmap.ic_4};

    public IndexPagerAdapter(Context context) {
        this.context =context;
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
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView image = new ImageView(context);
        image.setImageResource(arrID[position % 4]);
        container.addView(image);
        return image;
    }
}
