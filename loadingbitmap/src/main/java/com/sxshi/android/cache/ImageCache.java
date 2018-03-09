package com.sxshi.android.cache;

import android.graphics.Bitmap;

/**
 * Created by sxshi on 2018-2-9.
 */

public interface ImageCache<T> {

    /**
     * get bitmap from memory or disk
     *
     * @param url
     * @return
     */
    T get(String url);

    /**
     * add bitmap to memory or disk
     *
     * @param url
     * @param t
     */
    void put(String url, T t);

    /**
     * remove bitmap with key
     *
     * @param url
     */
    void remove(String url);
}
