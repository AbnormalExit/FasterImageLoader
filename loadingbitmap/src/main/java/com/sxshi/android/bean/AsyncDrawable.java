package com.sxshi.android.bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.sxshi.android.task.LoadImageResourceTask;

import java.lang.ref.WeakReference;


/**
 * Created by sxshi on 2018-2-9.
 * 这个类为了处理{@link LoadImageResourceTask}的并发问题，加载一张图片的时候设置一个默认加载图片
 */

public class AsyncDrawable extends BitmapDrawable {

    private final WeakReference<LoadImageResourceTask> loadBigBitmapTaskWeakReference;

    public AsyncDrawable(Resources res, Bitmap bgBitmap, LoadImageResourceTask task) {
        super(res, bgBitmap);
        loadBigBitmapTaskWeakReference = new WeakReference<LoadImageResourceTask>(task);
    }

    public LoadImageResourceTask getLoadBigBitmapTask() {
        return loadBigBitmapTaskWeakReference.get();
    }
}
