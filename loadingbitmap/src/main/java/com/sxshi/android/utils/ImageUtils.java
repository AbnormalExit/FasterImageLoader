package com.sxshi.android.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by sxshi on 2018-2-8.
 */

public class ImageUtils {
    private static final String TAG = "ImageUtils";
    private static final String UNKNOW_IMAGE_TYPE = "unkonw image type";

    private ImageUtils() {
    }

    /**
     * calc inSampleSize
     *
     * @param options   BitmapFactory.Options
     * @param reqWidth  image width
     * @param reqHeight image height
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * decode bitmap from resoucre with custom size
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * decode bimap from resoucre with the same size
     *
     * @param res
     * @param resId
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        return bitmap;

    }

    /**
     * decode bitmap from inputstream with custom size
     *
     * @param inputStream
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampleBitmapFromInputStream(FileInputStream inputStream, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] bytes = null;
        try {
            bytes = readInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bytes != null) {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }
        return null;
    }

    /**
     * decode bitmp from IntutStrem with the same size
     *
     * @param inputStream
     * @return
     */
    public static Bitmap decodeSampleBitmapFromInputStream(InputStream inputStream) {
        Bitmap bitmap = null;
        FlushedInputStream flushedInputStream = new FlushedInputStream(inputStream);
        byte[] bytes = null;
        try {
            bytes = readInputStream(flushedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bytes != null) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        return bitmap;
    }

    /**
     * get bytes from inputStream
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inputStream.close();
        return outStream.toByteArray();
    }

    /**
     * bitmp convert to byte[]
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();
        return datas;
    }

    /**
     * bytes convert to bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytesToBitmap(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * 得到资源文件中图片的Uri
     *
     * @param context 上下文对象
     * @param id      资源id
     * @return Uri
     */
    public static Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getApplicationContext().getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        Log.d(TAG, "path: " + path);
        return Uri.parse(path);
    }


    /**
     * get drawable resource filetype
     *
     * @param resourceId
     * @return
     */
    public static String getDrawableResourceType(Resources res, int resourceId) {


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resourceId, options);
        String type = options.outMimeType;
        if (TextUtils.isEmpty(type)) {
            type = UNKNOW_IMAGE_TYPE;
        } else {
            type = type.split("/")[1];
        }
        return type;
    }

    /**
     * get drawable file  filetype
     *
     * @param filePath
     * @return
     */
    public static String getDrawableFileType(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        String type = options.outMimeType;//image/png”、”image/jpeg”、”image/gif”…….
        if (TextUtils.isEmpty(type)) {
            type = UNKNOW_IMAGE_TYPE;
        } else {
            type = type.split("/")[1];
        }
        return type;
    }

}
