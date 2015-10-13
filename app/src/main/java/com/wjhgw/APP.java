package com.wjhgw;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

import com.lidroid.xutils.HttpUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by Administrator on 2015/9/16 0016.
 */
public class APP extends Application {

    private HttpUtils httpUtils;
    private ImageLoaderConfiguration configuration;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    private static APP application;

    // 应用启动时调用
    @Override
    public void onCreate() {
        super.onCreate();

        // 指向当前的application
        application = this;

        // 初始化xUtils
        initXUtils();

        initUniversalImageLoader();


    }

    private void initXUtils() {
        /**
         * httpUtils初始化
         */
        httpUtils = new HttpUtils("utf-8");

        // 设置HttpUtils线程池中线程数量
        httpUtils.configRequestThreadPoolSize(5);

        // 设置请求重试次数
        httpUtils.configRequestRetryCount(3);

        // 设置响应的编码
        httpUtils.configResponseTextCharset("utf-8");

        // 设置请求超时时间
        httpUtils.configSoTimeout(30000);

    }

    private void initUniversalImageLoader() {
        // 创建ImageLoader,使用getInstance()
        mImageLoader = ImageLoader.getInstance();

        /**
         * BitmapUtils初始化
         */
        // 获取SDCard目录路径
        String diskCachePath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            diskCachePath = new StringBuffer()
                    .append(Environment.getExternalStorageDirectory())
                    .append(File.separator)
                    .append("wjhgw/imageCache").toString();
            // /mnt/sdcard/wjhgw/imageCache
        } else {
            diskCachePath = new StringBuffer().append(Environment.getDataDirectory())
                    .append(File.separator)
                    .append(this.getPackageName())
                    .append("wjhgw/imageCache").toString();
            // /data/data/com.wjhgw/wjhgw/imageCache
        }


        // 设置内存缓存
        int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;

        configuration =
                new ImageLoaderConfiguration
                        .Builder(getApplicationContext())
                        .threadPoolSize(5) // 设置线程池线程大小
                                // 磁盘缓存位置
                        .diskCache(new UnlimitedDiskCache(new File(diskCachePath)))
                        .diskCacheSize(60 * 1024 * 1024) // 磁盘缓存的大小 60M
                        .diskCacheFileCount(200) // 磁盘缓存的文件数量
                                // 可以设置自定义内存缓存，并且自定义缓存大小
                                //.memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                        .memoryCacheSize(cacheSize) // 设置内存缓存大小
                        .build();


        // init configuration
        ImageLoader.getInstance().init(configuration);

        // 在全局设置图片显示选项
        options =
                new DisplayImageOptions.Builder()
                        .cacheInMemory(true) // 是否缓存到内存
                        .cacheOnDisk(true) // 是否缓存到磁盘
                        .showImageOnLoading(R.mipmap.ic_launcher) // 加载显示的图片
                        .showImageOnFail(R.mipmap.ic_launcher) // 加载失败显示的图片
                        .bitmapConfig(Bitmap.Config.ARGB_8888) // 设置图片样式 RGB_565 无效，4.0后无效
                        .build();
    }

    public static APP getApp() {
        return application;
    }

    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public DisplayImageOptions getImageOptions() {
        return options;
    }
}
