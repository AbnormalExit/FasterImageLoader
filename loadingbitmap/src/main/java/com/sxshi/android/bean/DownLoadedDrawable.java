package com.sxshi.android.bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.sxshi.android.task.DownloadBitmapTask;

import java.lang.ref.WeakReference;

/**
 * Created by sxshi on 2018-2-9.
 * 这个类为了处理{@link DownloadBitmapTask}的并发问题，加载一张图片的时候设置一个默认加载色
 */

public class DownLoadedDrawable extends BitmapDrawable {
    private final WeakReference<DownloadBitmapTask> bitmapDownloaderTaskReference;

    public DownLoadedDrawable(Resources res, Bitmap bgBitmap, DownloadBitmapTask downloadBitmapTask) {
        super(res, bgBitmap);
        bitmapDownloaderTaskReference =
                new WeakReference<DownloadBitmapTask>(downloadBitmapTask);
    }

    public DownloadBitmapTask getBitmapDownloaderTask() {
        return bitmapDownloaderTaskReference.get();
    }
}
