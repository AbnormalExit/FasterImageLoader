package com.sxshi.android.task;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.sxshi.android.bean.AsyncDrawable;
import com.sxshi.android.utils.BitmapDecodeUtils;

import java.lang.ref.WeakReference;

/**
 * Created by sxshi on 2018-2-9.
 * This task is used to load the big picture
 */

public class LoadBigBitmapTask extends AsyncTask<Integer, Void, Bitmap> {
    private static final String TAG = "LoadBigBitmapTask";

    private final WeakReference<ImageView> imageViewReference;

    private Resources mResources;

    private int resId;

    public int getResId() {
        return resId;
    }

    public LoadBigBitmapTask(ImageView imageView, Resources resources) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.mResources = resources;
    }

    @Override
    protected Bitmap doInBackground(Integer... integers) {
        resId = integers[0];
        // TODO: 2018-2-9 这里是缩放了大小 ，其实需要做一个等大小压缩
//        return BitmapDecodeUtils.decodeSampledBitmapFromResource(mResources, resId, 200, 200);
        return BitmapDecodeUtils.decodeSampledBitmapFromResource(mResources, resId);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            LoadBigBitmapTask loadBigBitmapTask = getLoadBitmapWorkerTask(imageView);
            if (imageView != null && this == loadBigBitmapTask) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * cancel load big bitmap from resource
     *
     * @param resId
     * @param imageView
     * @return
     */
    public static boolean cancelPotentialWork(int resId, ImageView imageView) {
        final LoadBigBitmapTask loadBigBitmapTask = getLoadBitmapWorkerTask(imageView);

        if (loadBigBitmapTask != null) {
            final int bitmapData = loadBigBitmapTask.getResId();
            if (bitmapData == 0 || bitmapData != resId) {
                // Cancel previous task
                loadBigBitmapTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    /**
     * get loading big imag task
     *
     * @param imageView
     * @return
     */
    private static LoadBigBitmapTask getLoadBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getLoadBigBitmapTask();
            }
        }
        return null;
    }
}
