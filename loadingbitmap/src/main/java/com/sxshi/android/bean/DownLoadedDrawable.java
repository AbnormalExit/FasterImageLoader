package com.sxshi.android.bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.sxshi.android.task.LoadImageUrlTask;

import java.lang.ref.WeakReference;

/**
 * Created by sxshi on 2018-2-9.
 * 这个类为了处理{@link LoadImageUrlTask}的并发问题，加载一张图片的时候设置一个默认加载色
 */

public class DownLoadedDrawable extends BitmapDrawable {
    private final WeakReference<LoadImageUrlTask> bitmapDownloaderTaskReference;

    public DownLoadedDrawable(Resources res, Bitmap bgBitmap, LoadImageUrlTask loadImageUrlTask) {
        super(res, bgBitmap);
        bitmapDownloaderTaskReference =
                new WeakReference<LoadImageUrlTask>(loadImageUrlTask);
    }

    public LoadImageUrlTask getBitmapDownloaderTask() {
        return bitmapDownloaderTaskReference.get();
    }
}
