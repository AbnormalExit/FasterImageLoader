package com.sxshi.android.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by sxshi on 2018-2-12.
 */

public class DoubleCache implements ImageCache<Bitmap> {
    private static final String TAG = "DoubleCache";
    private DiskCache diskCache;
    private MemoryCache memoryCache;

    private Context mContext;

    public DoubleCache(Context mContext) {
        Log.d(TAG, "DoubleCache: init");
        this.mContext = mContext.getApplicationContext();
        this.diskCache = new DiskCache(mContext);
        this.memoryCache = new MemoryCache();
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap == null) {
            bitmap = diskCache.get(url);
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        memoryCache.put(url, bitmap);
        diskCache.put(url, bitmap);
    }

    @Override
    public void remove(String url) {
        memoryCache.remove(url);
        diskCache.remove(url);
    }
}
