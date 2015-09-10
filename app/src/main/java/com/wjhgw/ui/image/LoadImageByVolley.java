package com.wjhgw.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.wjhgw.R;

/**
 * 利用Volley异步加载图片
 *
 * 注意方法参数:
 * getImageListener(ImageView view, int defaultImageResId, int errorImageResId)
 * 第一个参数:显示图片的ImageView
 * 第二个参数:默认显示的图片资源
 * 第三个参数:加载错误时显示的图片资源
 */
public class LoadImageByVolley {

    private Context mContext;
    private ImageView mImageView;

    public LoadImageByVolley(Context context,ImageView view) {
        mContext = context;
        mImageView = view;
    }

    public void loadImageByVolley(String imageUrl){
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final LruCache<String, Bitmap> lruCache = new LruCache<>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView, R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        imageLoader.get(imageUrl, listener);
    }
}
