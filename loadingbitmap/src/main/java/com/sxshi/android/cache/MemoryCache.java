package com.sxshi.android.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.sxshi.android.ImageLoader;

/**
 * Created by sxshi on 2018-2-9.
 */

public class MemoryCache implements ImageCache {
    private static final String TAG = "MemoryCache";
    private LruCache<String, Bitmap> mMemoryCache;

    // Default memory cache size in kilobytes
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB


    public MemoryCache() {
        Log.d(TAG, "MemoryCache: init");
        mMemoryCache = new LruCache<String, Bitmap>(DEFAULT_MEM_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        Log.d(TAG, "get: " + url);
        return mMemoryCache.get(url);
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        Log.d(TAG, "put: " + url);
        if (get(url) == null) {
            mMemoryCache.put(url, bitmap);
        }
    }
    @Override
    public void remove(String url) {
        if (get(url) != null)
            mMemoryCache.remove(url);
    }
}
