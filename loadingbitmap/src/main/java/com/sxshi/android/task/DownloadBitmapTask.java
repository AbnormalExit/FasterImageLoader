package com.sxshi.android.task;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.sxshi.android.bean.DownLoadedDrawable;
import com.sxshi.android.cache.ImageCache;
import com.sxshi.android.utils.BitmapDecodeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sxshi on 2018-2-8.
 */

public class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "DownloadBitmapTask";
    private String url;

    public String getUrl() {
        return url;
    }

    private final WeakReference<ImageView> imageViewReference;

    private ImageCache mImageCache;

    public DownloadBitmapTask(ImageView imageView, ImageCache imageCache) {
        this.imageViewReference = new WeakReference<ImageView>(imageView);
        this.mImageCache = imageCache;
        Log.d(TAG, "DownloadBitmapTask: init download task");
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        url = params[0];
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = downloadBitmap(params[0]);
        if (mImageCache != null && !TextUtils.isEmpty(url)) {
            mImageCache.put(url, bitmap);
        }
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            DownloadBitmapTask downloadBitmapTask = getBitmapDownloaderTask(imageView);
            if (imageView != null && this == downloadBitmapTask) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * cancel download task
     *
     * @param url
     * @param imageView
     * @return
     */
    public static boolean cancelPotentialDownload(String url, ImageView imageView) {
        DownloadBitmapTask downloadBitmapTask = getBitmapDownloaderTask(imageView);

        if (downloadBitmapTask != null) {
            String bitmapUrl = downloadBitmapTask.getUrl();
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                downloadBitmapTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }


    /**
     * Get the current Image View download task
     *
     * @param imageView
     * @return
     */
    public static DownloadBitmapTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownLoadedDrawable) {
                DownLoadedDrawable downloadedDrawable = (DownLoadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    /**
     * download iname from inageUrl
     *
     * @param imageUrl
     * @return
     */
    private Bitmap downloadBitmap(String imageUrl) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        URL url = null;
        try {
            url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "download fail ,http code: " + connection.getResponseCode());
                return null;
            }
            inputStream = connection.getInputStream();
            bitmap = BitmapDecodeUtils.decodeSampleBitmapFromInputStream(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}