package com.sxshi.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sxshi.android.bean.AsyncDrawable;
import com.sxshi.android.bean.DownLoadedDrawable;
import com.sxshi.android.cache.ImageCache;
import com.sxshi.android.cache.MemoryCache;
import com.sxshi.android.cache.DiskCache;
import com.sxshi.android.cache.DoubleCache;
import com.sxshi.android.task.DownloadBitmapTask;
import com.sxshi.android.task.LoadBigBitmapTask;
import com.sxshi.android.utils.BitmapDecodeUtils;

/**
 * Created by sxshi on 2018-2-9.
 */

public class ImageLoader {
    private static ImageLoader mInstance;
    private Context mContext;
    /**
     * Loading background image id
     */
    private int loadingBgResId = R.drawable.loding;

    private ImageCache mImageCache = new MemoryCache();//默认使用内存缓存

    /**
     * <p>
     * Cache can be customized configuration, configurable cache as follows<br/>
     * {@link MemoryCache}<br/>
     * {@link DiskCache}<br/>
     * {@link DoubleCache}
     * </p>
     *
     * @param imageCache
     */
    public ImageLoader setImageCache(ImageCache imageCache) {
        this.mImageCache = imageCache;
        return this;
    }

    private ImageLoader(Context context, Builder builder) {
        // TODO: 2018-2-9 架构需要重构，能够支持自定义配置
        this.mContext = context.getApplicationContext();
    }

    private ImageLoader(Context context) {
        this.mContext = context.getApplicationContext();

    }

    public static ImageLoader getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * load image form url
     *
     * @param url
     * @param imageView
     */
    public void loadImage(String url, ImageView imageView) {
        Bitmap bitmap = null;
        if (mImageCache != null) {
            bitmap = mImageCache.get(url);
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        } else {
            if (DownloadBitmapTask.cancelPotentialDownload(url, imageView)) {
                DownloadBitmapTask task = new DownloadBitmapTask(imageView, mImageCache);
                final DownLoadedDrawable downLoadedDrawable = new DownLoadedDrawable(mContext.getResources(),
                        BitmapDecodeUtils.decodeSampledBitmapFromResource(mContext.getResources(), loadingBgResId), task);
                imageView.setImageDrawable(downLoadedDrawable);
                task.execute(url);
            }
        }
    }

    public void loadBitmap(int resId, ImageView imageView) {
        if (LoadBigBitmapTask.cancelPotentialWork(resId, imageView)) {
            final LoadBigBitmapTask task = new LoadBigBitmapTask(imageView, mContext.getResources());
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(mContext.getResources(), BitmapDecodeUtils.decodeSampledBitmapFromResource(mContext.getResources(),
                            loadingBgResId), task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }


    /**
     * Imageloader config
     */
    public static class Builder {
        private int loadingBitmapResId;

        public int getLoadingBitmapResId() {
            return loadingBitmapResId;
        }

        public Builder setLoadingBitmapResId(int loadingBitmapResId) {
            this.loadingBitmapResId = loadingBitmapResId;
            return this;
        }

        public Builder build() {
            return new Builder();
        }
    }
}
