package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wjhgw.APP;
import com.wjhgw.business.bean.Home_Pager_Data;
import com.wjhgw.ui.activity.W1_GeneralPrductDetailActivity;

import java.util.List;

/**
 * 首页ViewPager 适配器
 */
public class HomePagerAdapter extends PagerAdapter {
    private List<Home_Pager_Data> data;
    private Context context;
    private int imageSize;
    private String[] advUrl;

    public HomePagerAdapter(Context context, List<Home_Pager_Data> data) {
        this.context = context;
        this.data = data;
        imageSize = data.size();
        advUrl = new String[data.size()];
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
    public Object instantiateItem(final ViewGroup container, final int position) {

        ImageView image = new ImageView(context);

        image.setScaleType(ImageView.ScaleType.FIT_XY);
        String imageUrl = data.get(position % imageSize).adv_pic;
        advUrl[position % imageSize] = data.get(position % imageSize).adv_url;

        APP.getApp().getImageLoader().displayImage(imageUrl, image, APP.getApp().getImageOptions());


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = advUrl[position % imageSize].replace("&amp;", "&");

                Intent intent = new Intent();
                intent.setClass(context, W1_GeneralPrductDetailActivity.class);
                intent.putExtra("url", url);
                /**
                 * 判断是否是商品详情
                 */
                if (!url.contains("goods")) {
                    intent.putExtra("isDetail", "no");
                }
                context.startActivity(intent);

            }
        });
        container.addView(image);
        return image;
    }
}
