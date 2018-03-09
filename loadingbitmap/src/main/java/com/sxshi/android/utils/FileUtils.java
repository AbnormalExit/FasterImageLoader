package com.sxshi.android.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by sxshi on 2018-3-8.
 */

public class FileUtils {
    private FileUtils() {

    }

    /**
     * 保存Bitmap到指定文件
     *
     * @param tempBitmap
     * @param fileName
     */
    public static String saveBitmapToFile(Context context, Bitmap tempBitmap, String fileName) {
        BufferedOutputStream bos = null;
        File tempFile = null;
        try {
            tempFile = getTempFile(context.getApplicationContext(), fileName);
            bos = new BufferedOutputStream(new FileOutputStream(tempFile));
            // compressBitmap(tempBitmap).compress(Bitmap.CompressFormat.JPEG, 100, bos);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();
    }

    /**
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getTempFile(Context context, String uniqueName) {
        final String tempFilePath = getExternalTempDir(context).getPath();
        return new File(tempFilePath + File.separator + uniqueName);
    }

    /**
     * Get the external app temp directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalTempDir(Context context) {
        if (Utils.hasFroyo()) {
            return context.getExternalCacheDir();
        }
        // Before Froyo we need to construct the external cache dir ourselves
        final String tempDir = "/Android/data/" + context.getPackageName() + "/temp/";
        return new File(Environment.getExternalStorageDirectory().getPath() + tempDir);
    }


}
