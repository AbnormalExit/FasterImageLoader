package com.sxshi.android.cache;

import android.graphics.Bitmap;

/**
 * Created by sxshi on 2018-2-9.
 */

public interface ImageCache {

    /**
     * get bitmap from memory or disk
     *
     * @param url
     * @return
     */
    Bitmap get(String url);

    /**
     * add bitmap to memory or disk
     *
     * @param url
     * @param bitmap
     */
    void put(String url, Bitmap bitmap);

    /**
     * remove bitmap with key
     *
     * @param url
     */
    void remove(String url);
}
